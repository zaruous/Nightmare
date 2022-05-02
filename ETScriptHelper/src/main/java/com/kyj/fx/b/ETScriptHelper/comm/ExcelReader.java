/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.comm
 *	작성일   : 2021. 11. 26.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.comm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class ExcelReader {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelReader.class);

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 26.
	 * @param excelFile
	 * @return
	 * @throws Exception
	 */
	public static String readExcel(File excelFile) throws Exception {
		return new ExcelReader().process(excelFile);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 26.
	 * @param excelFile
	 * @return
	 * @throws Exception
	 */
	String process(File excelFile) throws Exception {
		var dir = new File("temp");
		if(!dir.exists())
			dir.mkdirs();
		File outFileName = new File(dir, DateUtil.getCurrentDateString() + ".xml");
		VbExcelReader reader = new VbExcelReader(outFileName.getAbsolutePath(), excelFile.getAbsolutePath());
		reader.convert();

		// System.out.println(Charset.defaultCharset());
		LOGGER.debug("Print error data : {}", new String(reader.errorData, "euc-kr"));
		LOGGER.debug("Print data : {}", new String(reader.outputData, "euc-kr"));

		if (!outFileName.exists())
			throw new Exception(excelFile.getName() + " Read failed.");

		return FileUtil.readToString(outFileName);
	}

	/**
	 * 64비트 처리.<br/>
	 * 
	 * @author KYJ (callakrsos@naver.com)
	 *
	 */
	class VbExcelReader {
		String vbFileName = "ExcelExtrator.vbs";
		private String excelFilePathName;
		private String outFilePathName;
		private byte[] outputData;
		private byte[] errorData;

		VbExcelReader(String outFilePathName, String excelFilePathName) {
			this.outFilePathName = outFilePathName;
			this.excelFilePathName = excelFilePathName;
		}

		public void convert() throws SQLException, IOException, InterruptedException {
			Process p = null;
			try {
				File scriptFile = new File(vbFileName);
				if(!scriptFile.exists())
					throw new FileNotFoundException("Script file does not exists.");
				
				String executorPath = scriptFile.getAbsolutePath();

				ProcessBuilder pb = new ProcessBuilder(
						new String[] { "cscript", executorPath, this.outFilePathName, this.excelFilePathName });

				Map<String, String> environment = pb.environment();
				System.out.println(environment);
				p = pb.start();

				// 스레드로 처리시켜야 결과를 동기방식으로 받을 수 있음.
				OutputHandler outputHandler = new OutputHandler(p.getInputStream());
				OutputHandler errHandler = new OutputHandler(p.getErrorStream());

				p.waitFor();

				outputHandler.join();
				errHandler.join();

				this.outputData = outputHandler.b;
				this.errorData = errHandler.b;

				// System.out.println(new String(outputData, "euc-kr"));
				// System.err.println(new String(errorData, "euc-kr"));

			} finally {
				if (p != null)
					p.destroy();
			}
		}

		/**
		 * 데이터 로딩 처리.
		 * 
		 * @author KYJ (callakrsos@naver.com)
		 *
		 */
		class OutputHandler extends Thread {
			private final StringBuilder buf = new StringBuilder();
			private InputStream in;
			private byte[] b = new byte[] {};

			OutputHandler(InputStream in) {
				this.in = in;
				setDaemon(true);
				start();
			}

			String getText() {
				synchronized (buf) {
					return buf.toString();
				}
			}

			@Override
			public void run() {
				this.b = ValueUtil.toByte(this.in);
			}
		}

	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 26.
	 * @param <T>
	 * @param <K>
	 * @param doc
	 * @param to
	 * @return
	 * @throws Exception
	 */
	public static <K> List<K> populateList(Document doc, Class<K> toClass) throws Exception {
		return populateList(doc, toClass, (colName, value) -> value);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 3.
	 * @param <K>
	 * @param doc
	 * @param toClass
	 * @param converter
	 * @return
	 * @throws Exception
	 */
	public static <K> List<K> populateList(Document doc, Class<K> toClass, Converter converter) throws Exception {
		List<Element> selectNodes = doc.selectNodes("//Data/Row");

		var list = new ArrayList<K>();
		for (Element row : selectNodes) {
			K to = toClass.getConstructor().newInstance();
			List<Element> attrs = row.elements();
			for (Element attr : attrs) {
				String name = attr.getName();
				String stringValue = attr.getStringValue();
				if (!converter.filter(name))
					continue;
				String convertValue = converter.convert(name, stringValue);
				if (convertValue == null)
					continue;

				name = ValueUtil.getIndexLowercase(name, 0);
				Field f = toClass.getDeclaredField(name);
				if (!f.canAccess(to)) {
					f.setAccessible(true);
				}
				Object object = f.get(to);
				if (object instanceof StringProperty) {
					StringProperty sp = (StringProperty) object;
					sp.set(convertValue);
				} else if (object instanceof IntegerProperty) {
					IntegerProperty ip = (IntegerProperty) object;
					ip.set(Integer.parseInt(convertValue, 10));
				}
			}
			list.add(to);
		}
		return list;
	}

	/**
	 * 엑셀에서 로딩된 XML파일로부터 데이터를 처리 <br/>
	 * 
	 * @author KYJ (callakrsos@naver.com)
	 *
	 */
	@FunctionalInterface
	public interface Converter {
		/**
		 * Data를 다른 값으로 대체하는 경우 사용 <br/>
		 * 
		 * @작성자 : KYJ (callakrsos@naver.com)
		 * @작성일 : 2021. 12. 3.
		 * @param value
		 * @return
		 */
		public String convert(String colName, String value);

		/**
		 * 특정 필드를 바인딩 처리하지 않을 경우에 사용 <br/>
		 * 
		 * @작성자 : KYJ (callakrsos@naver.com)
		 * @작성일 : 2021. 12. 3.
		 * @param colName
		 * @return
		 */
		public default boolean filter(String colName) {
			return true;
		};
	}
}
