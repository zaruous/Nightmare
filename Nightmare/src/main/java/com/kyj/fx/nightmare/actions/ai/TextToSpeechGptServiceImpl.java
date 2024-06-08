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
import chat.rest.api.service.impl.TextToSpeechGptService;

/**
 * 
 */
public class TextToSpeechGptServiceImpl extends TextToSpeechGptService {

	public TextToSpeechGptServiceImpl() throws Exception {
		super();
	}

	@Override
	public ChatBotConfig createConfig() throws Exception {

		ChatBotConfig chatBotConfig = new ChatBotConfig();
		DataConnection mariaDbStore = new DataConnection();
		try (Connection connection = mariaDbStore.getConnection()) {
			Properties config = new Properties();

			String sql = """
					select c.* from tbm_sm_cnf c where 1=1 and c.GROUP = 'OPEN_AI' AND c.KEY='TEXT_TO_SPEECH' \n
					""";

			Map<String, Object> select = DbUtil.selectOne(connection, sql);
			if (!select.isEmpty()) {
				config.setProperty("model", select.get("CNF_CMF_1").toString());
				config.setProperty("rootUrl", select.get("VALUE").toString());
				config.setProperty("apikey", select.get("CNF_CMF_2") == null ? "" : select.get("CNF_CMF_2").toString());
//				config.setProperty("language", select.get("CNF_CMF_3") == null ? "" : select.get("CNF_CMF_3").toString());
//				config.setProperty("prompt", select.get("CNF_CMF_4") == null ? "" : select.get("CNF_CMF_4").toString());
				config.setProperty("response_format", select.get("CNF_CMF_5") == null ? "" : select.get("CNF_CMF_5").toString());
				config.setProperty("voice", select.get("CNF_CMF_6") == null ? "" : select.get("CNF_CMF_6").toString());
				config.setProperty("voice speed", select.get("CNF_CMF_8") == null ? "" : select.get("CNF_CMF_8").toString());
//				config.setProperty("voice", select.get("CNF_CMF_8") == null ? "" : select.get("CNF_CMF_8").toString());
			}

			chatBotConfig.setConfig(config);
		}

		return chatBotConfig;
	}
}
