/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.scripts
 *	작성일   : 2021. 12. 3.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.ec.ec.scripts;

import java.io.File;
import java.util.List;

import org.dom4j.Document;

import com.kyj.fx.b.ETScriptHelper.actions.comm.core.AbstractEtTab;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnExcelTableViewList;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnCommitService;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnLoadEquipmentClass;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnLoadEquipmentClassEvent;

import javafx.scene.control.TableView;

/**
 * Event script.
 * 
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EquipmentClassEventScriptTab extends AbstractEtTab
		implements OnLoadEquipmentClassEvent, OnLoadEquipmentClass, OnExcelTableViewList, OnCommitService {

	EquipmentClassEventScriptComposite c = new EquipmentClassEventScriptComposite();

	public EquipmentClassEventScriptTab() {
		setContent(c);
		setText("Event Script");
		setDisable(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kyj.fx.b.ETScriptHelper.actions.comm.OnLoadEquipmentClass#
	 * onLoadEquipmentClass(java.lang.String)
	 */
	@Override
	public void onLoadEquipmentClass(String equipmentClassGuid) {
		c.onLoadEquipmentClass(equipmentClassGuid);
	}

	@Override
	public void onLoadEquipmentClassEvent(String equipmentClassGuid, String eventGuid) {
		c.onLoadEquipmentClassEvent(equipmentClassGuid, eventGuid);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TableView<EquipmentScriptDVO>> excelTableList() {
		return c.excelTableList();
	}

	@Override
	public void importExcel(File fromFile, Document doc) {
		c.importExcel(fromFile, doc);
	}

	@Override
	public boolean exportExcel(File out) {
		return c.exportExcel(out);
	}

	@Override
	public void onCommit() {
		c.onCommit();
	}

	@Override
	public void reload() {
		c.reload();
	}

	@Override
	public boolean enableCommitButton() {
		return true;
	}

	@Override
	public boolean enableReloadButton() {
		return true;
	}

	@Override
	public boolean enableExcelExportButton() {
		return true;
	}

	@Override
	public boolean enableExcelImportButton() {
		return true;
	}

}
