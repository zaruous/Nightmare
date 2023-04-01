/**
 * 
 */
package com.kyj.fx.nightmare.eqtree;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.kyj.fx.nightmare.actions.ec.ec.scripts.EquipmentClassEventScriptComposite;
import com.kyj.fx.nightmare.actions.ec.ec.scripts.EquipmentScriptDVO;
import com.kyj.fx.nightmare.actions.ec.ec.scripts.Event;
import com.kyj.fx.nightmare.comm.DialogUtil;
import com.kyj.fx.nightmare.comm.FileUtil;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.ResourceLoader;
import com.kyj.fx.nightmare.comm.StageStore;
import com.kyj.fx.nightmare.comm.ValueUtil;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * 
 * ET Script를 추출하여 지정한 디렉토리에 파일로 출력시킨다 <br/>
 * 
 * @author KYJ
 *
 */
public class MenuItemImportEtScript extends MenuItem {

	private EtConfigurationTreeView tv;

	public MenuItemImportEtScript(EtConfigurationTreeView tv, String text) {
		super(text);
		this.tv = tv;
		this.setOnAction(this::onAction);
	}

	/**
	 * @param ae
	 */
	private void onAction(ActionEvent ae) {
		TreeItem<EtConfigurationTreeDVO> selectedItem = this.tv.getSelectionModel().getSelectedItem();
		if (selectedItem == null)
			return;
		EtConfigurationTreeDVO value = selectedItem.getValue();
		if (value == null)
			return;
		// String eqClassName = value.getDisplayText();
		String eqClassGuid = value.getId();

		File file = DialogUtil.showFileDialog(StageStore.getPrimaryStage(), chooser -> {
			chooser.setSelectedExtensionFilter(new ExtensionFilter("CSV files (*.csv)", "*.csv"));
			chooser.setSelectedExtensionFilter(new ExtensionFilter("Text files (*.txt)", "*.txt"));
		});
		if (file == null)
			return;

		if (file.isDirectory())
			return;

		doImport(eqClassGuid, file);

	}

	List<String> COMMON_TYPES = Arrays.asList("공통1", "공통2", "공통3");
	List<String> ACTION_TYPES = Arrays.asList("Event_OnCancel", "Event_OnPause", "Event_OnRestart", "Event_OnUpdate",
			"Event_OnStart", "Event_OnComplete");
	List<String> ACTION_TYPES2 = Arrays.asList("On Cancel", "On Pause", "On Restart", "On Update", "On Start", "On Complete");
	/**
	 * @author KYJ (zaruous@naver.com)
	 *
	 */
	enum Action {
		Event_OnCancel(0), Event_OnPause(1), Event_OnRestart(2), Event_OnUpdate(3), Event_OnStart(4), Event_OnComplete(5);

		Action(int code) {
			this.code = code;
		}

		int code;

		/**
		 * @작성자 : KYJ (zaruous@naver.com)
		 * @작성일 : 2022. 6. 27.
		 * @param code
		 * @return
		 */
		public static Action fromCode(int code) {
			for (Action a : Action.values()) {
				if (a.code == code)
					return a;
			}
			return null;
		}
	}
	
	private void doImport(String eqClassGuid, File csvFile) {

		RootPane parent = new RootPane();
		FxUtil.createStageAndShow(parent, new Consumer<Stage>() {
			@Override
			public void accept(Stage s) {
				s.initOwner(StageStore.getPrimaryStage());
				s.setWidth(1200d);
				s.setHeight(800d);
			}
		});
		Platform.runLater(() -> {
			parent.onLoadEquipmentClass(eqClassGuid);

			String scriptBasePath = ResourceLoader.getInstance().get("et.common.script.base.path", "");
			if (ValueUtil.isEmpty(scriptBasePath)) {
				DialogUtil.showMessageDialog("et.common.script.base.path에 대한 값이 정의되야 합니다.");
				return;
			}
			try {
				var rootFile = new File(scriptBasePath);

				/* [START]COMMON_TYPE에 대한 기준 데이터 수집 */

				Path rootPath = rootFile.toPath();
				try (Stream<Path> walk = Files.walk(rootPath, 1)) {
					COMMON_TYPES = walk.filter(v -> {
						return v != rootPath;
					}).filter(v -> {
						File file = v.toFile();
						return file.isDirectory();
					}).map(v -> v.getFileName().toString()).collect(Collectors.toList());
				}
				/* [END]COMMON_TYPE에 대한 기준 데이터 수집 */

				List<String> readAllLines = Files.readAllLines(csvFile.toPath());

				

				for (String line : readAllLines) {
					String[] split = line.split("\t");
					String commonType = split[0].trim();
					String eventName = split[1].trim();
					String actionName = split[2].trim();

					if (COMMON_TYPES.contains(commonType) && (ACTION_TYPES.contains(actionName))) {
						parent.onRequestedScript(eventName, Action.valueOf(actionName).code , commonType);
					} else if (COMMON_TYPES.contains(commonType) && (ACTION_TYPES2.contains(actionName))) {
						parent.onRequestedScript(eventName, ACTION_TYPES2.indexOf(actionName), commonType);
						
					} else {
						throw new Exception("정의되지않은 commonType이거나 Action 입니다. \n"
								+ String.format("Skip  event : %s action name : %s type : %s", eventName, actionName, commonType));
					}
				}
			} catch (Exception ex) {
				DialogUtil.showExceptionDailog(ex);
			}

		});
	}

	

