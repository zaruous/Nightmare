/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec
 *	작성일   : 2021. 11. 23.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.ec.eq.states;

import com.kyj.fx.b.ETScriptHelper.comm.AbstractDVO;
import com.kyj.fx.b.ETScriptHelper.grid.ColumnVisible;
import com.kyj.fx.b.ETScriptHelper.grid.ColumnWidth;
import com.kyj.fx.b.ETScriptHelper.grid.UseCommonClick;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
@UseCommonClick(false)
public class EquipmentEventStateDVO extends AbstractDVO {

	@ColumnWidth(250)
	private StringProperty equipmentClassName;
	@ColumnWidth(250)
	private StringProperty equipmentName;
	@ColumnVisible(false)
	@ColumnWidth(250)
	private StringProperty equipmenGuid;

	@ColumnVisible(false)
	private StringProperty equipmentEventGUID;
	@ColumnVisible(false)
	private StringProperty eventGUID;
	@ColumnWidth(150)
	private StringProperty name;
	@ColumnWidth(200)
	private StringProperty description;
	private StringProperty eventTypeNm;
	
	private StringProperty state;
	private StringProperty stateName;

	@ColumnVisible(false)
	private StringProperty eventType;
	@ColumnVisible(false)
	private StringProperty trueStateText;
	@ColumnVisible(false)
	private StringProperty falseStateText;

	public EquipmentEventStateDVO() {

		this.equipmentClassName = new SimpleStringProperty();
		this.equipmentName = new SimpleStringProperty();
		this.equipmenGuid = new SimpleStringProperty();
		this.equipmentEventGUID = new SimpleStringProperty();
		this.eventGUID = new SimpleStringProperty();
		this.name = new SimpleStringProperty();
		this.description = new SimpleStringProperty();
		this.eventTypeNm = new SimpleStringProperty();
		this.eventType = new SimpleStringProperty();
		this.trueStateText = new SimpleStringProperty();
		this.falseStateText = new SimpleStringProperty();
		this.state = new SimpleStringProperty();
		this.stateName = new SimpleStringProperty();
	}

	public String getEquipmenGuid() {
		return equipmenGuid.get();
	}

	public void setEquipmenGuid(String equipmenGuid) {
		this.equipmenGuid.set(equipmenGuid);
	}

	public String getEquipmentClassName() {
		return equipmentClassName.get();
	}

	public void setEquipmentClassName(String equipmentClassName) {
		this.equipmentClassName.set(equipmentClassName);
	}

	public String getEquipmentName() {
		return equipmentName.get();
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName.set(equipmentName);
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public String getName() {
		return name.get();
	}

	public StringProperty nameProperty() {
		return name;
	}

	public void setDescription(String description) {
		this.description.set(description);
	}

	public String getDescription() {
		return description.get();
	}

	public StringProperty descriptionProperty() {
		return description;
	}

	public void setEventTypeNm(String eventTypeNm) {
		this.eventTypeNm.set(eventTypeNm);
	}

	public String getEventTypeNm() {
		return eventTypeNm.get();
	}

	public StringProperty eventTypeNmProperty() {
		return eventTypeNm;
	}

	public void setEventType(String eventType) {
		this.eventType.set(eventType);
	}

	public String getEventType() {
		return eventType.get();
	}

	public StringProperty eventTypeProperty() {
		return eventType;
	}

	public void setTrueStateText(String trueStateText) {
		this.trueStateText.set(trueStateText);
	}

	public String getTrueStateText() {
		return trueStateText.get();
	}

	public StringProperty trueStateTextProperty() {
		return trueStateText;
	}

	public void setFalseStateText(String falseStateText) {
		this.falseStateText.set(falseStateText);
	}

	public String getFalseStateText() {
		return falseStateText.get();
	}

	public StringProperty falseStateTextProperty() {
		return falseStateText;
	}

	public final StringProperty equipmentEventGUIDProperty() {
		return this.equipmentEventGUID;
	}

	public final String getEquipmentEventGUID() {
		return this.equipmentEventGUIDProperty().get();
	}

	public final void setEquipmentEventGUID(final String equipmentEventGUID) {
		this.equipmentEventGUIDProperty().set(equipmentEventGUID);
	}

	public final StringProperty eventGUIDProperty() {
		return this.eventGUID;
	}

	public final String getEventGUID() {
		return this.eventGUIDProperty().get();
	}

	public final void setEventGUID(final String eventGUID) {
		this.eventGUIDProperty().set(eventGUID);
	}

	public final StringProperty stateProperty() {
		return this.state;
	}

	public final String getState() {
		return this.stateProperty().get();
	}

	public final void setState(final String state) {
		this.stateProperty().set(state);
	}

	public final StringProperty stateNameProperty() {
		return this.stateName;
	}

	public final String getStateName() {
		return this.stateNameProperty().get();
	}

	public final void setStateName(final String stateName) {
		this.stateNameProperty().set(stateName);
	}

}
