/**
 * 
 */
package com.kyj.fx.nightmare.comm.template;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.InputSource;

import com.kyj.fx.nightmare.comm.FileUtil;
import com.kyj.fx.nightmare.comm.ValueUtil;

/**
 * @author KYJ
 *
 */
public class TemplateUtil {

	/**
	 * @param xmlFile
	 * @return
	 * @throws Exception
	 */
	public static Template GetTemplate(File xmlFile) throws Exception {
		if (xmlFile == null || xmlFile.exists())
			throw new FileNotFoundException("xmlFile cant found.");
		String xml = FileUtil.readToString(xmlFile);
		return JaxbUtils.load(xml, Template.class);
	}

	/**
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	public static Template GetTemplate(String xml) throws Exception {
		return JaxbUtils.load(xml, Template.class);
	}

	/**
	 * @param resource
	 * @return
	 * @throws Exception
	 */
	public static Template GetTemplate(URL resource) throws Exception {
		if (resource == null )
			throw new RuntimeException("resource is null");
		String xml = ValueUtil.toString(resource.openStream());
		return JaxbUtils.load(xml, Template.class);
	}

	/**
	 * @author KYJ
	 *
	 */
	public static class JaxbUtils {
		/**
		 * @작성자 : KYJ
		 * @작성일 : 2018. 5. 31.
		 * @param out
		 * @param v
		 * @return
		 * @throws JAXBException
		 * @throws TransformerFactoryConfigurationError
		 * @throws TransformerConfigurationException
		 */
		public static <T> void write(File out, T v) throws Exception {
			try (FileOutputStream stream = new FileOutputStream(out)) {
				write(stream, v);
			}
		}

		/**
		 * @작성자 : KYJ (callakrsos@naver.com)
		 * @작성일 : 2019. 11. 6.
		 * @param <T>
		 * @param out
		 * @param v
		 * @throws Exception
		 */
		public static <T> void write(OutputStream out, T v) throws Exception {
			JAXBContext context = JAXBContext.newInstance(v.getClass());

			Marshaller createMarshaller = context.createMarshaller();
			DOMResult domResult = new DOMResult();

			// createMarshaller.setProperty(name, value);
			createMarshaller.marshal(v, domResult);

			// try format xml
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			// transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			// transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
			// "2");

			StreamResult outputTarget = new StreamResult(out);
			DOMSource xmlSource = new DOMSource(domResult.getNode());
			transformer.transform(xmlSource, outputTarget);

		}

		/**
		 * @작성자 : KYJ
		 * @작성일 : 2018. 5. 31.
		 * @param stream
		 * @param requireType
		 * @throws Exception
		 */
		public static <T> T load(InputStream stream, Class<T> requireType) throws Exception {
			JAXBContext context = JAXBContext.newInstance(requireType);
			Unmarshaller um = context.createUnmarshaller();
			return (T) um.unmarshal(stream);
		}

		/**
		 * @작성자 : KYJ
		 * @작성일 : 2018. 6. 18.
		 * @param source
		 * @param requireType
		 * @return
		 * @throws Exception
		 */
		public static <T> T load(InputSource source, Class<T> requireType) throws Exception {
			JAXBContext context = JAXBContext.newInstance(requireType);
			Unmarshaller um = context.createUnmarshaller();
			return (T) um.unmarshal(source);
		}

		/**
		 * @작성자 : KYJ
		 * @작성일 : 2018. 6. 18.
		 * @param source
		 * @param requireType
		 * @return
		 * @throws Exception
		 */
		public static <T> T load(String source, Class<T> requireType) throws Exception {
			return load(new InputSource(new StringReader(source)), requireType);
		}
	}

}