	/**
	 * @author KYJ (zaruous@naver.com)
	 *
	 */
	class RootPane extends BorderPane {
		DummyEtScriptComposite c;
		Button btnCommit;

		public RootPane() {

			c = new DummyEtScriptComposite();

			setCenter(c);
			btnCommit = new Button("Commit");
			btnCommit.setOnAction(this::btnCommitOnAction);
			setBottom(new HBox(btnCommit));
		}

		public void onLoadEquipmentClass(String equipmentClassGuid) {
			c.onLoadEquipmentClass(equipmentClassGuid);
		}

		public void onRequestedScript(String eventName, int actionNameCode, String commonType) {
			c.onRequestedScript(eventName, actionNameCode, commonType);
		}

		/**
		 * @작성자 : KYJ (zaruous@naver.com)
		 * @작성일 : 2022. 6. 24.
		 * @param actionevent1
		 */
		private void btnCommitOnAction(ActionEvent ae) {
			c.onCommit();
		}
	}

	/**
	 * @author KYJ (zaruous@naver.com)
	 *
	 */
	class DummyEtScriptComposite extends EquipmentClassEventScriptComposite {

		public DummyEtScriptComposite() {
			super();

		}

		@Override
		public void onLoadEquipmentClass(String equipmentClassGuid) {
			setEvent(Event.AllEvent(equipmentClassGuid));
		}

		/**
		 * @작성자 : KYJ (zaruous@naver.com)
		 * @작성일 : 2022. 6. 24.
		 * @param eventName
		 * @param actionName
		 * @param commonType
		 */
		public void onRequestedScript(String eventName, int actionNameCode, String commonType) {
			TableView<EquipmentScriptDVO> scriptTable = getScriptTable();
			Optional<EquipmentScriptDVO> findAny = scriptTable.getItems().stream().filter(v -> {
				
				Action fromCode = Action.fromCode(actionNameCode);
				String actionName = fromCode.name().toString();
				if (ValueUtil.equals(eventName, v.getEventName()) && ValueUtil.equals(actionName, v.getActionName())) {
					return true;
				}
				return false;
			}).findAny();

			if (!findAny.isPresent())
				throw new RuntimeException("일치하는 이벤트 정보가 존재하지않습니다. \n"
						+ String.format("Skip  event : %s action name : %s type : %s", eventName, Action.fromCode(actionNameCode) , commonType));

			findAny.ifPresent(v -> {
				String scriptBasePath = ResourceLoader.getInstance().get("et.common.script.base.path", "");
				if (ValueUtil.isEmpty(scriptBasePath)) {
					throw new RuntimeException("et.common.script.base.path에 대한 값이 정의되야 합니다.");
				}

				File dir = new File(new File(scriptBasePath), commonType);
				if (!dir.exists())
					throw new RuntimeException(dir + " 디렉토리가 존재하지 않음.");

				Action fromCode = Action.fromCode(actionNameCode);
				String actionName = fromCode.name().toString();
				File scriptFile = new File(dir, actionName + ".vbs");
				if (!scriptFile.exists())
					throw new RuntimeException(scriptFile.getAbsolutePath() + " 에 대한 스크립트가 정의되지 않았음.");

				try {
					String readToString = FileUtil.readToString(scriptFile);
					v.setCode(readToString);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});

		}
	}

}
