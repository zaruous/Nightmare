package com.kyj.fx.nightmare.comm;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 파일 내용 변환작업을 대표하는 인터페이스 <br/>
 * 
 * @author KYJ
 *
 */
public interface FileContentConversionable {

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 4. 2. 
	 * @param in
	 */
	public void in(InputStream in);


	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 4. 2. 
	 * @param out
	 */
	public void out(OutputStream out);

}
