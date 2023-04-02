/**
 * 
 */
package com.kyj.fx.nightmare.ui.tree.eqtree;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 장비 클래스를 정보를 보여줌. <br/>
 * 
 * @author KYJ
 *
 */
public class EquipmentClassDVO {

	private StringProperty equipmentClassGuid = new SimpleStringProperty();
	private StringProperty equipmentClassName = new SimpleStringProperty();

	public EquipmentClassDVO(String equipmentClassGuid, String equipmentClassName) {
		super();
		this.equipmentClassGuid.set(equipmentClassGuid);
		this.equipmentClassName.set(equipmentClassName);
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
