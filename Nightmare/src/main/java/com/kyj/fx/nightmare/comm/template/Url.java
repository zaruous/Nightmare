/********************************
 *	프로젝트 : api-service
 *	패키지   : beans.common
 *	작성일   : 2020. 8. 5.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.nightmare.comm.template;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class Url {

	private Pattern pattern;

	private Params params;

	@XmlElement(name = "Pattern")
	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	@XmlElement(name = "Params", nillable = false)
	public Params getParams() {
		return params;
	}

	public void setParams(Params params) {
		this.params = params;
	}

}
