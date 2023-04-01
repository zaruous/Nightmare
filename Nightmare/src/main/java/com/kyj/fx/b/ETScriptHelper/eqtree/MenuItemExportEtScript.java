/**
 * 
 */
package com.kyj.fx.b.ETScriptHelper.eqtree;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.HashMap;

import com.kyj.fx.b.ETScriptHelper.comm.DbUtil;
import com.kyj.fx.b.ETScriptHelper.comm.DialogUtil;
import com.kyj.fx.b.ETScriptHelper.comm.FileUtil;
import com.kyj.fx.b.ETScriptHelper.comm.Hex;
import com.kyj.fx.b.ETScriptHelper.comm.StageStore;
import com.kyj.fx.b.ETScriptHelper.comm.ValueUtil;

import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;

/**
 * 
 * ET Script를 추출하여 지정한 디렉토리에 파일로 출력시킨다 <br/>
 * 
 * @author KYJ
 *
 */
public class MenuItemExportEtScript extends MenuItem {

	private EtConfigurationTreeView tv;

	public MenuItemExportEtScript(EtConfigurationTreeView tv, String text) {
		super(text);
		this.tv = tv;
		this.setOnAction(this::onAction);
	}

	/**
	 * @param ae
	 */
	private void onAction(ActionEvent ae) {
		TreeItem<EtConfigurationTreeDVO> selectedItem = this.tv.getSelectionModel().getSelectedItem();
		if (selectedItem == null)
			return;
		EtConfigurationTreeDVO value = selectedItem.getValue();
		if (value == null)
			return;
		String eqClassName = value.getDisplayText();
//		String eqClassGuid = value.getId();

		File dir = DialogUtil.showDirectoryDialog(StageStore.getPrimaryStage());
		if (dir == null)
			return;

		if(dir.isFile())return;
		
		
		export(dir, eqClassName);
	}

	public boolean export(File outRootDir, String eqClassName) {

		/*********************************************************
		 * Equipment Event의 스크립트를 데이터베이스로부터 조회한뒤 파일에 Write.
		 **********************************************************************/

		StringBuffer sb = new StringBuffer();
//		sb.append("\n");
//		sb.append("\n");
		sb.append("with eee as ( select\n");
		sb.append("    a.Name [Name],\n");
		sb.append("    a.EventGUID,\n");
		sb.append("    a.EquipmentClassGUID,\n");
		sb.append("    d.Name [ActionName],\n");
		sb.append("    d.ScriptGUID \n");
		sb.append("from\n");
		sb.append("    DMI_ET.dbo.ET_Events a cross \n");
		sb.append("join\n");
		sb.append("    DMI_ET.dbo.ET_AvailableScripts d ) select\n");
		sb.append("    c.Name as [Equipment_Name],\n");
		sb.append("    a.Name as [EventName],\n");
		sb.append("    a.ActionName,\n");
		sb.append("    b.EventScriptGUID,\n");
		sb.append("    b.code,\n");
		sb.append("    b.eventguid,\n");
		sb.append("    a.EquipmentClassGUID \n");
		sb.append("from\n");
		sb.append("    eee a \n");
		sb.append("left outer join\n");
		sb.append("    DMI_ET.dbo.ET_EventScripts b(nolock) \n");
		sb.append("        on a.EventGUID = b.EventGUID \n");
		sb.append("        and a.ScriptGUID = b.ScriptGUID \n");
		sb.append("inner join\n");
		sb.append("    DMI_ET.dbo.ET_EquipmentClasses c(nolock) \n");
		sb.append("        on a.EquipmentClassGUID = c.EquipmentClassGUID \n");
		sb.append("where 1=1\n");
		sb.append("	and c.Name = '$eqClassName'\n");
		// sb.append(" and ( UPPER(c.Name) LIKE '%AUTOCLAVE%' or UPPER(c.Name) LIKE
		// '%GWD%' UPPER(c.Name) LIKE '%COP%' )\n");
		sb.append("	and b.EventScriptGUID is not null\n");
		sb.append("order by\n");
		sb.append("    c.Name,\n");
		sb.append("    a.Name \n");
//		sb.toString();

		var query = sb.toString();

		var rootDir = outRootDir; // new File("C:\\Users\\KYJ\\SBLSource_20201102\\ETScript");

		if (!rootDir.exists())
			rootDir.mkdirs();

//		Connection conn = null;
		try (Connection conn = DbUtil.getConnection()) {

			var dic = new HashMap<String, Object>();
			dic.put("eqClassName", eqClassName);
			query = ValueUtil.getVelocityToText(query, dic);
			var result = DbUtil.select(conn, query);
			for (var m : result) {
				var equipmentName = m.get("Equipment_Name").toString().trim();
				var eventName = m.get("EventName").toString().trim();
				var actionName = m.get("ActionName");
				var code = m.get("code") == null ? "" : m.get("code").toString().trim();
				var b = Hex.decode(code);

				code = new String(b, "UTF-16LE");

				m.put("code", code);

//					println(equipmentName + "|" + eventName + "|" + actionName);		
				var outDir = new File(new File(rootDir, equipmentName), eventName);
				if (!outDir.exists())
					outDir.mkdirs();

				var outFile = new File(outDir, actionName + ".vbs");
				FileUtil.writeFile(outFile, code, StandardCharsets.US_ASCII, false);

			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
