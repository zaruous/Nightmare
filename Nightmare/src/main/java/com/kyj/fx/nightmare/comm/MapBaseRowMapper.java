/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.comm
 *	작성일   : 2021. 11. 22.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.comm;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class MapBaseRowMapper implements RowMapper<Map<String, Object>> {

	public Map<String, Object> newMapInstance() {
		return new LinkedHashMap<>();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet,
	 * int)
	 */
	@Override
	public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {

		Map<String, Object> map = newMapInstance();

		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {
			String columnName = metaData.getColumnName(i);
			Object value = rs.getObject(i);
			map.put(columnName, value);
		}

		return map;

	}

}
