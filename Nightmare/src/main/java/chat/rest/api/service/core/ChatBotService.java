package chat.rest.api.service.core;

public interface ChatBotService extends ResponseHandler {

	/**
	 * @param message
	 */
	public void send(String message, ResponseHandler handler) throws Exception;

	public String send(String message) throws Exception;

}
