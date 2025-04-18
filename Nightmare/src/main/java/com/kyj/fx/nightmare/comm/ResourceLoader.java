/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.memory
 *	작성일   : 2018. 4. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.nightmare.comm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 사용자 정의 속성들 Writable속성
 *
 * @author KYJ
 *
 */
/**
 * @author KYJ
 *
 */
/**
 *
 * @author KYJ
 */
public class ResourceLoader {

	/**
	 * 컬럼 크기가 큰 경우 데이터 맵핑을 생략할건지 유무
	 *
	 * @최초생성일 2016. 2. 11.
	 */
	public static final String SKIP_BIG_DATA_COLUMN = "skip.big.data.column";

	public static final String USE_PROXY_YN = "use.proxy.yn";

	public static final String HTTP_PROXY_HOST = "http.proxyHost";

	public static final String HTTP_PROXY_PORT = "http.proxyPort";

	public static final String SYNCADE_ROOT_URL = "syncade.root.url";
	
	public static final String SYNCADE_RA_ROOT_URL = "syncade.ra.root.url";
	
	private static ResourceLoader loader;

	private final String FILE_NAME = "UserConf.properties";
	
	public static final String DEFAULT_LOCALE ="default.locale";
	
	public static final String FILE_ENCODING = "file.encoding";
	public static final String SUN_JNU_ENCODING = "sun.jnu.encoding";
	
	//#AI에서 음성파일 녹음시 파일 생성 여부
	public static final String AI_CREATE_WAVE_FILE_YN = "ai.create.wave.file.yn";
	public static final String AI_CREATE_WAVE_FILE_DIR = "ai.create.wave.file.dir";
	public static final String AI_CREATE_WAVE_FILE_DELETE_PERIOD_DAY = "ai.create.wave.file.delete.period.day";
	//자동으로 음성을 플레이할것인지 여부
	public static final String AI_AUTO_PLAY_SOUND_YN = "ai.auto.play.sound.yn";
	
	/*스킨 설정 프로젝트 루트 skins/디렉토리에 있는 파일명 설정*/
	public static final String DEFAULT_SKIN = "default.skin";
	/*화면 로드시 히스토리 로드할 데이터수를 리턴*/
	public static final String AI_HISTORY_FETCH_COUNT = "ai.history.fetch.count";
	
	public static final String SSL_VERIFY = "ssl.verify";
	
	public static final String TRAY_SUPPORT_YN = "tary.support.yn";
	
	/**
	 * 파이썬 실행기에서 프로그램 실행후 파일 삭제 여부 
	 */
	public static final String PYCODE_BUILDER_CLEAN_YN = "pycode.builder.clean.yn";
	/**
	 * 앱 버젼 <br/>
	 * @최초생성일 2021. 12. 8.
	 */
	public static final String APP_VERSION = "app.version";

	public static final String DEFAULT_WORKSPACE_PATH = "default.workspace.location";

	public static synchronized ResourceLoader getInstance() {
		if (loader == null) {
			loader = new ResourceLoader();
		}
		return loader;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 28.
	 * @return
	 */
	public static ResourceLoader createInstance() {
		return new ResourceLoader();
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 22.
	 * @return
	 */
	public static ResourceLoader createInstance(Properties properties) {
		return new ResourceLoader(properties);
	}

	private ResourceLoader() {

	}

	private ResourceLoader(Properties initialize) {

	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 11. 22.
	 * @param string
	 * @return
	 */
	public String get(String string) {
		return get(string, "");
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 11. 22.
	 * @param string
	 * @param defValue
	 * @return
	 */
	public String get(String string, String defValue) {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(new File(FILE_NAME)));
			String property = properties.getProperty(string);
			if(ValueUtil.isEmpty(property))
				return defValue;
			return property;
		} catch (IOException e) {
			return defValue;
		}
	}

}
