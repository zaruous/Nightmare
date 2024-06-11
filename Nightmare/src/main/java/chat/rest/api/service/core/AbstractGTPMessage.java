/**
 * 
 */
package chat.rest.api.service.core;

import java.util.Map;

/**
 * 
 */
public abstract class AbstractGTPMessage {
	private String role;
	private String type;
	private String content;

	public AbstractGTPMessage() {
	}

	public AbstractGTPMessage(String role, String content) {
		this.role = role;
		this.content = content;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return
	 */
	public abstract Map<String, Object> getRequestFormat();

}
