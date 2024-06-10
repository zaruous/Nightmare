/********************************
 *	프로젝트 : NashornExamples
 *	패키지   : engine
 *	작성일   : 2018. 1. 22.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.groovy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import javax.script.SimpleScriptContext;

/**
 * @author KYJ
 *
 */
public abstract class AbstractEngine {

	private ScriptEngine engine;

	public AbstractEngine() {

	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 23.
	 * @return
	 */
	public abstract ScriptEngine createEngine();

	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 2. 14. 
	 * @return
	 */
	public ScriptContext createContext() {
		return new SimpleScriptContext();
	};

	public Object execute(File location) throws Exception {
		try (InputStream openStream = new FileInputStream(location)) {
			return execute(openStream);
		}
	}

	public Object execute(URL location) throws Exception {
		try (InputStream openStream = location.openStream()) {
			return execute(openStream);
		}
	}

	public Object execute(InputStream in) throws Exception {
		return execute(toString(in));
	}

	private String toString(InputStream in) throws IOException {
		return toString(in, StandardCharsets.UTF_8);
	}

	private String toString(InputStream in, Charset charset) throws IOException {
		int temp = -1;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		while ((temp = in.read()) != -1)
			out.write(temp);
		return out.toString(charset.name());
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 10. 12.
	 * @param script
	 * @param b
	 * @return
	 * @throws IOException
	 * @throws ScriptException
	 */
	public Object execute(String script, Bindings b) throws IOException, ScriptException {
		return execute(script, b, ScriptContext.ENGINE_SCOPE);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 10. 12.
	 * @param script
	 * @param b
	 * @param scope
	 *            <br/>
	 *            100 ENGINE_SCOPE <br/>
	 *            200 GLOBAL_SCOPE <br/>
	 * @return
	 * @throws IOException
	 * @throws ScriptException
	 */
	public Object execute(String script, Bindings b, int scope) throws IOException, ScriptException {

		if (this.engine == null)
			this.engine = createEngine();

		setBindings(engine, b, scope);
		return engine.eval(script, b);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 10. 12.
	 * @param script
	 * @param scope
	 * @return
	 * @throws IOException
	 * @throws ScriptException
	 */
	public Object execute(String script, int scope) throws IOException, ScriptException {
		return execute(script, createBindings(new HashMap<>()), scope);
	}

	public Object execute(String script) throws Exception {
		return eval(script);
	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 10. 10. 
	 * @param script
	 * @return
	 * @throws IOException
	 * @throws ScriptException
	 */
	public Object eval(String script) throws Exception {
		if (this.engine == null)
			this.engine = createEngine();
		return engine.eval(script);
	}

	/***************************************************************************************************/
	// invokable
	/***************************************************************************************************/

	public Invocable invocable(InputStream in) throws IOException, ScriptException {
		return invocable(toString(in));
	}

	public Invocable invocable(String script) throws IOException, ScriptException {
		if (this.engine == null)
			this.engine = createEngine();
		engine.eval(script);
		return (Invocable) engine;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 2. 20.
	 * @param m
	 * @return
	 */
	public Bindings createBindings(Map<String, Object> m) {
		return new SimpleBindings(m);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 10. 12.
	 * @param createBindings
	 * @param scope
	 *            refer ScriptContext
	 */
	public void setBindings(ScriptEngine engine, Bindings bindings, int scope) {
		engine.setBindings(bindings, scope);
	}

}
