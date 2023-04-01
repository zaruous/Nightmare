/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.comm.service
 *	작성일   : 2021. 12. 27.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.ec.ec.par;

import com.kyj.fx.nightmare.comm.service.XMLUtils;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class CamelCaseDataHandler implements XMLUtils.XmlDataHander<EventParameterDVO> {

	private String eqname;
	private String eventName;

	public CamelCaseDataHandler(String eqname, String eventName) {
		this.eqname = eqname;
		this.eventName = eventName;		
	}

	@Override
	public void handle(EventParameterDVO instance, String nodeName, String nodeValue) {
		switch (nodeName) {
		case "GUID":
			instance.setGuid(nodeValue);
			break;
		case "Name":
			instance.setName(nodeValue);
			break;
		case "DataType":
			instance.setDataType(nodeValue);
			break;
		case "RecordType":
			instance.setRecordType(nodeValue);
			break;
		case "Required":
			instance.setRequired(nodeValue);
			break;
		case "IsRepeatable":
			instance.setIsRepeatable(nodeValue);
			break;
		case "DefaultValue":
			instance.setDefaultValue(nodeValue);
			break;
		case "Sequence":
			instance.setSequence(/*Integer.parseInt(nodeValue, 10)*/ nodeValue);
			break;
		case "Description":
			instance.setDescription(nodeValue);
			break;
		}
		instance.setEquipmentClassName(eqname);
		instance.setEquipmentEventName(eventName);
	}

}
