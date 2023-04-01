/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.rule
 *	작성일   : 2021. 12. 9.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.ec.ec.rule;

import com.kyj.fx.nightmare.comm.AbstractDVO;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class RuleDVO extends AbstractDVO {

	private StringProperty ruleName = new SimpleStringProperty();
	private StringProperty ruleType = new SimpleStringProperty();
	private StringProperty ruleEventWorkflow = new SimpleStringProperty();
	private StringProperty ruleDependentEventGUID = new SimpleStringProperty();

	public RuleDVO() {
		super();
	}

	public final StringProperty ruleNameProperty() {
		return this.ruleName;
	}

	public final String getRuleName() {
		return this.ruleNameProperty().get();
	}

	public final void setRuleName(final String ruleName) {
		this.ruleNameProperty().set(ruleName);
	}

	public final StringProperty ruleTypeProperty() {
		return this.ruleType;
	}

	public final String getRuleType() {
		return this.ruleTypeProperty().get();
	}

	public final void setRuleType(final String ruleType) {
		this.ruleTypeProperty().set(ruleType);
	}

	public final StringProperty ruleEventWorkflowProperty() {
		return this.ruleEventWorkflow;
	}

	public final String getRuleEventWorkflow() {
		return this.ruleEventWorkflowProperty().get();
	}

	public final void setRuleEventWorkflow(final String ruleEventWorkflow) {
		this.ruleEventWorkflowProperty().set(ruleEventWorkflow);
	}

	public final StringProperty ruleDependentEventGUIDProperty() {
		return this.ruleDependentEventGUID;
	}

	public final String getRuleDependentEventGUID() {
		return this.ruleDependentEventGUIDProperty().get();
	}

	public final void setRuleDependentEventGUID(final String ruleDependentEventGUID) {
		this.ruleDependentEventGUIDProperty().set(ruleDependentEventGUID);
	}

}
