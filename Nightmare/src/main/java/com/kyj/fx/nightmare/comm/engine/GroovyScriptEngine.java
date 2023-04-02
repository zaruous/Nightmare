/********************************
 *	프로젝트 : Nightmare
 *	패키지   : com.kyj.fx.nightmare.comm.engine
 *	작성일   : 2023. 4. 1.
 *	작성자   : (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.comm.engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import com.kyj.fx.nightmare.comm.FileUtil;

/**
 * @author (zaruous@naver.com)
 *
 */
public class GroovyScriptEngine {

	private ScriptEngine groovyEngine;
	private StringBuilder scriptBuffer = new StringBuilder();
	/**
	 */
	public GroovyScriptEngine() {
		createEngine(Collections.emptyMap());
		loadDefault();
	}

	/**
	 * @param userProp
	 */
	public GroovyScriptEngine(Map<String, Object> userProp) {
		createEngine(userProp);
	}

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 4. 1.
	 * @param userProp
	 */
	public void createEngine(Map<String, Object> userProp) {
		ScriptEngineManager factory = new ScriptEngineManager();
		groovyEngine = factory.getEngineByName("groovy");
		Bindings createBindings = createBinding();
		groovyEngine.setBindings(createBindings, ScriptContext.ENGINE_SCOPE);
		createBindings.putAll(userProp);
	}

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 4. 1.
	 * @return
	 */
	protected Bindings createBinding() {
		Bindings createBindings = groovyEngine.createBindings();
		createBindings.put("BASEDIR", new File(new File("").getAbsolutePath(), "groovy"));
		return createBindings;
	}

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 4. 1.
	 * @return
	 */
	protected ScriptContext createContext() {
		return new SimpleScriptContext();
	};

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 4. 1.
	 * @param reader
	 * @throws ScriptException
	 */
	public void eval(Reader reader) throws ScriptException {
		this.groovyEngine.eval(reader);
	}

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 4. 1.
	 * @param script
	 * @throws ScriptException
	 */
	public void eval(String script) throws ScriptException {
		this.groovyEngine.eval(script.concat(System.lineSeparator()).concat(scriptBuffer.toString()));
	}

	

	private void loadDefault() {
		// init.
		scriptBuffer.setLength(0);
		File commonsFile = new File(new File("").getAbsolutePath(), "groovy/commons");
		try {
			CommonsGroovyFileVisitor visitor = new CommonsGroovyFileVisitor();
			Files.walkFileTree(commonsFile.toPath(), visitor);

			visitor.getItems().forEach(path -> {
				try {
					scriptBuffer.append(FileUtil.readToString(path.toFile())).append(System.lineSeparator());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	static class CommonsGroovyFileVisitor implements FileVisitor<Path> {

		List<Path> items = new ArrayList<>();

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			// TODO Auto-generated method stub
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {

			if (path.getFileName().toString().toLowerCase().endsWith("groovy")) {
				items.add(path);

			}

			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		public List<Path> getItems() {
			return this.items;
		}
	}
}
