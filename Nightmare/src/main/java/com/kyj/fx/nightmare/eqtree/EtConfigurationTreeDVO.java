/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper
 *	작성일   : 2021. 11. 22.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.eqtree;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * ET 설정에 관련된 항목을 정의하는 트리 트리 아이템 데이터셋 <br/>
 * 
 * @author KYJ (zaruous@naver.com)
 *
 */
public class EtConfigurationTreeDVO {

	private StringProperty id = new SimpleStringProperty();
	private StringProperty displayText = new SimpleStringProperty();
	private Map<String, Object> properties = new HashMap<>();

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2022. 7. 22.
	 * @param key
	 * @param value
	 */
	public void addProperty(String key, Object value) {
		this.properties.put(key, value);
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2022. 7. 22.
	 * @return
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2022. 7. 22.
	 * @param properties
	 */
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

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
