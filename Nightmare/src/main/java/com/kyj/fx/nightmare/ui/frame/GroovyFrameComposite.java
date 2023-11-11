/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.frame
 *	작성일   : 2023. 4. 1.
 *	작성자   : (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.ui.frame;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.groovy.DefaultScriptEngine;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.ResourceLoader;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.ui.tab.ExecutionDefaultTab;
import com.kyj.fx.nightmare.ui.tab.SystemDefaultFileTabPaneManager;
import com.kyj.fx.nightmare.ui.tree.filetree.DefaultFileTreeItem;
import com.kyj.fx.nightmare.ui.tree.filetree.DefaultFileTreeView;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

/**
 * @author (zaruous@naver.com)
 *
 */
public class GroovyFrameComposite extends AbstractCommonsApp {

	private static final Logger LOGGER = LoggerFactory.getLogger(GroovyFrameComposite.class);

	@FXML
	TabPane tpManagement;

	@FXML
	private Button btnCommit, btnReload, btnExportExcel, btnImportExcel;

	@FXML
	BorderPane borRoot, borLeft;

	private DefaultFileTreeView fileTreeView;

	private SystemDefaultFileTabPaneManager<ExecutionDefaultTab> tabPaneManager;

	public GroovyFrameComposite() {
		FXMLLoader loader = FxUtil.newLaoder();
		loader.setLocation(GroovyFrameComposite.class.getResource("Groovy.fxml"));
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

		tabPaneManager = new SystemDefaultFileTabPaneManager<ExecutionDefaultTab>(this.tpManagement) {

			@Override
			public ExecutionDefaultTab createNewTab(File f) {
				return new ExecutionDefaultTab(f);
			}

		};

		// tab context Menu setup
		this.tpManagement.setContextMenu(tabCommonContextMenu());

		/* draw fileTreeView */
		fileTreeView = new DefaultFileTreeView();
		fileTreeView.setFileFilter(path -> {
			File file = path.toFile();
			if (file.isDirectory())
				return true;
			return true;
		});
		fileTreeView.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
		borLeft.setCenter(fileTreeView);
		fileTreeView.addEventHandler(MouseEvent.MOUSE_CLICKED, fileTreeViewOnMouseClick);

		String defLoacation = ResourceLoader.getInstance().get(ResourceLoader.DEFAULT_WORKSPACE_PATH);
		// treeview default path.
		Path path = Paths.get(new File(defLoacation).getAbsolutePath());
		fileTreeView.setRoot(new DefaultFileTreeItem(path));

	}

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 4. 2.
	 * @return
	 */
	private ContextMenu tabCommonContextMenu() {
		MenuItem menuItem = new MenuItem("Close");
		menuItem.setOnAction(ev -> {
			Tab selectedItem = tpManagement.getSelectionModel().getSelectedItem();
			if (selectedItem == null)
				return;
			tpManagement.getTabs().remove(selectedItem);
		});

		MenuItem menuItem2 = new MenuItem("CloseAll");
		menuItem2.setOnAction(closeAllEventHandler);

		menuItem2.setAccelerator(
				new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
		return new ContextMenu(menuItem, menuItem2);
	}

	@FXML
	public void miVersionOnAction() {

	}

	EventHandler<ActionEvent> closeAllEventHandler = ev -> {
		tpManagement.getTabs().clear();
	};

	private EventHandler<MouseEvent> fileTreeViewOnMouseClick = ev -> {
		if (ev.getClickCount() == 2 && ev.getButton() == MouseButton.PRIMARY) {
			// if (ev.isConsumed())
			// return;
			// ev.consume();

			TreeItem<Path> selectedItem = fileTreeView.getSelectionModel().getSelectedItem();
			if (selectedItem == null)
				return;
			Path value = selectedItem.getValue();
			File f = value.toFile();
			if (!f.exists() || f.isDirectory())
				return;

			loadFile(f);

		}
	};

	@FXML
	public void txtClassNameFilterOnKeyPressed() {

	}

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 4. 2.
	 * @param f
	 */
	private void loadFile(File f) {
		if (f.isDirectory())
			return;

		Tab add = tabPaneManager.add(f);

	}

	@FXML
	public void btnExportExcel() {

	}

	@FXML
	public void btnImportExcel() {

	}

	@FXML
	public void btnRunOnAction() throws IOException {
		ExecutionDefaultTab activeTab = tabPaneManager.getActiveTab();
		String codeText = activeTab.getCodeText();
		DefaultScriptEngine engine = new DefaultScriptEngine();
//		new StringReader();
//		engine.setReader(new Print);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(out);
		engine.setWriter(writer);
		engine.setErrorWriter(writer);

		try {
			activeTab.getConsole().clear();
			Object execute = engine.execute(codeText);
			activeTab.getConsole().appendText(execute == null ? "" : execute.toString());
			writer.flush();
			activeTab.getConsole().appendText(out.toString());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			String jsonString = ValueUtil.toString(e);
			activeTab.getConsole().appendText(jsonString);

		} finally {
			writer.close();
			out.close();
		}

	}

	@FXML
	public void btnReloadOnAction() {

	}

}
