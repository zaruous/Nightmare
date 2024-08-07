/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.commons.fx.controls.grid
 *	작성일   : 2021. 11. 22.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.ui.grid;

import java.sql.Connection;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public interface ConnectionSupplier {

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 11. 22. 
	 * @return
	 */
	Connection get();

}
