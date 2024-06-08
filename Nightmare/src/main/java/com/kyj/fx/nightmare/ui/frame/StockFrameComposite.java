/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.frame
 *	작성일   : 2023. 4. 1.
 *	작성자   : (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.ui.frame;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.fxloader.FxPostInitialize;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.ui.stock.StockDataMasterDVO;
import com.kyj.fx.nightmare.ui.stock.StockDataService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

/**
 * @author (zaruous@naver.com)
 *
 */
public class StockFrameComposite extends AbstractCommonsApp {

	private static final Logger LOGGER = LoggerFactory.getLogger(StockFrameComposite.class);

	@FXML
	private Label lblStatus;

	@FXML
	TabPane tpManagement;

	@FXML
	private Button btnCommit, btnReload, btnExportExcel, btnImportExcel;

	@FXML
	BorderPane borRoot, borLeft;

	public StockFrameComposite() {
		FXMLLoader loader = FxUtil.newLaoder();
		loader.setLocation(StockFrameComposite.class.getResource("StockView.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}

	@FXML
	public void initialize() {

	}

	@FxPostInitialize
	public void loadData() {
		try {
			StockDataMasterDVO doService = StockDataService.doService();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void showStatusMessage(String message) {
		this.lblStatus.setText(message);
	}

}
