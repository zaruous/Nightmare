/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.scripts
 *	작성일   : 2021. 12. 6.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.ec.ec.scripts;

import com.kyj.fx.nightmare.comm.ResourceLoader;
import com.kyj.fx.nightmare.comm.service.DmiService;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class EquipmentClassEventScriptService {

	/**
	 * 이벤트 정보 조회 <br/>
	 * 
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 12. 6.
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	public Object item(String eventGuid) throws Exception {
		DmiService s = new DmiService(
				ResourceLoader.getInstance().get(ResourceLoader.SYNCADE_ROOT_URL) + "/et/WebService/Events.asmx?wsdl");
		return s.execute("Item", "EventsSoap", new String[] { eventGuid });
	}

	/**
	 * 이벤트 정보 변경. <br/>
	 * 
	 * @return
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 12. 6.
	 * @throws Exception
	 */
	public Object update(String saToken, String itemXml) throws Exception {
		// https://p3mesdev/et/WebService/EventsTx.asmx?wsdl
		DmiService s = new DmiService(
				ResourceLoader.getInstance().get(ResourceLoader.SYNCADE_ROOT_URL) + "/et/WebService/EventsTx.asmx?wsdl");
		return s.execute("Update", "EventsTxSoap", new String[] { saToken, itemXml });
	}
}
