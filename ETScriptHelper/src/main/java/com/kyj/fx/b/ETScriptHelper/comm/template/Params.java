/********************************
 *	프로젝트 : api-service
 *	패키지   : beans.common
 *	작성일   : 2020. 8. 5.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.comm.template;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
@XmlRootElement
public class Params {
	private List<Param> params;

	@XmlElements(value = { @XmlElement(name = "Param", type = Param.class) })
	public List<Param> getParams() {
		return params;
	}

	public void setParams(List<Param> params) {
		this.params = params;
	}

	@Override
	public String toString() {
		return "Params [params=" + params + "]";
	}

}
