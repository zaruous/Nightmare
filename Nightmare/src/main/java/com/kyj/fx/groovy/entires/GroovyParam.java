/********************************
 *	프로젝트 : gargoyle-nashorn
 *	패키지   : com.kyj.fx.nashorn.entires
 *	작성일   : 2018. 10. 11.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.groovy.entires;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
@XmlRootElement(name = "nashornParam")
public class GroovyParam {
	private String name;
	private Object value;
	private String type;

	/**
	 * @return the name
	 */
	@XmlAttribute(name = "name", required = true)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the value
	 */
	@XmlAttribute(name = "value")
	public Object getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * @return the type
	 */
	@XmlAttribute(name = "type")
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

}
