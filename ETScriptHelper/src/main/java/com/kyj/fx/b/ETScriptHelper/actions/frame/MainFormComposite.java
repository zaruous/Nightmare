/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.frame
 *	작성일   : 2021. 12. 3.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.frame;

import java.io.IOException;

import com.kyj.fx.b.ETScriptHelper.comm.FxUtil;
import com.kyj.fx.b.ETScriptHelper.comm.StageStore;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class MainFormComposite extends BorderPane {

	public MainFormComposite() {
		FXMLLoader newLaoder = FxUtil.newLaoder();
		newLaoder.setLocation(MainFormComposite.class.getResource("MainForm.fxml"));
		newLaoder.setRoot(this);
		newLaoder.setController(this);
		try {
			newLaoder.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void vbEquipmentOnClick() {
		Stage stage = StageStore.getPrimaryStage();
		ETFrameComposite compisite = new ETFrameComposite();
		StageStore.setApp(compisite);
		Scene scene = stage.getScene();
		scene.setRoot(compisite);
	}

}
