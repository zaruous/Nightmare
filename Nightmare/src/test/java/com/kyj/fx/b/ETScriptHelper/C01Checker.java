/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper
 *	작성일   : 2021. 12. 2.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.junit.jupiter.api.Test;

import com.kyj.fx.nightmare.comm.service.XMLUtils;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class C01Checker {

	@Test
	public void test() throws Exception {

		File[] dirs = new File[] { new File("C:\\Users\\KYJ\\Desktop\\새 폴더\\emr-20211201-1\\C01"),
				new File("C:\\Users\\KYJ\\Desktop\\새 폴더\\emr-20211201-2\\C01") };

		int total = 0;
		int pass = 0;
		for (File dir : dirs) {
			File[] listFiles = dir.listFiles();
			for (File f : listFiles) {
				total++;
				String r = FileUtils.readFileToString(f, StandardCharsets.UTF_8);
				int r1 = r.indexOf("<TransactionRequest>");
				int r2 = r.indexOf("</TransactionRequest>", r1) + "</TransactionRequest>".length();

				int r3 = r.indexOf("<TransactionRequest>", r2);
				int r4 = r.indexOf("</TransactionRequest>", r3) + "</TransactionRequest>".length();

//				System.out.println("###########################");
//				System.out.println(r.substring(r1, r2));

				Document doc = XMLUtils.load(r.substring(r3, r4));
				var ret = "PASS".equals(doc.selectSingleNode("//Status").getText());
				if (ret) {
					pass++;
				} else {
					System.err.println(r.substring(r3, r4));
					
				}

			}

		}
		System.out.println("total " + total + " pass : " + pass);
	}
}
