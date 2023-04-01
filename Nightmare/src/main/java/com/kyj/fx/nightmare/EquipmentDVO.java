/**
 * 
 */
package com.kyj.fx.nightmare;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author KYJ
 *
 */
public class EquipmentDVO {

	private StringProperty equipmentGuid = new SimpleStringProperty();
	private StringProperty equipmentName = new SimpleStringProperty();
	private StringProperty equipmentClassName = new SimpleStringProperty();

	public EquipmentDVO(String equipmentGuid, String equipmentName) {
		super();
		this.equipmentGuid.set(equipmentGuid);
		this.equipmentName.set(equipmentName);
	}

	public EquipmentDVO(String name) {
		this.equipmentName.set(name);
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

	@Override
	public String toString() {
		return equipmentName.get();
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

	public final StringProperty equipmentClassNameProperty() {
		return this.equipmentClassName;
	}

	public final String getEquipmentClassName() {
		return this.equipmentClassNameProperty().get();
	}

	public final void setEquipmentClassName(final String equipmentClassName) {
		this.equipmentClassNameProperty().set(equipmentClassName);
	}

}
