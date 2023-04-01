/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper
 *	작성일   : 2021. 7. 22.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.comm.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.kyj.fx.nightmare.comm.DbUtil;
import com.kyj.fx.nightmare.comm.FileUtil;
import com.kyj.fx.nightmare.comm.Hex;
import com.kyj.fx.nightmare.comm.IResultItemHandler;
import com.kyj.fx.nightmare.comm.ValueUtil;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class ExportEtScript {

	private String equipmentClassName;
	private File outRootDir;

	public String getEquipmentClassName() {
		return equipmentClassName;
	}

	public void setEquipmentClassName(String equipmentClassName) {
		this.equipmentClassName = equipmentClassName;
	}

	public File getOutRootDir() {
		return outRootDir;
	}

	public void setOutRootDir(File outRootDir) {
		this.outRootDir = outRootDir;
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 7. 22.
	 * @return
	 * @throws Exception
	 */
	public boolean export() throws Exception {

		if (ValueUtil.isEmpty(equipmentClassName))
			throw new Exception("equipment empty.");

		if (outRootDir == null)
			throw new FileNotFoundException("outRootDir empty.");

		return export(this.equipmentClassName, this.outRootDir);
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 11. 23.
	 * @param equipmentClassName
	 * @param outRootDir
	 * @return
	 * @throws Exception
	 */
	public boolean export(String equipmentClassName, File outRootDir) throws Exception {
		return export(equipmentClassName, outRootDir, new IResultItemHandler<Map<String, Object>>() {

			@Override
			public boolean next(Map<String, Object> m) {
				try {
					String _equipmentName = m.get("Equipment_Name").toString().trim();
					String eventName = m.get("EventName").toString().trim();
					String actionName = m.get("ActionName").toString().trim();
					String code = m.get("code") == null ? "" : m.get("code").toString().trim();
					byte[] b = Hex.decode(code);

					code = new String(b, "UTF-16LE");

					m.put("code", code);

					File outDir = new File(new File(outRootDir, _equipmentName), eventName);
					if (!outDir.exists())
						outDir.mkdirs();

					File outFile = new File(outDir, actionName + ".vbs");
					FileUtil.writeFile(outFile, code, StandardCharsets.US_ASCII, false);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				return true;
			}
		});
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 7. 22.
	 * @return
	 * @throws Exception
	 */
	public boolean export(String equipmentClassName, File outRootDir, IResultItemHandler<Map<String, Object>> handler) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		sb.append("\n");
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
		sb.append("    DMI_ET.dbo.ET_EventScripts b \n");
		sb.append("        on a.EventGUID = b.EventGUID \n");
		sb.append("        and a.ScriptGUID = b.ScriptGUID \n");
		sb.append("inner join\n");
		sb.append("    DMI_ET.dbo.ET_EquipmentClasses c \n");
		sb.append("        on a.EquipmentClassGUID = c.EquipmentClassGUID \n");
		sb.append("where 1=1\n");
		// sb.append(" and c.Name = 'USP3-Cell Bank Freezer'\n");
		sb.append("	and c.Name = '" + equipmentClassName + "'\n");
		sb.append("	and b.EventScriptGUID is not null\n");
		sb.append("order by\n");
		sb.append("    c.Name,\n");
		sb.append("    a.Name \n");

		String query = sb.toString();
		// println query

		File rootDir = outRootDir;
		// println("## Root Dir ##");
		// println(rootDir);
		// println("##############");
		if (!rootDir.exists())
			rootDir.mkdirs();

		
		Connection conn = null;
		try {

			conn = DbUtil.getConnection();

			List<Map<String, Object>> select = DbUtil.select(conn, query);
			for (Map<String, Object> m : select) {
				if (!handler.next(m))
					break;
			}
		} finally {
			if (conn != null)
				conn.close();
		}

		// com.kyj.fx.commons.utils.ExcelUtil.createExcelFromQuery(new
		// File("hey.xlsx"), conn, query);

		return true;
	}
}
