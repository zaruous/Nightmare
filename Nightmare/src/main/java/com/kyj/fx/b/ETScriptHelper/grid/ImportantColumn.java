/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.grid
 *	작성일   : 2015. 10. 20.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.b.ETScriptHelper.grid;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 
 * 컬럼명 좌측에 *을 추가하여 보여준다. <br/>
 * 
 * @author KYJ
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ImportantColumn {

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 20.
	 * @return
	 */
	public boolean value() default true;
	
}
