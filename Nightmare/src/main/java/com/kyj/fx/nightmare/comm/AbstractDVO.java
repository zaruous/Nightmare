/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.comm
 *	작성일   : 2021. 11. 22.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.comm;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.kyj.fx.nightmare.ui.grid.CommonConst;

import javafx.beans.property.Property;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class AbstractDVO {
	private BooleanProperty commonsClicked;

	private String _status;

	public static final String UPDATE = CommonConst._STATUS_UPDATE;
	public static final String DELETE = CommonConst._STATUS_DELETE;
	public static final String CREATE = CommonConst._STATUS_CREATE;

	public AbstractDVO() {

		commonsClicked = new SimpleBooleanProperty();
	}

	public boolean getClicked() {
		return commonsClicked.get();
	}

	public void setClicked(boolean clicked) {
		this.commonsClicked.set(clicked);
	}

	public BooleanProperty commonsClickedProperty() {
		return commonsClicked;
	}

	public String get_status() {
		return _status;
	}

	public void set_status(String _status) {
		this._status = _status;
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 12. 10.
	 * @return
	 */
	public Map<String, Object> getMetadata() {
		Field[] declaredFields = this.getClass().getDeclaredFields();
		var m = new HashMap<String, Object>();
		try {
			for (Field f : declaredFields) {
				String name = f.getName();
				f.setAccessible(true);
				Object v = f.get(this);

				if (Property.class.isAssignableFrom(v.getClass())) {
					Property p = (Property) v;
					m.put(name, p.getValue());
				} else
					m.put(name, v);
			}
		} catch (Exception ex) {
			/*Nothing*/
		}
		return m;
	}

}
