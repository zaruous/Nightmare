/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.rule
 *	작성일   : 2021. 12. 9.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.ec.ec.rule;

import com.kyj.fx.nightmare.comm.AbstractDVO;
import com.kyj.fx.nightmare.grid.ColumnVisible;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class GroupRuleDVO extends AbstractDVO {

	private StringProperty groupSeq = new SimpleStringProperty();
	private StringProperty groupName = new SimpleStringProperty();
	@ColumnVisible(false)
	private StringProperty groupGuid = new SimpleStringProperty();
	private StringProperty groupType = new SimpleStringProperty();

	private StringProperty ruleRank = new SimpleStringProperty();
	@ColumnVisible(false)
	private StringProperty ruleGuid = new SimpleStringProperty();
	private StringProperty ruleType = new SimpleStringProperty();
	private StringProperty ruleName = new SimpleStringProperty();
	@ColumnVisible(false)
	private StringProperty ruleDependentEventGUID = new SimpleStringProperty();
	private StringProperty ruleResultState = new SimpleStringProperty();
	private StringProperty ruleTimeInterval = new SimpleStringProperty();
	private StringProperty ruleTimeIntervalType = new SimpleStringProperty();

	private StringProperty expresionSeq = new SimpleStringProperty();
	@ColumnVisible(false)
	private StringProperty expressionGuid = new SimpleStringProperty();
	private StringProperty expressionName = new SimpleStringProperty();
	private StringProperty expressionCondition = new SimpleStringProperty();
	private StringProperty expressionConditionType = new SimpleStringProperty();
	private StringProperty epressionValue = new SimpleStringProperty();
	private StringProperty expressionDataType = new SimpleStringProperty();
	private StringProperty expressionParameterType = new SimpleStringProperty();
	private StringProperty expressionValue = new SimpleStringProperty();
	private StringProperty expressionValueText = new SimpleStringProperty();
	private StringProperty expressionValueIntevalType = new SimpleStringProperty();
	private StringProperty expressionType = new SimpleStringProperty();
	
	@ColumnVisible(false)

	public GroupRuleDVO() {
		super();
	}

	public final StringProperty groupNameProperty() {
		return this.groupName;
	}

	public final String getGroupName() {
		return this.groupNameProperty().get();
	}

	public final void setGroupName(final String groupName) {
		this.groupNameProperty().set(groupName);
	}

	public final StringProperty groupGuidProperty() {
		return this.groupGuid;
	}

	public final String getGroupGuid() {
		return this.groupGuidProperty().get();
	}

	public final void setGroupGuid(final String groupGuid) {
		this.groupGuidProperty().set(groupGuid);
	}

	public final StringProperty groupTypeProperty() {
		return this.groupType;
	}

	public final String getGroupType() {
		return this.groupTypeProperty().get();
	}

	public final void setGroupType(final String groupType) {
		this.groupTypeProperty().set(groupType);
	}

	public final StringProperty groupSeqProperty() {
		return this.groupSeq;
	}

	public final String getGroupSeq() {
		return this.groupSeqProperty().get();
	}

	public final void setGroupSeq(final String groupSeq) {
		this.groupSeqProperty().set(groupSeq);
	}

	public final StringProperty ruleGuidProperty() {
		return this.ruleGuid;
	}

	public final String getRuleGuid() {
		return this.ruleGuidProperty().get();
	}

	public final void setRuleGuid(final String ruleGuid) {
		this.ruleGuidProperty().set(ruleGuid);
	}

	public final StringProperty ruleNameProperty() {
		return this.ruleName;
	}

	public final String getRuleName() {
		return this.ruleNameProperty().get();
	}

	public final void setRuleName(final String ruleName) {
		this.ruleNameProperty().set(ruleName);
	}

	public final StringProperty ruleDependentEventGUIDProperty() {
		return this.ruleDependentEventGUID;
	}

	public final String getRuleDependentEventGUID() {
		return this.ruleDependentEventGUIDProperty().get();
	}

	public final void setRuleDependentEventGUID(final String ruleDependentEventGUID) {
		this.ruleDependentEventGUIDProperty().set(ruleDependentEventGUID);
	}

	public final StringProperty ruleResultStateProperty() {
		return this.ruleResultState;
	}

	public final String getRuleResultState() {
		return this.ruleResultStateProperty().get();
	}

	public final void setRuleResultState(final String ruleResultState) {
		this.ruleResultStateProperty().set(ruleResultState);
	}

	public final StringProperty ruleTimeIntervalProperty() {
		return this.ruleTimeInterval;
	}

	public final String getRuleTimeInterval() {
		return this.ruleTimeIntervalProperty().get();
	}

	public final void setRuleTimeInterval(final String ruleTimeInterval) {
		this.ruleTimeIntervalProperty().set(ruleTimeInterval);
	}

	public final StringProperty ruleTimeIntervalTypeProperty() {
		return this.ruleTimeIntervalType;
	}

	public final String getRuleTimeIntervalType() {
		return this.ruleTimeIntervalTypeProperty().get();
	}

	public final void setRuleTimeIntervalType(final String ruleTimeIntervalType) {
		this.ruleTimeIntervalTypeProperty().set(ruleTimeIntervalType);
	}

	public final StringProperty ruleRankProperty() {
		return this.ruleRank;
	}

	public final String getRuleRank() {
		return this.ruleRankProperty().get();
	}

	public final void setRuleRank(final String ruleRank) {
		this.ruleRankProperty().set(ruleRank);
	}

	public final StringProperty expressionGuidProperty() {
		return this.expressionGuid;
	}

	public final String getExpressionGuid() {
		return this.expressionGuidProperty().get();
	}

	public final void setExpressionGuid(final String expressionGuid) {
		this.expressionGuidProperty().set(expressionGuid);
	}

	public final StringProperty expressionNameProperty() {
		return this.expressionName;
	}

	public final String getExpressionName() {
		return this.expressionNameProperty().get();
	}

	public final void setExpressionName(final String expressionName) {
		this.expressionNameProperty().set(expressionName);
	}

	public final StringProperty expressionConditionProperty() {
		return this.expressionCondition;
	}

	public final String getExpressionCondition() {
		return this.expressionConditionProperty().get();
	}

	public final void setExpressionCondition(final String expressionCondition) {
		this.expressionConditionProperty().set(expressionCondition);
	}

	public final StringProperty epressionValueProperty() {
		return this.epressionValue;
	}

	public final String getEpressionValue() {
		return this.epressionValueProperty().get();
	}

	public final void setEpressionValue(final String epressionValue) {
		this.epressionValueProperty().set(epressionValue);
	}

	public final StringProperty expressionDataTypeProperty() {
		return this.expressionDataType;
	}

	public final String getExpressionDataType() {
		return this.expressionDataTypeProperty().get();
	}

	public final void setExpressionDataType(final String expressionDataType) {
		this.expressionDataTypeProperty().set(expressionDataType);
	}

	public final StringProperty expressionParameterTypeProperty() {
		return this.expressionParameterType;
	}

	public final String getExpressionParameterType() {
		return this.expressionParameterTypeProperty().get();
	}

	public final void setExpressionParameterType(final String expressionParameterType) {
		this.expressionParameterTypeProperty().set(expressionParameterType);
	}

	public final StringProperty expressionValueProperty() {
		return this.expressionValue;
	}

	public final String getExpressionValue() {
		return this.expressionValueProperty().get();
	}

	public final void setExpressionValue(final String expressionValue) {
		this.expressionValueProperty().set(expressionValue);
	}

	public final StringProperty expressionValueTextProperty() {
		return this.expressionValueText;
	}

	public final String getExpressionValueText() {
		return this.expressionValueTextProperty().get();
	}

	public final void setExpressionValueText(final String expressionValueText) {
		this.expressionValueTextProperty().set(expressionValueText);
	}

	public final StringProperty expressionValueIntevalTypeProperty() {
		return this.expressionValueIntevalType;
	}

	public final String getExpressionValueIntevalType() {
		return this.expressionValueIntevalTypeProperty().get();
	}

	public final void setExpressionValueIntevalType(final String expressionValueIntevalType) {
		this.expressionValueIntevalTypeProperty().set(expressionValueIntevalType);
	}

	public final StringProperty expresionSeqProperty() {
		return this.expresionSeq;
	}

	public final String getExpresionSeq() {
		return this.expresionSeqProperty().get();
	}

	public final void setExpresionSeq(final String expresionSeq) {
		this.expresionSeqProperty().set(expresionSeq);
	}

	public final StringProperty expressionConditionTypeProperty() {
		return this.expressionConditionType;
	}

	public final String getExpressionConditionType() {
		return this.expressionConditionTypeProperty().get();
	}

	public final void setExpressionConditionType(final String expressionConditionType) {
		this.expressionConditionTypeProperty().set(expressionConditionType);
	}

	public final StringProperty ruleTypeProperty() {
		return this.ruleType;
	}

	public final String getRuleType() {
		return this.ruleTypeProperty().get();
	}

	public final void setRuleType(final String ruleType) {
		this.ruleTypeProperty().set(ruleType);
	}

	public final StringProperty expressionTypeProperty() {
		return this.expressionType;
	}
	

	public final String getExpressionType() {
		return this.expressionTypeProperty().get();
	}
	

	public final void setExpressionType(final String expressionType) {
		this.expressionTypeProperty().set(expressionType);
	}
	

}
