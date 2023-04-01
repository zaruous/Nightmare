/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.group
 *	작성일   : 2021. 12. 17.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.ec.ec.group;

import java.io.File;
import java.util.List;

import org.dom4j.Document;

import com.kyj.fx.nightmare.actions.comm.core.AbstractEtTab;
import com.kyj.fx.nightmare.actions.comm.core.OnCommitEnableService;
import com.kyj.fx.nightmare.actions.comm.core.OnExcelTableViewList;
import com.kyj.fx.nightmare.actions.comm.core.OnLoadEquipmentClass;

import javafx.scene.control.TableView;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EquipmentClassGroupTab extends AbstractEtTab implements OnLoadEquipmentClass, OnCommitEnableService, OnExcelTableViewList {

	private EquipmentClassGroupComposite c;

	public EquipmentClassGroupTab() {
		c = new EquipmentClassGroupComposite();
		setText("Group");
		this.setContent(c);
		setDisable(true);
	}

	/* (non-Javadoc)
	 * @see com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnReload#reload()
	 */
	@Override
	public void reload() {
		c.reload();
	}

	@Override
	public void onLoadEquipmentClass(String equipmentClassGuid) {
		c.onLoadEquipmentClass(equipmentClassGuid);
	}

	@Override
	public void onCommit() {
		c.onCommit();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TableView<EquipmentClassGroupDVO>> excelTableList() {
		return c.excelTableList();
	}

	@Override
	public void importExcel(File fromFile, Document doc) {
		c.importExcel(fromFile, doc);
	}

	@Override
	public void importExcel(File excelFile) {
		c.importExcel(excelFile);
	}

	@Override
	public boolean exportExcel(File out) {
		return c.exportExcel(out);
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

}
