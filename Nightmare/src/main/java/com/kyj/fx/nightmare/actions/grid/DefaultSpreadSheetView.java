/**
 *
 */
package com.kyj.fx.nightmare.actions.grid;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.script.Bindings;
import javax.script.ScriptException;

import org.controlsfx.control.spreadsheet.Grid;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.GridChange;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetColumn;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.controlsfx.control.spreadsheet.SpreadsheetViewSelectionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.comm.FileUtil;
import com.kyj.fx.nightmare.comm.FxClipboardUtil;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.StageStore;
import com.kyj.fx.nightmare.comm.ValueUtil;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TablePosition;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * @author KYJ
 *
 */
public class DefaultSpreadSheetView extends StackPane {

	private SpreadsheetView ssv;
	private Label status;
	private static Logger LOGGER = LoggerFactory.getLogger(DefaultSpreadSheetView.class);

	private Stack<Command> undoStack = new Stack<>();
	private Stack<Command> redoStack = new Stack<>();

//	public DefaultSpreadSheetView() {
//		init();
//	}

	public DefaultSpreadSheetView(Grid grid) {

		ssv = new SpreadsheetView();
		this.ssv.gridProperty().addListener(new ChangeListener<Grid>() {

			@Override
			public void changed(ObservableValue<? extends Grid> observable, Grid oldValue, Grid newValue) {
				if (newValue == null)
					return;
				LOGGER.debug("new grid listener.");
				newValue.addEventHandler(GridChange.GRID_CHANGE_EVENT, gridChangeListener);
				newValue.addEventHandler(GridChange.GRID_CHANGE_EVENT, gridUnRedo);
			}
		});
//		undoStack.add(new GridCommand(ssv, ssv.getGrid(), grid));
		ssv.setGrid(grid);

		ObservableList<SpreadsheetColumn> columns = ssv.getColumns();
		columns.forEach(col -> {
			col.setPrefWidth(100d);
		});
		// ssv.setGrid(grid);
		init();

		Platform.runLater(() -> {
			Stage window = (Stage) this.getScene().getWindow();
			if (window == null)
				window = StageStore.getPrimaryStage();
			FxUtil.installFindKeyEvent(window, ssv);
		});

	}

	public SpreadsheetView getView() {
		return this.ssv;
	}

	public int getRowCount() {
		return ssv.getGrid().getRowCount();
	}

	public int getColumnCount() {
		return ssv.getGrid().getColumnCount();
	}

	/**
	 * @return
	 */
	public SpreadsheetViewSelectionModel getSelectionModel() {
		return getView().getSelectionModel();
	}

	public void init() {
		BorderPane root = new BorderPane();
		status = new Label();
		root.setCenter(ssv);
		root.setBottom(status);
		getChildren().add(root);

		this.addEventFilter(MouseEvent.ANY, event -> {
			status.textProperty().set(String.format(" x: %s y : %s", event.getX(), event.getY()));
		});

		this.addEventHandler(KeyEvent.KEY_PRESSED, this::spreadSheetKeyPress);

		ContextMenu contextMenu = this.ssv.getContextMenu();
		ObservableList<MenuItem> contextMenuItems = contextMenu.getItems();

		Menu mCode = new Menu("Code");
		MenuItem miCopyJavaArrayList = new MenuItem("Copy java ArrayList");
		miCopyJavaArrayList.setOnAction(this::miCopyJavaArrayListOnAction);
		mCode.getItems().add(miCopyJavaArrayList);

		Menu mCellType = new Menu("Cell Type");
		MenuItem miCellNumber = new MenuItem("Integer");
		miCellNumber.setOnAction(this::miCellTypeNumberOnAction);
		mCellType.getItems().add(miCellNumber);

		contextMenuItems.add(mCellType);

//		this.ssv.addEventHandler(GridChange.GRID_CHANGE_EVENT, gridChangeListener);
//		this.ssv.addEventHandler(GridChange.GRID_CHANGE_EVENT, gridUnRedo);

//		this.ssv.getGrid().addEventHandler(GridChange.GRID_CHANGE_EVENT, gridChangeListener);
//		this.ssv.getGrid().addEventHandler(GridChange.GRID_CHANGE_EVENT, gridUnRedo);
	}

