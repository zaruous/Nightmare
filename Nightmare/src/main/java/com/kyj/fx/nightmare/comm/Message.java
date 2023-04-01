/********************************
 *	프로젝트 : ReleaseAagent
 *	패키지   : com.kyj.fx.release.core.bundle
 *	작성일   : 2017. 12. 28.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.nightmare.comm;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.fxloader.GagoyleResourceBundle;

/**
 * 메세지 관련 유틸리티 클래스
 * 
 * @author KYJ
 *
 */
public class Message {
	private static final Logger LOGGER = LoggerFactory.getLogger(Message.class);
	private Locale locale;
	private String bundleName;

	private static Message message;

	/**
	 * 디폴트 인스턴스 리턴
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 12. 28.
	 * @return
	 */
	public static Message getInstance() {
		if (message == null) {
			message = new Message();
		}
		return message;
	}

	/**
	 */
	public Message() {
		this(GagoyleResourceBundle.BUNDLE_NAME, Locale.getDefault());
	}

	/**
	 * @param bundleName
	 * @param locale
	 */
	public Message(String bundleName, Locale locale) {
		this.bundleName = bundleName;
		this.locale = locale;
	}

	/**
	 * 다국어 메세지 리턴 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 12. 28.
	 * @param key
	 * @return
	 */
	public String getMessage(String key) {
		return getMessage(key, this.locale);
	}

	/**
	 * 다국어 메세지 리턴 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 12. 28.
	 * @param key
	 * @param values
	 *            파라미터 값
	 * @return
	 */
	public String getMessage(String key, Object... values) {
		return getMessage(key, this.locale, values);
	}

	/**
	 * 다국어 메세지 리턴 <br/>
	 * 
	 * 사용법 <br/>
	 * 
	 * 다국어 데이터에 아래와 같은 파타미터 변수가 존재하면 치환해줌. <br/>
	 * 
	 * {0} - values[0] <br/>
	 * {1} - values[1]
	 * 
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 12. 28.
	 * @param key
	 * @param locale
	 * @return
	 */
	public String getMessage(String key, Locale locale, Object... values) {
		ResourceBundle bundle = getBundle(this.bundleName, getClass().getClassLoader(), locale);
		String string = bundle.getString(key);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				String regex = "\\{".concat(String.valueOf(i)).concat("\\}");
				string = string.replaceAll(regex, values[i].toString());
			}
			LOGGER.debug(string);
			return string;
		}

		LOGGER.debug(bundle.getString(key));
		return bundle.getString(key);
	}

	/**
	 * instance bundle. <br/>
	 * 
	 * @최초생성일 2018. 6. 28.
	 */
	private ResourceBundle bundle;

	public ResourceBundle getBundle() {
		if (this.bundle == null) {
//			var loader = this.getClass().getClassLoader();
			try {
				this.bundle = GagoyleResourceBundle.getDefaultBundle(this.getClass(), this.bundleName, this.locale);

				if (bundle == null) {
					URL resource = this.getClass().getResource("/bundles/GargoyleBundle_en.properties");
					bundle = new GagoyleResourceBundle(new InputStreamReader(resource.openStream(), "UTF-8"));
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return bundle;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 28.
	 * @param bundleName
	 * @param loader
	 * @param locale
	 * @return
	 */
	public ResourceBundle getBundle(String bundleName, ClassLoader loader, Locale locale) {
		if (bundle == null) {
			try {
				bundle = GagoyleResourceBundle.getDefaultBundle(bundleName, loader, locale);
			} catch (IllegalAccessException | InstantiationException | IOException e) {
				throw new RuntimeException(e);
			}
		}
		return bundle;
	}
}
