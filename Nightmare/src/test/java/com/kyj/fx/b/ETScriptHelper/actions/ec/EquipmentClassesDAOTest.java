/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec
 *	작성일   : 2021. 11. 24.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.ec;

import org.junit.jupiter.api.Test;

import com.kyj.fx.nightmare.actions.ec.ec.EquipmentClassDVO;
import com.kyj.fx.nightmare.actions.ec.ec.EquipmentClassesDAO;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
class EquipmentClassesDAOTest {

	@Test
	void getEquipmentClassTest() {

		EquipmentClassesDAO equipmentClassesDAO = new EquipmentClassesDAO();
		EquipmentClassDVO equipmentClass = equipmentClassesDAO.getEquipmentClass("0599EA39-DD63-42D5-95C6-F6A4B2E7189A");
		System.out.println(equipmentClass);
	}

}
