/**
 * 
 */
package com.kyj.fx.nightmare.actions.comm.ai;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 
 */
public class PythonHelper {
	
	String fontPath = new File("fonts/NANUMBARUNGOTHIC.TTF").getAbsolutePath();
	
	String pre = """
			from matplotlib import font_manager, rc, rcParams

			
			font_path = '%s'
			font_manager.fontManager.addfont(font_path)
			font = font_manager.FontProperties(fname=font_path).get_name()
			rc('font', family=font)
			""";
	private static final PythonHelper INSTANCE = new PythonHelper();
	public PythonHelper() {}
	
	
	public static void exec(String codeType, String code) {
		INSTANCE.pythonRun(codeType, code , PyCodeBuilder.DEFAULT_OUTPUT_STREAM);
	}
	public static void exec(String codeType, String code, OutputStream out) {
		INSTANCE.pythonRun(codeType, code , out);
	}
	public void pythonRun(String codeType, String code, OutputStream out) {
		try {
			PyCodeBuilder pyCodeBuilder = new PyCodeBuilder();
			pyCodeBuilder.codeType(codeType);
			pre = String.format(pre, fontPath.replace('\\', '/'));
			pyCodeBuilder.setOutputStream(out);
			pyCodeBuilder.setErrorStream(out);
			pyCodeBuilder.code(pre + code);
			pyCodeBuilder.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
