/**
 * 
 */
package chat.rest.api.service.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class ChatFlowBuilder {

	private List<ChatFlowItem> buildSeqneuce;

	public ChatFlowBuilder() {
		buildSeqneuce = new ArrayList<>();
	}

	public ChatFlowBuilder(List<ChatFlowItem> buildSeqneuce) {
		this.buildSeqneuce = buildSeqneuce;
	}

	public ChatFlowBuilder append(ChatFlowItem chatFlowItem) {
		this.buildSeqneuce.add(chatFlowItem);
		return this;
	}

}
