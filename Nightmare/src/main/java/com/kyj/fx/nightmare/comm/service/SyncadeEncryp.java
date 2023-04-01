/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.comm.service
 *	작성일   : 2021. 11. 23.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.nightmare.comm.service;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class SyncadeEncryp {

	/**
	 * DMI.Core.GUI
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 27.
	 * @param DecryptStr
	 * @param encryptKey
	 * @return
	 * @throws Exception
	 */
	public static String encryptString(String encryptStr, String encryptKey) throws Exception {
		int length = encryptKey.length();
		String str1 = "";
		for (int index = 1; index <= encryptStr.length(); ++index) {
			int a = index % length == 0 ? 1 : 0;
			char b = (char) encryptKey.charAt(index % length - length * (int) -a - 1);
			str1 += (char) (encryptStr.charAt(index - 1) ^ b);
		}
		encryptStr = str1;
		String str2 = "";
		NumberFormat nf3 = new DecimalFormat("#000");
		for (int startIndex = 0; startIndex < encryptStr.length(); ++startIndex) {
			byte c = (byte) encryptStr.charAt(startIndex);
			str2 += nf3.format(c);
		}
		return str2;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(encryptString("D1D5D40E-C59A-4563-9C60-0811C1B4DFAF", "zeppelin"));
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 27.
	 * @param Expression
	 * @param Key
	 * @return
	 * @throws Exception
	 */
	public static String decryptString(String Expression, String Key) throws Exception {
		int num = 1;
		String str1 = "";
		String str2 = "";
		while (num < Expression.length()) {

			int int32 = Integer.parseInt(Expression.substring(num - 1, (num - 1 + 3)), 10);
			str1 += (char) int32;
			num += 3;
		}
		Expression = str1;
		// int length = Key.length();
		int int32_1 = Expression.length();
		for (int index = 0; index < int32_1; ++index) {
			int temp = index % Key.length();
			int int32_2 = Key.substring(temp, temp + 1).toCharArray()[0];
			char ch = Expression.toCharArray()[index];
			str2 += (char) (ch ^ int32_2);
		}
		return str2;
	}

}
