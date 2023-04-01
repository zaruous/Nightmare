/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper
 *	작성일   : 2021. 7. 6.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.nightmare;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.kyj.fx.nightmare.actions.ec.ec.scripts.EquipmentScriptDVO;
import com.kyj.fx.nightmare.comm.DateUtil;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class Backup {

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 19. 
	 * @param d
	 */
	@Deprecated
	public static void backup(EquipmentScriptDVO d) {
		String equipmentClassName = d.getEquipmentClassName().trim();
		String eventName = d.getEventName().trim();
		String actionName = d.getActionName().trim();

		String dirPath = String.format("%s\\%s\\%s", ".backup", equipmentClassName, eventName);
		File dir = new File(dirPath);
		if (!dir.exists())
			dir.mkdirs();
		
		File file = new File(dir, actionName + "_" + DateUtil.getCurrentDateString("yyyyMMddHHmm") + ".vbs");
		
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(d.getCode() == null ? "" : d.getCode());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
