/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.comm
 *	작성일   : 2021. 11. 24.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.comm.core;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public interface OnLoadEquipmentClass {

	/**
	 * Equipment Class에 대한 정보 로드 요청이 호출될시 <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 24.
	 * @param equipmentClassGuid
	 * @param equipmentName
	 */
	public void onLoadEquipmentClass(String equipmentClassGuid);
}
