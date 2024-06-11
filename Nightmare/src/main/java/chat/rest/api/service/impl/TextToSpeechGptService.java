/**
 * 
 */
package chat.rest.api.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Properties;
import java.util.stream.Stream;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

import chat.rest.api.service.core.ChatBotConfig;
import chat.rest.api.service.core.TTSGTPRequest;
import chat.rest.api.service.core.TextGTPMessage;

/**
 * 
 */
public class TextToSpeechGptService extends ChatGpt3Service {

	public TextToSpeechGptService() throws Exception {
		super();
	}

	
	@Override
	public ChatBotConfig createConfig() throws Exception {
		ChatBotConfig chatBotConfig = new ChatBotConfig();

		Properties properties = new Properties();
		try (InputStream in = new FileInputStream(new File("tts.gpt.properties"))) {
			properties.load(in);
		}

		chatBotConfig.setConfig(properties);
		return chatBotConfig;
	}


	public byte[] send(TTSGTPRequest request) throws Exception {
		var param = new HashMap<>();
		param.put("model", request.getModel());

		TextGTPMessage message = request.getMessage();

		param.put("input", message.getContent());
		param.put("response_format", request.getResponseFormat());
		param.put("speed", request.getSpeed());
		param.put("voice", request.getVoice());
		// API 요청 생성
		Gson gson = new Gson();
		String requestJson = gson.toJson(param);

		StringEntity entity = new StringEntity(requestJson, StandardCharsets.UTF_8);
		HttpPost httpPost = new HttpPost(getConfig().getRootUrl());
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("Accept-Type", "'application/octet-stream'");
		httpPost.setHeader("Authorization", "Bearer " + getConfig().getConfig().getProperty("apikey"));
		httpPost.setEntity(entity);

		// HttpClient를 사용하여 API 호출
		HttpEntity responseEntity = null;
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(httpPost)) {
			// API 응답 처리
			System.out.println(response.getStatusLine().getStatusCode());
			Stream.of(response.getAllHeaders()).forEach(System.out::println);
			responseEntity = response.getEntity();
			return EntityUtils.toByteArray(responseEntity);
		}
	}
	
	/**
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public byte[] getData(String message) throws Exception {
		var param = new HashMap<>();
		param.put("model", getConfig().getModel());

		param.put("input", message);
		param.put("response_format", getConfig().getConfig().getProperty("response_format"));
		param.put("speed", getConfig().getConfig().getProperty("speed"));
		param.put("voice", getConfig().getConfig().getProperty("voice"));
		// API 요청 생성
		Gson gson = new Gson();
		String requestJson = gson.toJson(param);

		StringEntity entity = new StringEntity(requestJson, StandardCharsets.UTF_8);
		HttpPost httpPost = new HttpPost(getConfig().getRootUrl());
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("Accept-Type", "'application/octet-stream'");
		httpPost.setHeader("Authorization", "Bearer " + getConfig().getConfig().getProperty("apikey"));
		httpPost.setEntity(entity);

		// HttpClient를 사용하여 API 호출
		HttpEntity responseEntity = null;
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(httpPost)) {
			// API 응답 처리
			System.out.println(response.getStatusLine().getStatusCode());
			Stream.of(response.getAllHeaders()).forEach(System.out::println);
			responseEntity = response.getEntity();
			return EntityUtils.toByteArray(responseEntity);
		}
	}

	
	/**
	 * 사용 x
	 */
	@Deprecated
	@Override
	public String send(String message) throws Exception {
		return super.send(message);
	}

}
