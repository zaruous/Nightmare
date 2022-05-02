/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.diff
 *	작성일   : 2019. 2. 28.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.comm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * beyound compare를 이용해 텍스트를 비교하고 <br/>
 * 결과레포트를 얻기 위한 중간 인터페이스 코드 이다. <br/>
 * 
 * The purpose of this program is to compare text files and obtain a report. <br/>
 * 
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class BeyoundCompareToolHelper {

	private static final String BEYOUND_COMPARE_PATH = "diff.beyound.compare.path";

	private static final Logger LOGGER = LoggerFactory.getLogger(BeyoundCompareToolHelper.class);
	private File outFile;
	/*
	 * 스크립트를 사용해서 비교를 할지 선택하기 위함이며 기본값으로 false이다.
	 */
	private boolean useScript = false;
	/*
	 * 스크립트 파일은 생성자에서 초기화된다. 만약 별도의 스크립트를 수행하고 싶다면 <br/> 본 프로그램 위치에
	 * 'default-Beyond-Script.txt' 파일을 생성하면된다. <br/>
	 */
	private File scriptFile;
	public static final String SCRIPT_DEFAULT_FILE_NAME = "default-Beyond-Script.txt";
	public static final String SCRIPT_FOLDER_COMPARE_FILE_NAME = "default-Beyond-FolderScript.txt";
	private String[] scriptFileNames = new String[] { SCRIPT_DEFAULT_FILE_NAME, SCRIPT_FOLDER_COMPARE_FILE_NAME };

	public BeyoundCompareToolHelper() {

		/*
		 * script 파일의 기본 위치를 설정한다. 사용자 정의 스크립트가 있다면 그것을 실행하겠지만 없으면 디폴트 스크립트를 새로
		 * 만들어준다.
		 */
		for (String scriptFileName : scriptFileNames) {
			File file = new File(scriptFileName);
			if (!file.exists()) {
				URL resource = BeyoundCompareToolHelper.class.getResource(scriptFileName);
				if (resource != null) {

					try (InputStream in = resource.openStream()) {
						FileUtil.writeFile(file, in, StandardCharsets.UTF_8);
						this.scriptFile = file;
					} catch (IOException e) {
						LOGGER.error(ValueUtil.toString(e));
					}
				}
			}
		}
		// String name = "default-Beyond-Script.txt";
		this.scriptFile = new File(SCRIPT_DEFAULT_FILE_NAME);
		// if (!file.exists()) {
		// URL resource = BeyoundCompareToolHelper.class.getResource(name);
		// if (resource != null) {
		//
		// try (InputStream in = resource.openStream()) {
		// FileUtil.writeFile(file, in, StandardCharsets.UTF_8);
		// this.scriptFile = file;
		// } catch (IOException e) {
		// LOGGER.error(ValueUtil.toString(e));
		// }
		// }
		// } else
		// this.scriptFile = file;

		this.setExternalExeFullPath(ResourceLoader.getInstance().get(BEYOUND_COMPARE_PATH, ""));
	}

	private String externalExeFullPath;

	private void setExternalExeFullPath(String externalExeFullPath) {
		this.externalExeFullPath = externalExeFullPath;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2022. 4. 28.
	 * @return
	 */
	private String getExternalExeFullPath() {
		return this.externalExeFullPath;
	}

	/**
	 * Beyound Compare를 사용할 수 있는 환경인지 체크 <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 3. 1.
	 * @return
	 */
	public boolean canUse() {
		String externalExeFullPath = this.getExternalExeFullPath();
		if (ValueUtil.isNotEmpty(externalExeFullPath)) {
			return new File(externalExeFullPath).exists();
		} else {
			// ResourceLoader.getInstance().put(BEYOUND_COMPARE_PATH, "");
		}
		return false;
	}

	/**
	 * @return the outFile
	 */
	public File getOutFile() {
		return outFile;
	}

	/**
	 * @param outFile
	 *            the outFile to set
	 */
	public void setOutFile(File outFile) {
		this.outFile = outFile;
	}

	private Function<File, String> converter = f -> {
		try {
			return FileUtil.readToString(f);
		} catch (IOException e) {
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
	};

	/**
	 * @return the scriptFile
	 */
	public File getScriptFile() {
		return scriptFile;
	}

	/**
	 * @param scriptFile
	 *            the scriptFile to set
	 */
	public void setScriptFile(File scriptFile) {
		this.scriptFile = scriptFile;
	}


	public Function<File, String> getConverter() {
		return converter;
	}

	
	/**
	 * @return the useScript
	 */
	public boolean isUseScript() {
		return useScript;
	}

	/**
	 * @param useScript
	 *            the useScript to set
	 */
	public void setUseScript(boolean useScript) {
		this.useScript = useScript;
	}

	/**
	 * 비교를 수행한다. 스크립트 사용유무에 따라 <br/>
	 * beyound compare 결과가 화면에 나오게 할지 <br/>
	 * 아니면 레포트만 나오게 할지 선택할 수 있다. <br/>
	 * 
	 * 별도의 스크립트를 수행하고자한다면 프로그램 설치 디렉토리에 'default-Beyond-Script.txt' 이 파일을 수정하면된다 . <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 2. 27.
	 * @throws Exception
	 */
	public void compare() throws Exception {
		if (isUseScript()) {
			execScript();
		} else {
			exec(true);
		}
	}

	/**
	 * @param demon
	 * @throws FileNotFoundException
	 */
	public void compare(boolean demon) throws Exception {
		if (isUseScript()) {
			execScript();
		} else {
			exec(demon);
		}
	}

	private File leftFile;
	private File rightFile;
	
	public File getLeftFile() {
		return leftFile;
	}

	public void setLeftFile(File leftFile) {
		this.leftFile = leftFile;
	}

	public File getRightFile() {
		return rightFile;
	}

	public void setRightFile(File rightFile) {
		this.rightFile = rightFile;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 2. 28.
	 * @throws Exception
	 */
	protected void execScript() throws Exception {
		File leftFile = getLeftFile();
		File rightFile = getRightFile();
		String externalExeFullPath = getExternalExeFullPath();
		LOGGER.debug("External Path : {} ", externalExeFullPath);
		File outFile = getOutFile();

		if (leftFile == null || rightFile == null || !leftFile.exists() || !rightFile.exists())
			throw new FileNotFoundException("File is Empty.");

		File scriptFile = getScriptFile();
		if (scriptFile == null || !scriptFile.exists()) {
			throw new FileNotFoundException("sript file can't found");
		}

		if (outFile == null) {
			LOGGER.info("beyound compare outfile does not exists. update default . ");
			File parent = new File(FileUtil.getTempGagoyle(), "compareOutDir");
			if (!parent.exists())
				parent.mkdirs();
			String fileName = leftFile.getName() + "_" + rightFile.getName();
			outFile = new File(parent, fileName + ".html");
			// outFile.createNewFile();
		}

		if (ValueUtil.isEmpty(externalExeFullPath)) {
			externalExeFullPath = ResourceLoader.getInstance().get(BEYOUND_COMPARE_PATH);
			if (ValueUtil.isEmpty(externalExeFullPath)) {
				throw new RuntimeException("beyound compare setup path is empty.");
			}
		}

		File beyoundCompareFile = new File(externalExeFullPath, compareFileName());
		if (!beyoundCompareFile.exists() && beyoundCompareFile.isDirectory())
			throw new RuntimeException("External File does not exists.");
		if (!beyoundCompareFile.canExecute())
			throw new RuntimeException("External File could not accessible.");

		List<String> params = new ArrayList<>();
		params.add(beyoundCompareFile.getAbsolutePath()); // exe-external-path
		params.add("@" + scriptFile.getAbsolutePath());// script
		if (closeScript)
			params.add("/closescript");  // close window result dialog

		params.add(leftFile.getAbsolutePath()); /* left */
		params.add(rightFile.getAbsolutePath()); /* right */
		params.add(outFile.getAbsolutePath()); /* outpath */

		// List<String> asList = Arrays.asList(beyoundCompareFile.getAbsolutePath(),
		// "@" + scriptFile.getAbsolutePath(),
		// "/closescript",
		// leftFile.getAbsolutePath(),
		// rightFile.getAbsolutePath(),
		// outFile.getAbsolutePath()
		// );
		LOGGER.debug("Print Arguments {}", params);
		// RuntimeClassUtil.simpleExeAsynchLazy(params);
		RuntimeClassUtil.simpleExec(params);
		this.outFile = outFile;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2022. 4. 28. 
	 * @return
	 */
	public String getResult() {
		return getConverter().apply(this.outFile);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 12. 3.
	 * @return
	 */
	protected String compareFileName() {
		return "BCompare.exe";
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 2. 28.
	 * @throws Exception
	 */
	protected void exec(boolean demon) throws Exception {
		File leftFile = getLeftFile();
		File rightFile = getRightFile();
		String externalExeFullPath = getExternalExeFullPath() + File.separator + compareFileName();

		if (leftFile == null || rightFile == null || !leftFile.exists() || !rightFile.exists())
			throw new FileNotFoundException("File is Empty.");

		// File scriptFile = getScriptFile();
		// if (scriptFile == null || !scriptFile.exists()) {
		// throw new FileNotFoundException("sript file can't found");
		// }

		RuntimeClassUtil.simpleExeAsynchLazy(Arrays.asList(externalExeFullPath, // exe-external-path
				leftFile.getAbsolutePath(), /* left */
				rightFile.getAbsolutePath() /* right */
		), demon);

	}

	/**
	 * external 프로그램을 호출할때 로그창은 자동을 닫히게 설정할지 유무를 결정함 <br/>
	 * 
	 * @최초생성일 2020. 7. 9.
	 */
	private boolean closeScript = true;

	public void closeScript(boolean flag) {
		this.closeScript = flag;
	}

	/**
	 * Folder Compare를 처리하기위한 인스턴스 생성 <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2020. 7. 9.
	 * @return
	 */
	public static BeyoundCompareToolHelper newFolderCompare() {
		BeyoundCompareToolHelper h = new BeyoundCompareToolHelper();
		h.setScriptFile(new File(SCRIPT_FOLDER_COMPARE_FILE_NAME));
		h.setUseScript(true);
		return h;
	}

	/**
	 * 디폴트 스크립트는 단순히 Beyound Compare을 실행시켜 비교한다.<br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 3. 17.
	 * @return
	 */
	public static BeyoundCompareToolHelper newDefaultCompare() {
		BeyoundCompareToolHelper h = new BeyoundCompareToolHelper();
		h.setUseScript(false);
		return h;
	}

	/**
	 * 스크립트를 활용해 비교 결과를 나타낸다 <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 3. 17.
	 * @return
	 */
	public static BeyoundCompareToolHelper newFileCompare() {
		BeyoundCompareToolHelper h = new BeyoundCompareToolHelper();
		h.setScriptFile(new File(SCRIPT_DEFAULT_FILE_NAME));
		h.setUseScript(true);
		return h;
	}

}
