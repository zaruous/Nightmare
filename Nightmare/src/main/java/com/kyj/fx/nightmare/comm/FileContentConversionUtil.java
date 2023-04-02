package com.kyj.fx.nightmare.comm;

import java.io.File;
import java.nio.charset.Charset;

/**
 * @author KYJ
 *
 */
class FileContentConversionUtil {

	public static String conversion(File f) {
		GargoyleFileContConversionPolicy m = new GargoyleFileContConversionPolicy();
		return m.conversion(f);
	}
	
	/**
	 * 파일 타입에 따라 적절하게 내용을 <br/>
	 * 사람이 읽을 수 있는 텍스트로 변환하는 API <br/>
	 * 
	 * gargoyle.cont.conversion.properties파일에 등록된 내용을 참조 <br/>
	 * Conversion 클래스는 AbstractStringFileContentConversion.java와 상속관계여야만 함 <br/>
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 4. 2. 
	 * @param f
	 * @param encoding
	 * @return
	 */
	public static String conversion(File f, Charset encoding) {
		GargoyleFileContConversionPolicy m = new GargoyleFileContConversionPolicy();
		return m.conversion(f, encoding);
	}
}
