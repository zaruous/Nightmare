/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.eqtree
 *	작성일   : 2021. 12. 9.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.eqtree;

import org.dom4j.Document;
import org.dom4j.Node;

import com.kyj.fx.b.ETScriptHelper.actions.ec.eq.eventform.CancelEventPerformService;
import com.kyj.fx.b.ETScriptHelper.comm.DialogUtil;
import com.kyj.fx.b.ETScriptHelper.comm.FxUtil;
import com.kyj.fx.b.ETScriptHelper.comm.ValueUtil;
import com.kyj.fx.b.ETScriptHelper.comm.service.XMLUtils;
import com.kyj.fx.b.ETScriptHelper.eqtree.EtConfigurationTreeItem.Action;

import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class MenuItemEventPerform extends MenuItem {

	private EtConfigurationTreeView tvEtConfigurationTree;
	private EventType eventType;

	public enum EventType {
		Start, Update, Complete, Cancel;
	}

	public MenuItemEventPerform(EtConfigurationTreeView tvEtConfigurationTree, EventType type) {
		super(type.name());
		this.eventType = type;
		this.tvEtConfigurationTree = tvEtConfigurationTree;
		this.setOnAction(this::onAction);
	}

	public MenuItemEventPerform(EtConfigurationTreeView tv, String text) {
		super(text);
		this.tvEtConfigurationTree = tv;
		this.setOnAction(this::onAction);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 9.
	 * @param actionevent1
	 */
	private void onAction(ActionEvent ae) {

		TreeItem<EtConfigurationTreeDVO> tv = tvEtConfigurationTree.getSelectionModel().getSelectedItem();
		EtConfigurationTreeItem findEquipment = (EtConfigurationTreeItem) findEquipment(tv);
		String equipemtnName = findEquipment.getValue().getDisplayText();
//		String equipmentGuid = findEquipment.getValue().getId();
		String eventName = tv.getValue().getDisplayText();
//		System.out.println(equipemtnName);
//		System.out.println(equipmentGuid);

		// DialogUtil.showMessageDialog(String.format("EquipmentGuid : %s, Equipment Name : %s", equipmentGuid, equipemtnName));

		CancelEventPerformService cancelEventPerformService = new CancelEventPerformService();
		try {
			Object cancel = cancelEventPerformService.cancel(equipemtnName, eventName);
//			System.out.println(cancel);

			Document load = XMLUtils.load(cancel.toString());
			Node n = load.selectSingleNode("//CS_MSG");
			if (n != null) {
				DialogUtil.showMessageDialog(n.getText());
			}
		} catch (Exception e) {
			FxUtil.showStatusMessage(ValueUtil.toString(e));
			DialogUtil.showExceptionDailog(e);
		}
	}

	/**
	 * 선택된 트리 아이템으로 할당된 부모 노드의 정보를 리턴 (장비 정보.) <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 9.
	 * @param selectedItem
	 * @return
	 */
	private TreeItem<EtConfigurationTreeDVO> findEquipment(TreeItem<EtConfigurationTreeDVO> selectedItem) {

		EtConfigurationTreeItem _selectedItem = (EtConfigurationTreeItem) selectedItem;
		if (_selectedItem.getAction() == Action.EC_EQ_ITEM)
			return _selectedItem;

		do {
			_selectedItem = (EtConfigurationTreeItem) _selectedItem.getParent();
			if (_selectedItem == null)
				return null;
		} while (!Action.EC_EQ_ITEM.equals(_selectedItem.getAction()));

		return _selectedItem;
	}
}
