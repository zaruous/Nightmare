/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec
 *	작성일   : 2021. 11. 29.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.ec.ec;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.dom4j.Document;

import com.kyj.fx.nightmare.actions.comm.core.AbstractEtTab;
import com.kyj.fx.nightmare.actions.comm.core.OnExcelTableViewList;
import com.kyj.fx.nightmare.actions.comm.core.OnLoadEquipmentClass;
import com.kyj.fx.nightmare.comm.AbstractDVO;

import javafx.scene.control.TableView;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EquipmentClassTab extends AbstractEtTab implements OnLoadEquipmentClass, OnExcelTableViewList {
	EquipmentClassComposite equipmentClassComposite = new EquipmentClassComposite();

	/**
	 */
	public EquipmentClassTab() {
		this.setText("Equipment Class");
		setContent(equipmentClassComposite);
		setDisable(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kyj.fx.b.ETScriptHelper.actions.comm.OnLoadEquipmentClass#
	 * onLoadEquipmentClass(java.lang.String, java.lang.String)
	 */
	@Override
	public void onLoadEquipmentClass(String equipmentClassGuid) {
		equipmentClassComposite.onLoadEquipmentClass(equipmentClassGuid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kyj.fx.b.ETScriptHelper.actions.comm.IExcelTableViewList#
	 * excelTableList()
	 */
	@Override
	public <T extends AbstractDVO> List<TableView<T>> excelTableList() {
		/*Nothing*/
		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kyj.fx.b.ETScriptHelper.actions.comm.IExcelTableViewList#exportExcel(
	 * java.io.File)
	 */
	@Override
	public boolean exportExcel(File out) {
		return equipmentClassComposite.exportExcel(out);
	}

	/* (non-Javadoc)
	 * @see com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnExcelTableViewList#importExcel(java.io.File, org.dom4j.Document)
	 */
	@Override
	public void importExcel(File fromFile, Document doc) {
		equipmentClassComposite.importExcel(fromFile, doc);
	}

	/* (non-Javadoc)
	 * @see com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnReload#reload()
	 */
	@Override
	public void reload() {
		equipmentClassComposite.reload();
	}

	@Override
	public boolean enableReloadButton() {
		return true;
	}

}
