/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.comm.service
 *	작성일   : 2021. 11. 30.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.comm.service;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.junit.jupiter.api.Test;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
class XMLUtilsTest2 {

	@Test
	void test() throws DocumentException {
		File file = new File(XMLUtilsTest2.class.getResource("sample.xml").getFile());
		Document doc = XMLUtils.load(file);
		Element rootElement = doc.getRootElement();
		rootElement.remove(rootElement.getNamespace());
		System.out.println(rootElement.asXML());
	}

}
