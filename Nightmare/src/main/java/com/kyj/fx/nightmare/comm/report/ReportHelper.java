/**
 * 
 */
package com.kyj.fx.nightmare.comm.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.kyj.fx.nightmare.actions.ai.DefaultLabel;
import com.kyj.fx.nightmare.actions.grid.GridHtmlReporter;
import com.kyj.fx.nightmare.comm.DialogUtil;

/**
 * 
 */
public class ReportHelper {

	public static void generate(List<DefaultLabel> items, File outFile) {
		try (FileOutputStream out = new FileOutputStream(outFile)) {
			generate(items, out);
		} catch (IOException e) {
			DialogUtil.showExceptionDailog(e);
		}
	}

	public static void generate(List<DefaultLabel> items, OutputStream outStrean) throws IOException {
		new GridHtmlReporter().generate(items, outStrean);
	}
}
