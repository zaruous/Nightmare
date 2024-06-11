/**
 * 
 */
package chat.rest.api.service.core;

/**
 * 
 */
public abstract class ChatFlowItem {

	/**
	 * @return
	 */
	public abstract String roleTemplate();

	/**
	 * @return
	 */
	public abstract String requestTemplate();

	/**
	 * @return
	 */
	public abstract String returnTemplate();

}
