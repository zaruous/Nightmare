/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec
 *	작성일   : 2021. 11. 23.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.ec.ec.par;

import org.dom4j.Document;

import com.kyj.fx.nightmare.comm.ResourceLoader;
import com.kyj.fx.nightmare.comm.service.DmiService;
import com.kyj.fx.nightmare.comm.service.XMLUtils;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EquipmentParameterService {

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 23.
	 * @param equipmentClassName
	 * @return
	 * @throws Exception
	 */
	public Document item(String equipmentClassGuid) throws Exception {

		String rootUrl = ResourceLoader.getInstance().get(ResourceLoader.SYNCADE_ROOT_URL);
		DmiService dmiService = new DmiService(rootUrl + "/et/WebService/Events.asmx?wsdl");
		Object execute = dmiService.execute("Item", "EventsSoap", equipmentClassGuid);
		String string = execute.toString();
		return XMLUtils.load(string);

	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 27.
	 * @param token
	 * @param updateXml
	 * @return
	 * @throws Exception
	 */
	public Document update(String token, String updateXml) throws Exception {
		
		
//		https://p3mesdev/et/WebService/EventsTx.asmx?wsdl
		String rootUrl = ResourceLoader.getInstance().get(ResourceLoader.SYNCADE_ROOT_URL);
		DmiService dmiService = new DmiService(rootUrl + "/et/WebService/EventsTx.asmx?wsdl");
		Object execute = dmiService.execute("Update", "EventsTxSoap", new String[] { token, updateXml } );
		String string = execute.toString();
		return XMLUtils.load(string);
	}
}
