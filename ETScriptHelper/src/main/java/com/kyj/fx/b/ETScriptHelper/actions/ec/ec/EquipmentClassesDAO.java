/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec
 *	작성일   : 2021. 11. 23.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.ec.ec;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.kyj.fx.b.ETScriptHelper.comm.AbstractDAO;



/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EquipmentClassesDAO extends AbstractDAO {

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 24.
	 * @param equipmentClassGuid
	 * @return
	 */
	public EquipmentClassDVO getEquipmentClass(String equipmentClassGuid) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("equipmentClassGuid", equipmentClassGuid);
		return getEquipmentClass(hashMap);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 24.
	 * @param param
	 * @return
	 */
	public EquipmentClassDVO getEquipmentClass(Map<String, Object> param) {

		StringBuffer sb = new StringBuffer();
		sb.append("select\n");
		sb.append("EquipmentClassGUID AS EQUIPMENT_CLASS_GUID,\n");
		sb.append("Name AS NAME,\n");
		sb.append("Description AS DESCRIPTION\n");
		sb.append("from DMI_ET.dbo.ET_EquipmentClasses (nolock) \n");
		sb.append("where 1=1\n");
		sb.append("and EquipmentClassGUID = :equipmentClassGuid\n");
		return queryScala(sb.toString(), param, new ResultSetExtractor<EquipmentClassDVO>() {
			@Override
			public EquipmentClassDVO extractData(ResultSet rs) throws SQLException {
				if (rs.next()) {
					return new BeanPropertyRowMapper<EquipmentClassDVO>(EquipmentClassDVO.class).mapRow(rs, rs.getRow());
				}
				return null;
			}
		});

	}

}
