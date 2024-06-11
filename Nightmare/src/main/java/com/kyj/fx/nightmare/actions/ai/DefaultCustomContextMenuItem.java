/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.MenuItem;

/**
 * 
 */
public class DefaultCustomContextMenuItem extends MenuItem {
	ObjectProperty<TbmSmPrompts> item = new SimpleObjectProperty<TbmSmPrompts>();

	public DefaultCustomContextMenuItem(TbmSmPrompts prompt) {
		this.item.set(prompt);
		setText(prompt.getDisplayText());
	}

	public final ObjectProperty<TbmSmPrompts> itemProperty() {
		return this.item;
	}

	public final TbmSmPrompts getItem() {
		return this.itemProperty().get();
	}

	public final void setItem(final TbmSmPrompts item) {
		this.itemProperty().set(item);
	}

}
