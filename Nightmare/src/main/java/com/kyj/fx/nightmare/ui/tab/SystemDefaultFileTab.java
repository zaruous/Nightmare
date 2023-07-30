/********************************
 *	프로젝트 : Nightmare
 *	패키지   : com.kyj.fx.nightmare.ui.tab
 *	작성일   : 2023. 4. 2.
 *	작성자   : (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.ui.tab;

import java.io.File;

import com.kyj.fx.nightmare.comm.FileUtil;
import com.kyj.fx.nightmare.comm.IdGenUtil;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

/**
 * @author (zaruous@naver.com)
 *
 */
public class SystemDefaultFileTab extends Tab {

	private StringProperty tabId = new SimpleStringProperty(IdGenUtil.generate());
	private final TextArea textArea = new TextArea();
	private ObjectProperty<File> f = new SimpleObjectProperty<File>();

	public SystemDefaultFileTab() {
		setContent(new BorderPane(textArea));
		setClosable(true);

		this.f.addListener(new ChangeListener<File>() {

			@Override
			public void changed(ObservableValue<? extends File> observable, File oldValue, File newValue) {
				if (oldValue == newValue)
					return;
				if (!newValue.exists())
					return;
				if (newValue.isDirectory())
					return;

				setText(newValue.getName());
				textArea.setText(readFile(newValue));
				tabId.set(newValue.getAbsolutePath());
			}
		});
	}

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 4. 2.
	 * @param f
	 */
	public void setFile(File f) {
		this.f.set(f);
	}

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 4. 2.
	 * @return
	 */
	public File getFile() {
		return this.f.get();
	}

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 4. 2.
	 * @param newValue
	 * @return
	 */
	protected String readFile(File newValue) {
		return FileUtil.readConversion(newValue);
	}

	public final StringProperty tabIdProperty() {
		return this.tabId;
	}

	public final String getTabId() {
		return this.tabIdProperty().get();
	}

	public final void setTabId(final String tabId) {
		this.tabIdProperty().set(tabId);
	}

}
