/**
 * 
 */
package com.kyj.fx.nightmare.ui.frame;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.comm.DialogUtil;
import com.kyj.fx.nightmare.comm.ExecutorDemons;
import com.kyj.fx.nightmare.comm.FileUtil;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.ui.notebook.NoteBookItem;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * 
 */
public class NotebookComposite extends AbstractCommonsApp {
	private static final double _150D = 150d;

	private static final Logger LOGGER = LoggerFactory.getLogger(NotebookComposite.class);

	@FXML
	private ListView<NoteBookItem> lvItems;
	@FXML
	private VBox vbResult;
	@FXML
	private TextField txtInput;

	private ObjectProperty<NoteBookItem> current = new SimpleObjectProperty<NoteBookItem>();

	private int historyPos = 0;
	private ArrayList<String> historyCommand = new ArrayList<>();

	public NotebookComposite() {

		FXMLLoader loader = FxUtil.newLaoder();
		loader.setLocation(NotebookComposite.class.getResource("NotebookView.fxml"));
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
		MenuItem miNewItem = new MenuItem("New Item");
		lvItems.setContextMenu(new ContextMenu(miNewItem));
		miNewItem.setOnAction(this::miNewItemOnAction);

		lvItems.setCellFactory(new Callback<ListView<NoteBookItem>, ListCell<NoteBookItem>>() {

			@Override
			public ListCell<NoteBookItem> call(ListView<NoteBookItem> param) {
				return new ListCell<>() {

					@Override
					public void updateIndex(int i) {
						super.updateIndex(i);
						setText(getItem() == null ? "" : getItem().toString());
					}

					@Override
					public void updateSelected(boolean selected) {
						super.updateSelected(selected);

						if (selected) {
							setStyle("-fx-font-weight:bold");
						} else {
							setStyle("");
						}
					}

				};

			}
		});

		lvItems.getSelectionModel().selectedItemProperty().addListener((oba, o, n) -> {
			this.current.set(n);
		});
	}

	@FXML
	public void btnEnterOnAction() {
		NoteBookItem value;
		if (current.get() == null) {
			value = new NoteBookItem("hello");
			current.set(value);
			this.lvItems.getItems().add(value);
			this.lvItems.getSelectionModel().selectFirst();
		} else {
			value = this.lvItems.getSelectionModel().getSelectedItem();

			if (value == null) {
				value = new NoteBookItem("hello");
				current.set(value);
				this.lvItems.getSelectionModel().selectFirst();
			}
		}

		String text = txtInput.getText();
		if (text.isBlank())
			return;

		historyCommand.add(text);
		historyPos = historyCommand.size() - 1;

		if ("clear".equals(text)) {
			vbResult.getChildren().clear();
			txtInput.setText("");
			return;
		}

		NoteBookItem _value = value;
		ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(() -> {

			String eval = loadImportScript().concat("\n").concat(text);
			Object run = _value.run(eval);
			if (run == null) {
				txtInput.setText("");
				return;
			}
			execute(run);
		});

	}

	private void execute(Object run) {
		if (run instanceof String || run instanceof Number) {

			String string = run.toString();
			//2023.12.18 java21에서 웹뷰로드에 문제가 있다.
//			if (string.startsWith("http://") || string.startsWith("https://")) {
//				try {
//					execute(URI.create(string).toURL());
//				} catch (MalformedURLException e) {
//					execute(e);
//				}
//			}
			
			new Button("asdasd").setOnMouseClicked(getOnDragDetected());

			Platform.runLater(() -> {
				ScrollPane n = new ScrollPane(new Label(string));
				n.setFitToWidth(true);
				n.setMinHeight(_150D);
				vbResult.getChildren().add(n);
				txtInput.setText("");
			});
			return;
		} else if (run instanceof Throwable) {
			Platform.runLater(() -> {
				Label content = new Label(ValueUtil.toString((Throwable) run));
				ScrollPane n = new ScrollPane(content);
				n.setMinHeight(_150D);
				vbResult.getChildren().add(n);
				
				txtInput.setText("");
			});
			return;
		} else if (run instanceof Node) {
			Platform.runLater(() -> {
				Node n = (Node) run;
				vbResult.getChildren().add(n);
				
				txtInput.setText("");
			});
			return;
		} else if (run instanceof Parent) {
			Platform.runLater(() -> {
				Parent n = (Parent) run;
				vbResult.getChildren().add(n);
				txtInput.setText("");
			});
			return;
		} else if (run instanceof Pane) {
			Platform.runLater(() -> {
				Pane n = (Pane) run;
				n.setMinHeight(_150D);
				vbResult.getChildren().add(n);
				txtInput.setText("");
			});
			return;
		} 
		//2023.12.18 java21에서 웹뷰로드에 문제가 있다.
//		else if (run instanceof URL) {
//			Platform.runLater(() -> {
//				WebView webView = new WebView();
//				
//				webView.setMinHeight(_150D);
//				vbResult.getChildren().add(webView);
//				webView.getEngine().load(run.toString());
//				txtInput.setText("");
//			});
//			return;
//		}

		Platform.runLater(() -> {
			Label n = new Label(run.toString());
			n.setMinHeight(150d);
			vbResult.getChildren().add(n);
			txtInput.setText("");
		});
	}

	/**
	 * @param ae
	 */
	public void miNewItemOnAction(ActionEvent ae) {
		this.lvItems.getItems().add(new NoteBookItem("hello"));
	}

	@FXML
	public void txtInputOnKeyPressed(KeyEvent ke) {
		if (ke.getCode() == KeyCode.ENTER) {
			btnEnterOnAction();
		} else if (ke.getCode() == KeyCode.UP) {
			System.out.println(historyPos);
			int index = historyPos;
			if (index < 0)
				return;

			String cmd = historyCommand.get(index);
			txtInput.setText(cmd);
			historyPos = index - 1;
		} else if (ke.getCode() == KeyCode.DOWN) {
			System.out.println(historyPos);
			int index = historyPos + 1;
			if (index >= historyCommand.size())
				return;
			System.out.println(index);
			String cmd = historyCommand.get(index);
			txtInput.setText(cmd);
			historyPos = index;
		}
	}

	private String loadImportScript() {
		File file = importScriptFile();
		if (!file.exists()) {
			return "";
		}

		String script = FileUtil.readConversion(file);
		return script;
	}

	private File importScriptFile() {
		String property = System.getProperty("user.dir");
		return new File(property, "groovy/import/import.groovy");
	}

	@FXML
	public void miImportOnAction() {

		TextArea parent = new TextArea(loadImportScript());
		FxUtil.createStageAndShow(parent, stage -> {
			stage.addEventFilter(KeyEvent.KEY_PRESSED, ev -> {
				if (ev.getCode() == KeyCode.S && ev.isControlDown()) {
					String text = parent.getText();
					try {
						FileUtil.writeFile(importScriptFile(), text);
						DialogUtil.showMessageDialog("저장되었습니다.");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		});
	}
}
