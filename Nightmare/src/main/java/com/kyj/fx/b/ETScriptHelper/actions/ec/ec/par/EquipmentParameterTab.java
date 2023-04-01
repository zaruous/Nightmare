/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec
 *	작성일   : 2021. 11. 29.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.ec.ec.par;

import java.io.File;
import java.util.List;

import org.dom4j.Document;

import com.kyj.fx.b.ETScriptHelper.actions.comm.core.AbstractEtTab;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnCommitService;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnExcelTableViewList;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnLoadEquipmentClassEvent;

import javafx.scene.control.TableView;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EquipmentParameterTab extends AbstractEtTab
		implements OnLoadEquipmentClassEvent, OnCommitService, OnExcelTableViewList {
	EquipmentParameterComposite c = new EquipmentParameterComposite();

	/**
	 */
	public EquipmentParameterTab() {
		this.setText("Equipment Parameter");
		setContent(c);
		setDisable(true);
	}

	@Override
	public void reload() {
		c.reload();
	}

	@Override
	public void onCommit() {
		c.onCommit();
	}

	@Override
	public List<TableView<EventParameterDVO>> excelTableList() {
		return c.excelTableList();
	}

	@Override
	public void importExcel(File fromFile, Document doc) {
		c.importExcel(fromFile, doc);
	}

	@Override
	public void onLoadEquipmentClassEvent(String equipmentClassGuid, String eventGuid) {
		c.onLoadEquipmentClassEvent(equipmentClassGuid, eventGuid);
	}

	@Override
	public boolean enableCommitButton() {
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
