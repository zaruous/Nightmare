/**
 * 
 */
package com.kyj.fx.websocket;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.actions.ai_webview.AIWebViewComposite;

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
		Platform.runLater(()->{
			Stage.getWindows().stream().filter(v -> v instanceof Stage).map(v -> ((Stage) v)).filter(v -> {
				return v.getScene().getRoot().getClass() == AIWebViewComposite.class;
			}).map(v -> (AIWebViewComposite) v.getScene().getRoot()).forEach(v -> {
				v.getActive(a -> {
					DataBody fromJsonString = ctx.jsonMapper().fromJsonString(ctx.body(), DataBody.class);
					LOGGER.debug("{}", fromJsonString);
					a.setLocation(fromJsonString.getLocation());
				});
			});	
		});
		
	}

	public Consumer<JavalinConfig> config() {
		return config -> {
			config.useVirtualThreads = true;
		};
	}

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
}
