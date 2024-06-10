/********************************
 *	프로젝트 : gargoyle-nashorn
 *	패키지   : com.kyj.fx.nashorn.entires
 *	작성일   : 2018. 10. 11.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.groovy.entires;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
@XmlRootElement(name = "nashornScript")
public class GroovyScript {

	private String content;

	private String executeFunction;

	/**
	 * @return the content
	 */
	@XmlValue
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the executeFunction
	 */
	@XmlAttribute(name = "executeFunction")
	public String getExecuteFunction() {
		return executeFunction;
	}

	/**
	 * @param executeFunction
	 *            the executeFunction to set
	 */
	public void setExecuteFunction(String executeFunction) {
		this.executeFunction = executeFunction;
	}

}
