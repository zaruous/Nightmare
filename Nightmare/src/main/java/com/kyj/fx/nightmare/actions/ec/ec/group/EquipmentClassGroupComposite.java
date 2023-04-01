/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.group
 *	작성일   : 2021. 12. 17.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.ec.ec.group;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.actions.comm.core.AbstractManagementBorderPane;
import com.kyj.fx.nightmare.actions.comm.core.OnExcelTableViewList;
import com.kyj.fx.nightmare.actions.comm.core.OnLoadEquipmentClass;
import com.kyj.fx.nightmare.comm.ExcelReader;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.IdGenUtil;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.comm.service.XMLUtils;
import com.kyj.fx.nightmare.grid.AnnotationOptions;
import com.kyj.fx.nightmare.grid.CrudBaseColumnMapper;
import com.kyj.fx.nightmare.grid.IOptions;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EquipmentClassGroupComposite extends AbstractManagementBorderPane<EquipmentClassGroupDVO>
		implements OnLoadEquipmentClass, OnExcelTableViewList {

	private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentClassGroupComposite.class);

	@FXML
	private TableView<EquipmentClassGroupDVO> tvGroup;

	private StringProperty equipmentClassGuid = new SimpleStringProperty();

	public EquipmentClassGroupComposite() {
		super(EquipmentClassGroupComposite.class.getResource("EquipmentClassGroupView.fxml"));

	}

	@FXML
	public void initialize() {
		tvGroup.setEditable(true);
		FxUtil.installCommonsTableView(EquipmentClassGroupDVO.class, tvGroup, new CrudBaseColumnMapper<EquipmentClassGroupDVO>() {

			@Override
			public TableColumn<EquipmentClassGroupDVO, ?> generateTableColumns(Class<?> classType, String columnName,
					IOptions columnNaming) {
				
				if("sequence".equals(columnName)) {
					TableColumn<EquipmentClassGroupDVO, String> tableColumn = new TableColumn<EquipmentClassGroupDVO, String>(columnName);
					tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
					tableColumn.setCellValueFactory(call->call.getValue().sequenceProperty());
					tableColumn.setEditable(true);
					return tableColumn;
				}
				
				
				TableColumn<EquipmentClassGroupDVO, ?> generateTableColumns = super.generateTableColumns(classType, columnName, columnNaming);
				return generateTableColumns;
			}
		}, new AnnotationOptions<>(EquipmentClassGroupDVO.class));
		
		
		this.equipmentClassGuid.addListener((ChangeListener<String>) (arg0, oldv, newv) -> {
			if (ValueUtil.isEmpty(newv)) {
				this.tvGroup.getItems().setAll(Collections.emptyList());
			} else {

				var dao = new EquipmentClassGroupDAO();
				List<EquipmentClassGroupDVO> listGroup = dao.listGroup(newv);
				this.tvGroup.getItems().setAll(listGroup);

			}
		});
	}

	@Override
	public void reload() {
		onLoadEquipmentClass(this.equipmentClassGuid.get());
	}

	private ObjectProperty<Map<String, String>> esigInfo = new SimpleObjectProperty<>();;

	@Override
	public void onCommit() {

		String permission = "5";
		String domain = "Syncade";
		String application = "DMI ET";
		String entityType = "RuleGroups";
		String entityId = "0";

		Map<String, String> showSimpleSigDialog = showSimpleSigDialog(permission, domain, application, entityType, entityId);
		this.esigInfo.set(showSimpleSigDialog);

		ObservableList<EquipmentClassGroupDVO> items = tvGroup.getItems();
		Map<String, List<EquipmentClassGroupDVO>> collect = items.stream().collect(Collectors.groupingBy((EquipmentClassGroupDVO a) -> {
			return a.getEquipmentClassGuid();
		}));
		setDeployItems(collect);

		LOGGER.debug("{}", showSimpleSigDialog);

		super.onCommit();
	}

	@Override
	public String onDeployItem(String name, List<EquipmentClassGroupDVO> items) {
		EquipmentClassGroupService s = new EquipmentClassGroupService();

		if (items.size() == 0) {
			return "data empty.";
		}

		try {
			String equipmentClassGuid = items.get(0).getEquipmentClassGuid();
			Object listRuleGroup = s.listRuleGroup(equipmentClassGuid);

			var doc = XMLUtils.load(listRuleGroup.toString());

			for (EquipmentClassGroupDVO d : items) {
				Element nGroupName = (Element) doc
						.selectSingleNode(String.format("//ListRuleGroups/RuleGroup[@Name='%s']", d.getGroupName()));
				if (nGroupName != null) {
					nGroupName.attribute("Sequence").setValue(d.getSequence());
					nGroupName.attribute("Type").setValue(d.getGroupType());
				} else {
//					LOGGER.debug("new group");
					Element rootListRuleGroup = (Element) doc.selectSingleNode("//ListRuleGroups");
					Element newElement = rootListRuleGroup.addElement("RuleGroup");
					newElement.addAttribute("Type", d.getGroupType());
					newElement.addAttribute("Sequence", d.getSequence());
					newElement.addAttribute("Name", d.getGroupName());
					newElement.addAttribute("GUID", IdGenUtil.randomGuid().toUpperCase());
					newElement.addAttribute("StampGUID", IdGenUtil.randomGuid().toUpperCase());
				}
			}

			String createTokenEx = createTokenEx(esigInfo.get());

			String retXml = doc.getRootElement().asXML();
			LOGGER.debug(retXml);
			Object ret = s.updateRuleGroup(createTokenEx, XMLUtils.escape(retXml));

			var retDoc = XMLUtils.load(ret.toString());
			var nFaultstring = retDoc.selectSingleNode("//faultstring");
			if (nFaultstring != null) {
				setDeployItemFail();
				return nFaultstring.getText();
			}

			return "success.";
		} catch (Exception ex) {
			LOGGER.error(ValueUtil.toString(ex));
			return ex.getMessage();
		}
	}

	@Override
	public void onLoadEquipmentClass(String equipmentClassGuid) {
		this.equipmentClassGuid.set(null);
		this.equipmentClassGuid.set(equipmentClassGuid);
	}

	@Override
	public List<TableView<EquipmentClassGroupDVO>> excelTableList() {
		return Arrays.asList(tvGroup);
	}

	@Override
	public void importExcel(File fromFile, Document doc) {
		LOGGER.debug("{}", doc.asXML());

		try {
			List<EquipmentClassGroupDVO> populateList = ExcelReader.populateList(doc, EquipmentClassGroupDVO.class);
			this.tvGroup.getItems().setAll(populateList);
		} catch (Exception e) {
			showErrorMessage(e);
		}
	}

}
