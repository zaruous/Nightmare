/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.comm
 *	작성일   : 2021. 11. 29.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.comm;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.kyj.fx.nightmare.comm.ExcelColumnExpression;
import com.kyj.fx.nightmare.comm.FxExcelUtil;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
class FxExcelUtilTest {

	/**
	 * Test method for
	 * {@link com.kyj.fx.nightmare.comm.FxExcelUtil#createExcel(java.io.File, java.util.LinkedHashMap, com.kyj.fx.nightmare.comm.IExcelDataSetHandler, java.util.Map, boolean)}.
	 */
	@Test
	void test() {
		File out = new File("test.xlsx");
		 createExcel(out);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 29.
	 * @param out
	 */
	public static void createExcel(File out) {
		try {
			LinkedHashMap<String, LinkedHashMap<ExcelColumnExpression, List<Object>>> dataSource = new LinkedHashMap<String, LinkedHashMap<ExcelColumnExpression, List<Object>>>();
			LinkedHashMap<ExcelColumnExpression, List<Object>> value = new LinkedHashMap<>();
			value.put(new ExcelColumnExpression(), Arrays.asList());

			ExcelColumnExpression key = new ExcelColumnExpression();
			key.setDisplayText("Equipment Class Name");
			key.setRealText("Equipment Class Name");
			key.setVisible(true);
			value.put(key, Arrays.asList("test", "test2", "test"));

			dataSource.put("Sheet0", value);
			HashMap<String, Map<String, String>> metadata = new HashMap<String, Map<String, String>>();
			HashMap<String, String> value2 = new HashMap<>();
			// value2.put(FxExcelUtil.$$META_COLUMN_MAX_HEIGHT$$, "0");
			metadata.put("Sheet0", value2);

			FxExcelUtil.createExcel(out, dataSource, metadata);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
