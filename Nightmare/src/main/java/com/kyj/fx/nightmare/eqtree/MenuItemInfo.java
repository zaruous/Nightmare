/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.eqtree
 *	작성일   : 2021. 11. 22.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.nightmare.eqtree;

import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.JsonFormatter;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.eqtree.EtConfigurationTreeItem.Action;

import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.stage.Modality;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class MenuItemInfo extends MenuItem {

	private EtConfigurationTreeView tvEtConfigurationTree;

	public MenuItemInfo(EtConfigurationTreeView tvEtConfigurationTree, String text) {
		super(text);
		this.tvEtConfigurationTree = tvEtConfigurationTree;
		this.setOnAction(this::ctmAddOnAction);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 22.
	 * @param ae
	 */
	private void ctmAddOnAction(ActionEvent ae) {

		// Object source = ae.getSource();
		TreeItem<EtConfigurationTreeDVO> selectedItem = tvEtConfigurationTree.getSelectionModel().getSelectedItem();

		if (selectedItem instanceof EtConfigurationTreeItem) {
			EtConfigurationTreeItem item = (EtConfigurationTreeItem) selectedItem;
			EtConfigurationTreeDVO value = item.getValue();
//			String equipmentClassGuid = value.getId();
//			String displayText = value.getDisplayText();
			Action action = item.getAction();

			switch (action) {
			case EC_EQ:

				// List<EtConfigurationTreeItem> listEquipments = listEquipments(equipmentClassGuid);
				// selectedItem.getChildren().setAll(listEquipments);

				break;
			default:
				String ret = ValueUtil.toJSONString(value);

				FxUtil.createStageAndShow(new TextArea(new JsonFormatter().format(ret)), stage -> {
					stage.setTitle("Info");
					stage.setWidth(800d);
					stage.setHeight(800d);
					stage.initModality(Modality.APPLICATION_MODAL);
				});
				// DialogUtil.showMessageDialog(String.format("ET. classs Gneral %1$s, %2$s %3$S", equipmentClassGuid, displayText,
				// action));
				break;
			}
		}
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 22.
	 * @param equipmentClassGuid
	 * @return
	 */
	// private List<EtConfigurationTreeItem> listEquipments(String equipmentClassGuid) {
	// try {
	// EquipmentDAO equipmentDAO = new EquipmentDAO();
	// HashMap<String, Object> hashMap = new HashMap<String,Object>();
	// hashMap.put("equipmentClassGuid", equipmentClassGuid);
	// return equipmentDAO.listEquipment(hashMap, new RowMapper<EtConfigurationTreeItem>() {
	//
	// @Override
	// public EtConfigurationTreeItem mapRow(ResultSet rs, int rowNum) throws SQLException {
	// EquipmentDVO equipmentDVO = new EquipmentDVO(rs.getString("EquipmentGUID"), rs.getString("EuipmentName"));
	// equipmentDVO.setEquipmentClassName(rs.getString("Name"));
	//
	// return new EtConfigurationTreeItem( rs.getString("EquipmentGUID"), rs.getString("EuipmentName"), Action.EC_EQ_ITEM);
	// }
	// });
	// } catch (Exception e) {
	// throw new RuntimeException(e);
	// }
	// }

}
