/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.wf
 *	작성일   : 2022. 7. 28.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.wf;

import java.util.Collections;

import com.kyj.fx.nightmare.comm.AbstractDAO;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class EMRWorkflowInstanceDQM extends AbstractDAO {

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2022. 7. 28.
	 * @param dmiEtWorkflowInstanceId
	 * @return
	 */
	public String getEmrWorkflowInstanceId(String dmiEtWorkflowInstanceId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select    \n");
		sb.append("	      \n");
		sb.append("	  a.WorkflowInstanceId    \n");
		sb.append("	  ,a.WorkflowId    \n");
		sb.append("	  ,a.Number    \n");
		sb.append("	  ,a.CompletedDate    \n");
		sb.append("	  , a.State    \n");
		sb.append("	  , a.sourceType    \n");
		sb.append("	  ,'' as RetMessage    \n");
		sb.append("	  ,'' as ErrorMessage     \n");
		sb.append("	      \n");
		sb.append("	  from    \n");
		sb.append("	      EMR_WF.dbo.WorkflowInstances(nolock) a  inner join DMI_ET.dbo.ET_WorkFlowInstance (nolock) b     \n");
		sb.append("	  	on a.number = b.ordernumber    \n");
		sb.append("	  where 1=1     \n");
		sb.append("\n");
		sb.append("	  and b.WorkFlowInstanceGUID = :workflowInstanceId\n");
		sb.append("	\n");

		return queryScala(sb.toString(), Collections.singletonMap("workflowInstanceId", dmiEtWorkflowInstanceId));
	}
}
