/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

import java.util.Map;

import com.kyj.fx.nightmare.comm.AbstractDAO;

/**
 * 
 */
public class AIDataDAO extends AbstractDAO {

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
}
