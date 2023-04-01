/********************************
 *	프로젝트 : CrudGrigSample
 *	패키지   : application
 *	작성일   : 2016. 5. 17.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.nightmare.grid;

import com.kyj.fx.nightmare.comm.AbstractDVO;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author KYJ
 *
 */
public class CodeDVO extends AbstractDVO {

	private StringProperty code = new SimpleStringProperty();
	private StringProperty nm = new SimpleStringProperty();

	public CodeDVO() {

	}

	public CodeDVO(String code, String nm) {
		super();
		this.code.set(code);
		this.nm.set(nm);
	}

	/**
	 * @return the code
	 */
	public final String getCode() {
		return code.get();
	}

	/**
	 * @return the nm
	 */
	public final String getNm() {
		return nm.get();
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public final void setCode(String code) {
		this.code.set(code);
	}

	/**
	 * @param nm
	 *            the nm to set
	 */
	public final void setNm(String nm) {
		this.nm.set(nm); 
	}

	@Override
	public String toString() {
		return nm.get();
	}

}
