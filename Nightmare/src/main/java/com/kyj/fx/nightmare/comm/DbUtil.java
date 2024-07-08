/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.comm
 *	작성일   : 2021. 11. 22.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.comm;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

import javax.sql.DataSource;

import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.transaction.support.TransactionTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class DbUtil {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DbUtil.class);

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 11. 22.
	 * @param sql
	 */
	public static void noticeQuery(String sql, Map<String, Object> paramMap) {
		LOGGER.debug("{}\nparameters:{}", sql, paramMap);
	}

	public static void noticeQuery(String sql) {
		LOGGER.debug("{}", sql);
	}

	public static void noticeQuery(String sql, Map[] array) {
		LOGGER.debug("{}\nParameters:\n", sql);
		for (Map m : array)
			LOGGER.debug("{}", m);
	}

	public static DataSource getDataSource() throws Exception {
		return getDataSource(getDriver(), getUrl(), getUserId(), getPassword());
	}
//	

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
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

	private static DataSource getDataSource(Connection con) throws Exception {
		SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource(con, true);
		return singleConnectionDataSource;
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
	public static DataSource getDataSource(String driver, String url, String id, String pass,
			Consumer<DataSource> handler) throws Exception {
		final String key = String.format("%s^%s^%s^%s", driver, url, id, pass);
		DataSource dataSource = null;

		synchronized (dataSourceCache) {
			if (dataSourceCache.containsKey(key)) {
				dataSource = dataSourceCache.get(key);
				// dataSource.checkAbandoned();
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
				configuration.setAutoCommit(true);
				configuration.setMaximumPoolSize(2);
				configuration.setConnectionTimeout(30000);
				configuration.setValidationTimeout(30000);
				configuration.setConnectionInitSql("select 1 ");
				configuration.setConnectionTestQuery("select 1");

				dataSource = new HikariDataSource(configuration);
				// dataSource.setDriverClassName(driver);
				// dataSource.setUrl(url);
				// dataSource.setUsername(id);
				// dataSource.setPassword(pass);
				// dataSource.setDefaultAutoCommit(false);
				// dataSource.setInitialSize(2);
				// dataSource.setLoginTimeout(3);
				// dataSource.setTestOnConnect(true);
				// dataSource.setTestOnBorrow(true);
				// dataSource.setValidationQuery("select 1");

				if (handler != null)
					handler.accept(dataSource);

				dataSourceCache.put(key, dataSource);

			}
		}
		return dataSource;
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 11. 22.
	 * @param driver
	 * @param url
	 * @param id
	 * @param pass
	 * @param handler
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection(String driver, String url, String id, String pass,
			Consumer<DataSource> handler) throws SQLException {
		final String key = String.format("%s^%s^%s^%s", driver, url, id, pass);

		if (dataSourceCache.containsKey(key)) {
			DataSource dataSource = dataSourceCache.get(key);
			// dataSource.checkAbandoned();
			return dataSource.getConnection();
		} else {
			// DataSource dataSource = new DataSource();
			// dataSource.setDriverClassName(driver);
			// dataSource.setUrl(url);
			// dataSource.setUsername(id);
			// dataSource.setPassword(pass);
			// dataSource.setDefaultAutoCommit(false);
			// dataSource.setInitialSize(1);
			// dataSource.setLoginTimeout(3);

			HikariConfig configuration = new HikariConfig();
			configuration.setDriverClassName(driver);
			configuration.setJdbcUrl(url);
			configuration.setUsername(id);
			configuration.setPassword(pass);
			configuration.setAutoCommit(true);
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
	public static List<Map<String, Object>> select(final Connection con, final String sql, int fetchCount)
			throws Exception {
		return select(con, sql, fetchCount, -1, new ResultSetToMapConverter());
	}

	public static <T> List<Map<String, T>> select(final Connection con, final String sql, int fetchCount,
			int limitedSize, BiFunction<ResultSetMetaData, ResultSet, List<Map<String, T>>> convert) throws Exception {
		return select(con, sql, fetchCount, limitedSize, DEFAULT_PREPAREDSTATEMENT_CONVERTER, convert);
	}

	public static <T> List<Map<String, T>> select(final Connection con, final String sql, int fetchCount,
			int limitedSize, BiFunction<Connection, String, PreparedStatement> prestatementConvert,
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

	public static Map<String, Object> selectOne(final Connection con, final String sql) throws Exception {
		return selectOne(con, sql, 1, 1, DEFAULT_PREPAREDSTATEMENT_CONVERTER, (meta, rs) -> {

			try {
				if (rs.next()) {
					MapBaseRowMapper mapBaseRowMapper = new MapBaseRowMapper();
					return mapBaseRowMapper.mapRow(rs, 0);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return Collections.emptyMap();
		});
	}

	public static Map<String, Object> selectOne(final Connection con, final String sql, int fetchCount, int limitedSize,
			BiFunction<Connection, String, PreparedStatement> prestatementConvert,
			BiFunction<ResultSetMetaData, ResultSet, Map<String, Object>> convert) throws Exception {

		Map<String, Object> ret = Collections.emptyMap();

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

				ret = convert.apply(metaData, executeQuery);
			}

		} catch (Throwable e) {
			throw e;
		}

		return ret;

	}

	public static List<String> pks(String tableNamePattern) throws Exception {
		return pks(getConnection(), null, null, tableNamePattern, t -> {
			try {
				return t.getString(4);
			} catch (SQLException e) {
				LOGGER.error(ValueUtil.toString(e));
			}
			return "";
		});
	}

	public static List<String> pks(Connection con, String tableNamePattern) throws Exception {
		return pks(con, null, null, tableNamePattern, t -> {
			try {
				return t.getString(4);
			} catch (SQLException e) {
				LOGGER.error(ValueUtil.toString(e));
			}
			return "";
		});
	}

	public static <T> List<T> pks(String tableNamePattern, Function<ResultSet, T> converter) throws Exception {
		List<T> tables = Collections.emptyList();
		try (Connection connection = getConnection()) {
			tables = pks(connection, null, null, tableNamePattern, converter);
		}
		return tables;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 11. 5.
	 * @param connection
	 * @param catalog
	 * @param schema
	 * @param tableNamePattern
	 * @return
	 * @throws Exception
	 */
	public static List<String> pks(Connection connection, String catalog, String schema, String tableNamePattern)
			throws Exception {
		return pks(connection, catalog, schema, tableNamePattern, t -> {
			try {
				return t.getString(4);
			} catch (SQLException e) {
				LOGGER.error(ValueUtil.toString(e));
			}
			return "";
		});
	}

	public static <T> List<T> pks(Connection connection, String catalog, String schema, String tableNamePattern,
			Function<ResultSet, T> converter) throws Exception {
		if (converter == null)
			throw new RuntimeException("converter is null ");

		List<T> tables = new ArrayList<>();

		DatabaseMetaData metaData = connection.getMetaData();

		ResultSet rs = PRIMARY_CONVERTER.apply(catalog, schema, tableNamePattern, metaData); // metaData.getPrimaryKeys(null,
		// null,
		// tableNamePattern);

		if (rs != null) {
			while (rs.next()) {
				tables.add(converter.apply(rs));
			}
		}

		return tables;
	}

	public static final Function<ResultSet, String> CAMEL_CONVERTER = (rs) -> {
		try {
			return ValueUtil.toCamelCase(rs.getString(4));
		} catch (SQLException e) {
			LOGGER.error(ValueUtil.toString(e));
		}
		return "";
	};

	public static final FourThFunction<String, String, String, DatabaseMetaData, ResultSet> PRIMARY_CONVERTER = (
			catalog, schema, tableNamePattern, metaData) -> {
		try {
			return metaData.getPrimaryKeys(catalog, schema, tableNamePattern);
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
		}
		return null;
	};

	public static final FourThFunction<String, String, String, DatabaseMetaData, ResultSet> PRIMARY_CONVERTER_CAMELCASE = (
			catalog, schema, tableNamePattern, metaData) -> {
		try {
			return metaData.getPrimaryKeys(catalog, schema, tableNamePattern);
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
		}
		return null;
	};

	/**
	 * 트랜잭션으로 감싸진 영역을 반환
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 29.
	 * @param consumer
	 * @throws Exception
	 */
	public static <T> int getTransactionedScope(T userObj, BiTransactionScope<T, NamedParameterJdbcTemplate> consumer) {
		return getTransactionedScope(userObj, consumer, null);
	}

	public static <T> int getTransactionedScope(T userObj, BiTransactionScope<T, NamedParameterJdbcTemplate> consumer,
			ExceptionHandler exceptionHandler) {

		DataSource defaultDataSource = null;
		try {
			defaultDataSource = getDataSource();
		} catch (Exception e) {
			if (exceptionHandler != null) {
				exceptionHandler.handle(e);
				return -1;
			} else
				LOGGER.error(ValueUtil.toString(e));
		}
		if (defaultDataSource == null) {
			RuntimeException ex = new RuntimeException("Default DataSource is null.");
			if (exceptionHandler != null) {
				exceptionHandler.handle(ex);
				return -1;
			} else
				throw ex;

		}

		return getTransactionedScope(defaultDataSource, userObj, consumer, exceptionHandler);
	}

	public static <T> int getTransactionedScope(DataSource dataSource, T userObj,
			BiTransactionScope<T, NamedParameterJdbcTemplate> consumer, ExceptionHandler exceptionHandler) {
		return getTransactionedScope(dataSource, userObj, consumer, true, exceptionHandler);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 7. 7.
	 * @param <T>
	 * @param con
	 * @param updateItems
	 * @param biTransactionScope
	 * @return
	 * @throws Exception
	 */
	public static <T> int getTransactionedScope(Connection con, T updateItems,
			BiTransactionScope<T, NamedParameterJdbcTemplate> biTransactionScope) throws Exception {
		return getTransactionedScope(getDataSource(con), updateItems, biTransactionScope, false,
				ExceptionHandler.getDefaultHandler());
	}

	public static <T> int getTransactionedScope(DataSource dataSource, T userObj,
			BiTransactionScope<T, NamedParameterJdbcTemplate> consumer, boolean autoClose,
			ExceptionHandler exceptionHandler) {
		// DataSource dataSource = null;
		try {
			// dataSource = getDataSource();
			NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

			TransactionTemplate template = new TransactionTemplate();
			DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
			template.setTransactionManager(transactionManager);

			return template.execute(status -> {
				int result = -1;
				try {
					result = consumer.scope(userObj, namedParameterJdbcTemplate);
				} catch (Exception e) {
					status.setRollbackOnly();
					LOGGER.error(ValueUtil.toString(e));
					if (exceptionHandler != null)
						exceptionHandler.handle(e);
					result = -1;
				}
				return result;
			});

		} catch (Exception e) {
			if (exceptionHandler != null)
				exceptionHandler.handle(e);
			else
				LOGGER.error(ValueUtil.toString(e));
		} finally {
			if (autoClose) {
				try {
					close(dataSource);
				} catch (Exception e) {
					LOGGER.error(ValueUtil.toString(e));
				}
			}
		}
		return -1;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 9. 18.
	 * @param con
	 * @param userObj
	 * @param sqlConverter
	 * @return
	 * @throws Exception
	 */
	public static <T> int getTransactionedScope(Connection con, T userObj, Function<T, List<String>> sqlConverter)
			throws Exception {
		return getTransactionedScope(con, userObj, sqlConverter, err -> {
			throw new RuntimeException(err);
		});

	}

	public static <T> int getTransactionedScope(Connection con, T userObj, Function<T, List<String>> sqlConverter,
			Consumer<Exception> exceptionHandler) throws Exception {
		int result = -1;
		try {
			LOGGER.debug("is AutoCommit ? : {}", con.getAutoCommit());
			con.setAutoCommit(false);
			List<String> apply = sqlConverter.apply(userObj);
			Statement createStatement = con.createStatement();
			for (String sql : apply) {

				/*
				 * sqlite에서 공백이 포함된 sql은 add한경우 에러. 확인해보니 isEmpty함수에 이상이 있는듯하여 수정.
				 */
				if (ValueUtil.isEmpty(sql))
					continue;

				LOGGER.debug(sql);
				createStatement.addBatch(sql);
			}

			int[] executeBatch = createStatement.executeBatch();

			con.commit();
			result = (int) IntStream.of(executeBatch).filter(v -> v == 0).count();
		} catch (Exception e) {
			con.rollback();
			exceptionHandler.accept(e);
			result = -1;
		} finally {
			con.commit();
			con.close();
		}
		return result;
	}

	/**
	 * 커넥션 종료
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 11. 17.
	 * @param con
	 * @throws Exception
	 */
	public static void close(javax.sql.DataSource dataSource) {
		if (dataSource != null) {
			try {

				if (dataSourceCache.containsValue(dataSource)) {
					Optional<Entry<String, DataSource>> findFirst = dataSourceCache.entrySet().stream()
							.filter(ent -> ent.getValue() == dataSource).findFirst();
					findFirst.ifPresent(v -> dataSourceCache.remove(v.getKey()));
				}

				LOGGER.debug("Close Connection.");
				close(dataSource.getConnection());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 커넥션 종료
	 *
	 * @Date 2015. 10. 16.
	 * @param con
	 * @throws Exception
	 * @User KYJ
	 */
	public static void close(Connection con) {
		if (con != null) {
			try {
				if (!con.isClosed()) {
					try {
						con.close();
					} catch (Exception e) {
						e.printStackTrace();
						con.close();

						LOGGER.error(e.getMessage());
					}
					con = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}	
}
