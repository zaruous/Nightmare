/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.wf
 *	작성일   : 2022. 7. 22.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.wf;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public enum ETWorkflowState {
	Created(0), Starting(1), Executing(2), Complete(3), Force_Complete(4), Archived(5), On_Hold(6);

	int code;

	ETWorkflowState(int code) {
		this.code = code;
	}
	
	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2022. 7. 28. 
	 * @return
	 */
	public int getCode() {
		return this.code;
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2022. 7. 22.
	 * @param code
	 * @return
	 */
	public static String getText(int code) {
		Optional<ETWorkflowState> findAny = Stream.of(ETWorkflowState.values()).filter(v -> v.code == code).findAny();
		if (findAny.isPresent())
			return findAny.get().name();
		return "";
	}

}
