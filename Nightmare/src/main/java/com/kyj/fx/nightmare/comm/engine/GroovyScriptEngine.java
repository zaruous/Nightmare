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
import java.util.stream.Collectors;

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
		this(true);
	}

	public GroovyScriptEngine(boolean loadCore) {
		createEngine(Collections.emptyMap());
		if (loadCore)
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
		SimpleScriptContext simpleScriptContext = new SimpleScriptContext();
		return simpleScriptContext;
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
	public Object eval(String script) throws ScriptException {
		return this.groovyEngine.eval(
				script.concat(System.lineSeparator())
				.concat(scriptBuffer.toString()));
	}

	public List<String> loadScripts() {
		File commonsFile = new File(new File("").getAbsolutePath(), "groovy/commons");
		try {
			CommonsGroovyFileVisitor visitor = new CommonsGroovyFileVisitor();
			Files.walkFileTree(commonsFile.toPath(), visitor);

			return visitor.getItems().stream().map(path -> {
				try {
					return FileUtil.readToString(path.toFile());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return "";
			}).collect(Collectors.toList());

		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	private void loadDefault() {
		// init.
		scriptBuffer.setLength(0);
		loadScripts().forEach(script -> {
			scriptBuffer.append(script).append(System.lineSeparator());
		});

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
