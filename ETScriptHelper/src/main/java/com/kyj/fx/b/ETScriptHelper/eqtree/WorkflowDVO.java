/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.eqtree
 *	작성일   : 2022. 7. 21.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.eqtree;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class WorkflowDVO {

	private StringProperty workflowGuid = new SimpleStringProperty();
	private StringProperty name = new SimpleStringProperty();
	private StringProperty description = new SimpleStringProperty();
	private StringProperty documentName = new SimpleStringProperty();

	public final StringProperty workflowGuidProperty() {
		return this.workflowGuid;
	}

	public final String getWorkflowGuid() {
		return this.workflowGuidProperty().get();
	}

	public final void setWorkflowGuid(final String workflowGuid) {
		this.workflowGuidProperty().set(workflowGuid);
	}

	public final StringProperty nameProperty() {
		return this.name;
	}

	public final String getName() {
		return this.nameProperty().get();
	}

	public final void setName(final String name) {
		this.nameProperty().set(name);
	}

	public final StringProperty descriptionProperty() {
		return this.description;
	}

	public final String getDescription() {
		return this.descriptionProperty().get();
	}

	public final void setDescription(final String description) {
		this.descriptionProperty().set(description);
	}

	public final StringProperty documentNameProperty() {
		return this.documentName;
	}

	public final String getDocumentName() {
		return this.documentNameProperty().get();
	}

	public final void setDocumentName(final String documentName) {
		this.documentNameProperty().set(documentName);
	}

}
