/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyj.fx.fxloader.FXMLController;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.ui.frame.AbstractCommonsApp;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

/**
 * 
 */
@FXMLController(value = "QuestionView.fxml", isSelfController = true)
public class QuestionComposite extends AbstractCommonsApp implements ICustomSupportView {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuestionComposite.class);
	private ObjectProperty<String> data = new SimpleObjectProperty<>();
//	private IntegerProperty userAnswer = new SimpleIntegerProperty(0);
	private ObjectProperty<QuestionLabel> questionLabel = new SimpleObjectProperty<>();
	
	@FXML
	private Label lblQuestion, lblReason;
	@FXML
	private RadioButton lblQ1, lblQ2, lblQ3, lblQ4, lblQ5;

	public QuestionComposite() {
		
		try {
			FxUtil.loadRoot(QuestionComposite.class, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ObjectMapper mapper = new ObjectMapper();
	RadioButton[] questionItems;

	@FXML
	public void initialize() {
		questionItems = new RadioButton[] { lblQ1, lblQ2, lblQ3, lblQ4, lblQ5 };
		

		this.data.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				if (ValueUtil.isEmpty(newValue))
					return;

				try {
					Question question = mapper.readValue(newValue, Question.class);
					lblQuestion.setText(question.getQuestion());
					List<Answer> questionList = question.getQuestionList();

					for (int i = 0; i < questionList.size(); i++) {
						Answer answer = questionList.get(i);
						boolean correct = answer.isCorrect();
						int userAnswer = questionLabel.getValue().getUserAnswer();
						
						questionItems[i].setText(answer.getText());
						questionItems[i].setUserData(correct);
						questionItems[i].setSelected(userAnswer == i);
						questionItems[i].setOnAction(ev -> {
							RadioButton rb = (RadioButton) ev.getSource();
							if (rb.isSelected()) {
								if (true == (boolean) rb.getUserData()) {
									Object userData = lblReason.getUserData();
									lblReason.setText(userData.toString());
								}
							}
							questionLabel.get().setUserAnswer(questionList.indexOf(answer));
						});
					}
					lblReason.setText("");
					lblReason.setUserData(question.getReason());

//					QuestionComposite.this.updateBounds();
				} catch (JsonProcessingException e) {
					LOGGER.error("error value : {}" , newValue);
					e.printStackTrace();
				}
			}
		});
		
	
	}

	@Override
	public Node getView() {
		return this;
	}

	@Override
	public void setData(String data) {
		this.data.set(data);

	}

	@Override
	public String getData() {
		return this.data.get();
	}

//	public final IntegerProperty userAnswerProperty() {
//		return this.userAnswer;
//	}
//
//	public final int getUserAnswer() {
//		return this.userAnswerProperty().get();
//	}
//
//	public final void setUserAnswer(final int userAnswer) {
//		this.userAnswerProperty().set(userAnswer);
//	}

	public final ObjectProperty<QuestionLabel> questionLabelProperty() {
		return this.questionLabel;
	}
	

	public final QuestionLabel getQuestionLabel() {
		return this.questionLabelProperty().get();
	}
	

	public final void setQuestionLabel(final QuestionLabel questionLabel) {
		this.questionLabelProperty().set(questionLabel);
	}
	

}
