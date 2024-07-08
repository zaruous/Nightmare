/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

import org.apache.http.entity.ContentType;

import com.kyj.fx.nightmare.actions.comm.ai.AiActionable;
import com.kyj.fx.nightmare.comm.ResourceLoader;
import com.kyj.fx.nightmare.comm.db.DataConnection;

import chat.rest.api.service.core.ChatBotConfig;
import chat.rest.api.service.impl.SpeechToTextGptService;

/**
 * 
 */
public class SpeechToTextGptServiceImpl implements AiActionable{
	private AISpeechDAO aiDataDAO;
	private SpeechToTextGptService service;

	public SpeechToTextGptServiceImpl() throws Exception {
		this.service = new SpeechToTextGptService() {
			@Override
			public ChatBotConfig createConfig() throws Exception {
				aiDataDAO = AISpeechDAO.getInstance();
				ChatBotConfig chatBotConfig = new ChatBotConfig();
				DataConnection mariaDbStore = new DataConnection();
				try (Connection connection = mariaDbStore.getConnection()) {
					Properties config = new Properties();

					// String sql = """
					// select c.* from tbm_sm_cnf c where 1=1 and c.GROUP =
					// 'OPEN_AI' AND c.KEY='TRANSLATE' \n
					// """;
					String group = ResourceLoader.getInstance().get("ai.speech.to.text.group", "OPEN_AI");
					String key = ResourceLoader.getInstance().get("ai.speech.to.text.key", "TRANSLATE");
					Map<String, Object> select = aiDataDAO.getAiConnectionConfig(group, key);
					if (!select.isEmpty()) {
						config.setProperty("id", select.get("ID") == null ? "" : select.get("ID").toString());
						config.setProperty("model", select.get("CNF_CMF_1") == null ? "" :select.get("CNF_CMF_1").toString());
						config.setProperty("rootUrl", select.get("VALUE") ==null ? "" : select.get("VALUE").toString());
						config.setProperty("apikey", select.get("CNF_CMF_2") == null ? "" : select.get("CNF_CMF_2").toString());
						config.setProperty("language", select.get("CNF_CMF_3") == null ? "" : select.get("CNF_CMF_3").toString());
						config.setProperty("prompt", select.get("CNF_CMF_4") == null ? "" : select.get("CNF_CMF_4").toString());
						config.setProperty("response_format", select.get("CNF_CMF_5") == null ? "" : select.get("CNF_CMF_5").toString());
					}

					
					chatBotConfig.setConfig(config);
				}

				return chatBotConfig;
			}
		};
	}

	/**
	 * @param of
	 */
	public void setSystemRole(Map<String, Object> of) {
		this.service.setSystemRole(of);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2024. 6. 15.
	 * @param systemContent
	 * @return
	 */
	public Map<String, String> createDefault(String systemContent) {
		return Map.of("type", "text", "content", systemContent);
	}

	public String getSystemRole() {
		return this.service.getSystemRule().get("content") == null ? "" : this.service.getSystemRule().get("content").toString();
	}

	/**
	 * @return
	 */
	public ChatBotConfig getConfig() {
		return this.service.getConfig();
	}

	public SpeechResponseModelDVO send(String filename, byte[] audio, ContentType contentType) throws Exception {
		long chatId = -1;
		Object aiId = service.getConfig().getConfig().get("id");
		chatId = aiDataDAO.insertHistory(aiId.toString(), this.getSystemRole(), USER.USER, audio, contentType);
		String send = service.send(filename, audio, contentType);
		aiDataDAO.updateAnswer(chatId, send);
		
		SpeechResponseModelDVO ret = new SpeechResponseModelDVO();
		ret.setId(chatId);
		ret.setText(send);
		return ret;
	}

	/**
	 * @param audio
	 * @return
	 * @throws Exception
	 */
	public SpeechResponseModelDVO send(File audio) throws Exception {
		return send(audio.getName(), Files.readAllBytes(audio.toPath()), ContentType.DEFAULT_BINARY);
	}

	public SpeechResponseModelDVO send(Path audio) throws Exception {
		return send(audio.getFileName().toString(), Files.readAllBytes(audio), ContentType.DEFAULT_BINARY);
	}

	public SpeechResponseModelDVO send(String filename, byte[] audio) throws Exception {
		return send(filename, audio, ContentType.DEFAULT_BINARY);
	}
}
