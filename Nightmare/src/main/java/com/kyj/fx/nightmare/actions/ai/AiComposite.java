/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sound.sampled.Mixer.Info;

import org.fxmisc.richtext.CodeArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.fxloader.FXMLController;
import com.kyj.fx.fxloader.FxPostInitialize;
import com.kyj.fx.groovy.DefaultScriptEngine;
import com.kyj.fx.nightmare.actions.ai.ResponseModelDVO.Choice;
import com.kyj.fx.nightmare.comm.DialogUtil;
import com.kyj.fx.nightmare.comm.ExecutorDemons;
import com.kyj.fx.nightmare.comm.FxClipboardUtil;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.Message;
import com.kyj.fx.nightmare.comm.ResourceLoader;
import com.kyj.fx.nightmare.comm.StageStore;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.comm.engine.GroovyScriptEngine;
import com.kyj.fx.nightmare.ui.frame.AbstractCommonsApp;

import chat.rest.api.service.core.VirtualPool;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.util.StringConverter;

/**
 * AI 관련 유저 인터페이스
 */
@FXMLController(value = "AiView.fxml", isSelfController = true, css = "AiView.css")
public class AiComposite extends AbstractCommonsApp {

	private static final Logger LOGGER = LoggerFactory.getLogger(AiComposite.class);

	@FXML
	private ListView lvChats;
	@FXML
	private TextArea txtPrompt;
	@FXML
	private ListView<DefaultLabel> lvResult;
	@FXML
	private Button btnMic, btnMicStop, btnEnter;
	private ObjectProperty<DefaultLabel> playingObject = new SimpleObjectProperty<>();
	// 오디오 플레이어
	AudioHelper audioHelper;
	// 마이크 세팅
	MixerSettings mixerSettings;
	// AI 리스트뷰 컨텍스트
	ContextMenu speechCtx = new ContextMenu();
	// boolean useMicrophoneFlag;
	BooleanProperty useMicrophoneFlag = new SimpleBooleanProperty();
	OpenAIService openAIService;
	AIDataDAO dao;
	
	public AiComposite() throws Exception {

		FxUtil.loadRoot(AiComposite.class, this);
	}

	StringConverter<DefaultLabel> stringConverter = new StringConverter<DefaultLabel>() {

		@Override
		public String toString(DefaultLabel object) {
			return object == null ? "" : object.getText();
		}

		@Override
		public DefaultLabel fromString(String string) {
			return null;
		}
	};

