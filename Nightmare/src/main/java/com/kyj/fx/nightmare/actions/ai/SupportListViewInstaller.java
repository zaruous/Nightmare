/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

import java.util.List;
import java.util.Optional;

import com.kyj.fx.nightmare.comm.DialogUtil;
import com.kyj.fx.nightmare.comm.ExecutorDemons;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;
import javafx.util.StringConverter;

/**
 * 
 */
public abstract class SupportListViewInstaller {

	/**
	 * @param lv
	 * @param aiComposite
	 */
	public static void install(ListView<TbmSmPrompts> lv, AiComposite aiComposite) {
		lv.setCellFactory(TextFieldListCell.forListView(new StringConverter<TbmSmPrompts>() {

			@Override
			public String toString(TbmSmPrompts object) {
				return object.getDisplayText();
			}

			@Override
			public TbmSmPrompts fromString(String string) {
				return null;
			}
		}));

		EventHandler<? super MouseEvent> defaultMouseClickEvent = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent ev) {
				if (ev.getButton() == MouseButton.PRIMARY && ev.getClickCount() == 2) {
					if (ev.isConsumed())
						return;
					ev.consume();
					if (aiComposite.isDisabledEnterButton())
						return;

					TbmSmPrompts selectedItem = lv.getSelectionModel().getSelectedItem();
					if (selectedItem == null)
						return;

					Optional<Pair<String, String>> showYesOrNoDialog = DialogUtil.showYesOrNoDialog("유용한 기능",
							selectedItem.getDisplayText() + " 을 실행하시겠습니까?");
					showYesOrNoDialog.ifPresent(v -> {
						if ("Y".equals(v.getValue())) {
							String prompt = selectedItem.getPrompt();
							String text = "오늘의 표현 실행";
							DefaultLabel lblMe = new DefaultLabel(text, new Label(" 나 "));
							lblMe.setTip("me");

							aiComposite.lvResult.getItems().add(lblMe);
							aiComposite.search(prompt, text);
						}
					});

				}
			}
		};

		lv.setOnMouseClicked(defaultMouseClickEvent);

		ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(() -> {
			List<TbmSmPrompts> supports = aiComposite.dao.getSupports();
			Platform.runLater(() -> {
				lv.getItems().addAll(supports);
			});
		});

	}

}
