/********************************
 *	프로젝트 : gargoyle-nashorn
 *	패키지   : com.kyj.fx.nashorn.entires
 *	작성일   : 2018. 10. 11.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.groovy.entires;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
@XmlRootElement(name = "nashornParams")
public class GroovyParams {

	private List<GroovyParam> nashornParams;

	/**
	 * @return the nashornParams
	 */
	@XmlElements(@XmlElement(name = "nashornParam", type = GroovyParam.class))
	public List<GroovyParam> getNashornParams() {
		return nashornParams;
	}

	/**
	 * @param nashornParams
	 *            the nashornParams to set
	 */
	public void setNashornParams(List<GroovyParam> nashornParams) {
		this.nashornParams = nashornParams;
	}

}
