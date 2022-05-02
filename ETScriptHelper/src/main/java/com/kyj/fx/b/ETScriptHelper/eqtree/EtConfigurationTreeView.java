/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper
 *	작성일   : 2021. 11. 22.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.eqtree;

import java.util.List;
import java.util.stream.Collectors;

import com.kyj.fx.b.ETScriptHelper.comm.FxUtil;
import com.kyj.fx.b.ETScriptHelper.comm.ValueUtil;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * 
 * ET에 대한 설정에 대한 정보를 기록.
 * 
 * @author KYJ (callakrsos@naver.com)
 *
 */

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EtConfigurationTreeView extends TreeView<EtConfigurationTreeDVO> {

	public EtConfigurationTreeView() {
		this.setShowRoot(false);
		FxUtil.installClipboardKeyEvent(this);
	}

	TreeItem<EtConfigurationTreeDVO> originalRoot;

	/**
	 * @return the originalRoot
	 */
	public TreeItem<EtConfigurationTreeDVO> getOriginalRoot() {
		return originalRoot;
	}

	/**
	 * @param originalRoot
	 *            the originalRoot to set
	 */
	public void setOriginalRoot(TreeItem<EtConfigurationTreeDVO> originalRoot) {
		this.originalRoot = originalRoot;
	}

	/**
	 * 텍스트 검색어시 트리 구조를 변경시키는 용도로 사용 <br/>
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 23. 
	 * @param text
	 */
	public void filter(String text) {

		/*
		 * 1. 텍스트가 공백이면 원본값 로드. 2. 텍스트가 존재하면 필터링.
		 */
		if (ValueUtil.isEmpty(text)) {
			this.setRoot(originalRoot);
			return;
		} else {
			// originalRoot = this.getRoot(T);
			ObservableList<TreeItem<EtConfigurationTreeDVO>> children = originalRoot.getChildren();

			List<TreeItem<EtConfigurationTreeDVO>> collect = children.stream().filter(v -> {
				if (v.getValue() == null)
					return false;

				String displayText = v.getValue().getDisplayText();

				if (displayText.toUpperCase().contains(text.toUpperCase())) {
					return true;
				}

				return false;
			}).collect(Collectors.toList());

			EtConfigurationTreeItem value = new EtConfigurationTreeItem();
			value.getChildren().addAll(collect);
			this.setRoot(value);
		}
	}

}
