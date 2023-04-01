/********************************
 *	프로젝트 : api-service
 *	패키지   : beans.common
 *	작성일   : 2020. 8. 5.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.comm.template;

import javax.xml.bind.annotation.XmlValue;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class Descroption {

	private String content;

	@XmlValue
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