	EventHandler<GridChange> gridUnRedo = new EventHandler<GridChange>() {
		@Override
		public void handle(GridChange ev) {
			Object oldValue = ev.getOldValue();
			Object newValue = ev.getNewValue();
			int row = ev.getRow();
			int column = ev.getColumn();
			Grid g = (Grid) ev.getSource();
			SpreadsheetCell cell = g.getRows().get(row).get(column);
			EditCommand command = new EditCommand(cell, oldValue, newValue);
			executeCommand(command);
			LOGGER.debug("{}", command);
		}
	};
	EventHandler<GridChange> gridChangeListener = new EventHandler<GridChange>() {
		@Override
		public void handle(GridChange ev) {
			int row = ev.getRow();
			int column = ev.getColumn();
			Grid g = (Grid) ev.getSource();
//			Object oldValue = ev.getOldValue();
			Object newValue = ev.getNewValue();
			if (newValue == null || !(newValue.toString().startsWith("=")))
				return;
			String script = newValue.toString().substring(1);

			com.kyj.fx.groovy.DefaultScriptEngine engine = new com.kyj.fx.groovy.DefaultScriptEngine();
			Bindings createBindings = engine.createBindings(new HashMap<String, Object>());

//			g.getRows().stream().flatMap(c -> c.stream()).forEach(c -> {
//				int row2 = c.getRow();
//				int column2 = c.getColumn();
//				char a = (char) (column2 + 65);
//				String exp = a + "" + row2;
//				createBindings.put(exp, c.getItem());
//			});

			String[] tokens = script.split("[\s,\\+,\\-,\\*,\\/]+");
			for (String token : tokens) {
				switch (token) {
				case "+", "=", "-", "*", "/":
					break;
				}

				String[] split = token.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
				if (split.length == 2) {
					int ch = split[0].toCharArray()[0] - 65;
					int num = Integer.parseInt(split[1], 10) - 1;

					SpreadsheetCell spreadsheetCell = g.getRows().get(num).get(ch);
					Object item = spreadsheetCell.getItem();
					createBindings.put(token, item);
				}

			}

			try {
				LOGGER.debug(script);
				Object ret = engine.execute(script, createBindings);
				g.setCellValue(row, column, ret);
			} catch (IOException | ScriptException e) {
				g.setCellValue(row, column, e.getMessage());
				e.printStackTrace();
			}

//			if(ev.getNewValue())

		}
	};
	@SuppressWarnings("rawtypes")
	public void miCellTypeNumberOnAction(ActionEvent e) {
		ObservableList<TablePosition> selectedCells = this.ssv.getSelectionModel().getSelectedCells();
		ObservableList<ObservableList<SpreadsheetCell>> items = this.ssv.getItems();

		selectedCells.forEach(pos -> {
			int row = pos.getRow();
			int column = pos.getColumn();

			Object item = items.get(row).get(column).getItem();
			try {
				int v = Integer.parseInt(item.toString());
				items.get(row).set(column, SpreadsheetCellType.INTEGER.createCell(row, column, 1, 1, v));
			} catch (Exception ex) {
				items.get(row).set(column, SpreadsheetCellType.INTEGER.createCell(row, column, 1, 1, 0));
			}
		});
	}

	/**
	 * 선택된 셀들을 코드형태로 바꿔서 클립보드에 복사한다<br/>
	 * 
	 * @param e
	 */
	@SuppressWarnings("rawtypes")
	public void miCopyJavaArrayListOnAction(ActionEvent e) {
		ObservableList<TablePosition> selectedCells = this.ssv.getSelectionModel().getSelectedCells();
//		ObservableList<ObservableList<SpreadsheetCell>> items = this.ssv.getItems();

		StringBuilder sb = new StringBuilder();
		sb.append("List a = new ArrayList();");
		for (TablePosition pot : selectedCells) {
			SpreadsheetCell cell = this.ssv.getItems().get(pot.getRow()).get(pot.getColumn());
			String text = cell.getText().trim();
			if (ValueUtil.isEmpty(text))
				continue;

			sb.append("a.add(").append("\"").append(text).append("\");");
		}
		FxClipboardUtil.putString(sb.toString());
	}
	
