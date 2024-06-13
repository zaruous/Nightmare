/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.main.initalize
 *	작성일   : 2017. 5. 24.
 *	프로젝트 : OPERA 
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.nightmare.comm.initializer;

/**
 * @author KYJ
 *
 */

public class HostNameVertifierInitializer implements Initializable {

	@Override
	public void initialize() throws Exception {

		GargoyleHostNameVertifier defaultVertifier = GargoyleHostNameVertifier.defaultVertifier();
		defaultVertifier.setup();
	}

}
