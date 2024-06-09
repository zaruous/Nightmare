/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

import javax.sound.sampled.Mixer.Info;

import com.kyj.fx.nightmare.comm.AbstractDAO;
import com.kyj.fx.nightmare.ui.grid.ColumnVisible;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 
 */
public class InfoDVO extends AbstractDAO {

	private StringProperty name = new SimpleStringProperty();
	private StringProperty desc = new SimpleStringProperty();
	@ColumnVisible(false)
	private ObjectProperty<Info> info = new SimpleObjectProperty();

	public InfoDVO(Info info) {
		name.set(info.getName());
		desc.set(info.getDescription());
		this.info.set(info);
	}

	public final StringProperty nameProperty() {
		return this.name;
	}

	public final String getName() {
		return this.nameProperty().get();
	}

	public final void setName(final String name) {
		this.nameProperty().set(name);
	}

	public final StringProperty descProperty() {
		return this.desc;
	}

	public final String getDesc() {
		return this.descProperty().get();
	}

	public final void setDesc(final String desc) {
		this.descProperty().set(desc);
	}

	public final ObjectProperty<Info> infoProperty() {
		return this.info;
	}

	public final Info getInfo() {
		return this.infoProperty().get();
	}

	public final void setInfo(final Info info) {
		this.infoProperty().set(info);
	}

}
