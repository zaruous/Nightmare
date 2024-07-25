/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

/**
 * 
 */
public class JsonFormatOpenAIService extends OpenAIService{

	public JsonFormatOpenAIService() throws Exception {
		super();	
		super.getConfig().setFormat("json");
	}

	
	
}
