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

	@Override
	public String toString() {
		return "TbmSmPrompts [group=" + group + ", id=" + id + ", displayText=" + displayText + ", prompt=" + prompt
				+ ", useYn=" + useYn + "]";
	}

}
