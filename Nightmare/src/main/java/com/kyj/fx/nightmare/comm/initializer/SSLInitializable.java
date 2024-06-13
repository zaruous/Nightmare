/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.main.initalize
 *	작성일   : 2016. 2. 5.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.nightmare.comm.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * SSL 통신을 위한 세팅
 *
 * @author KYJ
 *
 */
public class SSLInitializable implements Initializable {

	private static Logger LOGGER = LoggerFactory.getLogger(SSLInitializable.class);

	@Override
	public void initialize() throws Exception {
		GargoyleSSLVertifier gargoyleSSLInit = new GargoyleSSLVertifier();
		gargoyleSSLInit.setup();
	}

}
