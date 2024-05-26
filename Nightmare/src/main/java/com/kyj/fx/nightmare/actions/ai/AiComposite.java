/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.fxloader.FXMLController;
import com.kyj.fx.nightmare.actions.ai.ResponseModelDVO.Choice;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.ui.frame.AbstractCommonsApp;

import chat.rest.api.service.core.VirtualPool;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * AI 관련 유저 인터페이스
 */
@FXMLController(value = "AiView.fxml", isSelfController = true)
public class AiComposite extends AbstractCommonsApp {

	private static final Logger LOGGER = LoggerFactory.getLogger(AiComposite.class);
	@FXML
	private TextArea txtPrompt;
	@FXML
	private ListView<Object> lvResult;

	public AiComposite() throws Exception {
		FxUtil.loadRoot(AiComposite.class, this);
	}

	StringConverter<Object> stringConverter = new StringConverter<Object>() {

		@Override
		public String toString(Object object) {

			if (object instanceof Label) {
				return ((Label) object).getText();
			}
			return object.toString();
		}

		@Override
		public Object fromString(String string) {
			return null;
		}
	};

	@FXML
	public void initialize() {
		this.lvResult.setCellFactory(new Callback<ListView<Object>, ListCell<Object>>() {

			@Override
			public ListCell<Object> call(ListView<Object> param) {

				ListCell<Object> listCell = new ListCell<Object>() {

					@Override
					protected void updateItem(Object item, boolean empty) {
						super.updateItem(item, empty);

						if (empty) {
							setText("");
						} else {
							if (item == null)
								setText("");
							else {
								setText(stringConverter.toString(item));
								Label lbl = (Label) item;
								setGraphic(lbl.getGraphic());
							}
						}
					}
				};
				return listCell;
			}
		});
		FxUtil.installClipboardKeyEvent(lvResult, stringConverter);
	}

	@FXML
	public void btnEnterOnAction() {

		String prompt = txtPrompt.getText();
		lvResult.getItems().add(new DefaultLabel(prompt, new Label(" 나 ")));

		VirtualPool.newInstance().execute(() -> {
			try {
				OpenAIService openAIService = new OpenAIService();
				String send = openAIService.send(prompt);
				Platform.runLater(() -> {
					
					lvResult.getItems().add(new DefaultLabel("", new Label(openAIService.getConfig().getModel())));
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

									CodeLabel content = new CodeLabel(sb.toString(), new Label("Copy"));

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

}
