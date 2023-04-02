/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.eqtree
 *	작성일   : 2021. 12. 9.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.ui.tree.eqtree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kyj.fx.nightmare.actions.ec.eq.hist.EventHsitroyDAO;
import com.kyj.fx.nightmare.comm.DialogUtil;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.MapToTableViewGenerator;
import com.kyj.fx.nightmare.comm.StageStore;
import com.kyj.fx.nightmare.ui.tree.eqtree.EtConfigurationTreeItem.Action;

import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class MenuItemEventHistoryPerform extends MenuItem {

	private EtConfigurationTreeView tvEtConfigurationTree;
	private EventType eventType;

	public enum EventType {
		Start, Update, Complete, Cancel;
	}

	public MenuItemEventHistoryPerform(EtConfigurationTreeView tvEtConfigurationTree, EventType type) {
		super(type.name());
		this.eventType = type;
		this.tvEtConfigurationTree = tvEtConfigurationTree;
		this.setOnAction(this::onAction);
	}

	public MenuItemEventHistoryPerform(EtConfigurationTreeView tv, String text) {
		super(text);
		this.tvEtConfigurationTree = tv;
		this.setOnAction(this::onAction);
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 12. 9.
	 * @param actionevent1
	 */
	private void onAction(ActionEvent ae) {

		TreeItem<EtConfigurationTreeDVO> tv = tvEtConfigurationTree.getSelectionModel().getSelectedItem();
		EtConfigurationTreeItem findEquipment = (EtConfigurationTreeItem) findEquipment(tv);
		String equipmentName = findEquipment.getValue().getDisplayText();
		String equipmentGuid = findEquipment.getValue().getId();
		String eventName = tv.getValue().getDisplayText();
//		System.out.println(equipemtnName);
//		System.out.println(equipmentGuid);

		try {
			EventHsitroyDAO d = new EventHsitroyDAO();

			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("equipmentGuid", equipmentGuid);
			hashMap.put("eventName", eventName);
			hashMap.put("equipmentName", equipmentName);
			List<Map<String, Object>> listEventHistory = d.listEventHistory(hashMap);
			
			TableView<Map<String, Object>> load = new MapToTableViewGenerator(listEventHistory).load();
			FxUtil.createStageAndShow(load, s->{
				s.initOwner(StageStore.getPrimaryStage());
			});

		} catch (Exception e1) {
			DialogUtil.showExceptionDailog(e1);
		}
	}

	/**
	 * 선택된 트리 아이템으로 할당된 부모 노드의 정보를 리턴 (장비 정보.) <br/>
	 * 
	 * @작성자 : KYJ (zaruous@naver.com)
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
