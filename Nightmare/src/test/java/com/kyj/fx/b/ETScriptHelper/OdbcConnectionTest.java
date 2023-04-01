/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper
 *	작성일   : 2021. 11. 25.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.junit.jupiter.api.Test;

import com.kyj.fx.nightmare.actions.ec.eq.states.EquipmentEventStateDVO;
import com.kyj.fx.nightmare.comm.ExcelReader;
import com.kyj.fx.nightmare.comm.ObjectUtil;
import com.kyj.fx.nightmare.comm.service.XMLUtils;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class OdbcConnectionTest {

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
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
