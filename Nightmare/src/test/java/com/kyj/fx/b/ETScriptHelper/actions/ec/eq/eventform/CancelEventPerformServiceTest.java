/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.eq.eventform
 *	작성일   : 2021. 12. 10.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.ec.eq.eventform;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.kyj.fx.nightmare.actions.ec.eq.eventform.CancelEventPerformService;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
class CancelEventPerformServiceTest {
	@Disabled
	/**
	 * Test method for
	 * {@link com.kyj.fx.nightmare.actions.ec.eq.eventform.CancelEventPerformService#cancel(java.lang.String, java.lang.String)}.
	 * 
	 * @throws Exception
	 */
	@Test
	void testCancel() throws Exception {
		var s = new CancelEventPerformService();
		s.cancel("KYJ-EQ-20201229", "Clean");
	}

}
