/**
 * 
 */
package com.kyj.fx.nightmare.actions.ec.ec.cp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.kyj.fx.nightmare.actions.comm.core.OnLoadEquipmentClass;
import com.kyj.fx.nightmare.actions.comm.core.OnReload;
import com.kyj.fx.nightmare.comm.AbstractDAO;
import com.kyj.fx.nightmare.comm.DialogUtil;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.comm.template.Sql;
import com.kyj.fx.nightmare.comm.template.Template;
import com.kyj.fx.nightmare.ui.grid.AnnotationOptions;
import com.kyj.fx.nightmare.ui.grid.CommonsBaseGridView;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.BorderPane;

/**
 * @author KYJ
 *
 */
public class EquipmentClassCpComposite extends BorderPane implements OnReload, OnLoadEquipmentClass {

	private static final String EC_CUSTOM_PROPERTIES_XML = "ecCustomProperties.xml";
	private CommonsBaseGridView<EquipmentClassCpPropertiesDVO> commonsBaseGridView;
	private StringProperty equipmentClassGuid = new SimpleStringProperty();

	public EquipmentClassCpComposite() {
		commonsBaseGridView = new CommonsBaseGridView<EquipmentClassCpPropertiesDVO>(EquipmentClassCpPropertiesDVO.class,
				new AnnotationOptions<EquipmentClassCpPropertiesDVO>(EquipmentClassCpPropertiesDVO.class));
		setCenter(commonsBaseGridView);
		commonsBaseGridView.setEditable(false);
		commonsBaseGridView.getSelectionModel().setCellSelectionEnabled(true);
		FxUtil.installClipboardKeyEvent(commonsBaseGridView);

		this.equipmentClassGuid.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (ValueUtil.isNotEmpty(newValue)) {

					try {
						Template.load(EquipmentClassCpComposite.class.getResource(EC_CUSTOM_PROPERTIES_XML));
						Template getTemplate = Template.load(EquipmentClassCpComposite.class.getResource(EC_CUSTOM_PROPERTIES_XML));
						Sql sql = getTemplate.getSql();

						Map<String, Object> hashMap = new HashMap<String, Object>();
						hashMap.put("equipmentClassGuid", newValue);

						List<EquipmentClassCpPropertiesDVO> list = new CpDAO().list(sql, hashMap);
						commonsBaseGridView.getItems().setAll(list);

					} catch (Exception e) {
						DialogUtil.showExceptionDailog(e);
					}

				}
			}
		});
	}

	/**
	 * @return
	 */
	public CommonsBaseGridView<EquipmentClassCpPropertiesDVO> getCommonsBaseGridView() {
		return commonsBaseGridView;
	}

	@Override
	public void reload() {
		String tmpEquipmentClassGuid = equipmentClassGuid.get();
		onLoadEquipmentClass(null);
		onLoadEquipmentClass(tmpEquipmentClassGuid);
	}

	class CpDAO extends AbstractDAO {

		/**
		 * @param sql
		 * @param hashMap
		 * @return
		 */
		public List<EquipmentClassCpPropertiesDVO> list(Sql sql, Map<String, Object> hashMap) {
			return query(sql.getContent(), hashMap,
					new BeanPropertyRowMapper<EquipmentClassCpPropertiesDVO>(EquipmentClassCpPropertiesDVO.class));
		}
	}

	@Override
	public void onLoadEquipmentClass(String equipmentClassGuid) {
		this.equipmentClassGuid.set(equipmentClassGuid);
	}
}
