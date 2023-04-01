/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.scripts
 *	작성일   : 2021. 12. 3.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.ec.ec.scripts;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public record Event(String equipmentClassGuid, String eventGuid) {

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2022. 6. 24.
	 * @return
	 */
	public static Event AllEvent(String equipmentClassGuid) {
		return new Event(equipmentClassGuid, "ALL");
	}
};
