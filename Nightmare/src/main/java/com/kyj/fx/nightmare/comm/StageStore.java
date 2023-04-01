/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.frame
 *	작성일   : 2021. 11. 22.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.nightmare.comm;

import com.kyj.fx.nightmare.actions.frame.AbstractCommonsApp;
import com.kyj.fx.nightmare.actions.frame.ETFrameComposite;

import javafx.stage.Stage;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class StageStore {

	private static AbstractCommonsApp app;
	private StageStore() {

	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 23. 
	 * @param composite
	 */
	public static void setApp(AbstractCommonsApp composite) {
		app = composite;
	}
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 23. 
	 * @return
	 */
	public static AbstractCommonsApp getApp() {
		return app;
	}
	/**
	 * 메인스테이지
	 *
	 * @최초생성일 2015. 11. 27.
	 */
	private static Stage primaryStage;

	public final static Stage getPrimaryStage() {
		return primaryStage;
	}

	public static synchronized void setPrimaryStage(Stage primaryStage) {
		StageStore.primaryStage = primaryStage;
	}

}
