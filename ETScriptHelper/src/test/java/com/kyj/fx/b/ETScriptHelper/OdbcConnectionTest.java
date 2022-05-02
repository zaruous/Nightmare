/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper
 *	작성일   : 2021. 11. 25.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.junit.jupiter.api.Test;

import com.kyj.fx.b.ETScriptHelper.actions.ec.eq.states.EquipmentEventStateDVO;
import com.kyj.fx.b.ETScriptHelper.comm.ExcelReader;
import com.kyj.fx.b.ETScriptHelper.comm.ObjectUtil;
import com.kyj.fx.b.ETScriptHelper.comm.service.XMLUtils;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class OdbcConnectionTest {

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 25.
	 * @param args
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception {
		String fromExcel = "20211125111512.xlsx";
		String readExcel = ExcelReader.readExcel(new File(fromExcel));
		Document doc = XMLUtils.load(readExcel);
		EquipmentEventStateDVO to = new EquipmentEventStateDVO();
		List<EquipmentEventStateDVO> populateList = ExcelReader.populateList(doc, EquipmentEventStateDVO.class);
		populateList.forEach(v ->{
			System.out.println(ObjectUtil.toMap(v));	
		});
		

		// System.out.println(readExcel);

	}

}
