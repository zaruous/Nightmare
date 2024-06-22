/**
 * 
 */
package com.kyj.fx.websocket;

import java.util.function.Consumer;

import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;

/**
 * 
 */
public class Controller {

	public Controller() {

	}

	public void handle() {

	}

	/**
	 * @param ctx
	 * @return
	 */
	public void getHtml(Context ctx) {
		ctx.result("Hello World");
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
