/**
 * 
 */
package com.kyj.fx.nightmare.ui.frame;

import java.io.File;
import java.io.IOException;

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
	private static final Logger LOGGER = LoggerFactory.getLogger(NotebookComposite.class);

	@FXML
	private ListView<NoteBookItem> lvItems;
	@FXML
	private VBox vbResult;
	@FXML
	private TextField txtInput;

	private ObjectProperty<NoteBookItem> current = new SimpleObjectProperty<NoteBookItem>();
//	@FXML
//	private List<NoteBookItem> notebooks = FXCollections.observableArrayList(); 

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

			if (run instanceof String || run instanceof Number) {
				Platform.runLater(() -> {
					ScrollPane e = new ScrollPane(new Label(run.toString()));
					e.setPrefHeight(150d);
					vbResult.getChildren().add(e);
					txtInput.setText("");
				});
				return;
			} else if (run instanceof Throwable) {
				Platform.runLater(() -> {
					Label content = new Label(ValueUtil.toString((Throwable) run));
					ScrollPane e = new ScrollPane(content);
					e.setPrefHeight(150d);
					vbResult.getChildren().add(e);
					txtInput.setText("");
				});
				return;
			} else if (run instanceof Node) {
				Platform.runLater(() -> {
					vbResult.getChildren().add((Node) run);
					txtInput.setText("");
				});
				return;
			} else if (run instanceof Parent) {
				Platform.runLater(() -> {
					vbResult.getChildren().add((Parent) run);
					txtInput.setText("");
				});
				return;
			} else if (run instanceof Pane) {
				Platform.runLater(() -> {
					vbResult.getChildren().add((Pane) run);
					txtInput.setText("");
				});
				return;
			}

			Platform.runLater(() -> {
				vbResult.getChildren().add(new Label(run.toString()));
				txtInput.setText("");
			});

//			new WebView();
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
		if (ke.getCode() == KeyCode.ENTER)
			btnEnterOnAction();
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
		FxUtil.createStageAndShow(parent, stage->{
			stage.addEventFilter(KeyEvent.KEY_PRESSED, ev->{
				if(ev.getCode() == KeyCode.S && ev.isControlDown()) {
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
