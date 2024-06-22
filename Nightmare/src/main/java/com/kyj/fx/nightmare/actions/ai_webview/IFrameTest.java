/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai_webview;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 */
public class IFrameTest {
	public static void main(String[] args) {
		try {
			// 주어진 URL에서 HTML 문서를 가져옴
			String url = "https://www.index.go.kr/unity/potal/main/EachDtlPageDetail.do?idx_cd=1007";
			Document doc = Jsoup.connect(url).get();
			String baseUri = doc.baseUri();
			URI uri = URI.create(baseUri);
			String host = uri.getHost();
			String scheme = uri.getScheme();
			String base = scheme + "://" + host;
			
			// iframe 요소를 선택하고, src 속성을 가져옴
			Elements iframes = doc.select("iframe");
			for (Element iframe : iframes) {
				String iframeSrc = iframe.attr("src");
				System.out.println("Iframe src: " + iframeSrc);

				// iframe의 src URL에서 HTML 내용을 가져옴
				String iframeContent = getHtmlContent(base, iframeSrc);
				if (iframeContent != null) {
					Document iframeDoc = Jsoup.parse(iframeContent);
					String textContent = iframeDoc.text();
					System.out.println("Iframe Text Content: \n" + textContent);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 주어진 URL에서 HTML 콘텐츠를 가져오는 메서드
	private static String getHtmlContent(String base, String iframeSrc) {
		try {
			
		 URI uri = URI.create(iframeSrc);
		 URL url = null;
		 if(!uri.isAbsolute())
		 {
			 url = URI.create(base + "/" + uri.getRawPath()).toURL();
		 }
		 else
			 url = uri.toURL();
		 
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuilder content = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}

			in.close();
			return content.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

