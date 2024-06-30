/**
 * 
 */
package com.kyj.fx.nightmare.actions.grid;

import java.util.List;

import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * 
 */
public class DefaultGridBase {

	public static GridBase createGrid(int rowSize, int colSize) {
		GridBase gridBase = new GridBase(rowSize, colSize);
		List<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();

		for (int row = 0; row < gridBase.getRowCount(); ++row) {
			ObservableList<SpreadsheetCell> currentRow = FXCollections.observableArrayList();
			for (int column = 0; column < gridBase.getColumnCount(); ++column) {
				SpreadsheetCell createCell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, "");
				currentRow.add(createCell);
			}
			rows.add(currentRow);
		}
		gridBase.setRows(rows);
		return gridBase;
	}
	
}
