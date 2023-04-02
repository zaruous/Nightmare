/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.eqtree
 *	작성일   : 2021. 12. 10.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.ui.tree.eqtree;

import com.kyj.fx.nightmare.comm.AbstractDVO;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class EquipmentEventsDVO extends AbstractDVO {
	private StringProperty eventExecuteGuid = new SimpleStringProperty();
	private StringProperty equipmentClassGuid = new SimpleStringProperty();
	private StringProperty equipmentGuid = new SimpleStringProperty();
	private StringProperty equipmentEventGuid = new SimpleStringProperty();
	private StringProperty equipmentName = new SimpleStringProperty();
	private StringProperty eventName = new SimpleStringProperty();
	private StringProperty eventGuid = new SimpleStringProperty();

	public EquipmentEventsDVO() {
	}

	public final StringProperty eventExecuteGuidProperty() {
		return this.eventExecuteGuid;
	}

	public final String getEventExecuteGuid() {
		return this.eventExecuteGuidProperty().get();
	}

	public final void setEventExecuteGuid(final String eventExecuteGuid) {
		this.eventExecuteGuidProperty().set(eventExecuteGuid);
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

	public final StringProperty equipmentGuidProperty() {
		return this.equipmentGuid;
	}

	public final String getEquipmentGuid() {
		return this.equipmentGuidProperty().get();
	}

	public final void setEquipmentGuid(final String equipmentGuid) {
		this.equipmentGuidProperty().set(equipmentGuid);
	}

	public final StringProperty equipmentEventGuidProperty() {
		return this.equipmentEventGuid;
	}

	public final String getEquipmentEventGuid() {
		return this.equipmentEventGuidProperty().get();
	}

	public final void setEquipmentEventGuid(final String equipmentEventGuid) {
		this.equipmentEventGuidProperty().set(equipmentEventGuid);
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