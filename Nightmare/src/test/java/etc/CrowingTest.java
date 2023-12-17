/**
 * 
 */
package etc;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import com.kyj.fx.nightmare.comm.PDFUtil;

/**
 * 
 */
public class CrowingTest {

	/**
	 *  페이지를 파싱하고 데이터를 객체에 보관
	 */
	static class Page {
		/**
		 *  URL
		 */
		String url;
		/**
		 *  [X]화. 해당 정보를 파일명으로 사용하기 위함.
		 */
		String name;

		public Page(String url, String name) {
			super();
			this.url = url;
			this.name = name;
		}

		@Override
		public String toString() {
			return "Page [url=" + url + ", name=" + name + "]";
		}

	}

	/**
	 * 파싱된 결과를 확인하기 위함.
	 */
	@Test
	public void listPagesTest() {
		listPages().forEach(System.out::println);
	}

	/**
	 * 페이지 목록을 리턴한다.
	 * @return
	 */
	public List<Page> listPages() {

		try {
			// URL of the novel's webpage
			String url = "https://newtoki.help/book/episode/20184";

			// Connect to the URL and parse the HTML
			Document document = Jsoup.connect(url).get();

			// Select the HTML element that contains the novel content
			// This might be a <div>, <p>, or other tag, depending on the website's
			// structure
			Elements select = document.select("#content_wrap > div.toon_index ul li a");

			return select.stream().map(p -> {
				List<TextNode> textNodes = p.textNodes();
				if (textNodes.isEmpty())
					return null;
				TextNode textNode = textNodes.get(0);
				System.out.println(textNode.text() + "\t" + p.attr("href"));
				return new Page(p.attr("href"), textNode.text());
			}).collect(Collectors.toList());

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 페이지를 파싱해서 리스트를 추출하고, 각 차수를 파일에 저장한다 
	 */
	@Test
	public void get() {

		List<Page> listPages = listPages();
		listPages.forEach(p -> {
			try {
				// URL of the novel's webpage
				String url = p.url;

				// Connect to the URL and parse the HTML
				Document document = Jsoup.connect(url).get();

				// Select the HTML element that contains the novel content
				// This might be a <div>, <p>, or other tag, depending on the website's
				// structure
				Elements select = document.select("#bo_v p");

				List<String> collect = select.stream().map(cont -> {
					return cont.text();
				}).collect(Collectors.toList());
				
				Charset defaultCharset = Charset.defaultCharset();
				Files.write(Paths.get("할배무사와 지존손녀\\"+ p.name + ".txt"), collect, 
						defaultCharset, StandardOpenOption.WRITE,
						StandardOpenOption.CREATE_NEW);

				System.out.println(p.name);
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}
	
	/**
	 * 파일로 만들어진 txt를 pdf에 저장하기 위한 처리
	 * @throws IOException
	 */
	@Ignore
	@Test
	public void createPdfFileTest() throws IOException {
		
		File dir = new File("할배무사와 지존손녀");
		File[] array = Stream.of(dir.listFiles())
				.sorted((f1, f2) -> {
				    int num1 = Integer.parseInt(f1.getName().replaceAll("\\D+", ""));
				    int num2 = Integer.parseInt(f2.getName().replaceAll("\\D+", ""));
				    return Integer.compare(num1, num2);
				})
				/*.limit(15)*/.toArray(File[]::new);
		System.out.println(array);
		
		PDFUtil.createNewFile(new File(dir.getName() + ".pdf"), array);
		
	}
}
