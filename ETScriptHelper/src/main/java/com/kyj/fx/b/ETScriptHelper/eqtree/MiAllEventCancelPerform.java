/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.eqtree
 *	작성일   : 2021. 12. 10.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.eqtree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dom4j.Node;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.kyj.fx.b.ETScriptHelper.actions.deploy.DeployComposite;
import com.kyj.fx.b.ETScriptHelper.actions.ec.eq.eventform.CancelEventPerformService;
import com.kyj.fx.b.ETScriptHelper.comm.AbstractDAO;
import com.kyj.fx.b.ETScriptHelper.comm.FxUtil;
import com.kyj.fx.b.ETScriptHelper.comm.Message;
import com.kyj.fx.b.ETScriptHelper.comm.service.XMLUtils;
import com.kyj.fx.b.ETScriptHelper.eqtree.EtConfigurationTreeItem.Action;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class MiAllEventCancelPerform extends MenuItem implements EventHandler<ActionEvent> {

	private EtConfigurationTreeView tv;

	public MiAllEventCancelPerform(EtConfigurationTreeView tv, String text) {
		super(text);
		this.tv = tv;
		setOnAction(this);
	}

	@Override
	public void handle(ActionEvent ae) {
		TreeItem<EtConfigurationTreeDVO> selectedItem = tv.getSelectionModel().getSelectedItem();
		if (selectedItem == null)
			return;
		TreeItem<EtConfigurationTreeDVO> findEquipmentClass = findEquipmentClass(selectedItem);
		String equipmentClassGuid = findEquipmentClass.getValue().getId();

		EquipmentDAO dao = new EquipmentDAO();
		List<EquipmentEventsDVO> inprogressEvents = dao.getInprogressEvents(equipmentClassGuid);
		Map<String, List<EquipmentEventsDVO>> collect = inprogressEvents.stream().collect(Collectors.groupingBy(v -> {
			return v.getEquipmentName() + "┐" + v.getEventName();
		}));

		var d = new EventPerformDeploy(collect);
		d.load();
		FxUtil.createStageAndShow(d, stage -> {
			stage.setWidth(600d);
			stage.setHeight(600d);
			//MiAllEventCancelPerform_0001=이벤트 취소
			stage.setTitle(Message.getInstance().getMessage("MiAllEventCancelPerform_0001"));
		});
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 3.
	 * @param selectedItem
	 * @return
	 */
	private TreeItem<EtConfigurationTreeDVO> findEquipmentClass(TreeItem<EtConfigurationTreeDVO> selectedItem) {

		EtConfigurationTreeItem _selectedItem = (EtConfigurationTreeItem) selectedItem;
		if (_selectedItem.getAction() == Action.EC)
			return _selectedItem;

		do {
			_selectedItem = (EtConfigurationTreeItem) _selectedItem.getParent();
		} while (!Action.EC.equals(_selectedItem.getAction()));

		return _selectedItem;
	}

	class EquipmentDAO extends AbstractDAO {

		public List<EquipmentEventsDVO> getInprogressEvents(String equipmentClassGuid) {
			StringBuffer sb = new StringBuffer();
			sb.append("select \n");
			sb.append("exe.EventExecuteGUID\n");
			sb.append(", eq.EquipmentClassGUID\n");
			sb.append(", eq.EquipmentGUID\n");
			sb.append(", exe.EquipmentEventGUID \n");
			sb.append(", eq.Name as EquipmentName\n");
			sb.append(", e.Name as EventName\n");
			sb.append(", exe.EventGUID\n");
			sb.append("from DMI_ET.dbo.ET_EventExecute exe inner join\n");
			sb.append("	dmi_et.dbo.ET_Events e\n");
			sb.append("	on exe.EventGUID = e.EventGUID\n");
			sb.append("	inner join DMI_ET.dbo.ET_Equipment eq\n");
			sb.append("	on exe.EquipmentGUID = eq.EquipmentGUID\n");
			sb.append("where 1=1\n");
			sb.append("and eq.EquipmentClassGUID = :equipmentClassGuid\n");
			sb.append("and exe.Deleted = 0\n");
			sb.append("\n");
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("equipmentClassGuid", equipmentClassGuid);
			return query(sb.toString(), paramMap, new BeanPropertyRowMapper<EquipmentEventsDVO>(EquipmentEventsDVO.class));
		}
	}

	class EventPerformDeploy extends DeployComposite<EquipmentEventsDVO> {

		private CancelEventPerformService cancelEventPerformService = new CancelEventPerformService();

		public EventPerformDeploy(Map<String, List<EquipmentEventsDVO>> items) {
			super(items);
		}

		@Override
		public String onProgress(String name, List<EquipmentEventsDVO> items) {

			if (items.size() == 1) {
				String equipmentName = items.get(0).getEquipmentName();
				String eventName = items.get(0).getEventName();
				try {
					var ret = cancelEventPerformService.cancel(equipmentName, eventName);
					if (ret != null) {
						var doc = XMLUtils.load(ret.toString());

						Node selectSingleNode = doc.selectSingleNode("//CS_PARAMETERVALUE");
						if (selectSingleNode != null && "Failed".equals(selectSingleNode.getText())) {
							setFail();
							return doc.selectSingleNode("//CS_MSG").getText();
						}
					}
					setPass();
					return "Pass";
				} catch (Exception e) {
					setFail();
					e.printStackTrace();
					return e.getMessage();
				}
			}
			return "";
		}

	}

}
