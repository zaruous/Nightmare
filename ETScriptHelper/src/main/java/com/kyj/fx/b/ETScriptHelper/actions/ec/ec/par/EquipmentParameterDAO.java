/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.par
 *	작성일   : 2021. 12. 27.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.ec.ec.par;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.kyj.fx.b.ETScriptHelper.comm.AbstractDAO;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EquipmentParameterDAO extends AbstractDAO {

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 27.
	 * @param name
	 * @return
	 */
	public String getEquipmentClassGuidByName(String name) {

		StringBuffer sb = new StringBuffer();
		sb.append("select\n");
		sb.append("EquipmentClassGUID AS EQUIPMENT_CLASS_GUID\n");
		sb.append("from DMI_ET.dbo.ET_EquipmentClasses (nolock) \n");
		sb.append("where 1=1\n");
		sb.append("and Name = :name\n");

		HashMap<String, Object> p = new HashMap<String, Object>();
		p.put("name", name);
		return queryScala(sb.toString(), p, new ResultSetExtractor<String>() {
			@Override
			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					return rs.getString(1);
				}
				return null;
			}
		});
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 27.
	 * @param equipmentClassGuid
	 * @return
	 */
	public String getEquipmentClassNameByGuid(String equipmentClassGuid) {

		StringBuffer sb = new StringBuffer();
		sb.append("select\n");
		sb.append("Name\n");
		sb.append("from DMI_ET.dbo.ET_EquipmentClasses (nolock) \n");
		sb.append("where 1=1\n");
		sb.append("and EquipmentClassGUID = :equipmentClassGuid\n");

		HashMap<String, Object> p = new HashMap<String, Object>();
		p.put("equipmentClassGuid", equipmentClassGuid);
		return queryScala(sb.toString(), p, new ResultSetExtractor<String>() {
			@Override
			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					return rs.getString(1);
				}
				return null;
			}
		});
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 27. 
	 * @param eqName
	 * @param eventName
	 * @return
	 */
	public String getEventGuid(String eqName, String eventName) {

		StringBuffer sb = new StringBuffer();
		sb.append("select ev.EventGUID from DMI_ET.dbo.ET_Events ev inner join DMI_ET.dbo.ET_EquipmentClasses ec\n");
		sb.append("	on ev.EquipmentClassGUID = ec.EquipmentClassGUID\n");
		sb.append("where 1=1\n");
		sb.append("and ec.name = :equipmentClassName\n");
		sb.append("and ev.name = :eventName\n");
		sb.toString();

		HashMap<String, Object> p = new HashMap<String, Object>();
		p.put("equipmentClassName", eqName);
		p.put("eventName", eventName);
		return queryScala(sb.toString(), p, new ResultSetExtractor<String>() {
			@Override
			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					return rs.getString(1);
				}
				return null;
			}
		});
	}

}
