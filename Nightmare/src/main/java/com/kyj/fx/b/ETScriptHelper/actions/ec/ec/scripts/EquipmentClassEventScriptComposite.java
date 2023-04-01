/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.scripts
 *	작성일   : 2021. 12. 3.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.ec.ec.scripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringEscapeUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.b.ETScriptHelper.actions.comm.core.AbstractManagementBorderPane;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnExcelTableViewList;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnLoadEquipmentClass;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnLoadEquipmentClassEvent;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnReload;
import com.kyj.fx.b.ETScriptHelper.actions.ec.ec.events.EtEventsDVO;
import com.kyj.fx.b.ETScriptHelper.actions.support.CommonsScriptPathDVO;
import com.kyj.fx.b.ETScriptHelper.actions.support.ETScriptHelperComposite;
import com.kyj.fx.b.ETScriptHelper.comm.BeyoundCompareToolHelper;
import com.kyj.fx.b.ETScriptHelper.comm.DefaultTableColumnForExcelImpl;
import com.kyj.fx.b.ETScriptHelper.comm.DialogUtil;
import com.kyj.fx.b.ETScriptHelper.comm.ExcelReader;
import com.kyj.fx.b.ETScriptHelper.comm.FileUtil;
import com.kyj.fx.b.ETScriptHelper.comm.FxExcelUtil;
import com.kyj.fx.b.ETScriptHelper.comm.FxUtil;
import com.kyj.fx.b.ETScriptHelper.comm.IExcelScreenHandler;
import com.kyj.fx.b.ETScriptHelper.comm.ITableColumnForExcel;
import com.kyj.fx.b.ETScriptHelper.comm.IdGenUtil;
import com.kyj.fx.b.ETScriptHelper.comm.Message;
import com.kyj.fx.b.ETScriptHelper.comm.ValueUtil;
import com.kyj.fx.b.ETScriptHelper.comm.service.XMLUtils;
import com.kyj.fx.b.ETScriptHelper.grid.NumberingCellValueFactory;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Callback;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EquipmentClassEventScriptComposite extends AbstractManagementBorderPane<EquipmentScriptDVO>
		implements OnLoadEquipmentClassEvent, OnLoadEquipmentClass, OnExcelTableViewList, OnReload {

	private static Logger LOGGER = LoggerFactory.getLogger(EquipmentClassEventScriptComposite.class);

	private ObjectProperty<Event> eventInfo;
	@FXML
	private TableView<EquipmentScriptDVO> tvScripts;

	TableColumn<EquipmentScriptDVO, String> tcEquipmentClassName;
	TableColumn<EquipmentScriptDVO, String> tcEventName;
	TableColumn<EquipmentScriptDVO, String> tcActionName;
	TableColumn<EquipmentScriptDVO, String> tcCode;

	// private TableColumn<EquipmentScriptDVO, String> tcName;
	@FXML
	private TextArea txtScript;
	@FXML
	private Label txtEventScriptName;

	/**
	 */
	public EquipmentClassEventScriptComposite() {
		super(EquipmentClassEventScriptComposite.class.getResource("EquipmentClassEventScriptView.fxml"));
	}

	@FXML
	public void initialize() {

		eventInfo = new SimpleObjectProperty<>();

		TableColumn<EquipmentScriptDVO, Integer> numberColumn = new TableColumn<EquipmentScriptDVO, Integer>();
		numberColumn.setCellValueFactory(new NumberingCellValueFactory<>(tvScripts.getItems()));
		numberColumn.setText("No.");
		numberColumn.setPrefWidth(40);

		tcEquipmentClassName = new TableColumn<>("EquipmentClassName");
		tcEquipmentClassName.setCellValueFactory(c -> c.getValue().equipmentClassNameProperty());
		tcEquipmentClassName.setPrefWidth(200d);
		tcEquipmentClassName.setCellFactory(cellFactory);
		tcEquipmentClassName.setId("tcEquipmentClassName");
		tcEquipmentClassName.setGraphic(new Label("*"));

		tcEventName = new TableColumn<>("EventName");
		tcEventName.setCellValueFactory(c -> c.getValue().eventNameProperty());
		tcEventName.setPrefWidth(200d);
		tcEventName.setCellFactory(cellFactory);
		tcEventName.setId("tcEventName");
		tcEventName.setGraphic(new Label("*"));

		tcActionName = new TableColumn<>("ActionName");
		tcActionName.setPrefWidth(200d);
		tcActionName.setCellFactory(cellFactory);
		tcActionName.setGraphic(new Label("*"));
		tcActionName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EquipmentScriptDVO, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<EquipmentScriptDVO, String> param) {
				return param.getValue().actionNameProperty();
			}
		});
		tcActionName.setId("tcActionName");

		tcCode = new TableColumn<>("Code");
		tcCode.setPrefWidth(0d);
		tcCode.setVisible(false);
		tcCode.setCellValueFactory(v -> v.getValue().codeProperty());
		tcCode.setId("tcCode");
		ChangeListener<Event> changeListener = (oba, o, n) -> {
			if (eventInfo.get() == null) {
				tvScripts.getItems().clear();
				txtScript.setText("");
				return;
			}

			EquipmentClassEventScriptDAO dao = new EquipmentClassEventScriptDAO();
			Event event = eventInfo.get();
			
//			String equipmentClassGuid = event.equipmentClassGuid();
//			String eventGuid = event.eventGuid();

			List<String> eventGuidArr = Collections.emptyList();
			if ("ALL".equals( event.eventGuid())) {
				eventGuidArr = dao.getAllEventGuids(event.equipmentClassGuid());
			} else {
				eventGuidArr = Arrays.asList( event.eventGuid());
			}
			tvScripts.getItems().clear();
			for (String eventGuid : eventGuidArr) {
				EtEventsDVO eventInfo = dao.getEventInfo(eventGuid);
				Number eventType = eventInfo.getEventType();
				var param = new HashMap<String, Object>();

				switch (eventType.intValue()) {
				/* transient */
				case 0:
					param.put("eventTypeNames", Arrays.asList("Event_OnComplete"));
					break;
				/* start-stop */
				case 1:
					param.put("eventTypeNames", Arrays.asList("Event_OnCancel", "Event_OnComplete", "Event_OnPause", "Event_OnRestart",
							"Event_OnStart", "Event_OnUpdate"));
					break;
				/* system */
				case 2:
					param.put("eventTypeNames", Arrays.asList("Event_OnComplete"));
					break;
				}

				param.put("equipmentClassGuid", event.equipmentClassGuid());
				param.put("eventGuid", eventGuid);
				List<EquipmentScriptDVO> scripts = dao.getEquipmentScript(param);
				tvScripts.getItems().addAll(scripts);
			}

		};

		tvScripts.getColumns().add(numberColumn);
		tvScripts.getColumns().add(tcEquipmentClassName);
		tvScripts.getColumns().add(tcEventName);
		tvScripts.getColumns().add(tcActionName);
		tvScripts.getColumns().add(tcCode);

		this.eventInfo.addListener(changeListener);

		Menu mCompareWith = new Menu("Compare with");
		List<MenuItem> collect = ETScriptHelperComposite.listCommonsScriptPath().stream().map((CommonsScriptPathDVO d) -> {
			MenuItem menuItem = new MenuItem(d.getFilePath());
			menuItem.setUserData(d);
			menuItem.setOnAction(this::miCompareScriptOnAction);

			return menuItem;
		}).collect(Collectors.toList());

		mCompareWith.getItems().setAll(collect);

		MenuItem mComopareCommons = new MenuItem("Compare commons scripts");
		mComopareCommons.setOnAction(this::mComopareCommonsOnAction);

		Menu mSetScript = new Menu("Set commons scripts.");
		List<MenuItem> collect2 = ETScriptHelperComposite.listCommonsScriptPath().stream().map((CommonsScriptPathDVO d) -> {
			MenuItem menuItem = new MenuItem(d.getFilePath());
			menuItem.setUserData(d);
			menuItem.setOnAction(this::miSetCommonScriptOnAction);

			return menuItem;
		}).collect(Collectors.toList());
		mSetScript.getItems().setAll(collect2);

		this.tvScripts.setContextMenu(new ContextMenu(mSetScript, mCompareWith, mComopareCommons));
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2022. 5. 10.
	 * @param actionevent1
	 */
	private void miSetCommonScriptOnAction(ActionEvent ae) {
		var selected = tvScripts.getSelectionModel();
		if (selected == null) {
			DialogUtil.showMessageDialog("선택된 데이터가 없음.");
			return;
		}

		if (selected.getSelectedItem() == null) {
			DialogUtil.showMessageDialog("데이터셋이 존재하지 않음.");
			return;
		}

		MenuItem mi = (MenuItem) ae.getSource();
		CommonsScriptPathDVO d = (CommonsScriptPathDVO) mi.getUserData();
		String fileFullPath = d.getFileFullPath();

		try {
			String code = FileUtil.readToString(new File(fileFullPath));
			selected.getSelectedItem().setCode(code);
			tvScripts.refresh();
			txtScript.setText(code);
			// tvScripts.requestLayout();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2022. 4. 28.
	 * @param e
	 */
	public void miCompareScriptOnAction(ActionEvent e) {
		MenuItem mi = (MenuItem) e.getSource();
		CommonsScriptPathDVO dvo = (CommonsScriptPathDVO) mi.getUserData();

		EquipmentScriptDVO selectedItem = this.tvScripts.getSelectionModel().getSelectedItem();

		// String scriptLeft = dvo.getCode() == null ? "" : dvo.getCode();
		String scriptRight = selectedItem.getCode() == null ? "" : selectedItem.getCode();

		try {
			// var leftFile = new File(FileUtil.getTempGagoyle(),
			// IdGenUtil.generate());
			// FileUtil.writeFile(leftFile, scriptLeft);

			var rightFile = new File(FileUtil.getTempGagoyle(), IdGenUtil.generate());
			FileUtil.writeFile(rightFile, scriptRight);

			BeyoundCompareToolHelper helper = new BeyoundCompareToolHelper();
			// helper.setScriptFile(BeyoundCompareToolHelper.SCRIPT_FOLDER_COMPARE_FILE_NAME);
			// BeyoundCompareToolHelper helper =
			// BeyoundCompareToolHelper.newFolderCompare();
			helper.setLeftFile(new File(dvo.getFileFullPath()));
			helper.setRightFile(rightFile);
			helper.compare();
			helper.setUseScript(true);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	String replaceComments(String str) {
		StringBuilder sb = new StringBuilder(str.length());
		try (BufferedReader br = new BufferedReader(new StringReader(str))) {

			// String readLine = br.readLine();
			String temp = null;
			while ((temp = br.readLine()) != null) {
				if (!temp.trim().startsWith("'")) {
					sb.append(temp);
				}
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * mComopareCommonsOnAction * @작성자 : KYJ (callakrsos@naver.com)
	 * 
	 * @작성일 : 2022. 4. 28.
	 * @param e
	 */
	public void mComopareCommonsOnAction(ActionEvent e) {

		// File dir = new File(FileUtil.getTempGagoyle(), IdGenUtil.generate());
		// dir.mkdirs();

		ObservableList<EquipmentScriptDVO> items = this.tvScripts.getItems();

		List<CommonsScriptPathDVO> listCommonsScriptPath = ETScriptHelperComposite.listCommonsScriptPath();
		List<File> commonsScripts = listCommonsScriptPath.stream().map(d -> new File(d.getFileFullPath())).collect(Collectors.toList());

		StringBuilder ret = new StringBuilder();
		StringBuilder detail = new StringBuilder();
		for (EquipmentScriptDVO d : items) {
			String code = d.getCode();
			String actionName = d.getActionName();

			if (!actionName.isEmpty()) {

				String code1 = replaceComments(code);

				List<File> collect = commonsScripts.stream()
						.filter(v -> ValueUtil.equals(v.getName().replace(".vbs", "").toUpperCase(), actionName.toUpperCase()))
						.filter(f -> {
							String str;
							try {
								str = FileUtil.readToString(f);
								String converted = replaceComments(str);
								return ValueUtil.equals(code1, converted);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							return false;
						}).collect(Collectors.toList());

				ret.append(actionName).append("\t:\t").append(collect.size()).append("\n");

				detail.append(actionName).append("\tmatched:\t").append(collect.size()).append("\n");
				detail.append("matched files :\t").append(collect.toString()).append("\n");
				detail.append("#####################\n");
			}

		}
		ret.append("\n").append(detail);

		FxUtil.createStageAndShow(new TextArea(ret.toString()), stage -> {
			stage.setHeight(600d);
		});

		// try {
		// BeyoundCompareToolHelper helper =
		// BeyoundCompareToolHelper.newFolderCompare();
		// helper.setLeftFile(new
		// File(ETScriptHelperComposite.getEtScriptBasePath()));
		// helper.setRightFile(dir);
		// helper.closeScript(true);
		// helper.compare();
		// File outFile = helper.getOutFile();
		// if (outFile != null && outFile.exists())
		// Desktop.getDesktop().open(outFile);
		// } catch (Exception e1) {
		// e1.printStackTrace();
		// }

	}

	Callback<TableColumn<EquipmentScriptDVO, String>, TableCell<EquipmentScriptDVO, String>> cellFactory = new Callback<TableColumn<EquipmentScriptDVO, String>, TableCell<EquipmentScriptDVO, String>>() {
		@Override
		public TableCell<EquipmentScriptDVO, String> call(TableColumn<EquipmentScriptDVO, String> ce) {
			TableCell<EquipmentScriptDVO, String> c = new TableCell<>() {
				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if (empty)
						setText("");
					else
						setText(item);

					EquipmentScriptDVO d = getTableRow().getItem();
					if (d == null)
						return;
					String code = d.getCode();
					if (ValueUtil.isEmpty(code)) {
						setTextFill(Color.BLACK);
					} else {
						setTextFill(Color.RED);
					}
				}

			};
			return c;
		}
	};

	@Override
	public void onLoadEquipmentClass(String equipmentClassGuid) {
		/* Clear */
		this.eventInfo.set(null);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 3.
	 */
	@FXML
	public void tvScriptsOnMouseClick(MouseEvent e) {

		if (1 == e.getClickCount()) {
			if (e.isConsumed())
				return;
			e.consume();

			EquipmentScriptDVO selectedItem = tvScripts.getSelectionModel().getSelectedItem();
			if (selectedItem == null)
				return;
			txtScript.setText(selectedItem.getCode());
			txtEventScriptName.setText(selectedItem.getActionName());
			FxUtil.showStatusMessage(selectedItem.getActionName());
		}
	}

	@FXML
	public void btnTempSaveOnAction() {
		EquipmentScriptDVO selectedItem = tvScripts.getSelectionModel().getSelectedItem();
		if (selectedItem == null) {
			// %EquipmentClassEventScriptComposite_000001 선택된 Script가 없습니다.
			DialogUtil.showMessageDialog(Message.getInstance().getMessage("%EquipmentClassEventScriptComposite_000001"));
			return;
		}
		String code = txtScript.getText();
		selectedItem.setCode(code);
		tvScripts.refresh();
		FxUtil.showStatusMessage("임시저장되었습니다.");
	}

	@Override
	public void onLoadEquipmentClassEvent(String equipmentClassGuid, String eventGuid) {
		Event value = new Event(equipmentClassGuid, eventGuid);
		setEvent(value);
	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2022. 6. 24. 
	 * @param value
	 */
	protected void setEvent(Event value) {
		eventInfo.set(null);
		eventInfo.set(value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TableView<EquipmentScriptDVO>> excelTableList() {
		return Arrays.asList(tvScripts);
	}

	@Override
	public boolean exportExcel(File out) {

		File outDir = out.getParentFile();
		try {
			List<TableView<EquipmentScriptDVO>> tableViews = excelTableList();
			return FxExcelUtil.createExcel(new IExcelScreenHandler<EquipmentScriptDVO>() {

				@Override
				public ITableColumnForExcel useTableColumnForExcel() {
					return new DefaultTableColumnForExcelImpl() {

						@Override
						public boolean test(TableView<?> t, TableColumn<?, ?> c) {

							if ("commonsClicked".equals(c.getId())) {
								return false;
							}

							return true;
						}

					};
				}

				@Override
				public Object valueMapper(TableView<EquipmentScriptDVO> table, TableColumn<EquipmentScriptDVO, ?> column, int columnIndex,
						int rowIndex) {
					Object valueMapper = IExcelScreenHandler.super.valueMapper(table, column, columnIndex, rowIndex);

					if (column == tcCode) {
						String id = IdGenUtil.generate();

						// EquipmentScriptDVO equipmentScriptDVO =
						// table.getItems().get(rowIndex);
						// String equipmentClassName =
						// equipmentScriptDVO.getEquipmentClassName();
						// String eventName = equipmentScriptDVO.getEventName();
						// String actionName =
						// equipmentScriptDVO.getActionName();
						// String.format("%s%s%s");

						File scriptFile = new File(outDir, id);
						try {
							FileUtil.writeFile(scriptFile, valueMapper == null ? "" : valueMapper.toString());
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
						return scriptFile.getName();
					}

					return valueMapper;
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * com.kyj.fx.commons.excel.IExcelScreenHandler#toSheetName(
				 * javafx.scene.control.TableView)
				 */
				@Override
				public String toSheetName(TableView<EquipmentScriptDVO> table) {
					return "Sheet" + tableViews.indexOf(table);
				}

			}, out, tableViews, true);
		} catch (Exception e) {
			FxUtil.showStatusMessage(e.getMessage());
			LOGGER.error(ValueUtil.toString(e));
		}
		return false;
	}

	@Override
	public void importExcel(File fromFile, Document doc) {
		List<EquipmentScriptDVO> populateList;
		try {
			populateList = ExcelReader.populateList(doc, EquipmentScriptDVO.class, (colName, v) -> {
				if ("Code".toUpperCase().equals(colName.toUpperCase())) {
					if (ValueUtil.isNotEmpty(v)) {
						try {

							var f = new File(fromFile.getParentFile(), v);
							if (!f.exists()) {
								// %EquipmentClassEventScriptComposite_000002=경로에
								// 파일이 존재하지 않습니다.
								throw new RuntimeException(Message.getInstance().getMessage("%EquipmentClassEventScriptComposite_0000023",
										f.getAbsolutePath()));
							}

							String code = FileUtil.readToString(f);
							return code;
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
				}
				return v;
			});
			this.tvScripts.getItems().setAll(populateList);
		} catch (Exception e) {
			DialogUtil.showExceptionDailog(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kyj.fx.b.ETScriptHelper.actions.comm.OnCommitService#onCommit()
	 */
	@Override
	public void onCommit() {

		if (this.tvScripts.getItems().isEmpty()) {
			// EquipmentClassEventScriptComposite_000005=아이템이 존재하지 않습니다.
			DialogUtil.showMessageDialog(Message.getInstance().getMessage("EquipmentClassEventScriptComposite_000005"));
			return;
		}

		// return;
		// if (this.eventInfo.get() == null) {
		// DialogUtil.showMessageDialog("이벤트 정보가 없습니다.");
		// return;
		// }
		// Event event = this.eventInfo.get();
		//
		// if (ValueUtil.isEmpty(event.eventGuid())) {
		// DialogUtil.showMessageDialog("Event guid 정보가 없습니다.");
		// return;
		// }

		String permission = "5", domain = "Syncade", application = "DMI ET", entityType = "Events", entityId = "0";
		// DialogUtil.showESigDialog(permission, domain, application,
		// entityType, entityId, new Callback<Map<String, String>, Void>() {
		// @Override
		// public Void call(Map<String, String> param) {
		//
		// ObservableList<EquipmentScriptDVO> items = tvScripts.getItems();
		// Map<String, List<EquipmentScriptDVO>> collect =
		// items.stream().filter(v ->
		// ValueUtil.isNotEmpty(v.getEquipmentClassName()))
		// .filter(v ->
		// ValueUtil.isNotEmpty(v.getEventName())).collect(Collectors.groupingBy((EquipmentScriptDVO
		// v) -> {
		// String eqName = v.getEquipmentClassName();
		// String eventName = v.getEventName();
		// // 그룹키 생성.
		// return eqName + "┐" + eventName;
		// }));
		// setDeployItems(collect);
		// EquipmentClassEventScriptComposite.super.onCommit();
		// return null;
		// }
		// });

		this.esigInfo = showSimpleSigDialog(permission, domain, application, entityType, entityId);

		if (esigInfo != null && !esigInfo.isEmpty()) {

			// String userName = esigInfo.get("userName");
			// String userPwd = esigInfo.get("userPwd");
			//
			// if (ValueUtil.isEmpty(userName)) {
			// return;
			// }
			// if (ValueUtil.isEmpty(userPwd)) {
			// return;
			// }

			ObservableList<EquipmentScriptDVO> items = tvScripts.getItems();
			Map<String, List<EquipmentScriptDVO>> collect = items.stream().filter(v -> ValueUtil.isNotEmpty(v.getEquipmentClassName()))
					.filter(v -> ValueUtil.isNotEmpty(v.getEventName())).collect(Collectors.groupingBy((EquipmentScriptDVO v) -> {
						String eqName = v.getEquipmentClassName();
						String eventName = v.getEventName();
						// 그룹키 생성.
						return eqName + "┐" + eventName;
					}));
			setDeployItems(collect);
			EquipmentClassEventScriptComposite.super.onCommit();

		}
	}

	private Map<String, String> esigInfo = null;

	@Override
	public String onDeployItem(String name, List<EquipmentScriptDVO> items) {
		var dao = new EquipmentClassEventScriptDAO();
		var s = new EquipmentClassEventScriptService();

		EquipmentScriptDVO equipmentScriptDVO = items.get(0);
		String className = equipmentScriptDVO.getEquipmentClassName();
		String eventName = equipmentScriptDVO.getEventName();
		// String eventGuid = equipmentScriptDVO.getEventGuid();

		String eventGuid = dao.getEventGuid(className, eventName);

		if (ValueUtil.isEmpty(eventGuid)) {
			setDeployItemFail();
			// EquipmentClassEventScriptComposite_000004=EvenGuid가 존재하지 않습니다
			// [{0}], [{1}]
			String message = Message.getInstance().getMessage("EquipmentClassEventScriptComposite_000004", className, eventName);
			return message;
		}
		try {
			LOGGER.debug("Event guid : {} ", eventGuid);
			Object itemXml = s.item(eventGuid);
			Document doc = XMLUtils.load(itemXml.toString());

			for (EquipmentScriptDVO ec : items) {
				String actionName = ec.getActionName();
				String scriptGuid = ec.getScriptGuid();
				// Node node =
				// doc.selectSingleNode(String.format("/Event/ListScripts/Script[@Name='{0}']",
				// actionName));
				Element parentNode = (Element) doc.selectSingleNode("/Event/ListScripts");
				Element scriptNode = (Element) parentNode.selectSingleNode(String.format("Script[@Name='%s']", actionName));

				if (scriptNode == null) {
					// 제거, 추가 구분 불가
					Element addElement = parentNode.addElement("Script");
					addElement.addAttribute("Name", ec.getActionName());
					addElement.addAttribute("ContinueOnFail", "0");
					addElement.addAttribute("Email", "");
					addElement.addAttribute("Code", ec.getCode());
					addElement.addAttribute("ScriptGUID", scriptGuid);
					addElement.addAttribute("GUID", IdGenUtil.randomGuid().toUpperCase());
					// Element parent = (Element)
					// doc.selectSingleNode("/Event/ListScripts");
					// parentNode.add(addElement);
				} else {
					scriptNode.attribute("Code").setValue(ec.getCode());
				}
			}

			String tokenXml = createTokenEx(esigInfo);

			String reqXml = doc.getRootElement().asXML();
			LOGGER.debug(reqXml);
			Object ret = s.update(tokenXml, StringEscapeUtils.escapeXml(reqXml));

			var retDoc = XMLUtils.load(ret.toString());
			var nFaultstring = retDoc.selectSingleNode("//faultstring");
			if (nFaultstring != null) {
				setDeployItemFail();
				return nFaultstring.getText();
			}

			LOGGER.debug("{}", ret);
			setDeployItemPass();
			return "success.";
		} catch (Exception e) {
			setDeployItemFail();
			return e.getMessage();
		}
	}

	@Override
	public void reload() {
		Event event = eventInfo.get();
		eventInfo.set(null);
		eventInfo.set(event);
	}
	
	public TableView<EquipmentScriptDVO> getScriptTable() {
		return this.tvScripts;
	}
	
}
