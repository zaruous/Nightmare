/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.nashorn
 *	작성일   : 2020. 11. 18.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.nightmare.comm.service;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.function.Consumer;

import org.dom4j.Document;
import org.dom4j.Node;

import com.kyj.fx.nightmare.comm.Message;
import com.kyj.fx.nightmare.comm.ResourceLoader;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class ESig {

	private String rootUrl;
	private String pathUrl = "/ESig/ESig.asmx?wsdl";

	public ESig() {
		this.rootUrl = ResourceLoader.getInstance().get(ResourceLoader.SYNCADE_ROOT_URL);
	}
	
	public ESig(String rootUrl) {
		this.rootUrl = rootUrl;
	}

	public void login(String login, String password, String permission, String domain, String application, String entityType,
			String entityId, Consumer<String> tokenHandler) throws Exception {
		boolean tokenNeeds = tokenNeeds(domain, application, entityType, entityId);
		if (tokenNeeds) {
			String tokenXml = createToken(login, password, "", "", permission, domain, application, entityType, entityId, "");

			Document doc = XMLUtils.load(tokenXml);
			Node selectSingleNode = doc.selectSingleNode("//Token/text()");
			String stringValue = selectSingleNode.getStringValue();
			tokenHandler.accept(stringValue);
		}
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 2. 14.
	 * @param domain
	 * @param application
	 * @param entityType
	 * @param entityId
	 * @return
	 * @throws Exception
	 */
	public boolean tokenNeeds(String domain, String application, String entityType, String entityId) throws Exception {
		DmiService s = new DmiService(rootUrl + pathUrl);
		String xml = (String) s.execute("TokensNeeded", "ESigSoap", new String[] { domain, application, entityType, entityId });

		Document doc = XMLUtils.load(xml);
		Node n = doc.selectSingleNode("//ReturnCode/text()");
		return "1".equals(n.getStringValue());
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 2. 14.
	 * @param login
	 * @param password
	 * @param permission
	 * @param domain
	 * @param application
	 * @param entityType
	 * @param entityId
	 * @param commment
	 * @return Token Xml
	 * @throws Exception
	 */
	public String createToken(String login, String password, String permission, String domain, String application, String entityType,
			String entityId, String commment) throws Exception {
		return createToken(login, password, "", "", permission, domain, application, entityType, entityId, commment);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 2. 14.
	 * @param login
	 * @param password
	 * @param permission
	 * @param domain
	 * @param application
	 * @param entityType
	 * @param entityId
	 * @return Token Xml
	 * @throws Exception
	 */
	public String createToken(String login, String password, String permission, String domain, String application, String entityType,
			String entityId) throws Exception {
		return createToken(login, password, "", "", permission, domain, application, entityType, entityId, "");
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 23.
	 * @param login
	 * @param password
	 * @param verifyLogin
	 * @param verifyPasword
	 * @param permission
	 * @param domain
	 * @param application
	 * @param entityType
	 * @param entityId
	 * @param comment
	 * @return
	 * @throws Exception
	 */
	public String createToken(String login, String password, String verifyLogin, String verifyPasword, String permission, String domain,
			String application, String entityType, String entityId, String comment) throws Exception {
		DmiService s = new DmiService(rootUrl + pathUrl);

		String xml = (String) s.execute("Authenticate", "ESigSoap", new String[] { login, password, verifyLogin, verifyPasword, permission,
				domain, application, entityType, entityId, "", comment, "0" });
		return xml;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 2. 14.
	 * @param login
	 * @param password
	 * @param verifyLogin
	 * @param verifyPasword
	 * @param permission
	 * @param domain
	 * @param application
	 * @param entityType
	 * @param entityId
	 * @param entityName
	 * @param comment
	 * @return Token Xml
	 * @throws Exception
	 */
	public String createToken(String login, String password, String userToekn, String verifyLogin, String verifyPasword, String permission,
			String domain, String application, String entityType, String entityId, String entityName, String comment,
			String userTokenExpiration) throws Exception {
		DmiService s = new DmiService(rootUrl + pathUrl);
		// String _pwd = SyncadeEncryp.encryptString(password, "zeppelin");
		String xml = (String) s.execute("AuthenticateEx", "ESigSoap", new String[] { login, password, userToekn, verifyLogin, verifyPasword,
				permission, domain, application, entityType, entityId, entityName, comment, userTokenExpiration });
		return xml;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2020. 9. 2.
	 * @param login
	 * @param encrypedPwd
	 * @param verifyLogin
	 * @param encrypedVerifyPwd
	 * @param permission
	 * @param domain
	 * @param application
	 * @param entityType
	 * @param entityId
	 * @param comment
	 * @return
	 * @throws Exception
	 */
	public String createTokenEx(String login, String encrypedPwd, String verifyLogin, String encrypedVerifyPwd, String permission,
			String domain, String application, String entityType, String entityId, String comment) throws Exception {
		DmiService s = new DmiService(rootUrl + pathUrl);
		String xml = (String) s.execute("AuthenticateEx", "ESigSoap", new String[] { login, encrypedPwd, "", verifyLogin, encrypedVerifyPwd,
				permission, domain, application, entityType, entityId, "", "<![CDATA[" + comment + "]]>", "0" });
		return xml;
	}

	public String createTokenEx(String login, String encrypedPwd, String permission, String domain, String application, String entityType,
			String entityId, String comment) throws Exception {
		DmiService s = new DmiService(rootUrl + pathUrl);
		String xml = (String) s.execute("AuthenticateEx", "ESigSoap", new String[] { login, encrypedPwd, "", "", "", permission, domain,
				application, entityType, entityId, "", "<![CDATA[" + comment + "]]>", "0" });
		return xml;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2020. 9. 2.
	 * @param login
	 * @param password
	 * @param verifyLogin
	 * @param verifyPasword
	 * @param permission
	 * @param domain
	 * @param application
	 * @param entityType
	 * @param entityId
	 * @param comment
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	// public String createTokenEx(String login, byte[] password, String verifyLogin, byte[] verifyPasword, String permission, String
	// domain,
	// String application, String entityType, String entityId, String comment) throws Exception {
	//
	//
	// return createTokenEx(login, new String(a, "utf-8"), verifyLogin, new String(b, "utf-8"), permission, domain, application,
	// entityType, entityId, comment);
	// }

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 30.
	 * @param pwd
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String base64Encoder(String pwd) throws Exception {
		byte[] encode = Base64.getEncoder().encode(pwd.getBytes("utf-8"));
		return new String(encode, "utf-8");
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 30.
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	public static String syncadeEncrypt(String pwd) throws Exception {
		return SyncadeEncryp.encryptString(pwd, "zeppelin");
	}

	public static ESigParser parser() {
		return new ESigParser();
	}

	/**
	 * @author KYJ (callakrsos@naver.com)
	 *
	 */
	public static class ESigParser {

		/**
		 * @작성자 : KYJ (callakrsos@naver.com)
		 * @작성일 : 2021. 12. 6. 
		 * @param tokenXml
		 * @return
		 * @throws Exception
		 */
		public String getToken(String tokenXml) throws Exception{
			
			var doc = XMLUtils.load(tokenXml);
			var nToken = doc.selectSingleNode("//Token");
			if(nToken == null)
			{
				//%ESig_000001=SA Token이 존재하지 않습니다.
				throw new Exception(Message.getInstance().getMessage("%ESig_000001"));
			}
			return nToken.getText();
		}

	}

}
