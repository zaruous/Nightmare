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

import com.kyj.fx.nightmare.comm.FileUtil;

/**
 * @author (zaruous@naver.com)
 *
 */
public class SearchUrlPattern {

	String[] patterns = { "http(s)?://[a-zA-Z0-9.-]+(\\.[a-zA-Z]{2,})?(:\\d{1,5})?(/\\S*)?" };
	String[] extFilter = { ".png" , ".fxml", ".xml" };
	String[] igmoreUrlPattern = { "http://www.w3.org" , "https://trends.google.com", "http://www.opensource.org"
			,"http://ajax.googleapis.com", "https://ko.wikipedia.org", "http://xml.apache.org" , "http://www.fontrix.com",
			"https://dev.naver.com", "http://ns.adobe.com" , "http://docs.oracle.com"
	};

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
					
					String lowerCase = file.getName().toLowerCase();
					
					
					for(String ext : extFilter) { if(lowerCase.endsWith(ext)) return false; }
					
					String readToString = FileUtil.readToString(file);
					Matcher matcher = regex.matcher(readToString);
					if (matcher.find()) {
						String group = matcher.group();
						
						
						for(String url : igmoreUrlPattern) {
							if(group.contains(url)) return false;
						}
						
						
						System.out.println(group+"]" + file.getAbsolutePath());
						return true;
					}

				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}

				return false;
			});

			recursive.forEach(a -> {
				
			});
			Assertions.assertTrue(recursive.size() > 0);
		}

	}
}
