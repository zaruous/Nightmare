/**
 * 
 */
package chat.rest.api.service.core;

/**
 * 
 */
public class TTSGTPRequest {

	public static final String VOICE_ALLOY = "alloy";
	public static final String VOICE_ECHO = "echo";
	public static final String VOICE_FABLE = "fable";
	public static final String VOICE_ONYX = "onyx";
	public static final String VOICE_NOVA = "nova";
	public static final String VOICE_SHIMMER = "shimmer";

//	"mp3", "opus", "aac", "flac", "wav", "pcm"
	public static final String RESPONSE_FORMAT_MP3 = "mp3";
	public static final String RESPONSE_FORMAT_WAV = "wav";

	public static final String MODEL_TTS_1 = "tts-1";
	public static final String MODEL_FTTS_1_HD = "tts-1-hd";

	public static final String RESPONSE = "tts-1-hd";

	private float speed = 1.0f;

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	private String voice = VOICE_ALLOY;

	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}

	private SystemGTPMessage systemMessage;
	private String model = MODEL_TTS_1;

	/**
	 * chat : json_object
	 */
	private String responseFormat = RESPONSE_FORMAT_MP3;

	public String getResponseFormat() {
		return responseFormat;
	}

	public void setResponseFormat(String responseFormat) {
		this.responseFormat = responseFormat;
	}

//	
	private TextGTPMessage message;

	public TextGTPMessage getMessage() {
		return message;
	}

	public void setMessage(TextGTPMessage message) {
		this.message = message;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

//	public List<AbstractGTPMessage> getList() {
//		return list;
//	}
//
//	public void setList(List<AbstractGTPMessage> list) {
//		this.list = list;
//	}

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
	public static TTSGTPRequest of(String model) {
		TTSGTPRequest gtpRequest = new TTSGTPRequest();
		gtpRequest.setModel(model);
		return gtpRequest;
	}

	public static TTSGTPRequest ttsModel() {
		return of("tts-1");
	}
}
