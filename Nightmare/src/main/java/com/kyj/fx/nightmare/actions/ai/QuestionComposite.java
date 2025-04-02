/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyj.fx.fxloader.FXMLController;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.ui.frame.AbstractCommonsApp;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

/**
 * 사용자 정의 뷰 모델
 */
@FXMLController(value = "QuestionView.fxml", isSelfController = true)
public class QuestionComposite extends AbstractCommonsApp implements ICustomSupportView {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuestionComposite.class);
	private ObjectProperty<String> data = new SimpleObjectProperty<>();
	private IntegerProperty selections = new SimpleIntegerProperty(-1);

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

	public static List<String> extractJsonStrings(String input) {
        List<String> jsonStrings = new ArrayList<>();

        // 정규 표현식으로 JSON 패턴을 매칭합니다.
        String jsonPattern = "\\{(?:\"[^\"]*\"\\s*:\\s*\"[^\"]*\"\\s*,?\\s*)+\\}";
        Pattern pattern = Pattern.compile(jsonPattern);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            jsonStrings.add(matcher.group());
        }

        return jsonStrings;
    }
	
	@FXML
	public void initialize() {
		questionItems = new RadioButton[] { lblQ1, lblQ2, lblQ3, lblQ4, lblQ5 };

		this.data.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				if (ValueUtil.isEmpty(newValue))
					return;

				String fixedText = newValue;
				final String preffix = "```json";
				int st = fixedText.indexOf(preffix);
				if(st > -1)
				{
					int ed = fixedText.indexOf("```", st + 1);
					fixedText = newValue.substring(st + preffix.length(), ed);
				}
				
				if(st == -1)
				{
					List<String> jsonStrings = extractJsonStrings(fixedText);
					if(!jsonStrings.isEmpty())
						fixedText = jsonStrings.get(0);
				}
				
				
				try {
					Question question = mapper.readValue(fixedText, Question.class);
					
					lblQuestion.setText(question.getQuestion());
					List<Answer> questionList = question.getQuestionList();

					for (int i = 0; i < questionList.size(); i++) {
						Answer answer = questionList.get(i);
						boolean correct = answer.isCorrect();
						int userAnswer = selections.get();

						questionItems[i].setText(answer.getText());
						questionItems[i].setUserData(correct);
						questionItems[i].setSelected(userAnswer == i);
						questionItems[i].setOnAction(ev -> {
							RadioButton rb = (RadioButton) ev.getSource();
							if (rb.isSelected()) {
								if (true == (boolean) rb.getUserData()) {
									Object userData = lblReason.getUserData();
									lblReason.setText(userData.toString());
								} else {
									lblReason.setText("");
								}
							}
							selections.set(questionList.indexOf(answer));
						});
					}
					lblReason.setText("");
					lblReason.setUserData(question.getReason());

				} catch (Exception e) {
					LOGGER.error("error value : {}", newValue);
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

	@Override
	public StringProperty dataProperty() {
		return this.dataProperty();
	}

	@Override
	public void showStatusMessage(String message) {
		// TODO Auto-generated method stub
		
	}

}
