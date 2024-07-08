/**
 * 
 */
package com.kyj.fx.nightmare.comm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import com.kyj.fx.nightmare.comm.db.mariadb.MariaDBUtil;

/**
 * 
 */
class DbUtilTest {

	/**
	 * Test method for
	 * {@link com.kyj.fx.nightmare.comm.DbUtil#exportTables(java.sql.Connection, java.io.File)}.
	 * 
	 * @throws Exception
	 * @throws SQLException
	 */
	@Test
	void testExportTables() throws SQLException, Exception {

		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			MariaDBUtil.exportTables(DbUtil.getConnection(), out);
			System.out.println(out.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;

	}

}
