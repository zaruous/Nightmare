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

import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * @author (zaruous@naver.com)
 *
 */
public class SystemDefaultFileTabPaneManager {

	private TabPane tabpane;
	private Map<String, SystemDefaultFileTab> cache = new HashMap<>();

	public SystemDefaultFileTabPaneManager(TabPane tabpane) {
		this.tabpane = tabpane;
	}

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 7. 30.
	 * @param tab
	 */
	public void add(SystemDefaultFileTab tab) {
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
		SystemDefaultFileTab tab = null;
		if (cache.containsKey(f.getAbsolutePath())) {
			tab = cache.get(f.getAbsolutePath());
		} else {
			tab = new SystemDefaultFileTab();
			tab.setFile(f);
			add(tab);
		}
		
		this.tabpane.getSelectionModel().select(tab);
		return tab;
	}

}
