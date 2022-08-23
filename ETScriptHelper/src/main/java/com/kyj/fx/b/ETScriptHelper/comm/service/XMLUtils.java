/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.comm.service
 *	작성일   : 2021. 11. 23.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.comm.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.text.StringEscapeUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;
import org.xml.sax.SAXException;


//import org.xml.sax.SAXException;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class XMLUtils {

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 23.
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static Document load(String str) throws Exception {
		if (str == null)
			throw new NullPointerException("str is null");
		return load(str.getBytes("utf-8"));
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 3. 2.
	 * @param data
	 * @param encoding
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static Document load(byte[] data) throws Exception {
		SAXReader reader = new SAXReader();
		Document document = reader.read(new ByteArrayInputStream(data));
		return document;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 30.
	 * @param file
	 * @return
	 * @throws DocumentException
	 */
	public static Document load(File file) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(file);
		return document;
	}

	/**
	 * 
	 * Xpath를 활용해 XML 문서의 Element를 탐색한 후 할당된 어트리뷰트의 이름값을 <br/>
	 * 자바 빈에 할당하기 위한 목적 <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 9.
	 * @param <T>
	 * @param doc
	 * @param xpath
	 * @param instanceClass
	 * @param handler
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public static <T> List<T> populateXmlElement(Document doc, String xpath, Class<T> instanceClass, XmlDataHander<T> handler)
			throws Exception {
		List<Node> elements = doc.selectNodes(xpath);
		var ret = new ArrayList<T>(elements.size());

		for (Node e : elements) {
			Constructor<T> constructor = instanceClass.getDeclaredConstructor();
			T instance = constructor.newInstance();
			Iterator<Attribute> it = ((DefaultElement) e).attributeIterator();
			while (it.hasNext()) {
				Attribute next = it.next();
				String name = next.getName();
				String value = next.getStringValue();
				handler.handle(instance, name, value);
			}
			ret.add(instance);
		}

		return ret;
	}

	/**
	 * XML에 속한 데이터를 자바 빈으로 할당하기 위한 객체.
	 * 
	 * @author KYJ (callakrsos@naver.com)
	 *
	 * @param <T>
	 */
	@FunctionalInterface
	public interface XmlDataHander<T> {

		/**
		 * @작성자 : KYJ (callakrsos@naver.com)
		 * @작성일 : 2021. 12. 9.
		 * @param instance
		 * @param nodeName
		 * @param nodeValue
		 */
		public void handle(T instance, String nodeName, String nodeValue);

	}

	/**
	 * escape <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 10.
	 * @param str
	 * @return
	 */
	public static String escape(String str) {
//		StringEscapeUtils.escapeXml11(str)
		return StringEscapeUtils.escapeXml11(str);
	}
}
