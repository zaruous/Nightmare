/********************************
 *	프로젝트 : api-service
 *	패키지   : beans.common
 *	작성일   : 2020. 8. 5.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.nightmare.comm.template;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
//@XmlRootElement(name = "Param")
public class Param {

	private String name;

	private String dataType;

	private String defaultValue;

	@XmlAttribute(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(name = "dataType")
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	@XmlAttribute(name = "defaultValue")
	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public String toString() {
		return "Param [name=" + name + ", dataType=" + dataType + ", defaultValue=" + defaultValue + "]";
	}

}
