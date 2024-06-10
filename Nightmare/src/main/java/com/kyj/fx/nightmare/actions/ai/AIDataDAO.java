/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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

	public enum USER {
		AI, USER
	}

	public long insertHistory(String aiId, USER user, String question) throws Exception {
		long id = System.currentTimeMillis();
		String state = """
					INSERT INTO `chat_history` (`ID`, `QUESTION`, `FIRST_REGER_ID`,`AI_ID`)
					VALUES (:id, :question , :user, :aiId);
				""";
		update(state, Map.of("id", id, "aiId", aiId, "user", user.name(), "question", question));

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
	 * 설정정보 로드
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2024. 6. 10.
	 * @return
	 */
	public Map<String, Object> getAiConnectionConfig() {
		String sql = """
				select c.* from tbm_sm_cnf c where 1=1 and c.GROUP = 'OPEN_AI' AND c.KEY='GTP_4_O' \n
				""";
		Map<String, Object> select = getNamedJdbcTemplate().queryForMap(sql, Collections.emptyMap());
		return select;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2024. 6. 10.
	 * @return
	 */
	public List<Map<String, Object>> getLastHistory() {
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
//		System.out.println(query.size());
		return query;
	}
}
