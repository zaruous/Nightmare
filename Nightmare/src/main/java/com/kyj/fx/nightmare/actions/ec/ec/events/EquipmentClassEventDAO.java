/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.events
 *	작성일   : 2021. 11. 30.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.ec.ec.events;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.kyj.fx.nightmare.comm.AbstractDAO;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class EquipmentClassEventDAO extends AbstractDAO {

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 11. 30.
	 * @return
	 */
	public List<EtEventsDVO> getEvents(String equipmentClassGuid) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		sb.append(" select\n");
		sb.append("	e.name\n");
		sb.append("	,e.EventGUID\n");
		sb.append("	,e.Description\n");
		sb.append("	,e.TrueStateText\n");
		sb.append("	,e.FalseStateText\n");
		sb.append("	,e.DefaultState\n");
		sb.append("	,e.EventType\n");
		sb.append("	,e.DisplayExecution\n");
		sb.append("	,e.DisplayEventStates\n");
		sb.append("	,e.TTCInterval --standard time for complete\n");
		sb.append("	/*  TTCIntervalType\n");
		sb.append("		standard time  for complete type\n");
		sb.append("		0:minute\n");
		sb.append("		1:hours\n");
		sb.append("		2:days\n");
		sb.append("		3:weeks\n");
		sb.append("		4:months\n");
		sb.append("		5:years\n");
		sb.append("	*/\n");
		sb.append("	,e.TTCIntervalType \n");
		sb.append("\n");
		sb.append("	/*\n");
		sb.append("		ScheduleType  \n");
		sb.append("		0: do not schedule\n");
		sb.append("		1: scheduled date\n");
		sb.append("		2: performed date\n");
		sb.append("	*/\n");
		sb.append("	, e.ScheduleType \n");
		sb.append("	, e.ScheduleInterval\n");
		sb.append("	/*\n");
		sb.append("		\n");
		sb.append("		0:hours\n");
		sb.append("		1:days\n");
		sb.append("		2:weeks\n");
		sb.append("		3:months\n");
		sb.append("		4:years\n");
		sb.append("	*/\n");
		sb.append("	, e.ScheduleIntervalType\n");
		sb.append("  from dmi_et.dbo.ET_Events e (nolock)\n");
		sb.append(" WHERE 1=1\n");
		sb.append(" AND e.EquipmentClassGUID = :equipmentClassGuid \n");
		sb.append(" and Disabled = 0\n");
		var m = new HashMap<String, Object>();
		m.put("equipmentClassGuid", equipmentClassGuid);
		return query(sb.toString(), m, new BeanPropertyRowMapper<EtEventsDVO>(EtEventsDVO.class));
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 11. 30.
	 * @return
	 */
	public EtEventsDVO getEvent(String eventGuid) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		sb.append(" select\n");
		sb.append("	e.name\n");
		sb.append("	,e.EventGUID\n");
		sb.append("	,e.Description\n");
		sb.append("	,e.TrueStateText\n");
		sb.append("	,e.FalseStateText\n");
		sb.append("	,e.DefaultState\n");
		sb.append("	,e.EventType\n");
		sb.append("	,e.DisplayExecution\n");
		sb.append("	,e.DisplayEventStates\n");
		sb.append("	,e.TTCInterval --standard time for complete\n");
		sb.append("	/*  TTCIntervalType\n");
		sb.append("		standard time  for complete type\n");
		sb.append("		0:minute\n");
		sb.append("		1:hours\n");
		sb.append("		2:days\n");
		sb.append("		3:weeks\n");
		sb.append("		4:months\n");
		sb.append("		5:years\n");
		sb.append("	*/\n");
		sb.append("	,e.TTCIntervalType \n");
		sb.append("\n");
		sb.append("	/*\n");
		sb.append("		ScheduleType  \n");
		sb.append("		0: do not schedule\n");
		sb.append("		1: scheduled date\n");
		sb.append("		2: performed date\n");
		sb.append("	*/\n");
		sb.append("	, e.ScheduleType \n");
		sb.append("	, e.ScheduleInterval\n");
		sb.append("	/*\n");
		sb.append("		\n");
		sb.append("		0:hours\n");
		sb.append("		1:days\n");
		sb.append("		2:weeks\n");
		sb.append("		3:months\n");
		sb.append("		4:years\n");
		sb.append("	*/\n");
		sb.append("	, e.ScheduleIntervalType\n");
		sb.append("  from dmi_et.dbo.ET_Events e (nolock)\n");
		sb.append(" WHERE 1=1\n");
		sb.append(" AND e.EventGUID = :eventGuid \n");
		sb.append(" and Disabled = 0\n");
		var m = new HashMap<String, Object>();
		m.put("eventGuid", eventGuid);

		
		return queryScala(sb.toString(), m, new ResultSetExtractor<EtEventsDVO>() {

			@Override
			public EtEventsDVO extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if(arg0.next())
					return new BeanPropertyRowMapper<EtEventsDVO>(EtEventsDVO.class).mapRow(arg0, 0);
				return null;
			}
		});
	}
}
