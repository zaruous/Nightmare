/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper
 *	작성일   : 2021. 11. 22.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.frame;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.actions.wf.EMRWorkflowInstanceDQM;
import com.kyj.fx.nightmare.actions.wf.ETWorkflowState;
import com.kyj.fx.nightmare.actions.wf.WorkflowInstanceService;
import com.kyj.fx.nightmare.actions.wf.WorkflowInstanceTxService;
import com.kyj.fx.nightmare.actions.wf.WorkflowStatusService;
import com.kyj.fx.nightmare.comm.DialogUtil;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.ResourceLoader;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.comm.service.XMLUtils;
import com.kyj.fx.nightmare.eqtree.EtConfigurationTreeDVO;
import com.kyj.fx.nightmare.eqtree.EtConfigurationTreeItem;
import com.kyj.fx.nightmare.eqtree.EtConfigurationTreeView;
import com.kyj.fx.nightmare.eqtree.MenuItemEventHistoryPerform;
import com.kyj.fx.nightmare.eqtree.MenuItemEventPerform;
import com.kyj.fx.nightmare.eqtree.MenuItemExportEtScript;
import com.kyj.fx.nightmare.eqtree.MenuItemImportEtScript;
import com.kyj.fx.nightmare.eqtree.MenuItemInfo;
import com.kyj.fx.nightmare.eqtree.MiAllEventCancelPerform;
import com.kyj.fx.nightmare.eqtree.EtConfigurationTreeItem.Action;
import com.kyj.fx.nightmare.grid.CodeDVO;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.util.Callback;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class ETFrameCompositeInitializer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ETFrameCompositeInitializer.class);
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
			// trEvents.getChildren().add(new EtConfigurationTreeItem(new
			// ImageView(imgComponent), "1-4-1", "Scripts", Action.EC_SCRIPT));

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
				case EC:

					Menu mExport = new Menu("Export");
					MenuItemExportEtScript miExportETScript = new MenuItemExportEtScript(tv, "ET Script");
					mExport.getItems().add(miExportETScript);

					Menu miImport = new Menu("Import");
					MenuItemImportEtScript miImportETScript = new MenuItemImportEtScript(tv, "ET Script");
					miImport.getItems().add(miImportETScript);

					contextMenu.getItems().addAll(mExport, miImport);

					break;
				case EC_EQ:
					MiAllEventCancelPerform mAllEventCalcen = new MiAllEventCancelPerform(tv, "All Event Cancel Perform");
					contextMenu.getItems().add(mAllEventCalcen);
					break;
				case EC_EQ_EVENT_ITEMS:
					MenuItemEventPerform mEventPerform = new MenuItemEventPerform(tv, "Event Cancel Perform");
					contextMenu.getItems().add(mEventPerform);
					MenuItemEventHistoryPerform miEventHistory = new MenuItemEventHistoryPerform(tv, "Event history");
					contextMenu.getItems().add(miEventHistory);
					break;
				case EC_EVENTS_ITEM:
					break;
				case EQ_WORKFLOW_CLASS:
					
					TreeItem<EtConfigurationTreeDVO> tiEquipment = etFrameComposite.findEquipment(selectedItem);
					if (tiEquipment == null)
						return;
					
					
					MenuItem miCreateNewWorkflow = new MenuItem("Create Workflow Instance");
					miCreateNewWorkflow.setOnAction(e -> miCreateNewWorkflowOnAction(tiEquipment.getValue(), selectedItem.getValue()));
					contextMenu.getItems().add(miCreateNewWorkflow);
					break;
				case EQ_WORKFLOW_INSTANCE:
					MenuItem miViewWorkflow = new MenuItem("View Workflow");
					miViewWorkflow.setOnAction(e -> miViewWorkflowOnAction(e));
					contextMenu.getItems().add(miViewWorkflow);

					MenuItem miReview = new MenuItem("Review");
					miReview.setOnAction(e -> miReviewOnAction(e));
					contextMenu.getItems().add(miReview);

					Map<String, Object> properties = selectedItem.getValue().getProperties();
					Short status = (Short) properties.get("Status");
					
					if(ETWorkflowState.Created.getCode() == status )
					{
						miReview.setText("Start");
//						MenuItem miStart = new MenuItem("Start");
//						miStart.setOnAction(e -> miViewWorkflowOnAction(e));
//						contextMenu.getItems().addAll(new SeparatorMenuItem(), miStart);
					}
					
					else if (ETWorkflowState.Complete.getCode() == status || ETWorkflowState.Force_Complete.getCode() == status) {
						MenuItem miClose = new MenuItem("Close");
						miClose.setOnAction(e -> miCloseOnAction(selectedItem.getValue()));
						contextMenu.getItems().addAll(new SeparatorMenuItem(), miClose);
					}

					break;
				default:
					break;
				}
				tv.setContextMenu(contextMenu);
			}

			

		});

	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2022. 7. 28. 
	 * @param value
	 * @return
	 */
