/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.par
 *	작성일   : 2021. 12. 27.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.ec.ec.par;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.text.StringEscapeUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.actions.comm.core.AbstractManagementBorderPane;
import com.kyj.fx.nightmare.actions.comm.core.OnExcelTableViewList;
import com.kyj.fx.nightmare.actions.comm.core.OnLoadEquipmentClassEvent;
import com.kyj.fx.nightmare.comm.DialogUtil;
import com.kyj.fx.nightmare.comm.ExcelReader;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.IdGenUtil;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.comm.service.XMLUtils;
import com.kyj.fx.nightmare.ui.grid.AnnotationOptions;
import com.kyj.fx.nightmare.ui.grid.CodeDVO;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Pair;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class EquipmentParameterComposite extends AbstractManagementBorderPane<EventParameterDVO>
		implements OnExcelTableViewList, OnLoadEquipmentClassEvent {

	private static Logger LOGGER = LoggerFactory.getLogger(EquipmentParameterComposite.class);
	@FXML
	public TableView<EventParameterDVO> tvParameter;
	private StringProperty equipmentClassGuid = new SimpleStringProperty();
	private StringProperty equipmentEventGuid = new SimpleStringProperty();

	private StringProperty equipmentClassName = new SimpleStringProperty();
	private StringProperty equipmentEventName = new SimpleStringProperty();

	@FXML
	private TextField txtParameterName, txtDefaultValue, txtSequence;

	@FXML
	private ComboBox<CodeDVO> cbDataType, cbType;

	@FXML
	private CheckBox cbRequired, cbRepectable;

	@FXML
	private TextArea txtDescription;

	public EquipmentParameterComposite() {
		super(EquipmentParameterComposite.class.getResource("EquipmentParameterView.fxml"));
	}

	@FXML
	public void btnNewParamterOnAction() {

		String parameterName = txtParameterName.getText();
		if (ValueUtil.isEmpty(parameterName)) {
			DialogUtil.showMessageDialog("parameter name is empty.");
			return;
		}

		try {
			Integer.parseInt(txtSequence.getText(), 10);
		} catch (NumberFormatException e) {
			DialogUtil.showMessageDialog("sequence is not int.");
			return;
		}

		EventParameterDVO e = new EventParameterDVO();
		e.setName(parameterName);
		e.setDescription(txtDescription.getText());
		e.setDataType(cbDataType.getSelectionModel().getSelectedItem().getCode());
		e.setRecordType(cbType.getSelectionModel().getSelectedItem().getCode());
		e.setRequired(cbRequired.isSelected() ? "1" : "0");
		e.setIsRepeatable(cbRepectable.isSelected() ? "1" : "0");
		e.setSequence(txtSequence.getText());
		e.setDefaultValue(txtDefaultValue.getText());

		e.setEquipmentClassName(equipmentClassName.get());
		e.setEquipmentEventName(equipmentEventName.get());

		this.tvParameter.getItems().add(e);

	}

	@FXML
	public void initialize() {
		CodeDVO[] dataTypes = new CodeDVO[] {new CodeDVO("0", "Boolean") ,  new CodeDVO("1", "Date/Time"),   new CodeDVO("2", "Number"),  new CodeDVO("3", "String") };
		cbDataType.getItems().addAll(dataTypes);
		CodeDVO[] eventTypes = new CodeDVO[] { new CodeDVO("0", "On Start"), new CodeDVO("1", "On Update"),
				new CodeDVO("2", "On Complete") };
		cbType.getItems().addAll(eventTypes);

		cbDataType.getSelectionModel().selectFirst();
		cbType.getSelectionModel().selectFirst();
		AnnotationOptions<EventParameterDVO> option = new AnnotationOptions<EventParameterDVO>(EventParameterDVO.class) {

			@Override
			public boolean editable(String columnName) {
				if ("name".equals(columnName) || 
						"sequence".equals(columnName)||
						"description".equals(columnName)||
						"dataType".equals(columnName)||
						"recordType".equals(columnName)||
						"required".equals(columnName)||
						"isRepeatable".equals(columnName)||
						"defaultValue".equals(columnName)
						)
					return true;

				return false;
			}

		};
		// tvParameter.setEditable(true);

		FxUtil.installCommonsTableView(EventParameterDVO.class, tvParameter, option);
		this.equipmentEventGuid.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if (ValueUtil.isEmpty(arg2)) {
					tvParameter.getItems().clear();
					return;
				}

				var s = new EquipmentParameterService();
				try {
					Document doc = s.item(arg2);
					Node nEqClassGuid = doc.selectSingleNode("//Event/@EquipmentClassGUID");
					String eqguid = nEqClassGuid.getText();
					String eqname = new EquipmentParameterDAO().getEquipmentClassNameByGuid(eqguid);

					Node nEvent = doc.selectSingleNode("//Event/@Name");
					String eventName = nEvent.getText();
					List<EventParameterDVO> populateXmlElement = XMLUtils.populateXmlElement(doc, "//Event/ListParameters/Parameter",
							EventParameterDVO.class, new CamelCaseDataHandler(eqname, eventName));

					equipmentClassName.set(eqname);
					equipmentEventName.set(eventName);

					tvParameter.getItems().setAll(populateXmlElement);
					tvParameter.getItems().sort((a, b) -> {
						return Integer.compare(Integer.parseInt(a.getSequence(), 10), Integer.parseInt(b.getSequence(), 10));
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		MenuItem miTbParameterDelete = new MenuItem("Delete");
		miTbParameterDelete.setOnAction(this::miTbParameterDeleteOnAction);

		
		tvParameter.setContextMenu(new ContextMenu(miTbParameterDelete));
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2022. 7. 19.
	 * @param ae
	 */
	public void miTbParameterDeleteOnAction(ActionEvent ae) {
		EventParameterDVO selectedItem = this.tvParameter.getSelectionModel().getSelectedItem();
		if (selectedItem == null)
			return;
		String name = selectedItem.getName();
		Optional<Pair<String, String>> showYesOrNoDialog = DialogUtil.showYesOrNoDialog("Delete", "선택하신 " + name + " 을 삭제하시겠습니까?");
		showYesOrNoDialog.ifPresent(p -> {

			if ("Y".equals(p.getValue())) {
				this.tvParameter.getItems().remove(selectedItem);
			}

		});
	}

	@Override
	public void reload() {
		onLoadEquipmentClassEvent(equipmentClassGuid.get(), equipmentEventGuid.get());
	}

	@Override
	public String onDeployItem(String name, List<EventParameterDVO> items) {

		var s = new EquipmentParameterService();
		try {

			String eqName = items.get(0).getEquipmentClassName();
			String eventName = items.get(0).getEquipmentEventName();

			String eventGuid = new EquipmentParameterDAO().getEventGuid(eqName, eventName);
			Document doc = s.item(eventGuid);
			Element selectSingleNode = (Element) doc.selectSingleNode("//ListParameters");
			List<Node> selectNodes = doc.selectNodes("//ListParameters/Parameter");
			for (Node e : selectNodes)
				selectSingleNode.remove(e);

			for (EventParameterDVO d : items) {
				Element addElement = selectSingleNode.addElement("Parameter");
				
				addElement.addAttribute("GUID",    ValueUtil.isEmpty(d.getGuid()) ? IdGenUtil.randomGuid().toUpperCase() : d.getGuid() );
				addElement.addAttribute("Name", d.getName());
				addElement.addAttribute("Description", d.getDescription());
				addElement.addAttribute("DataType", d.getDataType());
				addElement.addAttribute("RecordType", d.getRecordType());
				addElement.addAttribute("Required", d.getRequired());
				addElement.addAttribute("IsRepeatable", d.getIsRepeatable());
				addElement.addAttribute("DefaultValue", d.getDefaultValue());
				addElement.addAttribute("Sequence", d.getSequence() + "");
			}

			String token = createTokenEx(this.esigInfo.get());

			String req = StringEscapeUtils.escapeXml10(doc.getRootElement().asXML());
			LOGGER.debug(req);
			// StringEscapeUtils.escapeXml(reqXml)
			Document update = s.update(token, req);

			Node nFault = update.selectSingleNode("//faultstring");
			if (nFault != null) {
				// DialogUtil.showMessageDialog(nFault.getText());
				setDeployItemFail();
				return nFault.getText();
			}
			setDeployItemPass();
			return "Pass";
		} catch (Exception e) {
			e.printStackTrace();
			setDeployItemFail();
			return e.getMessage();
		}
	}

	// @Override
	// public void onLoadEquipmentClass(String equipmentClassGuid) {
	// this.equipmentClassGuid.set("");
	// this.equipmentClassGuid.set(equipmentClassGuid);
	// }

	private ObjectProperty<Map<String, String>> esigInfo = new SimpleObjectProperty<>();

	@Override
	public void onCommit() {
		ObservableList<EventParameterDVO> items = this.tvParameter.getItems();
		Map<String, List<EventParameterDVO>> collect = items.stream()
				.collect(Collectors.groupingBy(v -> v.getEquipmentClassName() + "┐" + v.getEquipmentEventName()));
		setDeployItems(collect);

		String permission = "5";
		String domain = "Syncade";
		String application = "DMI ET";
		String entityType = "Events";
		String entityId = "0";

		Map<String, String> showSimpleSigDialog = showSimpleSigDialog(permission, domain, application, entityType, entityId);
		esigInfo.set(showSimpleSigDialog);
		super.onCommit();
	}

	@Override
	public List<TableView<EventParameterDVO>> excelTableList() {
		return Arrays.asList(tvParameter);
	}

	@Override
	public void importExcel(File fromFile, Document doc) {
		try {
			List<EventParameterDVO> populateList = ExcelReader.populateList(doc, EventParameterDVO.class);
			tvParameter.getItems().setAll(populateList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onLoadEquipmentClassEvent(String equipmentClassGuid, String eventGuid) {
		this.equipmentClassGuid.set(equipmentClassGuid);
		this.equipmentEventGuid.set(eventGuid);
		
		
		txtParameterName.setText("");
		txtDefaultValue.setText("");
		txtSequence.setText("0");
		
		cbRequired.setSelected(false);
		cbRepectable.setSelected(false);
		
		txtDescription.setText("");		
	}

}
