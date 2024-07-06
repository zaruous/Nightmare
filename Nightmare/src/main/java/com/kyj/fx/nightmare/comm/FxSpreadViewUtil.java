/**
 * 
 */
package com.kyj.fx.nightmare.comm;

import java.util.function.BiFunction;

import org.controlsfx.control.spreadsheet.SpreadsheetView;

import com.kyj.fx.nightmare.comm.codearea.SpreadViewSearchComposite;

import javafx.scene.control.TableColumn;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * 
 */
public class FxSpreadViewUtil {

	public static <T> void installFindKeyEvent(Stage owner, SpreadsheetView ssv,
			BiFunction<TableColumn<?, ?>, Object, Object> customConverter) {
		ssv.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
			if (!ev.isAltDown() && !ev.isShiftDown() && ev.isControlDown() && ev.getCode() == KeyCode.F) {
				if (ev.isConsumed())
					return;
				ev.consume();
				SpreadViewSearchComposite composite = new SpreadViewSearchComposite(owner, ssv);
				composite.setCustomConverter(customConverter);
				composite.show();

			}
		});
	
	}

}
