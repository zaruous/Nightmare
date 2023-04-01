/**
 * 
 */
package com.kyj.fx.nightmare.actions.ec.ec.scripts;

import com.kyj.fx.nightmare.comm.AbstractDVO;
import com.kyj.fx.nightmare.grid.ImportantColumn;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author KYJ
 *
 */
public class EquipmentScriptDVO extends AbstractDVO {
	private StringProperty equipmentClassGuid = new SimpleStringProperty();
	private StringProperty eventScriptGUID = new SimpleStringProperty();
	@ImportantColumn
	private StringProperty equipmentClassName = new SimpleStringProperty();
	
	private StringProperty equipmentName = new SimpleStringProperty();
	@ImportantColumn
	private StringProperty eventName = new SimpleStringProperty();
	private StringProperty actionName = new SimpleStringProperty();
	private StringProperty code = new SimpleStringProperty();
	private StringProperty scriptGuid = new SimpleStringProperty();
	private StringProperty eventGuid = new SimpleStringProperty();

	/**
	 */
	public EquipmentScriptDVO() {
		super();
	}

	public EquipmentScriptDVO(String equipmentClassGuid, String eventScriptGUID, String actionName, String code) {
		super();
		this.equipmentClassGuid.set(equipmentClassGuid);
		this.eventScriptGUID.set(eventScriptGUID);
		this.actionName.set(actionName);
		this.code.set(code);
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

	public final StringProperty eventScriptGUIDProperty() {
		return this.eventScriptGUID;
	}

	public final String getEventScriptGUID() {
		return this.eventScriptGUIDProperty().get();
	}

	public final void setEventScriptGUID(final String eventScriptGUID) {
		this.eventScriptGUIDProperty().set(eventScriptGUID);
	}

	public final StringProperty codeProperty() {
		return this.code;
	}

	public final String getCode() {
		return this.codeProperty().get();
	}

	public final void setCode(final String code) {
		this.codeProperty().set(code);
	}

	public final StringProperty actionNameProperty() {
		return this.actionName;
	}

	public final String getActionName() {
		return this.actionNameProperty().get();
	}

	public final void setActionName(final String actionName) {
		this.actionNameProperty().set(actionName);
	}

	public final StringProperty equipmentNameProperty() {
		return this.equipmentName;
	}

	public final String getEquipmentName() {
		return this.equipmentNameProperty().get();
	}

	public final void setEquipmentName(final String equipmentName) {
		this.equipmentNameProperty().set(equipmentName);
	}

	public final StringProperty eventNameProperty() {
		return this.eventName;
	}

	public final String getEventName() {
		return this.eventNameProperty().get();
	}

	public final void setEventName(final String eventName) {
		this.eventNameProperty().set(eventName);
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

	public final StringProperty scriptGuidProperty() {
		return this.scriptGuid;
	}

	public final String getScriptGuid() {
		return this.scriptGuidProperty().get();
	}

	public final void setScriptGuid(final String scriptGuid) {
		this.scriptGuidProperty().set(scriptGuid);
	}

	public final StringProperty eventGuidProperty() {
		return this.eventGuid;
	}

	public final String getEventGuid() {
		return this.eventGuidProperty().get();
	}

	public final void setEventGuid(final String eventGuid) {
		this.eventGuidProperty().set(eventGuid);
	}

}
