package com.kyj.fx.nightmare;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.comm.Message;
import com.kyj.fx.nightmare.comm.ResourceLoader;
import com.kyj.fx.nightmare.comm.StageStore;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.comm.initializer.HostNameVertifierInitializer;
import com.kyj.fx.nightmare.comm.initializer.HttpURLInitializer;
import com.kyj.fx.nightmare.comm.initializer.ProxyInitializable;
import com.kyj.fx.nightmare.comm.initializer.SSLInitializable;
import com.kyj.fx.nightmare.ui.frame.MainFormComposite;
import com.kyj.fx.websocket.JavalinStarter;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainApp extends Application implements UncaughtExceptionHandler {

	public static final String IMAGES_NIGHTMARE_PNG = "images/Nightmare.png";
	private static final Logger LOGGER = LoggerFactory.getLogger(MainApp.class);
	
	public static void main(String[] args) throws Exception {
		
		String strLocale = ResourceLoader.getInstance().get(ResourceLoader.DEFAULT_LOCALE);
		Locale locale = Locale.KOREAN;
		if (!ValueUtil.isEmpty(strLocale)) {
			locale = Locale.of(strLocale);
		}
		LOGGER.debug("Default Loacale : {} " , locale);
		Locale.setDefault(locale);
		
		String file_encoding = ResourceLoader.getInstance().get(ResourceLoader.FILE_ENCODING, "UTF-8");
		String jnu_encoding = ResourceLoader.getInstance().get(ResourceLoader.SUN_JNU_ENCODING, "UTF-8");
		System.setProperty(ResourceLoader.FILE_ENCODING, file_encoding);
		System.setProperty(ResourceLoader.SUN_JNU_ENCODING, jnu_encoding);
		
		LOGGER.debug("Default file encoding : {} " , file_encoding);
		LOGGER.debug("Default jnu encoding : {} " , jnu_encoding);
		
		
		if("Y".equals(ResourceLoader.getInstance().get(ResourceLoader.SSL_VERIFY, "Y"))) {
			new SSLInitializable().initialize();
			new HttpURLInitializer().initialize();
			new HostNameVertifierInitializer().initialize();	
		}
		new ProxyInitializable().initialize();
		
		launch(args);
	}
	
	private JavalinStarter javalinStarter;
	public void start(Stage stage) throws Exception {
		
		
		//socker port binding 으로 어플리케이션 실행여부 확인
		if("N".equals(ResourceLoader.getInstance().get("multi.application.support.yn", "N"))) {
			javalinStarter = new JavalinStarter();
			javalinStarter.start();
			
		}
		
		//어플리케이션을 닫을때 트레이아이콘 처리할지 여부
		if("Y".equals(ResourceLoader.getInstance().get(ResourceLoader.TRAY_SUPPORT_YN, "Y")))
			TraySupport.addAppToTray(stage);
		else
		{
			//트레이아이콘 모드가 아닌경우 앱 종료
			stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, ev->{
				/*
				 * 자벨린 종료시 이후 프로세스는 
				 * 현재 개발 상태로는 종료됨 자벨린이 처리하는 데이터 처리가 끝난 후 종료되도록 고려차원에서 시스템 강제종료 처리 부분은 주석처리.
 				강제 종료가 필요한 상황이 될 경우 아래 두 종료코드를 고려해볼것.
 				Platform.exit();
//				System.exit(0);
				*/ 
				javalinStarter.stop();

			});
		}
		StageStore.setPrimaryStage(stage);

		stage.addEventFilter(KeyEvent.KEY_RELEASED, ev -> {
			if (!ev.isConsumed() && !ev.isAltDown() && !ev.isShiftDown() && ev.getCode() == KeyCode.F11) {
				if (ev.isConsumed())
					return;
				ev.consume();

				stage.setFullScreen(!stage.isFullScreen());
			}
			else if (!ev.isConsumed() && ev.isAltDown() && !ev.isShiftDown() && ev.getCode() == KeyCode.HOME) {
				if (ev.isConsumed())
					return;
				ev.consume();
				updateComponent(StageStore.getPrimaryStage());
			}
		});
		
		//예상치못한 예외 상황에 대한 이벤트 핸들링.
		Thread.setDefaultUncaughtExceptionHandler(this);

		updateComponent(stage);

		
		String w = ResourceLoader.getInstance().get("app.width");
		String h = ResourceLoader.getInstance().get("app.height");
		int width = 1200;
		int height = 800;
		try {width = Integer.parseInt(w, 10);}catch(NumberFormatException ex) {/*Nothing*/}
		try {height = Integer.parseInt(h, 10);}catch(NumberFormatException ex) {/*Nothing*/}
		
		stage.setWidth(width);
		stage.setHeight(height);
		
		String appVersion = ResourceLoader.getInstance().get(ResourceLoader.APP_VERSION);
		String appTitle = Message.getInstance().getMessage("%MainApp_000001",  appVersion);
		LOGGER.debug("appTitle : {}  :  + appVersion : {} ", appTitle , appVersion);
		stage.setTitle(appTitle);
		stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream(IMAGES_NIGHTMARE_PNG)));
		
		stage.show();
		
	}

	
	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 12. 3.
	 * @param primaryStage
	 */
	public static void updateComponent(Stage primaryStage) {
		// ETFrameComposite etFrameComposite = new ETFrameComposite();
		// StageStore.setApp(etFrameComposite);

		MainFormComposite form = new MainFormComposite();
		Scene scene = new Scene(form);
		
		scene.getStylesheets().add(MainApp.class.getResource("styles.css").toExternalForm());

		primaryStage.setScene(scene);
		// primaryStage.setFullScreen(true);
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		LOGGER.error(ValueUtil.toString(t.getName(), e));
	}
}
