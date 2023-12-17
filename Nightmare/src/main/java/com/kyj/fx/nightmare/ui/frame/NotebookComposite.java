/**
 * 
 */
package com.kyj.fx.nightmare.ui.frame;

import java.io.IOException;
import java.util.Collection;

import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.comm.ExecutorDemons;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.ui.notebook.NoteBookItem;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
		NoteBookItem _value = value;
		ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(() -> {
			try {
				Object run = _value.run(text);
				if (run instanceof String || run instanceof Number) {
					Platform.runLater(() -> {
						vbResult.getChildren().add(new Label(run.toString()));
					});
					return;
				}
				Platform.runLater(() -> {
					vbResult.getChildren().add(new Label(run.toString()));
				});
			} catch (ScriptException e) {
				e.printStackTrace();
			}
		});

		txtInput.setText("");

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
}
