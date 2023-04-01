/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.eq.eventform
 *	작성일   : 2021. 12. 10.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.ec.eq.eventform;

import com.kyj.fx.nightmare.comm.ResourceLoader;
import com.kyj.fx.nightmare.comm.service.DmiService;
import com.kyj.fx.nightmare.comm.service.XMLUtils;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class CancelEventPerformService {

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 10.
	 * @param equipmentGuid
	 * @param eventGuid
	 * @return 
	 * @throws Exception
	 */
	public Object cancel(String equipmentName, String eventName) throws Exception {
		var service = new DmiService(
				ResourceLoader.getInstance().get(ResourceLoader.SYNCADE_ROOT_URL) + "/WF/webservice/Execution.asmx?wsdl");
		String behaviorFileName = "PSSB_ET_Perform_Event.wib";

		StringBuffer buffBehaviorValues = new StringBuffer();
		buffBehaviorValues.append("\n");
		buffBehaviorValues.append("<BehaviorValues>\n");
		buffBehaviorValues.append("  <Object\n");
		buffBehaviorValues.append("   name=\"parExpectedEquipmentClass\"></Object>\n");
		buffBehaviorValues.append("  <Object\n");
		buffBehaviorValues.append("   name=\"parEventParameterValues\"></Object>\n");
		buffBehaviorValues.append("  <Object\n");
		buffBehaviorValues.append("   name=\"parEventParameters\"></Object>\n");
		buffBehaviorValues.append("  <Object\n");
		buffBehaviorValues.append("   name=\"parUOM\"></Object>\n");
		buffBehaviorValues.append("  <Object\n");
		buffBehaviorValues.append("   name=\"parLotSize\"></Object>\n");
		buffBehaviorValues.append("  <Object\n");
		buffBehaviorValues.append("   name=\"parEventAction\">Cancel</Object>\n");
		buffBehaviorValues.append("  <Object\n");
		buffBehaviorValues.append("   name=\"parEquipID1\">" + equipmentName + "</Object>\n");
		buffBehaviorValues.append("  <Object\n");
		buffBehaviorValues.append("   name=\"parEventName\">" + eventName + "</Object>\n");
		buffBehaviorValues.append("  <Object\n");
		buffBehaviorValues.append("   name=\"parEquipID2\"></Object>\n");
		buffBehaviorValues.append("</BehaviorValues>\n");

		StringBuffer buffValueableValues = new StringBuffer();
		buffValueableValues.append("<VariableValues></VariableValues>");

		var ret = service.execute("ExecuteBehavior", "ExecutionSoap", new String[] { behaviorFileName,
				XMLUtils.escape(buffBehaviorValues.toString()), XMLUtils.escape(buffValueableValues.toString()) });

		/*
<BehaviorResults Version="4.0.3"><CS_PARAMETERVALUE>Failed</CS_PARAMETERVALUE><CS_MSG>Server was unable to process request. ---&gt; An invalid value has been entered.
ExecutionState has an invalid value: 4 This equipment event is not already in process!</CS_MSG><CS_RESULT>CS_EVENTFAILED</CS_RESULT></BehaviorResults>
		 * */
//		System.out.println(ret);
		return ret;
	}

}
