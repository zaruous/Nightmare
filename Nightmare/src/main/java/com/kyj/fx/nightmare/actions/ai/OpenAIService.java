/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

import com.kyj.fx.nightmare.comm.DbUtil;
import com.kyj.fx.nightmare.comm.db.MariaDbStore;

import chat.rest.api.service.core.ChatBotConfig;
import chat.rest.api.service.impl.ChatGpt4oService;

/**
 * 
 */
public class OpenAIService extends ChatGpt4oService {

	public OpenAIService() throws Exception {
		super();

	}

	@Override
	public ChatBotConfig createConfig() throws Exception {
		ChatBotConfig chatBotConfig = new ChatBotConfig();
		MariaDbStore mariaDbStore = new MariaDbStore();
		try (Connection connection = mariaDbStore.getConnection(db -> {
		})) {
			Properties config = new Properties();

			String sql = """
					select c.* from tbm_sm_cnf c where 1=1 and c.GROUP = 'OPEN_AI' AND c.KEY='GTP_4_O' \n
					""";

			Map<String, Object> select = DbUtil.selectOne(connection, sql);
			if (!select.isEmpty()) {
				config.setProperty("model", select.get("CNF_CMF_1").toString());
				config.setProperty("rootUrl", select.get("VALUE").toString());
				config.setProperty("apikey", select.get("CNF_CMF_2").toString());
			}

			chatBotConfig.setConfig(config);
		}

		return chatBotConfig;
	}

}
