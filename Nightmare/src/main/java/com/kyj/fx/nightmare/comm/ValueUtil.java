/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.comm
 *	작성일   : 2021. 11. 22.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.nightmare.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;

import com.google.gson.Gson;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class ValueUtil {

	public static boolean isNotEmpty(Object obj) {
		boolean flag = true;
		if (obj != null) {
			if (obj instanceof String) {
				String valueOf = obj.toString().trim();
				flag = valueOf.length() > 0 && !valueOf.equals("") && !valueOf.equals("null");
			} else if (obj instanceof Collection) {
				Collection<?> list = (Collection<?>) obj;
				flag = !list.isEmpty();

				// flag = list.size() > 0;
			} else if (obj instanceof Map) {

				Map<?, ?> map = (Map<?, ?>) obj;
				flag = map.size() > 0;
			}
		} else {
			flag = false;
		}
		return flag;

	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 22. 
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Object obj) {
		return !isNotEmpty(obj);
	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 22. 
	 * @param e
	 * @return
	 */
	public static String toString(Throwable e) {
		return toString(null, e);
	}
	
	/**
	 * 에러 메세지 상세화
	 *
	 * @param title
	 *            메세지 타이틀
	 * @param e
	 * @return
	 */
	public static String toString(String title, Throwable e) {
		if (e == null)
			return "[warnning] Exception is null";

		String errMsg = "";
		try (StringBuilderWriter sbw = new StringBuilderWriter()) {
			try (PrintWriter printWriter = new PrintWriter(sbw, true)) {
				if (title != null)
					printWriter.write("#############  ".concat(title).concat("  ##############\n"));
				e.printStackTrace(printWriter);
			}
			errMsg = sbw.toString();
		}
		return errMsg;
	}
	
	public static String getVelocityToText(String dynamicSql, String key, Object value) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put(key, value);
		return getVelocityToText(dynamicSql, hashMap, false, null);
	}
	
	/**
	 * Velocity문법의 텍스트를 맵핑된 텍스트결과값으로 반환
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 21.
	 * @param dynamicSql
	 * @param paramMap
	 * @return
	 */
	public static String getVelocityToText(String dynamicSql, Map<String, Object> paramMap) {
		return getVelocityToText(dynamicSql, paramMap, false, null);
	}
	
	/**
	 * Velocity문법의 텍스트를 맵핑된 텍스트결과값으로 반환
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 22.
	 * @param dynamicSql
	 * @param paramMap
	 * @param replaceNamedValue
	 *            namedParameter값을 바인드 변수로 사용하여 보여줄지 유무
	 * @return
	 */
	public static String getVelocityToText(String dynamicSql, Map<String, Object> paramMap, boolean replaceNamedValue,
			Context velocityContext) {
		return getVelocityToText(dynamicSql, paramMap, replaceNamedValue, velocityContext, str -> String.format("'%s'", str));
	}
	
	/**
	 * Velocity문법의 텍스트를 맵핑된 텍스트결과값으로 반환
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 22.
	 * @param dynamicSql
	 * @param paramMap
	 * @param replaceNamedValue
	 *            namedParameter값을 바인드 변수로 사용하여 보여줄지 유무
	 * @param customReplaceFormat
	 *            변환할 문자열을 커스텀한 포멧으로 리턴받을 수 있는 기능을 제공하기 위한 파라미터
	 * @return
	 */
	public static String getVelocityToText(String dynamicSql, Map<String, Object> paramMap, boolean replaceNamedValue,
			Context velocityContext, Function<String, String> customReplaceFormat) {
		StringWriter writer = new StringWriter();
		VelocityContext context = new VelocityContext(paramMap, new DateFormatVelocityContextExtension(velocityContext));

		String _dynamicSql = dynamicSql;

		Velocity.evaluate(context, writer, "DaoWizard", _dynamicSql);
		String convetedString = writer.toString();
		if (replaceNamedValue) {
			convetedString = replace(convetedString, paramMap, customReplaceFormat);
		}
		return convetedString.trim();
	}
	
	static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"|\'([^\"\\\\]|\\\\.)*\'";
	static final String COMMENT_PATTERN = "(?:/\\*[^;]*?\\*/)|(?:--[^\\n]*)";
	
	private static String replace(String sql, Map<String, Object> paramMap, Function<String, String> customFormat) {
		if (sql == null || sql.trim().isEmpty())
			return sql;

		// String specialCharacter = getDynamicValueSpecialCharacter();
		String _sql = sql.replaceAll(COMMENT_PATTERN, "");
		String pattern = ":\\w+";

		String result = regexReplaceMatchs(pattern, _sql, v -> {
			String replace = v.replaceAll(":", "");
			Object object = paramMap.get(replace);
			String string = object.toString();
			if (object instanceof List) {
				StringBuffer sb = new StringBuffer();
				List<Object> items = (List<Object>) object;
				for (Object o : items) {
					if (ValueUtil.isNotEmpty(o)) {
						sb.append(String.format("'%s',", o.toString()));
					}
				}
				int length = sb.length();
				if (length != 0) {
					sb.setLength(length - 1);
				}
				return sb.toString();
			}
			// 2016-11-01 custom 포멧 제공
			return customFormat.apply(string); // String.format("'%s'", string);
		});
		return result;
	}
	
	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 2. 2.
	 * @param regex
	 * @param value
	 * @param replacedText
	 * @return
	 */
	public static String regexReplaceMatchs(String regex, String value, Function<String, String> replacedText) {

		Pattern compile = Pattern.compile(regex);

		StringBuffer sb = new StringBuffer(value);
		Matcher matcher = compile.matcher(value);

		/*
		 * 18.02.05 문자열 치환 버그 수정 <br/> from [[ start = start + end ]] <br/> to
		 * [[ start = start + replaceText.length(); ]] 로 수정 <br/>
		 * 
		 * <br/>
		 * 
		 * 18.02.02 <br/> 중요한 버그 수정. replace되는 값에도 :value 패턴이 존재하면 <br/> 에러가
		 * 발생함. start와 end 인덱스를 유지하여 치환된 텍스트는 검색대상에서 제외하는 로직 추가. <br/>
		 */
		int start = 0;
		int end = 0;
		// 패턴에 일치하는 문자가 없을때까지 반복한다.
		while (matcher.find(start)) {
			start = matcher.start();
			end = matcher.end();
			String replaceText = replacedText.apply(sb.substring(start, end));
			sb.replace(start, end, replaceText);
			matcher = compile.matcher(sb.toString());

			start = start + replaceText.length();
			end = -1;
		}
		return sb.toString();
	}
	
	/**
	 * get메소드 이름 패턴의 명에서 get을 제거하고 앞글자는 소문자로 바꾼 글자를 반환
	 *
	 * @param methodName
	 * @return
	 */
	public static String getSimpleMethodName(final String methodName) {
		String getMethodName = methodName;
		// validation
		char[] charArray = getMethodName.replaceFirst("get", "").toCharArray();
		String lowerCase = String.valueOf(charArray[0]).toLowerCase();
		charArray[0] = lowerCase.charAt(0);
		getMethodName = String.valueOf(charArray);
		return getMethodName;
	}

	/**
	 * TODO 메세지 처리 방안 기술.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 12. 14.
	 * @param messageId
	 * @return
	 */
	public static String getMessage(String messageId) {
		return "";
	}

	/**
	 * value값이 빈값이아니면 notEmpty리턴 value값이 빈값이면 empty 리턴
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 26.
	 * @param value
	 * @param emptyThan
	 * @return
	 */
	public static Object decode(Object value, Object notEmpty, Object empty) {
		if (isEmpty(value)) {
			return empty;
		}
		return notEmpty;
	}

	public static Object decode(Object value, Object empty) {
		if (isEmpty(value)) {
			return empty;
		}
		return value;
	}
	
	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 5. 26.
	 * @param value
	 * @param notEmptyThan
	 * @param emptyThan
	 * @return
	 */
	public static <T, R> R decode(T value, Function<T, R> notEmptyThan, Supplier<R> emptyThan) {
		if (isNotEmpty(value))
			return notEmptyThan.apply(value);
		return emptyThan.get();
	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 23. 
	 * @param inputStream
	 * @return
	 */
	public static String toString(InputStream inputStream) {
		return toString(inputStream, Charset.forName("UTF-8"));
	}
	
	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 11. 1.
	 * @param inputStream
	 *            stream
	 * @param charset
	 *            encoding
	 * @return
	 * @throws IOException
	 */
	public static String toString(InputStream inputStream, Charset charset) {
		try {
			return IOUtils.toString(inputStream, charset);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 16.
	 * @param inputStream
	 * @return
	 */
	public static byte[] toByte(InputStream inputStream) {
		try {
			return IOUtils.toByteArray(inputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}	
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 30. 
	 * @param text
	 * @return
	 */
	public static String rightTrim(String text) {
		return text.replaceAll("\\s+$", "");
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 30. 
	 * @param text
	 * @param replaceText
	 * @return
	 */
	public static String leftReplace(String text, String replaceText) {
		return text.replaceAll("^\\s+", replaceText);
	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 30. 
	 * @param text
	 * @return
	 */
	public static String leftTrim(String text) {
		return leftReplace(text, "");
	}

	/**
	 * str 문자열에서 index에 해당하는 부분만 소문자로 고친다.
	 *
	 * @param str
	 * @param index
	 * @return
	 */
	public static String getIndexLowercase(String str, int index) {
		return getIndexcase(str, index, IndexCaseTypes.LOWERCASE);
	}

	public enum IndexCaseTypes {
		UPPERCASE, LOWERCASE
	}
	
	/**
	 * str 문자열에서 index에 해당하는 부분만 대문자, 혹은 소문자로 고친다. (IndexCaseTypes에 따른 설정)
	 *
	 * @param str
	 *            target
	 * @param index
	 *            character index
	 * @param type
	 *            lower or upper
	 * @return
	 */
	public static String getIndexcase(String str, int index, IndexCaseTypes type) {
		StringBuffer sb = new StringBuffer();

		// DEFAULT UPPERCASE
		char indexChar = Character.toUpperCase(str.charAt(index));
		if (type == IndexCaseTypes.LOWERCASE) {
			indexChar = Character.toLowerCase(str.charAt(index));
		}

		switch (index) {
		case 0:
			sb.append(indexChar).append(str.substring(index + 1));
			break;
		default:
			sb.append(str.substring(0, index)).append(indexChar).append(str.substring(index + 1));
			break;
		}
		return sb.toString();
	}
	
	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 4. 19.
	 * @param obj
	 * @return
	 */
	public static String toJSONString(Object obj) {
		Map m = ObjectUtil.toMap(obj);
		Gson gson = new Gson();
		String fromJson = gson.toJson(m);
		return fromJson;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 9. 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean equals(String s1, String s2) {
		return StringUtils.equals(s1, s2);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 4.
	 * @param value
	 */
	public static boolean isNumber(String value) {
		int length = value.length();
		for (int i = 0; i < length; i++)
			if (!Character.isDigit(value.charAt(i)))
				return false;
		return length == 0 ? false : true;

	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2022. 6. 27. 
	 * @param text
	 * @param text2
	 * @return
	 */
	public static int compare(String text, String text2) {

		if(isNumber(text) && isNumber(text2))
			return Double.compare(Double.parseDouble(text.toString()), Double.parseDouble(text2.toString()));

		if (text == null && text2 == null)
			return 0;
		else if (text == null)
			return 1;
		else if (text2 == null)
			return -1;
		return text.compareTo(text2);
		
	}		

}
