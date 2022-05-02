/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.events
 *	작성일   : 2021. 11. 30.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.ec.ec.events;

import java.io.IOException;

import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnLoadEquipmentClassEvent;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnReload;
import com.kyj.fx.b.ETScriptHelper.comm.DialogUtil;
import com.kyj.fx.b.ETScriptHelper.comm.FxUtil;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EquipmentClassEventComposite extends BorderPane implements OnLoadEquipmentClassEvent, OnReload {

	/**
	 */
	public EquipmentClassEventComposite() {
		FXMLLoader newLaoder = FxUtil.newLaoder();
		newLaoder.setLocation(EquipmentClassEventComposite.class.getResource("EquipmentClassEventView.fxml"));
		newLaoder.setRoot(this);
		newLaoder.setController(this);
		try {
			newLaoder.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kyj.fx.b.ETScriptHelper.actions.comm.OnLoadEquipmentClassEvent#
	 * onLoadEquipmentClassEvent(java.lang.String, java.lang.String)
	 */
	@Override
	public void onLoadEquipmentClassEvent(String equipmentClassGuid, String eventGuid) {

	}

	@Override
	public void reload() {
		DialogUtil.showMessageDialog("미구현.");
	}
}
