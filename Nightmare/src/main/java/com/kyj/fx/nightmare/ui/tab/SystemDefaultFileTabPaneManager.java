/********************************
 *	프로젝트 : Nightmare
 *	패키지   : com.kyj.fx.nightmare.ui.tab
 *	작성일   : 2023. 4. 2.
 *	작성자   : (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.ui.tab;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * @author (zaruous@naver.com)
 *
 */
/**
 * @param <T>
 */
public abstract class SystemDefaultFileTabPaneManager<T extends SystemDefaultFileTab> {

	private TabPane tabpane;
	private Map<String, T> cache = new HashMap<>();

	public SystemDefaultFileTabPaneManager(TabPane tabpane) {
		this.tabpane = tabpane;
	}

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 7. 30.
	 * @param tab
	 */
	public void add(T tab) {
		this.cache.put(tab.getTabId(), tab);
		this.tabpane.getTabs().add(tab);
	}

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 7. 30.
	 * @param f
	 * @return
	 */
	public Tab add(File f) {
		T tab = null;
		if (cache.containsKey(f.getAbsolutePath())) {
			tab = cache.get(f.getAbsolutePath());
		} else {
			tab = createNewTab(f);
			add(tab);
		}

		this.tabpane.getSelectionModel().select(tab);
		return tab;
	}

	/**
	 * @param f
	 * @return
	 */
	public abstract T createNewTab(File f);

	/**
	 * @return
	 */
	public T getActiveTab() {
		T selectedItem = (T) this.tabpane.getSelectionModel().getSelectedItem();
		return selectedItem;

	}

}
