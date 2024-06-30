/**
 * 
 */
package com.kyj.fx.nightmare.actions.comm.ai;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.exec.InvalidExitValueException;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.stream.LogOutputStream;

import com.kyj.fx.nightmare.comm.ValueUtil;

/**
 * 
 */
public class PyCodeBuilder extends AbstractCodeRunner {
	private static final Logger LOGGER = LoggerFactory.getLogger(PyCodeBuilder.class);
	private static final String PYTHON = "python";
	private File pythonFile;
	final static String BASE_DIR = "../python/execute/";

	public PyCodeBuilder() {
		super(PYTHON);

	}

	public PyCodeBuilder code(String fileName, String code) throws IOException {
		Path of = Path.of(BASE_DIR + fileName);
		LOGGER.debug("temp python file path {}", of.toAbsolutePath().toString());
		Files.writeString(of, code, StandardCharsets.UTF_8);
		this.pythonFile = of.getFileName().toFile();
		return this;
	}

	public PyCodeBuilder file(String simpleFileName) throws IOException {
		this.pythonFile = new File(BASE_DIR, simpleFileName);
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
		defaultProcessExecutor.directory(new File("../python/"));
		defaultProcessExecutor.command(PYTHON, "execute/" + pythonFile.getName()).redirectOutput(new LogOutputStream() {
			@Override
			protected void processLine(String line) {
				LOGGER.debug("{}", line);
			}
		}).environment("PYTHONIOENCODING", "utf8");
		defaultProcessExecutor.destroyOnExit();
		return defaultProcessExecutor;
	}

	@Override
	public void run() {
		try {
			defaultBuilder().execute();
			Files.deleteIfExists(pythonFile.toPath());
		} catch (InvalidExitValueException | IOException | InterruptedException | TimeoutException e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}
}
