/**
 * 
 */
package etc.tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AbstractParser;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.junit.jupiter.api.Test;

/**
 * 
 */
public class TikaTest {
	@Test
	public void csvTest() throws Exception {
//		String csvFilePath = "excel.csv";
//		String xlsxFilePath = "excel.xlsx";
//		String pdfFilePath = "path/to/your/file.pdf";

		TikaFileParser parser = new TikaFileParser();
		parser.parseFile(new File("C:\\Users\\KYJ\\git\\Nightmare\\Nightmare\\src\\test\\java\\etc\\tika\\excel.csv"));
		parser.parseFile(new File("C:\\Users\\KYJ\\git\\Nightmare\\Nightmare\\src\\test\\java\\etc\\tika\\excel.xlsx"));
		parser.parseFile(new File("C:\\Users\\KYJ\\git\\Nightmare\\Nightmare\\src\\test\\java\\etc\\tika\\excel.pdf"));
//		parser.parseFile(pdfFilePath);

	}

	public class TikaFileParser {


//		TikaConfig config;

		TikaFileParser() throws Exception {
//			config = new TikaConfig(new File("tika-config.xml"));
		}

		public void parseFile(File f) throws Exception {
//			AbstractParser parser = new EmptyParser();
			AbstractParser parser = new AutoDetectParser();
//			if(f.getName().endsWith(".csv"))
//				parser = new TXTParser();
//			if(f.getName().endsWith(".xlsx"))
//				parser = new OOXMLParser();
			Metadata metadata = new Metadata();
			BodyContentHandler handler = new BodyContentHandler();
			ParseContext parseContext = new ParseContext();
			
			try (InputStream input = new FileInputStream(f)) {
				// 파일 형식을 자동으로 감지하고 파싱
				
				parser.parse(input, handler, metadata, parseContext);
				
				// 파일 내용 출력
				System.out.println("파일 내용:\n" + handler.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
