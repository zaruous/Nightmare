/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai_webview;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.kyj.fx.fxloader.FXMLController;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.ValueUtil;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;

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

	public AIWebViewComposite() {
		try {
			FxUtil.loadRoot(AIWebViewComposite.class, this);
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}

	@FXML
	public void initialize() {
		wbDefault.getEngine().setOnError(ev -> {
			LOGGER.error(ValueUtil.toString(ev.getException()));
		});
		wbDefault.getEngine().load("https://news.google.com/home?hl=ko&gl=KR&ceid=KR:ko");
		wbDefault.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {

			@Override
			public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
				if (State.SUCCEEDED == newValue) {
					// JavaScript를 사용하여 <script> 태그를 제거한 body 콘텐츠를 가져오기
					String bodyContentWithoutScripts = (String) wbDefault.getEngine()
							.executeScript("document.body.innerText");

					// HTML 출력
					System.out.println(bodyContentWithoutScripts);
				}

			}
		});

	}
}
