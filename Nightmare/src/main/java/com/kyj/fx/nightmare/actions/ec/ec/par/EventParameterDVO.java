/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.par
 *	작성일   : 2021. 12. 27.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.ec.ec.par;

import com.kyj.fx.nightmare.comm.AbstractDVO;
import com.kyj.fx.nightmare.grid.ImportantColumn;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EventParameterDVO extends AbstractDVO {

	@ImportantColumn
	private StringProperty equipmentClassName = new SimpleStringProperty();
	@ImportantColumn
	private StringProperty equipmentEventName = new SimpleStringProperty();
	@ImportantColumn
	private StringProperty guid = new SimpleStringProperty();
	
	private StringProperty name = new SimpleStringProperty();
	private StringProperty description = new SimpleStringProperty();
	private StringProperty dataType = new SimpleStringProperty();
	private StringProperty recordType = new SimpleStringProperty();
	private StringProperty required = new SimpleStringProperty();
	private StringProperty isRepeatable = new SimpleStringProperty();
	private StringProperty defaultValue = new SimpleStringProperty();
	private StringProperty sequence = new SimpleStringProperty();

	public final StringProperty guidProperty() {
		return this.guid;
	}

	public final String getGuid() {
		return this.guidProperty().get();
	}

	public final void setGuid(final String guid) {
		this.guidProperty().set(guid);
	}

	public final StringProperty nameProperty() {
		return this.name;
	}

	public final String getName() {
		return this.nameProperty().get();
	}

	public final void setName(final String name) {
		this.nameProperty().set(name);
	}

	public final StringProperty descriptionProperty() {
		return this.description;
	}

	public final String getDescription() {
		return this.descriptionProperty().get();
	}

	public final void setDescription(final String description) {
		this.descriptionProperty().set(description);
	}

	public final StringProperty dataTypeProperty() {
		return this.dataType;
	}

	public final String getDataType() {
		return this.dataTypeProperty().get();
	}

	public final void setDataType(final String dataType) {
		this.dataTypeProperty().set(dataType);
	}

	public final StringProperty recordTypeProperty() {
		return this.recordType;
	}

	public final String getRecordType() {
		return this.recordTypeProperty().get();
	}

	public final void setRecordType(final String recordType) {
		this.recordTypeProperty().set(recordType);
	}

	public final StringProperty requiredProperty() {
		return this.required;
	}

	public final String getRequired() {
		return this.requiredProperty().get();
	}

	public final void setRequired(final String required) {
		this.requiredProperty().set(required);
	}

	public final StringProperty isRepeatableProperty() {
		return this.isRepeatable;
	}

	public final String getIsRepeatable() {
		return this.isRepeatableProperty().get();
	}

	public final void setIsRepeatable(final String isRepeatable) {
		this.isRepeatableProperty().set(isRepeatable);
	}

	public final StringProperty defaultValueProperty() {
		return this.defaultValue;
	}

	public final String getDefaultValue() {
		return this.defaultValueProperty().get();
	}

	public final void setDefaultValue(final String defaultValue) {
		this.defaultValueProperty().set(defaultValue);
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

	public final StringProperty equipmentEventNameProperty() {
		return this.equipmentEventName;
	}

	public final String getEquipmentEventName() {
		return this.equipmentEventNameProperty().get();
	}

	public final void setEquipmentEventName(final String equipmentEventName) {
		this.equipmentEventNameProperty().set(equipmentEventName);
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

}
