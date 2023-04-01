/**
 * 
 */
package com.kyj.fx.nightmare.actions.ec.eq.hist;

import java.util.List;
import java.util.Map;

import com.kyj.fx.nightmare.comm.AbstractDAO;
import com.kyj.fx.nightmare.comm.MapBaseRowMapper;
import com.kyj.fx.nightmare.comm.template.Sql;
import com.kyj.fx.nightmare.comm.template.Template;

/**
 * @author KYJ
 *
 */
public class EventHsitroyDAO extends AbstractDAO {

	/**
	 * @param <T>
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> listEventHistory(Map<String, Object> param) throws Exception {
		Template load = Template.load(getClass().getResource("EventHistoryList.xml"));
		Sql sql = load.getSql();

		return query(sql.getContent(), param, new MapBaseRowMapper());
	}
}