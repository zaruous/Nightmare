/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper
 *	작성일   : 2021. 11. 19.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.frame;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.kyj.fx.b.ETScriptHelper.ETScriptComposite;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnCommitService;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnExcelTableViewList;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnReload;
import com.kyj.fx.b.ETScriptHelper.actions.comm.util.ExportEtScript;
import com.kyj.fx.b.ETScriptHelper.actions.ec.ec.EquipmentClassTab;
import com.kyj.fx.b.ETScriptHelper.actions.ec.ec.events.EquipmentClassEventTab;
import com.kyj.fx.b.ETScriptHelper.actions.ec.ec.group.EquipmentClassGroupTab;
import com.kyj.fx.b.ETScriptHelper.actions.ec.ec.par.EquipmentParameterTab;
import com.kyj.fx.b.ETScriptHelper.actions.ec.ec.rule.EquipmentClassRuleTab;
import com.kyj.fx.b.ETScriptHelper.actions.ec.ec.scripts.EquipmentClassEventScriptTab;
import com.kyj.fx.b.ETScriptHelper.actions.ec.eq.cp.EquipmentCpTab;
import com.kyj.fx.b.ETScriptHelper.actions.ec.eq.states.EquipmentEventStateTab;
import com.kyj.fx.b.ETScriptHelper.actions.support.ETScriptHelperComposite;
import com.kyj.fx.b.ETScriptHelper.actions.wf.ETWorkflowState;
import com.kyj.fx.b.ETScriptHelper.comm.DialogUtil;
import com.kyj.fx.b.ETScriptHelper.comm.ExcelFileChooserHandler;
import com.kyj.fx.b.ETScriptHelper.comm.FileUtil;
import com.kyj.fx.b.ETScriptHelper.comm.FxUtil;
import com.kyj.fx.b.ETScriptHelper.comm.Message;
import com.kyj.fx.b.ETScriptHelper.comm.StageStore;
import com.kyj.fx.b.ETScriptHelper.comm.ValueUtil;
import com.kyj.fx.b.ETScriptHelper.eqtree.EquipmentClassDVO;
import com.kyj.fx.b.ETScriptHelper.eqtree.EquipmentDAO;
import com.kyj.fx.b.ETScriptHelper.eqtree.EtConfigurationTreeDVO;
import com.kyj.fx.b.ETScriptHelper.eqtree.EtConfigurationTreeItem;
import com.kyj.fx.b.ETScriptHelper.eqtree.EtConfigurationTreeItem.Action;
import com.kyj.fx.b.ETScriptHelper.eqtree.EtConfigurationTreeView;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;

