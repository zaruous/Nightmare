
package com.kyj.fx.nightmare.ui.grid;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryKeyColumn {

	/**
	 * MessageId를 기입하여 다국어 처리가 가능하게한다.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 22.
	 * @return
	 */
	public String messageId() default "";

}
