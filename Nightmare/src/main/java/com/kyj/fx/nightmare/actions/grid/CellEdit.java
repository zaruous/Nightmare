/**
 * 
 */
package com.kyj.fx.nightmare.actions.grid;

import org.controlsfx.control.spreadsheet.SpreadsheetCell;

/**
 * 
 */
public class CellEdit {

	private final SpreadsheetCell cell;
	private final Object oldValue;
	private final Object newValue;

	public CellEdit(SpreadsheetCell cell, Object oldValue, Object newValue) {
		this.cell = cell;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public SpreadsheetCell getCell() {
		return cell;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public Object getNewValue() {
		return newValue;
	}

}
