/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.par
 *	작성일   : 2021. 12. 27.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.ec.ec.par;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringEscapeUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.b.ETScriptHelper.actions.comm.core.AbstractManagementBorderPane;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnExcelTableViewList;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnLoadEquipmentClassEvent;
import com.kyj.fx.b.ETScriptHelper.comm.ExcelReader;
import com.kyj.fx.b.ETScriptHelper.comm.FxUtil;
import com.kyj.fx.b.ETScriptHelper.comm.IdGenUtil;
import com.kyj.fx.b.ETScriptHelper.comm.ValueUtil;
import com.kyj.fx.b.ETScriptHelper.comm.service.XMLUtils;
import com.kyj.fx.b.ETScriptHelper.grid.AnnotationOptions;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EquipmentParameterComposite extends AbstractManagementBorderPane<EventParameterDVO>
		implements OnExcelTableViewList, OnLoadEquipmentClassEvent {

	private static Logger LOGGER = LoggerFactory.getLogger(EquipmentParameterComposite.class);
	@FXML
	public TableView<EventParameterDVO> tvParameter;
	private StringProperty equipmentClassGuid = new SimpleStringProperty();
	private StringProperty equipmentEventGuid = new SimpleStringProperty();

	public EquipmentParameterComposite() {
		super(EquipmentParameterComposite.class.getResource("EquipmentParameterView.fxml"));
	}

	@FXML
	public void initialize() {
		FxUtil.installCommonsTableView(EventParameterDVO.class, tvParameter, new AnnotationOptions<EventParameterDVO>(EventParameterDVO.class));
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

					tvParameter.getItems().setAll(populateXmlElement);
					tvParameter.getItems().sort((a,b)->{
						return Integer.compare(a.getSequence(), b.getSequence());
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
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
			List<Element> selectNodes = doc.selectNodes("//ListParameters/Parameter");
			for (Element e : selectNodes)
				selectSingleNode.remove(e);

			for (EventParameterDVO d : items) {
				Element addElement = selectSingleNode.addElement("Parameter");
				addElement.addAttribute("GUID", IdGenUtil.randomGuid().toUpperCase());
				addElement.addAttribute("Name", d.getName());
				addElement.addAttribute("Description", d.getDescription());
				addElement.addAttribute("DataType", d.getDataType());
				addElement.addAttribute("RecordType", d.getRecordType());
				addElement.addAttribute("Required", d.getRequired());
				addElement.addAttribute("IsRepeatable", d.getIsRepeatable());
				addElement.addAttribute("DefaultValue", d.getDefaultValue());
				addElement.addAttribute("Sequence", d.getSequence()+"");
			}

			String token = createTokenEx(this.esigInfo.get());
			String req = StringEscapeUtils.escapeXml(doc.getRootElement().asXML());
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
	}

}
