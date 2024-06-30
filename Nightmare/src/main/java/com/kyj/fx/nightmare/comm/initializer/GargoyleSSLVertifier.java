/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.functions
 *	작성일   : 2018. 5. 13.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.nightmare.comm.initializer;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.comm.ValueUtil;

/**
 * @author KYJ
 *
 */
public class GargoyleSSLVertifier {

	private static Logger LOGGER = LoggerFactory.getLogger("GargoyleSSLVertifier");

	private SSLContext ctx;

	public void setup() {
		try {
			ctx = SSLContext.getInstance("TLS");
			ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() }, new SecureRandom());
			SSLContext.setDefault(ctx);
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}

	private static GargoyleSSLVertifier gargoyleSSLVertifier = new GargoyleSSLVertifier();
	static {
		gargoyleSSLVertifier.setup();
	}

	public static final SSLContext defaultContext() {
		return gargoyleSSLVertifier.getSSLContext();
	}

	public SSLContext getSSLContext() {
		return ctx;
	}

	/**
	 *
	 * SSL 통신 인증
	 *
	 * @author KYJ
	 *
	 */
	private static class DefaultTrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			// LOGGER.debug("######################");
			// LOGGER.debug("checkClientTrusted");
			LOGGER.debug("client {} " , arg1);
			// LOGGER.debug("######################");
		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			 LOGGER.debug("checkServerTrusted {} ", arg1);
//			 for(X509Certificate xc : arg0)
//			 {
//				 xc.checkValidity();
//				 LOGGER.debug("sv {} {}", xc.getSigAlgName(), xc.getSubjectX500Principal().getName());	 
//			 }
//			if("UNKNOWN".equals(arg1))
//				throw new CertificateException(arg1);
//			boolean present = Stream.of(arg0).filter(v -> {
//
//				switch (v.getSigAlgName()) {
//				case "SHA256withRSA":
//					return true;
//				case "SHA384withECDSA":
//					return true;
//				case "SHA384withRSA":
//					return true;
//				case "SHA1withRSA":
//					return true;
//				}
//
//				return false;
//			}).findFirst().isPresent();
//
//			if (!present) {
//				LOGGER.debug("Can't not found Truested Algorisms ");
//				Stream.of(arg0).forEach(v -> LOGGER.warn(v.getSigAlgName()));
//				throw new CertificateException();
//			}
		

			// LOGGER.debug("########################################################################################");
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}

}
