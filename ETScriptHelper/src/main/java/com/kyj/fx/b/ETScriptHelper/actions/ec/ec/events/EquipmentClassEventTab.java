/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.events
 *	작성일   : 2021. 12. 2.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.ec.ec.events;

import com.kyj.fx.b.ETScriptHelper.actions.comm.core.AbstractEtTab;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnEquipmentClassEventScript;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EquipmentClassEventTab extends AbstractEtTab implements OnEquipmentClassEventScript {

	private EquipmentClassEventComposite equipmentClassEventComposite = new EquipmentClassEventComposite();

	public EquipmentClassEventTab() {
		this.setText("Equipment Event");
		setContent(equipmentClassEventComposite);
		setDisable(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kyj.fx.b.ETScriptHelper.actions.comm.OnEquipmentClassEventScript#
	 * onEquipmenbtClassEventScript(java.lang.String, java.lang.String)
	 */
	@Override
	public void onEquipmenbtClassEventScript(String equipmentClassGuid, String eventGuid) {
		equipmentClassEventComposite.onLoadEquipmentClassEvent(equipmentClassGuid, eventGuid);
	}

	@Override
	public void reload() {
		equipmentClassEventComposite.reload();
	}

}