/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.events
 *	작성일   : 2021. 11. 30.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.ec.ec.events;

import com.kyj.fx.nightmare.comm.AbstractDVO;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EtEventsDVO extends AbstractDVO {
	private StringProperty eventGUID;
	private StringProperty equipmentClassGUID;
	private StringProperty name;
	private StringProperty description;
	private IntegerProperty eventType;
	private IntegerProperty tTCType;
	private IntegerProperty tTCIntervalType;
	private IntegerProperty tTCInterval;
	private StringProperty recordOrder;
	private StringProperty preventEquipmentScheduling;
	private StringProperty downTimeClassGUID;
	private StringProperty utilizationClassGUID;
	private IntegerProperty schedulingSequence;
	private IntegerProperty scheduleType;
	private IntegerProperty scheduleInterval;
	private IntegerProperty scheduleIntervalType;
	private StringProperty expires;
	private IntegerProperty expireInterval;
	private IntegerProperty expireState;
	private StringProperty trueStateText;
	private StringProperty falseStateText;
	private StringProperty stampGUID;
	private StringProperty scheduleEndOfMonth;
	private StringProperty displayExecution;
	private StringProperty displayEventStates;
	private IntegerProperty recordItemType;
	private StringProperty defaultState;
	private StringProperty disabled;

	public EtEventsDVO() {
		this.eventGUID = new SimpleStringProperty();
		this.equipmentClassGUID = new SimpleStringProperty();
		this.name = new SimpleStringProperty();
		this.description = new SimpleStringProperty();
		this.eventType = new SimpleIntegerProperty();
		this.tTCType = new SimpleIntegerProperty();
		this.tTCIntervalType = new SimpleIntegerProperty();
		this.tTCInterval = new SimpleIntegerProperty();
		this.recordOrder = new SimpleStringProperty();
		this.preventEquipmentScheduling = new SimpleStringProperty();
		this.downTimeClassGUID = new SimpleStringProperty();
		this.utilizationClassGUID = new SimpleStringProperty();
		this.schedulingSequence = new SimpleIntegerProperty();
		this.scheduleType = new SimpleIntegerProperty();
		this.scheduleInterval = new SimpleIntegerProperty();
		this.scheduleIntervalType = new SimpleIntegerProperty();
		this.expires = new SimpleStringProperty();
		this.expireInterval = new SimpleIntegerProperty();
		this.expireState = new SimpleIntegerProperty();
		this.trueStateText = new SimpleStringProperty();
		this.falseStateText = new SimpleStringProperty();
		this.stampGUID = new SimpleStringProperty();
		this.scheduleEndOfMonth = new SimpleStringProperty();
		this.displayExecution = new SimpleStringProperty();
		this.displayEventStates = new SimpleStringProperty();
		this.recordItemType = new SimpleIntegerProperty();
		this.defaultState = new SimpleStringProperty();
		this.disabled = new SimpleStringProperty();
	}

	public void setEventGUID(String eventGUID) {
		this.eventGUID.set(eventGUID);
	}

	public String getEventGUID() {
		return eventGUID.get();
	}

	public StringProperty eventGUIDProperty() {
		return eventGUID;
	}

	public void setEquipmentClassGUID(String equipmentClassGUID) {
		this.equipmentClassGUID.set(equipmentClassGUID);
	}

	public String getEquipmentClassGUID() {
		return equipmentClassGUID.get();
	}

	public StringProperty equipmentClassGUIDProperty() {
		return equipmentClassGUID;
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

	public void setEventType(Integer eventType) {
		this.eventType.set(eventType);
	}

	public Number getEventType() {
		return eventType.get();
	}

	public IntegerProperty eventTypeProperty() {
		return eventType;
	}

	public void setTTCType(Integer tTCType) {
		this.tTCType.set(tTCType);
	}

	public Number getTTCType() {
		return tTCType.get();
	}

	public IntegerProperty tTCTypeProperty() {
		return tTCType;
	}

	public void setTTCIntervalType(Integer tTCIntervalType) {
		this.tTCIntervalType.set(tTCIntervalType);
	}

	public Number getTTCIntervalType() {
		return tTCIntervalType.get();
	}

	public IntegerProperty tTCIntervalTypeProperty() {
		return tTCIntervalType;
	}

	public void setTTCInterval(Integer tTCInterval) {
		this.tTCInterval.set(tTCInterval);
	}

	public Number getTTCInterval() {
		return tTCInterval.get();
	}

	public IntegerProperty tTCIntervalProperty() {
		return tTCInterval;
	}

	public void setRecordOrder(String recordOrder) {
		this.recordOrder.set(recordOrder);
	}

	public String getRecordOrder() {
		return recordOrder.get();
	}

	public StringProperty recordOrderProperty() {
		return recordOrder;
	}

	public void setPreventEquipmentScheduling(String preventEquipmentScheduling) {
		this.preventEquipmentScheduling.set(preventEquipmentScheduling);
	}

	public String getPreventEquipmentScheduling() {
		return preventEquipmentScheduling.get();
	}

	public StringProperty preventEquipmentSchedulingProperty() {
		return preventEquipmentScheduling;
	}

	public void setDownTimeClassGUID(String downTimeClassGUID) {
		this.downTimeClassGUID.set(downTimeClassGUID);
	}

	public String getDownTimeClassGUID() {
		return downTimeClassGUID.get();
	}

	public StringProperty downTimeClassGUIDProperty() {
		return downTimeClassGUID;
	}

	public void setUtilizationClassGUID(String utilizationClassGUID) {
		this.utilizationClassGUID.set(utilizationClassGUID);
	}

	public String getUtilizationClassGUID() {
		return utilizationClassGUID.get();
	}

	public StringProperty utilizationClassGUIDProperty() {
		return utilizationClassGUID;
	}

	public void setSchedulingSequence(Integer schedulingSequence) {
		this.schedulingSequence.set(schedulingSequence);
	}

	public Number getSchedulingSequence() {
		return schedulingSequence.get();
	}

	public IntegerProperty schedulingSequenceProperty() {
		return schedulingSequence;
	}

	public void setScheduleType(Integer scheduleType) {
		this.scheduleType.set(scheduleType);
	}

	public Number getScheduleType() {
		return scheduleType.get();
	}

	public IntegerProperty scheduleTypeProperty() {
		return scheduleType;
	}

	public void setScheduleInterval(Integer scheduleInterval) {
		this.scheduleInterval.set(scheduleInterval);
	}

	public Number getScheduleInterval() {
		return scheduleInterval.get();
	}

	public IntegerProperty scheduleIntervalProperty() {
		return scheduleInterval;
	}

	public void setScheduleIntervalType(Integer scheduleIntervalType) {
		this.scheduleIntervalType.set(scheduleIntervalType);
	}

	public Number getScheduleIntervalType() {
		return scheduleIntervalType.get();
	}

	public IntegerProperty scheduleIntervalTypeProperty() {
		return scheduleIntervalType;
	}

	public void setExpires(String expires) {
		this.expires.set(expires);
	}

	public String getExpires() {
		return expires.get();
	}

	public StringProperty expiresProperty() {
		return expires;
	}

	public void setExpireInterval(Integer expireInterval) {
		this.expireInterval.set(expireInterval);
	}

	public Number getExpireInterval() {
		return expireInterval.get();
	}

	public IntegerProperty expireIntervalProperty() {
		return expireInterval;
	}

	public void setExpireState(Integer expireState) {
		this.expireState.set(expireState);
	}

	public Number getExpireState() {
		return expireState.get();
	}

	public IntegerProperty expireStateProperty() {
		return expireState;
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

	public void setStampGUID(String stampGUID) {
		this.stampGUID.set(stampGUID);
	}

	public String getStampGUID() {
		return stampGUID.get();
	}

	public StringProperty stampGUIDProperty() {
		return stampGUID;
	}

	public void setScheduleEndOfMonth(String scheduleEndOfMonth) {
		this.scheduleEndOfMonth.set(scheduleEndOfMonth);
	}

	public String getScheduleEndOfMonth() {
		return scheduleEndOfMonth.get();
	}

	public StringProperty scheduleEndOfMonthProperty() {
		return scheduleEndOfMonth;
	}

	public void setDisplayExecution(String displayExecution) {
		this.displayExecution.set(displayExecution);
	}

	public String getDisplayExecution() {
		return displayExecution.get();
	}

	public StringProperty displayExecutionProperty() {
		return displayExecution;
	}

	public void setDisplayEventStates(String displayEventStates) {
		this.displayEventStates.set(displayEventStates);
	}

	public String getDisplayEventStates() {
		return displayEventStates.get();
	}

	public StringProperty displayEventStatesProperty() {
		return displayEventStates;
	}

	public void setRecordItemType(Integer recordItemType) {
		this.recordItemType.set(recordItemType);
	}

	public Number getRecordItemType() {
		return recordItemType.get();
	}

	public IntegerProperty recordItemTypeProperty() {
		return recordItemType;
	}

	public void setDefaultState(String defaultState) {
		this.defaultState.set(defaultState);
	}

	public String getDefaultState() {
		return defaultState.get();
	}

	public StringProperty defaultStateProperty() {
		return defaultState;
	}

	public void setDisabled(String disabled) {
		this.disabled.set(disabled);
	}

	public String getDisabled() {
		return disabled.get();
	}

	public StringProperty disabledProperty() {
		return disabled;
	}

	@Override
	public String toString() {
		return getName();
	}
	
	
}
