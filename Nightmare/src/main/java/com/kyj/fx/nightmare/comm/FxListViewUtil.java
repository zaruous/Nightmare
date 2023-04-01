/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.util
 *	작성일   : 2018. 4. 9.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.nightmare.comm;

import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

/**
 * @author KYJ
 *
 */
public class FxListViewUtil {

	public static <T> void installClipboardKeyEvent(ListView<T> tv) {
		installClipboardKeyEvent(tv, null);
	}
	/********************************
	 * 작성일 : 2016. 9. 3. 작성자 : KYJ
	 *
	 * TableView 키이벤트를 등록
	 *
	 * @param tb
	 ********************************/
	public static <T> void installClipboardKeyEvent(ListView<T> tv, StringConverter<T> converter) {

		tv.addEventHandler(KeyEvent.KEY_RELEASED, ev -> {

			// Copy
			if (KeyCode.C == ev.getCode() && ev.isControlDown() && !ev.isAltDown() && !ev.isShiftDown()) {

				if (ev.isConsumed())
					return;
				ObservableList<T> selectedItems = tv.getSelectionModel().getSelectedItems();
				if (selectedItems != null) {
					Optional<String> reduce = selectedItems.stream().map(v -> {
						if(converter == null)
							return v.toString();
						return converter.toString(v);
					}).reduce((str1, str2) -> {
						return str1.concat("\n").concat(str2);
					});

					reduce.ifPresent(v -> {
						FxClipboardUtil.putString(v);
					});
				}
				ev.consume();
			}

		});
	}

}
