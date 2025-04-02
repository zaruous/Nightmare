/**
 * 
 */
package com.kyj.fx.nightmare.actions.aiflow;

import java.util.HashMap;

/**
 * 
 */
public interface AiFlowItem {

	/**
	 * @param requestMessage
	 * @return
	 */
	public String request(HashMap<String, Object> metadata, String requestMessage);

	/**
	 * AI 응답에 대한 검증 작업.
	 * 
	 * @return
	 */
	public boolean verifyResponse(String message);
}
