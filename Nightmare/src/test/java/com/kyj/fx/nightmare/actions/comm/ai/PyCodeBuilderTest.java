/**
 * 
 */
package com.kyj.fx.nightmare.actions.comm.ai;

import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 * 
 */
class PyCodeBuilderTest {

	/**
	 * Test method for {@link com.kyj.fx.nightmare.actions.comm.ai.PyCodeBuilder#run()}.
	 * @throws IOException 
	 */
	@Test
	void testRun() throws IOException {
		PyCodeBuilder pyCodeBuilder = new PyCodeBuilder();
		pyCodeBuilder.code("print('hello world!')");
		pyCodeBuilder.run();
	}
	
	@Test
	void testFile() throws IOException {
		PyCodeBuilder pyCodeBuilder = new PyCodeBuilder();
		pyCodeBuilder.file("TestPy.py");
		pyCodeBuilder.run();
	}
	
	
}
