/**
 * 
 */
package chat.rest.api.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.kyj.fx.nightmare.comm.ResourceLoader;
import com.kyj.fx.nightmare.comm.initializer.GargoyleHostNameVertifier;
import com.kyj.fx.nightmare.comm.initializer.GargoyleSSLVertifier;
import com.kyj.fx.nightmare.comm.initializer.ProxyInitializable;

/**
 * 
 */
public class DeepSeekStreamService extends Ollama3Service {
	private static final Logger LOGGER = LoggerFactory.getLogger(DeepSeekStreamService.class);

	
	public DeepSeekStreamService() throws Exception {
		super();
	}

	@Override
	public String send(List<Map<String, Object>> assistance, String message) throws Exception {

		var param = new HashMap<>();
		param.put("model_class", getConfig().getModel());
//		if (assistance.isEmpty())
//			param.put("messages", List.of(getSystemRule(), Map.of("role", "user", "content", message)));
//		else {
//			ArrayList<Map<String, Object>> arrayList = new ArrayList<>(assistance.size() + 2);
//			arrayList.add(getSystemRule());
//			arrayList.addAll(assistance);
//			arrayList.add(Map.of("role", "user", "content", message));
//
//			param.put("messages", arrayList);
//		}
		String sys = getSystemRule().get("content").toString();
		String assitst = assistance.stream().map( m -> m.get("content").toString()).collect(Collectors.joining("\n"));
		
		String msg = String.format("%s\n%s\n%s", sys, assitst, message);
		param.put("message", msg);
		
		param.put("model_preference", null);
		param.put("temperature", 0);
//		param.put("format", getFormat());
		param.put("stream", isStream());

		// API 요청 생성
		Gson gson = new Gson();
		String requestJson = gson.toJson(param);
		StringEntity entity = new StringEntity(requestJson, StandardCharsets.UTF_8);
		LOGGER.debug("{}", requestJson);
		HttpPost httpPost = new HttpPost(getConfig().getRootUrl());
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("Authorization", "Bearer " + getConfig().getConfig().getProperty("apikey"));

		if (ProxyInitializable.isUseProxy()) {
			httpPost.setConfig(RequestConfig.custom()
					.setProxy(
							new HttpHost(ProxyInitializable.getHttpsProxyHost(), ProxyInitializable.getHttpProxyPort()))
					.build());
		}
		httpPost.setEntity(entity);

		// HttpClient를 사용하여 API 호출
		HttpEntity responseEntity = null;
		HttpClientBuilder httpClientBuilder = HttpClients.custom();
		if ("Y".equals(ResourceLoader.getInstance().get(ResourceLoader.SSL_VERIFY, "Y"))) {
			httpClientBuilder.setSSLContext(GargoyleSSLVertifier.defaultContext());
			httpClientBuilder.setSSLHostnameVerifier(GargoyleHostNameVertifier.defaultVertifier());
		}

		try (CloseableHttpClient httpClient = httpClientBuilder.build()) {

			try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
//				System.out.println("!!");
				// API 응답 처리
				// System.out.println(response.getStatusLine().getStatusCode());
				Stream.of(response.getAllHeaders()).forEach(h -> LOGGER.debug("{}", h));
				responseEntity = response.getEntity();
				String str;
				if (response.getStatusLine().getStatusCode() == 200) {
					String contType = responseEntity.getContentType().getValue();

					// var sb = new StringBuilder();
					try (BufferedReader r = new BufferedReader(new InputStreamReader(responseEntity.getContent()))) {
						if (contType.contains("text/event-stream")) {
							str = r.lines().map(s -> s.startsWith("data: ") ? s.substring("data: ".length()) : s)
									.map(ret -> {
//										LOGGER.debug("response : {}", ret);
										ChatCompletionChunk d = gson.fromJson(ret, ChatCompletionChunk.class);
										return d == null ? "" : d.choices.get(0).delta.content; 
//										return 
//										return D.getMessage().getContent();
									}).collect(Collectors.joining());
						} else {
							str = r.lines().map(ret -> {
								LOGGER.debug("response : {}", ret);
								Ollama3ResponseDVO D = gson.fromJson(ret, Ollama3ResponseDVO.class);
								return D.getMessage().getContent();
							}).collect(Collectors.joining());
						}

					}
				} else {
					str = IOUtils.toString(responseEntity.getContent(), Charset.defaultCharset());
					LOGGER.error(str);
				}

				return str;
			}
		}

	}

	public static class ChatCompletionChunk {

	    @SerializedName("id")
	    private String id;

	    @SerializedName("object")
	    private String object;

	    @SerializedName("created")
	    private long created;

	    @SerializedName("model")
	    private String model;

	    @SerializedName("system_fingerprint")
	    private String systemFingerprint;

	    @SerializedName("choices")
	    private List<Choice> choices;

	    @SerializedName("usage")
	    private Object usage;

	    @SerializedName("request_id")
	    private String requestId;

	    @SerializedName("context_id")
	    private int contextId;

	    @SerializedName("chat_id")
	    private String chatId;

	    // Getters and setters

	    public static class Choice {
	        @SerializedName("index")
	        private int index;

	        @SerializedName("delta")
	        private Delta delta;

	        @SerializedName("logprobs")
	        private Object logprobs;

	        @SerializedName("finish_reason")
	        private Object finishReason;

	        // Getters and setters
	    }

	    public static class Delta {
	        @SerializedName("content")
	        private String content;

	        // Getters and setters
	    }

	    // Getters and setters
	}

	@Override
	protected boolean isStream() {
		return true;
	}

	public String send(String message) throws Exception {
		return send(Collections.emptyList(), message);
	}

}
