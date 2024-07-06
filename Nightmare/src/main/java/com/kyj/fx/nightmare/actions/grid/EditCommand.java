package com.kyj.fx.nightmare.actions.grid;

import org.controlsfx.control.spreadsheet.SpreadsheetCell;

public class EditCommand implements Command {
	private final SpreadsheetCell cell;
	private final Object oldValue;
	private final Object newValue;

	public EditCommand(SpreadsheetCell cell, Object oldValue, Object newValue) {
		this.cell = cell;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	@Override
	public void execute() {
		cell.setItem(newValue);
	}

	@Override
	public void undo() {
		cell.setItem(oldValue);
	}

	@Override
	public String toString() {
		return "EditCommand [cell=" + cell + ", oldValue=" + oldValue + ", newValue=" + newValue + "]";
	}

}