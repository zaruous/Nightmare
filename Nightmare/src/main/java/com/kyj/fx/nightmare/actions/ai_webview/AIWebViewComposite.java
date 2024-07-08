/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai_webview;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.fxloader.FXMLController;
import com.kyj.fx.nightmare.actions.ai.OpenAIService;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.ui.frame.AbstractCommonsApp;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.WritableImage;

/**
 * 
 */
@FXMLController(value = "AIWebView.fxml", isSelfController = true)
public class AIWebViewComposite extends AbstractCommonsApp {
	private static final Logger LOGGER = LoggerFactory.getLogger(AIWebViewComposite.class);
	@FXML
	private TabPane tbWebView;
	private ObjectProperty<OpenAIService> openAIService = new SimpleObjectProperty<>();

	public AIWebViewComposite() {
		try {
			FxUtil.loadRoot(AIWebViewComposite.class, this);
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}

	@FXML
	public void miScreenshotOnAction() {

		Tab selectedItem = this.tbWebView.getSelectionModel().getSelectedItem();
		DefaultWebViewComposite content = (DefaultWebViewComposite) selectedItem.getContent();

		SnapshotParameters snapshotParameters = new SnapshotParameters();
		snapshotParameters.setDepthBuffer(false);

		WritableImage snapshot = content.getWebView().snapshot(snapshotParameters, null);
		BufferedImage fromFXImage = SwingFXUtils.fromFXImage(snapshot, null);
		File outputFile = new File("output.png");
		try {
			ImageIO.write(fromFXImage, "png", outputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void initialize() {
		try {
			this.openAIService.set(new OpenAIService());
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}

	Tab createNewTab(String title, Consumer<DefaultWebViewComposite> handler) {
		DefaultWebViewComposite value = new DefaultWebViewComposite();
		Tab e = new Tab(title, value);
		value.openAIServiceProperty().bind(openAIService);
//		value.setAiService(openAIService.get());
		value.setParentComposite(this);
		value.setCurrentTab(e);
		if (handler != null) {
			handler.accept(value);
		}
		return e;
	}

	public Tab addNewTabView(String location) {
		Tab newTab = createNewTab("새 탭", c ->{
			c.setLocation(location);
		});
		newTab.setText("새 탭");
		tbWebView.getTabs().add(newTab);
		tbWebView.getSelectionModel().selectLast();
		return newTab;
	}

	/**
	 * @param handle
	 * @return
	 */
	public DefaultWebViewComposite getActive(Consumer<DefaultWebViewComposite> handle) {
		Tab selectedItem = this.tbWebView.getSelectionModel().getSelectedItem();
		if (selectedItem != null)
			handle.accept((DefaultWebViewComposite) selectedItem.getContent());
		return (DefaultWebViewComposite) selectedItem.getContent();
	}
	
	/**
	 * + 버튼 클릭시 신규 탭 추가.
	 */
	@FXML
	public void miNewTabOnSelectionChange() {
		addNewTabView("about:blank");
	}
}
