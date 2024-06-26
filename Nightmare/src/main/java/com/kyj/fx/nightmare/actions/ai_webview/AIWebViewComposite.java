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
	private OpenAIService openAIService;

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
			this.openAIService = new OpenAIService();
		} catch (Exception e) {
			e.printStackTrace();
		}
		tbWebView.getTabs().add(createNewTab("default", null));
	}

	Tab createNewTab(String title, Consumer<DefaultWebViewComposite> handler) {
		Tab e = new Tab(title);
		DefaultWebViewComposite value = new DefaultWebViewComposite();
		value.setAiService(openAIService);
		value.setParentComposite(this);
		value.setCurrentTab(e);
		e.setContent(value);
		if (handler != null) {
			handler.accept(value);
		}
		return e;
	}

	public Tab addNewTabView(String location) {
		Tab newTab = createNewTab("", c ->{
			c.setLocation(location);
		});
		tbWebView.getTabs().add(newTab);
		tbWebView.getSelectionModel().selectLast();
		return newTab;
	}

	public DefaultWebViewComposite getActive(Consumer<DefaultWebViewComposite> handle) {
		Tab selectedItem = this.tbWebView.getSelectionModel().getSelectedItem();
		if (selectedItem != null)
			handle.accept((DefaultWebViewComposite) selectedItem.getContent());
		return (DefaultWebViewComposite) selectedItem.getContent();
	}
}
