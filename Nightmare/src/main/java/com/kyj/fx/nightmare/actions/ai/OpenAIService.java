/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chat.rest.api.service.core.ChatBotConfig;
import chat.rest.api.service.impl.ChatGpt4oService;

/**
 * 
 */
public class OpenAIService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OpenAIService.class);
	private long speechId = -1;
	private AIDataDAO aiDataDAO;
	private ChatGpt4oService serivce;

	public OpenAIService() throws Exception {

		this.serivce = new ChatGpt4oService() {
			@Override
			public ChatBotConfig createConfig() throws Exception {
				aiDataDAO = AIDataDAO.getInstance();
				ChatBotConfig chatBotConfig = new ChatBotConfig();
				Properties config = new Properties();
				Map<String, Object> select = aiDataDAO.getAiConnectionConfig();
				if (!select.isEmpty()) {
					config.setProperty("id", select.get("ID").toString());
					config.setProperty("model", select.get("CNF_CMF_1").toString());
					config.setProperty("rootUrl", select.get("VALUE").toString());
					config.setProperty("apikey", select.get("CNF_CMF_2").toString());
				}

				chatBotConfig.setConfig(config);
				return chatBotConfig;
			}
		};
	}

	public String send(String message) throws Exception {
		return send(-1, message, true);
	}

	/**
	 * @param message
	 * @param writeHistory
	 * @return
	 * @throws Exception
	 */
	public String send(String message, boolean writeHistory) throws Exception {
		return send(-1, message, writeHistory);
	}
	
	/**
	 * @param message
	 * @param writeHistory
	 * @return
	 * @throws Exception
	 */
	public String send(long speechId, String message) throws Exception {
		return send(speechId, message, true);
	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2024. 6. 15.
	 * @param speechId
	 * @param message
	 * @param writeHistory
	 * @return
	 * @throws Exception
	 */
	public String send(long speechId, String message, boolean writeHistory) throws Exception {
		long chatId = -1;
		if (writeHistory) {

			Object aiId = serivce.getConfig().getConfig().get("id");
			chatId = aiDataDAO.insertHistory(aiId.toString(), speechId, this.getSystemRole(), USER.USER, message);
		}
		String send = serivce.send(message);

		if (writeHistory)
			aiDataDAO.updateAnswer(chatId, send);

		return send;
	}

	/**
	 * @param of
	 */
	public void setSystemRole(Map<String, String> of) {
		this.serivce.setSystemRole(of);
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
		return this.serivce.getSystemRule().get("content");
	}

	/**
	 * @return
	 */
	public ChatBotConfig getConfig() {
		return this.serivce.getConfig();
	}

	public void setSpeechId(long speechId) {
		this.speechId = speechId;
	}

	public long getSpeechId() {
		return speechId;
	}

}
