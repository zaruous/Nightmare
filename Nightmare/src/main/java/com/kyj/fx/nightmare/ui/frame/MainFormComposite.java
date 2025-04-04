/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.frame
 *	작성일   : 2021. 12. 3.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.ui.frame;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import com.kyj.fx.nightmare.actions.ai.AiComposite;
import com.kyj.fx.nightmare.actions.grid.DefaultSpreadComposite;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.ResourceLoader;
import com.kyj.fx.nightmare.comm.StageStore;
import com.kyj.fx.nightmare.comm.ValueUtil;

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
//		newLaoder.setLocation(MainFormComposite.class.getResource("MainForm2.fxml"));
		newLaoder.setRoot(this);
		newLaoder.setController(this);
		
        this.setStyle(
        	   """
        		-fx-background-image: url("/images/Nightmare.png");
        		-fx-background-size: stretch ; /* 이미지를 컨테이너 크기에 맞춥니다 */
			    -fx-background-position: center; /* 이미지를 중앙에 위치시킵니다 */
			    -fx-background-repeat: no-repeat; /* 이미지를 반복하지 않습니다 */ 
			    -fx-background-radius: 5.0;
			    -fx-opacity: 0.9;
			    -fx-cursor: hand ;
			    
        		"""
        	);
		try {
			newLaoder.load();
			this.getStylesheets().add(MainFormComposite.class.getResource("MainForm.css").toExternalForm());
//			ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(()->{
//				
//				Timer timer = new Timer();
//				timer.schedule(new TimerTask() {
//					
//					@Override
//					public void run() {
//						AbstractCommonsApp app = StageStore.getApp();
//						if(app!=null && (app.getClass() == AiComposite.class))
//							return;
//						Platform.runLater(()->{
//							aiOnClick();	
//						});
//					}
//				}, 2000);
//			});
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
	
	@FXML
	public void spreadOnClick(){
		try {
			launch(new DefaultSpreadComposite());
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
		//Style.
		try {
			String skinFileName = ResourceLoader.getInstance().get(ResourceLoader.DEFAULT_SKIN, null);
			if(ValueUtil.isNotEmpty(skinFileName))
			{
				File file = new File("styles/application_simple_wine.css");
				if(file.exists())
					scene.getStylesheets().add(file.toURI().toURL().toExternalForm());	
			}
		} catch (MalformedURLException e) {/*Nothing*/ }
		
		scene.setRoot(compisite);
	}
}
