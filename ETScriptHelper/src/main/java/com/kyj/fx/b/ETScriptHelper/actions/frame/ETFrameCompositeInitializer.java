/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper
 *	작성일   : 2021. 11. 22.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.frame;

import java.net.URL;
import java.util.Collections;
import java.util.List;

import com.kyj.fx.b.ETScriptHelper.eqtree.EtConfigurationTreeDVO;
import com.kyj.fx.b.ETScriptHelper.eqtree.EtConfigurationTreeItem;
import com.kyj.fx.b.ETScriptHelper.eqtree.EtConfigurationTreeItem.Action;
import com.kyj.fx.b.ETScriptHelper.eqtree.EtConfigurationTreeView;
import com.kyj.fx.b.ETScriptHelper.eqtree.MenuItemEventPerform;
import com.kyj.fx.b.ETScriptHelper.eqtree.MenuItemInfo;
import com.kyj.fx.b.ETScriptHelper.eqtree.MiAllEventCancelPerform;
import com.kyj.fx.b.ETScriptHelper.grid.CodeDVO;

import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class ETFrameCompositeInitializer {

	/**
	 * @최초생성일 2021. 11. 22.
	 */
	private ETFrameComposite etFrameComposite;

	/**
	 * @param etFrameComposite
	 */
	public ETFrameCompositeInitializer(ETFrameComposite etFrameComposite) {
		this.etFrameComposite = etFrameComposite;
	}

	public void init() {
		EtConfigurationTreeView tv = (EtConfigurationTreeView) this.etFrameComposite.apEtTree.getChildren().get(0);
		EtConfigurationTreeItem trEquipmentClass = new EtConfigurationTreeItem("1", "Equipment Class", Action.ROOT);
		tv.setRoot(trEquipmentClass);
		// equipment class childrens

		
		URL urlEquipmentClass = ETFrameCompositeInitializer.class.getResource("/images/FolderOpen.gif");
		Image imgEquipmentClass = new Image(urlEquipmentClass.toExternalForm(), 15, 15, false, false);

		URL urlComponent = ETFrameCompositeInitializer.class.getResource("/images/component.gif");
		Image imgComponent = new Image(urlComponent.toExternalForm(), 15, 15, false, false);

		URL urlGroup = ETFrameCompositeInitializer.class.getResource("/images/Group.png");
		Image imgGroup = new Image(urlGroup.toExternalForm(), 15, 15, false, false);

		URL urlRule = ETFrameCompositeInitializer.class.getResource("/images/Rule.png");
		Image imgRule = new Image(urlRule.toExternalForm(), 15, 15, false, false);
		
		EquipmentClassDQM dqm = new EquipmentClassDQM();
		List<CodeDVO> listEquipmentClasses = dqm.listEquipmentClasses(Collections.emptyMap());
		listEquipmentClasses.forEach(v -> {

			EtConfigurationTreeItem tiEtClass = new EtConfigurationTreeItem(new ImageView(imgEquipmentClass), v.getCode(), v.getNm(),
					Action.EC);
			tiEtClass.getChildren().add(new EtConfigurationTreeItem(new ImageView(imgComponent), "1-1", "General", Action.EC_GENERAL));
			tiEtClass.getChildren()
					.add(new EtConfigurationTreeItem(new ImageView(imgComponent), "1-2", "Custom Properties", Action.EC_CUSTOM_PROP));
			tiEtClass.getChildren()
					.add(new EtConfigurationTreeItem(new ImageView(imgComponent), "1-3", "Event States", Action.EC_EVENT_STATES));
			EtConfigurationTreeItem trEvents = new EtConfigurationTreeItem(new ImageView(imgComponent), "1-4", "Events", Action.EC_EVENTS);
			// trEvents.getChildren().add(new EtConfigurationTreeItem(new ImageView(imgComponent), "1-4-1", "Scripts", Action.EC_SCRIPT));

			tiEtClass.getChildren().add(trEvents);
			tiEtClass.getChildren().add(new EtConfigurationTreeItem(new ImageView(imgComponent), "1-5", "Workflows", Action.EC_WORKFLOW));
			tiEtClass.getChildren().add(new EtConfigurationTreeItem(new ImageView(imgGroup), "1-6", "Group", Action.EC_GROUP));
			tiEtClass.getChildren().add(new EtConfigurationTreeItem(new ImageView(imgRule), "1-7", "Rule", Action.EC_RULE));

			EtConfigurationTreeItem trEquipments = new EtConfigurationTreeItem(new ImageView(imgComponent), "1-8", "Equipments",
					Action.EC_EQ);
			tiEtClass.getChildren().add(trEquipments);

			trEquipmentClass.getChildren().add(tiEtClass);
		});

		tv.setOriginalRoot(trEquipmentClass);

		tv.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, new EventHandler<ContextMenuEvent>() {
			@Override
			public void handle(ContextMenuEvent ev) {
				TreeItem<EtConfigurationTreeDVO> selectedItem = tv.getSelectionModel().getSelectedItem();
				if (selectedItem == null)
					return;
				EtConfigurationTreeItem ti = (EtConfigurationTreeItem) selectedItem;

				ContextMenu contextMenu = new ContextMenu();
				contextMenu.getItems().add(new MenuItemInfo(tv, "Info"));

				switch (ti.getAction()) {
				case EC_EQ:
					MiAllEventCancelPerform mAllEventCalcen = new MiAllEventCancelPerform(tv,"All Event Cancel Perform");
					contextMenu.getItems().add(mAllEventCalcen);
					break;
				case EC_EQ_EVENT_ITEMS:
					MenuItemEventPerform mEventPerform = new MenuItemEventPerform(tv,"Event Cancel Perform");
					contextMenu.getItems().add(mEventPerform);
					
					break;
				default:
					break;
				}
				tv.setContextMenu(contextMenu);
			}
		});

		// tv.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
		// @Override
		// public void handle(ContextMenuEvent ev) {
		// ev.consume();
		// Node intersectedNode = ev.getPickResult().getIntersectedNode();
		// System.out.println(intersectedNode);
		// }
		// });
	}

}
