package com.kyj.fx.nightmare.comm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

public class PDFUtil {
	public static final String DEFAULT_FONT_SIMPLE = "NANUMBARUNGOTHIC";
	public static final String DEFAULT_FONT = DEFAULT_FONT_SIMPLE + ".TTF";
	public static final String FONTS_NANUMBARUNGOTHIC_TTF = "fonts/" + DEFAULT_FONT;

	/**
	 * 기본 폰트 리턴.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 23.
	 * @return
	 * @throws IOException
	 */
	public static PDType0Font getFont(PDDocument doc) throws IOException {
		// GraphicsEnvironment localGraphicsEnvironment =
		// GraphicsEnvironment.getLocalGraphicsEnvironment();
		// Font[] allFonts = localGraphicsEnvironment.getAllFonts();
		InputStream resourceAsStream = ClassLoader.getSystemClassLoader()
				.getResourceAsStream(FONTS_NANUMBARUNGOTHIC_TTF);
		PDType0Font font = PDType0Font.load(doc, resourceAsStream, true);
		return font;
	}

	public static PDType0Font getFont(PDDocument doc, URL fontURL) throws IOException {
		PDType0Font font = PDType0Font.load(doc, fontURL.openStream());
		return font;
	}

	public static PDType0Font getFont(PDDocument doc, String windowFontName) throws IOException {
		String fontPath = "C:\\Windows\\Fonts\\" + windowFontName; // 예시로 '맑은 고딕' 폰트 사용
		File file = new File(fontPath);
		boolean exists = file.exists();
		PDType0Font font = PDType0Font.load(doc, file);
		return font;
	}

	/**
	 * @param out
	 * @param targets
	 * @throws IOException
	 */
	public static void createNewFile(File out, File... targets) throws IOException {
		try (PDDocument doc = new PDDocument()) {
			PDType0Font font = getFont(doc);

			PDPage page = new PDPage();
			doc.addPage(page);
			PDPageContentStream contents = new PDPageContentStream(doc, page);
			contents.beginText();
			contents.setFont(font, 8);
			contents.newLineAtOffset(50, 750);

			int lineCount = 0;
			for (File file : targets) {
				List<String> lines = readAllLine(file);

				for (String line : lines) {
					writeText(font, contents, line);
					contents.newLineAtOffset(0, -15);
					lineCount++;

					if (lineCount >= 43) {
						contents.endText();
						contents.close();
						page = new PDPage();
						doc.addPage(page);
						contents = new PDPageContentStream(doc, page);
						contents.beginText();
						contents.setFont(font, 8);
						contents.newLineAtOffset(50, 750);
						lineCount = 0;
					}
				}
			}

			contents.endText();
			contents.close();
			doc.save(out);
		}
	}

	static List<String> readAllLine(File f) throws FileNotFoundException {
		ArrayList<String> arrayList = new ArrayList<String>(50);
		arrayList.add("######### " + f.getName() + " ############\n");
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)))) {

			String readLine;
			while ((readLine = br.readLine()) != null) {
				int length = readLine.length();

				if (length < 80) {
					arrayList.add(readLine);
					continue;
				}
				
				arrayList.add(readLine.substring(0, 80));
				arrayList.add(readLine.substring(80, length));
//				int page = length / 80 + (length % 80 > 0 ? 1 : 0);
//
//				int start = 0;
//				for (int i = 0; i < page; i++) {
//
//					if (page == i) {
//						arrayList.add(readLine.substring(start, length));
//					} else {
//						arrayList.add(readLine.substring(start, (start + 80)));
//					}
//
//					start += (page * 80);
//				}

			}

		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arrayList;

	}

	private static void writeText(PDType0Font font, PDPageContentStream contents, String line) throws IOException {
//		System.out.println(line);
		StringBuilder filteredText = new StringBuilder();
		byte[] encode;
		try {
			for (char ch : line.toCharArray()) {
				try {
					encode = font.encode(String.valueOf(ch));
					filteredText.append(ch);
				} catch (Exception ex) {
				}
			}
			contents.showText(filteredText.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
