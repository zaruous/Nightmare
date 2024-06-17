/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

import javafx.beans.property.StringProperty;

/**
 * 사용자 정의 커스텀 데이터 모델
 */
public class CustomLabel extends DefaultLabel {
	private ICustomSupportView customGraphics;

	public CustomLabel(ICustomSupportView customGraphics) {
		super("");
		this.customGraphics = customGraphics;
		setGraphic(customGraphics.getView());
	}

	public final StringProperty dataProperty() {
		return this.customGraphics.dataProperty();
	}

	public final String getData() {
		return this.dataProperty().get();
	}

	public final void setData(final String data) {
		this.dataProperty().set(data);
	}

}