/**
 * 
 */
package com.kyj.fx.nightmare.actions.grid;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;

import javax.imageio.ImageIO;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.fxloader.FXMLController;
import com.kyj.fx.nightmare.actions.ai.OpenAIService;
import com.kyj.fx.nightmare.comm.DialogUtil;
import com.kyj.fx.nightmare.comm.ExcelUtil;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.GargoyleExtensionFilters;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.ui.frame.AbstractCommonsApp;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Pair;

/**
 * 
 */
@FXMLController(value = "DefaultSpreadView.fxml", isSelfController = true)
public class DefaultSpreadComposite extends AbstractCommonsApp {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSpreadComposite.class);
	@FXML
	private TabPane tabPane;
	@FXML
	private Tab tabNew;
	@FXML
	private BorderPane borContent;

	private OpenAIService openAIService;
	private ObjectProperty<File> currentFile = new SimpleObjectProperty<File>();

	public DefaultSpreadComposite() {
		try {
			FxUtil.loadRoot(DefaultSpreadComposite.class, this);
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}

	@FXML
	public void miScreenshotOnAction() {

		Tab selectedItem = this.tabPane.getSelectionModel().getSelectedItem();
		DefaultSpreadItemComposite content = (DefaultSpreadItemComposite) selectedItem.getContent();

		SnapshotParameters snapshotParameters = new SnapshotParameters();
		snapshotParameters.setDepthBuffer(false);

		WritableImage snapshot = content.snapshot(snapshotParameters, null);
		BufferedImage fromFXImage = SwingFXUtils.fromFXImage(snapshot, null);
		File outputFile = new File("output.png");
		try {
			ImageIO.write(fromFXImage, "png", outputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void initialize() {
		try {
			this.openAIService = new OpenAIService();
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
		}
		Label value = new Label("+");
		value.setPrefWidth(15d);
		tabNew.setGraphic(value);
		value.addEventHandler(MouseEvent.MOUSE_RELEASED, ev -> {
			if (ev.getButton() == MouseButton.PRIMARY) {
//				if(ev.isConsumed())return;
//				ev.consume();
				addNewTabView("Sheet" + tabPane.getTabs().size());
			}
		});

		tabPane.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
			if (ev.getCode() == KeyCode.F2) {
				renameOnAction(new ActionEvent());
			}
		});

		addNewTabView("Sheet1");
	}

	Tab createNewTab(String title, Consumer<DefaultSpreadItemComposite> handler) {
		return createNewTab(title, 100, 100, handler);
	}

	Tab createNewTab(String title, int maxRow, int maxCol, Consumer<DefaultSpreadItemComposite> handler) {
		DefaultSpreadItemComposite value = new DefaultSpreadItemComposite(maxRow, maxCol);
		Tab e = new Tab(title, value);
		value.setAiService(openAIService);
		value.setParentComposite(this);
		value.setCurrentTab(e);

		MenuItem menuItem = new MenuItem("Rename");
		menuItem.setOnAction(this::renameOnAction);

		e.setContextMenu(new ContextMenu(menuItem));

		if (handler != null) {
			handler.accept(value);
		}
		return e;
	}

	/**
	 * @param e
	 */
	public void renameOnAction(ActionEvent e) {
		Tab tab = tabPane.getSelectionModel().getSelectedItem();
		Optional<Pair<String, String>> showInputDialog = DialogUtil.showInputDialog("Rename", "Rename", tab.getText());
		showInputDialog.ifPresent(pair -> {
			String newTabName = pair.getValue();
			if (!isTabNameValid(newTabName)) {
				return;
			}
			tab.setText(newTabName);
		});
	}

	public boolean isTabNameValid(String newTabName) {
		Optional<Tab> any = tabPane.getTabs().stream().filter(t -> ValueUtil.equals(t.getText(), newTabName)).findAny();
		if (any.isPresent()) {
			DialogUtil.showMessageDialog(String.format("중복된 이름이 존재합니다.[%s]", newTabName));
			// throw new TabNameAlreadyExistsException(String.format("중복된 이름이 존재합니다.[%s]",
			// newTabName));
			return false;
		}
		return true;
	}

	public Tab addNewTabView(String newTabName) {

		Tab newTab = createNewTab(newTabName, c -> {

		});
		if (!isTabNameValid(newTabName)) {
			return null;
		}
		tabPane.getTabs().add(newTab);

		tabPane.getSelectionModel().selectLast();
		return newTab;
	}

	/**
	 * @param handle
	 * @return
	 */
	public DefaultSpreadItemComposite getActive(Consumer<DefaultSpreadItemComposite> handle) {
		Tab selectedItem = this.tabPane.getSelectionModel().getSelectedItem();
		if (selectedItem != null)
			handle.accept((DefaultSpreadItemComposite) selectedItem.getContent());
		return (DefaultSpreadItemComposite) selectedItem.getContent();
	}

	@FXML
	public void miFileOpenOnAction() {

		ObservableList<Tab> tabs = tabPane.getTabs();

		Optional<Pair<String, String>> optional = DialogUtil.showYesOrNoDialog("Confirm", "데이터가 초기화 됩니다. 계속하시겠습니까?");
		if (!optional.isPresent()) {
			return;
		}
		if (!"Y".equals(optional.get().getValue()))
			return;

		File openFile = DialogUtil.showFileDialog(chooser -> {
			chooser.getExtensionFilters()
					.add(new ExtensionFilter(GargoyleExtensionFilters.XLSX_NAME, GargoyleExtensionFilters.XLSX));
		});
		if (openFile == null)
			return;

		
		
		tabPane.getTabs().remove(1, tabPane.getTabs().size());

		ObservableList<Tab> tabList = FXCollections.emptyObservableList();
		try (Workbook readXlsx = ExcelUtil.readXlsx(openFile)) {
			int numberOfSheets = readXlsx.getNumberOfSheets();
			tabList = FXCollections.observableArrayList();
			int activeSheetIndex = readXlsx.getActiveSheetIndex();
			for (int s = 0; s < numberOfSheets; s++) {

				Sheet sheetAt = readXlsx.getSheetAt(s);
				int lastRowNum = sheetAt.getLastRowNum();
				int firstRowNum = sheetAt.getFirstRowNum();
				Tab newTab = createNewTab(sheetAt.getSheetName(), composite -> {

					DefaultSpreadSheetView spreadSheet = composite.getSpreadSheet();
					for (int i = firstRowNum; i < lastRowNum; i++) {
						Row row = sheetAt.getRow(i);
						short lastCellNum = row.getLastCellNum();
						short firstCellNum = row.getFirstCellNum();
						for (int c = firstCellNum; c < lastCellNum; c++) {
							Cell cell = row.getCell(c);

							
							SpreadsheetCell spreadsheetCell = spreadSheet.getRows().get(i).get(c);
							switch (cell.getCellType()) {
							case BLANK:
								spreadsheetCell.setItem("");
								break;
							case BOOLEAN:
								spreadsheetCell.setItem(cell.getBooleanCellValue());
								break;
							case ERROR:
								spreadsheetCell.setItem(cell.getErrorCellValue());
								break;
							case FORMULA:
								spreadsheetCell.setItem(cell.getCellFormula());
								break;
							case NUMERIC:
								double numericCellValue = cell.getNumericCellValue();
								SpreadsheetCell newCell = SpreadsheetCellType.DOUBLE.createCell(i, c, 1, 1, numericCellValue);
								spreadSheet.getRows().get(i).set(c, newCell);
								break;
							case STRING:
								spreadsheetCell.setItem(cell.getStringCellValue());
								break;
							default:
								spreadsheetCell.setItem(cell.getStringCellValue());
								break;
							}
						}
					}

				});
				tabList.add(newTab);

			}
			tabPane.getTabs().addAll(tabList);
			tabPane.getSelectionModel().select( activeSheetIndex + 1);
			currentFile.set(openFile);
		} catch (IOException e) {
			DialogUtil.showExceptionDailog(e);
		}

	}

	@FXML
	public void miFileSaveOnAction() {
	}

	@FXML
	public void miFileSaveAsOnAction() {
		Tab selectedItem = this.tabPane.getSelectionModel().getSelectedItem();
		DefaultSpreadItemComposite c = null;
		if (selectedItem != null)
			c = (DefaultSpreadItemComposite) selectedItem.getContent();
		DefaultSpreadSheetView ss = c.getSpreadSheet();

		new SpreadSaveAction().miSaveAsOnAction(ss.getRows(), 0, 100, 0, 100);
	}
	
	@FXML
	public void miDBToolsOnAction() {
		
	}
}
