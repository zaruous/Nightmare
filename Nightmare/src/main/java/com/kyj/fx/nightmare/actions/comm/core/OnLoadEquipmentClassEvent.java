/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.states
 *	작성일   : 2021. 11. 24.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.comm.core;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public interface OnLoadEquipmentClassEvent {

	/**
	 * 장비클래스의 이벤트 호출될시 <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 24.
	 */
	public void onLoadEquipmentClassEvent(String equipmentClassGuid, String eventGuid);
}