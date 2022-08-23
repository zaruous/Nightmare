/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.wf
 *	작성일   : 2022. 7. 22.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.wf;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public enum WorkflowState {
	Created(1)
	,Initializing(2)
	,running(3)
	,Completed(4)
	,Held(5)
	,COLLECTING_SIGNATURES(6)
	,Force_Complete(7)
	,PreCompleted(8)
	,SELECTING_FORMULA(9)
	,SettingParameters(10)
	,Started(11)
	,Closed(12)
	
	,Fail_to_Start(15)
	,Error(16)
	,COLLECTING_ATTACHMENTS(17);
	
	int code;
	WorkflowState(int code)
	{
		this.code = code;
	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2022. 7. 22. 
	 * @param code
	 * @return
	 */
	public static String getText(int code) {
		Optional<WorkflowState> findAny = Stream.of(WorkflowState.values()).filter(v -> v.code == code).findAny();
		if(findAny.isPresent())
			return findAny.get().name();
		return "";
	}
	
	/*
	when 1 then 'Created'
	when 2 then 'Initializing' 
	when 3 then 'running'
	when 4 then 'Complete'
	when 5 then 'Held'
	when 6 then 'COLLECTING SIGNATURES'
	when 7 then 'Force Complete'
	When 8 then ‘PreCompleted'
	when 9 then 'SELECTING FORMULA'
	when 10 then 'SettingParameters‘
	When 11 then ‘Started’
	when 12 then 'Closed'
	when 15 then 'Fail to Start‘
	when 16 then 'Error'
	when 17 then 'COLLECTING ATTACHMENTS'
	*/

}
