/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper
 *	작성일   : 2021. 12. 7.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.kyj.fx.b.ETScriptHelper.comm.AES256Util;
import com.kyj.fx.b.ETScriptHelper.comm.GargoyleHostNameVertifier;
import com.kyj.fx.b.ETScriptHelper.comm.GargoyleSSLVertifier;
import com.kyj.fx.b.ETScriptHelper.comm.RequestUtil;
import com.kyj.fx.b.ETScriptHelper.comm.ResourceLoader;
import com.kyj.fx.b.ETScriptHelper.comm.ValueUtil;

/**
 * @author KYJ (callakrsos@naver.com)
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

	@Disabled
	@Test
	public void naskFileUpdownTest() throws Exception {

		HttpEntityEnclosingRequestBase http = new HttpPost("https://p3mesdev/OrderManagementWebpart/NaskaFileUpDown.aspx");
		http.setConfig(RequestConfig.custom().setConnectTimeout(RequestUtil.connectionTimeout()).build());

		HttpClientBuilder b = HttpClientBuilder.create();

		String h = ResourceLoader.getInstance().get(ResourceLoader.HTTP_PROXY_HOST);
		String p = ResourceLoader.getInstance().get(ResourceLoader.HTTP_PROXY_PORT);
		HttpHost proxy = new HttpHost(h, Integer.parseInt(p, 10), "http");
		b.setProxy(proxy);

		// b = b.setDefaultCookieStore(cookieStore);
		b = b.setSSLContext(GargoyleSSLVertifier.defaultContext());
		b = b.setSSLHostnameVerifier(GargoyleHostNameVertifier.defaultVertifier());
		CloseableHttpClient build = b.build();
		http.setHeader(new BasicHeader("user", "et"));
		http.setEntity(new InputStreamEntity(new FileInputStream("C:\\Users\\KYJ\\00155d301f6exz73zm1k575e1d0yhj000.xlsx")));
		try (CloseableHttpResponse execute = build.execute(http)) {
			HttpEntity entity = execute.getEntity();
			try (InputStream content = entity.getContent()) {

				var bytes = ValueUtil.toByte(content);
				System.out.println(bytes.length);
				try (FileOutputStream fileOutputStream = new FileOutputStream(new File("test.xlsx"))) {
					fileOutputStream.write(bytes);
				}
			} catch (UnsupportedOperationException e) {
				e.printStackTrace();
			}

		}

	}
}
