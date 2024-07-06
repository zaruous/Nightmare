package com.kyj.fx.nightmare.actions.grid;

import java.util.List;

public class RangeCommand implements Command {
	private final List<CellEdit> cells;

	public RangeCommand(List<CellEdit> cells) {
		this.cells = cells;
	}

	@Override
	public void execute() {
		cells.forEach(c -> {
			c.getCell().setItem(c.getNewValue());
		});
	}

	@Override
	public void undo() {
		for (CellEdit c : cells) {
			c.getCell().setItem(c.getOldValue());
		}
	}

}