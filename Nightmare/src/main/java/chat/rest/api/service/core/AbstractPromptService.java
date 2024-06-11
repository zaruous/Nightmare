package chat.rest.api.service.core;

import java.io.IOException;
import java.util.Map;

public abstract class AbstractPromptService implements ChatBotService {

	private ChatBotConfig config;
	private Rules rule = Rules.newInstance();

	public AbstractPromptService() throws Exception {
		this.config = createConfig();
	}

	public AbstractPromptService(Rules rule) throws Exception {
		this();
		this.rule = rule;
	}

	public ChatBotConfig getConfig() {
		return config;
	}

	public Rules getRule() {
		return rule;
	}

	public void setRule(Rules rule) {
		this.rule = rule;
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public abstract ChatBotConfig createConfig() throws Exception;


	public Map<String, String> getSystemRule() {
		return this.rule.getSystemRole();
	}
	
	public void setSystemRole(Map<String, String> systemRole) {
		this.rule.setSystemRole(systemRole);
	}

	public Map<String, String> assistant() {
		return  this.rule.getAssistant();
	}

}
