/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.frame
 *	작성일   : 2021. 11. 22.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.comm;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.dom4j.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.comm.service.ESig;
import com.kyj.fx.nightmare.comm.service.XMLUtils;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Pair;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class DialogUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(DialogUtil.class);

	public static Optional<Pair<String, String>> showInputDialog(Node owner, String title, String message) {
		return showInputDialog(owner.getScene().getWindow(), title, message, "", null);
	}
	
	public static Optional<Pair<String, String>> showInputDialog(Node owner, String title, String message, Predicate<String> satisfied) {
		return showInputDialog(FxUtil.getWindow(owner), title, message, "", satisfied);
	}
	
	public static Optional<Pair<String, String>> showInputDialog(Window owner, String title, String message) {
		return showInputDialog(owner, title, message, "", null);
	}
	
	public static Optional<Pair<String, String>> showInputDialog(String title, String message, String inputValue) {
		return showInputDialog(StageStore.getPrimaryStage(), title, message, inputValue, null);
	}
	
	public static Optional<Pair<String, String>> showInputDialog(Window owner, String title, String message, String inputValue,
			Predicate<String> satisfied) {

		BaseDialogComposite composite = new BaseDialogComposite(title, message);
		Button btnOk = new Button("OK");
		btnOk.setMinWidth(80d);
		Button btnCancel = new Button("Cancel");
		btnCancel.setMinWidth(80d);
		composite.addButton(btnOk);
		composite.addButton(btnCancel);

		TextField text = new TextField();
		if (ValueUtil.isNotEmpty(inputValue)) {
			text.setText(inputValue);
		}

		text.setOnAction(ev -> {

			btnOk.fireEvent(mouseEventForDummy());

		});

		composite.setGraphic(text);
		Optional<Pair<String, String>> empty = Optional.empty();
		SimpleObjectProperty<Optional<Pair<String, String>>> prop = new SimpleObjectProperty<>(empty);

		// Modal
		composite.show(owner, stage -> {
			stage.initModality(Modality.APPLICATION_MODAL);
			text.requestFocus();

			text.addEventHandler(KeyEvent.KEY_RELEASED, ev -> {
				if (ev.getCode() == KeyCode.ENTER) {

					Optional<Pair<String, String>> pair = Optional.of(new Pair<>("OK", text.getText()));
					prop.set(pair);

					if (satisfied != null) {

						if (satisfied.test(text.getText())) {
							stage.close();
						}
					}

				} else {

					if (satisfied != null) {
						if (satisfied.test(text.getText())) {
							btnOk.setDisable(false);
						} else
							btnOk.setDisable(true);

					}
				}
			});

			btnOk.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
				Optional<Pair<String, String>> pair = Optional.of(new Pair<>("OK", text.getText()));
				prop.set(pair);
				stage.close();
			});

			btnCancel.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
				stage.close();
			});

		});

		return prop.get();
	}
	public static Optional<Pair<String, String>> showYesOrNoDialog(String title, String message) {
		return showYesOrNoDialog(StageStore.getPrimaryStage(), title, message, null, null);
	}

	public static Optional<Pair<String, String>> showYesOrNoDialog(Stage stage, String title, String message) {

		return showYesOrNoDialog(stage, title, message, null, null);
	}

	public static Optional<Pair<String, String>> showYesOrNoDialog(Stage stage, String title, String message,
			Consumer<Dialog<Pair<String, String>>> dialogHandler) {
		return showYesOrNoDialog(stage, title, message, null, dialogHandler);
	}

	public static Optional<Pair<String, String>> showYesOrNoDialog(Stage stage, String title, String message,
			Consumer<? super Pair<String, String>> consumer, Consumer<Dialog<Pair<String, String>>> dialogHandler) {

		// Create the custom dialog.
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle(title);
		dialog.setHeaderText(message);

		// Set the button types.
		ButtonType yesBtn = new ButtonType("Yes", ButtonData.YES);
		ButtonType noBtn = new ButtonType("No", ButtonData.NO);

		dialog.getDialogPane().getButtonTypes().addAll(yesBtn, noBtn);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == yesBtn) {
				return new Pair<>("RESULT", "Y");
			} else if (dialogButton == noBtn) {
				return new Pair<>("RESULT", "N");
			}
			return null;
		});

		dialog.initOwner(stage);
		if (dialogHandler != null)
			dialogHandler.accept(dialog);

		Optional<Pair<String, String>> result = dialog.showAndWait();

		if (consumer != null)
			result.ifPresent(consumer);

		return result;

	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 11. 22.
	 * @param ownerWindow
	 * @return
	 */
	public static File showDirectoryDialog(final Window ownerWindow) {
		return showDirectoryDialog(ownerWindow, null);
	}

	/**
	 *
	 *
	 * 디렉토리 선택 다이얼로그 오픈
	 *
	 * @Date 2015. 10. 12.
	 * @param ownerWindow
	 * @param option
	 * @return
	 * @User KYJ
	 */
	public static File showDirectoryDialog(final Window ownerWindow, Consumer<DirectoryChooser> option) {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Open Resource Directory");
		installDefaultPath(chooser);
		if (option != null)
			option.accept(chooser);

		File showDialog = chooser.showDialog(ownerWindow);

		applyLastPath(showDialog);
		return showDialog;
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 11. 22.
	 * @param fileChooser
	 */
	public static void installDefaultPath(DirectoryChooser fileChooser) {
		File initDir = getInitDir();
		if (initDir != null) {
			if (initDir.isDirectory()) {
				if (fileChooser != null) {
					fileChooser.setInitialDirectory(initDir);
				}
			} else {
				File parentFile = initDir.getParentFile();
				if (fileChooser != null) {
					fileChooser.setInitialDirectory(parentFile);
				}
			}
		}
	}

	public static File getInitDir() {
		String path = PreferencesUtil.getDefault().get(PreferencesUtil.KEY_LAST_SELECTED_PATH, "");
		if (ValueUtil.isNotEmpty(path)) {
			File file = new File(path);
			if (file.exists()) {
				if (file.isDirectory())
					return file;
				else
					return file.getParentFile();
			}
		}
		return null;
	}

	private static void applyLastPath(File file) {
		if (file != null && file.exists())
			PreferencesUtil.getDefault().put(PreferencesUtil.KEY_LAST_SELECTED_PATH, file.getAbsolutePath());
	}

	/**
	 * info Dialog 메세지 다이얼로그
	 *
	 * @param message
	 */
	public static void showMessageDialog(String message) {
		showMessageDialog(StageStore.getPrimaryStage(), message);
	}

	public static void showMessageDialog(Window initOwner, String title, String message) {
		showMessageDialog((Stage) initOwner, title, "", message, alert -> {
			alert.initModality(Modality.APPLICATION_MODAL);
			alert.initOwner(initOwner);
			alert.showAndWait();
		});
	}

	/**
	 * show info Dialog info Dialog
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 7. 12.
	 * @param initOwner
	 * @param message
	 */
	public static void showMessageDialog(Window initOwner, String message) {

		showMessageDialog((Stage) initOwner, "Info", "", message, alert -> {
			alert.initModality(Modality.APPLICATION_MODAL);
			alert.initOwner(initOwner);
			alert.showAndWait();
		});

	}

	/**
	 * show info Dialog info Dialog
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 7. 12.
	 * @param initOwner
	 * @param message
	 */
	public static void showMessageDialog(Stage initOwner, String message) {
		showMessageDialog(initOwner, "Info", "", message, alert -> {
			alert.initModality(Modality.APPLICATION_MODAL);
			alert.showAndWait();
		});

	}

	/**
	 * show info Dialog
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 7. 12.
	 * @param initOwner
	 * @param title
	 * @param headerText
	 * @param message
	 * @param apply
	 */
	public static void showMessageDialog(Stage initOwner, String title, String headerText, String message, Consumer<Alert> apply) {

		// Platform.runLater(() -> {
		//
		// });

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(message);
		if (initOwner == null)
			alert.initOwner(StageStore.getPrimaryStage());
		else
			alert.initOwner(initOwner);
		apply.accept(alert);

		// Dialog<Pair<String, String>> dialog = new Dialog<>();
		// dialog.setTitle(title);
		// dialog.setHeaderText(headerText);
		// dialog.setContentText(message);
		// dialog.initOwner(initOwner);
		// apply.accept(dialog);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 1. 31.
	 * @return
	 */
	public static Consumer<Exception> exceptionHandler() {
		return err -> {
			DialogUtil.showExceptionDailog(err);
		};
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 11. 29.
	 * @param ex
	 */
	public static void showExceptionDailog(Throwable ex) {
		showExceptionDailog(StageStore.getPrimaryStage(), ex, ex.getLocalizedMessage(),
				"The exception stacktrace was:\n" + ValueUtil.toString(ex));
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 26.
	 * @param owner
	 * @param ex
	 */
	public static void showExceptionDailog(Window owner, Throwable ex) {
		showExceptionDailog(owner, ex, ex.getMessage(), ValueUtil.toString(ex));
	}

	/**
	 * Exception Dialog 예외가 발생햇을때 표시할 다이얼로그
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 8. 22.
	 * @param owner
	 * @param ex
	 * @param message
	 */
	public static void showExceptionDailog(Node owner, Throwable ex, String message) {
		showExceptionDailog(getWindow(owner), ex, ex.getMessage(), message);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 26.
	 * @param owner
	 * @param ex
	 * @param title
	 * @param message
	 */
	public static void showExceptionDailog(Node owner, Throwable ex, String title, String message) {
		Window window = getWindow(owner);
		showExceptionDailog(window, ex, title, message);
	}

	/**
	 * Exception Dialog 예외가 발생햇을때 표시할 다이얼로그
	 *
	 * @param ex
	 */
	public static void showExceptionDailog(Window owner, Throwable ex, String title, String message) {
		/*
		 * 2016-08-23 기존에 사용하던 에러 다이얼로그는 사용되지않음.
		 *
		 * 이유는 팝업에 대한 우선순위 핸들링처리가 불가.
		 *
		 * ex) A팝업이 화면에 떠있는 상태에서 , 또 다른 B 팝업이 뜬 상태에서 에러 다이얼로그가 보여지는경우
		 *
		 * 연관성이 없는 A팝업이 화면 최상단으로 올라오는 버그가 있음. by kyj.
		 *
		 *
		 */
		// Alert alert = new Alert(AlertType.ERROR);
		// alert.setTitle("Exception Dialog");
		//
		// alert.setHeaderText(message);
		// alert.setContentText(ex.getMessage());
		//
		// // Create expandable Exception.
		// StringWriter sw = new StringWriter();
		// PrintWriter pw = new PrintWriter(sw);
		// ex.printStackTrace(pw);
		// String exceptionText = sw.toString();
		//
		// Label label = new Label(message);
		//
		// TextArea textArea = new TextArea(exceptionText);
		// textArea.setEditable(false);
		// textArea.setWrapText(true);
		//
		// textArea.setMaxWidth(Double.MAX_VALUE);
		// textArea.setMaxHeight(Double.MAX_VALUE);
		// GridPane.setVgrow(textArea, Priority.ALWAYS);
		// GridPane.setHgrow(textArea, Priority.ALWAYS);
		//
		// GridPane expContent = new GridPane();
		// expContent.setMaxWidth(Double.MAX_VALUE);
		// expContent.add(label, 0, 0);
		// expContent.add(textArea, 0, 1);
		//
		// alert.getDialogPane().setExpandableContent(expContent);
		// alert.initOwner(owner);
		// alert.showAndWait();

		Platform.runLater(() -> {
			new ExceptionDialogComposite(ex, title, message).show(owner);
		});

	}

	private static Window getWindow(Node owner) {
		Window _owner = null;
		if (owner != null) {
			Scene scene = owner.getScene();
			if (scene != null) {
				_owner = scene.getWindow();
			}
		}

		if (_owner == null) {
			_owner = StageStore.getPrimaryStage();
		}
		return _owner;
	}

	/**
	 * @param option
	 * @return
	 */
	public static File showFileSaveDialog(Consumer<FileChooser> option) {
		return showFileSaveDialog(StageStore.getPrimaryStage(), option);
	}
	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 4. 22.
	 * @param ownerWindow
	 * @param option
	 * @return
	 */
	public static File showFileSaveDialog(final Window ownerWindow, Consumer<FileChooser> option) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");

		installDefaultPath(fileChooser);

		option.accept(fileChooser);

		File result = fileChooser.showSaveDialog(ownerWindow);
		if (result != null)
			applyLastPath(result.getParentFile());

		return result;
	}

	/********************************
	 * 작성일 : 2016. 6. 19. 작성자 : KYJ
	 *
	 *
	 * 최근 설정했는 경로로 세팅.
	 *
	 *
	 * @param fileChooser
	 ********************************/
	public static void installDefaultPath(FileChooser fileChooser) {
		File initDir = getInitDir();
		if (initDir != null) {
			if (initDir.isDirectory()) {
				if (fileChooser != null) {
					fileChooser.setInitialDirectory(initDir);
				}
			} else {
				File parentFile = initDir.getParentFile();
				if (fileChooser != null) {
					fileChooser.setInitialDirectory(parentFile);
				}
			}
		}

	}

	/**
	 * @param option
	 * @return
	 */
	public static File showFileDialog(Consumer<FileChooser> option) {
		return showFileDialog(StageStore.getPrimaryStage(),option);
	}
	
	public static File showFileDialog(final Window ownerWindow) {
		return showFileDialog(ownerWindow, option -> {
		});
	}

	/**
	 * 파일다이얼로그 오픈
	 *
	 * @Date 2015. 10. 12.
	 * @param ownerWindow
	 * @param option
	 * @return
	 * @User KYJ
	 */
	public static File showFileDialog(final Window ownerWindow, Consumer<FileChooser> option) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");

		installDefaultPath(fileChooser);
		option.accept(fileChooser);

		File file = fileChooser.showOpenDialog(ownerWindow);
		applyLastPath(file);
		return file;
	}


	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 12. 7. 
	 * @param permission
	 * @param domain
	 * @param application
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public static Optional<Map<String, String>> showESigDialog(String permission, String domain, String application, String entityType,
			String entityId) {
		return showESigDialog(permission, domain, application, entityType, entityId, null);
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 12. 7. 
	 * @param permission
	 * @param domain
	 * @param application
	 * @param entityType
	 * @param entityId
	 * @param callback
	 * @return
	 */
	public static Optional<Map<String, String>> showESigDialog(String permission, String domain, String application, String entityType,
			String entityId, Callback<Map<String, String>, Void> callback) {
		// 저장된 변수가 있는 경우 로딩
		String userName = PreferencesUtil.getDefault().get("gargoyle.rax.simulate.username", "");
		String userPwd = PreferencesUtil.getDefault().get("gargoyle.rax.simulate.userpwd", "");
		Pair<String, String> idPass = new Pair<String, String>(userName, userPwd);
		SaLoginDialog loginDialog = new SaLoginDialog(permission, domain, application, entityType, entityId, idPass, callback);
		loginDialog.initOwner(StageStore.getPrimaryStage());
		return loginDialog.showAndWait();
	}
	
	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 11. 30.
	 * @param permission
	 * @param domain
	 * @param application
	 * @param entityType
	 * @param entityId
	 * @param callback
	 * @return
	 */
	public static Optional<Map<String, String>> showVerifierESigDialog(String permission, String domain, String application,
			String entityType, String entityId, Callback<Map<String, String>, Void> callback) {
		String userName = PreferencesUtil.getDefault().get("gargoyle.rax.simulate.username", "");
		String userPwd = PreferencesUtil.getDefault().get("gargoyle.rax.simulate.userpwd", "");
		Pair<String, String> idPass = new Pair<String, String>(userName, userPwd);
		
		Pair<String, String> verifier = new Pair<String, String>("", "");
		SaLoginDialog loginDialog = new SaLoginDialog(permission, domain, application, entityType, entityId, idPass, verifier, callback);
		return loginDialog.showAndWait();
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 12. 6.
	 * @param permission
	 * @param domain
	 * @param application
	 * @param entityType
	 * @param entityId
	 * @param verifier
	 * @param callback
	 * @return
	 */
	public static Optional<Map<String, String>> showVerifierESigDialog(String permission, String domain, String application,
			String entityType, String entityId, Pair<String, String> verifier, Callback<Map<String, String>, Void> callback) {
		// 저장된 변수가 있는 경우 로딩
		String userName = PreferencesUtil.getDefault().get("gargoyle.rax.simulate.username", "");
		String userPwd = PreferencesUtil.getDefault().get("gargoyle.rax.simulate.userpwd", "");

		Pair<String, String> idPass = new Pair<String, String>(userName, userPwd);
		SaLoginDialog loginDialog = new SaLoginDialog(permission, domain, application, entityType, entityId, idPass, verifier, callback);
		return loginDialog.showAndWait();
	}

	
	/**
	 * 인증정보를 임시적으로 저장하기 위한 용도로 사용 <br/>
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 12. 7. 
	 * @param permission
	 * @param domain
	 * @param application
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public static Optional<Map<String, String>> showSimpleSigDialog(String permission, String domain, String application, String entityType,
			String entityId) {
		// 저장된 변수가 있는 경우 로딩
		String userName = PreferencesUtil.getDefault().get("gargoyle.rax.simulate.username", "");
		String userPwd = PreferencesUtil.getDefault().get("gargoyle.rax.simulate.userpwd", "");
		Pair<String, String> idPass = new Pair<String, String>(userName, userPwd);
		SimpleLoginDialog loginDialog = new SimpleLoginDialog(permission, domain, application, entityType, entityId, idPass, null);
		loginDialog.initOwner(StageStore.getPrimaryStage());
		return loginDialog.showAndWait();
	}
	
	static class SaLoginDialog extends Dialog<Map<String, String>> {

		private final ButtonType loginButtonType;
		private final TextField txUserName;
		private final PasswordField txPassword;
		private final TextField txtComment;

		private final TextField txVerifierUserName;
		private final PasswordField txVerifierPassword;

		/**
		 * @작성자 : KYJ (zaruous@naver.com)
		 * @작성일 : 2021. 11. 30.
		 * @return
		 */
		public Function<Map<String, String>, String> getTokenProcessor() {
			return tokenProcessor;
		}

		/**
		 * @작성자 : KYJ (zaruous@naver.com)
		 * @작성일 : 2021. 11. 30.
		 * @param tokenProcessor
		 */
		public void setTokenProcessor(Function<Map<String, String>, String> tokenProcessor) {
			this.tokenProcessor = tokenProcessor;
		}

		/*
		 * String permission, String domain, String application, String
		 * entityType, String entityId, String comment
		 */
		public SaLoginDialog(String permission, String domain, String application, String entityType, String entityId,
				final Pair<String, String> initialUserInfo, final Callback<Map<String, String>, Void> authenticator) {
			this(permission, domain, application, entityType, entityId, initialUserInfo, null, authenticator);
		}

		private Function<Map<String, String>, String> tokenProcessor = new Function<>() {

			@Override
			public String apply(Map<String, String> m) {

				String createToken = "";
				try {
					String _password = ESig.base64Encoder(txPassword.getText());
					String _verifierPwd = ESig.base64Encoder(txVerifierPassword.getText());

					String userName = m.get("userName");
					String txVerifierUserName = m.get("verifierUserName");
					String permission = m.get("permission");
					String domain = m.get("domain");
					String application = m.get("application");
					String entityType = m.get("entityType");
					String entityId = m.get("entityId");
					String comment = m.get("comment");

					var esig = new ESig(ResourceLoader.getInstance().get(ResourceLoader.SYNCADE_ROOT_URL));
					createToken = esig.createTokenEx(userName, _password, txVerifierUserName, _verifierPwd, permission, domain, application,
							entityType, entityId, comment);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}

				return createToken;
			}
		};

		/**
		 * @param permission
		 * @param domain
		 * @param application
		 * @param entityType
		 * @param entityId
		 * @param initialUserInfo
		 * @param verifierUserInfo
		 * @param authenticator
		 */
		public SaLoginDialog(String permission, String domain, String application, String entityType, String entityId,
				final Pair<String, String> initialUserInfo, final Pair<String, String> verifierUserInfo,
				final Callback<Map<String, String>, Void> authenticator) {
			final DialogPane dialogPane = getDialogPane();

			setTitle("Login"); //$NON-NLS-1$
			dialogPane.setHeaderText("Login"); //$NON-NLS-1$
			// dialogPane.getStyleClass().add("login-dialog"); //$NON-NLS-1$
			// dialogPane.getStylesheets().add(LoginDialog.class.getResource("dialogs.css").toExternalForm());

			dialogPane.getButtonTypes().addAll(ButtonType.CANCEL);

			HBox h1 = new HBox(new Label("Permission:"), new Label(permission));
			HBox h2 = new HBox(new Label("domain:"), new Label(domain));
			HBox h3 = new HBox(new Label("application:"), new Label(application));
			HBox h4 = new HBox(new Label("entityType:"), new Label(entityType));
			HBox h5 = new HBox(new Label("entityId:"), new Label(entityId));

			txUserName = new TextField();
			txPassword = new PasswordField();

			txVerifierUserName = new TextField();
			txVerifierPassword = new PasswordField();

			// txVerifierUserName.setVisible(false);
			// txVerifierPassword.setVisible(false);

			Label lbMessage = new Label("");
			lbMessage.setVisible(false);
			lbMessage.setManaged(false);

			final VBox content = new VBox(10);

			content.getChildren().add(h1);
			content.getChildren().add(h2);
			content.getChildren().add(h3);
			content.getChildren().add(h4);
			content.getChildren().add(h5);

			content.getChildren().add(lbMessage);
			Label lblUser = new Label("User"); lblUser.prefWidth(120d);
			Label lblUsePwd = new Label("User Pwd");lblUsePwd.prefWidth(120d);
			
			content.getChildren().add(new HBox(lblUser, txUserName));
			content.getChildren().add(new HBox(lblUsePwd,txPassword));

			// Label label = new Label("Verifier Id:");
			// Label label2 = new Label("Verifier Pwd:");
			// label.setPrefWidth(120d);
			// label2.setPrefWidth(120d);
			Label lblVerifer = new Label("Verifier");lblVerifer.prefWidth(120d);
			Label lblVerifierPwd = new Label("Verifier Pwd");lblVerifierPwd.prefWidth(120d);
			
			HBox v1 = new HBox(lblVerifer, txVerifierUserName);
			
			HBox v2 = new HBox(lblVerifierPwd, txVerifierPassword);

			if (verifierUserInfo == null) {
				v1.setVisible(false);
				v2.setVisible(false);
			}
			content.getChildren().add(v1);
			content.getChildren().add(v2);

			txtComment = new TextField();
			content.getChildren().add(new Label("Comment"));
			content.getChildren().add(txtComment);

			dialogPane.setContent(content);

			loginButtonType = new javafx.scene.control.ButtonType("OK", ButtonData.OK_DONE); //$NON-NLS-1$
			dialogPane.getButtonTypes().addAll(loginButtonType);

			Button btnOk = (Button) dialogPane.lookupButton(loginButtonType);

			btnOk.addEventFilter(ActionEvent.ACTION, ev -> {

				if (ValueUtil.isEmpty(txUserName.getText())) {
					ev.consume();
					return;
				}
				if (ValueUtil.isEmpty(txPassword.getText())) {
					ev.consume();
					return;
				}

				if (v1.isVisible() && ValueUtil.isEmpty(txVerifierUserName.getText())) {
					ev.consume();
					return;
				}
				if (v2.isVisible() && ValueUtil.isEmpty(txVerifierPassword.getText())) {
					ev.consume();
					return;
				}

			});
			btnOk.setOnAction(actionEvent -> {

				try {

					Map<String, String> ret = new HashMap<String, String>();
					ret.put("userName", txUserName.getText());
					ret.put("userPwd", txPassword.getText());
					ret.put("verifierUserName", txVerifierUserName.getText());
					ret.put("verifierUserPwd", txVerifierPassword.getText());

					ret.put("permission", permission);
					ret.put("domain", domain);
					ret.put("application", application);
					ret.put("entityType", entityType);
					ret.put("entityId", entityId);
					ret.put("comment", txtComment.getText());

					try {

						Document doc = XMLUtils.load(getTokenProcessor().apply(ret));

						String token = doc.selectSingleNode("//Token") == null ? "" : doc.selectSingleNode("//Token").getText();
						String userToken = doc.selectSingleNode("//UserToken") == null ? "" : doc.selectSingleNode("//UserToken").getText();
						String err = doc.selectSingleNode("//Error/Description") == null ? ""
								: doc.selectSingleNode("//Error/Description").getText();
						ret.put("userToken", userToken);
						ret.put("token", token);
						ret.put("err", err);

					} catch (Exception ex) {
						ret.put("token", "");
						ret.put("userToken", "");
						ret.put("err", ex.getMessage());
						LOGGER.error(ValueUtil.toString(ex));
					}
					if (authenticator != null)
						authenticator.call(ret);

					setResult(ret);
					lbMessage.setVisible(false);
					lbMessage.setManaged(false);
					hide();
					// dlg.setResult(this);
				} catch (Throwable ex) {
					lbMessage.setVisible(true);
					lbMessage.setManaged(true);
					lbMessage.setText(ex.getMessage());
					setResult(Collections.emptyMap());
					LOGGER.error(ValueUtil.toString(ex));
				}
			});

			String userNameCation = "UserName";
			String passwordCaption = "UserPasswd";
			String verifierCaption = "Verifier";
			String verifierPwdCaption = "VerifierPasswd";
			txUserName.setPromptText(userNameCation);
			txUserName.setText(initialUserInfo == null ? "" : initialUserInfo.getKey()); //$NON-NLS-1$
			txPassword.setPromptText(passwordCaption);
			txPassword.setText(new String(initialUserInfo == null ? "" : initialUserInfo.getValue())); //$NON-NLS-1$

			txVerifierUserName.setPromptText(verifierCaption);
			txVerifierPassword.setText(new String(verifierUserInfo == null ? "" : verifierUserInfo.getKey()));
			txVerifierPassword.setPromptText(verifierPwdCaption);
			txVerifierPassword.setText(new String(verifierUserInfo == null ? "" : verifierUserInfo.getValue()));

			ValidationSupport validationSupport = new ValidationSupport();
			Platform.runLater(() -> {
				String requiredFormat = "'%s' is required"; //$NON-NLS-1$

				validationSupport.registerValidator(txUserName,
						Validator.createEmptyValidator(String.format(requiredFormat, userNameCation)));
				validationSupport.registerValidator(txPassword,
						Validator.createEmptyValidator(String.format(requiredFormat, passwordCaption)));
				validationSupport.registerValidator(txVerifierUserName,
						Validator.createEmptyValidator(String.format(requiredFormat, verifierCaption)));
				validationSupport.registerValidator(txVerifierPassword,
						Validator.createEmptyValidator(String.format(requiredFormat, verifierPwdCaption)));

				// loginButton.disabledProperty().bind(validationSupport.invalidProperty());
				txUserName.requestFocus();
			});
			// setResult(Collections.emptyMap());
			setResultConverter(dialogButton -> Collections.emptyMap());

		}

		/**************************************************************************
		 * 
		 * Support classes
		 * 
		 **************************************************************************/

	}

	static class SimpleLoginDialog extends SaLoginDialog {

		public SimpleLoginDialog(String permission, String domain, String application, String entityType, String entityId,
				Pair<String, String> initialUserInfo, Callback<Map<String, String>, Void> authenticator) {
			super(permission, domain, application, entityType, entityId, initialUserInfo, authenticator);
			setTokenProcessor(a -> "<SimpleLoginDialog/>");
		}

	}
	
	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 9. 18.
	 * @return
	 */
	public static MouseEvent mouseEventForDummy() {
		return mouseEventForDummy(1);
	}

	public static MouseEvent mouseEventForDummy(int clickCount) {
		return mouseEventForDummy(MouseButton.PRIMARY, clickCount);
	}

	public static MouseEvent mouseEventForDummy(MouseButton button, int clickCount) {
		return new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, button, clickCount, false, false, false, false, false, false, false,
				false, false, false, null);
	}
}
