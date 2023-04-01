/**
 * 
 */
package com.kyj.fx.nightmare;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.actions.ec.ec.scripts.EquipmentScriptDVO;
import com.kyj.fx.nightmare.actions.frame.ETFrameComposite;
import com.kyj.fx.nightmare.actions.support.CommonsScriptPathDVO;
import com.kyj.fx.nightmare.comm.DialogUtil;
import com.kyj.fx.nightmare.comm.FileUtil;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.Hex;
import com.kyj.fx.nightmare.comm.ResourceLoader;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.eqtree.EquipmentClassDVO;
import com.kyj.fx.nightmare.eqtree.EquipmentDAO;
import com.kyj.fx.nightmare.grid.AnnotationOptions;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.StringConverter;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class ETScriptComposite extends BorderPane {

	private static final Logger LOGGER = LoggerFactory.getLogger(ETScriptComposite.class);

	@FXML
	private ComboBox<EquipmentClassDVO> cbEquipmentClass;
	@FXML
	private ComboBox<EquipmentDVO> cbEquipmentId;
	@FXML
	private ComboBox<EquipmentEventDVO> cbEventName;
	@FXML
	private RadioButton rCancel, rComplete, rPause, rRestart, rStart, rUpdate;
	@FXML
	private ToggleGroup eventScriptType;

	@FXML
	private TextArea txtCode;
	@FXML
	private TableView<CommonsScriptPathDVO> tbCommonScriptPath;

	@FXML
	private Label lblEquipmentClassName, lblEventType;

	private ObjectProperty<EquipmentScriptDVO> currentEquipmentScript = new SimpleObjectProperty<>();

	/**
	 * @return the cbEquipmentClass
	 */
	public ComboBox<EquipmentClassDVO> getCbEquipmentClass() {
		return cbEquipmentClass;
	}

	/**
	 * @return the cbEventName
	 */
	public ComboBox<EquipmentEventDVO> getCbEventName() {
		return cbEventName;
	}

	private ETFrameComposite root;

	/**
	 */
	public ETScriptComposite(ETFrameComposite root) {
		this.root = root;

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ETScriptComposite.class.getResource("ETScriptView.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}

	@FXML
	public void initialize() {

		
		
		this.addEventHandler(KeyEvent.KEY_RELEASED, ev -> {
			if (!ev.isControlDown())
				return;
			switch (ev.getCode()) {
			case DIGIT1:
				rCancel.setSelected(true);
				break;
			case DIGIT2:
				rComplete.setSelected(true);
				break;
			case DIGIT3:
				rPause.setSelected(true);
				break;
			case DIGIT4:
				rRestart.setSelected(true);
				break;
			case DIGIT5:
				rStart.setSelected(true);
				break;
			case DIGIT6:
				rUpdate.setSelected(true);
				break;
			default:
			}

		});
		StringConverter<EquipmentDVO> converterEquipment = new StringConverter<EquipmentDVO>() {

			@Override
			public String toString(EquipmentDVO object) {
				if (object == null)
					return "";
				return object.getEquipmentName();
			}

			@Override
			public EquipmentDVO fromString(String string) {

				Optional<EquipmentDVO> findFirst = cbEquipmentId.getItems().stream()
						.filter(v -> v.getEquipmentGuid().equals(string) || v.getEquipmentName().equals(string)).findFirst();
				if (findFirst.isPresent())
					return findFirst.get();
				return new EquipmentDVO(string);
			}
		};

		cbEquipmentId.setConverter(converterEquipment);
		cbEquipmentId.setCellFactory(TextFieldListCell.forListView(converterEquipment));
		cbEquipmentId.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<EquipmentDVO>() {

			@Override
			public void changed(ObservableValue<? extends EquipmentDVO> observable, EquipmentDVO oldValue, EquipmentDVO newValue) {
				if (newValue == null || ValueUtil.isEmpty(newValue.getEquipmentGuid()))
					return;
				rCancel.setTextFill(Color.BLACK);
				rComplete.setTextFill(Color.BLACK);
				rPause.setTextFill(Color.BLACK);
				rRestart.setTextFill(Color.BLACK);
				rStart.setTextFill(Color.BLACK);
				rUpdate.setTextFill(Color.BLACK);
				cbEventName.getItems().clear();
				if (eventScriptType.getSelectedToggle() != null)
					eventScriptType.getSelectedToggle().setSelected(false);

				LOGGER.debug("Equopment GUID : {} ", newValue.getEquipmentGuid());
				lblEquipmentClassName.setText(newValue.getEquipmentClassName());
				Map<String, Object> map = Utils.toMap(newValue);
				List<EquipmentEventDVO> listEvents;
				try {
					listEvents = new EquipmentDAO().listEventStatus(map);
					cbEventName.getItems().setAll(listEvents);
					showStatusMessage("Event 조회 완료");
				} catch (Exception e) {
					showStatusMessage(e.getMessage());
					cbEventName.getItems().clear();
				}

			}
		});

		StringConverter<EquipmentEventDVO> converterEquipmentEvent = new StringConverter<EquipmentEventDVO>() {

			@Override
			public String toString(EquipmentEventDVO object) {
				if (object == null)
					return "";
				return object.getEventName();
			}

			@Override
			public EquipmentEventDVO fromString(String string) {
				Optional<EquipmentEventDVO> findFirst = cbEventName.getItems().stream().filter(v -> v.getEventName().equals(string))
						.findFirst();
				if (findFirst.isPresent())
					return findFirst.get();
				return null;
			}
		};
		cbEventName.setConverter(converterEquipmentEvent);

		cbEquipmentClass.setConverter(new StringConverter<EquipmentClassDVO>() {

			@Override
			public String toString(EquipmentClassDVO object) {
				return object == null ? "" : object.getEquipmentClassName();
			}

			@Override
			public EquipmentClassDVO fromString(String string) {

				Optional<EquipmentClassDVO> findFirst = cbEquipmentClass.getItems().stream()
						.filter(v -> v.getEquipmentClassName().equals(string)).findFirst();
				if (findFirst.isPresent())
					return findFirst.get();
				return null;
			}
		});
		eventScriptType.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				RadioButton rb = (RadioButton) eventScriptType.getSelectedToggle();
				if (rb != null) {
					String actionName = rb.getText();
					EquipmentEventDVO e = cbEventName.getValue();
					equipmentScriptAction(e, actionName);
				}

			}
		});

		
