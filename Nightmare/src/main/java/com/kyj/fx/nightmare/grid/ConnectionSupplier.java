/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.commons.fx.controls.grid
 *	작성일   : 2021. 11. 22.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.nightmare.grid;

import java.sql.Connection;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public interface ConnectionSupplier {

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 22. 
	 * @return
	 */
	Connection get();

}
