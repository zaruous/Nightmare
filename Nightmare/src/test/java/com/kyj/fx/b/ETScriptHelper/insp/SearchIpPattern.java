/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper
 *	작성일   : 2023. 4. 1.
 *	작성자   : (zaruous@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.insp;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kyj.fx.b.ETScriptHelper.comm.FileUtil;

/**
 * @author (zaruous@naver.com)
 *
 */
public class SearchIpPattern {

	String[] patterns = { "\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b" };

	@Test
	public void test() throws IOException {

		for (String pattern : patterns) {
			Pattern regex = Pattern.compile(pattern);
			var rootFile = new File("");
			// File[] listFiles = rootFile.listFiles();
			List<File> recursive = FileUtil.recursive(rootFile, dir -> {
				return dir.getFileName().toString().equals("target");
			}, path -> {

				try {
					File file = path.toFile();
					// String name = file.getName();
					String readToString = FileUtil.readToString(file);
					Matcher matcher = regex.matcher(readToString);
					if (matcher.find())
						return true;

				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}

				return false;
			});

			recursive.forEach(System.out::println);
			Assertions.assertTrue(recursive.size() > 0);
		}

	}
}
