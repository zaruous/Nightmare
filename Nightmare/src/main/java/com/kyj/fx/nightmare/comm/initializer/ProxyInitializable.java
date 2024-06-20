/********************************
 *	프로젝트 : Nightmare
 *	패키지   : com.kyj.fx.nightmare.comm.initializer
 *	작성일   : 2024. 06. 20.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.nightmare.comm.initializer;

import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.comm.ResourceLoader;



/**
 * @author KYJ
 *
 */
public class ProxyInitializable implements Initializable {

	private static Logger LOGGER = LoggerFactory.getLogger(ProxyInitializable.class);

	@Override
	public void initialize() throws Exception {

		boolean useProxy = isUseProxy();
		if (useProxy) {
			LOGGER.debug(" Use Proxy Settings");
			String httpHost = getHttpHost();
			String httpPort = String.valueOf(getHttpProxyPort());
			String httpsHost = getHttpsProxyHost();
			String httpsPort = String.valueOf(getHttpsProxyPort());

			LOGGER.debug("http host :{} port :{} , https host : {}, port : {}", httpHost, httpPort, httpsHost, httpsPort);

			httpSetup(httpHost, httpPort);
			httpsSetup(httpsHost, httpsPort);
		} else {
			LOGGER.debug("No Use Proxy Settings");
		}

	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 26. 
	 * @return
	 */
	public static boolean isUseProxy() {
		return "Y".equals(ResourceLoader.getInstance().get(ResourceLoader.USE_PROXY_YN,"N"));
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 26.
	 * @param httpHost
	 * @param httpPort
	 */
	protected void httpSetup(String httpHost, String httpPort) {
		LOGGER.debug("setup http host :{} port :{} ", httpHost, httpPort);
		System.setProperty(ResourceLoader.HTTP_PROXY_HOST, httpHost);
		System.setProperty(ResourceLoader.HTTP_PROXY_PORT, httpPort);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 26.
	 * @param httpsHost
	 * @param httpsPort
	 */
	protected void httpsSetup(String httpsHost, String httpsPort) {
		LOGGER.debug("setup https host :{} port :{} ", httpsHost, httpsPort);
		System.setProperty(ResourceLoader.HTTP_PROXY_HOST, httpsHost);
		System.setProperty(ResourceLoader.HTTP_PROXY_PORT, httpsPort);
	}

	/**
	 * htpps 프록시 포트 리턴
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 11. 17.
	 * @return
	 */
	public static final int getHttpsProxyPort() {
		return Integer.parseInt(ResourceLoader.getInstance().get(ResourceLoader.HTTP_PROXY_PORT), 10); 
	}

	/**
	 * https 프록시 호스트 리턴
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 11. 17.
	 * @return
	 */
	public static final String getHttpsProxyHost() {
		return ResourceLoader.getInstance().get(ResourceLoader.HTTP_PROXY_HOST);
	}

	/**
	 * Proxy port 리턴
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 11. 17.
	 * @return
	 */
	public static final int getHttpProxyPort() {
		return Integer.parseInt(ResourceLoader.getInstance().get(ResourceLoader.HTTP_PROXY_PORT), 10); 
	}

	/**
	 * Proxy Host 리턴
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 11. 17.
	 * @return
	 */
	public static final String getHttpHost() {
		return ResourceLoader.getInstance().get(ResourceLoader.HTTP_PROXY_HOST);
	}

}
