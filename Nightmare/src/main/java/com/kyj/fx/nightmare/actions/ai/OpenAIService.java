/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.actions.ai.ResponseModelDVO.Choice;
import com.kyj.fx.nightmare.comm.ResourceLoader;
import com.kyj.fx.nightmare.comm.ValueUtil;

import chat.rest.api.ChatBot;
import chat.rest.api.ChatBot.API;
import chat.rest.api.service.core.ChatBotConfig;
import chat.rest.api.service.core.ChatBotService;

/**
 * 
 */
public class OpenAIService {

	private static final int LIMIT_MAX_LENGTH = 65535;
	private static final Logger LOGGER = LoggerFactory.getLogger(OpenAIService.class);
	private long speechId = -1;
	private AIDataDAO aiDataDAO;
	private ChatBotService serivce;
	API model = API.LLAMA3;

	public OpenAIService() throws Exception {
		
		String apiName = ResourceLoader.getInstance().get("chat.ai.api.key", API.GTP_4_O.name());
		LOGGER.info("default apiname : {}", apiName);
		model = API.valueOf(apiName);
		
		
		this.serivce = ChatBot.newBotService(model);
		ChatBotConfig chatBotConfig = this.serivce.getConfig();

		aiDataDAO = AIDataDAO.getInstance();
		Properties config = new Properties();
		String group = ResourceLoader.getInstance().get("chat.ai.api.group", "OPEN_AI");
		Map<String, Object> select = aiDataDAO.getAiConnectionConfig(group, model.name());
		if (!select.isEmpty()) {
			config.setProperty("id", select.get("ID").toString());
			config.setProperty("model", select.get("CNF_CMF_1").toString());
			config.setProperty("rootUrl", select.get("VALUE").toString());
			config.setProperty("apikey", select.get("CNF_CMF_2").toString());
		}
		chatBotConfig.setConfig(config);
	}

	public String send(String message) throws Exception {
		return send("", -1, message, true);
	}

	/**
	 * @param message
	 * @param writeHistory
	 * @return
	 * @throws Exception
	 */
	public String send(String message, boolean writeHistory) throws Exception {
		return send("", -1, message, writeHistory);
	}

	/**
	 * @param message
	 * @param writeHistory
	 * @return
	 * @throws Exception
	 */
	public String send(long speechId, String message) throws Exception {
		return send("", speechId, message, true);
	}

	public String send(String promptId, String msg) throws Exception {
		return send(promptId, -1, msg, true);
	}
	
	public String send(String promptId, long speechId, String msg) throws Exception {
		return send(promptId, speechId , msg, true);
	}

	/**
	 * @param promptId
	 * @param speechId
	 * @param message
	 * @param writeHistory
	 * @return
	 * @throws Exception
	 */
	public String send(String promptId, long speechId, String message, boolean writeHistory) throws Exception {
		long chatId = -1;
		if (writeHistory) {
			Object aiId = serivce.getConfig().getConfig().get("id");
			chatId = aiDataDAO.insertHistory(aiId.toString(),  promptId , speechId, this.getSystemRole(), USER.USER, message);
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
		String string = this.serivce.getSystemRule().get("content");
		if(string.length() > LIMIT_MAX_LENGTH)
			return string.substring(0, LIMIT_MAX_LENGTH);
		return string;
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

	public String toUserMessage(String message) {
		return toUserMessage(model, message);
	}
	public String toUserMessage(String apiName, String message) {
		if(ValueUtil.isEmpty(apiName))
			return toUserMessage(model, message);
		
		try {
		API valueOf = API.valueOf(apiName);
		return toUserMessage(valueOf, message);
		}catch(Exception ex) { 
			LOGGER.error("error apiname : {}" , apiName);
			ex.printStackTrace();
			return toUserMessage(model, message);	
		}
	}
	
	public String toUserMessage(API api, String message) {
		if (API.LLAMA3 == api) {
			return message;
		} else {
			ResponseModelDVO ret = ResponseModelDVO.fromGtpResultMessage(message);
			Choice first = ret.getChoices().getFirst();
			String content2 = first.getMessage().getContent();
			return content2;
		}

	}

}
