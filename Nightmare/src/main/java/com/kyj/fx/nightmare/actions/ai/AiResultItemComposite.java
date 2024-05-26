/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

import com.kyj.fx.fxloader.FXMLController;
import com.kyj.fx.nightmare.comm.FxUtil;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

/**
 * 
 */
@FXMLController(value = "AiResultItemView.fxml", isSelfController = true, css = "AiResultItemView.css")
public class AiResultItemComposite extends BorderPane implements IResultItem {

	@FXML
	private BorderPane borRoot;
	@FXML
	private TextArea txtUserCommand;

	public AiResultItemComposite() throws Exception {
		FxUtil.loadRoot(AiResultItemComposite.class, this);
	}

	@FXML
	public void initialize() {
	
	}

	@Override
	public String getText() {
		return txtUserCommand.getText();
	}

	@Override
	public void setUserCommand(Object command) {
		this.txtUserCommand.setText(command.toString());
	}

	public void setResult(String content) {
		TextArea value = new TextArea(content);
		value.setEditable(false);
		this.borRoot.setCenter(value);
	}

}
