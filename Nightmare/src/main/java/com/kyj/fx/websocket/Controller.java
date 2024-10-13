/**
 * 
 */
package com.kyj.fx.websocket;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.actions.ai_webview.AIWebViewComposite;
import com.kyj.fx.nightmare.comm.ValueUtil;

import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * 
 */
public class Controller {
	private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

	public Controller() {

	}

	public void handle() {

	}

	/**
	 * @param ctx
	 * @return
	 */
	public void getHtml(Context ctx) {
		ctx.result("1");
		String body = ctx.body();
		DataBody fromJsonString = ctx.jsonMapper().fromJsonString(body, DataBody.class);
		
		Platform.runLater(()->{
			Stage.getWindows().stream().filter(v -> v instanceof Stage).map(v -> ((Stage) v)).filter(v -> {
				return v.getScene().getRoot().getClass() == AIWebViewComposite.class;
			}).map(v -> (AIWebViewComposite) v.getScene().getRoot()).forEach(v -> {
				v.getActive(a -> {
					try {
						LOGGER.debug("{}", fromJsonString.getLocation());
						a.setLocation(fromJsonString.getLocation());
//						a.setHtml(fromJsonString.getHtml());
					}catch(Exception ex) {
//						LOGGER.info("{}", body);
						LOGGER.error(ValueUtil.toString(ex));
					}
				});
			});	
		});
		ctx.result("ok");
	}

//	public Consumer<JavalinConfig> config() {
//		return config -> {
//			
//		};
//	}

	/**
	 * @param ctx
	 * @return
	 */
	public void hellWorld(Context ctx) {
		ctx.result("Hello World");
	}

	public void status(Context ctx) {
		ctx.result("1");
	}
	
	public void ping(Context ctx) { ctx.status(200); }
}
