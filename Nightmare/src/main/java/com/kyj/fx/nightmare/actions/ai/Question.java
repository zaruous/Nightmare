/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 */
public class Question {

	private String question;
	private List<Answer> questionList;
	private String reason;

	public Question(@JsonProperty("question") String question, @JsonProperty("questionList") List<Answer> questionList,
			@JsonProperty("reason") String reason) {
		this.question = question;
		this.questionList = questionList;
		this.reason = reason;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public List<Answer> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(List<Answer> questionList) {
		this.questionList = questionList;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	// Getters and setters

}
