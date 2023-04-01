/**
 * 
 */
package com.kyj.fx.b.ETScriptHelper.actions.ec.eq.cp;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.dom4j.Document;

import com.kyj.fx.b.ETScriptHelper.actions.comm.core.AbstractEtTab;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnExcelTableViewList;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnLoadEquipment;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnReload;
import com.kyj.fx.b.ETScriptHelper.comm.DialogUtil;
import com.kyj.fx.b.ETScriptHelper.comm.ExcelReader;

import javafx.scene.control.TableView;

/**
 * @author KYJ
 *
 */
public class EquipmentCpTab extends AbstractEtTab implements OnLoadEquipment, OnReload, OnExcelTableViewList {

	private EquipmentCpComposite c = new EquipmentCpComposite();

	public EquipmentCpTab() {
		setText("Custom Properties");
		setContent(c);
		setDisable(true);
	}

	@Override
	public void reload() {
		c.reload();
	}

	@Override
	public void onLoadEquipment(String equipmentGuid) {
		c.onLoadEquipment(equipmentGuid);
	}

	@Override
	public boolean enableReloadButton() {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TableView<EquipmentCpPropertiesDVO>> excelTableList() {
		TableView<EquipmentCpPropertiesDVO> commonsBaseGridView = c.getCommonsBaseGridView();
		return Arrays.asList(commonsBaseGridView);
	}

	@Override
	public void importExcel(File fromFile, Document doc) {
		TableView<EquipmentCpPropertiesDVO> commonsBaseGridView = c.getCommonsBaseGridView();

		List<EquipmentCpPropertiesDVO> populateList;
		try {
			populateList = ExcelReader.populateList(doc, EquipmentCpPropertiesDVO.class);
			commonsBaseGridView.getItems().setAll(populateList);
		} catch (Exception e) {
			DialogUtil.showExceptionDailog(e);
		}
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
