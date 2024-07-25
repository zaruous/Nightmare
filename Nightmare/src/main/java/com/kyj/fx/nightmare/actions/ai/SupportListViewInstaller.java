/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import com.kyj.fx.nightmare.comm.DialogUtil;
import com.kyj.fx.nightmare.comm.ExecutorDemons;
import com.kyj.fx.nightmare.comm.ValueUtil;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
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
					showYesOrNoDialog.ifPresent(new Consumer<Pair<String, String>>() {
						@Override
						public void accept(Pair<String, String> v) {
							if ("Y".equals(v.getValue())) {
								String systemMsg = selectedItem.getPrompt();
								String text = selectedItem.getDisplayText();
								String graphicClass = selectedItem.getGraphicClass();
								String promptId = selectedItem.getId();
								Node graphic = new Label(" 나 ");
								if (ValueUtil.isNotEmpty(graphicClass)) {
									try {
										Object newInstance = newInstance(graphicClass);
										if (newInstance instanceof ICustomSupportView) {

											DefaultLabel lblMe = new DefaultLabel(text, graphic);
											lblMe.setTip("me");
											aiComposite.lvResult.getItems().add(lblMe);
											ICustomSupportView supportView = (ICustomSupportView) newInstance;

											
											search(aiComposite, promptId, systemMsg, text, data -> {
												// TODO 동적 타입으로 변경
												CustomLabel ret = new CustomLabel(supportView);
												supportView.setData(data);
												return ret;
											});
											
//											aiComposite.search(promptId, systemMsg, text, data -> {
//												// TODO 동적 타입으로 변경
//												CustomLabel ret = new CustomLabel(supportView);
//												supportView.setData(data);
//												return ret;
//											});
										} 
//										else {
//											DefaultLabel lblMe = new DefaultLabel(text, graphic);
//											lblMe.setTip("me");
//											aiComposite.lvResult.getItems().add(lblMe);
////											aiComposite.search(systemMsg, text);
//											search(aiComposite, systemMsg, text );
//										}

									} catch (Exception e) {
										e.printStackTrace();
									}
								} else {
									DefaultLabel lblMe = new DefaultLabel(text, graphic);
									lblMe.setTip("me");
									aiComposite.lvResult.getItems().add(lblMe);
									aiComposite.search(systemMsg, text);
								}

							}
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
	

	/**
	 * ollama 모델에서 json 포멧으로 처리하기 위한 처리.
	 * @param aiComposite
	 * @param system
	 * @param message
	 * @param customResponseHandler
	 * @throws Exception
	 */
	static void search(AiComposite aiComposite, String promptId , String system, String message, Function<String, DefaultLabel> customResponseHandler) throws Exception {
		ListView<DefaultLabel> lv = aiComposite.lvResult;
		JsonFormatOpenAIService openAIService = new JsonFormatOpenAIService();
		ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(() -> {
			try {
				openAIService.setSystemRole(openAIService.createDefault(system));

				String send = openAIService.send(promptId, message);
				Platform.runLater(() -> {
					lv.getItems().add(new DefaultLabel("", new Label(openAIService.getConfig().getModel())));
					if (customResponseHandler != null) {
						DefaultLabel apply = customResponseHandler.apply(send);
						lv.getItems().add(apply);
					}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}

	public static Object newInstance(String graphicClass) throws ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException, InvocationTargetException {
		Class<?> forName = Class.forName(graphicClass);
		Constructor<?> declaredConstructor = forName.getDeclaredConstructor();
		Object newInstance = declaredConstructor.newInstance();
		return newInstance;
	}
}
