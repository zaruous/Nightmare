/**
 * 
 */
package com.kyj.fx.nightmare.actions.grid;

import java.io.IOException;
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
import com.kyj.fx.nightmare.actions.ai.ResponseModelDVO.Choice;
import com.kyj.fx.nightmare.actions.comm.ai.PyCodeBuilder;
import com.kyj.fx.nightmare.comm.DialogUtil;
import com.kyj.fx.nightmare.comm.ExecutorDemons;
import com.kyj.fx.nightmare.comm.FxClipboardUtil;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.ui.frame.AbstractCommonsApp;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * 
 */
@FXMLController(value = "DefaultSpreadItemView.fxml", css = "DefaultSpreadView.css", isSelfController = true)
public class DefaultSpreadItemComposite extends AbstractCommonsApp {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSpreadItemComposite.class);

	@FXML
	private BorderPane borRoot, borContent;
	@FXML
	private Button btnEnter;
	@FXML
	private TextArea txtPrompt;
	@FXML
	private ListView<DefaultLabel> lvResult;

	private ObjectProperty<OpenAIService> openAIService = new SimpleObjectProperty<>();
	private ObjectProperty<DefaultSpreadSheetView> sv = new SimpleObjectProperty<>();

	private int row = 100;
	private int column = 100;
	ContextMenu ctx = new ContextMenu();

	public DefaultSpreadItemComposite() {
		try {
			FxUtil.loadRoot(DefaultSpreadItemComposite.class, this);
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}

	public DefaultSpreadItemComposite(int row, int column) {
		this.row = row;
		this.column = column;
		try {
			FxUtil.loadRoot(DefaultSpreadItemComposite.class, this);
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}

	public final DefaultSpreadSheetView getSpreadSheet() {
		return sv.get();
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
		sv.set(new DefaultSpreadSheetView(DefaultGridBase.createGrid(row, column)));
		borContent.setCenter(sv.get());

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

		MenuItem miRun = new MenuItem("Run");
		miRun.setAccelerator(KeyCombination.keyCombination("F5"));
		EventHandler<ActionEvent> value = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ev) {
				DefaultLabel selectedItem = lvResult.getSelectionModel().getSelectedItem();
				if (selectedItem instanceof CodeLabel) {
					CodeLabel codeLabel = (CodeLabel) selectedItem;
					String codeType = codeLabel.getCodeType();
					if ("python".equals(codeType)) {
						ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(() -> {
							try {
								PyCodeBuilder pyCodeBuilder = new PyCodeBuilder();
								pyCodeBuilder.codeType(codeType);
								
								String pre = """
								from matplotlib import font_manager, rc, rcParams

								# 한글 폰트 설정
								font_path = 'C:/Users/KYJ/AppData/Local/Microsoft/Windows/Fonts/NanumGothic.ttf'  # 폰트 파일 경로
								font = font_manager.FontProperties(fname=font_path).get_name()
								
								""";
								
								
								pyCodeBuilder.code(System.currentTimeMillis() + ".py", pre + codeLabel.getText());
								pyCodeBuilder.run();
							} catch (IOException e) {
								e.printStackTrace();
							}
						});
					}

				}
			}
		};
		miRun.setOnAction(value);
		ctx.getItems().add(miRun);
	}

	public void setAiService(OpenAIService openAIService) {
		this.openAIService.set(openAIService);
	}

	private DefaultSpreadComposite parentComposite;

	public void setParentComposite(DefaultSpreadComposite defaultSpreadComposite) {
		this.parentComposite = defaultSpreadComposite;
	}

	private Tab currentTab;

	public void setCurrentTab(Tab e) {
		this.currentTab = e;
	}

	/*******************************************************************************************************************/

	@FXML
	public void txtPromptOnKeyPressed(KeyEvent ke) {
		if (ke.getCode() == KeyCode.ENTER && ke.isShiftDown()) {
			btnEnterOnAction();
		}
	}

	@FXML
	public void btnEnterOnAction() {

		if (btnEnter.isDisable())
			return;

		String text = txtPrompt.getText();
		String systemContent = getAllDocumentText();
		if (systemContent == null) {
			btnEnter.setDisable(true);
			return;
		}

		OpenAIService openAIService = this.openAIService.get();
		Map<String, String> default1 = openAIService.createDefault(systemContent);
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
	 * @return
	 */
	private String getAllDocumentText() {
		return parentComposite.getDocumentText();
	}

	/**
	 * @return
	 */
	private String getDocumentText() {
		return DefaultGridBase.toString(getSpreadSheet().getView().getGrid());
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2024. 6. 10.
	 * @param send
	 * @param speack
	 */
	private void updateChatList(String send) {
		
//		ResponseModelDVO fromGtpResultMessage = //ResponseModelDVO.fromGtpResultMessage(send);
//		LOGGER.info("{}", fromGtpResultMessage);
//		List<Choice> choices = fromGtpResultMessage.getChoices();
//		choices.forEach(c -> {});

		try {
			String content2 = openAIService.get().toUserMessage(send);
//			String content2 = c.getMessage().getContent();

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
					graphic.getStyleClass().add("code-label-item-cell");
					graphic.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent ev) {
							FxClipboardUtil.putString(content.getText());
							DialogUtil.showMessageDialog("클립보드에 복사되었습니다.");
						}
					});

					VBox vBox = new VBox(graphic);
					vBox.setSpacing(5.0d);
					content.setGraphic(vBox);

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
	
	}
}
