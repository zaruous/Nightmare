/**
 * 
 */
package com.kyj.fx.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.AlreadyRuningException;
import com.kyj.fx.nightmare.comm.ResourceLoader;
import com.kyj.fx.nightmare.comm.ValueUtil;

import io.javalin.Javalin;
import io.javalin.util.JavalinBindException;

/**
 * 
 */
public class JavalinStarter {

	private static final Logger LOGGER = LoggerFactory.getLogger(JavalinStarter.class);

	@Deprecated
	public static void main(String[] args) {

	}
	
	/*어플리케이션 실행 여부 체크*/
	public void start() {
		
		String string = ResourceLoader.getInstance().get("application.check.port", "10022");
		try {
			Controller getHtml = new Controller();
			var app = Javalin.create(getHtml.config())
					.get("/", ctx -> getHtml.hellWorld(ctx))
					.get("/getHtml", ctx -> getHtml.getHtml(ctx))
					.get("/status", ctx -> getHtml.status(ctx))
					.start(Integer.parseInt(string, 10));
		} catch (JavalinBindException ex) {
			LOGGER.error(ValueUtil.toString(ex));
			throw new AlreadyRuningException("어플리케이션이 이미 실행중입니다.");
		}
	}
}
