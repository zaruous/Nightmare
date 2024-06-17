/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

import javafx.beans.property.StringProperty;
import javafx.scene.Node;

/**
 * 
 */
public interface ICustomSupportView {

	/**
	 * @return
	 */
	public Node getView();

	public void setData(String data);

	public String getData();
	
	public StringProperty dataProperty();
}
