/**
 * 
 */
package com.kyj.fx.nightmare.actions.ec.eq.cp;

import com.kyj.fx.nightmare.comm.AbstractDVO;
import com.kyj.fx.nightmare.grid.ColumnWidth;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author KYJ
 *
 */
public class EquipmentCpPropertiesDVO extends AbstractDVO{

	@ColumnWidth(200)
	private StringProperty name = new SimpleStringProperty();
	@ColumnWidth(200)
	private StringProperty value = new SimpleStringProperty();
	public final StringProperty nameProperty() {
		return this.name;
	}
	
	public final String getName() {
		return this.nameProperty().get();
	}
	
	public final void setName(final String name) {
		this.nameProperty().set(name);
	}
	
	public final StringProperty valueProperty() {
		return this.value;
	}
	
	public final String getValue() {
		return this.valueProperty().get();
	}
	
	public final void setValue(final String value) {
		this.valueProperty().set(value);
	}
	
	
	
}
