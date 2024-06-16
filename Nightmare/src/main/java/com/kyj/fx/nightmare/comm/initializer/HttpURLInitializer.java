/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.main.initalize
 *	작성일   : 2018. 4. 14.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.nightmare.comm.initializer;

import java.net.HttpURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author KYJ
 *
 */

public class HttpURLInitializer implements Initializable {

	private static Logger LOGGER = LoggerFactory.getLogger(HttpURLInitializer.class);

	@Override
	public void initialize() throws Exception {
		LOGGER.debug(getClass().getName() + "  initialize.");

		HttpURLConnection.setFollowRedirects(true);

//		HttpURLConnection.setContentHandlerFactory(new ContentHandlerFactory() {
//
//			@Override
//			public ContentHandler createContentHandler(String mimetype) {
//				LOGGER.debug("mimetype : {} ", mimetype);
//				return null;
//			}
//		});
	}

}
