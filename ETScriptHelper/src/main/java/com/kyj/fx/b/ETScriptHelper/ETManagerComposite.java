/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper
 *	작성일   : 2021. 11. 22.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.b.ETScriptHelper.comm.ValueUtil;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class ETManagerComposite extends BorderPane {

	private static final Logger LOGGER = LoggerFactory.getLogger(ETManagerComposite.class);

	public ETManagerComposite() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ETScriptComposite.class.getResource("ETManagerView.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}
}
