/**
 * 
 */
package chat.rest.api.service.core;

import java.util.Map;

/**
 * 
 */
public class TextGTPMessage extends AbstractGTPMessage {

	public TextGTPMessage() {
		super();
		setType("text");
	}

	public TextGTPMessage(String content) {
		this();
		setRole("user");
		setContent(content);
	}

	public TextGTPMessage(String role, String content) {
		this();
		setRole("user");
		setContent(content);
	}

	@Override
	public Map<String, Object> getRequestFormat() {
		return Map.of("type", super.getType(), "text", super.getContent());
	}

	/**
	 * @param content
	 * @return
	 */
	public static TextGTPMessage of(String content) {
		return new TextGTPMessage(content);
	}
}
