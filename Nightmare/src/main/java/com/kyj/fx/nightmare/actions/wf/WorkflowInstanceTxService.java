/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.wf
 *	작성일   : 2022. 7. 28.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.wf;

import com.kyj.fx.nightmare.comm.ResourceLoader;
import com.kyj.fx.nightmare.comm.service.DmiService;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class WorkflowInstanceTxService {

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2022. 7. 28.
	 * @param token
	 * @param workflowInstanceGuid
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public String createOrder(String token, String equipmentGuid, String workflowGuid, String createOrderXml) throws Exception {

		String rootUrl = ResourceLoader.getInstance().get(ResourceLoader.SYNCADE_ROOT_URL);
		DmiService dmiService = new DmiService(rootUrl + "/et/WebService/WorkFlowInstancesTx.asmx?wsdl");
		Object execute = dmiService.execute("CreateOrder", "WorkFlowInstancesTxSoap", token, equipmentGuid, workflowGuid, createOrderXml);
		return execute.toString();

	}
}
