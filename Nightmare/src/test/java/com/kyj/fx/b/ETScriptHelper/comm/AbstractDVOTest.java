/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.comm
 *	작성일   : 2021. 12. 10.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.comm;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.kyj.fx.nightmare.actions.ec.ec.EquipmentClassDVO;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
class AbstractDVOTest {

	@Test
	void test() {
		var d = new EquipmentClassDVO();
		d.setCalibrationGroupGUID("assdasdasd");
		Map<String, Object> metadata = d.getMetadata();
		System.out.println(metadata);

	}

}
