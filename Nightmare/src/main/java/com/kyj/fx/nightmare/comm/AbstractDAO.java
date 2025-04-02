/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.dao
 *	작성일   : 2018. 4. 13.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.nightmare.comm;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import com.kyj.fx.nightmare.ui.grid.TableName;

/**
 * @author KYJ
 *
 */
public class AbstractDAO {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AbstractDAO.class);

	// JdbcTemplate and data source
	private NamedParameterJdbcTemplate template;
	private DataSource dataSource;

	public AbstractDAO() {

	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public NamedParameterJdbcTemplate getNamedJdbcTemplate() {

		if (dataSource == null) {
			try {
				dataSource = DbUtil.getDataSource();
			} catch (Exception e) {
				throw new RuntimeException("can't load database ");
			}
		}

		if (template == null) {
			template = new NamedParameterJdbcTemplate(dataSource);
		}

		return template;
	}

	public void setNamedJdbcTemplate(NamedParameterJdbcTemplate namedJdbcTemplate) {
		this.template = namedJdbcTemplate;
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2022. 7. 28.
	 * @param query
	 * @param paramMap
	 * @return
	 */
	public String queryScala(String query, Map<String, Object> paramMap) {
		return queryScala(query, paramMap, new ResultSetExtractor<String>() {

			@Override
			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					return rs.getString(1);
				}
				return null;
			}
		});
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2019. 2. 14.
	 * @param query
	 * @param paramMap
	 * @param mapper
	 * @return
	 */
	public <T> T queryScala(String query, Map<String, Object> paramMap, ResultSetExtractor<T> mapper) {
		String _query = replaceVelocityText(query, paramMap);

		/**/
		StringBuilder sb = new StringBuilder();
		sb.append(query);
		sb.append("\n");
		sb.append(paramMap);
		DbUtil.noticeQuery(sb.toString());
		/**/
		return getNamedJdbcTemplate().query(_query, paramMap, mapper);
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2020. 12. 16.
	 * @param <T>
	 * @param query
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> query(String query, Map<String, Object> paramMap) {
		return query(query, paramMap, new MapBaseRowMapper());
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2020. 12. 16.
	 * @param <T>
	 * @param query
	 * @param paramMap
	 * @param mapper
	 * @return
	 */
	public <T> List<T> query(String query, Map<String, Object> paramMap, RowMapper<T> mapper) {
		String _query = replaceVelocityText(query, paramMap);

		/**/
		StringBuilder sb = new StringBuilder();
		sb.append(query);
		sb.append("\n");
		sb.append(paramMap);
		DbUtil.noticeQuery(sb.toString());
		/**/

		return getNamedJdbcTemplate().query(_query, paramMap, mapper);
	}

	protected String replaceVelocityText(String query, Map<String, Object> paramMap) {
		return ValueUtil.getVelocityToText(query, paramMap);
	}

	protected boolean isVelocity(String sql) {
		return (sql.indexOf("#if") > -1 || sql.indexOf("#foreach") > -1 || sql.indexOf("$") !=-1 ||  sql.indexOf("#end") !=-1 -1);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 12. 21.
	 * @param sql
	 * @param velocityParam
	 * @param paramMaps
	 * @return
	 */
	public int[] batchUpdate(String sql, SqlParameterSource[] paramMaps) throws Exception {
		return batchUpdate(sql, Collections.emptyMap(), paramMaps);
	}

	/*
	 * @ update batch 추가.
	 * 
	 */
	public int[] batchUpdate(String sql, Map<String, Object>[] paramMaps) throws Exception {
		if (paramMaps != null && isVelocity(sql)) {
			BufferedWriter writer = new BufferedWriter(new PrintWriter(new ByteArrayOutputStream()));
			Velocity.evaluate(new VelocityContext(paramMaps[0]), writer, "AbstractDAO", sql.toString());
			sql = writer.toString();
		}
		DbUtil.noticeQuery(sql);
		return getNamedJdbcTemplate().batchUpdate(sql, paramMaps);
	}

	/*
	 * @update 추가.
	 * 
	 */
	public int update(String sql, Map<String, Object> paramMap) throws Exception {
		if (isVelocity(sql)) {
			BufferedWriter writer = new BufferedWriter(new PrintWriter(new ByteArrayOutputStream()));
			Velocity.evaluate(new VelocityContext(paramMap), writer, "AbstractDAO", sql.toString());

			sql = writer.toString();
		}

		DbUtil.noticeQuery(sql, paramMap);
		return getNamedJdbcTemplate().update(sql, paramMap);
	}

	/**
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 12. 21.
	 * @param sql
	 * @param paramMaps
	 * @return
	 */
	public int[] batchUpdate(String sql, Map<String, Object> velocityParam, SqlParameterSource[] paramMaps)
			throws Exception {
		if (paramMaps != null && isVelocity(sql)) {
			BufferedWriter writer = new BufferedWriter(new PrintWriter(new ByteArrayOutputStream()));
			Velocity.evaluate(new VelocityContext(velocityParam), writer, "AbstractDAO", sql.toString());
			sql = writer.toString();
		}
		DbUtil.noticeQuery(sql);
		return getNamedJdbcTemplate().batchUpdate(sql, paramMaps);
	}

	/**
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 12. 21.
	 * @param sql
	 * @param paramMaps
	 * @return
	 */
	public <T> int[] updateBatch(List<T> list) throws Exception {
		if(list.isEmpty()) {return new int[0]; };
		T t = list.get(0);
		
		TableName annotation = t.getClass().getAnnotation(TableName.class);		
		String tableName = null;
		if(annotation!=null)
			tableName = annotation.value();
		else
			tableName = t.getClass().getName();
		return updateBatch(list, tableName);
	}
	
	public <T> int[] updateBatch(List<T> list, String tableName) throws Exception {

		if(list.isEmpty()) {return new int[0]; };
		T instance = list.get(0);
		List<String> pks = DbUtil.pks(tableName, DbUtil.CAMEL_CONVERTER);

		Map<String, Object> allColumns = ObjectUtil.getFileteredKeys(instance, name -> !pks.contains(name));
		StringBuilder sb = new StringBuilder();
		sb.append("update $tableName \n");
		sb.append("set ");
		for(String key : allColumns.keySet())
		{
			sb.append("\n").append(ValueUtil.toColumnNamePattern(key)).append(" = :").append(key).append(",");
		}
		sb.deleteCharAt(sb.length() -1 );
		sb.append("\nwhere 1=1");
		
		for(int i=0, size = pks.size(); i < size ; i++)
		{
			sb.append("\nand ").append(ValueUtil.toColumnNamePattern(pks.get(i))).append(" = :").append(pks.get(i)).append(",");
		}
		sb.deleteCharAt(sb.length() -1 );
		
		String sql = sb.toString();
		Map<String, Object> param = Map.of("tableName", tableName);
		if (instance != null && isVelocity(sql)) {
			BufferedWriter writer = new BufferedWriter(new PrintWriter(new ByteArrayOutputStream()));
			Velocity.evaluate(new VelocityContext(param), writer, "AbstractDAO", sql);
			sql = writer.toString();
		}
//		
		DbUtil.noticeQuery(sql);
		Map[] array = new HashMap[list.size()];
		for(int i=0, size = list.size(); i< size; i++)
		{
			array[i] = ObjectUtil.getKeys(list.get(i));
		}
		
		return getNamedJdbcTemplate().batchUpdate(sql, array);
	}
	public <T> int[] insertBatch(List<T> list) throws Exception {
		if(list.isEmpty()) {return new int[0]; };
		T t = list.get(0);
		
		TableName annotation = t.getClass().getAnnotation(TableName.class);		
		String tableName = null;
		if(annotation!=null)
			tableName = annotation.value();
		else
			tableName = t.getClass().getName();
		return insertBatch(list, tableName);
	}
	
	public <T> int[] insertBatch(List<T> list, String tableName) throws Exception {

		if(list.isEmpty()) {return new int[0]; }

		T instance = list.get(0);
		Map<String, Object> allColumns = ObjectUtil.getKeys(instance, ObjectUtil.STR_COLUMN_NAME_CONVERTER);
		Map<String, Object> camelColumns = ObjectUtil.getKeys(instance);
		StringBuilder sb = new StringBuilder();
		sb.append("insert into $tableName \n");
		sb.append("( ");
		for(String key : allColumns.keySet())
		{
			sb.append(key).append(",");
		}
		sb.deleteCharAt(sb.length() -1 );
		
		sb.append(" )\nvalues\n(");
		
		for(String key : camelColumns.keySet())
		{
			sb.append(" :").append(key).append(",");
		}
		sb.deleteCharAt(sb.length() -1 );
		sb.append(" )\n");
		
		String sql = sb.toString();
		Map<String, Object> param = Map.of("tableName", tableName);
		if (instance != null && isVelocity(sql)) {
			BufferedWriter writer = new BufferedWriter(new PrintWriter(new ByteArrayOutputStream()));
			Velocity.evaluate(new VelocityContext(param), writer, "AbstractDAO", sql);
			sql = writer.toString();
		}
//		
		
		Map[] array = new HashMap[list.size()];
		for(int i=0, size = list.size(); i< size; i++)
		{
			array[i] = ObjectUtil.getKeys(list.get(i));
		}
		DbUtil.noticeQuery(sql, array);
		return getNamedJdbcTemplate().batchUpdate(sql, array);
	}
	
	public <T> int[] deleteBatch(List<T> list) throws Exception {
		if(list.isEmpty()) {return new int[0]; };
		T t = list.get(0);
		
		TableName annotation = t.getClass().getAnnotation(TableName.class);		
		String tableName = null;
		if(annotation!=null)
			tableName = annotation.value();
		else
			tableName = t.getClass().getName();
		return deleteBatch(list, tableName);
	}
	
	public <T> int[] deleteBatch(List<T> list, String tableName) throws Exception {

		if(list.isEmpty()) {return new int[0]; }

		T instance = list.get(0);
		List<String> pks = DbUtil.pks(tableName, DbUtil.CAMEL_CONVERTER);
		if(pks.isEmpty())throw new RuntimeException("pk does not exists.");
		StringBuilder sb = new StringBuilder();
		sb.append("delete from $tableName \n");
		sb.append("where 1=1\n");
		for(String key : pks)
		{
			sb.append("and ").append(ValueUtil.toColumnNamePattern(key)).append(" = :").append(key).append("\n");
		}
		
		String sql = sb.toString();
		Map<String, Object> param = Map.of("tableName", tableName);
		if (instance != null && isVelocity(sql)) {
			BufferedWriter writer = new BufferedWriter(new PrintWriter(new ByteArrayOutputStream()));
			Velocity.evaluate(new VelocityContext(param), writer, "AbstractDAO", sql);
			sql = writer.toString();
		}
//		
		DbUtil.noticeQuery(sql);
		Map[] array = new HashMap[list.size()];
		for(int i=0, size = list.size(); i< size; i++)
		{
			array[i] = ObjectUtil.getKeys(list.get(i));
		}
		return getNamedJdbcTemplate().batchUpdate(sql, array);
	}
	
	/**
	 * @param <T>
	 * @param list
	 * @throws Exception
	 */
	public <T extends AbstractDVO> void saveBatch(List<T> list) throws Exception {
		if(list.isEmpty()) {return; };
		T t = list.get(0);
		
		TableName annotation = t.getClass().getAnnotation(TableName.class);		
		String tableName = null;
		if(annotation!=null)
			tableName = annotation.value();
		else
			tableName = t.getClass().getSimpleName();
		
		
		
		List<T> insertBatch = list.stream().filter(v -> AbstractDVO.CREATE.equals(v.get_status())).collect(Collectors.toList());
		List<T> updateBatch = list.stream().filter(v -> AbstractDVO.UPDATE.equals(v.get_status())).collect(Collectors.toList());
		List<T> deleteBatch = list.stream().filter(v -> AbstractDVO.DELETE.equals(v.get_status())).collect(Collectors.toList());
		
		insertBatch(insertBatch, tableName);
		updateBatch(updateBatch, tableName);
		deleteBatch(deleteBatch, tableName);
		
	}
	public <T> List<T> queryForList(String sql, Map<String, Object> paramMap, Class<T> clazz) {
		if (isVelocity(sql)) {
			BufferedWriter writer = new BufferedWriter(new PrintWriter(new ByteArrayOutputStream()));
			Velocity.evaluate(new VelocityContext(paramMap), writer, "AbstractDAO", sql.toString());
			sql = writer.toString();
		}
		DbUtil.noticeQuery(sql);
		return getNamedJdbcTemplate().query(sql, paramMap, new BeanPropertyRowMapper<>(clazz));
	}

	/**
	 * 프로시저 호출 <br/>
	 * 
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2018. 10. 24.
	 * @param procedureName
	 * @param paramMap
	 * @param clazz
	 * @return
	 */
	public Map<String, Object> callProcedure(String procedureName, Map<String, Object> paramMap) {

		JdbcTemplate template = (JdbcTemplate) this.template.getJdbcOperations();

		SimpleJdbcCall call = new SimpleJdbcCall(template.getDataSource()).withProcedureName(procedureName);
		LOGGER.debug("procedure : {} ", procedureName);
		DbUtil.noticeQuery(procedureName);
		Map<String, Object> execute = call.execute(paramMap);
		return execute;
	}

}
