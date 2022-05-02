/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.comm.service
 *	작성일   : 2021. 11. 29.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.comm.service;

import org.dom4j.Document;
import org.junit.jupiter.api.Test;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
class XMLUtilsTest {

	@Test
	void escapeTest() throws Exception {
		
		Document doc = XMLUtils.load("<Person><Name/></Person>");
		doc.selectSingleNode("/Person/Name").setText("<Age>13</Age>");
		System.out.println(doc.asXML());
	}

}
