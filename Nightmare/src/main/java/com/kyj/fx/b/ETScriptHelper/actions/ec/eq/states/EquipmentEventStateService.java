/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.ec.states
 *	작성일   : 2021. 11. 29.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.ec.eq.states;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.kyj.fx.b.ETScriptHelper.comm.ResourceLoader;
import com.kyj.fx.b.ETScriptHelper.comm.ValueUtil;
import com.kyj.fx.b.ETScriptHelper.comm.service.DmiService;
import com.kyj.fx.b.ETScriptHelper.comm.service.XMLUtils;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EquipmentEventStateService {

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 30. 
	 * @param saToken
	 * @param items
	 */
	public int updateEquipmentEvents(String saToken, List<EquipmentEventStateDVO> items) {

		Map<String, List<EquipmentEventStateDVO>> collect = items.stream().collect(Collectors.groupingBy(v -> {
			String groupKey = v.getEquipmentClassName() + "┐" + v.getEquipmentName();
			return groupKey;
		}));

		return (int) collect.entrySet()
			.stream().map(v -> {
			try {
				String equipmentClassName = v.getValue().get(0).getEquipmentClassName();
				String equipmentName = v.getValue().get(0).getEquipmentName();

				// 데이터베이스에서 이름으로 장비 guid를 찾는다.
				EquipmentEventStatesDAO dao = new EquipmentEventStatesDAO();
				String equipmentGuid = dao.getEquipmentGuid(equipmentClassName, equipmentName);
				if (ValueUtil.isEmpty(equipmentGuid))
					throw new RuntimeException("일치되는 장비 정보가 존재하지 않음.\t" + equipmentClassName + "\t" + equipmentName);

				// 웹서비스를 통해 이벤트에 대한 정보를 읽어온다.
				String execute = getEquipmentXmlInfo(equipmentGuid);
				if (execute == null)
					throw new RuntimeException("일치되는 장비 정보가 존재하지 않음.");

				Document doc = XMLUtils.load(execute);
				Element rootElement = doc.getRootElement();
				rootElement.remove(rootElement.getNamespace());
				
				//상태명에 일치하면 값을 변경.
				v.getValue().forEach(n -> {
					Node selectSingleNode = doc.selectSingleNode(String.format("//EquipmentEvent[@Name='%s']/@State", n.getName()));
					if (selectSingleNode != null)
						selectSingleNode.setText(n.getState());
				});

				String itemXml = rootElement.asXML();//doc.asXML();
//				System.out.println(itemXml);

				
				return updateEquipmentEvents(saToken,  "<![CDATA[" + itemXml + "]]>");

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}).filter(v -> v!=null).count();
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 29.
	 * @param equipmentClassName
	 * @param equipmentName
	 * @return
	 */
	public String getEquipmentGuid(String equipmentClassName, String equipmentName) {
		EquipmentEventStatesDAO dao = new EquipmentEventStatesDAO();
		return dao.getEquipmentGuid(equipmentClassName, equipmentName);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 29.
	 * @param equipmentGuid
	 * @return
	 * @throws Exception
	 */
	public String getEquipmentXmlInfo(String equipmentGuid) throws Exception {
		// 웹서비스를 통해 이벤트에 대한 정보를 읽어온다.

		DmiService s = new DmiService(
				ResourceLoader.getInstance().get(ResourceLoader.SYNCADE_ROOT_URL) + "/et/WebService/Equipment.asmx?wsdl");
		Object execute = s.execute("ListEvents", "EquipmentSoap", new String[] { equipmentGuid });
		return execute == null ? "" : execute.toString();
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 29.
	 * @param saToken
	 * @param itemXml
	 * @return
	 * @throws Exception
	 */
		// 웹서비스를 통해 이벤트에 대한 정보를 읽어온다.
	public String updateEquipmentEvents(String saToken, String itemXml) throws Exception {
		DmiService s = new DmiService(
				ResourceLoader.getInstance().get(ResourceLoader.SYNCADE_ROOT_URL) + "/et/WebService/EquipmentTx.asmx?wsdl");
		Object execute = s.execute("UpdateEquipmentEvents", "EquipmentTxSoap", new String[] { saToken,  itemXml  });
		return execute == null ? "" : execute.toString();
	}

}
