/********************************
 *	프로젝트 : gargoyle-nashorn
 *	패키지   : com.kyj.fx.nashorn
 *	작성일   : 2018. 10. 11.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.groovy;

import java.util.HashMap;
import java.util.Map;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class GroovyScriptDataSet {

	private String name;
	private String executeFunction;
	private String script;
	private Map<String, Object> params = new HashMap<String, Object>();

	/**
	 * @return the executeFunction
	 */
	public String getExecuteFunction() {
		return executeFunction;
	}

	/**
	 * @param executeFunction
	 *            the executeFunction to set
	 */
	public void setExecuteFunction(String executeFunction) {
		this.executeFunction = executeFunction;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the script
	 */
	public String getScript() {
		return script;
	}

	/**
	 * @param script
	 *            the script to set
	 */
	public void setScript(String script) {
		this.script = script;
	}

	/**
	 * @return the params
	 */
	public Map<String, Object> getParams() {
		return params;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

}