	@SuppressWarnings("rawtypes")
	public void spreadSheetKeyPress(KeyEvent e) {
		if (e.isControlDown() && e.getCode() == KeyCode.C) {

			StringBuffer clipboardContent = new StringBuffer();
			
			ObservableList<TablePosition> selectedCells = ssv.getSelectionModel().getSelectedCells();

			int prevRow = -1;

			for (TablePosition<?, ?> pos : selectedCells) {

				int currentRow = pos.getRow();
				int currentColumn = pos.getColumn();
				if ((prevRow != -1 && prevRow != currentRow)) {
					clipboardContent.setLength(clipboardContent.length() - 1);
					LOGGER.debug(clipboardContent.toString());
					/*
					 * 라인세퍼레이터 사용하지말것. 이유는 클립보드에 들어가는 컨텐츠가 /r/n이되면서 엑셀에 붙여넣기시 잘못된 값이 입력됨. [ 금지 :
					 * SystemUtils.LINE_SEPARATOR = /r/n ]
					 */
					clipboardContent.append("\n");
				}
				prevRow = currentRow;

				SpreadsheetCell spreadsheetCell = ssv.getGrid().getRows().get(currentRow).get(currentColumn);
				clipboardContent.append(spreadsheetCell.getText()).append("\t");

			}
			clipboardContent.setLength(clipboardContent.length() - 1);

			LOGGER.debug(String.format("clipboard content : \n%s", clipboardContent.toString()));
			FxClipboardUtil.putString(clipboardContent.toString());

			// 상위 이벤트가 호출되서 클립보드가 없어지는것을 방지한다.
			e.consume();
		} else if (e.isControlDown() && e.getCode() == KeyCode.V) {

			int type = FxClipboardUtil.getCipboardContentTypes();

			switch (type) {
			case FxClipboardUtil.IMAGE: {
				Image pastImage = FxClipboardUtil.pastImage();
				ObservableList<TablePosition> selectedCells = ssv.getSelectionModel().getSelectedCells();
				TablePosition tablePosition = selectedCells.get(0);

				int row = tablePosition.getRow();
				int column = tablePosition.getColumn();
				SpreadsheetCell cell = new ImageCellType().createCell(row, column, 1, 1, pastImage);

				ssv.getGrid().getRows().get(tablePosition.getRow()).set(tablePosition.getColumn(), cell);

			}

				break;

			case FxClipboardUtil.FILE:
				List<File> pastFiles = FxClipboardUtil.pasteFiles();
				if (pastFiles.size() == 1) {
					File file = pastFiles.get(0);
					if (file != null && file.exists()) {
						try {
							if (FileUtil.isImageFile(file)) {
								Image pastImage = new Image(file.toURI().toURL().openStream());
//								double height = pastImage.getHeight();
//								double width = pastImage.getWidth();
								ObservableList<TablePosition> selectedCells = ssv.getSelectionModel()
										.getSelectedCells();
								TablePosition tablePosition = selectedCells.get(0);

								int row = tablePosition.getRow();
								int column = tablePosition.getColumn();

								var imgcellType = new ImageCellType();
//								SpreadsheetCellEditor editor = imgcellType.createEditor(ssv);

								SpreadsheetCell cell = imgcellType.createCell(row, column, 1, 1, pastImage);
//								cell.setHasPopup(true);
//								cell.getPopupItems().add(new MenuItem("test"));
//								cell.getOptionsForEditor().add(editor);
								ObservableList<SpreadsheetCell> observableList = ssv.getGrid().getRows()
										.get(tablePosition.getRow());
								observableList.set(tablePosition.getColumn(), cell);

							}

						} catch (Exception e1) {
							LOGGER.error(ValueUtil.toString(e1));
						}
					}
				} else {
					ObservableList<TablePosition> selectedCells = ssv.getSelectionModel().getSelectedCells();
					TablePosition tablePosition = selectedCells.get(0);
					int row = tablePosition.getRow();
					int column = tablePosition.getColumn();
					for (File f : pastFiles) {
						String name = f.getName();
						ssv.getGrid().getRows().get(row++).get(column).setItem(name);
					}
				}
				break;
			case FxClipboardUtil.URL: {
				String pasteUrl = FxClipboardUtil.pasteUrl();
				Image pastImage = new Image(pasteUrl);
				
				ObservableList<TablePosition> selectedCells = ssv.getSelectionModel().getSelectedCells();
				TablePosition tablePosition = selectedCells.get(0);

				int row = tablePosition.getRow();
				int column = tablePosition.getColumn();
				SpreadsheetCell cell = new ImageCellType().createCell(row, column, 1, 1, pastImage);

				ssv.getGrid().getRows().get(tablePosition.getRow()).set(tablePosition.getColumn(), cell);

			}
				break;

			case FxClipboardUtil.STRING:
				paste();
				break;

			default:
				paste();
				break;
			}
		} else if (e.getCode() == KeyCode.Z && e.isControlDown()) {
			undo();
		} else if (e.getCode() == KeyCode.U && e.isControlDown()) {
			redo();
		}
//		e.consume();

	}

	/**
	 * 붙여넣기
	 */
	public void paste() {
		paste(FxClipboardUtil.pastString());
	}

	/**
	 * 붙여넣기
	 *
	 * @param pastString
	 */
	public void paste(String pastString) {
		TablePosition<?, ?> focusedCell = ssv.getSelectionModel().getFocusedCell();
		int row = focusedCell.getRow();
		int column = focusedCell.getColumn();
		paste(pastString, row, column);
	}

