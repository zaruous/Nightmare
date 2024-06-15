/********************************
 *	프로젝트 : Nightmare
 *	패키지   : chat.rest.api.service.impl
 *	작성일   : 2024. 6. 15.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package chat.rest.api.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.kyj.fx.nightmare.actions.ai.SpeechResponseModelDVO;
import com.kyj.fx.nightmare.actions.ai.SpeechToTextGptServiceImpl;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class SpeechToTextGptServiceTest {
	SpeechToTextGptServiceImpl service;

	@Before
	public void init() throws Exception {
		service = new SpeechToTextGptServiceImpl();
	}

	/**
	 * Test method for
	 * {@link chat.rest.api.service.impl.SpeechToTextGptService#send(java.io.File)}.
	 * 
	 * @throws Exception
	 */
	@Ignore
	@Test
	public void testSendFile() throws Exception {

		ArrayList<File> arrayList = new ArrayList<File>();
		File dir = new File("C:\\Users\\KYJ\\git\\chat-rest-api\\app\\tmp");
		java.nio.file.Path path = dir.toPath();
		Files.walkFileTree(path, new SimpleFileVisitor<>() {

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				File file2 = file.toFile();
				if (file2.length() < 20_000_000L) {
					arrayList.add(file2);
				}
				return super.visitFile(file, attrs);
			}

		});

		arrayList.forEach(f -> {
			try {
				SpeechResponseModelDVO send = service.send(f);
				System.out.println(f.getName());
				System.out.println(send);
				System.out.println("##############################################");
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		//
		// System.out.println(send);
		// ResponseModelDVO fromGtpResultMessage =
		// ResponseModelDVO.fromGtpResultMessage(send);
		// fromGtpResultMessage.getChoices().forEach(a -> {
		// System.out.println(a.getMessage().getContent());
		// });
	}

}
