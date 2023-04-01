/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.deploy
 *	작성일   : 2021. 12. 6.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.actions.deploy;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.b.ETScriptHelper.actions.deploy.DeployItemDVO.StatusCode;
import com.kyj.fx.b.ETScriptHelper.comm.AbstractDVO;
import com.kyj.fx.b.ETScriptHelper.comm.DialogUtil;
import com.kyj.fx.b.ETScriptHelper.comm.ExecutorDemons;
import com.kyj.fx.b.ETScriptHelper.comm.FxUtil;
import com.kyj.fx.b.ETScriptHelper.comm.JsonFormatter;
import com.kyj.fx.b.ETScriptHelper.comm.Message;
import com.kyj.fx.b.ETScriptHelper.comm.ValueUtil;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public abstract class DeployComposite<T extends AbstractDVO> extends BorderPane {

	private static Logger LOGGER = LoggerFactory.getLogger(DeployComposite.class);

	private Map<String, List<T>> items = null;

	@FXML
	private TableView<DeployItemDVO<T>> tvDeploy;

	private TableColumn<DeployItemDVO<T>, String> tcName;
	private TableColumn<DeployItemDVO<T>, Integer> tcCountOfItem;
	private TableColumn<DeployItemDVO<T>, String> tcMessage;
	private TableColumn<DeployItemDVO<T>, DeployItemDVO.StatusCode> tcStatus;

	@FXML
	private ListView<String> lvDetail;

	public DeployComposite(Map<String, List<T>> items) {
		this.items = new TreeMap<>(items);

		FXMLLoader newLaoder = FxUtil.newLaoder();
		newLaoder.setLocation(DeployComposite.class.getResource("DeployView.fxml"));
		newLaoder.setRoot(this);
		newLaoder.setController(this);
		try {
			newLaoder.load();
		} catch (IOException e) {
			LOGGER.error(ValueUtil.toString(e));
			FxUtil.showStatusMessage(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@FXML
	public void initialize() {

		this.tvDeploy.getSelectionModel().setCellSelectionEnabled(true);

		tcName = new TableColumn<DeployItemDVO<T>, String>("Name");
		tcCountOfItem = new TableColumn<DeployItemDVO<T>, Integer>("Count of item");
		tcMessage = new TableColumn<DeployItemDVO<T>, String>("Message");
		tcStatus = new TableColumn<DeployItemDVO<T>, DeployItemDVO.StatusCode>("Status");
		this.tvDeploy.getColumns().addAll(tcName, tcCountOfItem, tcMessage, tcStatus);

		this.tcName.setCellValueFactory(p -> p.getValue().nameProperty());
		this.tcCountOfItem.setCellValueFactory(p -> {
			return new SimpleObjectProperty<>(p.getValue().getItem().size());
		});
		this.tcMessage.setCellValueFactory(p -> p.getValue().messageProperty());
		this.tcStatus.setCellValueFactory(p -> p.getValue().statusCodeProperty());
		this.tcStatus.setCellFactory(new Callback<TableColumn<DeployItemDVO<T>, StatusCode>, TableCell<DeployItemDVO<T>, StatusCode>>() {

			@Override
			public TableCell<DeployItemDVO<T>, StatusCode> call(TableColumn<DeployItemDVO<T>, StatusCode> param) {

				return new TableCell<>() {

					@Override
					protected void updateItem(StatusCode item, boolean empty) {
						super.updateItem(item, empty);

						if (empty) {
							setText("");
						} else {
							String text = "";
							switch (item) {
							case COMPLETE:
								// %DeployComposite_000003=완료
								text = Message.getInstance().getMessage("DeployComposite_000003");
								break;
							case FAILED:
								// %DeployComposite_000005=실패
								text = Message.getInstance().getMessage("DeployComposite_000005");
								break;
							case PROCESSING:
								// %DeployComposite_000002=진행중
								text = Message.getInstance().getMessage("DeployComposite_000002");
								break;
							case STOP:
								// %DeployComposite_000004=중지
								text = Message.getInstance().getMessage("DeployComposite_000004");
								break;
							case WAIT:
								// %DeployComposite_000001=대기
								text = Message.getInstance().getMessage("DeployComposite_000001");
								break;
							case PAUSE:
								// %DeployComposite_000006=일시중지
								text = Message.getInstance().getMessage("DeployComposite_000006");
								break;
							}
							setText(text);
						}
					}

				};
			}
		});
		tcName.setPrefWidth(150d);
		tcMessage.setPrefWidth(250d);

		/*
		 *  DeployComposite_000010=선택 항목 제거
			DeployComposite_000011=완료 항목 제거
		 */
		MenuItem miRemove = new MenuItem(Message.getInstance().getMessage("DeployComposite_000010"));
		MenuItem miRemoveIfComplete = new MenuItem(Message.getInstance().getMessage("DeployComposite_000011"));

		miRemove.setOnAction(this::miRemoveOnAction);
		miRemoveIfComplete.setOnAction(this::miRemoveIfCompleteOnAction);
		this.tvDeploy.setContextMenu(new ContextMenu(miRemove, miRemoveIfComplete));
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 7.
	 * @param e
	 */
	public void miRemoveOnAction(ActionEvent e) {
		ObservableList<DeployItemDVO<T>> selectedItems = this.tvDeploy.getSelectionModel().getSelectedItems();
		this.tvDeploy.getItems().removeAll(selectedItems);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 7.
	 * @param e
	 */
	public void miRemoveIfCompleteOnAction(ActionEvent e) {
		List<DeployItemDVO<T>> dataset = this.tvDeploy.getItems().stream().filter(v -> {
			if (v.getStatusCode() != DeployItemDVO.StatusCode.COMPLETE) {
				return true;
			}
			return false;
		}).collect(Collectors.toList());
		this.tvDeploy.getItems().setAll(dataset);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 7.
	 */
	public void load() {
		List<DeployItemDVO<T>> dataset = items.entrySet().stream().map(v -> {
			var d = new DeployItemDVO<T>();
			d.setName(v.getKey());
			List<T> value = v.getValue();
			d.setItem(value);
			d.setMessage("");
			// %DeployComposite_000001=대기
			d.setStatusCode(DeployItemDVO.StatusCode.WAIT);
			return d;
		}).collect(Collectors.toList());
		this.tvDeploy.getItems().setAll(dataset);
	}

	private AtomicBoolean requestStop = new AtomicBoolean(false);
	private AtomicBoolean requestPause = new AtomicBoolean(false);
	// private AtomicBoolean requestPause = new AtomicBoolean(false);
	private AtomicInteger currentPos = new AtomicInteger(0);

	@FXML
	public final void btnStartOnAction() {
		this.onStart();
		currentPos.set(0);
		requestStop.set(false);
		requestPause.set(false);

		ObservableList<DeployItemDVO<T>> dataset = this.tvDeploy.getItems();
		Service<Void> s = new Service<Void>() {

			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {

					@Override
					protected Void call() throws Exception {

						Iterator<DeployItemDVO<T>> iterator = dataset.iterator();
						int pos = 0;
						while (iterator.hasNext()) {

							DeployItemDVO<T> next = iterator.next();

							if (requestStop.get()) {
								next.setStatusCode(DeployItemDVO.StatusCode.STOP);
								break;
							}

							if (next.getStatusCode() != DeployItemDVO.StatusCode.COMPLETE) {
								next.setStatusCode(DeployItemDVO.StatusCode.PROCESSING);
								String message = onProgress(next.getName(), next.getItem());
								next.setMessage(message);

								boolean flag = fail.get();
								if (flag) {
									next.setStatusCode(DeployItemDVO.StatusCode.FAILED);
								} else {
									next.setStatusCode(DeployItemDVO.StatusCode.COMPLETE);
								}

							}
							pos++;
							currentPos.set(pos);

						}
						LOGGER.debug("End of process.");

						return null;
					}
				};
			}
		};
		s.setExecutor(ExecutorDemons.getGargoyleSystemExecutorSerivce());
		s.start();
		LOGGER.debug("Start");
	}

	@FXML
	public void btnSelectionStartOnAction() {

		this.onStart();
		currentPos.set(0);
		requestStop.set(false);
		requestPause.set(false);

		int selectedIndex = this.tvDeploy.getSelectionModel().getSelectedIndex();
		DeployItemDVO<T> selectedItem = this.tvDeploy.getSelectionModel().getSelectedItem();
		if (selectedItem == null)
		{
			//DeployComposite_000014=선택만 항목이 없습니다.
			DialogUtil.showMessageDialog(Message.getInstance().getMessage("DeployComposite_000014"));
			return;
		}
		
		Service<Void> s = new Service<Void>() {

			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {

					@Override
					protected Void call() throws Exception {
//						int pos = 0;

						DeployItemDVO<T> next = selectedItem;

						if (requestStop.get()) {
							next.setStatusCode(DeployItemDVO.StatusCode.STOP);
							return null;
						}

						if (next.getStatusCode() != DeployItemDVO.StatusCode.COMPLETE) {
							next.setStatusCode(DeployItemDVO.StatusCode.PROCESSING);
							String message = onProgress(next.getName(), next.getItem());
							next.setMessage(message);

							boolean flag = fail.get();
							if (flag) {
								next.setStatusCode(DeployItemDVO.StatusCode.FAILED);
							} else {
								next.setStatusCode(DeployItemDVO.StatusCode.COMPLETE);
							}
						}
//						pos++;
						currentPos.set(selectedIndex);

						LOGGER.debug("End of process.");

						return null;
					}
				};
			}
		};
		s.setExecutor(ExecutorDemons.getGargoyleSystemExecutorSerivce());
		s.start();
		LOGGER.debug("Start");

	}

	@FXML
	public void btnStopOnAction() {
		this.onStop();
		this.requestStop.set(true);
		this.currentPos.set(0);
	}

	@FXML
	public void tvDeployOnMouseClicked(MouseEvent e) {
		if (e.getButton() == MouseButton.PRIMARY) {
			if (e.getClickCount() == 2) {
				if (e.isConsumed())
					return;
				e.consume();

				DeployItemDVO<T> selectedItem = tvDeploy.getSelectionModel().getSelectedItem();
				List<String> collect = selectedItem.getItem().stream().map(v -> {
					return new JsonFormatter().format(ValueUtil.toJSONString(v));
				}).collect(Collectors.toList());
				lvDetail.getItems().setAll(collect);
			}
		}
	}

	@FXML
	public void lvDetailOnMouseClick(MouseEvent e) {
		if (e.getButton() == MouseButton.PRIMARY) {
			if (e.getClickCount() == 2) {
				if (e.isConsumed())
					return;
				e.consume();

				String selectedItem = lvDetail.getSelectionModel().getSelectedItem();
				FxUtil.createStageAndShow(new TextArea(selectedItem), stage -> {
					stage.setWidth(600d);
					stage.setHeight(600d);
					Stage p = (Stage) DeployComposite.this.getScene().getWindow();
					stage.initOwner(p);
				});
			}
		}
	}

	@FXML
	public void btnResetOnAction() {
		this.tvDeploy.getItems().stream().forEach(v -> {
			v.setStatusCode(DeployItemDVO.StatusCode.WAIT);
		});
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 7.
	 * @param name
	 * @param items
	 * @return message
	 */
	public abstract String onProgress(String name, List<T> items);

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 6.
	 */
	public void onComplete(int successCount, int failCount) {
	}

	public void onStart() {
	};

	public void onStop() {
	};

	private BooleanProperty fail = new SimpleBooleanProperty();

	public void setFail() {
		this.fail.set(true);
	}

	public void setPass() {
		this.fail.set(false);
	}

}
