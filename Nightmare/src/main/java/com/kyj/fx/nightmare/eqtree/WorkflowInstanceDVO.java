/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.eqtree
 *	작성일   : 2022. 7. 22.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.nightmare.eqtree;

import java.sql.Timestamp;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class WorkflowInstanceDVO {
	private StringProperty workFlowInstanceGUID;
	private StringProperty workFlowGUID;
	private StringProperty name;
	private StringProperty description;
	private StringProperty documentID;
	private StringProperty documentName;
	private StringProperty equipmentClassGUID;
	private StringProperty equipmentClassName;
	private StringProperty equipmentGUID;
	private StringProperty equipmentName;
	private StringProperty equipmentCode;
	private StringProperty orderGUID;
	private StringProperty orderNumber;
	private ObjectProperty<Timestamp> startDate;
	private IntegerProperty status;
	private StringProperty stampGUID;
	private IntegerProperty wFVersion;

	public WorkflowInstanceDVO() {
		this.workFlowInstanceGUID = new SimpleStringProperty();
		this.workFlowGUID = new SimpleStringProperty();
		this.name = new SimpleStringProperty();
		this.description = new SimpleStringProperty();
		this.documentID = new SimpleStringProperty();
		this.documentName = new SimpleStringProperty();
		this.equipmentClassGUID = new SimpleStringProperty();
		this.equipmentClassName = new SimpleStringProperty();
		this.equipmentGUID = new SimpleStringProperty();
		this.equipmentName = new SimpleStringProperty();
		this.equipmentCode = new SimpleStringProperty();
		this.orderGUID = new SimpleStringProperty();
		this.orderNumber = new SimpleStringProperty();
		this.startDate = new SimpleObjectProperty<Timestamp>();
		this.status = new SimpleIntegerProperty();
		this.stampGUID = new SimpleStringProperty();
		this.wFVersion = new SimpleIntegerProperty();
	}

	public void setWorkFlowInstanceGUID(String workFlowInstanceGUID) {
		this.workFlowInstanceGUID.set(workFlowInstanceGUID);
	}

	public String getWorkFlowInstanceGUID() {
		return workFlowInstanceGUID.get();
	}

	public StringProperty workFlowInstanceGUIDProperty() {
		return workFlowInstanceGUID;
	}

	public void setWorkFlowGUID(String workFlowGUID) {
		this.workFlowGUID.set(workFlowGUID);
	}

	public String getWorkFlowGUID() {
		return workFlowGUID.get();
	}

	public StringProperty workFlowGUIDProperty() {
		return workFlowGUID;
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

	public void setDocumentID(String documentID) {
		this.documentID.set(documentID);
	}

	public String getDocumentID() {
		return documentID.get();
	}

	public StringProperty documentIDProperty() {
		return documentID;
	}

	public void setDocumentName(String documentName) {
		this.documentName.set(documentName);
	}

	public String getDocumentName() {
		return documentName.get();
	}

	public StringProperty documentNameProperty() {
		return documentName;
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

	public void setEquipmentClassName(String equipmentClassName) {
		this.equipmentClassName.set(equipmentClassName);
	}

	public String getEquipmentClassName() {
		return equipmentClassName.get();
	}

	public StringProperty equipmentClassNameProperty() {
		return equipmentClassName;
	}

	public void setEquipmentGUID(String equipmentGUID) {
		this.equipmentGUID.set(equipmentGUID);
	}

	public String getEquipmentGUID() {
		return equipmentGUID.get();
	}

	public StringProperty equipmentGUIDProperty() {
		return equipmentGUID;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName.set(equipmentName);
	}

	public String getEquipmentName() {
		return equipmentName.get();
	}

	public StringProperty equipmentNameProperty() {
		return equipmentName;
	}

	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode.set(equipmentCode);
	}

	public String getEquipmentCode() {
		return equipmentCode.get();
	}

	public StringProperty equipmentCodeProperty() {
		return equipmentCode;
	}

	public void setOrderGUID(String orderGUID) {
		this.orderGUID.set(orderGUID);
	}

	public String getOrderGUID() {
		return orderGUID.get();
	}

	public StringProperty orderGUIDProperty() {
		return orderGUID;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber.set(orderNumber);
	}

	public String getOrderNumber() {
		return orderNumber.get();
	}

	public StringProperty orderNumberProperty() {
		return orderNumber;
	}

	public void setStatus(Number status) {
		this.status.set(status.intValue());
	}

	public Number getStatus() {
		return status.get();
	}

	public IntegerProperty statusProperty() {
		return status;
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

	public void setWFVersion(Number wFVersion) {
		this.wFVersion.set(wFVersion.intValue());
	}

	public Number getWFVersion() {
		return wFVersion.get();
	}

	public IntegerProperty wFVersionProperty() {
		return wFVersion;
	}

	public final ObjectProperty<Timestamp> startDateProperty() {
		return this.startDate;
	}

	public final Timestamp getStartDate() {
		return this.startDateProperty().get();
	}

	public final void setStartDate(final Timestamp startDate) {
		this.startDateProperty().set(startDate);
	}

}
