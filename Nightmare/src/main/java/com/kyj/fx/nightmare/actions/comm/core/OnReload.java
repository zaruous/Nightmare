/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.comm
 *	작성일   : 2021. 12. 6.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.comm.core;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public interface OnReload {

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 12. 6.
	 */
	public abstract void reload();
	
	/**
	 * 탭 컨텐츠에 할당된 버튼인 Reload 을 활성화 할지 유무 <br/>
	 * 
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 12. 9.
	 * @return
	 */
	public default boolean enableReloadButton() {
		return false;
	}
}
