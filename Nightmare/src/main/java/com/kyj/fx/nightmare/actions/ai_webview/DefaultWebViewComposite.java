/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai_webview;

import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.fxloader.FXMLController;
import com.kyj.fx.nightmare.actions.ai.CodeLabel;
import com.kyj.fx.nightmare.actions.ai.DefaultLabel;
import com.kyj.fx.nightmare.actions.ai.OpenAIService;
import com.kyj.fx.nightmare.comm.DialogUtil;
import com.kyj.fx.nightmare.comm.ExecutorDemons;
import com.kyj.fx.nightmare.comm.FxClipboardUtil;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.ResourceLoader;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.comm.XmlW3cUtil;
import com.kyj.fx.nightmare.comm.codearea.CodeAreaHelper;
import com.kyj.fx.nightmare.ui.frame.AbstractCommonsApp;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.PromptData;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.util.StringConverter;

/**
 * 
 */
@FXMLController(value = "DefaultAIWebView.fxml", isSelfController = true)
public class DefaultWebViewComposite extends AbstractCommonsApp {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWebViewComposite.class);
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
	@FXML
	private TextArea txtcConsole;
	private StringProperty content = new SimpleStringProperty();
	private ObjectProperty<OpenAIService> openAIService = new SimpleObjectProperty<OpenAIService>();
	private AIWebViewComposite parentComposite;

	public WebView getWebView() {
		return this.wbDefault;
	}

	public DefaultWebViewComposite() {
		try {
			FxUtil.loadRoot(DefaultWebViewComposite.class, this);
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}

	@FXML
	public void initialize() {
		cbProtocol.getItems().addAll("https://", "http://");
		cbProtocol.getSelectionModel().selectFirst();

		wbDefault.getEngine().setUserDataDirectory(new File(".webview"));
		wbDefault.setContextMenuEnabled(true);
		wbDefault.getEngine().setJavaScriptEnabled(true);
		wbDefault.getEngine().setCreatePopupHandler(new Callback<PopupFeatures, WebEngine>() {

			@Override
			public WebEngine call(PopupFeatures param) {

				Tab newTab = parentComposite.addNewTabView("");
				DefaultWebViewComposite c = (DefaultWebViewComposite) newTab.getContent();

				WebView webView = c.getWebView();
//				// 팝업 WebEngine의 location 변경 이벤트를 통해 URL을 알림
				webView.getEngine().locationProperty().addListener((obs, oldLocation, newLocation) -> {

					webView.getEngine().load(newLocation);
				});
//
				return webView.getEngine();
			}
		});

		this.setOnKeyPressed(ev -> {
			if ((ev.getCode() == KeyCode.LEFT || ev.getCode() == KeyCode.BACK_SPACE) && ev.isAltDown()) {
				wbDefault.getEngine().getHistory().go(-1);
			}

			else if ((ev.getCode() == KeyCode.RIGHT) && ev.isControlDown()) {
				wbDefault.getEngine().getHistory().go(1);
			} else if ((ev.getCode() == KeyCode.F5)) {
				wbDefault.getEngine().reload();
			}
		});

		wbDefault.getEngine().locationProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//				String newHost = getHost(newValue);
				LOGGER.debug("newHost : {}", newValue);
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
			DialogUtil.showExceptionDailog(ev.getException());
		});

		wbDefault.getEngine().load("about:blank");

		wbDefault.getEngine().getLoadWorker().exceptionProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				LOGGER.error(ValueUtil.toString(newValue));
			}
		});

		wbDefault.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {

			@Override
			public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
				content.set(null);
				wbDefault.setOpacity(0.5d);
				btnEnter.setDisable(true);
				txtUrl.setText(wbDefault.getEngine().getLocation());

//				LOGGER.debug("engine state : {} ", newValue);
				if (State.SUCCEEDED == newValue) {
					wbDefault.setOpacity(1.0d);
					btnEnter.setDisable(false);
					current.setText(wbDefault.getEngine().getTitle());
//					current.setText(wbDefault.getEngine().getTitle());
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

						CodeArea parent = new CodeArea(text);
						IntFunction<javafx.scene.Node> intFunction = LineNumberFactory.get(parent);
						parent.setParagraphGraphicFactory(intFunction);
						new CodeAreaHelper<CodeArea>(parent);
						parent.setWrapText(true);
						FxUtil.createStageAndShow(new VirtualizedScrollPane<>(parent), stage -> {
							stage.setWidth(1200d);
							stage.setHeight(800d);
						});

					});
				} catch (Exception e) {
					e.printStackTrace();
				}

			});
		});

		MenuItem miHtml = new MenuItem("html");
		miHtml.setOnAction(ev -> {
			WebEngine engine = wbDefault.getEngine();
			String string = (String) engine.executeScript("document.documentElement.outerHTML");
			Platform.runLater(() -> {
				CodeArea parent = new CodeArea(string);
				IntFunction<javafx.scene.Node> intFunction = LineNumberFactory.get(parent);
				parent.setParagraphGraphicFactory(intFunction);
				new CodeAreaHelper<CodeArea>(parent);
				parent.setWrapText(true);
				FxUtil.createStageAndShow(new VirtualizedScrollPane<>(parent), stage -> {
					stage.setWidth(1200d);
					stage.setHeight(800d);
				});
			});
		});

		MenuItem miParseText = new MenuItem("parseText");
		miParseText.setOnAction(ev -> {

			Platform.runLater(() -> {

				CodeArea parent = new CodeArea(getDocumentText());
				IntFunction<javafx.scene.Node> intFunction = LineNumberFactory.get(parent);
				parent.setParagraphGraphicFactory(intFunction);
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

								setGraphic(item.getGraphic());
								setPrefWidth(lvResult.getWidth() - 20); // 패딩 고려
								setStyle("-fx-wrap-text: true;");
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

			load(txtUrl.getText());
		}
	}

	/**
	 * @param location
	 */
	public void load(String location) {

		if (ValueUtil.isEmpty(location))
			return;

		String _location = "";
		if (location.startsWith("https://") || (location.startsWith("http://")))
			wbDefault.getEngine().load(location);
		else {
			String def = ResourceLoader.getInstance().get("default.web.search.url", "https://www.google.com/search?q=%s");
			String srch = URLEncoder.encode(location, StandardCharsets.UTF_8);
			_location = String.format(def, srch);
//			_location = URLEncoder.encode(location, StandardCharsets.UTF_8);
//			String url = cbProtocol.getSelectionModel().getSelectedItem() + _location;
			LOGGER.debug(_location);
			wbDefault.getEngine().load(_location);
		}

		txtUrl.setText(location);

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

		OpenAIService openAIService = this.openAIService.get();
		Map<String, Object> default1 = openAIService.createDefault(systemContent);
		openAIService.setSystemRole(default1);

		String prompt = txtPrompt.getText();
		DefaultLabel lblMe = new DefaultLabel(prompt, new Label(" 나 "));
		lblMe.setTip("me");
		lvResult.getItems().add(lblMe);

		ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(() -> {
			try {
				String send = openAIService.send(text, true);
				Platform.runLater(() -> {
					try {
						updateChatList(send);
					} catch (Exception e) {
						LOGGER.error(ValueUtil.toString(e));
					} finally {
						btnEnter.setDisable(false);
					}
				});
			} catch (Exception e) {
				LOGGER.error(ValueUtil.toString(e));
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
//		String userMessage = openAIService.get().toUserMessage(send);
//		ResponseModelDVO fromGtpResultMessage = ResponseModelDVO.fromGtpResultMessage(send);
//		LOGGER.info("{}", fromGtpResultMessage);
//		List<Choice> choices = fromGtpResultMessage.getChoices();

//		String content2 = c.getMessage().getContent();
		try {
			LOGGER.debug(send);
			LineNumberReader br = new LineNumberReader(new StringReader(send));
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
		} catch (Exception ex) {
			LOGGER.error(ValueUtil.toString(ex));
		}

	}

//	Object executeScript = wbDefault.getEngine().executeScript("document.documentElement.outerHTML");
//	String string = executeScript.toString();
//
//	ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(() -> {
//		try {
//			Document document = Jsoup.parse(string);
//			String text = XmlW3cUtil.parseElement(document.body());

	public String getDocumentText() {
		Object executeScript = wbDefault.getEngine().executeScript("document.documentElement.outerHTML");
		String string = executeScript.toString();
		Document document = Jsoup.parse(string);
		document.removeClass("script");
		try {
			String text = XmlW3cUtil.parseElement(document.body());
			return text;
		} catch (IOException e) {
			e.printStackTrace();
		}

		Element body = document.body();
		return body.wholeText();
	}

	public void wholeTest(Node body, StringBuilder accum) {
		LinkedList<Node> queue = new LinkedList<Node>();

		for (Node n : body.childNodes()) {

			if (n instanceof LeafNode) {
				accum.append((n.toString())).append(" ");
				continue;
			}

			List<Node> childNodes = n.childNodes();
			if (childNodes.size() != 0) {
				queue.addAll(childNodes);
			}

		}

	}

	/**
	 * @param location
	 */
	public void setLocation(String location) {
		if (Platform.isFxApplicationThread())
			load(location);
		else
			Platform.runLater(() -> {
				load(location);
			});
	}

	public void setAiService(OpenAIService openAIService) {
		this.openAIService.set(openAIService);
	}

	public void setParentComposite(AIWebViewComposite parentComposite) {
		this.parentComposite = parentComposite;
	}

	private Tab current;

	public void setCurrentTab(Tab e) {
		this.current = e;
	}

	public final ObjectProperty<OpenAIService> openAIServiceProperty() {
		return this.openAIService;
	}
	

	public final OpenAIService getOpenAIService() {
		return this.openAIServiceProperty().get();
	}
	

	public final void setOpenAIService(final OpenAIService openAIService) {
		this.openAIServiceProperty().set(openAIService);
	}

	public void setHtml(String html) {
		this.getWebView().getEngine().loadContent(html);
	}
	

}
