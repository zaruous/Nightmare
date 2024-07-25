package chat.rest.api.service.core;

import java.util.Properties;

public class ChatBotConfig {
//	private String rootUrl;
	private Properties config;
	//response format
	private String format;
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

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

}
