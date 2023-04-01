/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.events
 *	작성일   : 2021. 11. 30.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.ec.ec.events;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.kyj.fx.nightmare.actions.ec.ec.events.EquipmentClassEventDAO;
import com.kyj.fx.nightmare.actions.ec.ec.events.EtEventsDVO;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
class EquipmentClassEventDAOTest {

	/**
	 * Test method for
	 * {@link com.kyj.fx.nightmare.actions.ec.ec.events.EquipmentClassEventDAO#getEvent()}.
	 */
	@Test
	void testGetEvent() {
		/*
		 *
		 */
		var dao = new EquipmentClassEventDAO();
		List<EtEventsDVO> event = dao.getEvents("BF560681-467A-48C8-812C-2A65E9A85DD4");
		event.forEach(System.out::println);
	}

}
