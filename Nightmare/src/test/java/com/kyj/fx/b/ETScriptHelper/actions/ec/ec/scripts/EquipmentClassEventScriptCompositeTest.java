/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.scripts
 *	작성일   : 2021. 12. 6.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.ec.ec.scripts;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.comm.ResourceLoader;
import com.kyj.fx.nightmare.comm.service.ESig;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
class EquipmentClassEventScriptCompositeTest {

	private static Logger LOGGER = LoggerFactory.getLogger(EquipmentClassEventScriptCompositeTest.class);

	/**
	 * Test method for {@link com.kyj.fx.nightmare.actions.ec.ec.scripts.EquipmentClassEventScriptComposite#onCommit()}.
	 * 
	 * @throws Exception
	 */
	@Disabled
	@Test
	void testOnCommit() throws Exception {
		String rootUrl = ResourceLoader.getInstance().get(ResourceLoader.SYNCADE_ROOT_URL);
		LOGGER.debug("url {}", rootUrl);
		var esig = new ESig(rootUrl);
		String pwd = ESig.base64Encoder("12");
		var createToken = esig.createTokenEx("kyjun.kim", pwd, "5", "Syncade", "DMI ET", "Events", "0", "Test");

		LOGGER.debug("create token result : {}", createToken);
	}

}
