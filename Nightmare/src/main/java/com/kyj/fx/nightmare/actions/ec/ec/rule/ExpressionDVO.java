/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.rule
 *	작성일   : 2021. 12. 8.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.ec.ec.rule;

import com.kyj.fx.nightmare.comm.AbstractDVO;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */

public class ExpressionDVO extends AbstractDVO {
	private StringProperty expressionName = new SimpleStringProperty();
	private StringProperty expressionCondition = new SimpleStringProperty();
	private StringProperty epressionValue = new SimpleStringProperty();
	private StringProperty expressionValueText = new SimpleStringProperty();
	private StringProperty expressionValueIntevalType = new SimpleStringProperty();

	public final StringProperty expressionNameProperty() {
		return this.expressionName;
	}

	public final String getExpressionName() {
		return this.expressionNameProperty().get();
	}

	public final void setExpressionName(final String expressionName) {
		this.expressionNameProperty().set(expressionName);
	}

	public final StringProperty expressionConditionProperty() {
		return this.expressionCondition;
	}

	public final String getExpressionCondition() {
		return this.expressionConditionProperty().get();
	}

	public final void setExpressionCondition(final String expressionCondition) {
		this.expressionConditionProperty().set(expressionCondition);
	}

	public final StringProperty epressionValueProperty() {
		return this.epressionValue;
	}

	public final String getEpressionValue() {
		return this.epressionValueProperty().get();
	}

	public final void setEpressionValue(final String epressionValue) {
		this.epressionValueProperty().set(epressionValue);
	}

	public final StringProperty expressionValueTextProperty() {
		return this.expressionValueText;
	}

	public final String getExpressionValueText() {
		return this.expressionValueTextProperty().get();
	}

	public final void setExpressionValueText(final String expressionValueText) {
		this.expressionValueTextProperty().set(expressionValueText);
	}

	public final StringProperty expressionValueIntevalTypeProperty() {
		return this.expressionValueIntevalType;
	}

	public final String getExpressionValueIntevalType() {
		return this.expressionValueIntevalTypeProperty().get();
	}

	public final void setExpressionValueIntevalType(final String expressionValueIntevalType) {
		this.expressionValueIntevalTypeProperty().set(expressionValueIntevalType);
	}

}
