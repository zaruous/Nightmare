package com.kyj.fx.nightmare.comm;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class XmlW3cUtil {

//	public static void main(String[] args) throws IOException {
//		// URL이나 HTML 파일을 로드하여 Document 객체 생성
//		String url = "https://example.com";
//		Document doc = Jsoup.connect(url).get();
//
//		// 파일 작성 초기화
//		try (FileWriter writer = new FileWriter("output.txt")) {
//			parseElement(doc.body(), writer);
//		}
//	}

	public static String parseElement(Element element) throws IOException {

		try (StringWriter writer = new StringWriter()) {
			parseElement(element, writer);
			return writer.toString();
		}
	}

	private static void parseElement(Element element, Writer writer) throws IOException {
		if (element.tagName().toLowerCase().equals("table")) {
			// 테이블을 CSV 형태로 변환하여 작성
			writeTableAsCSV(element, writer);
		} 
		
		else {
			// 자식 요소들을 재귀적으로 탐색
			for (Element child : element.children()) {
				parseElement(child, writer);
			}
			// TextNode의 경우 텍스트를 작성
			if (element.children().isEmpty()) {
				String text = element.text();
				String nodeName = element.nodeName();
				
				if("img".equals(nodeName))
				{
					writer.write(element.attributes().getIgnoreCase("src"));
					writer.write(" \n");
				}
				
				else if(!text.isEmpty())
				{	writer.write(text.trim());
				
					if("div".equals(nodeName) || "p".equals(nodeName))
						writer.write("\n");
				}	
			}
		}
	}

	private static void writeTableAsCSV(Element table, Writer writer) throws IOException {
		Elements rows = table.select("tr");
		for (Element row : rows) {
			Elements cells = row.select("th, td");
			for (int i = 0; i < cells.size(); i++) {
				writer.write(cells.get(i).text());
				writer.write(",");
//				if (i < cells.size() - 1) {
//					writer.write(",");
//				}
			}
			writer.write("\n");
		}
	}
}
