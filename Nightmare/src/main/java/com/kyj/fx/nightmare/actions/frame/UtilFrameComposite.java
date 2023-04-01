/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.frame
 *	작성일   : 2023. 4. 1.
 *	작성자   : (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.frame;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.filetree.DefaultFileTreeItem;
import com.kyj.fx.nightmare.filetree.DefaultFileTreeView;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * @author (zaruous@naver.com)
 *
 */
public class UtilFrameComposite extends AbstractCommonsApp {

	private static final Logger LOGGER = LoggerFactory.getLogger(UtilFrameComposite.class);

	@FXML
	TabPane tpEtManagement;

	@FXML
	private Button btnCommit, btnReload, btnExportExcel, btnImportExcel;

	@FXML
	BorderPane borLeft;
	
	private DefaultFileTreeView fileTreeView;

	public UtilFrameComposite() {
		FXMLLoader loader = FxUtil.newLaoder();
		loader.setLocation(UtilFrameComposite.class.getResource("UtilView.fxml"));
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
		fileTreeView = new DefaultFileTreeView();
		fileTreeView.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
		borLeft.setCenter(fileTreeView);
//		this.apTree.getChildren().add(fileTreeView);
//
//		AnchorPane.setLeftAnchor(apTree, 5.0);
//		AnchorPane.setTopAnchor(apTree, 5.0);
//		AnchorPane.setRightAnchor(apTree, 5.0);
//		AnchorPane.setBottomAnchor(apTree, 5.0);

		
		Path path = Paths.get(new File("").getAbsolutePath());
		fileTreeView.setRoot(new DefaultFileTreeItem(path));
	}

	@FXML
	public void miVersionOnAction() {

	}

	@FXML
	public void txtClassNameFilterOnKeyPressed() {

	}

	@FXML
	public void btnExportExcel() {

	}

	@FXML
	public void btnImportExcel() {

	}

	@FXML
	public void btnCommitOnAction() {

	}

	@FXML
	public void btnReloadOnAction() {

	}
}
