/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.ec.group
 *	작성일   : 2021. 12. 17.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.ec.ec.group;

import java.util.HashMap;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.kyj.fx.b.ETScriptHelper.comm.AbstractDAO;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EquipmentClassGroupDAO extends AbstractDAO {

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 17.
	 * @param equipmentClassGuid
	 * @return
	 */
	public List<EquipmentClassGroupDVO> listGroup(String equipmentClassGuid) {
		StringBuffer sb = new StringBuffer();
		sb.append("select\n");
		sb.append("g.EquipmentClassGUID AS EQUIPMENT_CLASS_GUID,\n");
		sb.append("ec.Name AS EQUIPMENT_CLASS_NAME,\n");
		sb.append("g.RulesGroupGUID AS RULES_GROUP_GUID,\n");
		sb.append("g.Sequence AS SEQUENCE,\n");
		sb.append("g.Type AS GROUP_TYPE,\n");
		sb.append("g.Name AS GROUP_NAME,\n");
		sb.append("g.StampGUID as STAMP_GUID, \n");
		sb.append("case g.Type when 0 then 'Execute Until True'\n");
		sb.append("	 when 1 then 'Execute All'\n");
		sb.append("	 else '' end as GROUP_TYPE_NAME\n");
		
		sb.append("from DMI_ET.dbo.ET_EquipmentClassRuleGroups g   with (nolock) inner join DMI_ET.dbo.ET_EquipmentClasses ec with (nolock)\n");
		sb.append("	on g.EquipmentClassGUID = ec.EquipmentClassGUID\n");
		sb.append("\n");
		sb.append("where 1=1 and g.EquipmentClassGUID = :equipmentClassGuid\n");
		sb.append("order by Sequence\n");
		
		var p = new HashMap<String, Object>();
		p.put("equipmentClassGuid", equipmentClassGuid);

		return query(sb.toString(), p, new BeanPropertyRowMapper<EquipmentClassGroupDVO>(EquipmentClassGroupDVO.class));
	}
}
