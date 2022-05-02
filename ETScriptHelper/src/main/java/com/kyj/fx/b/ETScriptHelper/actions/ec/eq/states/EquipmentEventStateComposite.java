/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec
 *	작성일   : 2021. 11. 23.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.ec.eq.states;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnExcelTableViewList;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnCommitService;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnLoadEquipment;
import com.kyj.fx.b.ETScriptHelper.actions.comm.core.OnLoadEquipmentClass;
import com.kyj.fx.b.ETScriptHelper.comm.DialogUtil;
import com.kyj.fx.b.ETScriptHelper.comm.ExcelReader;
import com.kyj.fx.b.ETScriptHelper.comm.FxUtil;
import com.kyj.fx.b.ETScriptHelper.comm.ValueUtil;
import com.kyj.fx.b.ETScriptHelper.grid.AnnotationOptions;
import com.kyj.fx.b.ETScriptHelper.grid.CrudBaseColumnMapper;
import com.kyj.fx.b.ETScriptHelper.grid.IOptions;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EquipmentEventStateComposite extends BorderPane
		implements OnLoadEquipmentClass, OnLoadEquipment, OnExcelTableViewList, OnCommitService  {

	private static Logger LOGGER = LoggerFactory.getLogger(EquipmentEventStateComposite.class);

	private StringProperty equipmentClassGuid = new SimpleStringProperty();
	private StringProperty equipmentGuid = new SimpleStringProperty();

	@FXML
	TableView<EquipmentEventStateDVO> tvEventStates;

	public EquipmentEventStateComposite() {
		var loader = FxUtil.newLaoder();
		loader.setLocation(EquipmentEventStateComposite.class.getResource("EquipmentEventStateView.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}

	@FXML
	public void initialize() {

		CrudBaseColumnMapper<EquipmentEventStateDVO> columnMapper = new CrudBaseColumnMapper<EquipmentEventStateDVO>() {

			@Override
			public TableColumn<EquipmentEventStateDVO, ?> generateTableColumns(Class<?> classType, String columnName, IOptions options) {
				if ("state".equals(columnName)) {
					TableColumn<EquipmentEventStateDVO, String> tc = new TableColumn<>("state");
					tc.setEditable(true);
					tc.setCellFactory(TextFieldTableCell.forTableColumn());
					tc.setCellValueFactory(
							new Callback<TableColumn.CellDataFeatures<EquipmentEventStateDVO, String>, ObservableValue<String>>() {

								@Override
								public ObservableValue<String> call(CellDataFeatures<EquipmentEventStateDVO, String> arg0) {
									return arg0.getValue().stateProperty();
								}
							});

					return tc;
				} else {
					return super.generateTableColumns(classType, columnName, options);
				}
			}
		};
		FxUtil.installCommonsTableView(EquipmentEventStateDVO.class, tvEventStates, columnMapper,
				new AnnotationOptions<EquipmentEventStateDVO>(EquipmentEventStateDVO.class));

		this.tvEventStates.setEditable(true);

		this.equipmentGuid.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if (ValueUtil.isEmpty(arg2)) {
					tvEventStates.getItems().clear();					
					return;
				}

				EquipmentEventStatesDAO dao = new EquipmentEventStatesDAO();
				var hashMap = new HashMap<String, Object>();
				hashMap.put("equipmentGuid", equipmentGuid.get());
				List<EquipmentEventStateDVO> listEventStates = dao.listEventStates(hashMap);
				tvEventStates.getItems().setAll(listEventStates);

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
		this.equipmentClassGuidProperty().set(null);
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
		LOGGER.debug("{} ", equipmentClassGuid);
		this.equipmentClassGuid.set(equipmentClassGuid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kyj.fx.b.ETScriptHelper.actions.comm.OnLoadEquipment#onLoadEquipment(
	 * java.lang.String)
	 */
	@Override
	public void onLoadEquipment(String equipmentGuid) {
		this.equipmentGuid.set(null);
		this.equipmentGuid.set(equipmentGuid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kyj.fx.b.ETScriptHelper.actions.comm.IExcelTableViewList#
	 * excelTableList()
	 */
	@Override
	public List<TableView<EquipmentEventStateDVO>> excelTableList() {
		return Arrays.asList(tvEventStates);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kyj.fx.b.ETScriptHelper.actions.comm.OnCommitService#onCommit()
	 */
	@Override
	public void onCommit() {

		DialogUtil.showVerifierESigDialog("5", "Syncade", "DMI ET", "EquipmentEventStates", "0", new Callback<Map<String, String>, Void>() {

			@Override
			public Void call(Map<String, String> pair) {
				String token = pair.get("token");
				if (ValueUtil.isEmpty(token)) {
					String msg = pair.get("err");
					DialogUtil.showMessageDialog("SAToken required.\n" + msg);
					return null;
				}

				ObservableList<EquipmentEventStateDVO> items = tvEventStates.getItems();
				new EquipmentEventStateService().updateEquipmentEvents(token, items);

				return null;
			}
		});

	}

	@Override
	public void importExcel(File fromFile, Document doc) {
		List<EquipmentEventStateDVO> populateList;
		try {
			populateList = ExcelReader.populateList(doc, EquipmentEventStateDVO.class);
			this.tvEventStates.getItems().setAll(populateList);
		} catch (Exception e) {
			DialogUtil.showExceptionDailog(e);
		}

	}

	@Override
	public boolean exportExcel(File out) {
		return OnExcelTableViewList.super.exportExcel(out);
	}

}
