/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.rule
 *	작성일   : 2021. 12. 8.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.ec.ec.rule;

import com.kyj.fx.nightmare.comm.AbstractDVO;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */

public class GroupDVO extends AbstractDVO {

	private StringProperty groupName = new SimpleStringProperty();
	private StringProperty groupType = new SimpleStringProperty();

	public GroupDVO() {
		super();
	}

	public final StringProperty groupNameProperty() {
		return this.groupName;
	}

	public final String getGroupName() {
		return this.groupNameProperty().get();
	}

	public final void setGroupName(final String groupName) {
		this.groupNameProperty().set(groupName);
	}

	public final StringProperty groupTypeProperty() {
		return this.groupType;
	}

	public final String getGroupType() {
		return this.groupTypeProperty().get();
	}

	public final void setGroupType(final String groupType) {
		this.groupTypeProperty().set(groupType);
	}

}
