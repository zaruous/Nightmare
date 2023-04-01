/********************************
 *	프로젝트 : api-service
 *	패키지   : dao.equipment
 *	작성일   : 2020. 8. 13.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.eqtree;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import com.kyj.fx.b.ETScriptHelper.actions.ec.ec.scripts.EquipmentScriptDVO;
import com.kyj.fx.b.ETScriptHelper.comm.AbstractDAO;
import com.kyj.fx.b.ETScriptHelper.comm.DbUtil;
import com.kyj.fx.b.ETScriptHelper.comm.Hex;
import com.kyj.fx.b.ETScriptHelper.comm.ValueUtil;
import com.kyj.fx.nightmare.EquipmentDVO;
import com.kyj.fx.nightmare.EquipmentEventDVO;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EquipmentDAO {

	public EquipmentDAO() throws Exception {
		super();
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2022. 7. 22. 
	 * @param hashMap
	 * @param rowMapper
	 * @return
	 * @throws Exception
	 */
	public List<EtConfigurationTreeItem> listWorkflowInstance(Map<String, Object> hashMap,
			RowMapper<EtConfigurationTreeItem> rowMapper) throws Exception {
		EquipmentDQM d = new EquipmentDQM();
		d.setDataSource(getDataSource());
		return d.listWorkflowInstance(hashMap, rowMapper);
	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2022. 7. 21.
	 * @param hashMap
	 * @return
	 * @throws Exception
	 */
	public List<WorkflowDVO> listWorkflow(Map<String, Object> hashMap) throws Exception {
		EquipmentDQM d = new EquipmentDQM();
		d.setDataSource(getDataSource());
		return d.listWorkflow(hashMap);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2022. 7. 21.
	 * @param hashMap
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> listWorkflow(Map<String, Object> hashMap, RowMapper<T> mapper) throws Exception {
		EquipmentDQM d = new EquipmentDQM();
		d.setDataSource(getDataSource());
		return d.listWorkflow(hashMap, mapper);
	}

	/**
	 * @param hashMap
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2020. 8. 13.
	 * @return
	 * @throws Exception
	 */
	public List<EquipmentDVO> listEquipment(Map<String, Object> hashMap) throws Exception {
		EquipmentDQM d = new EquipmentDQM();
		d.setDataSource(getDataSource());
		return d.listEquipment(hashMap);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 22.
	 * @param <T>
	 * @param hashMap
	 * @param mapper
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> listEquipment(Map<String, Object> hashMap, RowMapper<T> mapper) throws Exception {
		EquipmentDQM d = new EquipmentDQM();
		d.setDataSource(getDataSource());
		return d.listEquipment(hashMap, mapper);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 22.
	 * @return
	 * @throws Exception
	 */
	private DataSource getDataSource() throws Exception {
		return DbUtil.getDataSource();
	}

	/**
	 * listEvent <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2020. 9. 2.
	 * @param hashMap
	 * @return
	 * @throws Exception
	 */
	public List<EquipmentEventDVO> listEventStatus(Map<String, Object> hashMap) throws Exception {
		EquipmentDQM d = new EquipmentDQM();
		d.setDataSource(getDataSource());
		return d.listEventStatus(hashMap);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 22.
	 * @param <T>
	 * @param hashMap
	 * @param mapper
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> listEvent(Map<String, Object> hashMap, RowMapper<T> mapper) throws Exception {
		EquipmentDQM d = new EquipmentDQM();
		d.setDataSource(getDataSource());
		return d.listEvent(hashMap, mapper);
	}

	/**
	 * @param hashMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> listEventParameters(Map<String, Object> hashMap) throws Exception {
		EquipmentDQM d = new EquipmentDQM();
		d.setDataSource(getDataSource());
		return d.listEventParameters(hashMap);
	}

	public List<EquipmentScriptDVO> getEquipmentScript(Map<String, Object> map) throws Exception {
		EquipmentDQM d = new EquipmentDQM();
		d.setDataSource(getDataSource());
		return d.getEquipmentScript(map);
	}

	public List<String> getEventScript(String eventScriptGuid, String scriptGuid) throws Exception {
		EquipmentDQM d = new EquipmentDQM();
		d.setDataSource(getDataSource());

		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("eventguid", eventScriptGuid);
		param.put("scriptguid", scriptGuid);

		return d.getEventScript(param);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 19.
	 * @param m
	 * @return
	 * @throws Exception
	 */
	public List<EquipmentClassDVO> listEquipmentClass(Map<String, Object> m) throws Exception {
		EquipmentDQM d = new EquipmentDQM();
		d.setDataSource(getDataSource());

		// HashMap<String, Object> param = new HashMap<String, Object>();
		// param.put("eventguid", eventScriptGuid);
		// param.put("scriptguid", scriptGuid);

		return d.listEquipmentClass(m);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 9.
	 * @param hashMap
	 * @param rowMapper
	 * @return
	 * @throws Exception
	 */
	public List<EtConfigurationTreeItem> listEquipmentEvent(HashMap<String, Object> hashMap, RowMapper<EtConfigurationTreeItem> rowMapper)
			throws Exception {
		EquipmentDQM d = new EquipmentDQM();
		d.setDataSource(getDataSource());
		return d.listEquipmentEvent(hashMap, rowMapper);
	}

	class EquipmentDQM extends AbstractDAO {

		/**
		 * @작성자 : KYJ (callakrsos@naver.com)
		 * @작성일 : 2021. 11. 19.
		 * @param m
		 * @return
		 */
		public List<EquipmentClassDVO> listEquipmentClass(Map<String, Object> m) {
			StringBuffer sb = new StringBuffer();
			sb.append("select \n");
			sb.append("EquipmentClassGUID\n");
			sb.append(",Name\n");
			sb.append(" from DMI_ET.dbo.ET_EquipmentClasses\n");
			sb.append("where 1=1\n");
			sb.append("and disabled = 0\n");
			sb.append("#if($name)\n");
			sb.append("and name like '%' + :name + '%'\n");
			sb.append("#end\n");

			return query(sb.toString(), m, new RowMapper<EquipmentClassDVO>() {

				@Override
				public EquipmentClassDVO mapRow(ResultSet rs, int rowNum) throws SQLException {
					EquipmentClassDVO d = new EquipmentClassDVO(rs.getString("EquipmentClassGUID"), rs.getString("Name"));
					return d;
				}
			});
		}

		/**
		 * @작성자 : KYJ (callakrsos@naver.com)
		 * @작성일 : 2022. 7. 21.
		 * @param hashMap
		 * @return
		 */
		

		public List<WorkflowDVO> listWorkflow(Map<String, Object> hashMap) {
			StringBuffer sb = new StringBuffer();
			sb.append("\n");
			sb.append("\n");
			sb.append("select \n");
			sb.append("a. WorkflowGuid\n");
			sb.append(", a.name\n");
			sb.append(", a.description\n");
			sb.append(", a.documentName\n");
			sb.append("from DMI_ET.dbo.ET_WorkFlows(nolock) a\n");
			sb.append("where 1=1 \n");
			sb.append("and EquipmentClassGuid = :equipmentClassGuid\n");
			sb.toString();

			return query(sb.toString(), hashMap, new BeanPropertyRowMapper<>(WorkflowDVO.class));
		}
		
		/**
		 * @작성자 : KYJ (callakrsos@naver.com)
		 * @작성일 : 2022. 7. 21. 
		 * @param <T>
		 * @param hashMap
		 * @param mapper
		 * @return
		 */
		public <T> List<T>   listWorkflow(Map<String, Object> hashMap, RowMapper<T> mapper) {
			StringBuffer sb  = new StringBuffer();
			sb.append("select \n");
			sb.append("a. WorkflowGuid\n");
			sb.append(", a.name\n");
			sb.append(", a.description\n");
			sb.append(", a.documentName\n");
			sb.append("from DMI_ET.dbo.ET_WorkFlows(nolock) a\n");
			sb.append("where 1=1 \n");
			sb.append("and EquipmentClassGuid = :equipmentClassGuid\n");
			return query(sb.toString(), hashMap, mapper);
		}

		/**
		 * @작성자 : KYJ (callakrsos@naver.com)
		 * @작성일 : 2021. 11. 22.
		 * @param hashMap
		 * @return
		 */
		public List<EquipmentDVO> listEquipment(Map<String, Object> hashMap) {
			return listEquipment(hashMap, new RowMapper<EquipmentDVO>() {
				@Override
				public EquipmentDVO mapRow(ResultSet rs, int rowNum) throws SQLException {
					EquipmentDVO equipmentDVO = new EquipmentDVO(rs.getString("EquipmentGUID"), rs.getString("EuipmentName"));
					equipmentDVO.setEquipmentClassName(rs.getString("Name"));
					return equipmentDVO;
				}
			});
		}

		/**
		 * @param hashMap
		 * @작성자 : KYJ (callakrsos@naver.com)
		 * @작성일 : 2020. 8. 13.
		 * @return
		 */
		public <T> List<T> listEquipment(Map<String, Object> hashMap, RowMapper<T> mapper) {
			StringBuffer sb = new StringBuffer();
			sb.append("Select\n");
			sb.append("    ET.[Name],\n");
			sb.append("    EQ.EquipmentGUID,\n");
			sb.append("    EQ.ParentGUID,\n");
			sb.append("    EQ.EquipmentGUID,\n");
			sb.append("    EQ.NAME as EuipmentName\n");
			// sb.append(" ET.[Description]\n");
			sb.append("\n");
			sb.append("from\n");
			sb.append("    [DMI_ET].[dbo].[ET_EquipmentClasses] ET (nolock) \n");
			sb.append("\n");
			sb.append("\n");
			sb.append("Inner join\n");
			sb.append("    [DMI_ET].[dbo].[ET_Equipment] EQ (nolock)\n");
			sb.append("	on EQ.EquipmentClassGUID = ET.EquipmentClassGUID\n");
			sb.append("	AND EQ.Deleted = 0\n");
			sb.append("WHERE 1=1\n");

			sb.append("#if($equipmentClassGuid and $equipmentClassGuid.length() > 0 )\n");
			sb.append("and ET.EquipmentClassGUID = :equipmentClassGuid \n");
			sb.append("and EQ.ParentGUID = '' \n");
			sb.append("#end\n");

			sb.append("#if($equipmentClassName and $equipmentClassName.length() > 0 )\n");
			sb.append("and ET.NAME like '%' + :equipmentClassName + '%' \n");
			sb.append("and EQ.ParentGUID = '' \n");
			sb.append("#end\n");

			sb.append("#if($parentEquipmentGuid and $parentEquipmentGuid.length() > 0 )\n");
			sb.append("and EQ.ParentGUID = :parentEquipmentGuid \n");
			sb.append("#end\n");

			sb.append("order by\n");
			sb.append("    eq.[name]\n");

			return query(sb.toString(), hashMap, mapper);
		}

		/**
		 * @작성자 : KYJ (callakrsos@naver.com)
		 * @작성일 : 2021. 7. 16.
		 * @param eventScriptGuid
		 * @return
		 */
		public List<String> getEventScript(HashMap<String, Object> param) {
			StringBuffer sb = new StringBuffer();
			sb.append("select\n");
			sb.append("Code AS CODE \n");
			sb.append("from DMI_ET.dbo.ET_EventScripts with (nolock) \n");
			sb.append("WHERE 1=1 AND eventguid = :eventguid\n");
			sb.append(" AND scriptguid = :scriptguid\n");

			return query(sb.toString(), param, new RowMapper<String>() {

				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getString("CODE");
				}
			});

			// return query(sb.toString(), param, new );
		}

		/**
		 * @작성자 : KYJ (callakrsos@naver.com)
		 * @작성일 : 2020. 9. 3.
		 * @param hashMap
		 * @return
		 */
		public <T> List<T> listEvent(Map<String, Object> hashMap, RowMapper<T> mapper) {
			StringBuffer sb = new StringBuffer();
			sb.append("select\n");
			sb.append("A.EventGUID\n");
			sb.append(", A.EquipmentClassGUID AS EQUIPMENT_CLASS_GUID\n");
			sb.append(", A.Name AS EVENT_NAME\n");
			sb.append(", A.Description\n");
			sb.append(", A.EventType AS EVENT_TYPE\n");
			sb.append(", A.Disabled AS EVENT_DISABLED\n");
			sb.append("\n");
			sb.append("\n");
			sb.append(",A.EventType \n");
			sb.append(",A.TrueStateText \n");
			sb.append(",A.FalseStateText \n");
			sb.append(",A.DisplayExecution \n");
			sb.append(",A.DisplayEventStates \n");
			sb.append("\n");
			sb.append("from DMI_ET.dbo.ET_Events  A with (nolock) \n");
			sb.append("WHERE 1=1\n");
			sb.append("AND A.EquipmentClassGUID  = :equipmentClassGuid\n");
			sb.append("order by A.Name\n");
			return query(sb.toString(), hashMap, mapper);
		}

		/**
		 * @작성자 : KYJ (callakrsos@naver.com)
		 * @작성일 : 2021. 12. 9.
		 * @param <T>
		 * @param hashMap
		 * @param mapper
		 * @return
		 */
		public <T> List<T> listEquipmentEvent(Map<String, Object> hashMap, RowMapper<T> mapper) {
			StringBuffer sb = new StringBuffer();
			sb.append("select\n");
			sb.append("A.EventGUID\n");
			sb.append(", A.EquipmentClassGUID AS EQUIPMENT_CLASS_GUID\n");
			sb.append(", eq.EquipmentGUID as EQUIPMENT_GUID\n");
			sb.append(", eq.name as EQUIPMENT_NAME\n");
			sb.append(", A.Name AS EVENT_NAME\n");
			sb.append(", A.Description\n");
			sb.append(", A.EventType AS EVENT_TYPE\n");
			sb.append(", A.Disabled AS EVENT_DISABLED\n");
			sb.append(",A.EventType \n");
			sb.append(",A.TrueStateText \n");
			sb.append(",A.FalseStateText \n");
			sb.append(",A.DisplayExecution\n");
			sb.append(",A.DisplayEventStates \n");
			sb.append("\n");
			sb.append("from DMI_ET.dbo.ET_Events  A inner join DMI_ET.dbo.ET_EquipmentClasses ec \n");
			sb.append("on a.EquipmentClassGUID = ec.EquipmentClassGUID\n");
			sb.append("and ec.Disabled = 0\n");
			sb.append("inner join DMI_ET.dbo.ET_Equipment eq\n");
			sb.append("on ec.EquipmentClassGUID = eq.EquipmentClassGUID\n");
			sb.append("and eq.Deleted = 0\n");
			sb.append("WHERE 1=1\n");
			sb.append("AND eq.EquipmentGUID  = :equipmentGuid\n");
			sb.append("and A.DisplayExecution  = 1\n");
			sb.append("order by A.Name;\n");
			sb.toString();
			return query(sb.toString(), hashMap, mapper);
		}

		/**
		 * @작성자 : KYJ (callakrsos@naver.com)
		 * @작성일 : 2020. 9. 3.
		 * @param hashMap
		 * @return
		 */
		public List<EquipmentEventDVO> listEventStatus(Map<String, Object> hashMap) {
			StringBuffer sb = new StringBuffer();
			sb.append("			select\n");
			sb.append("			E.EquipmentGUID\n");
			sb.append("			, E.NAME AS EQUIPMENT_NAME\n");
			sb.append("			, A.EventGUID\n");
			sb.append("			, A.EquipmentClassGUID AS EQUIPMENT_CLASS_GUID\n");
			sb.append("			, A.Name AS EVENT_NAME\n");
			sb.append("			, A.Description\n");
			sb.append("			, A.EventType AS EVENT_TYPE\n");
			sb.append("			, A.Disabled AS EVENT_DISABLED\n");
			sb.append("			,ee.State as STATUS_VALUE \n");

			sb.append("			,A.EventType \n");
			sb.append("			,A.TrueStateText \n");
			sb.append("			,A.FalseStateText \n");
			sb.append("			,A.DisplayExecution \n");
			sb.append("			,A.DisplayEventStates \n");

			sb.append("			from DMI_ET.dbo.ET_Events  A with (nolock)  INNER JOIN DMI_ET.dbo.ET_Equipment  E with (nolock) \n");
			sb.append("				ON A.EquipmentClassGUID = E.EquipmentClassGUID\n");
			sb.append("	AND E.EquipmentGUID = :equipmentGuid\n");
			sb.append("			left join DMI_ET.dbo.ET_EquipmentEvents EE\n");
			sb.append("				on A.EventGUID = EE.EventGUID\n");
			sb.append("				and E.EquipmentGUID = EE.EquipmentGUID\n");
			sb.append("			WHERE 1=1\n");
			sb.append("			order by A.Name\n");

			return query(sb.toString(), hashMap, new RowMapper<EquipmentEventDVO>() {

				@Override
				public EquipmentEventDVO mapRow(ResultSet rs, int rowNum) throws SQLException {

					EquipmentEventDVO equipmentEventDVO = new EquipmentEventDVO(rs.getString("EQUIPMENT_CLASS_GUID"),
							rs.getString("EquipmentGUID"), rs.getString("EventGUID"), rs.getString("EVENT_NAME"));
					equipmentEventDVO.setEventType(rs.getString("EventType"));
					return equipmentEventDVO;
				}
			});
		}

		/**
		 * @param hashMap
		 * @return
		 */
		public List<Map<String, Object>> listEventParameters(Map<String, Object> hashMap) {
			StringBuffer sb = new StringBuffer();
			sb.append("\n");
			sb.append("SELECT * FROM  DMI_ET.dbo.ET_EventParameters\n");
			sb.append("WHERE 1=1 AND EVENTGUID = :eventGuid\n");
			sb.append("order by sequence ");

			return query(sb.toString(), hashMap);
		}

		public List<EquipmentScriptDVO> getEquipmentScript(Map<String, Object> hashMap) {
			StringBuffer sb = new StringBuffer();
			sb.append("with eee as ( select\n");
			sb.append("    a.Name [Name],\n");
			sb.append("    a.EventGUID,\n");
			sb.append("    a.EquipmentClassGUID,\n");
			sb.append("    d.Name [ActionName],\n");
			sb.append("    d.ScriptGUID \n");
			sb.append("from\n");
			sb.append("    DMI_ET.dbo.ET_Events a cross \n");
			sb.append("join\n");
			sb.append("    DMI_ET.dbo.ET_AvailableScripts d ) select\n");
			sb.append("    c.Name as [Equipment_Class_Name],\n");
			sb.append("    a.Name as [EventName],\n");
			sb.append("    a.EventGUID,\n");
			sb.append("    a.ActionName,\n");
			sb.append("    b.EventScriptGUID,\n");
			sb.append("    b.code,\n");
			sb.append("    b.eventguid,\n");
			sb.append("    a.EquipmentClassGUID, \n");
			sb.append("    a.ScriptGUID \n");
			sb.append("from\n");
			sb.append("    eee a \n");
			sb.append("left outer join\n");
			sb.append("    DMI_ET.dbo.ET_EventScripts b \n");
			sb.append("        on a.EventGUID = b.EventGUID \n");
			sb.append("        and a.ScriptGUID = b.ScriptGUID \n");
			sb.append("inner join\n");
			sb.append("    DMI_ET.dbo.ET_EquipmentClasses c \n");
			sb.append("        on a.EquipmentClassGUID = c.EquipmentClassGUID \n");
			sb.append("where 1=1\n");
			sb.append("\n");
			sb.append("	and c.EquipmentClassGUID = :equipmentClassGuid\n");
			sb.append("	and a.Name = :eventName\n");
			// sb.append(" and a.ActionName = :actionName\n");
			sb.append("\n");
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
		 * @작성자 : KYJ (callakrsos@naver.com)
		 * @작성일 : 2022. 7. 22. 
		 * @param <T>
		 * @param hashMap
		 * @param rowMapper
		 * @return
		 */
		public <T> List<T>  listWorkflowInstance(Map<String, Object> hashMap, RowMapper<T> rowMapper) {
			StringBuffer sb = new StringBuffer();
			sb.append("select top 300 * from dmi_et.dbo.ET_WorkFlowInstance a (nolock)\n");
			sb.append("where 1=1\n");
			sb.append("and a.EquipmentGUID = :equipmentGuid\n");
			sb.append("and a.WorkFlowGUID = :workflowId\n");
			sb.append("order by a.StartDate desc \n");
			
			return query(sb.toString(), hashMap, rowMapper );
		}
		
		/**
		 * @작성자 : KYJ (callakrsos@naver.com)
		 * @작성일 : 2022. 7. 22. 
		 * @param hashMap
		 * @return
		 */
		public List<WorkflowInstanceDVO>  listWorkflowInstance(Map<String, Object> hashMap) {
			StringBuffer sb = new StringBuffer();
			sb.append("\n");
			sb.append("\n");
			sb.append("\n");
			sb.append("select * from dmi_et.dbo.ET_WorkFlowInstance a (nolock)\n");
			sb.append("where 1=1\n");
			sb.append("and a.EquipmentClassGUID = :equipmentGuid\n");
			sb.append("and a.WorkFlowGUID = :workflowId\n");
			
			return query(sb.toString(), hashMap, new BeanPropertyRowMapper<WorkflowInstanceDVO>(WorkflowInstanceDVO.class) );
		}
	}



	

}
