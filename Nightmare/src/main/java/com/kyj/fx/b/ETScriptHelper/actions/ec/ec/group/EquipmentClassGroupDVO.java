/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.group
 *	작성일   : 2021. 12. 17.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.ec.ec.group;

import com.kyj.fx.b.ETScriptHelper.comm.AbstractDVO;
import com.kyj.fx.b.ETScriptHelper.grid.ColumnVisible;
import com.kyj.fx.b.ETScriptHelper.grid.ImportantColumn;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EquipmentClassGroupDVO extends AbstractDVO {

	@ColumnVisible(false)
	private StringProperty ruleGroupGuid = new SimpleStringProperty();
	@ColumnVisible(false)
	private StringProperty equipmentClassGuid = new SimpleStringProperty();

	@ImportantColumn
	private StringProperty equipmentClassName = new SimpleStringProperty();
	@ImportantColumn
	private StringProperty groupName = new SimpleStringProperty();
	private StringProperty groupType = new SimpleStringProperty();
	private StringProperty groupTypeName = new SimpleStringProperty();
	private StringProperty sequence = new SimpleStringProperty();
	@ColumnVisible(false)
	private StringProperty stampGuid = new SimpleStringProperty();

	public EquipmentClassGroupDVO() {
		super();
	}

	public final StringProperty ruleGroupGuidProperty() {
		return this.ruleGroupGuid;
	}

	public final String getRuleGroupGuid() {
		return this.ruleGroupGuidProperty().get();
	}

	public final void setRuleGroupGuid(final String ruleGroupGuid) {
		this.ruleGroupGuidProperty().set(ruleGroupGuid);
	}

	public final StringProperty equipmentClassGuidProperty() {
		return this.equipmentClassGuid;
	}

	public final String getEquipmentClassGuid() {
		return this.equipmentClassGuidProperty().get();
	}

	public final void setEquipmentClassGuid(final String equipmentClassGuid) {
		this.equipmentClassGuidProperty().set(equipmentClassGuid);
	}

	public final StringProperty sequenceProperty() {
		return this.sequence;
	}

	public final String getSequence() {
		return this.sequenceProperty().get();
	}

	public final void setSequence(final String sequence) {
		this.sequenceProperty().set(sequence);
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

	public final StringProperty groupTypeProperty() {
		return this.groupType;
	}

	public final String getGroupType() {
		return this.groupTypeProperty().get();
	}

	public final void setGroupType(final String groupType) {
		this.groupTypeProperty().set(groupType);
	}

	public final StringProperty equipmentClassNameProperty() {
		return this.equipmentClassName;
	}

	public final String getEquipmentClassName() {
		return this.equipmentClassNameProperty().get();
	}

	public final void setEquipmentClassName(final String equipmentClassName) {
		this.equipmentClassNameProperty().set(equipmentClassName);
	}

	public final StringProperty stampGuidProperty() {
		return this.stampGuid;
	}

	public final String getStampGuid() {
		return this.stampGuidProperty().get();
	}

	public final void setStampGuid(final String stampGuid) {
		this.stampGuidProperty().set(stampGuid);
	}

	public final StringProperty groupTypeNameProperty() {
		return this.groupTypeName;
	}
	

	public final String getGroupTypeName() {
		return this.groupTypeNameProperty().get();
	}
	

	public final void setGroupTypeName(final String groupTypeName) {
		this.groupTypeNameProperty().set(groupTypeName);
	}
	

}
