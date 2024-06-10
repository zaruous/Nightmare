/********************************
 *	프로젝트 : NashornExamples
 *	패키지   : engine
 *	작성일   : 2018. 1. 22.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.groovy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * 
 * 
 * $STAGE 파라미터를 받고 실행 처리 제거할것.
 * 
 * 
 * later, this code should be located on the server.
 * 
 * @author KYJ
 *
 */

public class FxScriptEngine extends DefaultScriptEngine {
	private static final Logger LOGGER = LoggerFactory.getLogger(FxScriptEngine.class);
	private DefaultScriptEngine engine;

	private ObjectProperty<EventHandler<WindowEvent>> closeEventRequest = new SimpleObjectProperty<>();

	public FxScriptEngine() {
		super();

	}

	
	// public FxScriptEngine(String... args) {
	// super(args);
	// }

	@Override
	public ScriptEngine createEngine() {
		ScriptEngine engine = super.createEngine();
		Bindings createBindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
		Stage value = new Stage();
		createBindings.put("$STAGE", value);
		
		engine.setBindings(createBindings, ScriptContext.ENGINE_SCOPE);
		value.setOnCloseRequest(this::stageOnCloseRequest);
		return engine;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 23.
	 * @param ev
	 */
	private void stageOnCloseRequest(WindowEvent ev) {
		EventHandler<WindowEvent> eventHandler = closeEventRequest.get();
		if (eventHandler != null) {
			eventHandler.handle(ev);
		}
	}

	// public Object execute() throws IOException, ScriptException {
	// String externalForm = this.url.toExternalForm();
	// LOGGER.debug("Script Location : {} ", externalForm);
	// NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
	//
	// ScriptEngine engineByName = factory.getScriptEngine(new String[] { "-fx",
	// externalForm });
	// return engineByName.eval("");
	// }

	@Override
	public Object execute(File location) throws IOException, ScriptException {
		LOGGER.debug("Script Location : {} ", location.getAbsolutePath());

		StringBuffer sb = new StringBuffer();
		try (BufferedReader br = new BufferedReader(new FileReader(location))) {
			String temp = null;
			while ((temp = br.readLine()) != null) {
				sb.append(temp);
			}
		}
		return execute(sb.toString());
	}

	@Override
	public Object execute(URL location) throws IOException, ScriptException {
		return null;
	}

	@Override
	public Object execute(InputStream in) throws IOException, ScriptException {
		return null;
	}

	@Override
	public Object execute(String script) throws IOException, ScriptException {
		ScriptEngine engine = createEngine();
		engine.eval(script);
		return null;
	}

	@Override
	public Invocable invocable(InputStream in) throws IOException, ScriptException {
		StringBuffer sb = new StringBuffer();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
			String temp = null;
			while ((temp = br.readLine()) != null) {
				sb.append(temp);
			}
		}
		return this.invocable(sb.toString());
	}

	@Override
	public Invocable invocable(String script) throws IOException, ScriptException {
		ScriptEngine engine = createEngine();
		return (Invocable) engine.eval(script);
	}

	public final ObjectProperty<EventHandler<WindowEvent>> closeEventRequestProperty() {
		return this.closeEventRequest;
	}

	public final EventHandler<WindowEvent> getCloseEventRequest() {
		return this.closeEventRequestProperty().get();
	}

	public final void setCloseEventRequest(final EventHandler<WindowEvent> closeEventRequest) {
		this.closeEventRequestProperty().set(closeEventRequest);
	}

}
