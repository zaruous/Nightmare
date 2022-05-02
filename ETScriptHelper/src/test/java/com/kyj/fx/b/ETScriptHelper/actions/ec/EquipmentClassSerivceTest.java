/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec
 *	작성일   : 2021. 11. 23.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.ec;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.kyj.fx.b.ETScriptHelper.actions.ec.ec.EquipmentClassesSerivce;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
@Disabled
class EquipmentClassSerivceTest {

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 23.
	 * @throws Exception
	 */
	@Test
	void itemTest() throws Exception {
		var s = new EquipmentClassesSerivce();
		var doc = s.item("56D0781D-70B7-4163-9992-BC05656AF6A0");
		System.out.println(doc.asXML());
	}

}
