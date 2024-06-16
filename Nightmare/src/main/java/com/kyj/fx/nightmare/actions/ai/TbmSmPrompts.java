/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

import com.kyj.fx.nightmare.comm.AbstractDVO;

/**
 * 
 */
public class TbmSmPrompts extends AbstractDVO {
	private String group;
	private String id;
	private String displayText;
	private String prompt;
	private String useYn;
	private String description;
	private String graphicClass;

	/*
	 * SUPPORT의 경우 대화 내용이 기록되고, 대화창에 정보가 남는다. CONTEXT의 경우 기능 결과가 팝업으로 나오고 대화 정보가
	 * 파라미터로 처리된다.
	 **/
	public enum GROUPS {
		SUPPORT, CONTEXT
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGraphicClass() {
		return graphicClass;
	}

	public void setGraphicClass(String graphicClass) {
		this.graphicClass = graphicClass;
	}

	@Override
	public String toString() {
		return "TbmSmPrompts [group=" + group + ", id=" + id + ", displayText=" + displayText + ", prompt=" + prompt
				+ ", useYn=" + useYn + "]";
	}

}
