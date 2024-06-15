/********************************
 *	프로젝트 : Nightmare
 *	패키지   : com.kyj.fx.nightmare.actions.ai.voice
 *	작성일   : 2024. 6. 15.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.ai_voice;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.fxloader.FXMLController;
import com.kyj.fx.nightmare.actions.ai.AiComposite;
import com.kyj.fx.nightmare.actions.ai.SpeechResponseModelDVO;
import com.kyj.fx.nightmare.actions.ai.SpeechToTextGptServiceImpl;
import com.kyj.fx.nightmare.comm.DialogUtil;
import com.kyj.fx.nightmare.comm.ExecutorDemons;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.PlayableFileChooserHandler;
import com.kyj.fx.nightmare.comm.StageStore;
import com.kyj.fx.nightmare.comm.ValueUtil;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

/**
 * 
 * 음성파일을 텍스트로 변환 용량제한 주의.
 * 
 * @author KYJ (callakrsos@naver.com)
 *
 */
@FXMLController(value = "SimpeAIVoiceConversionView.fxml", isSelfController = true)
public class SimpleAIVoiceConversionComposite extends BorderPane {
	private static final Logger LOGGER = LoggerFactory.getLogger(AiComposite.class);
	@FXML
	private TextField txtLocation;
	@FXML
	private TextArea txtContent;
	private ObjectProperty<File> file = new SimpleObjectProperty<File>();
	private SpeechToTextGptServiceImpl service;

	public SimpleAIVoiceConversionComposite() {

		try {
			FxUtil.loadRoot(SimpleAIVoiceConversionComposite.class, this);
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}

	@FXML
	public void initialize() {
		this.file.addListener(new ChangeListener<File>() {

			@Override
			public void changed(ObservableValue<? extends File> observable, File oldValue, File newValue) {
				if (newValue == null || !newValue.exists())
					return;

				txtLocation.setOpacity(0.5d);
				txtLocation.setText(newValue.getAbsolutePath());
				ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(() -> {
					try {
						SpeechResponseModelDVO send = service.send(newValue);
						Platform.runLater(() -> {
							txtContent.setText(send.getText());
							txtLocation.setOpacity(1.0d);
							// file.set(null);
						});
					} catch (Exception e) {
						LOGGER.error(ValueUtil.toString(e));
					}
				});

			}
		});
		try {
			service = new SpeechToTextGptServiceImpl();
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}

	@FXML
	public void btnFileOnAction() {
		File showFileDialog = DialogUtil.showFileDialog(StageStore.getPrimaryStage(), new PlayableFileChooserHandler());
		file.set(showFileDialog);
	}

	@FXML
	public void txtLocationOnKeyRelease(KeyEvent ke) {
		if (ke.getCode() == KeyCode.ENTER) {
			file.set(new File(txtLocation.getText()));
		}
	}
}
