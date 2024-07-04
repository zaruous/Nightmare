/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.frame
 *	작성일   : 2021. 11. 22.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.comm;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.fxloader.FxLoader;
import com.kyj.fx.nightmare.GargoyleBuilderInitializer;
import com.kyj.fx.nightmare.comm.ValueUtil.IndexCaseTypes;
import com.kyj.fx.nightmare.ui.grid.AnnotationOptions;
import com.kyj.fx.nightmare.ui.grid.ColumnName;
import com.kyj.fx.nightmare.ui.grid.ColumnVisible;
import com.kyj.fx.nightmare.ui.grid.ColumnWidth;
import com.kyj.fx.nightmare.ui.grid.IColumnMapper;
import com.kyj.fx.nightmare.ui.grid.IOptions;
import com.kyj.fx.nightmare.ui.grid.NonEditable;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class FxUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(FxUtil.class);
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2020. 9. 25.
	 * @param <T>
	 * @param modifyList
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public static <T> TableView<T> createTableView(List<T> modifyList)
			throws NoSuchFieldException, SecurityException, NoSuchMethodException {
		T t = modifyList.get(0);
		TableView<T> tv = new TableView<T>();
		Class<T> clazz = (Class<T>) t.getClass();
		generateTableColumns(clazz, tv, "tc");
		tbAutoMapping(clazz, tv, "tc");
		tv.getItems().setAll(modifyList);
		return tv;
	}
	
	/**
	 * 번거로운 테이블 value 맵핑을 간편하게 하기 위해 만듬. <br/>
	 * ㅠ
	 * 
	 * 조건 : fxml작성된 id와 vo 필드에 xxproperty 명이 일치하는 대상이 조건이며 <br/>
	 * 
	 * 틀린경우는 로그를 출력. <r/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 1. 30.
	 * @param beanClass
	 * @param tb
	 * @param startPrefix
	 *            테이블 컬럼 preffix. <br/>
	 */
	public static <T> void tbAutoMapping(Class<T> beanClass, TableView<T> tb, String startPrefix) {
		tbAutoMapping(beanClass, tb, startPrefix, null);
	}
	/**
	 * 번거로운 테이블 value 맵핑을 간편하게 하기 위해 만듬. <br/>
	 * ㅠ
	 * 
	 * 조건 : fxml작성된 id와 vo 필드에 xxproperty 명이 일치하는 대상이 조건이며 <br/>
	 * 
	 * 틀린경우는 로그를 출력. <r/>
	 * 
	 * 2019.11.13 컬럼에 하위 항목도 존재하는 경우 대상에 추가하도록 수정.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 1. 31.
	 * @param beanClass
	 * @param tb
	 * @param startPrefix
	 * @param filter
	 *            자동으로 지정하지않을 컬럼이 존재한경우
	 */
	public static <T> void tbAutoMapping(Class<T> beanClass, TableView<T> tb, String startPrefix, Predicate<TableColumn<T, ?>> filter) {
		// ObservableList<TableColumn<T, ?>> columns = tb.getColumns();
		LinkedList<TableColumn<T, ?>> columns = new LinkedList<TableColumn<T, ?>>();
		columns.addAll(tb.getColumns());
		while (!columns.isEmpty()) {
			TableColumn<T, ?> col = columns.pop();

			ObservableList<TableColumn<T, ?>> sub = col.getColumns();
			if (sub != null && !sub.isEmpty())
				columns.addAll(sub);

			if (filter != null) {
				if (!filter.test(col)) {
					continue;
				}
			}

			col.setCellValueFactory(callback -> {

				String id = col.getId();
				if (id.startsWith(startPrefix)) {

					String beanProperyName = ValueUtil.getIndexLowercase(id.replace(startPrefix, ""), 0) + "Property";
					boolean existsPropertyMethod = false;

					try {
						Method declaredMethod = null;
						try {
							declaredMethod = beanClass.getDeclaredMethod(beanProperyName);
							existsPropertyMethod = true;
						} catch (NoSuchMethodException e) {
							/* Nothing */}

						if (!existsPropertyMethod) {
							String getterMethod = "get" + ValueUtil.getIndexcase(id.replace(startPrefix, ""), 0, IndexCaseTypes.UPPERCASE);
							try {
								declaredMethod = beanClass.getDeclaredMethod(getterMethod);
							} catch (NoSuchMethodException e) {
								/* Nothing */}
						}

						if (declaredMethod == null)
							throw new RuntimeException("no suitable method found ");

						if (declaredMethod != null) {
							if (!declaredMethod.isAccessible()) {
								declaredMethod.setAccessible(true);
							}
						}

						if (existsPropertyMethod) {
							T value = callback.getValue();
							Object invoke = declaredMethod.invoke(value);
							if (invoke instanceof ObservableValue) {
								return (ObservableValue) invoke;
							}
						}
						// method is null.
						else {
							T value = callback.getValue();
							Object invoke = declaredMethod.invoke(value);
							return (ObservableValue) new ReadOnlyStringWrapper(invoke == null ? "" : invoke.toString());
						}

					} catch (Exception e) {
						LOGGER.error(ValueUtil.toString("fail to bind property. ", e));
					}

				}
				return null;
			});

		}

	}
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2020. 9. 14.
	 * @param <T>
	 * @param beanClass
	 * @param tb
	 * @param startPrefix
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public static <T> void generateTableColumns(Class<T> beanClass, TableView<T> tb, String startPrefix)
			throws NoSuchFieldException, SecurityException, NoSuchMethodException {
		generateTableColumns(beanClass, tb, startPrefix, null);
	}
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2020. 9. 9.
	 * @param <T>
	 * @param beanClass
	 * @param tb
	 * @param startPrefix
	 * @param filter
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws NoSuchMethodException
	 */
	public static <T> void generateTableColumns(Class<T> beanClass, TableView<T> tb, String startPrefix,
			Predicate<TableColumn<T, ?>> filter) throws NoSuchFieldException, SecurityException, NoSuchMethodException {

		Field[] declaredFields = beanClass.getDeclaredFields();
		Method[] declaredMethods = beanClass.getDeclaredMethods();

		// HashBag fieldNames = new HashBag();
		List<String> fieldNames = new ArrayList<String>(declaredFields.length);
		List<String> methodNames = new ArrayList<String>(declaredMethods.length);

		// int columnIndex = 0;
		for (Field f : declaredFields) {
			fieldNames.add(f.getName());
		}

		for (Method f : declaredMethods) {
			methodNames.add(f.getName());
		}

		Iterator<String> it = fieldNames.iterator();
		while (it.hasNext()) {
			String fieldName = it.next();
			// Class<?> type = beanClass.getDeclaredField(fieldName).getType();
			// Method getterMethod = beanClass.getDeclaredMethod();

			if (!methodNames.contains("get" + ValueUtil.capitalize(fieldName)))
				continue;

			// Method setterMethod = beanClass.getDeclaredMethod("set" +
			// ValueUtil.capitalize(fieldName), Object.class);
			if (!methodNames.contains("set" + ValueUtil.capitalize(fieldName)))
				continue;

			String preffixName = startPrefix + ValueUtil.capitalize(fieldName);

			TableColumn<T, ?> tc = new TableColumn<>();
			tc.setId(preffixName);

			Field field = beanClass.getDeclaredField(fieldName);
			ColumnName cn = field.getAnnotation(ColumnName.class);
			if (cn != null) {
				if (ValueUtil.isNotEmpty(cn.messageId())) {
					String name = Message.getInstance().getMessage(cn.messageId());
					tc.setText(name);
				} else
					tc.setText(cn.value());
			} else
				tc.setText(fieldName);

			ColumnWidth cw = field.getAnnotation(ColumnWidth.class);
			if (cw != null) {
				tc.setPrefWidth(cw.value());
			}

			ColumnVisible cv = field.getAnnotation(ColumnVisible.class);
			if (cv != null)
				tc.setVisible(cv.value());

			NonEditable ne = field.getAnnotation(NonEditable.class);
			tc.setEditable(ne == null);

			if (filter != null) {
				if (!filter.test(tc))
					continue;
			}
			tb.getColumns().add(tc);
		}
	}
	/**
	 * background color 객체 성성후 리턴
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 19.
	 * @param fill
	 * @return
	 */
	public static Background getBackgroundColor(Paint fill) {
		return new Background(new BackgroundFill(fill, CornerRadii.EMPTY, Insets.EMPTY));
	}
	
	/********************************
	 * 작성일 : 2016. 8. 23. 작성자 : KYJ
	 *
	 * Node의 Window 객체를 리턴함.
	 *
	 * @param node
	 * @return
	 ********************************/
	public static Window getWindow(Node node) {
		return getWindow(node, () -> StageStore.getPrimaryStage());
	}

	public static Window getWindow(Node node, Supplier<Window> emptyThan) {
		if (node != null) {
			Scene scene = node.getScene();
			if (scene != null) {
				return scene.getWindow();
			}
		}

		if (emptyThan != null)
			return emptyThan.get();

		return null;
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 11. 22.
	 * @param <T>
	 * @param baseModel
	 * @param view
	 */
	public static <T extends AbstractDVO> void installCommonsTableView(Class<T> baseModel, TableView<T> view) {
		FxTableViewUtil.installCommonsTableView(baseModel, view, new AnnotationOptions<T>(baseModel) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.kyj.fx.commons.fx.controls.grid.AnnotationOptions#
			 * useCommonCheckBox()
			 */
			@Override
			public boolean useCommonCheckBox() {
				return false;
			}

		});
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 11. 22.
	 * @param <T>
	 * @param baseModel
	 * @param view
	 * @param option
	 */
	public static <T extends AbstractDVO> void installCommonsTableView(Class<T> baseModel, TableView<T> view, IOptions option) {
		FxTableViewUtil.installCommonsTableView(baseModel, view, option);
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 11. 30.
	 * @param <T>
	 * @param baseModel
	 * @param view
	 * @param columnMapper
	 * @param option
	 */
	public static <T extends AbstractDVO> void installCommonsTableView(Class<T> baseModel, TableView<T> view, IColumnMapper<T> columnMapper,
			IOptions option) {
		FxTableViewUtil.installCommonsTableView(baseModel, view, columnMapper, option);
	}

	public static Object getValue(TableColumn<?, ?> column, int rowIndex) {
		return FxTableViewUtil.getValue(column.getTableView(), column, rowIndex);
	}

	public static Object getValue(TableView<?> table, TableColumn<?, ?> column, int rowIndex) {
		return FxTableViewUtil.getValue(table, column, rowIndex);
	}


	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 12. 8. 
	 * @param scene
	 * @param option
	 * @return
	 */
	public static Stage createStageAndShowAndWait(Scene scene, Consumer<Stage> option) {
		Stage stage = craeteStage(scene, option);
		stage.showAndWait();
		return stage;
	}
	
	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 12. 8. 
	 * @param parent
	 * @param option
	 * @return
	 */
	public static Stage createStageAndShowAndWait(Parent parent, Consumer<Stage> option) {
		Stage stage = craeteStage(new Scene(parent), option);
		stage.showAndWait();
		return stage;
	}
	
	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 6. 23.
	 * @param parent
	 * @param option
	 */
	public static <P extends Parent> Stage createStageAndShow(P parent, Consumer<Stage> option) {

		if (parent == null)
			throw new RuntimeException("Parent cannot be null. ");

		Scene scene = null;
		boolean createNew = true;
		SingtonPopupStage annotation = null;
		try {
			annotation = parent.getClass().getAnnotation(SingtonPopupStage.class);
		} catch (Exception ex) {
			/* Nothing */ }

		if (annotation != null) {
			boolean singleton = annotation.singleton();
			if (singleton) {
				Scene checkScene = parent.getScene();
				if (checkScene != null) {
					createNew = false;
				}
			}
		}

		if (createNew) {
			scene = new Scene(parent);
		} else
			scene = parent.getScene();

		if (annotation != null) {
			Stage window = (Stage) scene.getWindow();
			if (window != null) {
				window.show();
				return window;
			}
		}

		return createStageAndShow(scene, option);
	}

	/**
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 30.
	 * @param parent
	 * @param option
	 * @exception RuntimeException
	 *                Not Support Parent Type.
	 * @return
	 */
	public static Stage createStageAndShow(Object parent, Consumer<Stage> option) {
		if (parent == null)
			throw new RuntimeException("parent can not be null");

		if (parent instanceof Parent) {
			return createStageAndShow((Parent) parent, option);

		}

		throw new RuntimeException("Not Support Parent Type :  " + parent);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 6. 23.
	 */
	public static Stage createStageAndShow(Scene scene, Consumer<Stage> option) {
		Stage stage = craeteStage(scene, option);
		stage.show();
		return stage;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 4. 18.
	 * @param scene
	 * @param option
	 * @return
	 */
	public static Stage craeteStage(Scene scene, Consumer<Stage> option) {
		// Stage stage = new Stage();
		// stage.initOwner(StageStore.getPrimaryStage());
		// stage.setScene(scene);
		//
		// if (option != null)
		// option.accept(stage);

		Stage stage = craeteStage();
		stage.setScene(scene);

		if (option != null)
			option.accept(stage);

		return stage;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 12.
	 * @param option
	 * @return
	 */
	public static Stage craeteStage() {
		Stage stage = new Stage();
		stage.initOwner(StageStore.getPrimaryStage());
		return stage;
	}

	/**
	 * 테이블컬럼에서 화면에 보여주는 텍스트를 리턴한다.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 3. 31.
	 * @param tc
	 * @param row
	 * @return
	 */

	public static String getDisplayText(TableColumn<?, ?> tc, int row) {
		return FxTableViewUtil.getDisplayText(tc, row, null).toString();
	}

	/**
	 * @param tc
	 * @param row
	 * @param stringconverter
	 * @return
	 */
	public static String getDisplayText(TableColumn<?, ?> tc, int row, BiFunction<TableColumn<?, ?>, Object, Object> stringconverter) {
		Object displayText = FxTableViewUtil.getDisplayText(tc, row, stringconverter);
		return displayText == null ? "" : displayText.toString();
	}
	
	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 11. 25.
	 * @param tv
	 */
	public static void installClipboardKeyEvent(TreeView<?> tv) {
		// FxTableViewUtil.installCopyPasteHandler(tb);
		FxTreeViewClipboardUtil.installCopyPasteHandler(tv);
		// 2017.05.26 사용안함.
		// ClipboardKeyEventInstaller.install(tb);
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 11. 25.
	 * @param tb
	 */
	public static void installClipboardKeyEvent(TableView<?> tb) {
		// FxTableViewUtil.installCopyPasteHandler(tb);
		FxTableViewUtil.installCopyHandler(tb);
		// 2017.05.26 사용안함.
		// ClipboardKeyEventInstaller.install(tb);
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 11. 25.
	 * @param saveFile
	 * @param sheetName
	 * @param tableView
	 * @throws Exception
	 */
	// @SuppressWarnings({ "rawtypes" })
	public static <T extends AbstractDVO> boolean exportExcelFile(File saveFile, List<TableView<T>> tableViews) throws Exception {
		return FxExcelUtil.createExcel(new IExcelScreenHandler<T>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.kyj.fx.commons.excel.IExcelScreenHandler#toSheetName(
			 * javafx.scene.control.TableView)
			 */
			@Override
			public String toSheetName(TableView<T> table) {
				return "Sheet" + tableViews.indexOf(table);
			}

		}, saveFile, tableViews, true);

	}


	/**
	 * @return
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 11. 25.
	 */
	public static FXMLLoader newLaoder() {
		FXMLLoader loader = new FXMLLoader();
		loader.setCharset(StandardCharsets.UTF_8);
		ResourceBundle bundle = Message.getInstance().getBundle();
		loader.setResources(bundle);
		loader.setBuilderFactory(new GargoyleBuilderInitializer());
		
		return loader;
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 12. 3.
	 * @param message
	 */
	public static void showStatusMessage(String message) {
		StageStore.getApp().showStatusMessage(message);
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 12. 7.
	 * @param <T>
	 * @param tv
	 * @param converter
	 */
	public static <T> void installClipboardKeyEvent(ListView<T> tv, StringConverter<T> converter) {
		FxListViewUtil.installClipboardKeyEvent(tv, converter);
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 12. 7.
	 * @param <T>
	 * @param tv
	 */
	public static <T> void installClipboardKeyEvent(ListView<T> tv) {
		FxListViewUtil.installClipboardKeyEvent(tv, null);
	}

	/**
	 * @param <T>
	 * @param <C>
	 * @param controllerClass
	 * @param instance
	 * @param exHandler
	 * @return
	 */
	public static <T, C> T loadRoot(Class<C> controllerClass, Object instance, ExceptionHandler exHandler) {
		try {
			return FxLoader.load(controllerClass, instance, null, null);
		} catch (Exception e) {
			if(exHandler!=null)
				exHandler.handle(e);
			else
				LOGGER.error(ValueUtil.toString(e));
		}
		return null;
	}
	
	/********************************
	 * 작성일 : 2016. 5. 21. 작성자 : KYJ
	 *
	 * FXMLController에 정의된 내용을 기준으로 FXML을 로드한다.
	 *
	 * @param controllerClass
	 * @param instance
	 * @return
	 * @throws GargoyleException
	 * @throws NullPointerException
	 * @throws IOException
	 ********************************/
	public static <T, C> T loadRoot(Class<C> controllerClass, Object instance) throws Exception {
		return FxLoader.load(controllerClass, instance, null, null);
	}

	/********************************
	 * 작성일 : 2016. 5. 21. 작성자 : KYJ
	 *
	 * FXMLController에 정의된 내용을 기준으로 FXML을 로드한다.
	 *
	 * @param controllerClass
	 * @return
	 * @throws GargoyleException
	 * @throws NullPointerException
	 * @throws IOException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 ********************************/
	public static <T, C> T loadRoot(Class<C> controllerClass) throws Exception {
		return FxLoader.load(controllerClass, controllerClass.newInstance(), null, null);
	}

	/********************************
	 * 작성일 : 2016. 5. 28. 작성자 : KYJ
	 *
	 * ref loadRoot() method.
	 *
	 * 에러를 뱉지않고 핸들링할 수 있는 파라미터를 받음.
	 *
	 * @param controllerClass
	 * @param errorCallback
	 * @return
	 ********************************/
	private static <T, C> T loadRoot(Class<C> controllerClass, Consumer<Exception> errorCallback) {
		try {
			return FxLoader.load(controllerClass, controllerClass.newInstance(), null, null);
		} catch (Exception e) {
			errorCallback.accept(e);
		}
		return null;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 12.
	 * @param owner
	 * @param tb
	 */
	public static <T> void installFindKeyEvent(Window owner, TableView<T> tb) {
		installFindKeyEvent(owner, tb, null);
	}

	public static <T> void installFindKeyEvent(Window owner, TableView<T> tb,
			BiFunction<TableColumn<?, ?>, Object, Object> customConverter) {
		FxTableViewUtil.installFindKeyEvent(owner, tb, customConverter);
	}

	public static void installFindKeyEvent(Stage window, SpreadsheetView ssv) {
		FxSpreadViewUtil.installFindKeyEvent(window, ssv, null);
	}
	
	public static void installFindKeyEvent(Stage window, SpreadsheetView ssv, BiFunction<TableColumn<?, ?>, Object, Object> customConverter) {
		FxSpreadViewUtil.installFindKeyEvent(window, ssv, customConverter);
	}
}
