/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.rule
 *	작성일   : 2021. 12. 8.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.ec.ec.rule;

import com.kyj.fx.b.ETScriptHelper.comm.ResourceLoader;
import com.kyj.fx.b.ETScriptHelper.comm.service.DmiService;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EquipmentClassRuleService {

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 8. 
	 * @param equipmentClassGuid
	 * @return
	 * @throws Exception
	 */
	public Object listRule(String equipmentClassGuid) throws Exception {
		var service = new DmiService(ResourceLoader.getInstance().get(ResourceLoader.SYNCADE_ROOT_URL) + "/et/WebService/EquipmentClasses.asmx?wsdl");
		return service.execute("ListRules", "EquipmentClassesSoap", new String[] {equipmentClassGuid});
	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 8. 
	 * @param equipmentClassGuid
	 * @return
	 * @throws Exception
	 */
	public Object listRuleGroup(String equipmentClassGuid) throws Exception {
		var service = new DmiService(ResourceLoader.getInstance().get(ResourceLoader.SYNCADE_ROOT_URL) + "/et/WebService/EquipmentClasses.asmx?wsdl");
		return service.execute("ListRuleGroups", "EquipmentClassesSoap", new String[] {equipmentClassGuid});
	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 8. 
	 * @param equipmentClassGuid
	 * @return
	 * @throws Exception
	 */
	public Object listRuleByGroup(String ruleGroupGuid) throws Exception {
		var service = new DmiService(ResourceLoader.getInstance().get(ResourceLoader.SYNCADE_ROOT_URL) + "/et/WebService/EquipmentClasses.asmx?wsdl");
		return service.execute("ListRulesByGroup", "EquipmentClassesSoap", new String[] {ruleGroupGuid});
	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 9. 
	 * @param dependentGuid
	 * @return
	 * @throws Exception 
	 */
	public Object ruleXml(String dependentGuid) throws Exception {
		var service = new DmiService(ResourceLoader.getInstance().get(ResourceLoader.SYNCADE_ROOT_URL) + "/et/WebService/Events.asmx?wsdl");
		return service.execute("RuleXML", "EventsSoap", new String[] {dependentGuid});
	}
	
}