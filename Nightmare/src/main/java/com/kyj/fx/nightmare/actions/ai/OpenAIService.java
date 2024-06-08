/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

import com.kyj.fx.nightmare.comm.DbUtil;
import com.kyj.fx.nightmare.comm.db.DataConnection;

import chat.rest.api.service.core.ChatBotConfig;
import chat.rest.api.service.core.GTPRequest;
import chat.rest.api.service.core.ResponseHandler;
import chat.rest.api.service.impl.ChatGpt4oService;

/**
 * 
 */
public class OpenAIService extends ChatGpt4oService {
	
	private AIDataDAO aiDataDAO = new AIDataDAO();
	
	public OpenAIService() throws Exception {
		super();
		
	}

	@Override
	public ChatBotConfig createConfig() throws Exception {
		ChatBotConfig chatBotConfig = new ChatBotConfig();
		DataConnection mariaDbStore = new DataConnection();
		try (Connection connection = mariaDbStore.getConnection(db -> {
		})) {
			Properties config = new Properties();

			String sql = """
					select c.* from tbm_sm_cnf c where 1=1 and c.GROUP = 'OPEN_AI' AND c.KEY='GTP_4_O' \n
					""";

			Map<String, Object> select = DbUtil.selectOne(connection, sql);
			if (!select.isEmpty()) {
				config.setProperty("id", select.get("ID").toString());
				config.setProperty("model", select.get("CNF_CMF_1").toString());
				config.setProperty("rootUrl", select.get("VALUE").toString());
				config.setProperty("apikey", select.get("CNF_CMF_2").toString());
			}

			chatBotConfig.setConfig(config);
		}

		return chatBotConfig;
	}

	@Override
	public String send(GTPRequest arg0) throws Exception {
		return super.send(arg0);
	}

	@Override
	public void send(String message, ResponseHandler handler) throws Exception {
		super.send(message, handler);
	}

	@Override
	public String send(String message) throws Exception {
		Object aiId = getConfig().getConfig().get("id");
		long chatId = aiDataDAO.insertHistory(aiId.toString(), AIDataDAO.USER.USER, message);
		String send = super.send(message);
		aiDataDAO.updateAnswer(chatId, send);
		return send;
	}

}
