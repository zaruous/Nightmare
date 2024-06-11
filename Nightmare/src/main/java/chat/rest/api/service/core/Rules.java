/**
 * 
 */
package chat.rest.api.service.core;

import java.util.Map;

/**
 * 
 */
public class Rules {

	Map<String, String> systemRole = Map.of("role", "system", "content", "");
	Map<String, String> assistant = Map.of("role", "assistant", "content", "");

	public Map<String, String> getSystemRole() {
		return systemRole;
	}

	public void setSystemRole(Map<String, String> systemRole) {
		this.systemRole = systemRole;
	}

	public Map<String, String> getAssistant() {
		return assistant;
	}

	public void setAssistant(Map<String, String> assistant) {
		this.assistant = assistant;
	}

	public static Rules newInstance() {
		return new Rules();
	}
	
	/**
	 * @param content
	 */
	public Rules systemRuleContent(String content) {
		systemRole = Map.of("role", "system", "content", content);
		return this;
	}
	/**
	 * @param content
	 */
	public Rules assistantRuleContent(String content) {
		assistant = Map.of("role", "assistant", "content", "");
		return this;
	}
}
