/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper
 *	작성일   : 2021. 12. 7.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.kyj.fx.nightmare.comm.AES256Util;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class ExcelUploadTest {

	@Disabled

	@Test
	public void test() throws UnsupportedEncodingException, GeneralSecurityException {
		String ec = "DSP3-Column Packing Pump";
		String ev = "Packing Post Cleaning";
		String ac = "Event_OnCancel";

		int hashCode = ec.hashCode();
		int hashCode2 = ev.hashCode();
		int hashCode3 = ac.hashCode();

		System.out.println(hashCode);
		System.out.println(hashCode2);
		System.out.println(hashCode3);

		AES256Util aes256Util = new AES256Util();
		String encrypt = aes256Util.encrypt(ec + "┐" + ev + "┐" + ac);
		String decrypt = aes256Util.decrypt(encrypt);
		System.out.println(encrypt);
		System.out.println(decrypt);
		// String md5 = md5("TEST", ec + ev + ac);
		// System.out.println(md5);

		// DigestUtils.md5
	}

	public static String md5(String plainText) throws NoSuchAlgorithmException {
		return md5(null, plainText);
	}

	public static String md5(String salt, String plainText) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");

		if (salt != null) {
			md.update(salt.getBytes());
		}
		md.update(plainText.getBytes());

		byte byteData[] = md.digest();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}

		return sb.toString();
	}

}
