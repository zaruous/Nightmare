/**
 * 
 */
package com.kyj.fx.nightmare.actions.grid;

import java.util.List;
import java.util.Map;

import com.kyj.fx.nightmare.actions.ai.AIDataDAO;
import com.kyj.fx.nightmare.comm.AbstractDAO;
import com.kyj.fx.nightmare.comm.DbUtil;

/**
 * 
 */
public class DataSourceDAO extends AbstractDAO {
	private String aliasName;
	public DataSourceDAO(String aliasName) throws Exception {
		this.aliasName = aliasName;
		
		var ds = AIDataDAO.getInstance();
		String statement = """
				select * from datasource where 1=1 and ALIAS_NAME = :aliasName
				""";
		List<Map<String, Object>> query = ds.query(statement, Map.of("aliasName", aliasName));
		if (query.isEmpty())
			throw new RuntimeException("no matched aliasName");

		Map<String, Object> map = query.get(0);

		setDataSource(DbUtil.getDataSource(map.get("DRIVER").toString(), map.get("URL").toString(),
				map.get("USER_ID").toString(), map.get("USER_PWD").toString()));
	}

}
