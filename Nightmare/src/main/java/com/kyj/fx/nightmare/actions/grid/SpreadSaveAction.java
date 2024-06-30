/**
 * 
 */
package com.kyj.fx.nightmare.actions.grid;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.stream.IntStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;

import com.kyj.fx.nightmare.ThreadUtil;
import com.kyj.fx.nightmare.comm.DialogUtil;
import com.kyj.fx.nightmare.comm.ExcelUtil;
import com.kyj.fx.nightmare.comm.ExecutorDemons;
import com.kyj.fx.nightmare.comm.GargoyleExtensionFilters;
import com.kyj.fx.nightmare.comm.StageStore;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * 
 */
public class SpreadSaveAction {
	
	/**
	 * @param rows
	 * @param sr
	 * @param er
	 * @param sc
	 * @param ec
	 */
	public void miSaveAsOnAction(ObservableList<ObservableList<SpreadsheetCell>> rows, int sr, int er, int sc, int ec) {

		File saveFile = DialogUtil.showFileSaveDialog(StageStore.getPrimaryStage(), chooser -> {
//			chooser.setSelectedExtensionFilter(new ExtensionFilter(GargoyleExtensionFilters.XLSX_NAME, GargoyleExtensionFilters.XLSX));
			chooser.getExtensionFilters()
					.add(new ExtensionFilter(GargoyleExtensionFilters.XLSX_NAME, GargoyleExtensionFilters.XLSX));
//			chooser.getSelectedExtensionFilter().getExtensions().add(GargoyleExtensionFilters.XLSX_NAME);
		});
		if (saveFile != null) {

			ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(() -> {

				try (Workbook workBookXlsx = ExcelUtil.createNewWorkBookXlsx()) {

					Sheet createSheet = workBookXlsx.createSheet("Sheet1");

					IntStream.iterate(sr, r -> r + 1).limit(er).forEach(rIndex -> {

						ObservableList<SpreadsheetCell> cellList = rows.get(rIndex);

						IntStream.iterate(sc, a -> a + 1).limit(ec).forEach(cidx -> {

							SpreadsheetCell spreadsheetCell = cellList.get(cidx);
							String text = spreadsheetCell.getText();

							try {
								ExcelUtil.createCell(createSheet, text, rIndex, cidx);
							} catch (Exception e1) {
								e1.printStackTrace();
							}

						});

					});

					workBookXlsx.write(new FileOutputStream(saveFile));

					Platform.runLater(()->{DialogUtil.showMessageDialog("Complete");});
					
				} catch (IOException e) {
					//e.printStackTrace();
					Platform.runLater(()->{DialogUtil.showExceptionDailog(e);});
				}

			});

		}
	}
}
