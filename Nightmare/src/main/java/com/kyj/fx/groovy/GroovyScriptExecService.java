/********************************
 *	프로젝트 : gargoyle-nashorn
 *	패키지   : com.kyj.fx.nashorn
 *	작성일   : 2018. 10. 11.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.groovy;

import javax.script.Bindings;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class GroovyScriptExecService<T> {

	private GroovyScriptDataSet scriptSet;
	private AbstractEngine engine;

	public GroovyScriptExecService() {
	}

	/**
	 * @param scriptSet
	 */
	public GroovyScriptExecService(GroovyScriptDataSet scriptSet) {
		this.scriptSet = scriptSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kyj.fx.rax.plugins.AbstractRaxPlguin#load()
	 */
	public void load() {
		engine = new DefaultScriptEngine();
	}

	/**
	 * @return the scriptSet
	 */
	public GroovyScriptDataSet getScriptSet() {
		return scriptSet;
	}

	/**
	 * @param scriptSet
	 *            the scriptSet to set
	 */
	public void setScriptSet(GroovyScriptDataSet scriptSet) {
		this.scriptSet = scriptSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kyj.fx.rax.plugins.AbstractRaxPlguin#execute()
	 */

	/**
	 * @return the engine
	 */
	public final AbstractEngine getEngine() {
		return engine;
	}

	public T execute() throws Exception {
		Bindings b = engine.createBindings(this.scriptSet.getParams());
		Object r = engine.execute(this.scriptSet.getScript(), b);
		return (T) r;
	}

	public String getName() {
		return this.scriptSet.getName();
	}

}
