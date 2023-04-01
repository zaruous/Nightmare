/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.group
 *	작성일   : 2021. 12. 17.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.ec.ec.group;

import com.kyj.fx.nightmare.comm.ResourceLoader;
import com.kyj.fx.nightmare.comm.service.DmiService;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class EquipmentClassGroupService {

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 12. 17.
	 * @param _saToken
	 * @param _ruleGroupXml
	 * @return
	 * @throws Exception
	 */
	public Object updateRuleGroup(String _saToken, String ruleGroupXml) throws Exception {
		DmiService s = new DmiService(
				ResourceLoader.getInstance().get(ResourceLoader.SYNCADE_ROOT_URL) + "/et/WebService/EquipmentClassesTx.asmx?wsdl");
		return s.execute("UpdateRuleGroups", "EquipmentClassesTxSoap", new String[] { _saToken, ruleGroupXml });

	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 12. 17.
	 * @param equipmentClassGuid
	 * @return
	 * @throws Exception
	 */
	public Object listRuleGroup(String equipmentClassGuid) throws Exception {
		DmiService s = new DmiService(
				ResourceLoader.getInstance().get(ResourceLoader.SYNCADE_ROOT_URL) + "/et/WebService/EquipmentClasses.asmx?wsdl");
		return s.execute("ListRuleGroups", "EquipmentClassesSoap", new String[] { equipmentClassGuid });
	}

}
