package com.kyj.fx.nightmare.actions.grid;

import org.controlsfx.control.spreadsheet.Grid;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

public class GridCommand implements Command {
	private final SpreadsheetView ssv;
	private final Grid oldValue;
	private final Grid newValue;

	public GridCommand(SpreadsheetView ssv, Grid oldValue, Grid newValue) {
		this.ssv = ssv;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	@Override
	public void execute() {
		if (newValue != null)
			ssv.setGrid(newValue);
	}

	@Override
	public void undo() {
		if (oldValue != null)
			ssv.setGrid(oldValue);

	}

}