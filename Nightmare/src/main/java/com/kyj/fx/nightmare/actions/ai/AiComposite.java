/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sound.sampled.Mixer.Info;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.fxloader.FXMLController;
import com.kyj.fx.fxloader.FxPostInitialize;
import com.kyj.fx.groovy.DefaultScriptEngine;
import com.kyj.fx.nightmare.actions.ai_voice.SimpleAIVoiceConversionComposite;
import com.kyj.fx.nightmare.actions.ai_webview.AIWebViewComposite;
import com.kyj.fx.nightmare.actions.comm.ai.PythonHelper;
import com.kyj.fx.nightmare.actions.grid.DefaultSpreadComposite;
import com.kyj.fx.nightmare.comm.DialogUtil;
import com.kyj.fx.nightmare.comm.ExecutorDemons;
import com.kyj.fx.nightmare.comm.FileUtil;
import com.kyj.fx.nightmare.comm.FxClipboardUtil;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.GargoyleExtensionFilters;
import com.kyj.fx.nightmare.comm.Message;
import com.kyj.fx.nightmare.comm.ResourceLoader;
import com.kyj.fx.nightmare.comm.StageStore;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.comm.codearea.CodeAreaHelper;
import com.kyj.fx.nightmare.comm.report.ReportHelper;
import com.kyj.fx.nightmare.ui.frame.AbstractCommonsApp;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Skin;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
	ListView<TbmSmPrompts> lvChats;
	@FXML
	private TextArea txtPrompt;
	@FXML
	ListView<DefaultLabel> lvResult;
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
	SpeechToTextGptServiceImpl speechService;
	AIDataDAO dao;
	@FXML
	private RadioMenuItem rbSpeackingYes, rbSpeackingNo;
	@FXML
	private CheckMenuItem cmiResponseMicAnswer;
	// 응답에 소리를 낼지 여부
	BooleanProperty useSpeakingFlag = new SimpleBooleanProperty();

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
		boolean f = "Y".equals(ResourceLoader.getInstance().get(ResourceLoader.AI_AUTO_PLAY_SOUND_YN, "N"));
		useMicrophoneFlag.set(true);
		useSpeakingFlag.set(f);
		
		if(f) {
			rbSpeackingYes.setSelected(true);
		}
		else
			rbSpeackingNo.setSelected(true);
			
		rbSpeackingYes.setOnAction(ev -> {
			useSpeakingFlag.set(true);
		});
		rbSpeackingNo.setOnAction(ev -> {
			useSpeakingFlag.set(false);
		});
		// rbNoAnswerMic.setOnAction(ev ->{
		// rbNoAnswerMic.setSelected(!rbNoAnswerMic.isSelected());
		// });

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
						try {
							DefaultScriptEngine engine = new DefaultScriptEngine();
							engine.execute(script);
						} catch (Exception e) {
							LOGGER.error(ValueUtil.toString(e));
							DialogUtil.showExceptionDailog(e);
						}
					});
				} else if ("python".equals(cl.getCodeType())) {

					ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(() -> {
						ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
						PythonHelper.exec(cl.getCodeType(), script, outputStream);
						String string = outputStream.toString(StandardCharsets.UTF_8);
						LOGGER.info(string);
						Platform.runLater(()->{
							cl.setResult(string);
							TextArea parent = new TextArea(string);
							parent.setWrapText(true);
							FxUtil.createStageAndShow(parent, stage->{});
						});
					});

				}

			}
		});
		
		
		MenuItem miProperty = new MenuItem("Property");
		miProperty.setOnAction(ev -> {

			CodeArea parent = new CodeArea();
			IntFunction<javafx.scene.Node> intFunction = LineNumberFactory.get(parent);
			parent.setParagraphGraphicFactory(intFunction);
			new CodeAreaHelper<CodeArea>(parent);
			parent.setWrapText(true);

			DefaultLabel lbl = lvResult.getSelectionModel().getSelectedItem();
			parent.appendText(lbl.getClass().getName() + "\n");
			if (lbl instanceof CodeLabel) {
				parent.appendText("code type : " + ((CodeLabel) lbl).getCodeType() + "\n");
			}
			parent.appendText("text : " + lbl.getText() + "\n");

			FxUtil.createStageAndShow(parent, stage -> {
				stage.setWidth(1200d);
				stage.setHeight(800d);
			});
		});

		MenuItem miReport = new MenuItem("Report");
		miReport.setOnAction(ev -> {
			ObservableList<DefaultLabel> selectedItems = lvResult.getSelectionModel().getSelectedItems();
			if(selectedItems.isEmpty())return;
			
			File outFile = DialogUtil.showFileSaveDialog(chooser->{
				chooser.setInitialFileName("report.html");
				chooser.getExtensionFilters().add(GargoyleExtensionFilters.HTML_FILTER);
				chooser.getExtensionFilters().add(GargoyleExtensionFilters.ALL_FILTER);
			});
			if(outFile == null)return;
			ReportHelper.generate(selectedItems, outFile);
			FileUtil.openFile(outFile);
		});
		
		MenuItem miClear = new MenuItem("Clear");
		miClear.setOnAction((ev)->{ lvResult.getItems().clear(); });
		
		speechCtx.getItems().addAll(miRunCode, miReport , miClear, miProperty);

		this.lvResult.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		this.lvResult.setCellFactory(new Callback<ListView<DefaultLabel>, ListCell<DefaultLabel>>() {

			@Override
			public ListCell<DefaultLabel> call(ListView<DefaultLabel> param) {

				ListCell<DefaultLabel> listCell = new ListCell<DefaultLabel>() {

					@Override
					protected Skin<?> createDefaultSkin() {
						return super.createDefaultSkin();
					}

					@Override
					public void updateIndex(int i) {
						super.updateIndex(i);
					}

					@Override
					public void updateItem(DefaultLabel item, boolean empty) {
						super.updateItem(item, empty);

						if (empty) {
							setText("");
							setGraphic(null);
						} else {
							if (item == null) {
								setText("");
								setGraphic(null);
							} else {
																
								if (item instanceof CustomLabel) {
									CustomLabel questionLabel = (CustomLabel) item;
									setGraphic(questionLabel.getGraphic());
									setText(stringConverter.toString(item));
								}
								
								else if (item instanceof DefaultLabel) {
									setGraphic(item.getGraphic());
									setText(stringConverter.toString(item));
								}
								
								if ("me".equals(item.getTip())) {
									if (getStyleClass().indexOf("me") == -1)
										getStyleClass().add("me");
								} else {
									getStyleClass().remove("me");
								}

								setPrefWidth(lvResult.getWidth() - 20); // 패딩 고려
								setStyle("-fx-wrap-text: true;");
							}
						}
					}

				};

				listCell.setContextMenu(speechCtx);
				listCell.setOnContextMenuRequested(ev -> {
					Object item = listCell.getItem();
					boolean speechMenuVisible = item instanceof SpeechLabel;
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
			public void changed(ObservableValue<? extends DefaultLabel> observable, DefaultLabel oldValue,
					DefaultLabel newValue) {

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

					// 마이크 사용인 경우만 재생.
					// if (useMicrophoneFlag.get())
					// {
					//
					// }
					if (useSpeakingFlag.get())
						newValue.playSound();
				}
			}
		});

		// 버튼에 대한 활성화에 따라 챗리스트도 UI적으로 비슷한 효과를 준다.
		btnEnter.disabledProperty().addListener((oba, o, n) -> {
			if (n) {
				txtPrompt.setOpacity(0.3d);
			} else
				txtPrompt.setOpacity(1.0d);
		});

		// 마이크 사용 여부에 따른 분기.
		if (useMicrophoneFlag.get()) {
			try {
				mixerSettings = new MixerSettings();
				mixerSettings.load();
				useMicrophoneFlag.set(true);
			} catch (MixerNotFound ex) {
				LOGGER.error(ValueUtil.toString(ex));
				miMicrophoneOnAction();
			}
		}

		try {

			dao = AIDataDAO.getInstance();
			openAIService = new OpenAIService();
			speechService = new SpeechToTextGptServiceImpl();
			List<TbmSmPrompts> customContext = dao.getCustomContext();
			customContext.forEach(v -> {
				DefaultCustomContextMenuItem e = new DefaultCustomContextMenuItem(v);
				e.setOnAction(ev -> {

					if (btnEnter.isDisable())
						return;

					DefaultCustomContextMenuItem mi = (DefaultCustomContextMenuItem) ev.getSource();
					DefaultLabel lbl = lvResult.getSelectionModel().getSelectedItem();
					String content = lbl.getText();
					TbmSmPrompts item = mi.getItem();
					String prompt = item.getPrompt();
					String request = ValueUtil.getVelocityToText(prompt, "content", content);

					ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(() -> {
						try {
							
							String send = openAIService.send(request, false);
//							String content2 = openAIService.toUserMessage(send);
							Platform.runLater(() -> {
								FxUtil.createStageAndShow(new TextArea(send), stage -> {
									stage.setTitle(item.getDisplayText());
								});
							});
						} catch (Exception e1) {
							LOGGER.error(ValueUtil.toString(e1));
						}
					});
				});
				speechCtx.getItems().add(e);
			});

		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
		}

		// Context에 대한 기능을 넣는다.
		SupportListViewInstaller.install(lvChats, this);
	}

	@FxPostInitialize
	public void after() {
		ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(() -> {
			List<Map<String, Object>> lastHistory = dao.getLatestHistory();
			lastHistory.stream().filter(v -> v != null).filter(m -> !m.isEmpty()).forEach(m -> {
				LOGGER.debug("{}", m);
				String apiName = m.get("API_NAME") == null ? "" : m.get("API_NAME").toString();
				String prompt = m.get("QUESTION") == null ? "" : m.get("QUESTION").toString();
//						String promptId = m.get("PROMPT_ID") == null ? "" : m.get("PROMPT_ID").toString();
				String graphicClass = m.get("GRAPHIC_CLASS") == null ? "" : m.get("GRAPHIC_CLASS").toString();

				DefaultLabel lblMe = new DefaultLabel(prompt, new Label(" 나 "));
				lblMe.setTip("me");
				Platform.runLater(() -> {
					lvResult.getItems().add(lblMe);
				});

				if (ValueUtil.isNotEmpty(graphicClass)) {
					try {
						ICustomSupportView supportView = (ICustomSupportView) SupportListViewInstaller
								.newInstance(graphicClass);
						CustomLabel ret = new CustomLabel(supportView);
//						String content2 = openAIService.toUserMessage(apiName, m.get("ANSWER").toString());
						supportView.setData(m.get("ANSWER") == null ? "" : m.get("ANSWER").toString());
						Platform.runLater(() -> {
							lvResult.getItems().add(ret);
						});

					} catch (Exception e) {
						e.printStackTrace();
					}
					return;
				}

				if (m.get("ANSWER") != null) {
					Platform.runLater(() -> {
						updateChatList(apiName, m.get("ANSWER").toString(), false);
					});
				}

			});
		});

	}

	public boolean isDisabledEnterButton() {
		return btnEnter.isDisable();
	}

	@FXML
	public void btnEnterOnAction() {
		if (btnEnter.isDisable())
			return;

		String prompt = txtPrompt.getText();
		DefaultLabel lblMe = new DefaultLabel(prompt, new Label(" 나 "));
		lblMe.setTip("me");
		lvResult.getItems().add(lblMe);
		lvResult.getItems().add(new DefaultLabel(""));
		search(prompt);
	}

	void search(String msg) {
		search(-1, "", msg);
	}

	void search(String systemMsg, String msg) {
		search(-1, systemMsg, msg);
	}

	void search(long speechId, String msg) {
		search(speechId, "", msg);
	}

	void search(long speechId, String system, String msg) {
		search("", speechId, system, msg, null);
	}

	void search(String promptId, String system, String msg, Function<String, DefaultLabel> customResponseHandler) {
		search(promptId, -1, system, msg, customResponseHandler);
	}

	void search(String promptId, long speechId, String system, String msg,
			Function<String, DefaultLabel> customResponseHandler) {

		btnEnter.setDisable(true);
		ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(() -> {
			try {
				openAIService.setSystemRole(openAIService.createDefault(system));
				
				String send = openAIService.send(promptId, speechId, msg);
//				String userMessage = openAIService.toUserMessage(send);
				Platform.runLater(() -> {
					lvResult.getItems().add(new DefaultLabel("", new Label(openAIService.getConfig().getModel())));
					if (customResponseHandler != null) {
						DefaultLabel apply = customResponseHandler.apply(send);
						lvResult.getItems().add(apply);

					} else {
						updateChatList(send);
					}

				});

			} catch (Exception e) {
				LOGGER.error(ValueUtil.toString(e));
			} finally {
				Platform.runLater(() -> {
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
		updateChatList(null, send, true);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2024. 6. 10.
	 * @param send
	 * @param speech
	 */
	private void updateChatList(String apiName, String send, boolean speech) {
		try {
			String content2 = send; //openAIService.toUserMessage(apiName, send);

			if (speech && useMicrophoneFlag.get()) {
				var allData = new DefaultLabel(content2);
				allData.setTip(apiName);
				playingObject.set(allData);
			}

			LOGGER.debug(content2);
			LineNumberReader br = new LineNumberReader(new StringReader(content2));
			String temp = null;
			boolean isCodeBlock = false;
			String codeType = "";
			StringBuilder sb = new StringBuilder();
			
			ArrayList<DefaultLabel> newItems = new ArrayList<DefaultLabel>();
			while ((temp = br.readLine()) != null) {
				if (temp.trim().startsWith("```") && !isCodeBlock) {
					isCodeBlock = true;
					codeType = temp.replace("```", "");
					continue;
				}

				if (temp.trim().startsWith("```") && isCodeBlock) {
					isCodeBlock = false;
					CodeLabel content = new CodeLabel(sb.toString());
					Label graphic = new Label("Copy");
					graphic.setOnMouseClicked(ev -> {
						FxClipboardUtil.putString(content.getText() + "\n" + content.getResult());
						DialogUtil.showMessageDialog("클립보드에 복사되었습니다.");
					});
					content.setGraphic(graphic);

					content.setCodeType(codeType);
					sb.setLength(0);
					newItems.add(content);
					continue;
				}

				if (isCodeBlock) {
					sb.append(temp).append(System.lineSeparator());
				} else {
					DefaultLabel content = new DefaultLabel(temp);
					newItems.add(content);
				}
			}
			//scrollTo(lvResult.getItems().size() - 1);
			if(!newItems.isEmpty())
			{
				newItems.get(0).setTip(apiName);
			}
			
			lvResult.getItems().addAll(newItems);
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
		}

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
			Pair<String, String> pair = DialogUtil.showYesOrNoDialog("마이크 선택", "마이크 설정이 비활성화되어 있습니다. 활성화 하시겠습니까?")
					.get();
			if ("Y".equals(pair.getValue())) {
				miMicrophoneOnAction();
				useMicrophoneFlag.set(true);
			}
			return;
		}

		if (audioHelper == null)
			audioHelper = new AudioHelper();

		if (audioHelper.isRecording()) {
			boolean createTempFlag = "Y"
					.equals(ResourceLoader.getInstance().get(ResourceLoader.AI_CREATE_WAVE_FILE_YN, "Y"));
			String tmpdir = ResourceLoader.getInstance().get(ResourceLoader.AI_CREATE_WAVE_FILE_DIR, "tmp");

			try {
				byte[] data = audioHelper.stop();
				Path tempFilePath = Path.of(tmpdir, System.currentTimeMillis() + ".wav");
				if (createTempFlag) {
					tempFilePath.toFile().getParentFile().mkdirs();
					Files.write(tempFilePath, data);
				}

				btnMic.setText("마이크");

				SpeechResponseModelDVO send = null;
				SpeechLabel content;
				if (createTempFlag) {
					send = speechService.send(tempFilePath);
					content = new SpeechLabel(send, new Label(" 나 "), tempFilePath);
				} else {
					send = speechService.send(tempFilePath.getFileName().toString(), data);
					content = new SpeechLabel(send, new Label(" 나 "), data);
				}
				content.setTip("me");
				lvResult.getItems().add(content);

				// if(!rbNoAnswerMic.isSelected())
				// {
				if (cmiResponseMicAnswer.isSelected())
					search(send.getId(), send.getText());
				// }

			} catch (Exception e) {
				LOGGER.error(ValueUtil.toString(e));
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
			} catch (Exception e) {
				LOGGER.error(ValueUtil.toString(e));
			}
		}
	}

	@FXML
	public void miMicrophoneOnAction() {
		Info[] mixerInfos = mixerSettings.getMixers();

		List<InfoDVO> collect = Stream.of(mixerInfos).map(InfoDVO::new).collect(Collectors.toList());
		try {

			BorderPane root = new BorderPane();
			root.setTop(new Label(mixerSettings.getConfigedMixerName()));
			TableView<InfoDVO> tableView = FxUtil.createTableView(collect);

			root.setCenter(tableView);
			Button btn = new Button("선택");
			btn.setOnAction(ae -> {

				DialogUtil
						.showYesOrNoDialog("마이크 선택", Message.getInstance()
								.getMessage("AiComposite_BaseMicSelectedQuestion" /* 기본 마이크로 선택하시겠습니까? */))
						.ifPresent(v -> {
							if ("Y".equals(v.getValue())) {
								InfoDVO selectedItem = tableView.getSelectionModel().getSelectedItem();
								// String name = selectedItem.getName();
								Info info = selectedItem.getInfo();

								mixerSettings.createSettings(info);
								mixerSettings.load();

								// SaveComplete=저장되었습니다.
								DialogUtil.showMessageDialog(
										Message.getInstance().getMessage("AiComposite_SaveComplete" /* 저장되었습니다. */));
							}
						});

			});
			btn.setPrefWidth(80d);
			HBox value = new HBox(btn);
			value.setPadding(new Insets(5));
			value.setAlignment(Pos.CENTER_RIGHT);
			root.setBottom(value);

			tableView.setOnMouseClicked(ev -> {
				if (ev.getClickCount() == 2 && ev.getButton() == MouseButton.PRIMARY) {
					btn.fire();
				}
			});
			FxUtil.createStageAndShowAndWait(root, stage -> {
				stage.setWidth(800d);
				stage.setTitle("마이크 세팅");
				stage.initOwner(StageStore.getPrimaryStage());
			});
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
			DialogUtil.showExceptionDailog(e);
		}
	}

	@FXML
	public void btnMicStopOnAction() {
		if (playingObject.get() != null) {
			playingObject.get().stop();
		}
	}

	@FXML
	public void miAiWebViewOnAction() {
		AIWebViewComposite parent = new AIWebViewComposite();
		FxUtil.createStageAndShow(parent, stage -> {
			stage.setTitle("웹페이지 처리");
		});
	}

	@FXML
	public void miSpeechToTextAction() {
		SimpleAIVoiceConversionComposite parent = new SimpleAIVoiceConversionComposite();
		FxUtil.createStageAndShow(parent, stage -> {
			stage.setTitle("Speech to Text");
		});
	}

	@Override
	public void miHomeOnAction() {
		btnMicStopOnAction();
		super.miHomeOnAction();
	}

	@FXML
	public void miExitApplicationOnAction() {
		Platform.exit();
		System.exit(0);
	}

	@FXML
	public void miSpreadOnAction() {
		DefaultSpreadComposite parent = new DefaultSpreadComposite();
		FxUtil.createStageAndShow(parent, stage -> {
			stage.setTitle("Spread");
			stage.setWidth(1200d);
			stage.setHeight(800d);
		});
	}

	@Override
	public void showStatusMessage(String message) {
		// TODO Auto-generated method stub
		
	}
}
