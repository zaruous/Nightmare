/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2018. 8. 3.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.b.ETScriptHelper.comm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Json 포멧 처리 유틸리티 클래스 <br/>
 * 
 * @author KYJ
 *
 */
public class JsonFormatter implements IFormatter {

	@Override
	public String format(String json) {
		GsonBuilder builder = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping();
		// setPrettyPrinting = setPrettyPrinting.disableHtmlEscaping();

		Gson gson = builder.create();
		JsonElement parse = JsonParser.parseString(json);
		return gson.toJson(parse);
	}

	/**
	 * pretty 포멧을 갖는 json의 공백 및 개행을 제거한 결과를 리턴. <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 3.
	 * @param json
	 * @return
	 */
	public String removeSpaces(String json) {
		GsonBuilder setPrettyPrinting = new GsonBuilder().disableHtmlEscaping();
		// setPrettyPrinting = setPrettyPrinting.disableHtmlEscaping();

		Gson gson = setPrettyPrinting.create();
		JsonElement je = JsonParser.parseString(json);
		return gson.toJson(je);
	}

}
