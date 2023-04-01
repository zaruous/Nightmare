/**
 * 
 */
package com.kyj.fx.nightmare.actions.ec.eq.cp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.kyj.fx.nightmare.actions.comm.core.OnLoadEquipment;
import com.kyj.fx.nightmare.actions.comm.core.OnReload;
import com.kyj.fx.nightmare.comm.AbstractDAO;
import com.kyj.fx.nightmare.comm.DialogUtil;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.comm.template.Sql;
import com.kyj.fx.nightmare.comm.template.Template;
import com.kyj.fx.nightmare.grid.AnnotationOptions;
import com.kyj.fx.nightmare.grid.CommonsBaseGridView;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.BorderPane;

/**
 * @author KYJ
 *
 */
public class EquipmentCpComposite extends BorderPane implements OnLoadEquipment, OnReload {

	private CommonsBaseGridView<EquipmentCpPropertiesDVO> commonsBaseGridView;
	private StringProperty equipmentGuid = new SimpleStringProperty();
	
	public EquipmentCpComposite() {
		commonsBaseGridView = new CommonsBaseGridView<EquipmentCpPropertiesDVO>(
				EquipmentCpPropertiesDVO.class, new AnnotationOptions<EquipmentCpPropertiesDVO>(EquipmentCpPropertiesDVO.class));
		setCenter(commonsBaseGridView);
		commonsBaseGridView.getSelectionModel().setCellSelectionEnabled(true);
		FxUtil.installClipboardKeyEvent(commonsBaseGridView);
	}

	
	/**
	 * @return
	 */
	public CommonsBaseGridView<EquipmentCpPropertiesDVO> getCommonsBaseGridView() {
		return commonsBaseGridView;
	}


	@Override
	public void onLoadEquipment(String equipmentGuid) {
		try {
			Template.load(EquipmentCpComposite.class.getResource("etCustomProperties.xml"));
			Template getTemplate = Template.load(EquipmentCpComposite.class.getResource("etCustomProperties.xml"));
			Sql sql = getTemplate.getSql();

			Map<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("equipmentGuid", equipmentGuid);

			List<EquipmentCpPropertiesDVO> list = new CpDAO().list(sql, hashMap);
			commonsBaseGridView.getItems().setAll(list);
			
			this.equipmentGuid.set(equipmentGuid);
		} catch (Exception e) {
			DialogUtil.showExceptionDailog(e);
		}
	}

	@Override
	public void reload() {
		if(ValueUtil.isEmpty(equipmentGuid.get()))
		return;
		
		onLoadEquipment(equipmentGuid.get());
	}

	class CpDAO extends AbstractDAO {
		
		/**
		 * @param sql
		 * @param hashMap
		 * @return
		 */
		public List<EquipmentCpPropertiesDVO> list(Sql sql, Map<String, Object> hashMap) {
			return query(sql.getContent(), hashMap,
					new BeanPropertyRowMapper<EquipmentCpPropertiesDVO>(EquipmentCpPropertiesDVO.class));
		}
	}
}
