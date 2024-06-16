/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 
 */
public class QuestionLabel extends DefaultLabel {
	QuestionComposite graphic;
	private StringProperty data = new SimpleStringProperty();
	private IntegerProperty userAnswer = new SimpleIntegerProperty(-1);

	public QuestionLabel(String text, QuestionComposite graphic) {
		super(text, graphic.getView());
		this.graphic = (QuestionComposite) graphic;
	}

	public QuestionLabel(String text) {
		super(text);

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

	public String getData() {
		return this.data.get();
	}

	public void setData(String data) {
		this.data.set(data);
	}

	public final IntegerProperty userAnswerProperty() {
		return this.userAnswer;
	}

	public final int getUserAnswer() {
		return this.userAnswerProperty().get();
	}

	public final void setUserAnswer(final int userAnswer) {
		this.userAnswerProperty().set(userAnswer);
	}

}