//		FxUtil.installCommonsTableView(CommonsScriptPathDVO.class, tbCommonScriptPath,
//				new AnnotationOptions<CommonsScriptPathDVO>(CommonsScriptPathDVO.class));
		FxUtil.installCommonsTableView(CommonsScriptPathDVO.class,  tbCommonScriptPath,
				new AnnotationOptions<CommonsScriptPathDVO>(CommonsScriptPathDVO.class));
		
		tbCommonScriptPath.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {

			if (ev.isConsumed())
				return;
			if (ev.getClickCount() == 2) {
				ev.consume();

				CommonsScriptPathDVO d = tbCommonScriptPath.getSelectionModel().getSelectedItem();
				if (d == null)
					return;

				Optional<Pair<String, String>> showYesOrNoDialog = DialogUtil.showYesOrNoDialog("Warning", "코드가 변경됩니다. 진행하시겠습니까? ");
				showYesOrNoDialog.ifPresent(pair -> {
					if ("Y".equals(pair.getValue())) {
						String fullPath = d.getFileFullPath();
						try {
							this.txtCode.setText(FileUtil.readToString(new File(fullPath)));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});

			}
		});

		currentEquipmentScript.addListener(new ChangeListener<EquipmentScriptDVO>() {

			@Override
			public void changed(ObservableValue<? extends EquipmentScriptDVO> observable, EquipmentScriptDVO oldValue,
					EquipmentScriptDVO newValue) {
				if (newValue != null) {
					String code = newValue.getCode();
					txtCode.setText(code);
				}

			}
		});

		Platform.runLater(() -> after());
		
//		ThreadUtil.createNewThreadAndRun(() -> {
//			
//		});
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 11.
	 * @param newValue
	 */
	private void searchEquipmentInfo(EquipmentEventDVO newValue) {
		if (newValue == null)
			return;
		String eventType = newValue.getEventType();

		switch (eventType) {
		case "0":
			/* transient */
			rCancel.setDisable(true);
			rComplete.setDisable(false);
			rPause.setDisable(true);
			rRestart.setDisable(true);
			rStart.setDisable(true);
			rUpdate.setDisable(true);
			lblEventType.setText("transient");
			break;
		case "1":
			/* start-stop */
			rCancel.setDisable(false);
			rComplete.setDisable(false);
			rPause.setDisable(false);
			rRestart.setDisable(false);
			rStart.setDisable(false);
			rUpdate.setDisable(false);
			lblEventType.setText("start-stop");
			break;
		case "2":
			/* system event */
			rCancel.setDisable(true);
			rComplete.setDisable(false);
			rPause.setDisable(true);
			rRestart.setDisable(true);
			rStart.setDisable(true);
			rUpdate.setDisable(true);
			lblEventType.setText("system event");
			break;
		}
		rCancel.setSelected(false);
		rComplete.setSelected(false);
		rPause.setSelected(false);
		rRestart.setSelected(false);
		rStart.setSelected(false);
		rUpdate.setSelected(false);

		try {
			Map<String, Object> map = Utils.toMap(newValue);
			// map.put("actionName", actionName);

			listEquipmentScripts.clear();
			listEquipmentScripts.addAll(new EquipmentDAO().getEquipmentScript(map));

			// Backup.
			// listEquipmentScripts.forEach(Backup::backup);

			showStatusMessage(" Script 조회 완료.");

			repaintEventScripts();

			RadioButton rb = (RadioButton) eventScriptType.getSelectedToggle();
			if (rb != null) {
				String actionName = rb.getText();
				equipmentScriptAction(newValue, actionName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			showStatusMessage(" Script 조회 실패.");
		}
	}

	/**
	 * @최초생성일 2021. 7. 6.
	 */
	private List<EquipmentScriptDVO> listEquipmentScripts = FXCollections.observableArrayList();

	private void equipmentScriptAction(EquipmentEventDVO newValue, String actionName) {
		// Map<String, Object> map = ValueUtil.toMap(newValue);
		// map.put("actionName", actionName);

		if (listEquipmentScripts.isEmpty())
			return;

		Optional<EquipmentScriptDVO> findFirst = listEquipmentScripts.stream().filter(v -> {
			return actionName.equals(v.getActionName());
		}).findFirst();

		if (findFirst.isPresent()) {
			EquipmentScriptDVO equipmentScriptDVO = findFirst.get();
			currentEquipmentScript.set(equipmentScriptDVO);

		}
	}

	public void after() {

		try {
			List<EquipmentClassDVO> listEquipmentClasses = new EquipmentDAO().listEquipmentClass(Collections.emptyMap());
			cbEquipmentClass.getItems().setAll(listEquipmentClasses);
		} catch (Exception e) {
			e.printStackTrace();
			showStatusMessage(e.getMessage());
		}

		try {
			List<EquipmentDVO> listEquipment = new EquipmentDAO().listEquipment(Collections.emptyMap());
			cbEquipmentId.getItems().setAll(listEquipment);
			showStatusMessage("Equipment 조회 완료");
		} catch (Exception e) {
			e.printStackTrace();
			showStatusMessage(e.getMessage());
		}

		String basePath = ResourceLoader.getInstance().get("et.common.script.base.path");
		if (ValueUtil.isEmpty(basePath)) {
			showStatusMessage("et.common.script.base.path 설정 내용이 비어 있음.");
			return;
		}

		File file = new File(basePath);
		if (!file.exists()) {
			showStatusMessage("et.common.script.base.path 접근 불가. : " + basePath);
			return;
		}

		DirWalker dirWalker = new DirWalker();
		dirWalker.setRootFile(file);
		ThreadUtil.createNewThreadAndRun(new Runnable() {

			@Override
			public void run() {
				try {
					ArrayList<CommonsScriptPathDVO> results = new ArrayList<CommonsScriptPathDVO>();
					dirWalker.handleDirectory(file, 0, results);

					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							tbCommonScriptPath.getItems().addAll(results);
						}
					});
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 7. 5.
	 */
	@FXML
	public void btnSearchOnAction() {
		EquipmentDVO value = cbEquipmentId.getSelectionModel().getSelectedItem();
		if (value == null)
			return;
		EquipmentEventDVO selectedItem = cbEventName.getSelectionModel().getSelectedItem();
		if (selectedItem == null)
			return;
		searchEquipmentInfo(selectedItem);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 7. 6.
	 */
	@FXML
	public void btnSaveOnAction() {

		try {
			String text = this.txtCode.getText();
			// char[] encode = Hex.encode(text.getBytes("UTF-16LE"));
			// String code = new String(encode);
			// LOGGER.debug(code);

			EquipmentScriptDVO equipmentScriptDVO = this.currentEquipmentScript.get();

			// 동일한 코드면 저장 x
			if (text.equals(equipmentScriptDVO.getCode()))
				return;

			equipmentScriptDVO.setCode(text);
			if (equipmentScriptDVO.getEventScriptGUID() == null)
				equipmentScriptDVO.set_status(EquipmentScriptDVO.CREATE);
			else
				equipmentScriptDVO.set_status(EquipmentScriptDVO.UPDATE);
			repaintEventScripts();

			DialogUtil.showMessageDialog(FxUtil.getWindow(this), "임시 저장됨.");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	@FXML
	public void btnEncoderOnAction() {
		String text = this.txtCode.getText();
		try {
			char[] encode = Hex.encode(text.getBytes("UTF-16LE"));
			String message = new String(encode);
			this.txtCode.setText(message);
		} catch (UnsupportedEncodingException e) {
			DialogUtil.showExceptionDailog(e);
		}
	}

	@FXML
	public void btnDecoderOnAction() {
		String code = this.txtCode.getText();
		String decode = "";
		if (code == null || code == "")
			decode = "";
		else
			try {
				decode = new String(Hex.decode(code), "UTF-16LE");
			} catch (Exception ex) {
				decode = "";
				DialogUtil.showExceptionDailog(ex);
			}
		LOGGER.debug(code);
		txtCode.setText(decode);
	}

	@FXML
	public void btnUploadOnAction() {
		long count = this.listEquipmentScripts.stream().filter(v -> ValueUtil.isNotEmpty(v.get_status())).count();
		if (count == 0) {
			DialogUtil.showMessageDialog(FxUtil.getWindow(this), "변경된 내용이 없습니다.");
			return;
		}

		Stage window = (Stage) FxUtil.getWindow(this);
		String title = "Confirm";
		String message = "Upload " + count + " 건이 변경됩니다. 수행하시겠습니까 ? ";
		Optional<Pair<String, String>> showYesOrNoDialog = DialogUtil.showYesOrNoDialog(window, title, message);
		showYesOrNoDialog.ifPresent(c -> {
			if ("Y".equals(c.getValue())) {

				// backup.
				listEquipmentScripts.forEach(v -> {
					Backup.backup(v);
				});

				try {
					DbUpdate.updateScript(listEquipmentScripts);
					showStatusMessage("");
				} catch (Exception e) {
					showStatusMessage(ValueUtil.toString(e));
					DialogUtil.showExceptionDailog(e);
				}
			}
		});

	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 19.
	 */
	private void repaintEventScripts() {
		repaintEventScripts(Color.RED);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 19.
	 * @param highlightColor
	 */
	private void repaintEventScripts(Color highlightColor) {
		listEquipmentScripts.forEach(v -> {
			String an = v.getActionName();
			switch (an) {
			case "Event_OnCancel":
				if (v.getCode() == null || v.getCode().length() == 0) {
					rCancel.setTextFill(Color.BLACK);
				} else
					rCancel.setTextFill(highlightColor);
				break;
			case "Event_OnComplete":
				if (v.getCode() == null || v.getCode().length() == 0) {
					rComplete.setTextFill(Color.BLACK);
				} else
					rComplete.setTextFill(highlightColor);
				break;
			case "Event_OnPause":
				if (v.getCode() == null || v.getCode().length() == 0) {
					rPause.setTextFill(Color.BLACK);
				} else
					rPause.setTextFill(highlightColor);
				break;
			case "Event_OnRestart":
				if (v.getCode() == null || v.getCode().length() == 0) {
					rRestart.setTextFill(Color.BLACK);
				} else
					rRestart.setTextFill(highlightColor);
				break;
			case "Event_OnStart":
				if (v.getCode() == null || v.getCode().length() == 0) {
					rStart.setTextFill(Color.BLACK);
				} else
					rStart.setTextFill(highlightColor);
				break;
			case "Event_OnUpdate":
				if (v.getCode() == null || v.getCode().length() == 0) {
					rUpdate.setTextFill(Color.BLACK);
				} else
					rUpdate.setTextFill(highlightColor);
				break;
			default:

			}

		});
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 19.
	 */
	@FXML
	public void cbEquipmentClassOnAction() {
		EquipmentClassDVO selectedItem = cbEquipmentClass.getSelectionModel().getSelectedItem();
		if (selectedItem == null)
			return;

		try {
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("equipmentClassName", selectedItem.getEquipmentClassName());
			List<EquipmentDVO> listEquipment = new EquipmentDAO().listEquipment(hashMap);
			cbEquipmentId.getItems().setAll(listEquipment);
			showStatusMessage("Equipment 조회 완료");
		} catch (Exception e) {
			e.printStackTrace();
			showStatusMessage(e.getMessage());

		}

	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 19.
	 */
	@FXML
	public void cbEventNameOnAction() {
		// cbEventName.getSelectionModel().clearSelection();
		EquipmentEventDVO selectedItem = cbEventName.getSelectionModel().getSelectedItem();
		if (selectedItem == null)
			return;

		searchEquipmentInfo(selectedItem);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 19.
	 * @param message
	 */
	public void showStatusMessage(String message) {
		this.root.showStatusMessage(message);
	}
}
