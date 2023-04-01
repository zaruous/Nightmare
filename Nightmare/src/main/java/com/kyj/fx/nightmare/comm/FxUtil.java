/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.frame
 *	작성일   : 2021. 11. 22.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.nightmare.comm;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.kyj.fx.nightmare.GargoyleBuilderInitializer;
import com.kyj.fx.nightmare.grid.AnnotationOptions;
import com.kyj.fx.nightmare.grid.IColumnMapper;
import com.kyj.fx.nightmare.grid.IOptions;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class FxUtil {

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
	 * @작성자 : KYJ (callakrsos@naver.com)
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
	 * @작성자 : KYJ (callakrsos@naver.com)
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
	 * @작성자 : KYJ (callakrsos@naver.com)
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
	 * @작성자 : KYJ (callakrsos@naver.com)
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
	 * @작성자 : KYJ (callakrsos@naver.com)
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
	 * @작성자 : KYJ (callakrsos@naver.com)
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
	 * @작성자 : KYJ (callakrsos@naver.com)
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
	 * @작성자 : KYJ (callakrsos@naver.com)
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
	 * @작성자 : KYJ (callakrsos@naver.com)
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
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 3.
	 * @param message
	 */
	public static void showStatusMessage(String message) {
		StageStore.getApp().showStatusMessage(message);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 7.
	 * @param <T>
	 * @param tv
	 * @param converter
	 */
	public static <T> void installClipboardKeyEvent(ListView<T> tv, StringConverter<T> converter) {
		FxListViewUtil.installClipboardKeyEvent(tv, converter);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 7.
	 * @param <T>
	 * @param tv
	 */
	public static <T> void installClipboardKeyEvent(ListView<T> tv) {
		FxListViewUtil.installClipboardKeyEvent(tv, null);
	}

	
}
