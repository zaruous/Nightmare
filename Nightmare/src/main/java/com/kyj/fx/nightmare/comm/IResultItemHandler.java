/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.comm
 *	작성일   : 2021. 11. 23.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.nightmare.comm;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
@FunctionalInterface
public interface IResultItemHandler<T> {

	/**
	 */
	public boolean next(T map);

}
