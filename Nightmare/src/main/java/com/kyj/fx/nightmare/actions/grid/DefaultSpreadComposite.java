/**
 * 
 */
package com.kyj.fx.nightmare.actions.grid;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import javax.imageio.ImageIO;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.fxloader.FXMLController;
import com.kyj.fx.fxloader.FxPostInitialize;
import com.kyj.fx.nightmare.actions.ai.AIDataDAO;
import com.kyj.fx.nightmare.actions.ai.DefaultLabel;
import com.kyj.fx.nightmare.actions.ai.OpenAIService;
import com.kyj.fx.nightmare.comm.DialogUtil;
import com.kyj.fx.nightmare.comm.ExcelUtil;
import com.kyj.fx.nightmare.comm.ExecutorDemons;
import com.kyj.fx.nightmare.comm.FileUtil;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.GargoyleExtensionFilters;
import com.kyj.fx.nightmare.comm.Message;
import com.kyj.fx.nightmare.comm.StageStore;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.comm.report.ReportHelper;
import com.kyj.fx.nightmare.ui.frame.AbstractCommonsApp;
import com.kyj.fx.nightmare.ui.grid.AnnotationOptions;
import com.kyj.fx.nightmare.ui.grid.Buttons;
import com.kyj.fx.nightmare.ui.grid.CrudBaseGridView;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
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
import javafx.util.StringConverter;

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
	private BorderPane borContent, borSql;

	private OpenAIService openAIService;
	private ObjectProperty<File> currentFile = new SimpleObjectProperty<File>();
	@FXML
	private ComboBox<Datasource> cbDatabase;
	private SqlKeywords sqlKeywords;

	public DefaultSpreadComposite() {
		try {
			FxUtil.loadRoot(DefaultSpreadComposite.class, this);
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}

	@FxPostInitialize
	public void after() {
		ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(() -> {
			List<Datasource> dataSourceList = getDataSourceList();
			Platform.runLater(() -> {
				cbDatabase.getItems().addAll(dataSourceList);
			});
		});

	}

	@FXML
	public void miReportOnAction() {
		DefaultSpreadItemComposite currentView = getCurrentView();
		ListView<DefaultLabel> lvResult = currentView.getLvResult();
		ObservableList<DefaultLabel> items = lvResult.getItems();

		File file = new File("test.html");
		try (FileOutputStream out = new FileOutputStream(file)) {
			ReportHelper.generate(items, out);
		} catch (IOException e) {
			DialogUtil.showExceptionDailog(e);
		}
		
		try {
			Desktop.getDesktop().open(file);
		} catch (IOException e) {
			DialogUtil.showExceptionDailog(e);
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
//		File outputFile = new File("output.png");
		try {
			var outFile = DialogUtil.showFileSaveDialog(FxUtil.getWindow(this), chooser -> {
				chooser.setInitialFileName("out.png");
				chooser.getExtensionFilters().add(GargoyleExtensionFilters.PNG_EXTENSION_FILTER);
				chooser.getExtensionFilters().add(GargoyleExtensionFilters.ALL_FILTER);
			});
			if (outFile != null) {
				ImageIO.write(fromFXImage, "png", outFile);
			}
		} catch (IOException e) {
			DialogUtil.showExceptionDailog(e);
		}

	}
	final String TAB_NAME_FORMT = "Sheet%d";  
	private int MAX_TAB_SIZE = 1000;
	
	@FXML
	public void initialize() {
		try {
			this.openAIService = new OpenAIService();
			
			String systemPrompt = """
					데이터 전문가가 되어줘.
					""";
			this.openAIService.setSystemRole(this.openAIService.createDefault(systemPrompt));
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
				
				for(int i= tabPane.getTabs().size(); i< MAX_TAB_SIZE; i++ ) {
					if(isTabNameValid("Sheet" + i))
					{
						addNewTabView("Sheet" + i);
						break;
					}
				}
			}
		});

		tabPane.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
			if (ev.getCode() == KeyCode.F2) {
				renameOnAction(new ActionEvent());
			}
		});

		addNewTabView(String.format(TAB_NAME_FORMT, 1));

		sqlKeywords = new SqlKeywords();
		borSql.setCenter(sqlKeywords);
		sqlKeywords.addEventFilter(KeyEvent.KEY_PRESSED, this::sqlKeywordOnKeyPressed);

		this.cbDatabase.setConverter(new StringConverter<Datasource>() {

			@Override
			public String toString(Datasource object) {
				return object.getAliasName();
			}

			@Override
			public Datasource fromString(String string) {
				return null;
			}
		});

	}

	public void sqlKeywordOnKeyPressed(KeyEvent ke) {
		if (ke.getCode() == KeyCode.ENTER && ke.isControlDown()) {
			if (!ke.isConsumed())
				ke.consume();
			Datasource ds = cbDatabase.getSelectionModel().getSelectedItem();

			try {
				var dao = new DataSourceDAO(ds.getAliasName());
				List<Map<String, Object>> query = dao.query(sqlKeywords.getCodeArea().getText(), Map.of());
				LOGGER.debug("data size : {}", query.size());
				DefaultSpreadItemComposite currentView = getCurrentView();
				currentView.updateUI(query, 0, 0);

			} catch (Exception e) {
				LOGGER.error(ValueUtil.toString(e));
			}

		}
	}

