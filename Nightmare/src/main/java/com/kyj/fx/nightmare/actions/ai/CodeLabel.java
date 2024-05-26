/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;

/**
 * 
 */
public class CodeLabel extends Label {

	public CodeLabel() {
		super();		
		getStyleClass().add("codeLabel");
	}

	public CodeLabel(String text, Node graphic) {
		super(text, graphic);
		getStyleClass().add("codeLabel");
	}

	public CodeLabel(String text) {
		super(text);
		getStyleClass().add("codeLabel");
	}

	private StringProperty codeType = new SimpleStringProperty();

	public final StringProperty codeTypeProperty() {
		return this.codeType;
	}

	public final String getCodeType() {
		return this.codeTypeProperty().get();
	}

	public final void setCodeType(final String codeType) {
		this.codeTypeProperty().set(codeType);
	}

}
