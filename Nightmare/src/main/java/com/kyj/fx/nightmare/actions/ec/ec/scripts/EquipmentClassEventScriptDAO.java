/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.scripts
 *	작성일   : 2021. 12. 3.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.ec.ec.scripts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.kyj.fx.nightmare.actions.ec.ec.events.EquipmentClassEventDAO;
import com.kyj.fx.nightmare.actions.ec.ec.events.EtEventsDVO;
import com.kyj.fx.nightmare.comm.AbstractDAO;
import com.kyj.fx.nightmare.comm.Hex;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.ui.grid.CodeDVO;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class EquipmentClassEventScriptDAO extends AbstractDAO {

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 12. 3.
	 * @return
	 */
	@Deprecated
	public List<CodeDVO> getAvaliableScript() {
		StringBuffer sb = new StringBuffer();
		sb.append("select \n");
		sb.append("s.ScriptGUID as code\n");
		sb.append(",s.Name as nm\n");
		sb.append(" from dmi_et.dbo.ET_AvailableScripts (nolock) s\n");
		return query(sb.toString(), Collections.emptyMap(), new BeanPropertyRowMapper<>(CodeDVO.class));
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 12. 6.
	 * @param equipmentClassName
	 * @param eventName
	 * @return
	 */
	public String getEventGuid(String equipmentClassName, String eventName) {
		StringBuffer sb = new StringBuffer();
		sb.append("select e.eventGuid from DMI_ET.dbo.ET_Events e with (nolock)\n");
		sb.append("	inner join DMI_ET.dbo.ET_EquipmentClasses c with (nolock)\n");
		sb.append("	on e.equipmentClassGuid = c.equipmentClassGuid\n");
		sb.append("where 1=1\n");
		sb.append("and e.name = :eventName\n");
		sb.append("and c.name = :equipmentClassName\n");

		var p = new HashMap<String, Object>();
		p.put("equipmentClassName", equipmentClassName);
		p.put("eventName", eventName);
		return queryScala(sb.toString(), p, new ResultSetExtractor<String>() {

			@Override
			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					return rs.getString("eventGuid");
				}
				return null;
			}
		});
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 12. 3.
	 * @param hashMap
	 * @return
	 */
	public List<EquipmentScriptDVO> getEquipmentScript(Map<String, Object> hashMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("with eee as ( select\n");
		sb.append("    a.Name [Name],\n");
		sb.append("    a.EventGUID,\n");
		sb.append("    a.EquipmentClassGUID,\n");
		sb.append("    d.Name [ActionName],\n");
		sb.append("    d.ScriptGUID\n");
		sb.append("from\n");
		sb.append("    DMI_ET.dbo.ET_Events a (nolock)   \n");
		sb.append("	cross  join\n");
		sb.append("    DMI_ET.dbo.ET_AvailableScripts (nolock) d \n");
		sb.append("	where 1=1\n");
		sb.append("	)\n");
		sb.append("select\n");
		sb.append("    c.Name as [Equipment_Class_Name],\n");
		sb.append("    a.Name as [EventName],\n");
		sb.append("    a.EventGUID,\n");
		sb.append("    a.ActionName,\n");
		sb.append("    b.EventScriptGUID,\n");
		sb.append("    b.code,\n");
		sb.append("    b.eventguid,\n");
		sb.append("    a.EquipmentClassGUID, \n");
		sb.append("    a.ScriptGUID\n");
		sb.append("from\n");
		sb.append("    eee a \n");
		sb.append("left outer join\n");
		sb.append("    DMI_ET.dbo.ET_EventScripts (nolock) b \n");
		sb.append("        on a.EventGUID = b.EventGUID \n");
		sb.append("        and a.ScriptGUID = b.ScriptGUID \n");
		sb.append("inner join\n");
		sb.append("    DMI_ET.dbo.ET_EquipmentClasses (nolock) c \n");
		sb.append("        on a.EquipmentClassGUID = c.EquipmentClassGUID \n");
		sb.append("where 1=1\n");
		sb.append("\n");
		sb.append("	and c.EquipmentClassGUID = :equipmentClassGuid\n");

		sb.append("	and a.EventGUID = :eventGuid\n");
		sb.append("#if($avaliableScriptGuid)");
		sb.append("	and a.ScriptGUID = :avaliableScriptGuid\n");
		sb.append("#end");
		sb.append("	and a.ActionName in (:eventTypeNames) \n");

		sb.append("	\n");
		sb.append("order by\n");
		sb.append("    c.Name,\n");
		sb.append("    a.Name \n");

		return query(sb.toString(), hashMap, new RowMapper<EquipmentScriptDVO>() {

			@Override
			public EquipmentScriptDVO mapRow(ResultSet rs, int rowNum) throws SQLException {
				EquipmentScriptDVO equipmentScriptDVO = new EquipmentScriptDVO(rs.getString("EquipmentClassGUID"),
						rs.getString("EventScriptGUID"), rs.getString("ActionName"), "");
				equipmentScriptDVO.setEquipmentClassName(rs.getString("Equipment_Class_Name"));
				equipmentScriptDVO.setEventName(rs.getString("EventName"));
				equipmentScriptDVO.setScriptGuid(rs.getString("ScriptGUID"));
				equipmentScriptDVO.setEventGuid(rs.getString("EventGUID"));

				try {
					if (ValueUtil.isEmpty(rs.getString("code")))
						equipmentScriptDVO.setCode("");
					else
						equipmentScriptDVO.setCode(new String(Hex.decode(rs.getString("code")), "UTF-16LE"));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				return equipmentScriptDVO;
			}
		});
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 12. 3.
	 * @param eventGuid
	 * @return
	 */
	public EtEventsDVO getEventInfo(String eventGuid) {
		var dao = new EquipmentClassEventDAO();
		return dao.getEvent(eventGuid);
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2022. 6. 24.
	 * @param equipmentClassGuid
	 * @return
	 */
	public List<String> getAllEventGuids(String equipmentClassGuid) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		sb.append("select e.eventGuid from DMI_ET.dbo.ET_Events e with (nolock)\n");
		sb.append("	inner join DMI_ET.dbo.ET_EquipmentClasses c with (nolock)\n");
		sb.append("	on e.equipmentClassGuid = c.equipmentClassGuid\n");
		sb.append("where 1=1\n");
		sb.append("and c.EquipmentClassGUID = :equipmentClassGuid\n");

		var p = new HashMap<String, Object>();
		p.put("equipmentClassGuid", equipmentClassGuid);
		return query(sb.toString(), p, new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getString(1);
			}
		});
//		return queryForList(sb.toString(), p, String.class);
	}
}
