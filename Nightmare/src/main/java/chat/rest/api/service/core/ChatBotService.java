package chat.rest.api.service.core;

import java.util.Map;

public interface ChatBotService extends ResponseHandler {

	/**
	 * @param message
	 */
	public void send(String message, ResponseHandler handler) throws Exception;

	public String send(String message) throws Exception;

	/**
	 * @return
	 */
	public ChatBotConfig getConfig();
	public void setSystemRole(Map<String, String> systemRole);
	public Map<String, String> getSystemRule();
}
