/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.nashorn
 *	작성일   : 2019. 2. 12.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.comm.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.Function;

import org.apache.http.client.CredentialsProvider;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.QName;
import org.dom4j.VisitorSupport;
import org.dom4j.tree.DefaultElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.comm.ValueUtil;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class DmiService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DmiService.class);

	private String url;
	private Map<String, Function<String[], Void>> functions = new HashMap<String, Function<String[], Void>>();
	CredentialsProvider credential;

	public DmiService(String url) throws Exception {
		this(null, url);
	}

	private Document domEndPoint;

	public DmiService(String sXml, String url) throws Exception {
		LOGGER.debug("new instance DmiService xsml : {}  url : {}", sXml, url);
		this.url = url;

//		domEndPoint = newDOM(sXml, this.url);
//
//		List<Element> serviceList = domEndPoint
//				.selectNodes("//EndpointDefinitions/Endpoints/Endpoint[not(@Service = preceding-sibling::Endpoint/@Service)]");
//		for (int i = 0; i < serviceList.size(); i++) {
//
//			Element element = serviceList.get(i);
//			Service service = new Service();
//
//			service.name = element.attribute("Service").getText();
//			service.url = domEndPoint.selectSingleNode("//EndpointDefinitions/WSDLS/WSDL[@id='" + element.attribute("WSDL") + "']/@URL")
//					.getText();
//			service.wsdl = null;
//			List<Element> functionList = domEndPoint
//					.selectNodes("//EndpointDefinitions/Endpoints/Endpoint[@Service='" + element.attribute("Service") + "']");
//			for (int j = 0; j < functionList.size(); j++) {
//				String sName = functionList.get(j).attribute("Operation").getText();
//				Service sFunction = service.setArgus(sName, functionList.get(j).attribute("Port").getText(), new String[] {});
//				functions.put(sName, sFunction);
//			}
//		}
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 2. 15.
	 * @return
	 */
	public Document getEndPoint() {
		return this.domEndPoint;
	}

	class Service implements Function<String[], Void> {

		String name;
		String url;
		String wsdl;
		String[] args;

		public Service() {
			super();
		}

		public Service(String name, String url, String[] args) {
			super();
			this.name = name;
			this.url = url;
			this.args = args;
		}

		@Override
		public Void apply(String[] t) {
			// TODO Auto-generated method stub
			return null;
		}

		public Service setArgus(String name, String port, String[] args) {
			return new Service(name, port, args);
		}
	}

	/**
	 * @author KYJ (zaruous@naver.com)
	 *
	 */
	private final class AttrHelper extends VisitorSupport {

		HashMap<String, String> attr = new HashMap<String, String>();

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.dom4j.VisitorSupport#visit(org.dom4j.Attribute)
		 */
		@Override
		public void visit(Attribute node) {
			super.visit(node);
			attr.put(node.getName(), node.getValue());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.dom4j.VisitorSupport#visit(org.dom4j.Namespace)
		 */
		@Override
		public void visit(Namespace namespace) {
			super.visit(namespace);
			// System.out.println(namespace.getName() + " !!!!!!!!!!!!! " + namespace.getPrefix() + "~~~~~~~~~~" + namespace);
		}

		public String getAttribute(String key) {
			return attr.get(key);
		}
	}

	public Object execute(String functionName, String portName, String... args) throws Exception {
		return execute(functionName, portName, Arrays.asList(args));
	}

	public Object execute(String functionName, String portName, List<String> args) throws Exception {

		LOGGER.debug("execute webservice : function Name : {} , portName : {} , args : {}", functionName, portName, args);
		// MSXML2XmlHttp6_0 http = new MSXML2XmlHttp6_0();
		// http.url = url;
		// http.send("");
		// String xmlString = http.responseText;

		Document domDocument = newDOM(null, url);

		Node ndPort = domDocument.selectSingleNode("//wsdl:port[@name='" + portName + "']");
		if (ndPort == null)
			ndPort = domDocument.selectSingleNode("//port[@name='" + portName + "']");

		AttrHelper ndPortAttr = new AttrHelper();
		ndPort.accept(ndPortAttr);

		String attribute = ndPortAttr.getAttribute("binding");
		Node ndBinding = domDocument.selectSingleNode("//wsdl:binding[@name='" + getBaseName(attribute) + "']");

		String sSoapAction = ndBinding.selectSingleNode("wsdl:operation[@name='" + functionName + "']/soap:operation/@soapAction")
				.getText();
		String sEncodingStyle = (ndBinding.selectSingleNode("wsdl:operation[@name='" + functionName + "']/soap:operation/@style") != null
				? ndBinding.selectSingleNode("wsdl:operation[@name='" + functionName + "']/soap:operation/@style").getText()
				: ndBinding.selectSingleNode("soap:binding/@style").getText());
		Node ndOperation = domDocument
				.selectSingleNode("//wsdl:portType[@name='" + portName + "']/wsdl:operation[@name='" + functionName + "']");

		domDocument.accept(ndPortAttr);
		String sTargetNamespace = (ndBinding
				.selectSingleNode("wsdl:operation[@name='" + functionName + "']/wsdl:input/soap:body/@namespace") != null
						? ndBinding.selectSingleNode("wsdl:operation[@name='" + functionName + "']/wsdl:input/soap:body/@namespace")
								.getText()
						: ndPortAttr.getAttribute("targetNamespace"));
		// var sTargetNamespace =
		// (ndBinding.selectSingleNode("wsdl:operation[@name='" + functionName +
		// "']/wsdl:input/soap:body/@namespace") != null
		// ? ndBinding.selectSingleNode("wsdl:operation[@name='" + functionName
		// + "']/wsdl:input/soap:body/@namespace").value :
		// domWSDL.documentElement.getAttribute

		String sXML = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body>";
		sXML += "<" + functionName + " xmlns=\"" + sTargetNamespace + "\">";

		Node ndMessageIn = domDocument.selectSingleNode(
				"//wsdl:message[@name='" + getBaseName(ndOperation.selectSingleNode("wsdl:input/@message").getText()) + "']");
		if (sEncodingStyle.equals("document")) {
			List<Node> ndPartList = domDocument.selectNodes(
					"//wsdl:types/s:schema/s:element[@name='" + getBaseName(ndMessageIn.selectSingleNode("wsdl:part/@element").getText())
							+ "']/s:complexType/s:sequence/s:element/@name");
			for (int i = 0; i < ndPartList.size(); i++) {
				Node node = ndPartList.get(i);
				sXML += "<" + node.getText() + " xmlns=\"" + sTargetNamespace + "\">" + args.get(i) + "</" + node.getText() + ">";
			}

		} else {
			List<Node> ndPartList = ndMessageIn.selectNodes("wsdl:part");
			for (int i = 0; i < ndPartList.size(); i++) {
				DefaultElement node = (DefaultElement) ndPartList.get(i);
				// node.accept(ndPortAttr);
				sXML += "<" + node.attribute("name").getStringValue() + " xmlns=\"\">" + args.get(i) + "</"
						+ node.attribute("name").getStringValue() + ">";
			}
		}
		sXML += "</" + functionName + "></soap:Body></soap:Envelope>";

		Document domExecute = newDOM(sXML);
		Document domReturn = null;
		String asXML = "";
		try {
			asXML = domExecute.asXML();
			domReturn = doPost(url, sSoapAction, asXML);
		} catch (Exception e) {
			throw new RuntimeException("Unable to load the response from the server. " + "The response was: \n" + e.getMessage());
		}
		// domReturn.setEntityResolver(new EntityResolver() {
		//
		// @Override
		// public InputSource resolveEntity(String publicId, String systemId)
		// throws SAXException, IOException {
		// // TODO Auto-generated method stub
		// return null;
		// }
		// });
		// Namespace aeServiceNs =
		// Namespace aeServiceNs =
		// Namespace aeServiceNs = ;
		// Namespace aeServiceNs = ;
		// Namespace aeServiceNs =

		// domReturn.setProperty("SelectionNamespaces", );
		// domReturn.addElement(new QName("SelectionNamespaces", new
		// Namespace("tns", sTargetNamespace)));
		// List attributes = domReturn.getRootElement().attributes();

		// Namespace namespace = DefaultNamespace.get("tns");
		// DefaultNamespace.get(uri)
		// domReturn.getRootElement().addElement(QName.get("tns", namespace));
		// domReturn.addElement(QName.get("Body", soapNamespace));
		// Namespace tnsNamespace = new Namespace("soap",sTargetNamespace);

		Namespace ns = new Namespace("tns", "http://www.w3.org/2003/05/soap-envelope/");
		domReturn.getRootElement().add(ns);
		// domReturn.getRootElement().addElement(QName.get("soap",
		// "http://schemas.xmlsoap.org/soap/envelope/"));
		// domReturn.getRootElement().addElement(QName.get("tns",
		// sTargetNamespace));
		domReturn.accept(ndPortAttr);
		// domReturn.getRootElement().addElement(QName.get("tns",
		// tnsNamespace));
		// domReturn.getRootElement().addAttribute("tns", sTargetNamespace);
		// domReturn.getRootElement().addAttribute("SOAP-ENV",
		// "http://schemas.xmlsoap.org/soap/envelope/");
		// domReturn.getRootElement().addAttribute("soap",
		// "http://schemas.xmlsoap.org/soap/envelope/");
		// domReturn.getRootElement().addAttribute("soapenc",
		// "http://schemas.xmlsoap.org/soap/encoding/");
		// domReturn.getRootElement().addAttribute("xsi",
		// "http://www.w3.org/2001/XMLSchema-instance");
		// domReturn.getRootElement().addAttribute("xsd",
		// "http://www.w3.org/2001/XMLSchema");
		// domReturn.getRootElement().addElement(new QName("SOAP-ENV", new
		// Namespace("soap",
		// "http://schemas.xmlsoap.org/soap/envelope/")));
		// domReturn.addElement(new QName("SelectionNamespaces", new
		// Namespace("soapenc", "http://schemas.xmlsoap.org/soap/encoding/")));
		// domReturn.addElement(new QName("SelectionNamespaces", new
		// Namespace("xsi", "http://www.w3.org/2001/XMLSchema-instance")));
		// domReturn.addElement(new QName("SelectionNamespaces", new
		// Namespace("xsd", "http://www.w3.org/2001/XMLSchema")));

		// .addAttribute("objectType", "endpoint.JMSPublisher")
		// .addAttribute("name", "Pub1EndPoint");
		// check for errors

		Node ndError = null;
		try {

			ndError = domReturn.selectSingleNode("//soap:Body/soap:Fault");

			if (ndError == null) {
				return extracted(domReturn);
				// LOGGER.error(ValueUtil.toString(e));
			}
		} catch (Exception e) {
			try {
				Node ndReturn = domReturn.selectSingleNode("//SOAP-ENV:Body/*");
				return ndReturn.asXML();
			} catch (Exception e2) {
				LOGGER.error(ValueUtil.toString(e));
			}
			throw e;
		}

		try {
			if (ndError == null) {
				// return results

				Node ndMessageOut = domDocument.selectSingleNode(
						"//wsdl:message[@name='" + getBaseName(ndOperation.selectSingleNode("wsdl:output/@message").getText()) + "']");
				Node ndReturn = domReturn.selectSingleNode("//soap:Body/*");
				// Object oReturn = new Object();
				Object oReturn; // = new HashMap<String, Object>();
				if (!sEncodingStyle.equals("document")) {
					List<Node> ndPartOutList = ndMessageOut.selectNodes("wsdl:part");
					if (ndPartOutList.size() == 1) {
						ndPartOutList.get(0).accept(ndPortAttr);

						Node ndOut = ndReturn.selectSingleNode(ndPortAttr.getAttribute("name"));
						oReturn = decodeType(getBaseName(ndPortAttr.getAttribute("type")), ndOut);
					} else {
						HashMap<String, Object> m = new HashMap<String, Object>();
						for (int i = 0; i < ndPartOutList.size(); i++) {
							Node node = ndPartOutList.get(i);
							node.accept(ndPortAttr);
							Node ndOut = ndReturn.selectSingleNode(ndPortAttr.getAttribute("name"));
							m.put(ndPortAttr.getAttribute("name"), decodeType(getBaseName(ndPortAttr.getAttribute("type")), ndOut));
						}
						oReturn = m;
					}
				} else {
					List<Node> ndPartOutList = domDocument.selectNodes("//wsdl:types/s:schema/s:element[@name='"
							+ getBaseName(ndMessageOut.selectSingleNode("wsdl:part/@element").getText())
							+ "']/s:complexType/s:sequence/s:element");
					if (ndPartOutList.size() == 1) {

						oReturn = extracted(ndReturn, ndPartOutList);
						//
						// Element node = ndPartOutList.get(0);
						//
						// String text = node.attribute("name").getText();
						// // Node ndOut = ndReturn.selectSingleNode("tns:" +
						// // text);
						// Node ndOut = ndReturn.selectSingleNode(text);
						//
						// String text2 = node.attribute("type").getText();
						// oReturn = decodeType(getBaseName(text2), ndOut);
					} else {
						HashMap<String, Object> m = new HashMap<String, Object>();
						for (int i = 0; i < ndPartOutList.size(); i++) {
							Node node = ndPartOutList.get(i);
							node.accept(ndPortAttr);
							Node ndOut = ndReturn.selectSingleNode("tns:" + ndPortAttr.getAttribute("name"));
							// oReturn[ndPortAttr.getAttribute("name")] =
							// decodeType(getBaseName(ndPortAttr.getAttribute("type")),
							// ndOut.getText());

							m.put(ndPortAttr.getAttribute("name"), decodeType(getBaseName(ndPortAttr.getAttribute("type")), ndOut));
						}
						oReturn = m;
					}
				}
				return oReturn;
			} else {

				return ndError.asXML();
				// throw new RuntimeException("Parse Error");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	private Object extracted(Document domReturn) {
		// String asXML = domReturn.asXML();
		// String text = domReturn.getText();
		String stringValue = domReturn.getStringValue();
		return stringValue;
	}

	private Object extracted(Node ndReturn, List<Node> ndPartOutList) {
		Object oReturn;
		// String nodeName = ndPartOutList.get(0).attribute("name").getText();
		// Node ndOut = ndReturn.selectSingleNode("//" + nodeName);
		// Node ndOut2 = ndReturn.selectSingleNode(nodeName);
		// Node ndOut3 = ndReturn.selectSingleNode("tns:" + nodeName);
		// Node ndOut4 = ndReturn.selectSingleNode("/" + nodeName);

		DefaultElement e = (DefaultElement) ndReturn;
		List elements = e.elements();
		Node object = (Node) elements.get(0);
		// String stringValue = object.getStringValue();
		// Node selectSingleNode = object.selectSingleNode("//" + nodeName);
		// String asXML = ndReturn.asXML();
		// ndReturn.sele

		// Node ndOut = ndReturn.selectSingleNode("tns:" +
		// ndPartOutList.get(0).attribute("name").getText());
		oReturn = decodeType(getBaseName(((DefaultElement)ndPartOutList.get(0)).attribute("type").getText()), object);
		return oReturn;
	}

	public Document doPost(String url, String sSoapAction, String body) throws Exception {
		String responseText = _doPost(url, sSoapAction, body);
		return newDOM(responseText);
	}

	public String _doPost(String url, String sSoapAction, String body) throws Exception {
		LOGGER.debug(body);
		MSXML2XmlHttp6_0 http = new MSXML2XmlHttp6_0(this.credential);
		http.open("POST", url, false);
		http.setRequestHeader("SOAPAction", sSoapAction);
		http.setRequestHeader("Content-Type", "text/xml; charset=utf-8");
		http.send(body);
		
		return http.responseText;
	}

	public String doGet() throws Exception {
		// LOGGER.debug(body);
		MSXML2XmlHttp6_0 http = new MSXML2XmlHttp6_0(this.credential);
		http.open("GET", url, false);
		// http.setRequestHeader("SOAPAction", sSoapAction);
		// http.setRequestHeader("Content-Type", "text/xml; charset=utf-8");
		http.send("");
		return http.responseText;
		// return newDOM(responseText);
	}

	private Document newDOM(String sXML) throws Exception {
		// Document domDocument = DocumentHelper.parseText(sXML);
		// return domDocument;
		return newDOM(sXML, null);
	}

	private Document newDOM(String sXML, String url) throws Exception {
		return newDOM(sXML, url, null);
	}

	// cache.
	// private static Map<String, String> cacheMap = new LinkedHashMap<String, String>() {
	// private int MAX_ENTREIS = 4;
	// /**
	// * @최초생성일 2021. 1. 11.
	// */
	// private static final long serialVersionUID = -1287866752612095441L;
	//
	// /* (non-Javadoc)
	// * @see java.util.LinkedHashMap#removeEldestEntry(java.util.Map.Entry)
	// */
	// @Override
	// protected boolean removeEldestEntry(Entry<String, String> eldest) {
	// return this.size() == MAX_ENTREIS ? true : false;
	// // return super.removeEldestEntry(eldest);
	// }
	//
	// };

	// private String xml;
	private Document newDOM(String sXML, String url, String namespace) throws Exception {
		String xml = null;
		{
			if (url != null) {
				MSXML2XmlHttp6_0 http = new MSXML2XmlHttp6_0(this.credential);
				http.open("GET", url, false);

				// http.setRequestHeader("SOAPAction", sSoapAction);
				// http.setRequestHeader("Content-Type", "text/xml; charset=utf-8");
				http.send("");
				xml = http.getResponseText();
			} else
				xml = sXML;
		}

		// LOGGER.debug(xml);
		Document domDocument = parseText(xml);// DocumentHelper.parseText(xml);
		// cacheMap.put(url, xml);
		if (namespace != null) {
			Namespace aeServiceNs = new Namespace("SelectionNamespaces", namespace);
			domDocument.addElement(new QName("jmsProducer", aeServiceNs));
		}

		return domDocument;
	}

	private static String getEncoding(String text) {
		String result = null;

		if (text == null)
			return "UTF-8";

		String xml = text.trim();

		if (xml.startsWith("<?xml")) {
			int end = xml.indexOf("?>");
			String sub = xml.substring(0, end);
			StringTokenizer tokens = new StringTokenizer(sub, " =\"\'");

			while (tokens.hasMoreTokens()) {
				String token = tokens.nextToken();

				if ("encoding".equals(token)) {
					if (tokens.hasMoreTokens()) {
						result = tokens.nextToken();
					}

					break;
				}
			}
		}

		return result;
	}

	public static Document parseText(String text) throws Exception {
		Document result = null;

		// SAXReader reader = new SAXReader();
		// DocumentFactory documentFactory = reader.getDocumentFactory();
		// Namespace createNamespace = documentFactory.createNamespace("sp", "http://schemas.microsoft.com/sharepoint/soap/");
		// reader.na
		// DefaultNamespace soapNamespace = new DefaultNamespace("tns",
		// "http://www.w3.org/2003/05/soap-envelope");
		//// Namespace soapNamespace = new Namespace("soap",
		//// "http://www.w3.org/2003/05/soap-envelope");
		// domDocument.getRootElement().addElement(QName.get("tns",
		// soapNamespace));

		// reader.getDocumentFactory().getXPathNamespaceURIs().put("soap",
		// "SOAP-ENV:");
		// HashMap m = new HashMap();
		// m.put("tns", "http://www.w3.org/2003/05/soap-envelope/");

		// DOMDocumentFactory documentFactory = new DOMDocumentFactory();
		// reader.setDocumentFactory(documentFactory);
		// reader.getDocumentFactory().setXPathNamespaceURIs(m);
		// documentFactory.setXPathNamespaceURIs(m);
		// reader.setProperty(, value);
		// reader.setProperty("SelectionNamespaces",
		// "xmlns='http://www.w3.org/2003/05/soap-envelope/'");
		// String encoding = getEncoding(text);

		// InputSource source = new InputSource(new StringReader(text));
		// source.setEncoding(encoding);
		// result = reader.read(text);
		return XMLUtils.load(text);

		// if the XML parser doesn't provide a way to retrieve the encoding,
		// specify it manually
		// if (result.getXMLEncoding() == null) {
		// result.setXMLEncoding(encoding);
		// }

		// Map hashMap = reader.getDocumentFactory().getXPathNamespaceURIs();
		// hashMap.put("soap", "http://www.w3.org/2003/05/soap-envelope");
		// return result;
	}

	static Map<String, Integer> schemaTypes = new HashMap<>();

	{
		schemaTypes.put("negativeInteger", 0);
		schemaTypes.put("unsignedShort", 0);
		schemaTypes.put("unsignedByte", 0);
		schemaTypes.put("unsignedLong", 0);
		schemaTypes.put("unsignedInt", 0);
		schemaTypes.put("decimal", 0);
		schemaTypes.put("boolean", 0);
		schemaTypes.put("integer", 0);
		schemaTypes.put("double", 0);
		schemaTypes.put("float", 0);
		schemaTypes.put("short", 0);
		schemaTypes.put("byte", 0);
		schemaTypes.put("long", 0);
		schemaTypes.put("int", 0);
		schemaTypes.put("QName", 1);
		schemaTypes.put("string", 1);
		schemaTypes.put("normalizedString", 2);
		schemaTypes.put("timeInstant", 3);
		schemaTypes.put("dateTime", 3);
		schemaTypes.put("date", 4);
		schemaTypes.put("time", 5);
	}

	public Object decodeType(String schemaType, Object value) {
		if (value == null) {
			return "";
		}

		Integer integer = schemaTypes.get(schemaType);
		if (integer == 0) {
			return value;
		} else if (integer == 1) {
			if (value instanceof Node)
				return ((Node) value).getStringValue();
			// else if (value instanceof Node) {
			// return ((Node) value).getStringValue();
			// }
			return value;
		} else if (integer == 2) {
			return value;
		} else if (integer == 3 || integer == 4) {
		} else if (integer == 5) {
		} else {
			throw (new Error("dmiService does not support " + schemaType));
		}
		return integer;
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2019. 2. 12.
	 * @param str
	 * @return
	 */
	public String getBaseName(String str) {

		if (ValueUtil.isNotEmpty(str)) {
			String[] arr = str.split(":");
			if (arr.length > 1)
				return arr[1];
		}
		return str;
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2020. 7. 30.
	 * @return
	 */
	public CredentialsProvider getCredential() {
		return credential;
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2020. 7. 30.
	 * @param credential
	 */
	public void setCredential(CredentialsProvider credential) {
		this.credential = credential;
	}
}
