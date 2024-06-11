/**
 * 
 */
package chat.rest.api.service.core;

import java.util.Map;

/**
 * 
 */
public class SystemGTPMessage extends AbstractGTPMessage {

	public SystemGTPMessage(String content) {
		super();
		setSystemRole("system");
		setContent(content);
	}

	private void setSystemRole(String role) { super.setRole(role); }
	
	@Override
	public final void setRole(String role) {
		throw new RuntimeException("u cannot modify this rule.");
	}
	
	@Override
	public final String getRole() {
		return super.getRole();
	}

	@Override
	public Map<String, Object> getRequestFormat() {
		return Map.of("rule", getRole(), "text", super.getContent());
	}

}
