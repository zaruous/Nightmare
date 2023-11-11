/**
 * 
 */
package com.kyj.fx.nightmare.ui.tab;

import java.io.File;

import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

/**
 * 
 */
public class ExecutionDefaultTab extends SystemDefaultFileTab{

	TextArea console = new TextArea();
	/**
	 * @param f
	 */
	public ExecutionDefaultTab(File f) {
		super();
		BorderPane bp = (BorderPane) getContent();
		
		bp.setBottom(console);
		setFile(f);
	}

	/**
	 * @return
	 */
	public TextArea getConsole() {
		return this.console;
	}
	
	/**
	 * @return
	 */
	public String getCodeText() {
		return getTextArea().getText();
	}
}
