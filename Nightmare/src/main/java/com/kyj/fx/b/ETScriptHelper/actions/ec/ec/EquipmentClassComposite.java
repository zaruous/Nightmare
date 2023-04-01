/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec
 *	작성일   : 2021. 11. 23.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.ec.ec;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnExcelTableViewList;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnLoadEquipmentClass;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnReload;
import com.kyj.fx.b.ETScriptHelper.actions.ec.ec.cp.EquipmentClassCpComposite;
import com.kyj.fx.b.ETScriptHelper.comm.AbstractDVO;
import com.kyj.fx.b.ETScriptHelper.comm.DialogUtil;
import com.kyj.fx.b.ETScriptHelper.comm.ExcelColumnExpression;
import com.kyj.fx.b.ETScriptHelper.comm.FxExcelUtil;
import com.kyj.fx.b.ETScriptHelper.comm.FxUtil;
import com.kyj.fx.b.ETScriptHelper.comm.ValueUtil;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EquipmentClassComposite extends BorderPane implements OnLoadEquipmentClass, OnExcelTableViewList, OnReload {

	private static Logger LOGGER = LoggerFactory.getLogger(EquipmentClassComposite.class);

	private StringProperty equipmentClassGuid = new SimpleStringProperty();

	@FXML
	private TextField txtEquipmentClassName;
	@FXML
	private TextArea txtEquipmentClassDesc;

	private EquipmentClassCpComposite ecCpComposite;
	public EquipmentClassComposite() {
		var loader = FxUtil.newLaoder();
		loader.setLocation(EquipmentClassComposite.class.getResource("EquipmentClassView.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
			
			
			BorderPane bp = loader.getRoot();
			ecCpComposite = new EquipmentClassCpComposite();
			bp.setBottom(ecCpComposite);
//			ecCpComposite.setPrefHeight(Double.MAX_VALUE);
			ecCpComposite.setMaxHeight(Double.MAX_VALUE);
		} catch (IOException e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}

	@FXML
	public void initialize() {
		this.equipmentClassGuid.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if (ValueUtil.isEmpty(arg2)) {
					txtEquipmentClassName.setText("");
					txtEquipmentClassDesc.setText("");
					return;
				}
				LOGGER.debug("ChangeListener  {} ", arg2);
				EquipmentClassesDAO equipmentClassesDAO = new EquipmentClassesDAO();
				EquipmentClassDVO equipmentClass = equipmentClassesDAO.getEquipmentClass(arg2);
				if (equipmentClass == null) {
					txtEquipmentClassName.setText("");
					txtEquipmentClassDesc.setText("");
				} else {
					txtEquipmentClassName.setText(equipmentClass.getName());
					txtEquipmentClassDesc.setText(equipmentClass.getDescription());
				}

			}
		});
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 23.
	 * @return
	 */
	public final StringProperty equipmentClassGuidProperty() {
		return this.equipmentClassGuid;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 23.
	 * @return
	 */
	public final String getEquipmentClassGuid() {
		return this.equipmentClassGuidProperty().get();
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 23.
	 * @param equipmentClassGuid
	 */
	public final void setEquipmentClassGuid(final String equipmentClassGuid) {
		this.equipmentClassGuidProperty().set(equipmentClassGuid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kyj.fx.b.ETScriptHelper.actions.comm.OnLoadEquipmentClass#
	 * onLoadEquipmentClass(java.lang.String, java.lang.String)
	 */
	@Override
	public void onLoadEquipmentClass(String equipmentClassGuid) {
//		this.equipmentClassGuid.set("");
		this.equipmentClassGuid.set(equipmentClassGuid);
		
		this.ecCpComposite.onLoadEquipmentClass(equipmentClassGuid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kyj.fx.b.ETScriptHelper.actions.comm.IExcelTableViewList#
	 * excelTableList()
	 * 
	 * @Deprecated not used.
	 */
	@Deprecated
	@Override
	public <T extends AbstractDVO> List<TableView<T>> excelTableList() {
		return null;
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

		try {
			LinkedHashMap<String, LinkedHashMap<ExcelColumnExpression, List<Object>>> dataSource = new LinkedHashMap<String, LinkedHashMap<ExcelColumnExpression, List<Object>>>();
			LinkedHashMap<ExcelColumnExpression, List<Object>> value = new LinkedHashMap<>();
			{
				ExcelColumnExpression key = new ExcelColumnExpression();
				key.setIndex(0);
				key.setVisible(false);
				value.put(key, Arrays.asList());
			}
			{
				ExcelColumnExpression key = new ExcelColumnExpression();
				key.setDisplayText("EquipmentClassName");
				key.setRealText("EquipmentClassName");
				key.setVisible(true);
				value.put(key, Arrays.asList(this.txtEquipmentClassName.getText()));
				key.setIndex(1);
			}

			{
				ExcelColumnExpression key = new ExcelColumnExpression();
				key.setDisplayText("EquipmentClassDesc");
				key.setRealText("EquipmentClassDesc");
				key.setVisible(true);
				key.setIndex(2);
				value.put(key, Arrays.asList(this.txtEquipmentClassDesc.getText()));
			}

			dataSource.put("Sheet0", value);
			HashMap<String, Map<String, String>> metadata = new HashMap<String, Map<String, String>>();
			HashMap<String, String> value2 = new HashMap<>();
			value2.put(FxExcelUtil.$$META_COLUMN_MAX_HEIGHT$$, "0");
			metadata.put("Sheet0", value2);

			/*
			if (FxExcelUtil.createExcel(out, dataSource, metadata))
				DialogUtil.showMessageDialog("작업에 성공하였습니다.");
			*/
			FxExcelUtil.createExcel(out, dataSource, metadata);
			return true;
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
			DialogUtil.showExceptionDailog(e);
		}

		return false;
	}

	@Override
	public void importExcel(File fromFile, Document doc) {
		var eqnm = doc.selectSingleNode("//Row/EquipmentClassName").getText();
		var eqdesc = doc.selectSingleNode("//Row/EquipmentClassDesc").getText();

		this.txtEquipmentClassName.setText(eqnm);
		this.txtEquipmentClassDesc.setText(eqdesc);
	}

	@Override
	public void reload() {
		String temp = this.equipmentClassGuid.get();		
		this.equipmentClassGuid.set(null);
		this.equipmentClassGuid.set(temp);
		
		this.ecCpComposite.reload();
	}

}
