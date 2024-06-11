/**
 * 
 */
package chat.rest.api.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

import chat.rest.api.service.core.AbstractGTPMessage;
import chat.rest.api.service.core.ChatBotConfig;
import chat.rest.api.service.core.GTPRequest;
import chat.rest.api.service.core.ResponseHandler;
import chat.rest.api.service.core.Rules;
import chat.rest.api.service.core.TextGTPMessage;
import chat.rest.api.service.core.VirtualPool;

/**
 * 
 */
public class ChatGpt4oService extends ChatGpt3Service {

	public ChatGpt4oService() throws Exception {
		super();
	}

	public ChatGpt4oService(Rules rules) throws Exception {
		super(rules);
	}

	@Override
	public ChatBotConfig createConfig() throws Exception {
		ChatBotConfig chatBotConfig = new ChatBotConfig();

		Properties properties = new Properties();
		try (InputStream in = new FileInputStream(new File("chat.gpt.properties"))) {
			properties.load(in);
		}
		properties.setProperty("model", "gpt-4o");
		chatBotConfig.setConfig(properties);
		return chatBotConfig;
	}

	@Override
	public void send(String message, ResponseHandler handler) throws Exception {
		StringEntity entity = new StringEntity(message);
		HttpPost httpPost = new HttpPost(getConfig().getRootUrl());
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("Authorization", "Bearer " + getConfig().getConfig().getProperty("api.key"));
		httpPost.setEntity(entity);
		VirtualPool.newInstance().execute(runAsync(handler, httpPost));
	}

	public String send(GTPRequest request) throws Exception {
		var param = new HashMap<>();
		param.put("model", request.getModel());

		List<AbstractGTPMessage> list = request.getList();
		List<Map<String, Object>> userContents =  Collections.emptyList();
		if(list ==null)
		{
			throw new RuntimeException("message of list is empty.");
		}
		else
		{
			userContents = list.stream().filter(m -> "user".equals(m.getRole()))
					.map(m -> m.getRequestFormat()).collect(Collectors.toList());		
		}
		

		Map<String, Object> c = Map.of("role", "user", "content", userContents);
		Map<String, String> d = getSystemRule();
		
		List<Object> asList = Arrays.asList(c, d);
		if (null != request.getSystemMessage())
			asList = Arrays.asList(c, request.getSystemMessage());
		param.put("messages", asList);

		if(request.getResponseFormat()!=null && !request.getResponseFormat().isEmpty())
			param.put("response_format", Map.of("type" , request.getResponseFormat()));
		
		// API 요청 생성
		Gson gson = new Gson();
		String requestJson = gson.toJson(param);

//		Files.writeString(Path.of("json.log"), requestJson);

		StringEntity entity = new StringEntity(requestJson, StandardCharsets.UTF_8);
		HttpPost httpPost = new HttpPost(getConfig().getRootUrl());
		httpPost.setHeader("Content-Type", "application/json");
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
			return EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
		}
	}

	public String send(String message) throws Exception {

		var param = new HashMap<>();
		param.put("model", getConfig().getModel());
		param.put("messages", List.of(getSystemRule(), Map.of("role", "user", "content", message)));

		// API 요청 생성
		Gson gson = new Gson();
		String requestJson = gson.toJson(param);
		StringEntity entity = new StringEntity(requestJson, StandardCharsets.UTF_8);
		HttpPost httpPost = new HttpPost(getConfig().getRootUrl());
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("Authorization", "Bearer " + getConfig().getConfig().getProperty("apikey"));
		httpPost.setEntity(entity);

		// HttpClient를 사용하여 API 호출
		HttpEntity responseEntity = null;
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(httpPost)) {
			// API 응답 처리
//			System.out.println(response.getStatusLine().getStatusCode());
			Stream.of(response.getAllHeaders()).forEach(System.out::println);
			responseEntity = response.getEntity();
			return EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
		}
	}

	private Runnable runAsync(ResponseHandler handler, HttpPost httpPost) {
		return new Runnable() {

			@Override
			public void run() {
				// HttpClient를 사용하여 API 호출
				try (CloseableHttpClient httpClient = HttpClients.createDefault();
						CloseableHttpResponse response = httpClient.execute(httpPost)) {

					System.out.println(response.getStatusLine().getStatusCode());
					if (200 == response.getStatusLine().getStatusCode()) {
						// API 응답 처리
						HttpEntity responseEntity = response.getEntity();
						if (responseEntity != null) {
							handler.onSuccess(responseEntity);
						}
					} else {
						// API 응답 처리
						HttpEntity responseEntity = response.getEntity();
						handler.onSuccess(responseEntity);
					}

				} catch (Exception e) {
					handler.onException(e);
				}

			}
		};
	}

}
