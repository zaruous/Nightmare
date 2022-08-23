/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec
 *	작성일   : 2021. 11. 23.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.ec;

import org.dom4j.Document;
import org.junit.jupiter.api.Test;

import com.kyj.fx.b.ETScriptHelper.actions.ec.ec.EquipmentClassesSerivce;
import com.kyj.fx.b.ETScriptHelper.comm.ResourceLoader;
import com.kyj.fx.b.ETScriptHelper.comm.service.DmiService;
import com.kyj.fx.b.ETScriptHelper.comm.service.XMLUtils;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */

class EquipmentClassSerivceTest {

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 23.
	 * @throws Exception
	 */
	@Test
	void itemTest() throws Exception {
		var s = new EquipmentClassesSerivce() {

			@Override
			public Document item(String equipmentClassGuid) throws Exception {
				String rootUrl = ResourceLoader.getInstance().get(ResourceLoader.SYNCADE_ROOT_URL);
				DmiService dmiService = new DmiService(rootUrl + "/et/WebService/EquipmentClasses.asmx?wsdl");
				Object execute = dmiService.execute("Item", "EquipmentClassesSoap", equipmentClassGuid);
				String string = execute.toString();
				return XMLUtils.load(string);
			}
			
		};
		
		var doc = s.item("56D0781D-70B7-4163-9992-BC05656AF6A0");
		System.out.println(doc.asXML());
	}

}
