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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.fxloader.FXMLController;
import com.kyj.fx.fxloader.FxPostInitialize;
import com.kyj.fx.nightmare.actions.ai.ResponseModelDVO.Choice;
import com.kyj.fx.nightmare.comm.DialogUtil;
import com.kyj.fx.nightmare.comm.FxClipboardUtil;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.ResourceLoader;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.ui.frame.AbstractCommonsApp;

import chat.rest.api.service.core.VirtualPool;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
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
	private Button btnMic;

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
		MenuItem miPlayMyVoice = new MenuItem("Play my voice");
		speechCtx.getItems().add(miPlayMyVoice);

		MenuItem miPlaySound = new MenuItem("Play sound");
		speechCtx.getItems().add(miPlaySound);
		
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

								if ("me".equals(lbl.getTip()))
								{
									if(getStyleClass().indexOf("me") == -1)
										getStyleClass().add("me");
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
					miPlayMyVoice.setOnAction(ac -> {
						SpechLabel lbl = (SpechLabel) item;
						lbl.playMyVoid();
					});
					
					miPlaySound.setVisible(!speechMenuVisible);
					miPlaySound.setOnAction(ac->{
						DefaultLabel lbl = (DefaultLabel) item;
						lbl.readAndPlay();
					});
				});
				return listCell;
			}
		});
		FxUtil.installClipboardKeyEvent(lvResult, stringConverter);

//		this.lvResult.getItems().addListener(new ListChangeListener<DefaultLabel>() {
//
//			@Override
//			public void onChanged(Change<? extends DefaultLabel> c) {
//				while(c.next())
//				{
//					if(c.wasAdded())
//					{
//						List<? extends DefaultLabel> addedSubList = c.getAddedSubList();
//						addedSubList.forEach(DefaultLabel::playSound);
//					}
//				}
//			}
//		});
//		this.lvResult.addEventFilter(EventType., null);
//		this.lvResult.itemsProperty().addListener(new ChangeListener<DefaultLabel>() {
//
//			@Override
//			public void changed(ObservableValue<? extends DefaultLabel> observable, DefaultLabel oldValue, DefaultLabel newValue) {
//				newValue.playSound();
//			}
//		});
		
		// this.lvResult.setContextMenu(speechCtx);
		// this.lvResult.setOnContextMenuRequested(ev->{
		// Object source = ev.getSource();
		// Node intersectedNode = ev.getPickResult().getIntersectedNode();
		// boolean value = intersectedNode instanceof SpechLabel;
		// miPlayMyVoice.setVisible(value);
		// miPlayMyVoice.setOnAction(ac->{
		// SpechLabel lbl = (SpechLabel) intersectedNode;
		// lbl.playMyVoid();
		// });
		// });
	}
	
	
	@FxPostInitialize
	public void after() {
		
	}

	ContextMenu speechCtx = new ContextMenu();

	@FXML
	public void btnEnterOnAction() {

		String prompt = txtPrompt.getText();
		DefaultLabel lblMe = new DefaultLabel(prompt, new Label(" 나 "));
		lblMe.setTip("me");
		lvResult.getItems().add(lblMe);
		search(prompt);
	}
	
	void search(String msg) {
		VirtualPool.newInstance().execute(() -> {
			try {
				OpenAIService openAIService = new OpenAIService();
				String send = openAIService.send(msg);
				Platform.runLater(() -> {

					lvResult.getItems().add(new DefaultLabel("", new Label(openAIService.getConfig().getModel())));
					ResponseModelDVO fromGtpResultMessage = ResponseModelDVO.fromGtpResultMessage(send);
					LOGGER.info("{}", fromGtpResultMessage);
					List<Choice> choices = fromGtpResultMessage.getChoices();
					choices.forEach(c -> {
						try {
							
							
							String content2 = c.getMessage().getContent();
							
							if("Y".equals(ResourceLoader.getInstance().get(ResourceLoader.AI_AUTO_PLAY_SOUND_YN, "N"))) {
								var allData = new DefaultLabel(content2);
								allData.readAndPlay();	
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

				});

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

	AudioHelper audioHelper;
	// 마이크 세팅
	MixerSettings mixerSettings;

	@FXML
	public void btnMicOnAction() {

		if (mixerSettings == null) {
			mixerSettings = new MixerSettings();
			mixerSettings.load();
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
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