	/**
	 * 특수문자에대한 문자열 paste에 대한 버그를 수정하기 위한 함수.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 11. 23.
	 * @param items
	 * @param startRowIndex
	 * @param startColumnIndex
	 */
	public void paste(List<Map<String, Object>> items, int startRowIndex, int startColumnIndex) {
		int row = startRowIndex;
		int column = startColumnIndex;

		int _column = column;
		// String[] split = pastString.split("\n");

		Grid grid = ssv.getGrid();
		ObservableList<ObservableList<SpreadsheetCell>> rows = grid.getRows();

		for (Map<String, Object> str : items) {
			// String[] split2 = str.split("\t");
			_column = column;
			Iterator<String> iterator = str.keySet().iterator();
			while (iterator.hasNext()) {
				String strCol = iterator.next();
				Object value = str.get(strCol);
				SpreadsheetCell spreadsheetCell = null;

				if (rows.size() > row) {
					ObservableList<SpreadsheetCell> observableList = rows.get(row);

					try {
						spreadsheetCell = observableList.get(_column);
					} catch (IndexOutOfBoundsException e) {
						e.printStackTrace();
					}

				}

				/* 새로운 로우를 생성함. */
				else {

					ObservableList<SpreadsheetCell> newCells = createNewRow();
					spreadsheetCell = newCells.get(_column);
				}

				if (value != null)
					value = value.toString();

				spreadsheetCell.setItem(value);
				_column++;
			}
			row++;
		}

	}

	/**
	 * 붙여넣기
	 *
	 * @param pastString
	 */
	public void paste(final String pastString, final int startRowIndex, final int startColumnIndex) {
		int row = startRowIndex;
		int column = startColumnIndex;

		int _column = column;
		String[] split = pastString.split("\n");
		Grid grid = ssv.getGrid();
		ObservableList<ObservableList<SpreadsheetCell>> rows = grid.getRows();

		ArrayList<CellEdit> cellEdits = new ArrayList<>();

		for (String str : split) {
			String[] split2 = str.split("\t");
			_column = column;
			for (String str2 : split2) {
				SpreadsheetCell spreadsheetCell = null;
				if (rows.size() > row)
					spreadsheetCell = rows.get(row).get(_column);
				/* 새로운 로우를 생성함. */
				else {
					ObservableList<SpreadsheetCell> newCells = createNewRow();
					spreadsheetCell = newCells.get(_column);
					rows.add(newCells);
				}

				cellEdits.add(new CellEdit(spreadsheetCell, spreadsheetCell.getItem(), str2));

				spreadsheetCell.setItem(str2);
				_column++;
			}
			row++;
		}

		executeCommand(new RangeCommand(cellEdits));
//		ssv.setGrid(grid);
	}

	/**
	 * 새로운 Row를 생성한다.
	 *
	 * @param newRow 생성할 로우
	 * @return
	 */
	private ObservableList<SpreadsheetCell> createNewRow() {

		Grid grid = ssv.getGrid();
		ObservableList<ObservableList<SpreadsheetCell>> rows = grid.getRows();

		int columnCount = grid.getColumnCount();
		int newRow = rows.size();

		ObservableList<SpreadsheetCell> newCells = FXCollections.observableArrayList(new ArrayList<>(columnCount));

		for (int newCol = 0; newCol < columnCount; newCol++) {
			newCells.add(SpreadsheetCellType.STRING.createCell(newRow, newCol, 1, 1, ""));
		}

		return newCells;
	}

	public ObservableList<String> getColumnHeaders() {
		return ssv.getGrid().getColumnHeaders();
	}

	public ObservableList<ObservableList<SpreadsheetCell>> getRows() {
		return ssv.getGrid().getRows();
	}

	/**
	 * 시트의 컬럼 리턴
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 5. 12.
	 * @return
	 */
	public ObservableList<SpreadsheetColumn> getColumns() {
		return this.ssv.getColumns();
	}

	public void setColumnWidth(int index, int width) {
		SpreadsheetColumn spreadsheetColumn = getColumns().get(index);
		spreadsheetColumn.setPrefWidth(width);
	}

	public SpreadsheetColumn getColumn(SpreadsheetCell cell) {
		int column = cell.getColumn();
		return getColumns().get(column);
	}

	public SpreadsheetColumn getColumn(int index) {
		return getColumns().get(index);
	}

	private void undo() {
		if (!undoStack.isEmpty()) {
			LOGGER.debug("undo");
			Command command = undoStack.pop();
			command.undo();
			redoStack.push(command);
		}
	}

	private void redo() {
		if (!redoStack.isEmpty()) {
			LOGGER.debug("redo");
			Command command = redoStack.pop();
			command.execute();
			undoStack.push(command);
		}
	}

	private void executeCommand(Command command) {
		command.execute();
		undoStack.push(command);
		redoStack.clear();
	}

	public void setGrid(GridBase newValue) {
		Grid oldValue = ssv.getGrid();
		ssv.setGrid(newValue);
		executeCommand(new GridCommand(ssv, oldValue, newValue));
	}

	public ObservableList<ObservableList<SpreadsheetCell>> getItems() {
		return ssv.getItems();
	}
	

}