	@FXML
	public void initialize() {
		useMicrophoneFlag.set("Y".equals(ResourceLoader.getInstance().get(ResourceLoader.AI_AUTO_PLAY_SOUND_YN, "N")));
		MenuItem miPlayMyVoice = new MenuItem("Play my voice");
		miPlayMyVoice.setOnAction(ac -> {
			DefaultLabel lbl = lvResult.getSelectionModel().getSelectedItem();
			playingObject.set(null);
			playingObject.set(lbl);
		});
		speechCtx.getItems().add(miPlayMyVoice);

		MenuItem miPlaySound = new MenuItem("Play sound");
		miPlaySound.setOnAction(ac -> {
			DefaultLabel lbl = lvResult.getSelectionModel().getSelectedItem();
			playingObject.set(null);
			playingObject.set(lbl);
		});
		speechCtx.getItems().add(miPlaySound);

		MenuItem miRunCode = new MenuItem("Run Code");
		miRunCode.setOnAction(ac -> {
			DefaultLabel lbl = lvResult.getSelectionModel().getSelectedItem();
			if (lbl instanceof CodeLabel) {
				CodeLabel cl = (CodeLabel) lbl;
				String script = cl.getText();

				if (script.isBlank())
					return;
				if ("groovy".equals(cl.getCodeType())) {
					ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(() -> {

						// FxUtil.createStageAndShow(new CodeArea(script),
						// stage->{
						//
						// });

						try {
							 DefaultScriptEngine engine = new DefaultScriptEngine();
							 engine.execute(script);
//							GroovyScriptEngine engine = new GroovyScriptEngine();
//							engine.eval(script);
						} catch (Exception e) {
							e.printStackTrace();
						}
					});
				}

			}
		});

		speechCtx.getItems().add(miRunCode);

		this.lvResult.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		this.lvResult.setCellFactory(new Callback<ListView<DefaultLabel>, ListCell<DefaultLabel>>() {

			@Override
			public ListCell<DefaultLabel> call(ListView<DefaultLabel> param) {

				ListCell<DefaultLabel> listCell = new ListCell<DefaultLabel>() {

					@Override
					public void updateIndex(int i) {
						super.updateIndex(i);
					}

					@Override
					protected void updateItem(DefaultLabel item, boolean empty) {
						super.updateItem(item, empty);

						if (empty) {
							setText("");
							setGraphic(null);
						} else {
							if (item == null) {
								setText("");
								setGraphic(null);
							} else {
								setText(stringConverter.toString(item));

								DefaultLabel lbl = (DefaultLabel) item;
								// Node graphic = lbl.getGraphic();

								if ("me".equals(lbl.getTip())) {
									if (getStyleClass().indexOf("me") == -1)
										getStyleClass().add("me");
								} else {
									getStyleClass().remove("me");
								}

								setGraphic(item.getGraphic());
							}
						}
					}

				};

				listCell.setContextMenu(speechCtx);
				listCell.setOnContextMenuRequested(ev -> {
					Object item = listCell.getItem();
					boolean speechMenuVisible = item instanceof SpechLabel;
					miPlayMyVoice.setVisible(speechMenuVisible);
					miPlaySound.setVisible(!speechMenuVisible);
					miRunCode.setVisible(item instanceof CodeLabel);
				});
				return listCell;
			}
		});
		FxUtil.installClipboardKeyEvent(lvResult, stringConverter);

		playingObject.addListener(new ChangeListener<DefaultLabel>() {

			@Override
			public void changed(ObservableValue<? extends DefaultLabel> observable, DefaultLabel oldValue, DefaultLabel newValue) {
				

				if (oldValue != null) {
					oldValue.setOnPlayEnd(null);
					oldValue.setOnPlayStart(null);
					oldValue.stop();
				}

				if (newValue != null) {
					newValue.setOnPlayStart(v -> {
						btnMicStop.setDisable(false);
					});
					newValue.setOnPlayEnd(v -> {
						btnMicStop.setDisable(true);
					});
					
					//마이크 사용인 경우만 재생.
					if (useMicrophoneFlag.get())
						newValue.playSound();
				}
			}
		});
		
		try {
			mixerSettings = new MixerSettings();
			mixerSettings.load();
			dao = AIDataDAO.getInstance();
			openAIService = new OpenAIService();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	@FxPostInitialize
	public void after() {
		ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(() -> {
			List<Map<String, Object>> lastHistory = dao.getLastHistory();
			lastHistory.stream().peek(System.out::println).filter(v -> v != null).filter(m -> !m.isEmpty()).forEach(m -> {

				String prompt = m.get("QUESTION") == null ? "" : m.get("QUESTION").toString();
				DefaultLabel lblMe = new DefaultLabel(prompt, new Label(" 나 "));
				lblMe.setTip("me");
				Platform.runLater(() -> {
					lvResult.getItems().add(lblMe);
				});

				if (m.get("ANSWER") != null) {
					Platform.runLater(() -> {
						updateChatList(m.get("ANSWER").toString(), false);
					});
				}

			});
		});

	}

	@FXML
	public void btnEnterOnAction() {
		if(btnEnter.isDisable())return;
		
		String prompt = txtPrompt.getText();
		DefaultLabel lblMe = new DefaultLabel(prompt, new Label(" 나 "));
		lblMe.setTip("me");
		lvResult.getItems().add(lblMe);
		search(prompt);
	}

	void search(String msg) {
		btnEnter.setDisable(true);
		VirtualPool.newInstance().execute(() -> {
			try {
				String send = openAIService.send(msg);
				Platform.runLater(() -> {
					lvResult.getItems().add(new DefaultLabel("", new Label(openAIService.getConfig().getModel())));
					updateChatList(send);
				});

			} catch (Exception e) {
				LOGGER.error(ValueUtil.toString(e));
			}finally {
				Platform.runLater(()->{
					btnEnter.setDisable(false);	
				});
			}
		});
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2024. 6. 10.
	 * @param send
	 */
	private void updateChatList(String send) {
		updateChatList(send, true);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2024. 6. 10.
	 * @param send
	 * @param speack
	 */
	private void updateChatList(String send, boolean speack) {
		ResponseModelDVO fromGtpResultMessage = ResponseModelDVO.fromGtpResultMessage(send);
		LOGGER.info("{}", fromGtpResultMessage);
		List<Choice> choices = fromGtpResultMessage.getChoices();
		choices.forEach(c -> {
			try {

				String content2 = c.getMessage().getContent();

				if (speack && useMicrophoneFlag.get()) {
					var allData = new DefaultLabel(content2);
					playingObject.set(allData);
				}

				LOGGER.debug(content2);
				LineNumberReader br = new LineNumberReader(new StringReader(content2));
				String temp = null;
				boolean isCodeBlock = false;
				String codeType = "";
				StringBuilder sb = new StringBuilder();
				while ((temp = br.readLine()) != null) {
					if (temp.startsWith("```") && !isCodeBlock) {
						isCodeBlock = true;
						codeType = temp.replace("```", "");
						continue;
					}

					if (temp.startsWith("```") && isCodeBlock) {
						isCodeBlock = false;
						CodeLabel content = new CodeLabel(sb.toString());
						Label graphic = new Label("Copy");
						graphic.setOnMouseClicked(ev -> {
							FxClipboardUtil.putString(content.getText());
							DialogUtil.showMessageDialog("클립보드에 복사되었습니다.");
						});
						content.setGraphic(graphic);

						content.setCodeType(codeType);
						sb.setLength(0);
						lvResult.getItems().add(content);
						continue;
					}

					if (isCodeBlock) {
						sb.append(temp).append(System.lineSeparator());
					} else {
						DefaultLabel content = new DefaultLabel(temp);
						lvResult.getItems().add(content);
					}
				}

			} catch (Exception e) {
				LOGGER.error(ValueUtil.toString(e));
			}
		});
	}

	@FXML
	public void txtPromptOnKeyPressed(KeyEvent ke) {
		if (ke.getCode() == KeyCode.ENTER && ke.isShiftDown()) {
			btnEnterOnAction();
		}
	}

	@FXML
	public void btnMicOnAction() {

		if (!useMicrophoneFlag.get()) {
			Pair<String, String> pair = DialogUtil.showYesOrNoDialog("마이크 선택", "마이크 설정이 비활성화되어 있습니다. 활성화 하시겠습니까?").get();
			if ("Y".equals(pair.getValue())) {
				miMicrophoneOnAction();
				useMicrophoneFlag.set(true);
			}
			return;
		}

		if (audioHelper == null)
			audioHelper = new AudioHelper();

		if (audioHelper.isRecording()) {
			boolean createTempFlag = "Y".equals(ResourceLoader.getInstance().get(ResourceLoader.AI_CREATE_WAVE_FILE_YN, "Y"));
			String tmpdir = ResourceLoader.getInstance().get(ResourceLoader.AI_CREATE_WAVE_FILE_DIR, "tmp");

			try {
				byte[] data = audioHelper.stop();
				Path tempFilePath = Path.of(tmpdir, System.currentTimeMillis() + ".wav");
				if (createTempFlag) {
					tempFilePath.toFile().getParentFile().mkdirs();
					Files.write(tempFilePath, data);
				}

				btnMic.setText("마이크");

				SpeechToTextGptServiceImpl service = new SpeechToTextGptServiceImpl();
				String send = "";
				SpechLabel content;
				if (createTempFlag) {
					send = service.send(tempFilePath);
					content = new SpechLabel(send, new Label(" 나 "), tempFilePath);
				} else {
					send = service.send(tempFilePath.getFileName().toString(), data);
					content = new SpechLabel(send, new Label(" 나 "), data);
				}
				content.setTip("me");

				txtPrompt.setText(tmpdir);
				lvResult.getItems().add(content);
				search(send);
				// Desktop.getDesktop().browse(of.toUri());

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {

			try {
				audioHelper.setMixer(mixerSettings.getMixer());
				audioHelper.start();
				btnMic.setText(audioHelper.isRecording() ? "중지" : "마이크");
			} catch (MixerNotFound e) {
				DialogUtil.showMessageDialog(e.getMessage());
			} catch (IllegalArgumentException e) {
				LOGGER.error(ValueUtil.toString(e));
				DialogUtil.showMessageDialog("지원되지않는 마이크 형식입니다.");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	public void miMicrophoneOnAction() {
		Info[] mixerInfos = mixerSettings.getMixers();

		List<InfoDVO> collect = Stream.of(mixerInfos).map(InfoDVO::new).collect(Collectors.toList());
		try {
			BorderPane root = new BorderPane();
			TableView<InfoDVO> tableView = FxUtil.createTableView(collect);

			root.setCenter(tableView);
			Button btn = new Button("선택");
			btn.setOnAction(ae -> {

				DialogUtil.showYesOrNoDialog("마이크 선택", "기본 마이크로 선택하시겠습니까?").ifPresent(v -> {
					if ("Y".equals(v.getValue())) {
						InfoDVO selectedItem = tableView.getSelectionModel().getSelectedItem();
						// String name = selectedItem.getName();
						Info info = selectedItem.getInfo();

						mixerSettings.createSettings(info);
						mixerSettings.load();

						// SaveComplete=저장되었습니다.
						DialogUtil.showMessageDialog(Message.getInstance().getMessage("SaveComplete"));
					}
				});

			});
			root.setBottom(btn);
			FxUtil.createStageAndShowAndWait(root, stage -> {
				stage.setWidth(800d);
				stage.initOwner(StageStore.getPrimaryStage());
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// StringBuilder sb = new StringBuilder();
		// for (int i = 0; i < mixerInfos.length; i++) {
		// Info info = mixerInfos[i];
		// sb.append(info.getName()).append("\t").append(info.getDescription()).append("\n");
		// }
		// DialogUtil.showMessageDialog(sb.toString());
	}

	@FXML
	public void btnMicStopOnAction() {
		if (playingObject.get() != null) {
			playingObject.get().stop();
		}
	}
}
