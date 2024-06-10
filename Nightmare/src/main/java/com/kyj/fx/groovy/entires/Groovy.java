/********************************
 *	프로젝트 : gargoyle-nashorn
 *	패키지   : com.kyj.fx.nashorn.entires
 *	작성일   : 2018. 10. 11.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.groovy.entires;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
@XmlRootElement(name = "groovy")
public class Groovy {
	private GroovyParams nashornParams;

	private GroovyScript nashornScript;

	private String name;

	private String description;

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
	 * @return the nashornParams
	 */
	@XmlElement(name = "nashornParams")
	public GroovyParams getNashornParams() {
		return nashornParams;
	}

	/**
	 * @param nashornParams
	 *            the nashornParams to set
	 */
	public void setNashornParams(GroovyParams nashornParams) {
		this.nashornParams = nashornParams;
	}

	/**
	 * @return the nashornScript
	 */
	@XmlElement(name = "nashornScript")
	public GroovyScript getNashornScript() {
		return nashornScript;
	}

	/**
	 * @param nashornScript
	 *            the nashornScript to set
	 */
	public void setNashornScript(GroovyScript nashornScript) {
		this.nashornScript = nashornScript;
	}

	/**
	 * @return the description
	 */
	@XmlAttribute(name = "description")
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
