/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec
 *	작성일   : 2021. 11. 23.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.ec.ec;

import com.kyj.fx.b.ETScriptHelper.comm.AbstractDVO;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EquipmentClassDVO extends AbstractDVO{

	private StringProperty equipmentClassGUID;
	private StringProperty name;
	private StringProperty description;
	private IntegerProperty calibrationType;
	private StringProperty calibrationGroupGUID;
	private IntegerProperty scheduleType;
	private IntegerProperty intervalUnit;
	private IntegerProperty intervalValue;
	private IntegerProperty graceUnit;
	private IntegerProperty graceValue;
	private StringProperty mustReview;
	private StringProperty stampGUID;
	private IntegerProperty disabled;
	private StringProperty copyGUID;

	public EquipmentClassDVO() {
		this.equipmentClassGUID = new SimpleStringProperty();
		this.name = new SimpleStringProperty();
		this.description = new SimpleStringProperty();
		this.calibrationType = new SimpleIntegerProperty();
		this.calibrationGroupGUID = new SimpleStringProperty();
		this.scheduleType = new SimpleIntegerProperty();
		this.intervalUnit = new SimpleIntegerProperty();
		this.intervalValue = new SimpleIntegerProperty();
		this.graceUnit = new SimpleIntegerProperty();
		this.graceValue = new SimpleIntegerProperty();
		this.mustReview = new SimpleStringProperty();
		this.stampGUID = new SimpleStringProperty();
		this.disabled = new SimpleIntegerProperty();
		this.copyGUID = new SimpleStringProperty();
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

	public void setCalibrationType(int calibrationType) {
		this.calibrationType.set(calibrationType);
	}

	public int getCalibrationType() {
		return calibrationType.get();
	}

	public IntegerProperty calibrationTypeProperty() {
		return calibrationType;
	}

	public void setCalibrationGroupGUID(String calibrationGroupGUID) {
		this.calibrationGroupGUID.set(calibrationGroupGUID);
	}

	public String getCalibrationGroupGUID() {
		return calibrationGroupGUID.get();
	}

	public StringProperty calibrationGroupGUIDProperty() {
		return calibrationGroupGUID;
	}

	public void setScheduleType(int scheduleType) {
		this.scheduleType.set(scheduleType);
	}

	public int getScheduleType() {
		return scheduleType.get();
	}

	public IntegerProperty scheduleTypeProperty() {
		return scheduleType;
	}

	public void setIntervalUnit(int intervalUnit) {
		this.intervalUnit.set(intervalUnit);
	}

	public int getIntervalUnit() {
		return intervalUnit.get();
	}

	public IntegerProperty intervalUnitProperty() {
		return intervalUnit;
	}

	public void setIntervalValue(int intervalValue) {
		this.intervalValue.set(intervalValue);
	}

	public int getIntervalValue() {
		return intervalValue.get();
	}

	public IntegerProperty intervalValueProperty() {
		return intervalValue;
	}

	public void setGraceUnit(int graceUnit) {
		this.graceUnit.set(graceUnit);
	}

	public int getGraceUnit() {
		return graceUnit.get();
	}

	public IntegerProperty graceUnitProperty() {
		return graceUnit;
	}

	public void setGraceValue(int graceValue) {
		this.graceValue.set(graceValue);
	}

	public int getGraceValue() {
		return graceValue.get();
	}

	public int graceValueProperty() {
		return graceValue.get();
	}

	public void setMustReview(String mustReview) {
		this.mustReview.set(mustReview);
	}

	public String getMustReview() {
		return mustReview.get();
	}

	public StringProperty mustReviewProperty() {
		return mustReview;
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

	public void setDisabled(int disabled) {
		this.disabled.set(disabled);
	}

	public int getDisabled() {
		return disabled.get();
	}

	public IntegerProperty disabledProperty() {
		return disabled;
	}

	public void setCopyGUID(String copyGUID) {
		this.copyGUID.set(copyGUID);
	}

	public String getCopyGUID() {
		return copyGUID.get();
	}

	public StringProperty copyGUIDProperty() {
		return copyGUID;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EquipmentClassDVO [name=" + name.getValue() + "]";
	}
	
	
}
