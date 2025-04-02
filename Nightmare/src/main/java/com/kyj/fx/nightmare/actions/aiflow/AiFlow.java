/**
 * 
 */
package com.kyj.fx.nightmare.actions.aiflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 
 */
public class AiFlow {

	private List<AiFlowItem> flowItems;
	/**
	 * flow가 처리할 메타 데이터들
	 */
	private HashMap<String, Object> metadata = new HashMap<>();
	/**
	 * 첫 질문 메세지
	 */
	private String requestMessage;

	public AiFlow() {
		flowItems = new ArrayList<>();
	}

	public void add(AiFlowItem flowItem) {
		this.flowItems.add(flowItem);
	}

	public HashMap<String, Object> getMetadata() {
		return metadata;
	}

	public void setMetadata(HashMap<String, Object> metadata) {
		this.metadata = metadata;
	}

	public String getRequestMessage() {
		return requestMessage;
	}

	public void setRequestMessage(String requestMessage) {
		this.requestMessage = requestMessage;
	}

	// 동작중인 현재 flow 시퀀스
	private int seq;
	/**
	 * 현재 동작중인 플로우
	 */
	private AiFlowItem current;
	
	public final AiFlowItem getCurrent() {
		return current;
	}

	public final int getSeq() {
		return seq;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public String build() {

		this.current = null;
		seq = 0;
		
		String req = this.requestMessage;
		boolean isValid;
		for (AiFlowItem item : flowItems) {
			try {
				this.current = item;
				req = item.request(this.metadata, req);
				isValid = item.verifyResponse(req);
				seq++;
			} catch (Exception ex) {
				isValid = false;
				ex.printStackTrace();
			}

			if (!isValid) {
				break;
			}
		}
		
		return req;
	}

}
