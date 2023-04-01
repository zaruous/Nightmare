/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec
 *	작성일   : 2021. 11. 23.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.ec.eq.states;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.kyj.fx.nightmare.comm.AbstractDAO;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EquipmentEventStatesDAO extends AbstractDAO {

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 24.
	 * @param param
	 * @return
	 */
	public List<EquipmentEventStateDVO> listEventStates(Map<String, Object> param) {

		StringBuffer sb = new StringBuffer();
		sb.append("select \n");
		sb.append("event.EquipmentClassGUID\n");
		sb.append(",ec.Name as EquipmentClassName\n");
		sb.append(",ee.EquipmentGUID\n");
		sb.append(",eq.Name as EquipmentName\n");
		sb.append(",ee.EquipmentEventGUID\n");
		sb.append(",event.EventGUID\n");
		sb.append(",event.name\n");
		sb.append(",event.description\n");
		sb.append(",case event.eventType when 0 then 'Transient'\n");
		sb.append("	when 1 then 'Start-Stop' \n");
		sb.append("	when 2 then 'System' \n");
		sb.append("   end 'EventTypeNm'\n");
		sb.append("\n");
		sb.append(",event.eventType\n");
		sb.append(",case ee.State when 1 then TrueStateText\n");
		sb.append("				else FalseStateText end StateName\n");
		sb.append(",ee.State\n");
		sb.append(",event.TrueStateText\n");
		sb.append(",event.FalseStateText\n");
		sb.append(" \n");
		sb.append("from DMI_ET.dbo.ET_EquipmentEvents ee (nolock)\n");
		sb.append("left join  DMI_ET.dbo.ET_Events [event] (nolock)\n");
		sb.append("on ee.eventguid = event.eventguid\n");
		sb.append("left join DMI_ET.dbo.ET_EquipmentClasses ec (nolock)\n");
		sb.append("on event.EquipmentClassGUID = ec.EquipmentClassGUID\n");
		sb.append("left join DMI_ET.dbo.ET_Equipment eq(nolock)\n");
		sb.append("on ee.EquipmentGUID = eq.EquipmentGUID\n");
		sb.append("\n");
		sb.append("where 1=1\n");

		sb.append("#if($equipmentGuid)\n");
		sb.append("and [ee].EquipmentGUID =:equipmentGuid \n");
		sb.append("#end\n");
		sb.append("#if($eventGuid)\n");
		sb.append("and [event].EventGUID = :eventGuid \n");
		sb.append("#end\n");
		sb.append("order by event.name\n");

		return query(sb.toString(), param,
				new BeanPropertyRowMapper<EquipmentEventStateDVO>(EquipmentEventStateDVO.class));

	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 29.
	 * @param equipmentClassName
	 * @param equipmentName
	 * @return
	 */
	public String getEquipmentGuid(String equipmentClassName, String equipmentName) {
		StringBuffer sb = new StringBuffer();
		sb.append(
				"select eq.EquipmentGUID from dmi_et.dbo.ET_Equipment eq inner join dmi_et.dbo.ET_EquipmentClasses ec\n");
		sb.append("	on eq.EquipmentClassGUID = ec.EquipmentClassGUID\n");
		sb.append("where 1=1\n");
		sb.append("and ec.Name = :equipmentClassName\n");
		sb.append("and eq.name = :equipmentName \n");

		var param = new HashMap<String, Object>();
		param.put("equipmentClassName", equipmentClassName);
		param.put("equipmentName", equipmentName);
		return queryScala(sb.toString(), param, new ResultSetExtractor<String>() {

			@Override
			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rs.getString("EquipmentGUID");
				return null;
			}
		});
	}
}
