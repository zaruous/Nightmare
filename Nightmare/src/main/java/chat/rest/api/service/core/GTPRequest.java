/**
 * 
 */
package chat.rest.api.service.core;

import java.util.List;

/**
 * 
 */
public class GTPRequest {

	public static final String RESPONSE_FORMAT_JSON = "json_object";
	public static final String RESPONSE_FORMAT_TEXT = "text";
	
	private SystemGTPMessage systemMessage;
	private String model;
	
	/**
	 * chat : json_object 
	 */
	private String responseFormat;
	public String getResponseFormat() {
		return responseFormat;
	}

	public void setResponseFormat(String responseFormat) {
		this.responseFormat = responseFormat;
	}
//	
	private List<AbstractGTPMessage> list;

	

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public List<AbstractGTPMessage> getList() {
		return list;
	}

	public void setList(List<AbstractGTPMessage> list) {
		this.list = list;
	}

	public SystemGTPMessage getSystemMessage() {
		return systemMessage;
	}

	/**
	 * @param systemMessage
	 */
	public void setSystemMessage(SystemGTPMessage systemMessage) {
		this.systemMessage = systemMessage;
	}

	/**
	 * @param systemMessage
	 */
	public void setSystemMessage(String systemMessage) {
		this.systemMessage = new SystemGTPMessage(systemMessage);
	}

	/**
	 * @return
	 */
	public static GTPRequest of(String model) {
		GTPRequest gtpRequest = new GTPRequest();
		gtpRequest.setModel(model);
		return gtpRequest;
	}

	public static GTPRequest gpt4oModel() {
		return of("gpt-4o");
	}
	public static GTPRequest whisper1Model() {
		return of("whisper-1");
	}
	
}
