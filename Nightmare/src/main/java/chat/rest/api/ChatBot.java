/**
 * 
 */
package chat.rest.api;

import java.io.IOException;

import chat.rest.api.service.core.ChatBotService;
import chat.rest.api.service.core.Rules;
import chat.rest.api.service.impl.ChatGpt3Service;
import chat.rest.api.service.impl.ChatGpt4oService;
import chat.rest.api.service.impl.Ollama3Service;
import chat.rest.api.service.impl.DeepSeekStreamService;
import chat.rest.api.service.impl.RekaService;
import chat.rest.api.service.impl.SpeechToTextGptService;

/**
 * 
 */
public final class ChatBot {

	private  ChatBot() {
		
	}
	public enum API {
		GTP_3_5, GTP_4_O, LLAMA3("OLLAMA"), REKA, GTP_SPEECH_TO_TEST, GEMMA("OLLAMA"), 
		DEEPSEEK_CODER_V2("OLLAMA"), CODEGEMMA("OLLAMA"), O1_PREVIEW, O1_MINI;
		private String group;
		API(String group) {
			this.group = group;
		}
		API() {
			this.group = "";
		}
		public String getGroup() {return this.group; }
	}

	/**
	 * @param api
	 * @return
	 * @throws Exception
	 */
	public static ChatBotService newBotService(API api) throws Exception {
		return newBotService(api, Rules.newInstance());
	}

	/**
	 * @param api
	 * @return
	 * @throws IOException
	 */
	public static ChatBotService newBotService(API api, Rules rules) throws Exception {
		ChatBotService chatGpt3Service = null;
		switch (api) {
		case GTP_3_5:
			chatGpt3Service = new ChatGpt3Service(rules);
			break;
		case O1_MINI:
		case O1_PREVIEW:
		case GTP_4_O:
			chatGpt3Service = new ChatGpt4oService(rules);
			break;
		case GEMMA:	
		case LLAMA3:
		case CODEGEMMA:
			chatGpt3Service = new Ollama3Service();
			break;
		case DEEPSEEK_CODER_V2:
			chatGpt3Service = new DeepSeekStreamService();
			break;
		case REKA:
			chatGpt3Service = new RekaService();
			break;
		case GTP_SPEECH_TO_TEST: 
			chatGpt3Service= new SpeechToTextGptService();
			break;
		}
		return chatGpt3Service;
	}
}
