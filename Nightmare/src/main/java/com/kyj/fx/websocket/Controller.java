/**
 * 
 */
package com.kyj.fx.websocket;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;

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
		Object fromJsonString = ctx.jsonMapper().fromJsonString( ctx.body(), DataBody.class);
		LOGGER.debug("{}", fromJsonString);
		ctx.result("1");
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
