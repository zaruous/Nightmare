/**
 * 
 */
package com.kyj.fx.nightmare.actions.support;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.comm.DialogUtil;
import com.kyj.fx.nightmare.comm.ExecutorDemons;
import com.kyj.fx.nightmare.comm.FileUtil;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.ResourceLoader;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.grid.AnnotationOptions;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class ETScriptHelperComposite extends BorderPane {

	private static final Logger LOGGER = LoggerFactory.getLogger(ETScriptHelperComposite.class);

	@FXML
	private TextArea txtCode;
	@FXML
	private TableView<CommonsScriptPathDVO> tbCommonScriptPath;

	/**
	 */
	public ETScriptHelperComposite() {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ETScriptHelperComposite.class.getResource("ETScriptHelperView.fxml"));
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

		// FxUtil.installCommonsTableView(CommonsScriptPathDVO.class, tbCommonScriptPath,
		// new AnnotationOptions<CommonsScriptPathDVO>(CommonsScriptPathDVO.class));
		FxUtil.installCommonsTableView(CommonsScriptPathDVO.class, tbCommonScriptPath,
				new AnnotationOptions<CommonsScriptPathDVO>(CommonsScriptPathDVO.class));

		tbCommonScriptPath.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {

			if (ev.isConsumed())
				return;
			if (ev.getClickCount() == 2) {
				ev.consume();

				CommonsScriptPathDVO d = tbCommonScriptPath.getSelectionModel().getSelectedItem();
				if (d == null)
					return;

				String fullPath = d.getFileFullPath();
				try {
					this.txtCode.setText(FileUtil.readToString(new File(fullPath)));
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});

		ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(() -> after());
	}

	public void after() {

//		String basePath = ResourceLoader.getInstance().get("et.common.script.base.path");
//		if (ValueUtil.isEmpty(basePath)) {
////			showStatusMessage("et.common.script.base.path 설정 내용이 비어 있음.");
//			Platform.runLater(new Runnable() {
//				@Override
//				public void run() {
//					DialogUtil.showMessageDialog("et.common.script.base.path 설정 내용이 비어 있음.");
//				}
//			});
//			
//			return;
//		}
//
//		File file = new File(basePath);
//		if (!file.exists()) {
//			Platform.runLater(new Runnable() {
//				@Override
//				public void run() {
//					DialogUtil.showMessageDialog("et.common.script.base.path 접근 불가. : " + basePath);
//				}
//			});
//			
//			return;
//		}
//
//		DirWalker dirWalker = new DirWalker();
//		dirWalker.setRootFile(file);
//
//		ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(new Runnable() {
//
//			@Override
//			public void run() {
//				try {
//					ArrayList<CommonsScriptPathDVO> results = new ArrayList<CommonsScriptPathDVO>();
//					dirWalker.handleDirectory(file, 0, results);
//
//					Platform.runLater(new Runnable() {
//						@Override
//						public void run() {
//							tbCommonScriptPath.getItems().addAll(results);
//						}
//					});
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		});
		tbCommonScriptPath.getItems().addAll(listCommonsScriptPath());
		
	}


	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2022. 4. 28. 
	 * @return
	 */
	public static String getEtScriptBasePath() {
		return ResourceLoader.getInstance().get("et.common.script.base.path");
	}
	
	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2022. 4. 28. 
	 * @return
	 */
	public static List<CommonsScriptPathDVO> listCommonsScriptPath(){
		
		String basePath = ResourceLoader.getInstance().get("et.common.script.base.path");
		if (ValueUtil.isEmpty(basePath)) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					DialogUtil.showMessageDialog("et.common.script.base.path 설정 내용이 비어 있음.");
				}
			});
			
			return Collections.emptyList();
		}

		File file = new File(basePath);
		if (!file.exists()) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					DialogUtil.showMessageDialog("et.common.script.base.path 접근 불가. : " + basePath);
				}
			});			
			return Collections.emptyList();
		}

		DirWalker dirWalker = new DirWalker();
		dirWalker.setRootFile(file);
		ArrayList<CommonsScriptPathDVO> results = new ArrayList<CommonsScriptPathDVO>();
		try {
			dirWalker.handleDirectory(file, 0, results);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return results;
	}
	
	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 11. 19.
	 * @param message
	 */
	public void showStatusMessage(String message) {
		if (Platform.isFxApplicationThread()) {
			FxUtil.showStatusMessage(message);
		}
		else
			Platform.runLater(() -> {
				FxUtil.showStatusMessage(message);
			});
	}
}
