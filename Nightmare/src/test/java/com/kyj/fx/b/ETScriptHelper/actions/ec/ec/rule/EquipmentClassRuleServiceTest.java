/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.rule
 *	작성일   : 2021. 12. 8.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.ec.ec.rule;



import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.dom4j.Node;
import org.dom4j.tree.DefaultElement;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.kyj.fx.nightmare.actions.ec.ec.rule.EquipmentClassRuleService;
import com.kyj.fx.nightmare.comm.service.XMLUtils;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
@Disabled
class EquipmentClassRuleServiceTest {

	EquipmentClassRuleService s = new EquipmentClassRuleService();
	/**
	 * Test method for {@link com.kyj.fx.nightmare.actions.ec.ec.rule.EquipmentClassRuleService#listRule(java.lang.String)}.
	 * 
	 * @throws Exception
	 */
	@Test
	void testListRule() throws Exception {
		var s = new EquipmentClassRuleService();
		var ret = s.listRule("C877C639-4324-4957-A94B-CD36D9677DB2");
		System.out.println(ret);
	}

	
	public String getGroupRule(String equipmentClassGuid) throws Exception {
		Object listRuleGroup = s.listRuleGroup(equipmentClassGuid);
		return listRuleGroup== null ? ""  :listRuleGroup.toString();
	}
	
	@Test
	void testListRuleEmpty() throws Exception {
		String strRuleGroup = getGroupRule("C877C639-4324-4957-A94B-CD36D9677DB5");
		var doc = XMLUtils.load(strRuleGroup);
		System.out.println(doc.asXML());
		
		String emptyDataMessage = doc.selectSingleNode("//faultstring").getText();
		System.out.println(emptyDataMessage);
		
	}
	@Test
	void testListRule2() throws Exception {

		
		String strRuleGroup = getGroupRule("C877C639-4324-4957-A94B-CD36D9677DB2");
		var doc = XMLUtils.load(strRuleGroup);
		System.out.println(doc.asXML());
		
		List<Node> ruleGroups = (List<Node>) doc.selectNodes("//RuleGroup");
		assertTrue(ruleGroups.size() > 0 );
		
		List<String> sortRuleGroups = ruleGroups.stream()
				.map(v -> (DefaultElement) v)
				.sorted((e1, e2) -> {
			var a1 = e1.attribute("Sequence").getText();
			var a2 = e2.attribute("Sequence").getText();
			return Integer.compare(Integer.parseInt(a1, 10), Integer.parseInt(a2, 10));
		}).map(ele -> {
			return ele.attribute("Name").getText();
		}).collect(Collectors.toList());
		System.out.println(sortRuleGroups);
		
		
		
//		assertNotNull(sortRuleGroups);
		
		
		// test rule..
		String ruleGroupGuid = doc.selectSingleNode("//RuleGroup[@Name='CIP_N02']/@GUID").getText();
		var listRuleByGoups = s.listRuleByGroup(ruleGroupGuid);
		var docListRuleByGroups = XMLUtils.load(listRuleByGoups.toString());
		List<Node> listRuleByGroups = docListRuleByGroups.selectNodes("/RuleGroup/Rule");
		List<String> sortListRuleByGroups = listRuleByGroups.stream()
				.map(v -> (DefaultElement) v)
				.sorted((e1, e2) -> {
			var a1 = e1.attribute("RuleRank").getText();
			var a2 = e2.attribute("RuleRank").getText();
			return Integer.compare(Integer.parseInt(a1, 10), Integer.parseInt(a2, 10));
		}).map(ele -> {
			return ele.attribute("Name").getText();
		}).collect(Collectors.toList());
		System.out.println(sortListRuleByGroups);

		String testRuleName = "Set CIP_N02 as True";
		List<Node> expressions = (List<Node>) docListRuleByGroups
				.selectNodes(String.format("//RuleGroup/Rule[@Name='%s']/Expression", testRuleName));

		List<String> sortedExpressions = expressions.stream()
				.map(v -> (DefaultElement) v)
				.sorted((e1, e2) -> {
			var a1 = e1.attribute("Sequence").getText();
			var a2 = e2.attribute("Sequence").getText();
			return Integer.compare(Integer.parseInt(a1, 10), Integer.parseInt(a2, 10));
		}).map(ele -> {
			String name = ele.attribute("Name").getText();
			String condition = ele.attribute("Condition").getText();
			String value = ele.attribute("Value").getText();
			String valueIntervalType = ele.attribute("ValueIntervalType").getText();

			return String.format("%s \t %s \t %s \t %s", name, condition, value, valueIntervalType);
		})
		.collect(Collectors.toList());
		
		System.out.println(String.format("%s \t %s \t %s \t %s", "Name", "Condition", "Value", "ValueIntervalType"));
		sortedExpressions.forEach(System.out::println);
	}
	
	
	@Test
	public void ruleXmlTest() throws Exception {
		String out = (String) s.ruleXml("40A47406-6AF7-4FA8-8D13-0A31E3E59AC7");
		System.out.println(out);
	}
}
