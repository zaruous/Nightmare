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
public class AISpeechDAO extends AbstractDAO {

	private static AISpeechDAO dao;

	public static synchronized AISpeechDAO getInstance() {
		if (dao == null)
			dao = new AISpeechDAO();
		return dao;
	}

	private AISpeechDAO() {
	}

	public long insertHistory(String aiId, String system, USER user, byte[] question, ContentType contentType) throws Exception {
		long id = System.currentTimeMillis();
		String state = """
					INSERT INTO `speech_history` (`ID`, `SYSTEM`, `QUESTION`, `FIRST_REGER_ID`,`AI_ID`, `CONTENT_TYPE`)
					VALUES (:id, :system, :question , :user, :aiId, :contentType);
				""";
		update(state, Map.of("id", id, "aiId", aiId, "user", user.name(), "question", question, "system", system, "contentType" , contentType.getMimeType()));

		return id;
	}

	public void updateAnswer(long id, String answer) throws Exception {

		String state = """
					UPDATE `speech_history`
					SET ANSWER = :answer
					WHERE 1=1
					AND id = :id
				""";
		update(state, Map.of("id", id, "answer", answer));
	}

	public Map<String, Object> getAiConnectionConfig() {
		String sql = """
				select c.* from tbm_sm_cnf c where 1=1 and c.GROUP = 'OPEN_AI' AND c.KEY='TRANSLATE' AND USE_YN = 'Y' \n
				""";
		Map<String, Object> select = getNamedJdbcTemplate().queryForMap(sql, Collections.emptyMap());
		return select;
	}

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
					SELECT `GROUP`, `ID`, DISPLAY_TEXT, PROMPT
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
					SELECT `GROUP`, `ID`, DISPLAY_TEXT, PROMPT
					FROM TBM_SM_PROMPTS
					WHERE 1=1
					AND `GROUP` = 'SUPPORT'
					AND USE_YN = 'Y'
				""";
		List<TbmSmPrompts> query = query(state, Collections.emptyMap(), new DataClassRowMapper<TbmSmPrompts>(TbmSmPrompts.class));
		return query;
	}

	

}
