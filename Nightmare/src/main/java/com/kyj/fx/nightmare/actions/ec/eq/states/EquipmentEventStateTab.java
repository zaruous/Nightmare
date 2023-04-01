/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.states
 *	작성일   : 2021. 11. 29.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.ec.eq.states;

import java.io.File;
import java.util.List;

import org.dom4j.Document;

import com.kyj.fx.nightmare.actions.comm.core.AbstractEtTab;
import com.kyj.fx.nightmare.actions.comm.core.OnCommitService;
import com.kyj.fx.nightmare.actions.comm.core.OnExcelTableViewList;
import com.kyj.fx.nightmare.actions.comm.core.OnLoadEquipment;
import com.kyj.fx.nightmare.actions.comm.core.OnLoadEquipmentClass;
import com.kyj.fx.nightmare.actions.comm.core.OnReload;

import javafx.scene.control.TableView;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class EquipmentEventStateTab extends AbstractEtTab
		implements OnLoadEquipmentClass, OnLoadEquipment, OnExcelTableViewList, OnCommitService, OnReload {

	private EquipmentEventStateComposite c = new EquipmentEventStateComposite();

	public EquipmentEventStateTab() {
		setText("EQ-Event state");
		setContent(c);
		setDisable(true);
	}

	@Override
	public void onLoadEquipment(String equipmentGuid) {
		c.onLoadEquipment(equipmentGuid);

	}

	@Override
	public void onLoadEquipmentClass(String equipmentClassGuid) {
		c.onLoadEquipmentClass(equipmentClassGuid);
	}

	@Override
	public void importExcel(File fromFile, Document doc) {
		c.importExcel(fromFile, doc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kyj.fx.b.ETScriptHelper.actions.comm.IExcelTableViewList#
	 * excelTableList()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TableView<EquipmentEventStateDVO>> excelTableList() {
		return c.excelTableList();
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
	public boolean enableExcelExportButton() {
		return true;
	}

	@Override
	public boolean enableExcelImportButton() {
		return true;
	}

	@Override
	public boolean enableReloadButton() {
		return true;
	}

	@Override
	public boolean enableCommitButton() {
		return true;
	}

}
