/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.events
 *	작성일   : 2022. 7. 20.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.ec.ec.events;

import org.junit.jupiter.api.Test;

import com.kyj.fx.nightmare.actions.ec.ec.events.EquipmentClassEventService;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
class EquipmentClassEventServiceTest {

	/**
	 * Test method for {@link com.kyj.fx.nightmare.actions.ec.ec.events.EquipmentClassEventService#item(java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	void testItem() throws Exception {
		var s = new EquipmentClassEventService();
		var retstr = s.item("A7C85028-7360-4E87-AC4C-AA1477F44E29");
		System.out.println(retstr);
	}

}
