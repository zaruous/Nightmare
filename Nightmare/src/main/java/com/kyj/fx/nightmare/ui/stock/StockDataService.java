/**
 * 
 */
package com.kyj.fx.nightmare.ui.stock;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * 증권거래소에서 체결된 데이터 정보를 리턴받는다 <br/>
 */
public class StockDataService {

	/**
	 * 증권 데이터를 요청한다<br/>
	 * 
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public static StockDataMasterDVO doService()
			throws MalformedURLException, IOException, UnsupportedEncodingException {
		return doService(new File("stock.json"));
	}

	/**
	 * 증권 데이터를 요청한다<br/>
	 * 
	 * @param tmp 임시파일명
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public static StockDataMasterDVO doService(File tmp)
			throws MalformedURLException, IOException, UnsupportedEncodingException {
		System.out.println(currentDate());
		String content = "";
		if (!tmp.exists()) {
			content = fetchData(tmp);
		} else {
			// 파일을 다시 다운받아야하는지 확인
			boolean downLoad = isDownLoad(tmp.lastModified());
			if (downLoad)
				content = fetchData(tmp);
			else {
				try (BufferedReader br = Files.newBufferedReader(tmp.toPath())) {
					content = br.lines().collect(Collectors.joining());
				}
			}
		}

		Gson gson = new Gson();
		StockDataMasterDVO fromJson = gson.fromJson(content, StockDataMasterDVO.class);
		return fromJson;

	}

	/**
	 * 현재 시간 리턴 <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 23.
	 * @return
	 */
	static String currentDate() {
		return new SimpleDateFormat("yyyyMMdd").format(new Date());
	}

	/**
	 * 파일 날짜가 현재 시간보다 20차이가 넘는다면 다시 받는다.
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 23.
	 * @return
	 */
	static boolean isDownLoad(long lastModified) {
		// long lastModified = tmp.lastModified();

		Date fileDate = new Date(lastModified);
		// Date currentDate = new Date();

		Calendar instance = Calendar.getInstance();
		instance.setTime(fileDate);
		instance.add(Calendar.MINUTE, 20);
		// TimeUnit.MINUTES.convert(instance.getTime().getTime());
		long abs = Math.abs(instance.getTime().getTime() - new Date().getTime());
		long convert = TimeUnit.MINUTES.convert(abs, TimeUnit.MILLISECONDS);
		System.out.println("diff minute: " + convert);
		return convert > 20;
	}

	/**
	 * 서버에서 데이터를 받는다.
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 23.
	 * @param tmp
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	private static String fetchData(File tmp) throws MalformedURLException, IOException, UnsupportedEncodingException {
		String content;
		String url = "http://data.krx.co.kr/comm/bldAttendant/getJsonData.cmd";
		URL u = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();

		conn.setDefaultUseCaches(true);
		conn.setUseCaches(true);

		conn.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:12.0) Gecko/20100101 Firefox/12.0");
		conn.setRequestProperty("Accept-Encoding", "UTF-8");
		// conn.setRequestProperty("Connection", "keep-alive");

		// conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("Accept-Charset", "UTF-8");
		conn.setRequestProperty("Accept-Encoding", "UTF-8");
		conn.setRequestProperty("Accept-Language", "KR");

		conn.setDoOutput(true);
		String currentDate = currentDate();
		conn.getOutputStream().write(String.format("bld=dbms/MDC/STAT/standard/MDCSTAT01501&mktId=ALL&trdDd=%s",
				currentDate, "&share=1&money=1&csvxls_isNo=false").getBytes("utf-8"));

		// conn.connect();

		int responseCode = conn.getResponseCode();
		System.out.println(responseCode);
		System.out.println(conn.getResponseMessage());
		if (responseCode == 200) {
			Gson create = new GsonBuilder().setPrettyPrinting().create();
			try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
				JsonElement parse = JsonParser.parseReader(br); // new JsonParser().parse(r);
				String json = create.toJson(parse);
				writeTempFile(tmp, json);
				content = json;
			}
		} else
			content = "";
		return content;
	}

	/**
	 * 임시 파일에 정보를 저장.
	 * 
	 * @param tmp
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 23.
	 * @param tmp
	 * @param json
	 * @throws IOException
	 */
	private static void writeTempFile(File tmp, String json) throws IOException {
		Files.deleteIfExists(tmp.toPath());
		BufferedWriter writer = null;
		try {
			writer = Files.newBufferedWriter(tmp.toPath(), StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW,
					StandardOpenOption.WRITE);
			writer.write(json);
		} finally {
			if (writer != null)
				writer.close();
		}

	}

}
