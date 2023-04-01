/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.frame
 *	작성일   : 2021. 11. 22.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.frame;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.kyj.fx.nightmare.comm.AbstractDAO;
import com.kyj.fx.nightmare.grid.CodeDVO;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class EquipmentClassDQM extends AbstractDAO {

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 11. 22.
	 * @param param
	 * @return
	 */
	public List<CodeDVO> listEquipmentClasses(Map<String, Object> param) {
		StringBuffer sb = new StringBuffer();
		sb.append("select\n");
		sb.append("    EquipmentClassGUID as code,\n");
		sb.append("    name as nm \n");
		sb.append("from\n");
		sb.append("    dmi_et.dbo.ET_EquipmentClasses (nolock)\n");
		sb.append("where 1=1\n");
		sb.append("and Disabled = 0\n");
		sb.append("order by name\n");
//		sb.toString();
		return query(sb.toString(), param, new BeanPropertyRowMapper<CodeDVO>(CodeDVO.class));
	}

}
