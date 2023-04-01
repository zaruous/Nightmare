/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.wf
 *	작성일   : 2022. 7. 28.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.wf;

import com.kyj.fx.nightmare.comm.ResourceLoader;
import com.kyj.fx.nightmare.comm.service.DmiService;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class WorkflowInstanceService {

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2022. 7. 28.
	 * @param token
	 * @param workflowInstanceGuid
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public String createOrderXml(String etOrderType, String workflowGuid) throws Exception {

		String rootUrl = ResourceLoader.getInstance().get(ResourceLoader.SYNCADE_ROOT_URL);
		DmiService dmiService = new DmiService(rootUrl + "/et/WebService/WorkFlowInstances.asmx?wsdl");
		Object execute = dmiService.execute("CreateOrderXML", "WorkFlowInstancesSoap",  etOrderType , workflowGuid);
		return execute.toString();

	}
}
