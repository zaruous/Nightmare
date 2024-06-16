/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 */
public class Answer {
	private int no;
	private String text;
	private boolean correct;

	public Answer(@JsonProperty("no") int no, @JsonProperty("text") String text,
			@JsonProperty("correct") boolean correct) {
		this.no = no;
		this.text = text;
		this.correct = correct;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

}
