/**
 * 
 */
package com.kyj.fx.nightmare.comm.db.mariadb;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Predicate;

/**
 * 
 */
public class MariaDBUtil {
	/**
	 * 테이블 스크립트 export
	 * 
	 * @param conn
	 * @param out
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void exportTables(Connection conn, OutputStream out) throws SQLException, IOException {
		exportTables(conn, out, a -> true);
	}

	/**
	 * @param conn
	 * @param out
	 * @param tableNameFilter
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void exportTables(Connection conn, OutputStream out, Predicate<String> tableNameFilter)
			throws SQLException, IOException {

		if (conn == null)
			throw new SQLException("connection is null");
		if (out == null)
			throw new FileNotFoundException("out is null");

		try (PrintWriter writer = new PrintWriter(out)) {

			DatabaseMetaData metaData = conn.getMetaData();

			ResultSet tables = metaData.getTables(conn.getCatalog(), conn.getSchema(), "%", new String[] { "TABLE" });

			while (tables.next()) {
				String tableName = tables.getString("TABLE_NAME");
				if (!tableNameFilter.test(tableName))
					continue;

				writer.println("-- Table structure for " + tableName);
				writer.println("DROP TABLE IF EXISTS `" + tableName + "`;");

				try (Statement stmt = conn.createStatement()) {
					ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE " + tableName);
					if (rs.next()) {
						String createTableStmt = rs.getString(2);
						writer.println(createTableStmt + ";");
					}
				}

				writer.println();
			}
		}
	}

	/**
	 * @param conn
	 * @param tableName
	 * @param out
	 * @throws SQLException
	 */
	public static void exportData(Connection conn, String tableName, OutputStream out) throws SQLException {
		try (PrintWriter writer = new PrintWriter(out)) {
			writer.println("\n-- Data for table " + tableName);

			try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + tableName)) {
				ResultSet rs = stmt.executeQuery();
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();

				while (rs.next()) {
					writer.print("INSERT INTO `" + tableName + "` VALUES (");
					for (int i = 1; i <= columnCount; i++) {
						if (i > 1)
							writer.print(", ");
						Object value = rs.getObject(i);
						if (value == null) {
							writer.print("NULL");
						} else if (value instanceof String) {
							writer.print("'" + value.toString().replace("'", "''") + "'");
						} else {
							writer.print(value.toString());
						}
					}
					writer.println(");");
				}
			}
		}

	}
}
