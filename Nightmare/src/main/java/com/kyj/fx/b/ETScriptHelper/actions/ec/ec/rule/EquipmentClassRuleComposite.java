/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.rule
 *	작성일   : 2021. 12. 8.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.ec.ec.rule;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.tree.DefaultElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.b.ETScriptHelper.actions.comm.core.AbstractManagementBorderPane;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnExcelTableViewList;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnLoadEquipmentClass;
import com.kyj.fx.b.ETScriptHelper.comm.DialogUtil;
import com.kyj.fx.b.ETScriptHelper.comm.FxUtil;
import com.kyj.fx.b.ETScriptHelper.comm.JsonFormatter;
import com.kyj.fx.b.ETScriptHelper.comm.ValueUtil;
import com.kyj.fx.b.ETScriptHelper.comm.service.XMLUtils;
import com.kyj.fx.b.ETScriptHelper.grid.AnnotationOptions;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EquipmentClassRuleComposite extends AbstractManagementBorderPane<EquipmentClassRuleDVO>
		implements OnLoadEquipmentClass, OnExcelTableViewList {

	private static Logger LOGGER = LoggerFactory.getLogger(EquipmentClassRuleComposite.class);

	@FXML
	private TableView<GroupRuleDVO> tvRuleData;
	@FXML
	public TableView<GroupDVO> tvGroup;
	@FXML
	public TableView<RuleDVO> tvRules;
	@FXML
	public TableView<ExpressionDVO> tvExpression;

	private EquipmentClassRuleService equipmentClassRuleService = new EquipmentClassRuleService();

	/**
	 * @param fxml
	 */
	public EquipmentClassRuleComposite() {
		super(EquipmentClassRuleComposite.class.getResource("EquipmentClassRuleView.fxml"));
	}

	@Override
	public void reload() {
		onLoadEquipmentClass(this.equipmentClass.get());
	}

	@FXML
	public void initialize() {
		FxUtil.installCommonsTableView(GroupRuleDVO.class, tvRuleData, new AnnotationOptions<GroupRuleDVO>(GroupRuleDVO.class) {

			@Override
			public int columnSize(String columnName) {
				if ("ruleName".equals(columnName))
					return 180;
				return super.columnSize(columnName);
			}

			// @SuppressWarnings("unchecked")
			// @Override
			// public TableColumn<GroupRuleDVO, String> customTableColumn(String columnName) {
			// return super.customTableColumn(columnName);
			// }

			// @Override
			// public String convert(String columnName) {
			// String h = super.convert(columnName);
			// return h.replace("expression", "");
			// }
		});
		MenuItem miViewJson = new MenuItem("View Json");
		miViewJson.setOnAction(ev -> {
			GroupRuleDVO selectedItem = tvRuleData.getSelectionModel().getSelectedItem();
			if (selectedItem == null)
				return;
			String m = ValueUtil.toJSONString(selectedItem);

			FxUtil.createStageAndShow(new TextArea(new JsonFormatter().format(m)), stage -> {
				stage.setWidth(600);
				stage.setHeight(600);
			});
		});

		tvRuleData.setContextMenu(new ContextMenu(miViewJson));

		tvRuleData.getSelectionModel().setCellSelectionEnabled(true);

		FxUtil.installCommonsTableView(GroupDVO.class, tvGroup, new AnnotationOptions<GroupDVO>(GroupDVO.class) {
			@Override
			public int columnSize(String columnName) {
				if ("ruleName".equals(columnName))
					return 180;
				return super.columnSize(columnName);
			}
		});

		FxUtil.installCommonsTableView(RuleDVO.class, tvRules, new AnnotationOptions<RuleDVO>(RuleDVO.class) {
			@Override
			public int columnSize(String columnName) {
				if ("ruleName".equals(columnName))
					return 180;
				return super.columnSize(columnName);
			}
		});
		FxUtil.installCommonsTableView(ExpressionDVO.class, tvExpression, new AnnotationOptions<ExpressionDVO>(ExpressionDVO.class));

		this.equipmentClass.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String newArgs) {

				if (ValueUtil.isEmpty(newArgs)) {
					tvGroup.getItems().clear();
					tvRules.getItems().clear();
					tvExpression.getItems().clear();
					return;
				}

				try {
					var ret = equipmentClassRuleService.listRule(newArgs);
					if (ret != null) {
						String xml = ret.toString();
						Document doc = XMLUtils.load(xml);
						var nFault = doc.selectSingleNode("//faultstring");
						if (nFault != null) {
							String msg = nFault.getText();
							DialogUtil.showMessageDialog(String.format("Data not found\n%s", msg));
							return;
						}

						List<Node> ruleGroups = doc.selectNodes("/ListRules/RuleGroup");
						ruleGroups = ruleGroups.stream()
								.map(v -> (DefaultElement) v)
								.sorted(ruleGroupComparator).collect(Collectors.toList());

						var array = new ArrayList<GroupRuleDVO>();
						for (Node _ruleGroup : ruleGroups) {
							DefaultElement ruleGroup = (DefaultElement) _ruleGroup;
							String groupName = ruleGroup.attributeValue("Name");
							String groupSequence = ruleGroup.attributeValue("Sequence");
							String groupGuid = ruleGroup.attributeValue("GUID");
							String groupType = ruleGroup.attributeValue("Type");

							List<Node> rules = ruleGroup.selectNodes("./Rule");
							rules = rules.stream().sorted(ruleComparator).collect(Collectors.toList());

							for (Node _rule : rules) {
								DefaultElement rule = (DefaultElement) _rule;
								/*
								<Rule GUID="582D4C28-9885-4C97-BC14-384A21D664CE" RuleType="1" Name="Set SIP_TM_TP_Expired as False" 
								DependentEventGUID="CBD9711C-DD11-4AD9-A447-800CC9C5DE8C" ResultState="2" TimeInterval="1" TimeIntervalType="0" 
								RuleRank="10">
								 * */
								String ruleGuid = rule.attributeValue("GUID");
								String ruleRuleType = rule.attributeValue("RuleType");
								String ruleName = rule.attributeValue("Name");
								String ruleDependentEventGUID = rule.attributeValue("DependentEventGUID");
								String ruleResultState = rule.attributeValue("ResultState");
								String ruleTimeInterval = rule.attributeValue("TimeInterval");
								String ruleTimeIntervalType = rule.attributeValue("TimeIntervalType");
								String ruleRank = rule.attributeValue("RuleRank");

								List<Node> expressions = rule.selectNodes("./Expression");
								expressions = expressions.stream()
										.map(v -> (DefaultElement)v)
										.sorted(expressionComparator).collect(Collectors.toList());

								for (Node _expression : expressions) {
									DefaultElement expression = (DefaultElement) _expression;
									String expressionName = expression.attributeValue("Name");
									String expressionSequence = expression.attributeValue("Sequence");

									String expressionGUID = expression.attributeValue("GUID");
									String expressionConditionType = expression.attributeValue("ConditionType");
									String expressionParameterType = expression.attributeValue("ParameterType");
									String expressionCondition = expression.attributeValue("Condition");
									String expressionValue = expression.attributeValue("Value");
									String expressionValueText = expression.attributeValue("ValueText");
									String expressionValueIntervalType = expression.attributeValue("ValueIntervalType");
									String expressionDataType = expression.attributeValue("DataType");
									String expressionType = expression.attributeValue("ExpressionType");

									// String t = String.format("[%s][%s][%s][%s]", expressionName, expressionCondition, expressionValue,
									// expressionValueIntervalType);
									//
									// System.out.println(
									// String.format("[%s][%s]\t[%s][%s]\t%s", groupSequence, groupName, ruleRank, ruleName, t));

									var retVal = new GroupRuleDVO();
									retVal.setGroupName(groupName);
									retVal.setGroupSeq(groupSequence);
									retVal.setGroupGuid(groupGuid);
									retVal.setGroupType(groupType);

									retVal.setRuleGuid(ruleGuid);
									retVal.setRuleType(ruleRuleType);
									retVal.setRuleName(ruleName);
									retVal.setRuleDependentEventGUID(ruleDependentEventGUID);
									retVal.setRuleResultState(ruleResultState);
									retVal.setRuleTimeInterval(ruleTimeInterval);
									retVal.setRuleTimeIntervalType(ruleTimeIntervalType);
									retVal.setRuleRank(ruleRank);

									retVal.setExpressionGuid(expressionGUID);
									retVal.setExpressionName(expressionName);
									retVal.setExpressionCondition(expressionCondition);
									retVal.setExpressionConditionType(expressionConditionType);
									retVal.setExpressionValue(expressionValue);
									retVal.setExpressionDataType(expressionDataType);
									retVal.setExpressionParameterType(expressionParameterType);
									retVal.setExpressionValue(expressionValue);
									retVal.setExpressionValueText(expressionValueText);
									retVal.setExpressionValueIntevalType(expressionValueIntervalType);
									retVal.setExpresionSeq(expressionSequence);
									retVal.setExpressionType(expressionType);
									array.add(retVal);
								}
							}
						}
						tvRuleData.getItems().setAll(array);
					}
				} catch (Exception e) {
					showErrorMessage(e);
				}
			}

			// private void test(Document doc) throws Exception {
			// XmlDataHander<GroupDVO> handler = (GroupDVO dvo, String nodeName, String attrValue) -> {
			// switch (nodeName) {
			// case "Name":
			// dvo.setName(attrValue);
			// break;
			// case "GUID":
			// dvo.setGroupGuid(attrValue);
			// break;
			// case "Type":
			// dvo.setGroupType(attrValue);
			// break;
			// }
			// };
			// List<GroupDVO> populate = XMLUtils.populateXmlElement(doc, "/ListRules/RuleGroup", GroupDVO.class, handler);
			// tvGroup.getItems().setAll(populate);
			// }
		});

		this.tvRuleData.getSelectionModel().selectedItemProperty().addListener((oba, o, n) -> {
			if (n == null)
				return;
			try {
				Method[] declaredMethods = n.getClass().getDeclaredMethods();
				var groupDVO = new GroupDVO();
				var ruleDVO = new RuleDVO();

				for (Method m : declaredMethods) {
					String methodName = m.getName();
					if (methodName.startsWith("getGroup")) {
						m.setAccessible(true);
						var val = m.invoke(n);

						try {
							Method setterMethod = GroupDVO.class.getDeclaredMethod(methodName.replace("getGroup", "setGroup"),
									String.class);
							setterMethod.setAccessible(true);
							setterMethod.invoke(groupDVO, val);
						} catch (NoSuchMethodException ex) {
							/*IGNORE.*/}

					} else if (methodName.startsWith("getRule")) {

						m.setAccessible(true);
						var val = m.invoke(n);

						try {
							Method setterMethod = RuleDVO.class.getDeclaredMethod(methodName.replace("getRule", "setRule"), String.class);
							setterMethod.setAccessible(true);
							setterMethod.invoke(ruleDVO, val);
						} catch (NoSuchMethodException ex) {
							/*IGNORE.*/}
					}

				}

				String ruleDependentEventGUID = ruleDVO.getRuleDependentEventGUID();

				String ruleXmlDoc = equipmentClassRuleService.ruleXml(ruleDependentEventGUID).toString();
				Document docRuleXml = XMLUtils.load(ruleXmlDoc);

				List<ExpressionDVO> expressions = this.tvRuleData.getItems().stream().filter(v -> {

					if (ValueUtil.equals(groupDVO.getGroupName(), v.getGroupName())
							&& ValueUtil.equals(ruleDVO.getRuleName(), v.getRuleName()))
						return true;

					return false;
				}).map(v -> {

					var exp = new ExpressionDVO();

					Attribute eName = (Attribute) docRuleXml
							.selectSingleNode(String.format("//Joins/Expression[@ExpressionType='%s' and @ConditionType='%s']/@Name",
									v.getExpressionType(), v.getExpressionConditionType()));
					if (eName != null) {
						exp.setExpressionName(eName.getText());
					} else {
						exp.setExpressionName(v.getExpressionName());
					}

					String format = String.format(
							"/Rule/ConditionTypes/ConditionType[@Type='%s']/ParameterType[@Type='%s']/Operator[@Condition=\"%s\"]/@Text",
							v.getExpressionConditionType(), v.getExpressionParameterType(), v.getExpressionCondition());

					LOGGER.debug(format);
					Attribute aOperator = (Attribute) docRuleXml.selectSingleNode(format);
					exp.setExpressionCondition(aOperator == null ? "" : aOperator.getText());

					exp.setExpressionValueText(v.getExpressionValueText());
					exp.setExpressionValueIntevalType(v.getExpressionValueIntevalType());
					return exp;
				}).collect(Collectors.toList());

				tvGroup.getItems().setAll(Arrays.asList(groupDVO));
				tvRules.getItems().setAll(Arrays.asList(ruleDVO));
				tvExpression.getItems().setAll(expressions);

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		});
	}

	Comparator<Element> ruleGroupComparator = new Comparator<Element>() {
		@Override
		public int compare(Element o1, Element o2) {
			var v1 = o1.attributeValue("Sequence");
			var v2 = o2.attributeValue("Sequence");
			return Integer.compare(Integer.parseInt(v1, 10), Integer.parseInt(v2, 10));
		}
	};

	Comparator<Node> ruleComparator = new Comparator<Node>() {
		@Override
		public int compare(Node o1, Node o2) {
			
			var v1 = ((DefaultElement)o1).attributeValue("RuleRank");
			var v2 = ((DefaultElement)o2).attributeValue("RuleRank");
			return Integer.compare(Integer.parseInt(v1, 10), Integer.parseInt(v2, 10));
		}
	};

	Comparator<Element> expressionComparator = new Comparator<Element>() {
		@Override
		public int compare(Element o1, Element o2) {
			var v1 = o1.attributeValue("Sequence");
			var v2 = o2.attributeValue("Sequence");
			return Integer.compare(Integer.parseInt(v1, 10), Integer.parseInt(v2, 10));
		}
	};

	private StringProperty equipmentClass = new SimpleStringProperty();

	@Override
	public void onLoadEquipmentClass(String equipmentClassGuid) {
		this.equipmentClass.set("");
		if (ValueUtil.isNotEmpty(equipmentClassGuid))
			this.equipmentClass.set(equipmentClassGuid);
	}

	@Override
	public List<TableView> excelTableList() {
		return null;
	}

	@Override
	public void importExcel(File fromFile, Document doc) {
		// TODO Auto-generated method stub

	}

	@Override
	public String onDeployItem(String name, List<EquipmentClassRuleDVO> items) {

		return null;
	}

}
