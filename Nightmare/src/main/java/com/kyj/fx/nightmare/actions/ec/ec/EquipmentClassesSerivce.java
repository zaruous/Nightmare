/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec
 *	작성일   : 2021. 11. 23.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.ec.ec;

import org.dom4j.Document;

import com.kyj.fx.nightmare.comm.ResourceLoader;
import com.kyj.fx.nightmare.comm.service.DmiService;
import com.kyj.fx.nightmare.comm.service.XMLUtils;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class EquipmentClassesSerivce {

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 11. 23.
	 * @param equipmentClassName
	 * @return
	 * @throws Exception
	 */
	public Document item(String equipmentClassGuid) throws Exception {
		String rootUrl = ResourceLoader.getInstance().get(ResourceLoader.SYNCADE_ROOT_URL);
		DmiService dmiService = new DmiService(rootUrl + "/et/WebService/EquipmentClasses.asmx?wsdl");
		Object execute = dmiService.execute("Item", "EquipmentClassesSoap", equipmentClassGuid);
		String string = execute.toString();
		return XMLUtils.load(string);

	}
}
