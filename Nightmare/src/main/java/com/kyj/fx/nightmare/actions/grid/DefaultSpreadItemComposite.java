/**
 * 
 */
package com.kyj.fx.nightmare.actions.grid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.fxloader.FXMLController;
import com.kyj.fx.nightmare.actions.ai.OpenAIService;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.ui.frame.AbstractCommonsApp;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

/**
 * 
 */
@FXMLController(value = "DefaultSpreadItemView.fxml", isSelfController = true)
public class DefaultSpreadItemComposite extends AbstractCommonsApp {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSpreadItemComposite.class);

	@FXML
	private BorderPane borRoot, borContent;
	private OpenAIService openAIService;
	DefaultSpreadSheetView sv;
	private int row = 100;
	private int column = 100;
	public DefaultSpreadItemComposite() {
		try {
			FxUtil.loadRoot(DefaultSpreadItemComposite.class, this);
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}
	public DefaultSpreadItemComposite(int row, int column) {
		this.row = row;
		this.column = column;
		try {
			FxUtil.loadRoot(DefaultSpreadItemComposite.class, this);
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}
	
	public final DefaultSpreadSheetView getSpreadSheet() {
		return sv;
	}

	@FXML
	public void initialize() {
		sv = new DefaultSpreadSheetView(DefaultGridBase.createGrid(row, column));
		borContent.setCenter(sv);
	}

	public void setAiService(OpenAIService openAIService) {
		this.openAIService = openAIService;
	}

	private DefaultSpreadComposite parentComposite;

	public void setParentComposite(DefaultSpreadComposite defaultSpreadComposite) {
		this.parentComposite = defaultSpreadComposite;
	}

	private Tab currentTab;

	public void setCurrentTab(Tab e) {
		this.currentTab = e;
	}
	
	/*******************************************************************************************************************/
	
	
	@FXML
	public void txtPromptOnKeyPressed(){
		
	}
	@FXML
	public void btnEnterOnAction(){
		
	}
}
