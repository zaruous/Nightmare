package com.kyj.fx.nightmare.actions.grid;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.function.IntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.comm.ExecutorDemons;
import com.kyj.fx.nightmare.comm.codearea.CodeAreaHelper;

import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class SqlKeywords extends BorderPane {

	private static Logger LOGGER = LoggerFactory.getLogger(SqlKeywords.class);

	// 2016-08-27 정규식 (?i) 추가옵션을 주는경오 대소문자 구분을 무시한다.
	private static final String KEYWORD_PATTERN = "(?i)\\b("
			+ String.join("|", SQLKeywordFactory.getInstance().getKeywords()) + ")\\b";
	private static final String FUNCTION_PATTERN = "(?i)\\b("
			+ String.join("|", SQLKeywordFactory.getInstance().getFunctionKeywords()) + ")\\b";
	private static final String PAREN_PATTERN = "\\(|\\)";
	private static final String BRACE_PATTERN = "\\{|\\}";
	private static final String BRACKET_PATTERN = "\\[|\\]";
	private static final String SEMICOLON_PATTERN = "\\;";
	private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
	// private static final String COMMENT_PATTERN = "//[^\n]*" + "|" +
	// "/\\*(.|\\R)*?\\*/";
	private static final String COMMENT_PATTERN = "(?:/\\*[^;]*?\\*/)|(?:--[^\\n]*)";

	private static final Pattern PATTERN = Pattern.compile("(?<KEYWORD>" + KEYWORD_PATTERN + ")" + "| (?<FUNCTION>"
			+ FUNCTION_PATTERN + ")" + "| (?<PAREN>" + PAREN_PATTERN + ")" + "|(?<BRACE>" + BRACE_PATTERN + ")"
			+ "|(?<BRACKET>" + BRACKET_PATTERN + ")" + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")" + "|(?<STRING>"
			+ STRING_PATTERN + ")" + "|(?<COMMENT>" + COMMENT_PATTERN + ")");

	private CodeArea codeArea;

	private Label lblLineInfo = new Label();

	public CodeArea getCodeArea() {
		return codeArea;
	}

	/**
	 * 인스턴스의 수와 상관없이 무조건 1개의 서비스로 사용. 처음에 생성자에 생성했는데, 스레드수가 너무 많이 늘어남. 사실 이 클래스는
	 * 어플리케이션당 하나만 사용하여 관리해도 상관없다고 판단.
	 * 
	 * @최초생성일 2016. 10. 18.
	 */
	private static final ExecutorService executor = ExecutorDemons.getGargoyleSystemExecutorSerivce();

	public SqlKeywords() {

		codeArea = new CodeArea();
		setCenter(codeArea);
		codeArea.setWrapText(true);
		IntFunction<javafx.scene.Node> intFunction = LineNumberFactory.get(codeArea);
		codeArea.setParagraphGraphicFactory(intFunction);
		new CodeAreaHelper<CodeArea>(codeArea);
		
		codeArea.richChanges().filter(ch -> !ch.getInserted().equals(ch.getRemoved())) // XXX
				.successionEnds(Duration.ofMillis(500)).supplyTask(this::computeHighlightingAsync)
				.awaitLatest(codeArea.richChanges()).filterMap(t -> {
					if (t.isSuccess()) {
						return Optional.of(t.get());
					} else {
						t.getFailure().printStackTrace();
						return Optional.empty();
					}
				}).subscribe(this::applyHighlighting);

		// 선택라인정보 보여주는 기능 추가.
		codeArea.selectionProperty().addListener((oba, oldval, newval) -> {
			int start = newval.getStart();
			int end = newval.getEnd();
			int caretColumn = codeArea.getCaretColumn();

			String format = String.format("line : %d selectionStart : %d selectionEnd : %d column : %d ",
					codeArea.getCurrentParagraph() + 1, start + 1, end + 1, caretColumn + 1);

			lblLineInfo.setText(format);
		});

		codeArea.appendText("");
		lblLineInfo.setPrefHeight(USE_COMPUTED_SIZE);
		this.setBottom(lblLineInfo);
		this.getStylesheets().add(SqlKeywords.class.getResource("sql-keywords.css").toExternalForm());
	}

	private static StyleSpans<Collection<String>> computeHighlighting(String _text) {
		String text = StringUtils.replace(_text, "\"", "'");

		Matcher matcher = PATTERN.matcher(text);
		int lastKwEnd = 0;
		StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
		while (matcher.find(lastKwEnd)) {

			String styleClass = matcher
					.group("KEYWORD") != null
							? "keyword"
							: matcher.group("FUNCTION") != null ? "function"
									: matcher.group("PAREN") != null ? "paren"
											: matcher.group("BRACE") != null ? "brace"
													: matcher.group("BRACKET") != null ? "bracket"
															: matcher.group("SEMICOLON") != null ? "semicolon"
																	: matcher.group("STRING") != null ? "string"
																			: matcher.group("COMMENT") != null
																					? "comment"
																					: null; /*
																							 * never happens
																							 */
			assert styleClass != null;
			spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
			spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
			lastKwEnd = matcher.end();
		}
		spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
		return spansBuilder.create();
	}

	private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
		String text = codeArea.getText();
		Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
			@Override
			protected StyleSpans<Collection<String>> call() throws Exception {
				return computeHighlighting(text);
			}
		};

		executor.execute(task);

		return task;
	}

	private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
		codeArea.setStyleSpans(0, highlighting);
	}

	public void setEditable(boolean editable) {
		codeArea.editableProperty().set(editable);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 31.
	 * @param b
	 */
	public void setWrapText(boolean b) {
		codeArea.setWrapText(b);
	}

	/**
	 * 
	 */
	public void clear() {
		this.codeArea.clear();
	}
}
