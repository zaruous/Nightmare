/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.comm
 *	작성일   : 2021. 11. 22.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.comm;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;

import org.apache.commons.lang3.SystemUtils;
import org.controlsfx.control.table.TableFilter;
import org.controlsfx.control.table.TableFilter.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.ui.grid.CommonsBaseGridView;
import com.kyj.fx.nightmare.ui.grid.IColumnMapper;
import com.kyj.fx.nightmare.ui.grid.IOptions;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.Clipboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.util.StringConverter;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class FxTableViewUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(FxTableViewUtil.class);

	private FxTableViewUtil() {
	}

	/**
	 * 테이블컬럼의 row에 해당하는 데이터가 무엇인지 정의한 값을 리턴.
	 *
	 * StringConverter를 이용한 TableCell인경우 정의된 StringConvert를 이용한 데이터를 Excel의
	 * Cell에 쓰고, StringConverter를 이용하지않는 UI의 TableCell의 경우 데이터셋에 바인드된 값을 사용하게됨.
	 *
	 * 작성된 API내에서 적절한 값이 아니라고 판단되는경우 Ovrride해서 재정의하도록한다.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 9.
	 * @param table
	 *            사용자 화면에 정의된 tableView
	 * @param column
	 *            사용자 화면에 정의된 tableColumn
	 * @param columnIndex
	 *            사용자 화면에 정의된 tableColumn의 인덱스
	 * @param rowIndex
	 *            사용자 화면에 정의된 tableCell의 인덱스
	 * @return Object 테이블셀에 정의된 데이터를 리턴할 값으로, 리턴해주는 값이 엑셀에 write된다.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object getValue(TableView<?> table, TableColumn<?, ?> column, int rowIndex) {

		Callback cellFactory = column.getCellFactory();
		if (cellFactory != null) {
			TableCell cell = (TableCell) cellFactory.call(column);

			if (cell != null) {
				StringConverter converter = null;
				if (cell instanceof TextFieldTableCell) {
					TextFieldTableCell txtCell = (TextFieldTableCell) cell;
					converter = txtCell.getConverter();
				} else if (cell instanceof ComboBoxTableCell) {
					ComboBoxTableCell txtCell = (ComboBoxTableCell) cell;
					converter = txtCell.getConverter();
				}
				/* else 기본값. */
				else {
					try {
						Method m = cell.getClass().getMethod("converterProperty");
						if (m != null) {
							Object object = m.invoke(cell);
							if (object != null && object instanceof ObjectProperty) {
								ObjectProperty<StringConverter> convert = (ObjectProperty<StringConverter>) object;
								converter = convert.get();
							}
						}
					} catch (Exception e) {
						// Nothing...
					}
				}

				if (converter != null) {
					Object cellData = column.getCellData(rowIndex);
					return converter.toString(cellData);
				}
			}
		}

		// 여기서 에러나는 케이스가 있는데 TableColumn에 cellFactory 구현을 안한경우 에러가 발생했음.
		return column.getCellData(rowIndex);
	}

	/**
	 *
	 * reference - getValueByConverter(TableView<?> table, TableColumn<?, ?>
	 * column, int rowIndex) <br/>
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 11. 28.
	 * @param table
	 * @param columnIndex
	 * @param rowIndex
	 * @return
	 */
	public static <T> Object getValue(TableView<?> table, int columnIndex, int rowIndex) {
		return getValue(table, table.getColumns().get(columnIndex), rowIndex);
	}

	/********************************
	 * 작성일 : 2016. 5. 12. 작성자 : KYJ
	 *
	 * 테이블뷰 클립보드 기능.
	 * 
	 * @Deprecated COPY 기능과 PASTE 기능을 분리. installCopyHandler 사용할것
	 * @param table
	 ********************************/
	@SuppressWarnings("rawtypes")
	@Deprecated
	public static void installCopyPasteHandler(TableView<?> table) {

		table.addEventHandler(KeyEvent.KEY_PRESSED, e -> {

			if (e.isConsumed())
				return;

			int type = -1;
			if (e.isControlDown() && e.getCode() == KeyCode.C) {
				if (e.isShiftDown()) {
					type = 2;
				} else {
					type = 1;
				}
			}

			if (type == -1)
				return;

			ObservableList<TablePosition> selectedCells = table.getSelectionModel().getSelectedCells();
			TablePosition tablePosition = selectedCells.get(0);
			TableColumn tableColumn = tablePosition.getTableColumn();
			int row = tablePosition.getRow();
			int col = table.getColumns().indexOf(tableColumn);

			switch (type) {
			case 1:
				StringBuilder sb = new StringBuilder();
				for (TablePosition cell : selectedCells) {
					// 행변경시
					if (row != cell.getRow()) {
						sb.append("\n");
						row++;
					}
					// 열 변경시
					else if (col != table.getColumns().indexOf(cell.getTableColumn())) {
						sb.append("\t");
					}
					Object cellData = cell.getTableColumn().getCellData(cell.getRow());
					sb.append(ValueUtil.decode(cellData, cellData, "").toString());
				}
				FxClipboardUtil.putString(sb.toString());
				e.consume();
				break;
			case 2:
				Object cellData = tableColumn.getCellData(row);
				FxClipboardUtil.putString(ValueUtil.decode(cellData, cellData, "").toString());
				e.consume();
				break;
			}

		});

	}

	@SuppressWarnings("rawtypes")
	public static void installCopyHandler(TableView<?> table) {

		table.addEventHandler(KeyEvent.KEY_PRESSED, e -> {

			if (e.isConsumed())
				return;
			
			int type = -1;
			if (e.isControlDown() && e.getCode() == KeyCode.C) {
				if (e.isShiftDown()) {
					type = 2;
				} else {
					type = 1;
				}
			}

			if (type == -1)
				return;

			TableViewSelectionModel<?> selectionModel = table.getSelectionModel();
			SelectionMode selectionMode = selectionModel.getSelectionMode();
			boolean cellSelectionEnabled = selectionModel.isCellSelectionEnabled();

			// switch (selectionMode) {
			// case SINGLE:
			//
			// break;
			// case MULTIPLE:
			//
			// break;
			// }

			if (!cellSelectionEnabled) {

				switch (selectionMode) {
				case SINGLE: {
					TableViewSelectionModel<?> selectionModel2 = table.getSelectionModel();
					Object selectedItem = selectionModel2.getSelectedItem();

					ObservableList<?> columns = table.getColumns();
					Optional<String> reduce = columns.stream().filter(ob -> ob instanceof TableColumn).map(obj -> (TableColumn) obj).map(

							tc -> {
								return tc.getCellData(selectedItem);
							}

					).filter(v -> v != null).map(v -> v.toString()).reduce((o1, o2) -> o1.toString().concat("\t").concat(o2.toString()));
					reduce.ifPresent(str -> {
						FxClipboardUtil.putString(str);
						e.consume();
					});
				}
					break;
				// 17.12.06 Multiple copy 적용.
				case MULTIPLE: {

					ObservableList<Integer> selectedIndices = table.getSelectionModel().getSelectedIndices();
					// ObservableList<?> selectedItems =
					// table.getSelectionModel().getSelectedItems();

					ObservableList<?> columns = table.getColumns();
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < selectedIndices.size(); i++) {
						final int row = selectedIndices.get(i);
						// Object data = selectedItmes.get(row);
						Optional<String> reduce = columns.stream().filter(ob -> ob instanceof TableColumn).map(obj -> (TableColumn) obj)

								.map(tc -> {
									Object displayText = getDisplayText(tc, row, null);
									return displayText == null ? "null" : ValueUtil.isEmpty(displayText) ? " " : displayText;
									// return displayText;
								}

								)
								// .filter(v -> v != null)
								.map(v -> v.toString()).reduce((o1, o2) -> o1.toString().concat("\t").concat(o2.toString()));

						reduce.ifPresent(str -> {
							sb.append(str).append("\n");
							e.consume();
						});
					}

					FxClipboardUtil.putString(sb.toString());
				}
					break;
				}

			} else if (cellSelectionEnabled) {
				ObservableList<TablePosition> selectedCells = selectionModel.getSelectedCells();
				TablePosition tablePosition = selectedCells.get(0);
				TableColumn tableColumn = tablePosition.getTableColumn();
				int row = tablePosition.getRow();
				int col = table.getColumns().indexOf(tableColumn);

				switch (type) {
				case 1:
					StringBuilder sb = new StringBuilder();
					for (TablePosition cell : selectedCells) {
						// 행변경시
						if (row != cell.getRow()) {
							sb.append("\n");
							row++;
						}
						// 열 변경시
						else if (col != table.getColumns().indexOf(cell.getTableColumn())) {
							sb.append("\t");
						}
						Object cellData = cell.getTableColumn().getCellData(cell.getRow());
						sb.append(ValueUtil.decode(cellData, cellData, "").toString());
					}
					FxClipboardUtil.putString(sb.toString());
					e.consume();
					break;
				case 2:
					Object cellData = tableColumn.getCellData(row);
					FxClipboardUtil.putString(ValueUtil.decode(cellData, cellData, "").toString());
					e.consume();
					break;
				}
			}

		});

	}

	public static <K> void installPasteClipboard(TableView<K> tb, K instance, String targetField) {
		try {
			Class<K> class1 = (Class<K>) instance.getClass();
			Field field = class1.getField(targetField);
			installPasteClipboard(tb, class1, field);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static <K> void installPasteClipboard(TableView<K> tb, Class<K> clazz, Field targetField) {
		tb.addEventHandler(KeyEvent.KEY_PRESSED, e -> {

			if (e.isControlDown() && e.getCode() == KeyCode.V) {
				Clipboard clip = Clipboard.getSystemClipboard();

				ObservableList<TableColumn<K, ?>> columns = tb.getColumns();
				if (columns.isEmpty())
					return;

				Object value = null;

				if (clip.hasFiles()) {
					value = clip.getFiles();
				} else if (clip.hasHtml()) {
					value = clip.getHtml();
				} else if (clip.hasImage()) {
					value = clip.getImage();
				} else if (clip.hasRtf()) {
					value = clip.getRtf();
				} else if (clip.hasString()) {
					value = clip.getString();
				} else if (clip.hasUrl()) {
					value = clip.getUrl();
				}

				if (value == null)
					return;

				try {

					K newInstance = clazz.newInstance();
					Field field = clazz.getField(targetField.getName());
					if (field != null) {
						if (!field.isAccessible())
							field.setAccessible(true);
						field.set(newInstance, value);
					}
					tb.getItems().add(newInstance);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

	}

	/**
	 * 테이블컬럼에서 화면에 보여주는 텍스트를 리턴한다.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 3. 31.
	 * @param tc
	 * @param row
	 * @param stringconverter
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object getDisplayText(TableColumn<?, ?> tc, int row, BiFunction<TableColumn<?, ?>, Object, Object> customDataConverter) {

		Callback cellFactory = tc.getCellFactory();
		// ObservableValue<?> cellObservableValue =
		// tc.getCellObservableValue(row);

		if (cellFactory != null /* && cellObservableValue != null */) {

			// Object value = cellObservableValue.getValue();

			Object call = cellFactory.call(tc /* value */);

			if (customDataConverter != null)
				return customDataConverter.apply(tc, call);

			if (call != null && call instanceof TableCell) {
				TableCell cell = (TableCell) call;
				StringConverter converter = null;
				if (cell instanceof TextFieldTableCell) {
					TextFieldTableCell txtCell = (TextFieldTableCell) cell;
					converter = txtCell.getConverter();
				}

				// else if (cell instanceof TextAreaTableCell) {
				// TextAreaTableCell txtCell = (TextAreaTableCell) cell;
				// converter = txtCell.getConverter();
				// }
				else if (cell instanceof ComboBoxTableCell) {
					ComboBoxTableCell txtCell = (ComboBoxTableCell) cell;
					converter = txtCell.getConverter();
				}

				// else if (cell instanceof HyperlinkTableCell) {
				// HyperlinkTableCell txtCell = (HyperlinkTableCell) cell;
				// converter = txtCell.getConverter();
				// }
				/* else 기본값. */
				else {
					try {
						Method m = cell.getClass().getMethod("converterProperty");
						if (m != null) {
							Object object = m.invoke(cell);
							if (object != null && object instanceof ObjectProperty) {
								ObjectProperty<StringConverter> convert = (ObjectProperty<StringConverter>) object;
								converter = convert.get();
							}
						}
					} catch (Exception e) {
						// Nothing...
						call = getValue(tc.getTableView(), tc, row);
						// Object item = cell.getItem();
						// call = item.toString();
					}
				}

				if (converter != null) {
					Object cellData = tc.getCellData(row);
					return converter.toString(cellData);
				}
			}

			/*
			 * 18.02.21 kyj. bug fix. call -> TableCell 이라는 Object가 리턴됨.
			 * 
			 */
			// return call;
		}

		return tc.getCellData(row);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 7. 17.
	 * @param baseModel
	 * @param view
	 * @param option
	 */
	public static <T extends AbstractDVO> void installCommonsTableView(Class<T> baseModel, TableView<T> view, IOptions option) {
		CommonsBaseGridView.install(baseModel, view, option);
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
	public static <T extends AbstractDVO> void installCommonsTableView(Class<T> baseModel, TableView<T> view, IColumnMapper<T> columnMapper, IOptions option) {
		CommonsBaseGridView.install(baseModel, view, columnMapper ,option);
	}
	
	private static final String FILE_OVERWIRTE_MESSAGE = "파일이 이미 존재합니다. 덮어씌우시겠습니까? ";

	public static class EasyMenuItem {
		/**
		 * @작성자 : KYJ
		 * @작성일 : 2017. 7. 18.
		 * @param target
		 * @return
		 */
		public static <T extends AbstractDVO> MenuItem createExcelExportMenuItem(TableView<T> target) {
			MenuItem miExportExcel = new MenuItem("Export Excel");

			miExportExcel.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {

					if (event.isConsumed())
						return;

					event.consume();
					Stage stage = (Stage) FxUtil.getWindow(target);
					if (stage == null)
						stage = StageStore.getPrimaryStage();

					File saveFile = DialogUtil.showFileSaveDialog(stage, option -> {
						option.setInitialFileName(DateUtil.getCurrentDateString(DateUtil.SYSTEM_DATEFORMAT_YYYYMMDDHHMMSS));
						option.getExtensionFilters().add(new ExtensionFilter("Excel files (*.xlsx)", "*.xlsx"));
						option.getExtensionFilters().add(new ExtensionFilter("Excel files (*.xls)", "*.xls"));
						option.getExtensionFilters().add(new ExtensionFilter("All files", "*.*"));
						option.setTitle("Save Excel");
						option.setInitialDirectory(new File(SystemUtils.USER_HOME));
					});

					if (saveFile == null) {
						return;
					}

					if (saveFile.exists()) {
						Optional<Pair<String, String>> showYesOrNoDialog = DialogUtil.showYesOrNoDialog("overwrite ?? ",
								FILE_OVERWIRTE_MESSAGE);
						showYesOrNoDialog.ifPresent(consume -> {
							String key = consume.getKey();
							String value = consume.getValue();

							if (!("RESULT".equals(key) && "Y".equals(value))) {
								return;
							}
						});

					}

					try {
						FxExcelUtil.createExcel(new IExcelScreenHandler<T>() {
						}, saveFile, Arrays.asList(target), true);
					} catch (Exception e1) {
						DialogUtil.showExceptionDailog(e1);
					}
				}
			});
			return miExportExcel;
		}
	}

	public static <T> TableFilter<T> installTableFilter(TableView<T> tb) {
		Builder<T> forTableView = TableFilter.forTableView(tb);
		return forTableView.apply();
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2018. 10. 22.
	 * @param tb
	 * @param namedColumn
	 */
	public static <T> void installKeywordMoveEvent(TableView<T> tb, TableColumn<T, ?> namedColumn) {

		tb.addEventHandler(KeyEvent.KEY_PRESSED, e -> {

			int selectedIndex = tb.getSelectionModel().getSelectedIndex();
			boolean flag = false;
			if (!(e.isControlDown() || e.isAltDown() || e.isShiftDown())) {
				if (e.getCode().isLetterKey()) {

					if (e.isConsumed())
						return;
					e.consume();

					String name = e.getCode().getName();
					int size = tb.getItems().size();

					int currentIdx = tb.getSelectionModel().getSelectedIndex();
					for (int i = selectedIndex; i < size; i++) {
						Object value = FxUtil.getValue(tb, namedColumn, i);
						if (value != null) {
							if (value.toString().toLowerCase().startsWith(name.toLowerCase())) {
								// LOGGER.debug("1-{}", value);

								if (currentIdx == i)
									continue;

								// } else {
								tb.getSelectionModel().clearSelection();
								tb.getSelectionModel().select(i);
								tb.scrollTo(i);
								// tb.getSelectionModel().clearAndSelect(i);

								// }
								//
								flag = true;
								break;
							}
						}
					}

					for (int i = 0; i < selectedIndex && !flag; i++) {
						Object value = FxUtil.getValue(tb, namedColumn, i);
						if (value != null) {
							if (value.toString().startsWith(name)) {
								// LOGGER.debug("2-{}", value);
								tb.getSelectionModel().select(i);
								tb.scrollTo(i);
								flag = true;
								break;
							}
						}
					}

				}
			}
		});
	}

}
