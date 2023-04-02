package com.kyj.fx.nightmare.comm;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author KYJ
 *
 */
public class XmlFileContentConversion extends AbstractStringFileContentConversion {

	private ByteArrayOutputStream out;

	/**
	 * @param size
	 *            buffer size
	 */
	public XmlFileContentConversion(int size) {
		out = new ByteArrayOutputStream(size);
	}

	public XmlFileContentConversion() {
		out = new ByteArrayOutputStream();
	}

	/*
	 * @Deprecated this class not used. <br/>
	 * 
	 */
	@Deprecated
	@Override
	public void out(OutputStream out) {
		// Nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kyj.fx.voeditor.visual.framework.file.conversion.
	 * AbstractFileContentConversion#getOut()
	 */
	@Override
	public OutputStream getOut() {
		return this.out;
	}

	/**
	 * 데이터 변환 처리 <br/>
	 * 
	 * 
	 * 
	 * 2021-05-14 특별히 Dom을 파싱할 이유가 없으므로 로직 변경. <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 6.
	 * @return String 변환된 데이터
	 * @throws Exception
	 */
	@Override
	public String conversion() throws Exception {
		String str = null;

//		org.w3c.dom.Document doc = null;
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//		InputSource source = null;
		try (InputStream in = getIn()) {
			str = ValueUtil.toString(in , getEncoding());
//			source = new InputSource(str);
//			
//			DocumentBuilder newDocumentBuilder = factory.newDocumentBuilder();
//			doc = newDocumentBuilder.parse(source);
//			XMLUtils.print(doc, out);
//			
//			str = out.toString(StandardCharsets.UTF_8.name());
		}
		/* catch(org.xml.sax.SAXParseException e) { return str; } */ 
		catch(Exception e) {return str;}

		return str;
	}
}
