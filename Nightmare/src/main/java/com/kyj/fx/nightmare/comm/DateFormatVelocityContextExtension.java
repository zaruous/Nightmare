/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.comm
 *	작성일   : 2021. 11. 22.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.comm;

import java.util.Date;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class DateFormatVelocityContextExtension extends VelocityContext {

	private String dateFormat = DateUtil.SYSTEM_DATEFORMAT_YYYYMMDDHHMMSS;

	public DateFormatVelocityContextExtension() {
		super();
	}

	public DateFormatVelocityContextExtension(String dateFormat) {
		super();
		this.dateFormat = dateFormat;
	}

	public DateFormatVelocityContextExtension(Context innerContext) {
		super(innerContext);
	}

	public DateFormatVelocityContextExtension(@SuppressWarnings("rawtypes") Map context, Context innerContext) {
		super(context, innerContext);
	}

	/* (non-Javadoc)
	 * @see org.apache.velocity.VelocityContext#internalGet(java.lang.String)
	 */
	@Override
	public Object internalGet(String key) {

		if ("date".equals(key))
			return DateUtil.getDateAsStr(new Date(), this.dateFormat);

		return super.internalGet(key);
	}
}
