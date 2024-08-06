/**
 * 
 */
package com.kyj.fx.nightmare.actions.grid;

import java.util.List;

import org.controlsfx.control.spreadsheet.Grid;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;

import com.kyj.fx.nightmare.comm.ValueUtil;

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
	
	/**
	 * @param gridBase
	 * @return
	 */
	public static String toString(Grid gridBase) {
		var sb = new StringBuilder();
		var rowAppender = new StringBuilder();
		List<ObservableList<SpreadsheetCell>> rows = gridBase.getRows();
		boolean rowIsNotEmpty = false;
		
		int maxValidColIndex = 0;
		
		for (int row = 0; row < gridBase.getRowCount(); ++row) {
			ObservableList<SpreadsheetCell> currentRow = rows.get(row);
			for (int column = gridBase.getColumnCount() -1 ; column >= 0 ; column --) {
				SpreadsheetCell createCell = currentRow.get(column);

				if(ValueUtil.isNotEmpty(createCell.getText()))
				{
					if(maxValidColIndex < column)
					{
						maxValidColIndex = Integer.max(maxValidColIndex, column);
						continue;
					}
				}
			}
			
		}
		
		
		for (int row = 0; row < gridBase.getRowCount(); ++row) {
			ObservableList<SpreadsheetCell> currentRow = rows.get(row);
			rowAppender.setLength(0);
			rowIsNotEmpty = false;
			for (int column = 0; column < maxValidColIndex; ++column) {
				SpreadsheetCell createCell = currentRow.get(column);
				String text = createCell.getText();
				rowIsNotEmpty |= ValueUtil.isNotEmpty(text);
				rowAppender.append(text).append("\t");
			}
			if(rowIsNotEmpty)
			{
				sb.append(rowAppender);
				sb.append("\n");
			}
		}
		return sb.toString();
	}
}