/**
 * 어플리케이션의 전체적인 구조에 대한 설정 담당. <br/>
 * 
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class ETFrameComposite extends AbstractCommonsApp {

	private static final Logger LOGGER = LoggerFactory.getLogger(ETFrameComposite.class);

	@FXML
	TabPane tpEtManagement;

	EquipmentClassTab tabGeneral;
	EquipmentEventStateTab tabEquipmentEventStates;
	EquipmentClassEventTab tabEquipmentClassEventTab;
	EquipmentClassEventScriptTab tabEquipmentClassEventScriptTab;
	EquipmentClassRuleTab tabEquipmentClassRuleTab;
	EquipmentClassGroupTab tabEquipmentClassGroupTab;
	EquipmentParameterTab tabEquipmentParameterTab;
	EquipmentCpTab tabEquipmentCpTab;

	// EquipmentClassTab _tabGeneral;
	// EquipmentClassEventStateTab _tabEquipmentEventStates;

	@FXML
	Label lblStatus;
	@FXML
	AnchorPane apEtTree;
	@FXML
	TextField txtClassNameFilter;
	// @FXML
	// TableView tvExportItem;

	ETScriptComposite etScriptComposite;

	@FXML
	private Button btnCommit, btnReload, btnExportExcel, btnImportExcel;

	public ETFrameComposite() {
		FXMLLoader loader = FxUtil.newLaoder();
		loader.setLocation(ETFrameComposite.class.getResource("ETFrameView.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}

	private EtConfigurationTreeView tvEtConfigurationTree;

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2022. 7. 22.
	 * @return
	 */
	public EtConfigurationTreeView getTvEtConfigurationTree() {
		return tvEtConfigurationTree;
	}

	@FXML
	public void initialize() {
		this.tabGeneral = new EquipmentClassTab();
		this.tabEquipmentCpTab = new EquipmentCpTab();
		this.tabEquipmentEventStates = new EquipmentEventStateTab();
		this.tabEquipmentClassEventTab = new EquipmentClassEventTab();
		this.tabEquipmentClassEventScriptTab = new EquipmentClassEventScriptTab();
		this.tabEquipmentClassRuleTab = new EquipmentClassRuleTab();
		this.tabEquipmentClassGroupTab = new EquipmentClassGroupTab();
		this.tabEquipmentParameterTab = new EquipmentParameterTab();

		tpEtManagement.getTabs().addAll(this.tabGeneral, tabEquipmentCpTab, this.tabEquipmentEventStates, this.tabEquipmentClassEventTab,
				this.tabEquipmentClassEventScriptTab, tabEquipmentClassRuleTab, this.tabEquipmentClassGroupTab, tabEquipmentParameterTab);

		String externalForm = ETFrameComposite.class.getResource("/images/excel16.png").toExternalForm();
		btnExportExcel.setGraphic(new ImageView(new Image(externalForm, 15.0, 15.0, false, false)));

		externalForm = ETFrameComposite.class.getResource("/images/ExcelDown.png").toExternalForm();
		btnImportExcel.setGraphic(new ImageView(new Image(externalForm, 15.0, 15.0, false, false)));

		this.tvEtConfigurationTree = new EtConfigurationTreeView();
		tvEtConfigurationTree.addEventHandler(MouseEvent.MOUSE_CLICKED, this::tvEtConfigurationTreeOnClick);

		apEtTree.getChildren().add(tvEtConfigurationTree);
		AnchorPane.setLeftAnchor(tvEtConfigurationTree, 5.0);
		AnchorPane.setTopAnchor(tvEtConfigurationTree, 5.0);
		AnchorPane.setRightAnchor(tvEtConfigurationTree, 5.0);
		AnchorPane.setBottomAnchor(tvEtConfigurationTree, 5.0);

		this.tpEtManagement.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {

			@Override
			public void changed(ObservableValue<? extends Tab> arg0, Tab arg1, Tab tab) {
				if (tab == null)
					return;

				if (tab instanceof OnCommitService) {
					var b = ((OnCommitService) tab).enableCommitButton();
					btnCommit.setDisable(!b);
				} else {
					btnCommit.setDisable(true);
				}

				if (tab instanceof OnExcelTableViewList) {
					boolean b = false;
					b = ((OnExcelTableViewList) tab).enableExcelExportButton();
					btnExportExcel.setDisable(!b);
					b = ((OnExcelTableViewList) tab).enableExcelImportButton();
					btnImportExcel.setDisable(!b);
				} else {
					btnExportExcel.setDisable(true);
					btnImportExcel.setDisable(true);
				}

				if (tab instanceof OnReload) {
					boolean b = false;
					b = ((OnReload) tab).enableReloadButton();
					btnReload.setDisable(!b);
				} else {
					btnReload.setDisable(true);
				}

			}
		});

		this.initializeApplicationSettings(this);

	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 24.
	 * @param etFrameComposite
	 */
	private void initializeApplicationSettings(ETFrameComposite etFrameComposite) {
		ETFrameCompositeInitializer etFrameCompositeInitializer = new ETFrameCompositeInitializer(this);
		etFrameCompositeInitializer.init();
	}

	@FXML
	public void miExportEtSccriptsOnAction() {

		// if(this.currentEquipmentScript.get() == null)
		// return;
		ComboBox<EquipmentClassDVO> cbEquipmentClass = this.etScriptComposite.getCbEquipmentClass();
		EquipmentClassDVO selectedItem = cbEquipmentClass.getSelectionModel().getSelectedItem();
		if (selectedItem == null)
			return;

		// if (ValueUtil.isEmpty(equipmentName))
		// return;
		File outDir = DialogUtil.showDirectoryDialog(FxUtil.getWindow(this));
		if (outDir == null)
			return;

		ExportEtScript exportEtScript = new ExportEtScript();
		exportEtScript.setOutRootDir(outDir);
		exportEtScript.setEquipmentClassName(selectedItem.getEquipmentClassName());
		try {
			exportEtScript.export();
			DialogUtil.showMessageDialog(FxUtil.getWindow(this), "Complete.");
		} catch (Exception e) {
			DialogUtil.showExceptionDailog(FxUtil.getWindow(this), e);
		}
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 19.
	 * @param message
	 */
	public void showStatusMessage(String message) {
		this.lblStatus.setText(message);
	}

	@FXML
	public void txtClassNameFilterOnKeyPressed(KeyEvent ke) {
		String text = txtClassNameFilter.getText();
		this.tvEtConfigurationTree.filter(text);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 22.
	 */
	@FXML
	public void miImportEtSccriptsOnAction() {

	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 3.
	 * @param selectedItem
	 * @return
	 */
	public TreeItem<EtConfigurationTreeDVO> findEquipmentClass(TreeItem<EtConfigurationTreeDVO> selectedItem) {

		EtConfigurationTreeItem _selectedItem = (EtConfigurationTreeItem) selectedItem;
		if (_selectedItem.getAction() == Action.EC)
			return _selectedItem;

		do {
			_selectedItem = (EtConfigurationTreeItem) _selectedItem.getParent();
		} while (!Action.EC.equals(_selectedItem.getAction()));

		return _selectedItem;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 3.
	 * @param selectedItem
	 * @return
	 */
	public TreeItem<EtConfigurationTreeDVO> findEquipment(TreeItem<EtConfigurationTreeDVO> selectedItem) {

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

	public void tvEtConfigurationTreeOnClick(MouseEvent me) {

		if (me.getButton() != MouseButton.PRIMARY)
			return;

		if (me.getClickCount() == 1) {
			if (me.isConsumed())
				return;
			me.consume();

			TreeItem<EtConfigurationTreeDVO> selectedItem = tvEtConfigurationTree.getSelectionModel().getSelectedItem();
			if (!(selectedItem instanceof EtConfigurationTreeItem))
				return;
			EtConfigurationTreeItem source = (EtConfigurationTreeItem) selectedItem;

			FxUtil.showStatusMessage("[" + source.getAction().toString() + "] " + source.getValue().getDisplayText());
			TreeItem<EtConfigurationTreeDVO> tiEquipmentClass = findEquipmentClass(selectedItem);// source.getParent();
			String equipmentClassGuid = tiEquipmentClass.getValue().getId();

			TreeItem<EtConfigurationTreeDVO> findEquipment = findEquipment(selectedItem);
			List<EtConfigurationTreeItem> listEquipments = Collections.emptyList();
			String equipmentGuid = null;
			String equipmentName = null;
			String eventGuid = null;
			if (findEquipment == null) {

				if (source.getAction() == Action.EC_EQ_ITEM) {
					equipmentGuid = selectedItem.getValue().getId();
					equipmentName = selectedItem.getValue().getDisplayText();
				}

			} else {
				equipmentGuid = findEquipment.getValue().getId();
				equipmentName = findEquipment.getValue().getDisplayText();
			}

			if (source.getAction() == Action.EC_EVENTS_ITEM) {
				eventGuid = source.getValue().getId();
			}

			LOGGER.debug("\nequipmentClassGuid : {}\nequipmentGuid : {}\nequipmentName : {}\nAction: {}", equipmentClassGuid, equipmentGuid,
					equipmentName, source.getAction());

			// tabGeneral.onLoadEquipmentClass("");
			// tabEquipmentEventStates.onLoadEquipment("");

			this.tpEtManagement.getTabs().forEach(tab -> tab.setDisable(true));

			Action action = source.getAction();
			switch (action) {
			case EC:
				List<EtConfigurationTreeItem> collect = selectedItem.getChildren().stream()
						.filter(chi -> chi instanceof EtConfigurationTreeItem).map(chi -> ((EtConfigurationTreeItem) chi)).filter(chi -> {
							if (chi instanceof EtConfigurationTreeItem) {
								if (Action.EC_EQ == chi.getAction() || Action.EC_EVENTS == chi.getAction()
										|| Action.EC_WORKFLOW == chi.getAction()) {
									return true;
								}
							}
							return false;
						}).collect(Collectors.toList());

				collect.forEach(new Consumer<EtConfigurationTreeItem>() {
					@Override
					public void accept(EtConfigurationTreeItem chi) {
						String id = source.getValue().getId();

						if (Action.EC_EQ == chi.getAction()) {
							List<EtConfigurationTreeItem> listEquipments = listEquipments(id);
							chi.getChildren().setAll(listEquipments);
							// chi.setExpanded(true);
						} else if (Action.EC_EVENTS == chi.getAction()) {
							List<EtConfigurationTreeItem> listEvents = listEvents(id);
							chi.getChildren().setAll(listEvents);
						} else if (Action.EC_WORKFLOW == chi.getAction()) {
							List<EtConfigurationTreeItem> listWorkflows = listWorkflows(id);
							chi.getChildren().setAll(listWorkflows);
						}

					}

				});
				tpEtManagement.getSelectionModel().select(tabGeneral);
				tabGeneral.onLoadEquipmentClass(equipmentClassGuid);
				tabGeneral.setDisable(false);
				break;
			case EC_GENERAL:
				tabGeneral.onLoadEquipmentClass(equipmentClassGuid);
				tpEtManagement.getSelectionModel().select(tabGeneral);
				tabGeneral.setDisable(false);
				break;
			case EC_CUSTOM_PROP:
				tabGeneral.onLoadEquipmentClass(equipmentClassGuid);
				tabGeneral.setDisable(false);
				break;
			case EC_EVENT_STATES:
				tabGeneral.onLoadEquipmentClass(equipmentClassGuid);
				tabGeneral.setDisable(false);
				break;
			case EC_EVENTS:
				List<EtConfigurationTreeItem> listEvents = listEvents(equipmentClassGuid);
				selectedItem.getChildren().setAll(listEvents);
				this.tabGeneral.onLoadEquipmentClass(equipmentClassGuid);
				tabGeneral.setDisable(false);

				break;
			case EC_EVENTS_ITEM:
				this.tabGeneral.onLoadEquipmentClass(equipmentClassGuid);
				this.tabGeneral.setDisable(false);

				this.tabEquipmentClassEventScriptTab.onLoadEquipmentClassEvent(equipmentClassGuid, eventGuid);
				this.tabEquipmentClassEventScriptTab.setDisable(false);

				this.tabEquipmentClassEventTab.onEquipmenbtClassEventScript(equipmentClassGuid, eventGuid);
				this.tabEquipmentClassEventTab.setDisable(false);

				this.tabEquipmentParameterTab.onLoadEquipmentClassEvent(equipmentClassGuid, eventGuid);
				this.tabEquipmentParameterTab.setDisable(false);

				// tpEtManagement.getSelectionModel().select(tabEquipmentClassEventTab);

				break;
			case EC_WORKFLOW:
				tabGeneral.onLoadEquipmentClass(equipmentClassGuid);
				tabGeneral.setDisable(false);
				break;
			case EC_WORKFLOW_CLASS:

				break;
			case EQ_WORKFLOW:

				// if(selectedItem.getChildren().isEmpty())
				selectedItem.getChildren().setAll(listEqWorkflows(equipmentClassGuid));

				break;
			case EQ_WORKFLOW_CLASS:
				List<EtConfigurationTreeItem> listWorkflowInstance = listWorkflowInstance(equipmentGuid, selectedItem.getValue().getId());
				selectedItem.getChildren().setAll(listWorkflowInstance);
				return;
			case EQ_WORKFLOW_INSTANCE:

				break;
			case EC_RULE:
				tabGeneral.onLoadEquipmentClass(equipmentClassGuid);
				tabGeneral.setDisable(false);
				tabEquipmentClassRuleTab.onLoadEquipmentClass(equipmentClassGuid);
				tabEquipmentClassRuleTab.setDisable(false);
				tpEtManagement.getSelectionModel().select(tabEquipmentClassRuleTab);
				break;
			case EC_GROUP:
				tabGeneral.onLoadEquipmentClass(equipmentClassGuid);
				tabGeneral.setDisable(false);
				this.tabEquipmentClassGroupTab.onLoadEquipmentClass(equipmentClassGuid);
				this.tabEquipmentClassGroupTab.setDisable(false);
				tpEtManagement.getSelectionModel().select(tabEquipmentClassGroupTab);
				break;
			case EC_EQ:
				if (findEquipment == null) {
					listEquipments = listEquipments(equipmentClassGuid);
				} else {
					listEquipments = listChildEquipments(equipmentGuid);
				}

				tabGeneral.setDisable(false);
				tabEquipmentCpTab.setDisable(false);
				tabEquipmentEventStates.setDisable(false);

				// listEquipments.add(new )
				selectedItem.getChildren().setAll(listEquipments);
				tabGeneral.onLoadEquipmentClass(equipmentClassGuid);

				tpEtManagement.getSelectionModel().select(tabGeneral);
				break;
			case EC_EQ_ITEM:

				tabGeneral.setDisable(false);
				tabEquipmentCpTab.setDisable(false);
				tabEquipmentEventStates.setDisable(false);
				tabGeneral.onLoadEquipmentClass(equipmentClassGuid);
				tabEquipmentCpTab.onLoadEquipment(equipmentGuid);

				break;
			case EC_EQ_EVENT_STATES:

				// tabGeneral.onLoadEquipmentClass(equipmentClassGuid);
				tabGeneral.setDisable(false);
				tabEquipmentCpTab.setDisable(false);

				tabEquipmentEventStates.onLoadEquipment(equipmentGuid);
				tabEquipmentEventStates.setDisable(false);
				tpEtManagement.getSelectionModel().select(tabEquipmentEventStates);
				break;

			case EC_EQ_EVENTS:
				List<EtConfigurationTreeItem> listEquipmentEvents = listEquipmentEvents(equipmentGuid);
				selectedItem.getChildren().setAll(listEquipmentEvents);
				break;
			case EQ_CUSTOM_PROP:

				tabGeneral.setDisable(false);
				tabEquipmentCpTab.setDisable(false);
				tabEquipmentEventStates.setDisable(false);

				tabEquipmentCpTab.onLoadEquipment(equipmentGuid);
				tpEtManagement.getSelectionModel().select(tabEquipmentCpTab);
				break;
			}
		}

	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2022. 7. 22.
	 * @param equipmentClassGuid
	 * @return
	 */
	private List<EtConfigurationTreeItem> listEqWorkflows(String equipmentClassGuid) {
		return listWorkflows(equipmentClassGuid, v -> {
			v.setAction(Action.EQ_WORKFLOW_CLASS);
			return v;
		});
	}

	private List<EtConfigurationTreeItem> listWorkflowInstance(String equipmentGuid, String workflowId) {

		if (ValueUtil.isEmpty(equipmentGuid))
			return Collections.emptyList();

		try {
			EquipmentDAO dao = new EquipmentDAO();
			HashMap<String, Object> hashMap = new HashMap<>();
			hashMap.put("equipmentGuid", equipmentGuid);
			hashMap.put("workflowId", workflowId);
			return dao.listWorkflowInstance(hashMap, new RowMapper<EtConfigurationTreeItem>() {

				URL url = ETFrameCompositeInitializer.class.getResource("/images/WorkflowInstance.png");
				Image img = new Image(url.toExternalForm(), 15, 15, false, false);

				@Override
				public EtConfigurationTreeItem mapRow(ResultSet rs, int rowNum) throws SQLException {

					String orderNumber = rs.getString("OrderNumber");
					EtConfigurationTreeItem etConfigurationTreeItem = new EtConfigurationTreeItem(new ImageView(img),
							rs.getString("WorkflowGuid"), orderNumber, Action.EQ_WORKFLOW_INSTANCE);

					EtConfigurationTreeDVO value = etConfigurationTreeItem.getValue();

					String stateNm = "";
					int columnCount = rs.getMetaData().getColumnCount();
					for (int i = 1; i < columnCount; i++) {
						var colname = rs.getMetaData().getColumnName(i);
						var v = rs.getObject(colname);
						value.addProperty(colname, v);

						if ("Status".equals(colname)) {
							value.addProperty("StatusNm", stateNm = ETWorkflowState.getText(Integer.parseInt(v.toString(), 10)));
						}
					}
					
					
					value.setDisplayText(String.format("%s(%s)", orderNumber, stateNm));
					

					// value.setProperties(hashMap);

					return etConfigurationTreeItem;
				}
			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2022. 7. 22.
	 * @param equipmentClassGuid
	 * @return
	 */
	private List<EtConfigurationTreeItem> listWorkflows(String equipmentClassGuid) {
		return listWorkflows(equipmentClassGuid, v -> v);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2022. 7. 21.
	 * @param equipmentClassGuid
	 * @return
	 */
	private List<EtConfigurationTreeItem> listWorkflows(String equipmentClassGuid,
			Function<EtConfigurationTreeItem, EtConfigurationTreeItem> dataHandler) {

		if (ValueUtil.isEmpty(equipmentClassGuid))
			return Collections.emptyList();

		try {
			EquipmentDAO dao = new EquipmentDAO();
			HashMap<String, Object> hashMap = new HashMap<>();
			hashMap.put("equipmentClassGuid", equipmentClassGuid);
			return dao.listWorkflow(hashMap, new RowMapper<EtConfigurationTreeItem>() {

				URL url = ETFrameCompositeInitializer.class.getResource("/images/Workflow.png");
				Image img = new Image(url.toExternalForm(), 15, 15, false, false);

				@Override
				public EtConfigurationTreeItem mapRow(ResultSet rs, int rowNum) throws SQLException {

					EtConfigurationTreeItem etConfigurationTreeItem = new EtConfigurationTreeItem(new ImageView(img),
							rs.getString("WorkflowGuid"), rs.getString("name"), Action.EC_WORKFLOW_CLASS);
					return dataHandler.apply(etConfigurationTreeItem);
				}
			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List<EtConfigurationTreeItem> listEvents(String equipmentClassGuid) {
		if (ValueUtil.isEmpty(equipmentClassGuid))
			return Collections.emptyList();

		try {
			EquipmentDAO dao = new EquipmentDAO();
			HashMap<String, Object> hashMap = new HashMap<>();
			hashMap.put("equipmentClassGuid", equipmentClassGuid);
			return dao.listEvent(hashMap, new RowMapper<EtConfigurationTreeItem>() {

				URL url = ETFrameCompositeInitializer.class.getResource("/images/flash32.gif");
				Image img = new Image(url.toExternalForm(), 15, 15, false, false);

				@Override
				public EtConfigurationTreeItem mapRow(ResultSet rs, int rowNum) throws SQLException {
					return new EtConfigurationTreeItem(new ImageView(img), rs.getString("EventGUID"), rs.getString("EVENT_NAME"),
							Action.EC_EVENTS_ITEM);
				}
			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 9.
	 * @param equipmentGuid
	 * @return
	 */
	private List<EtConfigurationTreeItem> listEquipmentEvents(String equipmentGuid) {
		if (ValueUtil.isEmpty(equipmentGuid))
			return Collections.emptyList();

		try {
			EquipmentDAO dao = new EquipmentDAO();
			HashMap<String, Object> hashMap = new HashMap<>();
			hashMap.put("equipmentGuid", equipmentGuid);
			return dao.listEquipmentEvent(hashMap, new RowMapper<EtConfigurationTreeItem>() {

				URL url = ETFrameCompositeInitializer.class.getClassLoader().getResource("images/flash32.gif");
				Image img = new Image(url.toExternalForm(), 15, 15, false, false);

				@Override
				public EtConfigurationTreeItem mapRow(ResultSet rs, int rowNum) throws SQLException {
					var r = new EtConfigurationTreeItem(new ImageView(img), rs.getString("EventGUID"), rs.getString("EVENT_NAME"),
							Action.EC_EQ_EVENT_ITEMS);
					return r;
				}
			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 22.
	 * @param equipmentClassGuid
	 * @return
	 */
	private List<EtConfigurationTreeItem> listEquipments(String equipmentClassGuid) {
		return listEquipments(equipmentClassGuid, null);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 26.
	 * @param parentEquipmentGuid
	 * @return
	 */
	private List<EtConfigurationTreeItem> listChildEquipments(String parentEquipmentGuid) {
		return listEquipments(null, parentEquipmentGuid);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 26.
	 * @param equipmentClassGuid
	 * @param parentEquipmentGuid
	 * @return
	 */
	private List<EtConfigurationTreeItem> listEquipments(String equipmentClassGuid, String parentEquipmentGuid) {
		if (ValueUtil.isEmpty(equipmentClassGuid) && ValueUtil.isEmpty(parentEquipmentGuid))
			return Collections.emptyList();

		try {
			EquipmentDAO dao = new EquipmentDAO();
			HashMap<String, Object> hashMap = new HashMap<>();
			hashMap.put("equipmentClassGuid", equipmentClassGuid);
			hashMap.put("parentEquipmentGuid", parentEquipmentGuid);
			return dao.listEquipment(hashMap, new RowMapper<EtConfigurationTreeItem>() {

				URL url = ETFrameCompositeInitializer.class.getResource("/images/et24.gif");
				Image img = new Image(url.toExternalForm(), 15, 15, false, false);

				URL url2 = ETFrameCompositeInitializer.class.getResource("/images/flag_yellow32.gif");
				Image img2 = new Image(url2.toExternalForm(), 15, 15, false, false);

				URL urlComponent = ETFrameCompositeInitializer.class.getResource("/images/component.gif");
				Image imgComponent = new Image(urlComponent.toExternalForm(), 15, 15, false, false);

				URL urlCp = ETFrameCompositeInitializer.class.getResource("/images/component.gif");
				Image imgCpComponent = new Image(urlCp.toExternalForm(), 15, 15, false, false);

				// URL urlWorkflow =
				// ETFrameCompositeInitializer.class.getResource("/images/Workflow.png");
				// Image imgWorkflowCpComponent = new
				// Image(urlWorkflow.toExternalForm(), 15, 15, false, false);

				@Override
				public EtConfigurationTreeItem mapRow(ResultSet rs, int rowNum) throws SQLException {

					EtConfigurationTreeItem etConfigurationTreeItem = new EtConfigurationTreeItem(new ImageView(img),
							rs.getString("EquipmentGUID"), rs.getString("EuipmentName"), Action.EC_EQ_ITEM);

					EtConfigurationTreeItem trEquipments = new EtConfigurationTreeItem(new ImageView(imgComponent), "2-8", "Equipments",
							Action.EC_EQ);

					EtConfigurationTreeItem trWorkflow = new EtConfigurationTreeItem(new ImageView(imgComponent), "2-9", "Workflows",
							Action.EQ_WORKFLOW);

					URL urlComponent = ETFrameCompositeInitializer.class.getResource("/images/component.gif");
					Image imgComponent = new Image(urlComponent.toExternalForm(), 15, 15, false, false);
					EtConfigurationTreeItem trEvents = new EtConfigurationTreeItem(new ImageView(imgComponent), "1-8-1", "Events",
							Action.EC_EQ_EVENTS);

					etConfigurationTreeItem.getChildren().add(trEquipments);
					etConfigurationTreeItem.getChildren().add(trEvents);
					etConfigurationTreeItem.getChildren().add(trWorkflow);

					etConfigurationTreeItem.getChildren()
							.add(new EtConfigurationTreeItem(new ImageView(img2), "1-9", "States", Action.EC_EQ_EVENT_STATES));

					etConfigurationTreeItem.getChildren().add(
							new EtConfigurationTreeItem(new ImageView(imgCpComponent), "1-9", "Custom Properties", Action.EQ_CUSTOM_PROP));

					return etConfigurationTreeItem;
				}
			});

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 23.
	 */
	@FXML
	public void btnDeleteItemOnAction() {

	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 23.
	 */
	@FXML
	public void btnUpdateOnAction() {

	}

	@FXML
	public void btnExportExcel() {
		Tab selectedItem = this.tpEtManagement.getSelectionModel().getSelectedItem();
		OnExcelTableViewList dataFrom = null;
		if (selectedItem instanceof OnExcelTableViewList) {
			dataFrom = (OnExcelTableViewList) selectedItem;
		}

		if (dataFrom == null) {
			// %ETFrameComposite_000006=해당 화면은 Excel export기능이 아직 지원되지 않습니다.
			DialogUtil.showMessageDialog(Message.getInstance().getMessage("%ETFrameComposite_000006"));
			return;
		}

		File out = DialogUtil.showFileSaveDialog(StageStore.getPrimaryStage(), new ExcelFileChooserHandler("File save"));
		if (out == null) {
			return;
		}

		boolean r = dataFrom.exportExcel(out);
		if (r) {

			// %ETFrameComposite_000005=작업에 성공하였습니다. 파일을 열어보시겠습니까?
			Optional<Pair<String, String>> showYesOrNoDialog = DialogUtil.showYesOrNoDialog("Open File",
					Message.getInstance().getMessage("%ETFrameComposite_000005"));

			showYesOrNoDialog.ifPresent(p -> {
				if ("Y".equals(p.getValue())) {
					FileUtil.windowOpen(out);
				}
			});
		} else {
			// %ETFrameComposite_000004=작업에 실패하였습니다.
			DialogUtil.showMessageDialog(Message.getInstance().getMessage("%ETFrameComposite_000004"));
		}

	}

	@FXML
	public void btnImportExcel() {
		File out = DialogUtil.showFileDialog(StageStore.getPrimaryStage(), new ExcelFileChooserHandler("File open"));
		if (out == null)
			return;

		Tab selectedItem = this.tpEtManagement.getSelectionModel().getSelectedItem();
		OnExcelTableViewList dataFrom = null;
		if (selectedItem instanceof OnExcelTableViewList) {
			dataFrom = (OnExcelTableViewList) selectedItem;
		}

		if (dataFrom == null) {
			// %ETFrameComposite_000003=해당 화면은 엑셀 Import 기능이 아직 지원되지 않습니다.
			DialogUtil.showMessageDialog(Message.getInstance().getMessage("%ETFrameComposite_000003"));
			return;
		}
		dataFrom.importExcel(out);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 26.
	 */
	@FXML
	public void btnCommitOnAction() {
		Tab selectedItem = this.tpEtManagement.getSelectionModel().getSelectedItem();
		OnCommitService dataFrom = null;
		if (selectedItem instanceof OnCommitService) {
			dataFrom = (OnCommitService) selectedItem;
		}

		if (dataFrom == null) {
			// %ETFrameComposite_000002=해당 화면은 리로드 기능이 아직 지원되지 않습니다.
			DialogUtil.showMessageDialog(Message.getInstance().getMessage("%ETFrameComposite_000002"));
			return;
		}
		dataFrom.onCommit();
	}

	@FXML
	public void btnReloadOnAction() {
		Tab selectedItem = this.tpEtManagement.getSelectionModel().getSelectedItem();

		OnReload dataFrom = null;
		if (selectedItem instanceof OnReload) {
			dataFrom = (OnReload) selectedItem;
		}

		if (dataFrom == null) {
			// %ETFrameComposite_000001=해당 화면은 리로드 기능이 아직 지원되지 않습니다.
			DialogUtil.showMessageDialog(Message.getInstance().getMessage("%ETFrameComposite_000001"));
			return;
		}
		dataFrom.reload();
	}



	@FXML
	public void miEtCommScriptOnAction() {
		var c = new ETScriptHelperComposite();
		FxUtil.createStageAndShow(c, stage -> {
			// ETFrameComposite_000007=공통 스크립트 뷰어
			stage.setTitle(Message.getInstance().getMessage("ETFrameComposite_000007"));
		});
	}
}
