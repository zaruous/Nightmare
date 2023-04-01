/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.scripts
 *	작성일   : 2021. 12. 6.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.ec.ec.scripts;

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.tree.DefaultElement;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.actions.ec.ec.scripts.EquipmentClassEventScriptService;
import com.kyj.fx.nightmare.comm.ResourceLoader;
import com.kyj.fx.nightmare.comm.service.ESig;
import com.kyj.fx.nightmare.comm.service.XMLUtils;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
@Disabled
class EquipmentClassEventScriptServiceTest {

	private static Logger LOGGER = LoggerFactory.getLogger(EquipmentClassEventScriptServiceTest.class);

	/**
	 * Test method for {@link com.kyj.fx.nightmare.actions.ec.ec.scripts.EquipmentClassEventScriptService#item(java.lang.String)}.
	 * 
	 * @throws Exception
	 */
	@Test
	void testItem() throws Exception {

		var s = new EquipmentClassEventScriptService();
		// 원본 xml
		Object item = s.item("C736F3BC-A05A-487C-8000-56EFFEE87B16");
		System.out.println(item);

		StringBuffer sb = new StringBuffer();
		sb.append("Function Event_OnComplete(Data)\n");
		sb.append("\n");
		sb.append(" a = \"V\"\n");
		sb.append("End Function\n");
		String testCode = sb.toString();
		// testCode = xmlEscapeText(testCode);

		System.err.println(testCode);
		Document doc = XMLUtils.load(item.toString());

		List<Node> nodes = doc.selectNodes("/Event/ListScripts/Script");
		for (Node _e : nodes) {
			DefaultElement e = (DefaultElement) _e;
			if ("Event_OnComplete".equals(e.attribute("Name").getText())) {
				e.attribute("Code").setValue(testCode);
			}
		}

		String tokenXml = createToken();
		String itemXml = doc.getRootElement().asXML();
		
		Object ret = s.update(tokenXml, StringEscapeUtils.escapeXml(itemXml));
		LOGGER.debug("{}", ret);
	}

	String createToken() throws Exception {

		String rootUrl = ResourceLoader.getInstance().get(ResourceLoader.SYNCADE_ROOT_URL);
		LOGGER.debug("url {}", rootUrl);
		var esig = new ESig(rootUrl);
		String pwd = ESig.base64Encoder("12");
		var createToken = esig.createTokenEx("kyjun.kim", pwd, "5", "Syncade", "DMI ET", "Events", "0", "Test");
		String token = ESig.parser().getToken(createToken.toString());
		LOGGER.debug("create token result : {}", token);
		return token;
	}

	@Test
	public void escapeTest() {
		StringBuffer sb = new StringBuffer();
		sb.append("Function Event_OnComplete(Data)\n");
		sb.append("\n");
		sb.append(" a = \"V\"\n");
		sb.append("End Function\n");
		LOGGER.debug(xmlEscapeText(sb.toString()));
	}

	String xmlEscapeText(String t) {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < t.length(); i++) {
			char c = t.charAt(i);
			switch (c) {
			case '<':
				sb.append("&lt;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			case '\"':
				sb.append("&quot;");
				break;
			case '&':
				sb.append("&amp;");
				break;
			case '\'':
				sb.append("&apos;");
				break;
			case '\n':
				sb.append("&#xA;");
				break;
			default:
				if (c > 0x7e) {
					sb.append("&#" + ((int) c) + ";");
				} else
					sb.append(c);
			}
		}
		return sb.toString();
	}

}
