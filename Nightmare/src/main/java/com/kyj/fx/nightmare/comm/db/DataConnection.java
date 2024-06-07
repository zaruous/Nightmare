package com.kyj.fx.nightmare.comm.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Consumer;

import javax.sql.DataSource;

import com.kyj.fx.nightmare.comm.DbUtil;
import com.kyj.fx.nightmare.comm.ResourceLoader;

public class MariaDbStore {

	/**
	 * @param handler
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection(Consumer<DataSource> handler) throws SQLException {
		String driver = ResourceLoader.getInstance().get("db.dev.driver");
		String url = ResourceLoader.getInstance().get("db.dev.url");
		String id = ResourceLoader.getInstance().get("db.dev.userid");
		String pwd = ResourceLoader.getInstance().get("db.dev.password");
		return DbUtil.getConnection(driver, url, id, pwd, handler);
	}

}
