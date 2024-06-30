package com.kyj.fx.groovy;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.codehaus.groovy.jsr223.GroovyScriptEngineFactory;

/********************************
 *  ###################################    
 *  수정일 : 2019.02.15
 *  기능 : 
 *    Groovy Script Engine
 *******************************/
/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class DefaultScriptEngine extends AbstractEngine {

	private URL[] args;
	private Writer writer;
	private Writer errorWriter;
	private Reader reader;
	private HashMap<String, Object> userBinding = new HashMap<String, Object>();

	public DefaultScriptEngine() {
		super();
	}

	public DefaultScriptEngine(URL... roots) {
		super();
//		this.args = args;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kyj.fx.groovy.AbstractEngine#createEngine()
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kyj.fx.groovy.AbstractEngine#createEngine()
	 */
	@Override
	public ScriptEngine createEngine() {

		// GroovyScriptEngine engine = new GroovyScriptEngine(this.args);
		// GroovyScriptEngineWrapper wrapper = new
		// GroovyScriptEngineWrapper(this.args);
		// CompilerConfiguration config = engine.getConfig();
		// config.
		// config.setOutput(output);

		ScriptEngine engineByName = null;
		if (this.args == null) {
			ScriptEngineManager factory = new ScriptEngineManager();
			engineByName = factory.getEngineByName("groovy");

			engineByName.setContext(createContext());
			if (writer != null)
				engineByName.getContext().setWriter(writer);
			if (errorWriter != null)
				engineByName.getContext().setErrorWriter(errorWriter);
			if (reader != null)
				engineByName.getContext().setReader(reader);
		}

		else {
			GroovyScriptEngineFactory factory = new GroovyScriptEngineFactory();
			engineByName = factory.getScriptEngine();
			// engineByName = factory.getScriptEngine(args);
			engineByName.setContext(createContext());
			if (writer != null)
				engineByName.getContext().setWriter(writer);
			if (errorWriter != null)
				engineByName.getContext().setErrorWriter(errorWriter);
			if (reader != null)
				engineByName.getContext().setReader(reader);
		}

		Bindings createBindings = engineByName.createBindings();
		// binding.setProperty(property, newValue);
//		engineByName.put("BASEDIR", new File(new File("").getAbsolutePath(), "script/nashorn").getAbsolutePath());
//		engineByName.put("$BASEDIR", new File(new File("").getAbsolutePath(), "script/nashorn").getAbsolutePath());
		createBindings.put("BASEDIR", new File(new File("").getAbsolutePath(), "groovy"));
		// 2019.10.21 ENGINE에 대한 공용 변수 추가.
		createBindings.put("ENGINE", engineByName);
		// 2019.11.28 addAll userBinding.
		createBindings.putAll(userBinding);
//		createBindings.put("BASEDIR", new File(new File("").getAbsolutePath(), "script/nashorn").getAbsolutePath());
		setBindings(engineByName, createBindings, ScriptContext.ENGINE_SCOPE);
		return engineByName;
	}

	// @Override
	// public ScriptContext createContext() {
	// return null;
	// }

	/**
	 * @return the writer
	 */
	public Writer getWriter() {
		return writer;
	}

	/**
	 * @param writer the writer to set
	 */
	public void setWriter(Writer writer) {
		this.writer = writer;
	}

	/**
	 * @return the errorWriter
	 */
	public Writer getErrorWriter() {
		return errorWriter;
	}

	/**
	 * @param errorWriter the errorWriter to set
	 */
	public void setErrorWriter(Writer errorWriter) {
		this.errorWriter = errorWriter;
	}

	/**
	 * @return the reader
	 */
	public Reader getReader() {
		return reader;
	}

	/**
	 * @param reader the reader to set
	 */
	public void setReader(Reader reader) {
		this.reader = reader;
	}

	public HashMap<String, Object> getUserBinding() {
		return userBinding;
	}

	public void setUserBinding(HashMap<String, Object> userBinding) {
		this.userBinding = userBinding;
	}

}