//	private Connection getConnection(Datasource v) throws SQLException {
//		return DbUtil.getConnection(v.getDriver(), v.getUrl(), v.getUserId(), v.getUserPwd() , ds->{
//		});	
//	}

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

	public DefaultSpreadItemComposite getCurrentView() {
		Tab selectedItem = tabPane.getSelectionModel().getSelectedItem();
		if (selectedItem == null)
			throw new RuntimeException("no tab selected.");
		return (DefaultSpreadItemComposite) selectedItem.getContent();
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
			return false;
		}
		return true;
	}

	public Tab addNewTabView(String newTabName) {

		Tab newTab = createNewTab(newTabName, c -> {

		});
		if (!isTabNameValid(newTabName)) {
			DialogUtil.showMessageDialog(String.format("중복된 이름이 존재합니다.[%s]", newTabName));
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
			chooser.getExtensionFilters().add(GargoyleExtensionFilters.XLSX_FILTER);
//			chooser.getExtensionFilters().add(GargoyleExtensionFilters.CSV_FILTER);
		});

		if (openFile == null)
			return;

		tabPane.getTabs().remove(1, tabPane.getTabs().size());

		ObservableList<Tab> tabList = FXCollections.emptyObservableList();

		
		if(openFile.getName().toLowerCase().endsWith(".csv"))
		{
			//TODO
		}
		else if (openFile.getName().toLowerCase().endsWith(".xlsx")) {
			try (Workbook readXlsx = ExcelUtil.readXlsx(openFile)) {
				int numberOfSheets = readXlsx.getNumberOfSheets();
				tabList = FXCollections.observableArrayList();
				int activeSheetIndex = readXlsx.getActiveSheetIndex();
				for (int s = 0; s < numberOfSheets; s++) {

					Sheet sheetAt = readXlsx.getSheetAt(s);
					int lastRowNum = sheetAt.getLastRowNum();
					int firstRowNum = sheetAt.getFirstRowNum();
					Tab newTab = createNewTab(sheetAt.getSheetName(), (lastRowNum > 100 ? lastRowNum : 100),
							(firstRowNum > 100 ? firstRowNum : 100), composite -> {

								DefaultSpreadSheetView spreadSheet = composite.getView();
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
											SpreadsheetCell newCell = SpreadsheetCellType.DOUBLE.createCell(i, c, 1, 1,
													numericCellValue);
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
				tabPane.getSelectionModel().select(activeSheetIndex + 1);
				currentFile.set(openFile);
			} catch (IOException e) {
				DialogUtil.showExceptionDailog(e);
			}
		}

	}

	public final TabPane getTabPane() {
		return tabPane;
	}

	/**
	 * @return
	 */
	public String getDocumentText() {

		ObservableList<Tab> tabs = getTabPane().getTabs();
		Optional<String> reduce = tabs.stream().filter(v -> v.getContent() != null).map(tab -> {
			DefaultSpreadItemComposite composite = (DefaultSpreadItemComposite) tab.getContent();
			DefaultSpreadSheetView spreadSheet = composite.getView();
			SpreadsheetView view = spreadSheet.getView();

			String text = tab.getText();
			String string = DefaultGridBase.toString(view.getGrid());
			return "#" + text + "\n" + string;
		}).reduce((a, b) -> a.concat("\n").concat(b));

		if (reduce.isPresent())
			return reduce.get();
		return "";
	}

	@FXML
	public void miFileSaveOnAction() {
		File saveFile = currentFile.get();
		if (saveFile == null) {
			miFileSaveAsOnAction();
			return;
		}
		saveFileProcessor(saveFile);
	}

	@FXML
	public void miFileSaveAsOnAction() {
		File saveFile = DialogUtil.showFileSaveDialog(StageStore.getPrimaryStage(), chooser -> {
			chooser.getExtensionFilters()
					.add(new ExtensionFilter(GargoyleExtensionFilters.XLSX_NAME, GargoyleExtensionFilters.XLSX));
		});
		if (saveFile == null)
			return;

		saveFileProcessor(saveFile);

	}

	/**
	 * @param saveFile
	 */
	private void saveFileProcessor(File saveFile) {
		ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(() -> {
			boolean success = false;
			SpreadSaveAction spreadSaveAction = new SpreadSaveAction();
			try (Workbook wb = ExcelUtil.createNewWorkBookXlsx()) {
				spreadSaveAction.addSheetData(wb, this);
				try (FileOutputStream stream = new FileOutputStream(saveFile)) {
					wb.write(stream);
				}
				success = true;
			} catch (IOException e) {
				Platform.runLater(() -> {
					DialogUtil.showExceptionDailog(e);
				});
				return;
			}

			if (success) {
				Platform.runLater(() -> {
					DialogUtil.showMessageDialog("Complete");
				});
			}
		});
	}

	@FXML
	public void miDBToolsOnAction() {
		CrudBaseGridView<Datasource> view = new CrudBaseGridView<Datasource>(Datasource.class,
				new AnnotationOptions<>(Datasource.class) {

					@Override
					public int useButtons() {
						return Buttons.ADD | Buttons.DELETE | Buttons.SAVE | Buttons.UPDATE;
					}

					@Override
					public boolean useCommonCheckBox() {
						return true;
					}
				});

		view.setSaveClickCallback(list -> {
			try {
				var ds = AIDataDAO.getInstance();
				ds.saveBatch(list);

				DialogUtil.showMessageDialog(Message.getInstance().getMessage("SaveComplete")/* "저장되었습니다." */);
			} catch (Exception e) {
				DialogUtil.showExceptionDailog(e);
			}
		});
		view.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		view.getSelectionModel().setCellSelectionEnabled(true);

		FxUtil.installClipboardKeyEvent(view.getRealGrid());

		FxUtil.createStageAndShow(view, stage -> {
			FxUtil.installFindKeyEvent(stage, view.getRealGrid());
		});

		ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(() -> {
			List<Datasource> queryForList = getDataSourceList();

			Platform.runLater(() -> {
				view.getItems().addAll(queryForList);
			});

		});

	}

	/**
	 * @return
	 */
	private List<Datasource> getDataSourceList() {
		var ds = AIDataDAO.getInstance();
		String statement = """
				select * from datasource where 1=1 order by FST_REG_DT
				""";
		List<Datasource> queryForList = ds.queryForList(statement, Map.of(), Datasource.class);
		return queryForList;
	}
}
