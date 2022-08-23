/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.events
 *	작성일   : 2021. 11. 30.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.ec.ec.events;

import java.io.IOException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Node;

import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnLoadEquipment;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnLoadEquipmentClassEvent;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnReload;
import com.kyj.fx.b.ETScriptHelper.actions.ec.ec.par.EquipmentParameterDAO;
import com.kyj.fx.b.ETScriptHelper.actions.ec.ec.par.EquipmentParameterService;
import com.kyj.fx.b.ETScriptHelper.comm.DialogUtil;
import com.kyj.fx.b.ETScriptHelper.comm.FxUtil;
import com.kyj.fx.b.ETScriptHelper.comm.ValueUtil;
import com.kyj.fx.b.ETScriptHelper.comm.service.XMLUtils;
import com.kyj.fx.b.ETScriptHelper.comm.service.XMLUtils.XmlDataHander;
import com.kyj.fx.b.ETScriptHelper.grid.CodeDVO;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EquipmentClassEventComposite extends BorderPane implements OnLoadEquipmentClassEvent, OnLoadEquipment, OnReload {

	private StringProperty equipmentClassGuid = new SimpleStringProperty();
	private StringProperty equipmentGuid = new SimpleStringProperty();
	private StringProperty eventGuid = new SimpleStringProperty();

	private StringProperty equipmentClassName = new SimpleStringProperty();
	private StringProperty equipmentEventName = new SimpleStringProperty();

	private ObjectProperty<EtEventsDVO> item = new SimpleObjectProperty<>();
	@FXML
	private RadioButton rbTransient, rbStartStop, rbSystemEvent;
	@FXML
	private CheckBox cbDisplayForExecution, cbDisplayEventStates;
	@FXML
	private TextField txtEventName, txtTrueText, txtFalseText, txtScheduleInteval;
	@FXML
	private TextArea txtEventDesc;
	@FXML
	private ComboBox<CodeDVO> cbDefaultState;

	/**
	 */
	public EquipmentClassEventComposite() {
		FXMLLoader newLaoder = FxUtil.newLaoder();
		newLaoder.setLocation(EquipmentClassEventComposite.class.getResource("EquipmentClassEventView.fxml"));
		newLaoder.setRoot(this);
		newLaoder.setController(this);
		try {
			newLaoder.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void initialize() {

		this.cbDefaultState.getItems().addAll(new CodeDVO("0", "False"), new CodeDVO("1", "True"));

		this.eventGuid.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (ValueUtil.isNotEmpty(newValue)) {

					var s = new EquipmentParameterService();
					try {
						Document doc = s.item(newValue);
						Node nEqClassGuid = doc.selectSingleNode("//Event/@EquipmentClassGUID");
						String eqguid = nEqClassGuid.getText();
						String eqname = new EquipmentParameterDAO().getEquipmentClassNameByGuid(eqguid);

						Node nEvent = doc.selectSingleNode("//Event/@Name");
						String eventName = nEvent.getText();

						List<EtEventsDVO> populateXmlElement = XMLUtils.populateXmlElement(doc, "//Event", EtEventsDVO.class,
								new XmlDataHander<EtEventsDVO>() {

									@Override
									public void handle(EtEventsDVO instance, String nodeName, String nodeValue) {
										/*
										 * DownTimeClassification {Not
										 * Specified} UtilizationClassification
										 * Standard
										 */
										switch (nodeName) {
										case "DownTimeClassGUID":
											instance.setDownTimeClassGUID(nodeValue);
											break;
										case "UtilizationClassification":
											instance.setUtilizationClassGUID(nodeValue);
											break;
										case "ExpireState":
											instance.setExpireState(Integer.parseInt(nodeValue, 10));
											break;
										case "StampGUID":
											instance.setStampGUID(nodeValue);
											break;
										case "ExpireInterval":
											instance.setExpireInterval(Integer.parseInt(nodeValue, 10));
											break;
										case "Expires":
											instance.setExpires(nodeValue);
											break;
										case "ScheduleEndOfMonth":
											instance.setScheduleEndOfMonth(nodeValue);
											break;
										case "PreventEquipmentScheduling":
											instance.setPreventEquipmentScheduling(nodeValue);
											break;
										case "RecordItemType":
											instance.setRecordItemType(Integer.parseInt(nodeValue, 10));
											break;
										case "RecordOrder":
											instance.setRecordOrder(nodeValue);
											break;
										case "TTCInterval":
											instance.setTTCInterval(Integer.parseInt(nodeValue, 10));
											break;
										case "TTCIntervalType":
											instance.setTTCIntervalType(Integer.parseInt(nodeValue, 10));
											break;
										case "TTCType":
											instance.setTTCType(Integer.parseInt(nodeValue, 10));
											break;
										case "EquipmentClassGUID":
											instance.setEquipmentClassGUID(nodeValue);
											break;
										case "SchedulingSequence":
											instance.setSchedulingSequence(Integer.parseInt(nodeValue, 10));
											break;
										case "ScheduleIntervalType":
											instance.setScheduleIntervalType(Integer.parseInt(nodeValue, 10));
											break;
										case "ScheduleInterval":
											instance.setScheduleInterval(Integer.parseInt(nodeValue, 10));
											break;
										case "ScheduleType":
											instance.setScheduleType(Integer.parseInt(nodeValue, 10));
											break;
										case "Disabled":
											instance.setDisabled(nodeValue);
											break;
										case "Name":
											instance.setName(nodeValue);
											break;
										case "DisplayExecution":
											instance.setDisplayExecution(nodeValue);
											break;
										case "DisplayEventStates":
											instance.setDisplayEventStates(nodeValue);
											break;
										case "EventType":
											instance.setEventType(Integer.parseInt(nodeValue, 10));
											break;
										case "DefaultState":
											instance.setDefaultState(nodeValue);
											break;
										case "FalseStateText":
											instance.setFalseStateText(nodeValue);
											break;
										case "TrueStateText":
											instance.setTrueStateText(nodeValue);
											break;
										case "GUID":
											instance.setEventGUID(nodeValue);
											break;
										case "Description":
											instance.setDescription(nodeValue);
											break;
										}

										System.out.println(nodeName + " " + nodeValue);
									}
								});
						if (!populateXmlElement.isEmpty()) {
							item.set(populateXmlElement.get(0));
						}

						equipmentClassName.set(eqname);
						equipmentEventName.set(eventName);

						// tvParameter.getItems().setAll(populateXmlElement);
						// tvParameter.getItems().sort((a, b) -> {
						// return
						// Integer.compare(Integer.parseInt(a.getSequence(),
						// 10), Integer.parseInt(b.getSequence(), 10));
						// });
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

		this.item.addListener(new ChangeListener<EtEventsDVO>() {

			@Override
			public void changed(ObservableValue<? extends EtEventsDVO> observable, EtEventsDVO oldValue, EtEventsDVO newValue) {
				if (newValue != null) {

					
					cbDisplayForExecution.selectedProperty().bind(newValue.displayExecutionProperty().isEqualTo("1"));
					cbDisplayEventStates.selectedProperty().bind(newValue.displayEventStatesProperty().isEqualTo("1"));

					txtTrueText.textProperty().bind(newValue.trueStateTextProperty());
					txtFalseText.textProperty().bind(newValue.falseStateTextProperty());

					txtEventName.textProperty().bind(newValue.nameProperty());
					txtEventDesc.textProperty().bind(newValue.descriptionProperty());

					rbTransient.selectedProperty().bind(newValue.eventTypeProperty().isEqualTo(0));
					rbStartStop.selectedProperty().bind(newValue.eventTypeProperty().isEqualTo(1));
					rbSystemEvent.selectedProperty().bind(newValue.eventTypeProperty().isEqualTo(2));

					txtScheduleInteval.textProperty().bind(newValue.scheduleIntervalProperty().asString());

					cbDefaultState.getSelectionModel().select( Integer.parseInt(newValue.getDefaultState(), 10) );
					// System.out.println();
				}
			}
		});

		cbDefaultState.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CodeDVO>() {

			@Override
			public void changed(ObservableValue<? extends CodeDVO> observable, CodeDVO oldValue, CodeDVO newValue) {
				if (newValue != null) {
					item.get().setDefaultState(newValue.getCode());
				}
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kyj.fx.b.ETScriptHelper.actions.comm.OnLoadEquipmentClassEvent#
	 * onLoadEquipmentClassEvent(java.lang.String, java.lang.String)
	 */
	@Override
	public void onLoadEquipmentClassEvent(String equipmentClassGuid, String eventGuid) {
		this.equipmentClassGuid.set(equipmentClassGuid);
		this.eventGuid.set(eventGuid);
	}

	@Override
	public void reload() {
		DialogUtil.showMessageDialog("미구현.");
	}

	@Override
	public void onLoadEquipment(String equipmentGuid) {
		this.equipmentGuid.set(equipmentGuid);
	}
}
