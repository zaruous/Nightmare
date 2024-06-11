package chat.rest.api.service.core;

import java.util.Properties;

public class ChatBotConfig {
//	private String rootUrl;
	private Properties config;
//	private String proxyIp;
//	private int proxyPort;

	public String getRootUrl() {
		return getConfig().getProperty("rootUrl");
	}

	public String getModel() {
		return getConfig().getProperty("model");
	}

	public Properties getConfig() {
		return config;
	}

	public void setConfig(Properties config) {
		this.config = config;
	}

}
