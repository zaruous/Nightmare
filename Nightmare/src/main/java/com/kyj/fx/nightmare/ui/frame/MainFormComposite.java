/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.frame
 *	작성일   : 2021. 12. 3.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.ui.frame;

import java.io.IOException;

import com.kyj.fx.nightmare.actions.ai.AiComposite;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.StageStore;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author KYJ (zaruous@naver.com)
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

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 4. 1.
	 */
	@FXML
	public void vbEquipmentOnClick() {
		launch(new ETFrameComposite());
	}

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 4. 1.
	 */
	@FXML
	public void utilsOnClick() {
		launch(new UtilFrameComposite());
	}

	@FXML
	public void groovyOnClick() {
		launch(new GroovyFrameComposite());
	}

	@FXML
	public void notebookOnClick(){
		launch(new NotebookComposite());
	}
	
	@FXML
	public void aiOnClick(){
		try {
			launch(new AiComposite());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param compisite
	 */
	protected void launch(AbstractCommonsApp compisite) {
		Stage stage = StageStore.getPrimaryStage();
		StageStore.setApp(compisite);
		Scene scene = stage.getScene();
		scene.setRoot(compisite);
	}
}
