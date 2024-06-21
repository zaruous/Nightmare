/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai_webview;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.fxloader.FXMLController;
import com.kyj.fx.nightmare.actions.ai.CodeLabel;
import com.kyj.fx.nightmare.actions.ai.DefaultLabel;
import com.kyj.fx.nightmare.actions.ai.OpenAIService;
import com.kyj.fx.nightmare.actions.ai.ResponseModelDVO;
import com.kyj.fx.nightmare.actions.ai.ResponseModelDVO.Choice;
import com.kyj.fx.nightmare.comm.DialogUtil;
import com.kyj.fx.nightmare.comm.ExecutorDemons;
import com.kyj.fx.nightmare.comm.FxClipboardUtil;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.comm.XmlW3cUtil;
import com.kyj.fx.nightmare.comm.codearea.CodeAreaHelper;
import com.kyj.fx.nightmare.ui.frame.AbstractCommonsApp;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.PromptData;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.util.StringConverter;

/**
 * 
 */
@FXMLController(value = "AIWebView.fxml", isSelfController = true)
public class AIWebViewComposite extends AbstractCommonsApp {
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

		wbDefault.setContextMenuEnabled(true);
		wbDefault.getEngine().setJavaScriptEnabled(true);
		// wbDefault.getEngine().setCreatePopupHandler(new
		// Callback<PopupFeatures, WebEngine>() {
		//
		// @Override
		// public WebEngine call(PopupFeatures param) {
		// return new WebView();
		// }
		// });
//		wbDefault.getEngine().documentProperty().addListener(new ChangeListener<Document>() {
//
//			@Override
//			public void changed(ObservableValue<? extends Document> observable, Document oldValue, Document newValue) {
//				if(newValue ==null)return;
//				
//						
//					Document doc = (Document) newValue.cloneNode(true);
//String text=					doc.getTextContent();
////					XmlW3cUtil.removeScriptNodes(doc);
////					
////					String string = XmlW3cUtil.toString(doc);
////					LOGGER.debug(string);
//					content.set(text);
//			}
//		});
		wbDefault.getEngine().locationProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				String newHost = getHost(newValue);
				String urlHost = getHost(txtUrl.getText());
				LOGGER.debug("newHost : {}", newHost);
				LOGGER.debug("urlHost : {}", urlHost);

//				if (!ValueUtil.equals(urlHost, newHost)) {
//					// 동일한 호스트가 아니면 차단
//					wbDefault.getEngine().getLoadWorker().cancel();
//					LOGGER.debug("location blocked : {}", newValue);
//				}
//				else
//					LOGGER.debug("location change : {}", newValue);
			}

			private String getHost(String url) {
				try {
					URI uri = new URI(url);
					return uri.getHost();
				} catch (URISyntaxException e) {
					e.printStackTrace();
					return null;
				}
			}
		});
		wbDefault.getEngine().setPromptHandler(new Callback<PromptData, String>() {

			@Override
			public String call(PromptData param) {
				LOGGER.debug("{}", param);
				return param.getMessage();
			}
		});

		wbDefault.getEngine().setOnError(ev -> {
			LOGGER.error(ValueUtil.toString(ev.getException()));
		});

		wbDefault.getEngine().load("https://news.google.com/home?hl=ko&gl=KR&ceid=KR:ko");
		wbDefault.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {

			@Override
			public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {	
				content.set(null);
				wbDefault.setOpacity(0.5d);
				btnEnter.setDisable(true);
				txtUrl.setText(wbDefault.getEngine().getLocation());
				LOGGER.debug("engine state : {} ", newValue);
				if (State.SUCCEEDED == newValue) {
					wbDefault.setOpacity(1.0d);
					btnEnter.setDisable(false);
				}

			}
		});

		wbDefault.getEngine().setConfirmHandler(new Callback<String, Boolean>() {

			@Override
			public Boolean call(String param) {
				Pair<String, String> pair = DialogUtil.showYesOrNoDialog("confirm", param).get();
				return "Y".equals(pair.getValue());
			}
		});
		wbDefault.getEngine().setOnAlert(new EventHandler<WebEvent<String>>() {

			@Override
			public void handle(WebEvent<String> event) {
				DialogUtil.showMessageDialog(event.getData());
			}
		});

		MenuItem menuItem = new MenuItem("text");
		menuItem.setOnAction(ev -> {
			Object executeScript = wbDefault.getEngine().executeScript("document.documentElement.outerHTML");
			String string = executeScript.toString();

			ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(() -> {
				try {
					Document document = Jsoup.parse(string);
					String text = XmlW3cUtil.parseElement(document.body());
					Platform.runLater(() -> {
						TextArea parent = new TextArea(text);
						parent.setWrapText(true);
						FxUtil.createStageAndShow(parent, stage -> {
						});

					});
				} catch (Exception e) {
					e.printStackTrace();
				}

			});
		});

		MenuItem miHtml = new MenuItem("html");
		miHtml.setOnAction(ev -> {
			Object executeScript = wbDefault.getEngine().executeScript("document.documentElement.outerHTML");
			String string = executeScript.toString();

			Platform.runLater(() -> {
				TextArea parent = new TextArea(string);
				parent.setWrapText(true);
				FxUtil.createStageAndShow(parent, stage -> {
				});

			});
		});
		
		MenuItem miParseText = new MenuItem("parseText");
		miParseText.setOnAction(ev -> {
			

			Platform.runLater(() -> {
				
				CodeArea parent = new CodeArea(getDocumentText());
				parent.setWrapText(true);
				new CodeAreaHelper<CodeArea>(parent);
				
				BorderPane borRoot = new BorderPane(new VirtualizedScrollPane<>(parent));
				borRoot.setPadding(new Insets(5));
				
				FxUtil.createStageAndShow(borRoot, stage -> {
					stage.setWidth(1200d);
					stage.setHeight(800d);
				});

			});
		});
		
		ContextMenu contextMenu = new ContextMenu(menuItem, miHtml, miParseText);
		wbDefault.setContextMenuEnabled(false);
		wbDefault.setOnContextMenuRequested(ev -> {
			contextMenu.show(wbDefault, ev.getScreenX(), ev.getScreenY());
		});

		//

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

								setPrefWidth(lvResult.getWidth() - 20); // 패딩 고려
								setStyle("-fx-wrap-text: true;");

								setGraphic(item.getGraphic());
							}
						}
					}

				};

				// listCell.setContextMenu(speechCtx);
				// listCell.setOnContextMenuRequested(ev -> {
				// Object item = listCell.getItem();
				// boolean speechMenuVisible = item instanceof SpeechLabel;
				// miPlayMyVoice.setVisible(speechMenuVisible);
				// miPlaySound.setVisible(!speechMenuVisible);
				// miRunCode.setVisible(item instanceof CodeLabel);
				// });

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

		if (btnEnter.isDisable())
			return;

		String text = txtPrompt.getText();
		String systemContent = getDocumentText();
		if (systemContent == null) {
			btnEnter.setDisable(true);
			return;
		}

		openAIService.setSystemRole(openAIService.createDefault(systemContent));

		String prompt = txtPrompt.getText();
		DefaultLabel lblMe = new DefaultLabel(prompt, new Label(" 나 "));
		lblMe.setTip("me");
		lvResult.getItems().add(lblMe);

		ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(() -> {
			try {
				String send = openAIService.send(text);
				Platform.runLater(() -> {
					try {
						updateChatList(send);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						btnEnter.setDisable(false);
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

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
	
	public String getDocumentText()  {
		Object executeScript = wbDefault.getEngine().executeScript("document.documentElement.outerHTML");
		String string = executeScript.toString();
		Document document = Jsoup.parse(string);
		String text;
		try {
			text = XmlW3cUtil.parseElement(document.body());
			return text;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
