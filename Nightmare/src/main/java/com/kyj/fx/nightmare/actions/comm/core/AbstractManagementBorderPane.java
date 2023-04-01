/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.comm.core
 *	작성일   : 2021. 12. 7.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.comm.core;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.actions.deploy.DeployComposite;
import com.kyj.fx.nightmare.comm.AbstractDVO;
import com.kyj.fx.nightmare.comm.DialogUtil;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.Message;
import com.kyj.fx.nightmare.comm.ResourceLoader;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.comm.service.ESig;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public abstract class AbstractManagementBorderPane<T extends AbstractDVO> extends BorderPane implements OnCommitService, OnReload {

	private static Logger LOGGER = LoggerFactory.getLogger(AbstractManagementBorderPane.class);

	public AbstractManagementBorderPane(URL fxml) {

		Platform.runLater(() -> {
			FXMLLoader newLaoder = FxUtil.newLaoder();
			newLaoder.setLocation(fxml);
			newLaoder.setRoot(this);
			newLaoder.setController(this);
			try {
				newLaoder.load();
			} catch (IOException e) {
				LOGGER.error(ValueUtil.toString(e));
				FxUtil.showStatusMessage(ValueUtil.toString(e));
			}
		});

	}

	class DefaultDeployComposite extends DeployComposite<T> {

		public DefaultDeployComposite(Map<String, List<T>> items) {
			super(items);
		}

		/* (non-Javadoc)
		 * @see com.kyj.fx.b.ETScriptHelper.actions.deploy.DeployComposite#onProgress(java.lang.String, java.util.List)
		 */
		@Override
		public String onProgress(String name, List<T> items) {
			// 메세지 리턴.
			return onDeployItem(name, items);
		}
	}

	/**
	 * 배포아이템 그룹 키, 배포아이템 리스트
	 * 
	 * @최초생성일 2021. 12. 7.
	 */
	private Map<String, List<T>> deployItems;

	public Map<String, List<T>> getDeployItems() {
		return deployItems;
	}

	public void setDeployItems(Map<String, List<T>> deployItems) {
		this.deployItems = deployItems;
	}

	private ObjectProperty<DefaultDeployComposite> deployComposite = new SimpleObjectProperty<>();;

	@Override
	public void onCommit() {
		Map<String, List<T>> collect = getDeployItems();

		if (collect == null) {
			// AbstractManagementBorderPane_00001=배포아이템 정보가 없습니다.
			DialogUtil.showMessageDialog(Message.getInstance().getMessage("AbstractManagementBorderPane_00001"));
			return;
		}
		Stage window = (Stage) this.getScene().getWindow();

		DefaultDeployComposite deployComposite = new DefaultDeployComposite(collect);
		this.deployComposite.set(deployComposite);
		deployComposite.load();
		FxUtil.createStageAndShow(deployComposite, stage -> {
			stage.setWidth(600d);
			stage.setHeight(600d);
			stage.initOwner(window);
			stage.initModality(Modality.APPLICATION_MODAL);
			// AbstractManagementBorderPane_00002=배포앱
			stage.setTitle(Message.getInstance().getMessage("AbstractManagementBorderPane_00002"));
			stage.setOnCloseRequest(ev -> {
				// AbstractManagementBorderPane_00004=리로드 요청
				FxUtil.showStatusMessage(Message.getInstance().getMessage("AbstractManagementBorderPane_00004"));
				reload();
				// AbstractManagementBorderPane_00003=데이터가 리로드 되었습니다.
				FxUtil.showStatusMessage(Message.getInstance().getMessage("AbstractManagementBorderPane_00003"));
			});
		});
	}

	/**
	 * 배포 아이템 처리 구현<br/>
	 * 
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 12. 7.
	 * @param name
	 * @param items
	 * @return
	 */
	public abstract String onDeployItem(String name, List<T> items);

	public void setDeployItemFail() {
		this.deployComposite.get().setFail();
	}

	public void setDeployItemPass() {
		this.deployComposite.get().setPass();
	}

	/**
	 * map information ret.put("userName", txUserName.getText()); ret.put("userPwd", txPassword.getText()); ret.put("verifierUserName",
	 * txVerifierUserName.getText()); ret.put("verifierUserPwd", txVerifierPassword.getText());
	 * 
	 * ret.put("permission", permission); ret.put("domain", domain); ret.put("application", application); ret.put("entityType", entityType);
	 * ret.put("entityId", entityId); ret.put("comment", txtComment.getText()); ret.put("userToken", userToken); ret.put("token", token);
	 * ret.put("err", err);
	 * 
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 12. 7.
	 * @param permission
	 * @param domain
	 * @param application
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public Map<String, String> showESigDialog(String permission, String domain, String application, String entityType, String entityId) {
		Optional<Map<String, String>> showESigDialog = DialogUtil.showESigDialog(permission, domain, application, entityType, entityId);
		if (showESigDialog.isPresent()) {
			return showESigDialog.get();
		}
		return null;
	}

	/**
	 * 인증에 대한 정보를 리턴받아 재사용하기 위함 <br/>
	 * map information ret.put("userName", txUserName.getText()); ret.put("userPwd", txPassword.getText()); ret.put("verifierUserName",
	 * txVerifierUserName.getText()); ret.put("verifierUserPwd", txVerifierPassword.getText());
	 * 
	 * ret.put("permission", permission); ret.put("domain", domain); ret.put("application", application); ret.put("entityType", entityType);
	 * ret.put("entityId", entityId); ret.put("comment", txtComment.getText());
	 * 
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 12. 7.
	 * @param permission
	 * @param domain
	 * @param application
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public Map<String, String> showSimpleSigDialog(String permission, String domain, String application, String entityType,
			String entityId) {
		Optional<Map<String, String>> showESigDialog = DialogUtil.showSimpleSigDialog(permission, domain, application, entityType,
				entityId);
		if (showESigDialog.isPresent()) {
			return showESigDialog.get();
		}
		return null;
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 12. 7.
	 * @param login
	 * @param purePwd
	 * @param permission
	 * @param domain
	 * @param application
	 * @param entityType
	 * @param entityId
	 * @param comment
	 * @return
	 * @throws Exception
	 */
	protected final String createTokenEx(Map<String, String> esigInfo) throws Exception {
		return createTokenEx(esigInfo.get("userName"), esigInfo.get("userPwd"), esigInfo.get("permission"), esigInfo.get("domain"),
				esigInfo.get("application"), esigInfo.get("entityType"), esigInfo.get("entityId"), esigInfo.get("comment"));
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 12. 7.
	 * @param login
	 * @param purePwd
	 * @param permission
	 * @param domain
	 * @param application
	 * @param entityType
	 * @param entityId
	 * @param comment
	 * @return
	 * @throws Exception
	 */
	protected final String createTokenEx(String login, String purePwd, String permission, String domain, String application,
			String entityType, String entityId, String comment) throws Exception {
		String rootUrl = ResourceLoader.getInstance().get(ResourceLoader.SYNCADE_ROOT_URL);
		ESig esig = new ESig(rootUrl);
		String pwd = ESig.base64Encoder(purePwd);
		String createToken = esig.createTokenEx(login, pwd, permission, domain, application, entityType, entityId, comment);
		String token = ESig.parser().getToken(createToken.toString());
		return token;
	}

	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 12. 9.
	 * @param ex
	 */
	protected void showErrorMessage(Exception ex) {
		FxUtil.showStatusMessage(ex.getMessage());
		DialogUtil.showExceptionDailog(ex);
	}
}
