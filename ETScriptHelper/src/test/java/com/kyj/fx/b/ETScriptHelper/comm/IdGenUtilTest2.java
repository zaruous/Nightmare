/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.comm
 *	작성일   : 2022. 4. 28.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.comm;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
class IdGenUtilTest2 {

	/**
	 * Test method for {@link com.kyj.fx.b.ETScriptHelper.comm.IdGenUtil#generate()}.
	 */
	@Test
	void testGenerate() {
		
		
		System.out.println(System.currentTimeMillis());
		System.out.println((System.currentTimeMillis() >>> 8));
		System.out.println((int)(System.currentTimeMillis() >>> 4));
		System.out.println((double) (System.currentTimeMillis() >> 8));
		
		var ret2 = IdGenUtil.generate('┐');
		System.out.println(ret2);
	}

	/**
	 * Test method for {@link com.kyj.fx.b.ETScriptHelper.comm.IdGenUtil#generate(char)}.
	 */
	@Test
	void testGenerateChar() {
		fail("Not yet implemented");
	}

}