//	private void miStartOnAction(EtConfigurationTreeDVO value) {
//		String fullUrl = String.format("%s/emrwf/app/workfloworder?order=%s", rootUrl, orderNumber);
//
//		try {
//			Desktop.getDesktop().browse(new URI(fullUrl));
//		} catch (IOException | URISyntaxException e1) {
//			DialogUtil.showExceptionDailog(e1);
//		}
//	}
	
	/**
	 * @param etConfigurationTreeDVO 
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2022. 7. 28.
	 * @param d
	 * @return
	 */
	private void miCreateNewWorkflowOnAction(EtConfigurationTreeDVO equipment, EtConfigurationTreeDVO workflow) {

		DialogUtil.showESigDialog("1", "Syncade", "DMI ET", "WorkFlowInstances", "0", new Callback<Map<String, String>, Void>() {

			@Override
			public Void call(Map<String, String> pair) {
				String token = pair.get("token");
				if (ValueUtil.isEmpty(token)) {
					String msg = pair.get("err");
					DialogUtil.showMessageDialog("SAToken required.\n" + msg);
					return null;
				}
				String ret = "";
				try {
					
					WorkflowInstanceService s = new WorkflowInstanceService();
					WorkflowInstanceTxService stx = new WorkflowInstanceTxService();
					
					String createOrderXml = s.createOrderXml("0", workflow.getId());
					
					ret = stx.createOrder(token, equipment.getId(), workflow.getId(), XMLUtils.escape(createOrderXml));
					
					
					FxUtil.createStageAndShow(new TextArea(ret), stage ->{});
					
//					String workflowInstanceId = dqm
//							.getEmrWorkflowInstanceId((String) selectedItem.getProperties().get("WorkFlowInstanceGUID"));
//					int code = ETWorkflowState.Archived.getCode();
//					WorkflowStatusService service = new WorkflowStatusService();
//					ret = service.updateOrderStatus(token, workflowInstanceId, Objects.toString(code));
					// <Order GUID = '9308' Name = 'OP_COM_VAR_TEST' OrderType =
					// '8' OrderStatus = '5' StampGUID =
					// '5F0EC42D-4B3E-4F2A-ACDC-8AB5B75FA501' >

				} catch (Exception e) {
					DialogUtil.showExceptionDailog(e);
				}
				return null;
			}
		});

	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2022. 7. 28.
	 * @param e
	 */
	private void miViewWorkflowOnAction(ActionEvent e) {
		String rootUrl = ResourceLoader.getInstance().get(ResourceLoader.SYNCADE_RA_ROOT_URL);
		EtConfigurationTreeView tvEtConfigurationTree = this.etFrameComposite.getTvEtConfigurationTree();
		TreeItem<EtConfigurationTreeDVO> selectedItem = tvEtConfigurationTree.getSelectionModel().getSelectedItem();
		if (selectedItem == null)
			return;
		EtConfigurationTreeDVO value = selectedItem.getValue();

		Object oOrderNumber = value.getProperties().get("OrderNumber");
		if (ValueUtil.isEmpty(oOrderNumber)) {
			DialogUtil.showMessageDialog("OrderNumber is empty.");
			return;
		}
		String orderNumber = oOrderNumber.toString();

		String fullUrl = String.format("%s/emrwf/app/workfloworder?order=%s", rootUrl, orderNumber);

		try {
			Desktop.getDesktop().browse(new URI(fullUrl));
		} catch (IOException | URISyntaxException e1) {
			DialogUtil.showExceptionDailog(e1);
		}
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2022. 7. 28.
	 * @param etConfigurationTreeDVO
	 * @return
	 */
	private void miCloseOnAction(EtConfigurationTreeDVO selectedItem) {
		DialogUtil.showESigDialog("5", "Syncade", "DMI ET", "WorkFlowInstances_Close", "0", new Callback<Map<String, String>, Void>() {

			@Override
			public Void call(Map<String, String> pair) {
				String token = pair.get("token");
				if (ValueUtil.isEmpty(token)) {
					String msg = pair.get("err");
					DialogUtil.showMessageDialog("SAToken required.\n" + msg);
					return null;
				}
				String ret = "";
				try {

					EMRWorkflowInstanceDQM dqm = new EMRWorkflowInstanceDQM();
					String workflowInstanceId = dqm
							.getEmrWorkflowInstanceId((String) selectedItem.getProperties().get("WorkFlowInstanceGUID"));
					int code = ETWorkflowState.Archived.getCode();
					WorkflowStatusService service = new WorkflowStatusService();
					ret = service.updateOrderStatus(token, workflowInstanceId, Objects.toString(code));
					
					LOGGER.debug(ret);
					// <Order GUID = '9308' Name = 'OP_COM_VAR_TEST' OrderType =
					// '8' OrderStatus = '5' StampGUID =
					// '5F0EC42D-4B3E-4F2A-ACDC-8AB5B75FA501' >

				} catch (Exception e) {
					DialogUtil.showExceptionDailog(e);
				}
				return null;
			}
		});

	}

	/**
	 * 리뷰 페이지 호출. <br/>
	 * 
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2022. 7. 22.
	 * @param e
	 */
	public void miReviewOnAction(ActionEvent e) {
		String rootUrl = ResourceLoader.getInstance().get(ResourceLoader.SYNCADE_RA_ROOT_URL);
		EtConfigurationTreeView tvEtConfigurationTree = this.etFrameComposite.getTvEtConfigurationTree();
		TreeItem<EtConfigurationTreeDVO> selectedItem = tvEtConfigurationTree.getSelectionModel().getSelectedItem();
		if (selectedItem == null)
			return;
		EtConfigurationTreeDVO value = selectedItem.getValue();

		Object oOrderNumber = value.getProperties().get("OrderNumber");
		if (ValueUtil.isEmpty(oOrderNumber)) {
			DialogUtil.showMessageDialog("OrderNumber is empty.");
			return;
		}

		String orderNumber = oOrderNumber.toString();

		String fullUrl = String.format("%s/emrwf/manufacturing/review?ordernumber=%s", rootUrl, orderNumber);

		try {
			Desktop.getDesktop().browse(new URI(fullUrl));
		} catch (IOException | URISyntaxException e1) {
			DialogUtil.showExceptionDailog(e1);
		}
	}

}
