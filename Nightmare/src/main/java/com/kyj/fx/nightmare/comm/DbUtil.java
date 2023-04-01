/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.comm
 *	작성일   : 2021. 11. 22.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.nightmare.comm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import javax.sql.DataSource;

import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class DbUtil {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DbUtil.class);

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 22.
	 * @param sql
	 */
	public static void noticeQuery(String sql) {
		LOGGER.debug(sql);
	}

	public static DataSource getDataSource() throws Exception {
		return getDataSource(getDriver(), getUrl(), getUserId(), getPassword());
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 22.
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException, Exception {
		return getDataSource().getConnection();
	}

	private static String getPassword() {
		return ResourceLoader.getInstance().get("db.dev.password");
	}

	private static String getUserId() {
		return ResourceLoader.getInstance().get("db.dev.userid");
	}

	private static String getUrl() {
		return ResourceLoader.getInstance().get("db.dev.url");
	}

	private static String getDriver() {
		return ResourceLoader.getInstance().get("db.dev.driver");
	}

	private static Map<String, DataSource> dataSourceCache = new ConcurrentHashMap<>();

	public static DataSource getDataSource(String driver, String url, String id, String pass) throws Exception {
		return getDataSource(driver, url, id, pass, null);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 4.
	 * @param driver
	 * @param url
	 * @param id
	 * @param pass
	 * @param handler
	 * @return
	 * @throws Exception
	 */
	public static DataSource getDataSource(String driver, String url, String id, String pass, Consumer<DataSource> handler)
			throws Exception {
		final String key = String.format("%s^%s^%s^%s", driver, url, id, pass);
		DataSource dataSource = null;

		synchronized (dataSourceCache) {
			if (dataSourceCache.containsKey(key)) {
				dataSource = dataSourceCache.get(key);
//				dataSource.checkAbandoned();
			}

			if (dataSource == null) {
				// 비밀번호는 입력안하는경우도 있기때문에 검증에서 제외
				/*
				 * 2016-08-10 id도 입력안하는 경우가 있음 sqlite. by kyj
				 */
				if (ValueUtil.isEmpty(driver) || ValueUtil.isEmpty(url)) {
					throw new Exception("Driver or url is empty.");
				}

				HikariConfig configuration = new HikariConfig();
				configuration.setDriverClassName(driver);
				configuration.setJdbcUrl(url);
				configuration.setUsername(id);
				configuration.setPassword(pass);
				configuration.setAutoCommit(false);
				configuration.setMaximumPoolSize(2);
				configuration.setConnectionTimeout(30000);
				configuration.setValidationTimeout(30000);
				configuration.setConnectionInitSql("select 1 ");
				configuration.setConnectionTestQuery("select 1");
				
				
				
				dataSource = new HikariDataSource(configuration);
//				dataSource.setDriverClassName(driver);
//				dataSource.setUrl(url);
//				dataSource.setUsername(id);
//				dataSource.setPassword(pass);
//				dataSource.setDefaultAutoCommit(false);
//				dataSource.setInitialSize(2);
//				dataSource.setLoginTimeout(3);
//				dataSource.setTestOnConnect(true);
//				dataSource.setTestOnBorrow(true);
//				dataSource.setValidationQuery("select 1");
				
				if (handler != null)
					handler.accept(dataSource);

				dataSourceCache.put(key, dataSource);

			}
		}
		return dataSource;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 22.
	 * @param driver
	 * @param url
	 * @param id
	 * @param pass
	 * @param handler
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection(String driver, String url, String id, String pass, Consumer<DataSource> handler)
			throws SQLException {
		final String key = String.format("%s^%s^%s^%s", driver, url, id, pass);

		if (dataSourceCache.containsKey(key)) {
			DataSource dataSource = dataSourceCache.get(key);
//			dataSource.checkAbandoned();
			return dataSource.getConnection();
		} else {
//			DataSource dataSource = new DataSource();
//			dataSource.setDriverClassName(driver);
//			dataSource.setUrl(url);
//			dataSource.setUsername(id);
//			dataSource.setPassword(pass);
//			dataSource.setDefaultAutoCommit(false);
//			dataSource.setInitialSize(1);
//			dataSource.setLoginTimeout(3);
			
			HikariConfig configuration = new HikariConfig();
			configuration.setDriverClassName(driver);
			configuration.setJdbcUrl(url);
			configuration.setUsername(id);
			configuration.setPassword(pass);
			configuration.setAutoCommit(false);
			configuration.setMaximumPoolSize(2);
			configuration.setConnectionTimeout(3000);
			configuration.setValidationTimeout(3000);
			configuration.setConnectionInitSql("select 1 ");
			configuration.setConnectionTestQuery("select 1");
			DataSource dataSource = new HikariDataSource(configuration);
			
			dataSourceCache.put(key, dataSource);
			return dataSource.getConnection();
		}
	}

	public static List<Map<String, Object>> select(Connection con, final String sql) throws Exception {
		return select(con, sql, 10);
	}

	static BiFunction<Connection, String, PreparedStatement> DEFAULT_PREPAREDSTATEMENT_CONVERTER = (c, sql) -> {
		try {
			return c.prepareStatement(sql);
		} catch (Exception e) {
			throw new RuntimeException(e);
			// return null;
		}
	};

	/**
	 * SQL을 실행하고 결과를 반환
	 *
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> select(final Connection con, final String sql, int fetchCount) throws Exception {
		return select(con, sql, fetchCount, -1, new ResultSetToMapConverter());
	}

	public static <T> List<Map<String, T>> select(final Connection con, final String sql, int fetchCount, int limitedSize,
			BiFunction<ResultSetMetaData, ResultSet, List<Map<String, T>>> convert) throws Exception {
		return select(con, sql, fetchCount, limitedSize, DEFAULT_PREPAREDSTATEMENT_CONVERTER, convert);
	}

	public static <T> List<Map<String, T>> select(final Connection con, final String sql, int fetchCount, int limitedSize,
			BiFunction<Connection, String, PreparedStatement> prestatementConvert,
			BiFunction<ResultSetMetaData, ResultSet, List<Map<String, T>>> convert) throws Exception {
		List<Map<String, T>> arrayList = Collections.emptyList();

		try {

			noticeQuery(sql);

			PreparedStatement prepareStatement = null;
			ResultSet executeQuery = null;

			/* 쿼리 타임아웃 시간 설정 SEC */
			// int queryTimeout = getQueryTimeout();

			prepareStatement = prestatementConvert.apply(con, sql); // con.prepareStatement(sql);
			// postgre-sql can't
			// prepareStatement.setQueryTimeout(queryTimeout);
			if (prepareStatement != null) {
				if (!(limitedSize <= 0)) {
					prepareStatement.setMaxRows(limitedSize);
				}

				if (fetchCount > 0) {
					prepareStatement.setFetchSize(fetchCount);
				}
				executeQuery = prepareStatement.executeQuery();

				ResultSetMetaData metaData = executeQuery.getMetaData();

				arrayList = convert.apply(metaData, executeQuery);
			}

		} catch (Throwable e) {
			throw e;
		}
		// finally {
		// close();
		// }

		return arrayList;
	}
}
