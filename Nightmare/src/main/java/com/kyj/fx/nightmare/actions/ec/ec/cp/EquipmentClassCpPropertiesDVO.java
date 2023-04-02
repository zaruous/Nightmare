/**
 * 
 */
package com.kyj.fx.nightmare.actions.ec.ec.cp;

import com.kyj.fx.nightmare.comm.AbstractDVO;
import com.kyj.fx.nightmare.ui.grid.ColumnWidth;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author KYJ
 *
 */
public class EquipmentClassCpPropertiesDVO extends AbstractDVO {

	@ColumnWidth(10)
	private StringProperty customPropertyGUID = new SimpleStringProperty();
	@ColumnWidth(200)
	private StringProperty name = new SimpleStringProperty();
	@ColumnWidth(50)
	private StringProperty dataType = new SimpleStringProperty();
	@ColumnWidth(200)
	private StringProperty defaultValue = new SimpleStringProperty();
	@ColumnWidth(200)
	private StringProperty description = new SimpleStringProperty();

	public final StringProperty customPropertyGUIDProperty() {
		return this.customPropertyGUID;
	}

	public final String getCustomPropertyGUID() {
		return this.customPropertyGUIDProperty().get();
	}

	public final void setCustomPropertyGUID(final String customPropertyGUID) {
		this.customPropertyGUIDProperty().set(customPropertyGUID);
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

	public final StringProperty dataTypeProperty() {
		return this.dataType;
	}

	public final String getDataType() {
		return this.dataTypeProperty().get();
	}

	public final void setDataType(final String dataType) {
		this.dataTypeProperty().set(dataType);
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

	public final StringProperty descriptionProperty() {
		return this.description;
	}

	public final String getDescription() {
		return this.descriptionProperty().get();
	}

	public final void setDescription(final String description) {
		this.descriptionProperty().set(description);
	}

}
