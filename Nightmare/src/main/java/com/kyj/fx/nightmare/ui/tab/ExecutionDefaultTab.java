/**
 * 
 */
package com.kyj.fx.nightmare.ui.tab;

import java.io.File;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

/**
 * 
 */
public class ExecutionDefaultTab extends SystemDefaultFileTab{

	private TextArea console = new TextArea();
	/**
	 * 임시 파일인지 여부를 가리킨다.
	 */
	BooleanProperty isTemp = new SimpleBooleanProperty(false);
	Label masking = new Label("");
	/**
	 * @param f
	 */
	public ExecutionDefaultTab(File f) {
		super();
		BorderPane bp = (BorderPane) getContent();
		
		bp.setBottom(console);
		setFile(f);
		this.setGraphic(masking);
		//임시파일인경우 파밀명에 * 표시를 한다.
		this.isTemp.addListener((oba,o,n)->{
			if(n) {
				masking.setText("*");
			}
			else
				masking.setText("");
		});
	}
	public ExecutionDefaultTab(File f, boolean isTemp) {
		this(f);
		this.isTemp.set(isTemp);
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
	public final BooleanProperty isTempProperty() {
		return this.isTemp;
	}
	
	public final boolean isIsTemp() {
		return this.isTempProperty().get();
	}
	
	public final void setIsTemp(final boolean isTemp) {
		this.isTempProperty().set(isTemp);
	}
	
	
	
}
