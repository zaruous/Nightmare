/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.grid
 *	작성일   : 2021. 12. 20.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.nightmare.ui.grid;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnStyle {

	/**
	 * 컬럼 스타일 
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 22.
	 * @return
	 */
	public String value() default "";



}
