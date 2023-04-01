/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.frame
 *	작성일   : 2023. 4. 1.
 *	작성자   : (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.frame;

import com.kyj.fx.nightmare.MainApp;
import com.kyj.fx.nightmare.comm.DialogUtil;
import com.kyj.fx.nightmare.comm.ResourceLoader;
import com.kyj.fx.nightmare.comm.StageStore;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author (zaruous@naver.com)
 *
 */
public abstract class AbstractCommonsApp extends BorderPane {

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 4. 1.
	 * @param message
	 */
	public void showStatusMessage(String message) {
		StageStore.getApp().showStatusMessage(message);
	}

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 4. 1.
	 */
	@FXML
	public void miHomeOnAction() {
		Stage stage = StageStore.getPrimaryStage();
		MainApp.updateComponent(stage);
	}

	@FXML
	public void miVersionOnAction() {
		String appVersion = ResourceLoader.getInstance().get(ResourceLoader.APP_VERSION);
		String msg = "app version " + appVersion;
		DialogUtil.showMessageDialog(msg);
	}
}
