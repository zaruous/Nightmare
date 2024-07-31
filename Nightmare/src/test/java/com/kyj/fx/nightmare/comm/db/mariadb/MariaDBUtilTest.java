/**
 * 
 */
package com.kyj.fx.nightmare.comm.db.mariadb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import com.kyj.fx.nightmare.comm.DbUtil;

/**
 * 
 */
class MariaDBUtilTest {

	/**
	 * Test method for
	 * {@link com.kyj.fx.nightmare.comm.db.mariadb.MariaDBUtil#exportData(java.sql.Connection, java.lang.String, java.io.OutputStream)}.
	 * 
	 * @throws Exception
	 * @throws SQLException
	 */
	@Test
	void testExportData() throws SQLException, Exception {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			MariaDBUtil.exportData(DbUtil.getConnection(), "datasource", out);
			System.out.println(out.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

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
			e.printStackTrace();
		}
	}
}
