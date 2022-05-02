/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper
 *	작성일   : 2021. 11. 22.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.eqtree;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * ET 설정에 관련된 항목을 정의하는 트리 트리 아이템 데이터셋 <br/>
 * 
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EtConfigurationTreeDVO {

	private StringProperty id = new SimpleStringProperty();
	private StringProperty displayText = new SimpleStringProperty();
	/**
	 * @param id
	 * @param displayText
	 */
	public EtConfigurationTreeDVO(String id, String displayText) {
		super();
		this.id.set(id);
		this.displayText.set(displayText);
	}

	public final StringProperty idProperty() {
		return this.id;
	}

	public final String getId() {
		return this.idProperty().get();
	}

	public final void setId(final String id) {
		this.idProperty().set(id);
	}

	public final StringProperty displayTextProperty() {
		return this.displayText;
	}

	public final String getDisplayText() {
		return this.displayTextProperty().get();
	}

	public final void setDisplayText(final String displayText) {
		this.displayTextProperty().set(displayText);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.displayText.get();
	}

}
