/**
 * 
 */
package com.kyj.fx.nightmare.ui.frame;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.ValueUtil;

import javafx.fxml.FXMLLoader;

/**
 * 
 */
public class NotebookComposite extends AbstractCommonsApp {
	private static final Logger LOGGER = LoggerFactory.getLogger(NotebookComposite.class);

	public NotebookComposite() {
		super();

		FXMLLoader loader = FxUtil.newLaoder();
		loader.setLocation(GroovyFrameComposite.class.getResource("NotebookView.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			LOGGER.error(ValueUtil.toString(e));
		}

	}

}
