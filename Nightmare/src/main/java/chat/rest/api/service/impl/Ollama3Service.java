/**
 * 
 */
package chat.rest.api.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.Gson;

import chat.rest.api.service.core.AbstractPromptService;
import chat.rest.api.service.core.ChatBotConfig;
import chat.rest.api.service.core.ResponseHandler;
import chat.rest.api.service.core.VirtualPool;

/**
 * 
 */
public class Ollama3Service extends AbstractPromptService {

	public Ollama3Service() throws Exception {
		super();
	}

	@Override
	public ChatBotConfig createConfig() throws IOException {
		ChatBotConfig chatBotConfig = new ChatBotConfig();

		Properties properties = new Properties();
		try (InputStream in = new FileInputStream(new File("ollama3.properties"))) {
			properties.load(in);
		}

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

		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
//				System.out.println("!!");
				// API 응답 처리
				// System.out.println(response.getStatusLine().getStatusCode());
				Stream.of(response.getAllHeaders()).forEach(System.out::println);
				responseEntity = response.getEntity();

				String str;
				// var sb = new StringBuilder();
				try (BufferedReader r = new BufferedReader(new InputStreamReader(responseEntity.getContent()))) {
					str = r.lines().map(ret -> {
						Ollama3ResponseDVO D = gson.fromJson(ret, Ollama3ResponseDVO.class);
						return D.getMessage().getContent();
					}).collect(Collectors.joining());
					// sb.append(r.readLine());
				}
				return str;
			}
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

	static class Ollama3ResponseDVO {
		private String model;
		private Date created_at;
		private Message message;
		private boolean done;

		public class Message {
			private String role;
			private String content;

			// Getters and Setters for Message fields
			public String getRole() {
				return role;
			}

			public void setRole(String role) {
				this.role = role;
			}

			public String getContent() {
				return content;
			}

			public void setContent(String content) {
				this.content = content;
			}
		}

		// Getters and Setters for Sample fields
		public String getModel() {
			return model;
		}

		public void setModel(String model) {
			this.model = model;
		}

		public Date getCreated_at() {
			return created_at;
		}

		public void setCreated_at(Date created_at) {
			this.created_at = created_at;
		}

		public Message getMessage() {
			return message;
		}

		public void setMessage(Message message) {
			this.message = message;
		}

		public boolean isDone() {
			return done;
		}

		public void setDone(boolean done) {
			this.done = done;
		}
	}

}
