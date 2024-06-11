/**
 * 
 */
package chat.rest.api.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Objects;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import chat.rest.api.service.core.ChatBotConfig;
import chat.rest.api.service.core.SpeechToTextRequest;

/**
 * 
 */
public class SpeechToTextGptService extends ChatGpt3Service {

	public SpeechToTextGptService() throws Exception {
		super();
	}

	@Override
	public ChatBotConfig createConfig() throws Exception {
		ChatBotConfig chatBotConfig = new ChatBotConfig();

		Properties properties = new Properties();
		try (InputStream in = new FileInputStream(new File("stt.gpt.properties"))) {
			properties.load(in);
		}

		chatBotConfig.setConfig(properties);
		return chatBotConfig;
	}

	/**
	 * @param audio
	 * @return
	 * @throws Exception
	 */
	public String send(File audio) throws Exception {
		return send(audio.getName(), Files.readAllBytes(audio.toPath()), ContentType.DEFAULT_BINARY);
	}

	public String send(Path audio) throws Exception {
		return send(audio.getFileName().toString(), Files.readAllBytes(audio), ContentType.DEFAULT_BINARY);
	}

	public String send(String filename, byte[] audio) throws Exception {
		return send(filename, audio, ContentType.DEFAULT_BINARY);
	}

	/**
	 * @param filename
	 * @param audio
	 * @param contentType
	 * @return
	 * @throws Exception
	 */
	public String send(String filename, byte[] audio, ContentType contentType) throws Exception {
		SpeechToTextRequest speechToTextRequest = new SpeechToTextRequest();
		speechToTextRequest.setAudio(audio);
		speechToTextRequest.setFileName(filename);
		speechToTextRequest.setContentType(contentType);
		speechToTextRequest.setModel(getConfig().getModel());
		speechToTextRequest.setPrompt(getConfig().getConfig().getProperty("prompt"));
		speechToTextRequest.setResponse_format(getConfig().getConfig().getProperty("response_format"));
		speechToTextRequest.setLanguage(getConfig().getConfig().getProperty("language"));
		speechToTextRequest.setApiKey(getConfig().getConfig().getProperty("apikey"));
		speechToTextRequest.setRootUrl(getConfig().getRootUrl());
		return send(speechToTextRequest);
	}

	/**
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String send(SpeechToTextRequest request) throws Exception {
		var param = new HashMap<>();
		param.put("model", getConfig().getModel());

		MultipartEntityBuilder builder = MultipartEntityBuilder.create().addTextBody("model", request.getModel())
				.addBinaryBody("file", request.getAudio(), request.getContentType(), request.getFileName());

		if (Objects.nonNull(request.getPrompt()))
			builder.addTextBody("prompt", request.getPrompt());

		if (Objects.nonNull(request.getResponse_format()))
			builder.addTextBody("response_format", request.getResponse_format());

		if (Objects.nonNull(request.getLanguage()))
			builder.addTextBody("language", request.getLanguage());

		if (Objects.nonNull(request.getTemperature()))
			builder.addTextBody("temperature", String.valueOf(request.getTemperature()));

		HttpEntity entity = builder.build();

		HttpPost httpPost = new HttpPost(request.getRootUrl());
		httpPost.setHeader("Authorization", "Bearer " + request.getApiKey());
		httpPost.setEntity(entity);

		// HttpClient를 사용하여 API 호출
		HttpEntity responseEntity = null;
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(httpPost)) {
			responseEntity = response.getEntity();
			return EntityUtils.toString(responseEntity);
		}
	}

	/**
	 *
	 * @deprecated not support. use others api.
	 */
	@Deprecated
	@Override
	public String send(String message) throws Exception {
		throw new RuntimeException("SpeechToText does not support.!");
	}

}
