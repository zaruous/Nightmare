/**
 * 
 */
package com.kyj.fx.nightmare.actions.comm.ai;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.exec.InvalidExitValueException;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.stream.LogOutputStream;

import com.kyj.fx.nightmare.comm.ResourceLoader;
import com.kyj.fx.nightmare.comm.ValueUtil;

/**
 * 
 */
public class PyCodeBuilder extends AbstractCodeRunner {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PyCodeBuilder.class);
	private static final String command = "python";
	private File pythonFile;
//	private String baseDir = "../python/execute/";
	private String baseDir = "../python/";

	public static final LogOutputStream DEFAULT_OUTPUT_STREAM = new LogOutputStream() {
		@Override
		protected void processLine(String line) {
			LOGGER.debug("{}", line);
		}
	};
	public static final LogOutputStream DEFAULT_ERROR_OUTPUT_STREAM = new LogOutputStream() {
		@Override
		protected void processLine(String line) {
			LOGGER.debug("{}", line);
		}
	};
	private OutputStream outputStream = DEFAULT_OUTPUT_STREAM;
	private OutputStream errorStream = DEFAULT_ERROR_OUTPUT_STREAM;
	
	

	public PyCodeBuilder() {
		super(command);
	}

	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}
	public final File getPythonFile() {return pythonFile;}

	public PyCodeBuilder code(String code) throws IOException {
		String filePathName = "../python/execute/" + System.currentTimeMillis() + ".py";
		var f = new File(filePathName);
//		Path of = Path.of(filePathName);
		LOGGER.debug("temp python file path {}", f.getAbsolutePath());
		Files.writeString(f.toPath(), code, StandardCharsets.UTF_8);
		this.pythonFile = f;
		return this;
	}

	public PyCodeBuilder file(String simpleFileName) throws IOException {
		this.pythonFile = new File(baseDir, simpleFileName);
		return this;
	}

	/**
	 * @return
	 * @throws InvalidExitValueException
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws TimeoutException
	 */
	public ProcessExecutor defaultBuilder()
			throws InvalidExitValueException, IOException, InterruptedException, TimeoutException {
		ProcessExecutor defaultProcessExecutor = createDefaultProcessExecutor();
		defaultProcessExecutor.redirectError(errorStream);
//		defaultProcessExecutor.redirectOutput(null)
		defaultProcessExecutor.directory(new File(baseDir));
//		String absolutePath = new File("../python/execute",".pythonrc.py").getAbsolutePath();
//		LOGGER.debug("pyrc {}" , absolutePath);
		defaultProcessExecutor.command(command, getPythonFile().getAbsolutePath()).redirectOutput(outputStream)
		.environment("PYTHONIOENCODING", "utf8");
//		.environment("PYTHONSTARTUP", absolutePath);
		defaultProcessExecutor.destroyOnExit();
		return defaultProcessExecutor;
	}

	@Override
	public void run() {
		try {
			defaultBuilder().execute();
			
			if("Y".equals(ResourceLoader.getInstance().get(ResourceLoader.PYCODE_BUILDER_CLEAN_YN)))
			{
				//파이썬 실행 후 파일 삭제가 필요한 경우
				Files.deleteIfExists(pythonFile.toPath());
			}
			
		} catch (InvalidExitValueException | IOException | InterruptedException | TimeoutException e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}
	
	public OutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public OutputStream getErrorStream() {
		return errorStream;
	}

	public void setErrorStream(OutputStream errorStream) {
		this.errorStream = errorStream;
	}
	
}
