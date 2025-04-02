/**
 * 
 */
package com.kyj.fx.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.AlreadyRuningException;
import com.kyj.fx.nightmare.comm.ResourceLoader;
import com.kyj.fx.nightmare.comm.StageStore;
import com.kyj.fx.nightmare.comm.ValueUtil;

import io.javalin.Javalin;
import io.javalin.util.JavalinBindException;
import javafx.stage.WindowEvent;

/**
 * 
 */
public class JavalinStarter {

	private static final Logger LOGGER = LoggerFactory.getLogger(JavalinStarter.class);

	@Deprecated
	public static void main(String[] args) {

	}
	
	private Javalin app;
	/*어플리케이션 실행 여부 체크*/
	public void start() {
		
		String string = ResourceLoader.getInstance().get("application.check.port", "10022");
		try {
			
			app = Javalin.create(config ->{
				 config.http.asyncTimeout = 10_000L;
				 config.http.maxRequestSize = 5_000_000L;
				 config.useVirtualThreads = true;
				 config.showJavalinBanner = false;
			})
			.start(Integer.parseInt(string, 10));
			
			Controller getHtml = new Controller();
			app.get("/", ctx -> getHtml.hellWorld(ctx))
			.post("/getHtml", ctx -> getHtml.getHtml(ctx))
			.get("/status", ctx -> getHtml.status(ctx))
			.get("/ping", ctx -> getHtml.ping(ctx));
			
		} catch (JavalinBindException ex) {
			LOGGER.error(ValueUtil.toString(ex));
			throw new AlreadyRuningException("어플리케이션이 이미 실행중입니다.");
		}
	}
	
	public void stop() {
		app.stop();
	}
}
