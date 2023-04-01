/**
 * 
 */
package com.kyj.fx.b.ETScriptHelper;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author KYJ
 *
 */
public class EquipmentEventDVO {
	private StringProperty equipmentClassGuid = new SimpleStringProperty();
	private StringProperty equipmentGuid = new SimpleStringProperty();
	private StringProperty eventGuid = new SimpleStringProperty();
	private StringProperty eventName = new SimpleStringProperty();
	private StringProperty eventType = new SimpleStringProperty();

	public EquipmentEventDVO(String equipmentClassGuid, String equipmentGuid, String eventGuid, String eventName) {
		this.equipmentClassGuid.set(equipmentClassGuid);
		this.equipmentGuid.set(equipmentGuid);
		this.eventGuid.set(eventGuid);
		this.eventName.set(eventName);
	}

	public final StringProperty equipmentGuidProperty() {
		return this.equipmentGuid;
	}

	public final String getEquipmentGuid() {
		return this.equipmentGuidProperty().get();
	}

	public final void setEquipmentGuid(final String equipmentGuid) {
		this.equipmentGuidProperty().set(equipmentGuid);
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

	public final StringProperty eventNameProperty() {
		return this.eventName;
	}

	public final String getEventName() {
		return this.eventNameProperty().get();
	}

	public final void setEventName(final String eventName) {
		this.eventNameProperty().set(eventName);
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

	public final StringProperty eventTypeProperty() {
		return this.eventType;
	}

	public final String getEventType() {
		return this.eventTypeProperty().get();
	}

	public final void setEventType(final String eventType) {
		this.eventTypeProperty().set(eventType);
	}

}
