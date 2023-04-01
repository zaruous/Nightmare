/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.comm
 *	작성일   : 2021. 12. 8.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.comm;

import org.junit.jupiter.api.Test;

import com.kyj.fx.nightmare.comm.IdGenUtil;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
class IdGenUtilTest {

	/**
	 * Test method for {@link com.kyj.fx.nightmare.comm.IdGenUtil#randomGuid()}.
	 */
	@Test
	void testRandomGuid() {
		String ret = IdGenUtil.randomGuid();
		System.out.println(ret.toUpperCase());
		// 2F1ED59A-6028-4AFA-B630-72BADA24CD03
		// 131F0331-97CC-4D7B-958E-DE36D0E8ACF6
	}

}
