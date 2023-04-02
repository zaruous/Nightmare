/********************************
 *	프로젝트 : Nightmare
 *	패키지   : com.kyj.fx.nightmare.comm.engine
 *	작성일   : 2023. 4. 1.
 *	작성자   : (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.comm.engine;

import javax.script.ScriptException;

import org.junit.jupiter.api.Test;

/**
 * @author (zaruous@naver.com)
 *
 */
public class GroovyScriptEngineTest {

	/**
	 * Test method for
	 * {@link com.kyj.fx.nightmare.comm.engine.GroovyScriptEngine#eval(java.lang.String)}.
	 * 
	 * @throws ScriptException
	 */
	@Test
	public void testEvalString() throws ScriptException {
		GroovyScriptEngine engine = new GroovyScriptEngine();
		engine.eval("  println hello() ; ");

	}

}
