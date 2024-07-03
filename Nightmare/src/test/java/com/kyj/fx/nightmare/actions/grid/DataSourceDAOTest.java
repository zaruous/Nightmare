/**
 * 
 */
package com.kyj.fx.nightmare.actions.grid;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * 
 */
class DataSourceDAOTest {

	/**
	 * Test method for {@link com.kyj.fx.nightmare.actions.grid.DataSourceDAO#DataSourceDAO(java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	void testDataSourceDAO() throws Exception {
		DataSourceDAO ds = new DataSourceDAO("investar");
		String sql = """
				select * from company_info limit 10
				""";
		List<Map<String, Object>> query = ds.query(sql, Collections.emptyMap());
		System.out.println(query.size());
		query.forEach(System.out::println);
	}

}
