/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper
 *	작성일   : 2021. 7. 9.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.kyj.fx.b.ETScriptHelper.actions.ec.ec.scripts.EquipmentScriptDVO;
import com.kyj.fx.b.ETScriptHelper.comm.Hex;
import com.kyj.fx.b.ETScriptHelper.eqtree.EquipmentDAO;
import com.kyj.fx.nightmare.DbUpdate;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
class DbUpdateTest {

	@Test
	public void uuid() {
		System.out.println(UUID.randomUUID());
	}

	/**
	 * Test method for
	 * {@link com.kyj.fx.nightmare.DbUpdate#updateScript(java.util.List)}.
	 * 
	 * @throws Exception
	 */
	@Test
	void testUpdateScript() throws Exception {
		String eventguid = "3D528FCD-2509-44F4-99C9-67AD73D7624A";
		String eventScriptGuid = "A2460A78-4171-421D-8B22-3884F188E177";
		String scriptGuid = "A2460A78-4171-421D-8B22-3884F188E177";

		EquipmentDAO equipmentDAO = new EquipmentDAO();
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("eventGuid", eventguid);
		List<Map<String, Object>> listEventParameters = equipmentDAO.listEventParameters(hashMap);
		listEventParameters.forEach(System.out::println);

		EquipmentScriptDVO equipmentScriptDVO = new EquipmentScriptDVO();
		equipmentScriptDVO.setCode("Function Event_OnComplete()\nEnd Function");
		equipmentScriptDVO.setEventScriptGUID(eventScriptGuid);
		equipmentScriptDVO.setEventGuid(eventguid);
		equipmentScriptDVO.setScriptGuid(scriptGuid);
		equipmentScriptDVO.set_status(EquipmentScriptDVO.UPDATE);
		DbUpdate.updateScript(Arrays.asList(equipmentScriptDVO));

		List<String> eventScript = equipmentDAO.getEventScript(eventguid, scriptGuid);
		eventScript.forEach(h -> {
			byte[] decode = Hex.decode(h);
			try {
				System.out.println(new String(decode, "UTF-16LE"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		});

	}
	
	
	@Test
	void testCreateScript() throws Exception {
		String eventguid = "3D528FCD-2509-44F4-99C9-67AD73D7624A";
		String eventScriptGuid = null;
		String scriptGuid = "A2460A78-4171-421D-8B22-3884F188E177";

		EquipmentDAO equipmentDAO = new EquipmentDAO();
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("eventGuid", eventguid);
		List<Map<String, Object>> listEventParameters = equipmentDAO.listEventParameters(hashMap);
		listEventParameters.forEach(System.out::println);

		EquipmentScriptDVO equipmentScriptDVO = new EquipmentScriptDVO();
		equipmentScriptDVO.setCode("Function Event_OnComplete()\nEnd Function");
		equipmentScriptDVO.setEventScriptGUID(eventScriptGuid);
		equipmentScriptDVO.setEventGuid(eventguid);
		equipmentScriptDVO.setScriptGuid(scriptGuid);
		equipmentScriptDVO.set_status(EquipmentScriptDVO.CREATE);
		DbUpdate.updateScript(Arrays.asList(equipmentScriptDVO));

		List<String> eventScript = equipmentDAO.getEventScript(eventguid, scriptGuid);
		eventScript.forEach(h -> {
			byte[] decode = Hex.decode(h);
			try {
				System.out.println(new String(decode, "UTF-16LE"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		});

	}
	
//	@Test
	public void testDeleteScript() throws Exception {
		String eventguid = "3D528FCD-2509-44F4-99C9-67AD73D7624A";
		String scriptGuid = "A2460A78-4171-421D-8B22-3884F188E177";
		EquipmentScriptDVO equipmentScriptDVO = new EquipmentScriptDVO();
		equipmentScriptDVO.setEventGuid(eventguid);
		equipmentScriptDVO.setScriptGuid(scriptGuid);
		int deleteScript = DbUpdate.deleteScript(null, Arrays.asList(equipmentScriptDVO));
		System.out.println(deleteScript);
	}

}
