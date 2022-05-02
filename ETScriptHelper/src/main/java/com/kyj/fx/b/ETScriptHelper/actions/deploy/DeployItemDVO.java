/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.deploy
 *	작성일   : 2021. 12. 6.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.deploy;

import java.util.ArrayList;
import java.util.List;

import com.kyj.fx.b.ETScriptHelper.comm.AbstractDVO;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class DeployItemDVO<T extends AbstractDVO> extends AbstractDVO {

	private StringProperty name = new SimpleStringProperty();
	private StringProperty message = new SimpleStringProperty();
	// private StringProperty status = new SimpleStringProperty();
	private List<T> item = new ArrayList<>();

	public enum StatusCode {
		WAIT, COMPLETE, PROCESSING, FAILED, STOP, PAUSE;
	}

	/*
	 * 
	 * */
	private ObjectProperty<StatusCode> statusCode = new SimpleObjectProperty<>(StatusCode.WAIT);

	public final StringProperty nameProperty() {
		return this.name;
	}

	public final String getName() {
		return this.nameProperty().get();
	}

	public final void setName(final String name) {
		this.nameProperty().set(name);
	}

	public final StringProperty messageProperty() {
		return this.message;
	}

	public final String getMessage() {
		return this.messageProperty().get();
	}

	public final void setMessage(final String message) {
		this.messageProperty().set(message);
	}

	public List<T> getItem() {
		return item;
	}

	public void setItem(List<T> item) {
		this.item = item;
	}

	public final ObjectProperty<StatusCode> statusCodeProperty() {
		return this.statusCode;
	}

	public final StatusCode getStatusCode() {
		return this.statusCodeProperty().get();
	}

	public final void setStatusCode(final StatusCode statusCode) {
		this.statusCodeProperty().set(statusCode);
	}

}
