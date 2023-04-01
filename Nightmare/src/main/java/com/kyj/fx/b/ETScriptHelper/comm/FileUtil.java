/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.frame
 *	작성일   : 2021. 11. 22.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.comm;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class FileUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * str 내용을 file로 write처리함, 디폴트 인코딩 UTF-8 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 26.
	 * @param file
	 * @param str
	 * @throws IOException
	 */
	public static boolean writeFile(File file, String str) throws IOException {
		return writeFile(file, str, StandardCharsets.UTF_8);
	}

	/**
	 * str 내용을 file로 write처리함.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 6. 23.
	 * @param file
	 * @param str
	 * @param charset
	 * @throws IOException
	 */
	public static boolean writeFile(File file, String str, Charset charset) throws IOException {
		return writeFile(file, str, charset, false);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 10. 11.
	 * @param file
	 * @param str
	 * @param charset
	 * @param append
	 * @return
	 * @throws IOException
	 */
	public static boolean writeFile(File file, String str, Charset charset, boolean append) throws IOException {

		if (!file.exists()) {
			file.createNewFile();
		}

		if (!file.canWrite())
			return false;

		boolean flag = false;
		try (FileOutputStream out = new FileOutputStream(file, append)) {
			try (OutputStreamWriter writer = new OutputStreamWriter(out, charset)) {
				writer.write(str);
				writer.flush();
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 4. 19.
	 * @param is
	 * @return
	 */
	public static String readToString(InputStream is) {
		return readToString(is, "UTF-8");
	}

	/**
	 * READ TO String
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 11.
	 * @param f
	 * @param charset
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String readToString(File f, String charset) throws FileNotFoundException, IOException {
		return readToString(f, Charset.forName(charset));
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 12. 9.
	 * @param f
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String readToString(File f) throws FileNotFoundException, IOException {
		return readToString(f, "utf-8");
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 12. 9.
	 * @param f
	 *            reading target.
	 * @param charset
	 *            encoding.
	 * @param handler
	 * @return if on error return null.
	 */
	public static String readToString(File f, String charset, ExceptionHandler handler) {
		try {
			return readToString(f, Charset.forName(charset));
		} catch (IOException e) {
			handler.handle(e);
		}
		return null;
	}

	/**
	 * READ TO String
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 11.
	 * @param f
	 * @param encoding
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String readToString(File f, Charset encoding) throws FileNotFoundException, IOException {
		try (FileInputStream fis = new FileInputStream(f)) {
			return readToString(fis, encoding);
		}
	}

	/**
	 * READ TO String
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 3.
	 * @param is
	 * @param encoding
	 * @return
	 */
	public static String readToString(InputStream is, Charset encoding) {
		return readToString(is, encoding == null ? "UTF-8" : encoding.name());
	}

	/**
	 * READ TO String
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 11. 1.
	 * @param is
	 * @param encoding
	 * @return
	 */
	public static String readToString(InputStream is, String encoding) {
		String result = "";
		if (is != null) {
			BufferedReader br = null;
			StringBuffer sb = new StringBuffer();
			String temp = null;
			try {
				br = new BufferedReader(new InputStreamReader(is, encoding));
				while ((temp = br.readLine()) != null) {
					sb.append(temp).append(System.lineSeparator());
				}
			} catch (Exception e) {

				try {
					if (br != null)
						br.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} finally {
				result = sb.toString();
			}
		}
		return result;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 24.
	 * @param path
	 * @param readStream
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static File writeFile(String path, InputStream readStream) throws Exception {
		byte[] b = new byte[1024];
		File file = new File(path);
		if (!file.exists())
			file.createNewFile();
		try (FileOutputStream stream = new FileOutputStream(file, false)) {
			while (readStream.read(b) != -1) {
				stream.write(b);
			}
			stream.flush();
		}
		return file;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 30.
	 * @param f
	 */
	public static void windowOpen(File f) {
		openFile(f);
	}

	/**
	 * openFile
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 2. 18.
	 * @param file
	 * @return
	 */
	public static boolean openFile(File file) {
		return openFile(file, err -> {
			LOGGER.error(ValueUtil.toString(err));
		});
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 10.
	 * @param file
	 * @param errorHandler
	 * @return
	 */
	public static boolean openFile(File file, Consumer<Exception> errorHandler) {
		return fileOpenAction(file, errorHandler);
	}

	/**
	 * @최초생성일 2017. 10. 10.
	 */
	private static BiFunction<File, Consumer<Exception>, Boolean> openAction = (file, errhandler) -> {
		boolean isSuccess = false;
		try {
			Desktop.getDesktop().open(file);
			isSuccess = true;
		} catch (IOException e) {

			String osName = SystemUtils.OS_NAME.toLowerCase();
			LOGGER.debug(osName);
			if (osName.contains("window")) {
				RuntimeClassUtil.simpleExeAsynchLazy(Arrays.asList("explorer", file.getAbsolutePath()), errhandler);
			} else
				errhandler.accept(e);
		}
		return isSuccess;
	};

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 10.
	 * @param file
	 * @param errorHandler
	 * @return
	 */
	private static boolean fileOpenAction(File file, Consumer<Exception> errorHandler) {
		return fileAction(file, openAction, errorHandler);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 10.
	 * @param file
	 * @param action
	 * @param errorHandler
	 * @return
	 */
	private static boolean fileAction(File file, BiFunction<File, Consumer<Exception>, Boolean> action, Consumer<Exception> errorHandler) {
		if (Desktop.isDesktopSupported()) {
			if (file.exists()) {
				return action.apply(file, errorHandler);
			}
		}
		return false;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2022. 4. 28.
	 * @param file
	 * @param is
	 * @param charset
	 * @throws IOException
	 */
	public static void writeFile(File file, InputStream is, Charset charset) throws IOException {
		try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), charset)) {
			try (InputStreamReader reader = new InputStreamReader(is, charset)) {
				int read = -1;
				while ((read = reader.read()) != -1) {
					writer.write(read);
				}
				writer.flush();
			}
		}
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2022. 4. 28.
	 * @return
	 */
	public static File getTempGagoyle() {
		File file = new File(getTempFileSystem(), "Gagoyle");
		if (!file.exists())
			file.mkdirs();
		return file;
	}

	/**
	 * 시스템 Temp 파일 위치 리턴.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 6. 23.
	 * @return
	 */
	public static File getTempFileSystem() {
		return new File(System.getProperty("java.io.tmpdir"));
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2022. 6. 24.
	 * @param file
	 * @throws IOException
	 */
	public static List<Path> recursive(File file) throws IOException {
		try (Stream<Path> walk = Files.walk(file.toPath(), 10)) {
			return walk.collect(Collectors.toList());
		}
	}
	
	
	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 4. 1. 
	 * @param file
	 * @param skipDirFilter
	 * @return
	 * @throws IOException
	 */
	public static List<File> recursive(File file, Predicate<Path> skipDirFilter) throws IOException {
		return recursive(file, skipDirFilter, f -> true);
	}

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 4. 1.
	 * @param file
	 * @param skipDirFilter
	 * @return
	 * @throws IOException
	 */
	public static List<File> recursive(File file, Predicate<Path> skipDirFilter, Predicate<Path> isAppend) throws IOException {

		List<File> arrayList = new ArrayList<File>();
		SimpleFileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				if (skipDirFilter.test(dir)) {
					return FileVisitResult.SKIP_SUBTREE;
				} else {
					return FileVisitResult.CONTINUE;
				}
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//				System.out.println("File name: " + file.toAbsolutePath());
				if(isAppend.test(file))
					arrayList.add(file.toFile());
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
				// System.out.println("Failed to access file: " + file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				// System.out.println("Directory name: " + dir.getFileName());
				return FileVisitResult.CONTINUE;
			}
		};
		Files.walkFileTree(file.toPath(), visitor);
		return arrayList;
	}
}
