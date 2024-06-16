/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.http.entity.ContentType;
import org.springframework.jdbc.core.DataClassRowMapper;

import com.kyj.fx.nightmare.comm.AbstractDAO;

/**
 * 
 */
public class AIDataDAO extends AbstractDAO {

	private static AIDataDAO dao;

	public static synchronized AIDataDAO getInstance() {
		if (dao == null)
			dao = new AIDataDAO();
		return dao;
	}

	private AIDataDAO() {
	}

	public long insertHistory(String aiId, long speechId, String system, USER user, String question) throws Exception {
		long id = System.currentTimeMillis();
		String state = """
					INSERT INTO `chat_history` (`ID`, `SYSTEM`, `QUESTION`, `FIRST_REGER_ID`,`AI_ID`, `SPEECH_ID`)
					VALUES (:id, :system, :question , :user, :aiId, :speechId);
				""";
		update(state, Map.of("id", id, "aiId", aiId, "user", user.name(), "question", question, "system", system, "speechId", speechId));

		return id;
	}

	public void updateAnswer(long id, String answer) throws Exception {

		String state = """
					UPDATE `chat_history`
					SET ANSWER = :answer
					WHERE 1=1
					AND id = :id
				""";
		update(state, Map.of("id", id, "answer", answer));
	}

	/**
	 * @param group
	 * @param key
	 * @return
	 */
	public Map<String, Object> getAiConnectionConfig(String group, String key) {
		String sql = """
				select c.* from tbm_sm_cnf c where 1=1 and c.GROUP = :group AND c.KEY=:key AND USE_YN = 'Y' \n
				""";
		Map<String, Object> select = getNamedJdbcTemplate().queryForMap(sql, Map.of("group", group, "key" , key));
		return select;
	}
	
	public Map<String, Object> getAiConnectionConfig(String key) {
		String sql = """
				select c.* from tbm_sm_cnf c where 1=1 and c.KEY=:key AND USE_YN = 'Y' \n
				""";
		Map<String, Object> select = getNamedJdbcTemplate().queryForMap(sql, Map.of("key" , key));
		return select;
	}
	/**
	 * 설정정보 로드
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2024. 6. 10.
	 * @return
	 */
	public Map<String, Object> getAiConnectionConfig() {
		String sql = """
				select c.* from tbm_sm_cnf c where 1=1 and c.GROUP = 'OPEN_AI' AND c.KEY='GTP_4_O' AND USE_YN = 'Y' \n
				""";
		Map<String, Object> select = getNamedJdbcTemplate().queryForMap(sql, Collections.emptyMap());
		return select;
	}

//	public Map<String, Object> getAiSpeechConnectionConfig() {
//		String sql = """
//				select c.* from tbm_sm_cnf c where 1=1 and c.GROUP = 'OPEN_AI' AND c.KEY='TRANSLATE' AND USE_YN = 'Y' \n
//				""";
//		Map<String, Object> select = getNamedJdbcTemplate().queryForMap(sql, Collections.emptyMap());
//		return select;
//	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2024. 6. 10.
	 * @return
	 */
	public List<Map<String, Object>> getLatestHistory() {
		String state = """
					SELECT * 
						FROM (
						    SELECT * 
						    FROM CHAT_HISTORY
						    ORDER BY ID DESC
						    LIMIT 10 
						) subquery
						ORDER BY ID ASC
			
				""";
		List<Map<String, Object>> query = query(state, Collections.emptyMap());
		return query;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2024. 6. 10.
	 * @return
	 */
	public List<TbmSmPrompts> getCustomContext() {
		String state = """
					SELECT *
					FROM TBM_SM_PROMPTS
					WHERE 1=1
					AND `GROUP` = 'CONTEXT'	
					AND USE_YN = 'Y'
				""";
		List<TbmSmPrompts> query = query(state, Collections.emptyMap(), new DataClassRowMapper<TbmSmPrompts>(TbmSmPrompts.class));
		return query;
	}
	
	public List<TbmSmPrompts> getSupports() {
		String state = """
					SELECT *
					FROM TBM_SM_PROMPTS
					WHERE 1=1
					AND `GROUP` = 'SUPPORT'	
					AND USE_YN = 'Y'
				""";
		List<TbmSmPrompts> query = query(state, Collections.emptyMap(), new DataClassRowMapper<TbmSmPrompts>(TbmSmPrompts.class));
		return query;
	}

	public long insertHistory(String string, String systemRole, USER user, byte[] audio, ContentType contentType) {
		// TODO Auto-generated method stub
		return 0;
	}
}
