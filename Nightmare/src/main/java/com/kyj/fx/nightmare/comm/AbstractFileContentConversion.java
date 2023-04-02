package com.kyj.fx.nightmare.comm;

/**
 * 데이터 변환 처리 추상 클래스 <br/>
 * 
 * @author KYJ
 *
 */
public abstract class AbstractFileContentConversion<T> implements FileContentConversionable, DataConversionable<T> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kyj.fx.voeditor.visual.framework.file.conversion.DataConversionable#
	 * conversion()
	 */
	public abstract T conversion() throws Exception;
}
