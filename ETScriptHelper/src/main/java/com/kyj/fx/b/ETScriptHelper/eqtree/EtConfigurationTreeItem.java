/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.eqtree
 *	작성일   : 2021. 11. 22.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.eqtree;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EtConfigurationTreeItem extends TreeItem<EtConfigurationTreeDVO> {

	private Action action = Action.Empty;

	public enum Action {
		Empty, 
		EC_GENERAL, 
		EC,
		EC_CUSTOM_PROP, 
		EC_EVENT_STATES, 
		EC_EVENTS, 
		EC_WORKFLOW, 
		EC_RULE, 
		EC_GROUP, 
		EC_EQ, 
		EC_EQ_ITEM,
		EC_EQ_EVENTS, 
		ROOT, 
		EC_EVENTS_ITEM, 
		EC_EQ_EVENT_STATES, EC_EQ_EVENT_ITEMS
	}

	/**
	 * @param id
	 * @param text
	 */
	private EtConfigurationTreeItem(String id, String text) {
		this(new EtConfigurationTreeDVO(id, text));
	}

	public EtConfigurationTreeItem(EtConfigurationTreeDVO root) {
		super(root);
	}

	/**
	 */
	public EtConfigurationTreeItem() {
		super();
	}

	/**
	 * @param value
	 * @param graphic
	 */
	public EtConfigurationTreeItem(EtConfigurationTreeDVO value, Node graphic) {
		super(value, graphic);
	}

	/**
	 * @param id
	 * @param text
	 * @param action
	 */
	public EtConfigurationTreeItem(String id, String text, Action action) {
		this(id, text);
		this.action = action;
	}
	/**
	 * @param id
	 * @param text
	 * @param action
	 */
	public EtConfigurationTreeItem(Node graphic, String id, String text, Action action) {
		this(id, text);
		this.setGraphic(graphic);
		this.action = action;
	}
	
	/**
	 * @return the action
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(Action action) {
		this.action = action;
	}

	


}