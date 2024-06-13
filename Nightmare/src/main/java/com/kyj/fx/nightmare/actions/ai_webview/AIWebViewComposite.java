/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai_webview;

import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.fxloader.FXMLController;
import com.kyj.fx.nightmare.actions.ai.CodeLabel;
import com.kyj.fx.nightmare.actions.ai.DefaultLabel;
import com.kyj.fx.nightmare.actions.ai.OpenAIService;
import com.kyj.fx.nightmare.actions.ai.ResponseModelDVO;
import com.kyj.fx.nightmare.actions.ai.SpeechLabel;
import com.kyj.fx.nightmare.actions.ai.ResponseModelDVO.Choice;
import com.kyj.fx.nightmare.comm.DialogUtil;
import com.kyj.fx.nightmare.comm.FxClipboardUtil;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.ValueUtil;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * 
 */
@FXMLController(value = "AIWebView.fxml", isSelfController = true)
public class AIWebViewComposite extends BorderPane {
	private static final Logger LOGGER = LoggerFactory.getLogger(AIWebViewComposite.class);
	@FXML
	private BorderPane borRoot;
	@FXML
	private WebView wbDefault;
	@FXML
	private TextField txtUrl;
	@FXML
	private TextArea txtPrompt;
	@FXML
	private ComboBox<String> cbProtocol;
	@FXML
	private SplitPane spContent;
	@FXML
	private Button btnEnter;
	@FXML
	private ListView<DefaultLabel> lvResult;
	OpenAIService openAIService;

	public AIWebViewComposite() {
		try {
			FxUtil.loadRoot(AIWebViewComposite.class, this);
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}

	private StringProperty content = new SimpleStringProperty();

	@FXML
	public void initialize() {
		cbProtocol.getItems().addAll("https://", "http://");
		cbProtocol.getSelectionModel().selectFirst();

		wbDefault.getEngine().setOnError(ev -> {
			LOGGER.error(ValueUtil.toString(ev.getException()));
		});
		wbDefault.getEngine().load("https://news.google.com/home?hl=ko&gl=KR&ceid=KR:ko");
		wbDefault.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {

			@Override
			public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
				content.set(null);
				if (State.SUCCEEDED == newValue) {
					// JavaScript를 사용하여 <script> 태그를 제거한 body 콘텐츠를 가져오기
					String bodyContentWithoutScripts = (String) wbDefault.getEngine()
							.executeScript("document.body.innerText");

					// HTML 출력
					System.out.println(bodyContentWithoutScripts);
					content.set(bodyContentWithoutScripts);
				}

			}
		});

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

//				listCell.setContextMenu(speechCtx);
//				listCell.setOnContextMenuRequested(ev -> {
//					Object item = listCell.getItem();
//					boolean speechMenuVisible = item instanceof SpeechLabel;
//					miPlayMyVoice.setVisible(speechMenuVisible);
//					miPlaySound.setVisible(!speechMenuVisible);
//					miRunCode.setVisible(item instanceof CodeLabel);
//				});

				return listCell;
			}
		});
		FxUtil.installClipboardKeyEvent(lvResult, stringConverter);
		try {
			openAIService = new OpenAIService();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	public void txtUrlOnKeyRelease(KeyEvent ke) {
		if (ke.getCode() == KeyCode.ENTER) {
			if (ke.isConsumed())
				return;
			ke.consume();

			String text = txtUrl.getText();
			if (text.startsWith("https://") || (text.startsWith("http://")))
				wbDefault.getEngine().load(text);
			else
				wbDefault.getEngine().load(cbProtocol.getSelectionModel().getSelectedItem() + text);
		}
	}

	public final StringProperty contentProperty() {
		return this.content;
	}

	public final String getContent() {
		return this.contentProperty().get();
	}

	public final void setContent(final String content) {
		this.contentProperty().set(content);
	}

	@FXML
	public void txtPromptOnKeyPressed(KeyEvent ke) {
		if (ke.getCode() == KeyCode.ENTER && ke.isShiftDown()) {
			btnEnterOnAction();
		}
	}

	@FXML
	private void btnEnterOnAction() {
		String text = txtPrompt.getText();
		openAIService.setSystemRole(Map.of("type", "text", "content", content.get()));
		try {

			String prompt = txtPrompt.getText();
			DefaultLabel lblMe = new DefaultLabel(prompt, new Label(" 나 "));
			lblMe.setTip("me");
			lvResult.getItems().add(lblMe);

			String send = openAIService.send(text);
			updateChatList(send);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2024. 6. 10.
	 * @param send
	 * @param speack
	 */
	private void updateChatList(String send) {
		ResponseModelDVO fromGtpResultMessage = ResponseModelDVO.fromGtpResultMessage(send);
		LOGGER.info("{}", fromGtpResultMessage);
		List<Choice> choices = fromGtpResultMessage.getChoices();
		choices.forEach(c -> {
			try {

				String content2 = c.getMessage().getContent();

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
}
