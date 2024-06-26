/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;

/**
 * 
 */
public class CodeLabel extends DefaultLabel {

	public CodeLabel() {
		super();

	}

	public CodeLabel(String text, Node graphic) {
		super(text, graphic);

	}

	public CodeLabel(String text) {
		super(text);

	}

	/**
	 * 프로그래밍 언어 <br/>
	 */
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
