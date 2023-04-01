/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.group
 *	작성일   : 2021. 12. 17.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.ec.ec.group;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;

import org.dom4j.Element;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.actions.ec.ec.group.EquipmentClassGroupDVO;
import com.kyj.fx.nightmare.actions.ec.ec.group.EquipmentClassGroupService;
import com.kyj.fx.nightmare.comm.ExcelReader;
import com.kyj.fx.nightmare.comm.IdGenUtil;
import com.kyj.fx.nightmare.comm.service.ESig;
import com.kyj.fx.nightmare.comm.service.XMLUtils;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
@Disabled
class EquipmentClassGroupServiceTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentClassGroupServiceTest.class);

	/**
	 * Test method for
	 * {@link com.kyj.fx.nightmare.actions.ec.ec.group.EquipmentClassGroupService#convertRuleGroupXml(java.util.List)}.
	 * 
	 * @throws Exception
	 */
	@Test
	void testConvertRuleGroupXml() throws Exception {
		var s = new EquipmentClassGroupService();
		var f = new File(EquipmentClassGroupServiceTest.class.getResource("20210205095242.xlsx").toURI());
		// Assert.assert(f.exists());
//		Assert.assertTrue(f.exists());
		assertTrue(f.exists());

		// String readExcelToXml = FxExcelUtil.readExcelToXml(f);
		Object readExcelToXml = s.listRuleGroup(equipmentClassGuid);
		var doc = XMLUtils.load(readExcelToXml.toString());
		LOGGER.debug(doc.asXML());
		List<EquipmentClassGroupDVO> populateList = ExcelReader.populateList(doc, EquipmentClassGroupDVO.class);

		EquipmentClassGroupDVO newData1 = new EquipmentClassGroupDVO();
		newData1.setEquipmentClassGuid(equipmentClassGuid);
		newData1.setGroupName("Test2");
		newData1.setSequence("3");
		newData1.setGroupType("0");
		populateList.add(newData1);

		Object listRuleGroup = s.listRuleGroup(equipmentClassGuid);
		doc = XMLUtils.load(listRuleGroup.toString());
		LOGGER.debug(doc.asXML());

		for (EquipmentClassGroupDVO d : populateList) {
			Element nGroupName = (Element) doc.selectSingleNode(String.format("//ListRuleGroups/RuleGroup[@Name='%s']", d.getGroupName()));
			if (nGroupName != null) {
				nGroupName.attribute("Sequence").setValue(d.getSequence());
				nGroupName.attribute("Type").setValue(d.getGroupType());
			} else {
				LOGGER.debug("new group");
				Element rootListRuleGroup = (Element) doc.selectSingleNode("//ListRuleGroups");
				Element newElement = rootListRuleGroup.addElement("RuleGroup");
				newElement.addAttribute("Type", d.getGroupType());
				newElement.addAttribute("Sequence", d.getSequence());
				newElement.addAttribute("Name", d.getGroupName());
				newElement.addAttribute("GUID", IdGenUtil.randomGuid().toUpperCase());
				newElement.addAttribute("StampGUID", IdGenUtil.randomGuid().toUpperCase());
			}
		}
		String newData = doc.asXML();
		LOGGER.debug(newData);

		String token = token();

		Object ret = s.updateRuleGroup(token, XMLUtils.escape(newData));
		LOGGER.debug("{}", ret);
	}

	private String token() throws Exception {

		ESig sig = new ESig();
		String login = "kyjun.kim";
		String password = "12";
		password = ESig.syncadeEncrypt(password);

		String permission = "5";
		String domain = "Syncade";
		String application = "DMI ET";
		String entityType = "RuleGroups";
		String entityId = "0";

		String createToken = sig.createToken(login, password, "", "", permission, domain, application, entityType, entityId, "");
		return ESig.parser().getToken(createToken);
	}

	String equipmentClassGuid = "BF560681-467A-48C8-812C-2A65E9A85DD4";

	@Test
	public void testListRuleGroup() throws Exception {

		var s = new EquipmentClassGroupService();
		Object ret = s.listRuleGroup(equipmentClassGuid);
		LOGGER.debug("{}", ret);
		assertNotEquals("", ret);
		
		// 존재하지않은 Equipment class를 호출할떄
		ret = s.listRuleGroup("12312312");
		LOGGER.debug("{}", ret);
		assertNotEquals("", ret);
		var doc = XMLUtils.load(ret.toString());
		assertTrue((doc.selectSingleNode("//faultstring") != null));
	}

}
