/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.scripts
 *	작성일   : 2021. 12. 3.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.ec.ec.rule;

import java.io.File;
import java.util.List;

import org.dom4j.Document;

import com.kyj.fx.b.ETScriptHelper.actions.comm.core.AbstractEtTab;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnExcelTableViewList;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnCommitService;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnLoadEquipmentClass;

import javafx.scene.control.TableView;

/**
 * Event script.
 * 
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EquipmentClassRuleTab extends AbstractEtTab implements OnLoadEquipmentClass, 
			OnExcelTableViewList, OnCommitService {

	EquipmentClassRuleComposite c = new EquipmentClassRuleComposite();

	public EquipmentClassRuleTab() {
		setContent(c);
		setText("Rule");
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

	@SuppressWarnings("unchecked")
	@Override
	public List<TableView> excelTableList() {
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

	}

}
