package com.kyj.fx.nightmare.comm.codearea;

import org.fxmisc.richtext.CodeArea;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Menu;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 *
 * 코드 처리 관련 Helper 클래스 <br/>
 * 
 * @author KYJ
 *
 */
public class CodeAreaHelper<T extends CodeArea> {

	// private static Logger LOGGER =
	// LoggerFactory.getLogger(CodeAreaHelper.class);

	public static final String CHARACTERS_MATCH = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";

	protected T codeArea;
	protected CodeAreaFindAndReplaceHelper<T> findAndReplaceHelper;
	private CodeAreaFileDragDropHelper<T> dragDropHelper;
//	protected MenuItem menuMoveToLine;
//	protected MenuItem miToUppercase;
//	protected MenuItem miToLowercase;

	// 선택 범위 지정
	protected EventHandler<? super MouseEvent> defaultSelectionHandler;

	private ObjectProperty<ContextMenu> menu = new SimpleObjectProperty<ContextMenu>();

	public CodeAreaHelper(T codeArea) {
		this.codeArea = codeArea;
		createContextMenu();
		init();
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 9.
	 */
	protected void createContextMenu() {
		menu.set(new ContextMenu());
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 5.
	 */
	protected void init() {
		this.dragDropHelper = new CodeAreaFileDragDropHelper<>(codeArea);
		this.findAndReplaceHelper = new CodeAreaFindAndReplaceHelper<>(codeArea);

		createMenus();
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 2. 9.
	 * @return
	 */
	public ContextMenu getContextMenu() {
		return this.getMenu();
	}

	public CodeArea getCodeArea() {
		return this.codeArea;
	}

	/**
	 *
	 * 2016-10-27 키 이벤트를 setAccelerator를 사용하지않고 이벤트 방식으로 변경 이유 : 도킹기능을 적용하하면
	 * setAccelerator에 등록된 이벤트가 호출안됨
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 27.
	 */
	public void createMenus() {
		Menu menuSearch = findAndReplaceHelper.createMenus();

		this.menu.get().getItems().addAll(menuSearch);

		this.codeArea.setOnContextMenuRequested(ev -> {
			ContextMenu cm = getContextMenu();
			if (cm.isShowing())
				return;
			double x = ev.getScreenX();
			double y = ev.getScreenY();
			getContextMenu().show(codeArea, x, y);
		});

	}

	public String getText() {
		return codeArea.getText();
	}

	/**
	 * 키클릭 이벤트 처리
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 12. 14.
	 * @param e
	 */
	public void codeAreaKeyClick(KeyEvent e) {

		// CTRL + F
		if (KeyCode.F == e.getCode() && e.isControlDown() && !e.isShiftDown() && !e.isAltDown()) {
			if (!e.isConsumed()) {
				findAndReplaceHelper.findReplaceEvent(new ActionEvent());
				e.consume();
			}
		}
		//////////////////////////////////////////////////////////////////////////////////////
		else {

		}

	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 11. 28.
	 * @return
	 */
	public IndexRange getSelection() {
		return codeArea.getSelection();
	}

	public final ObjectProperty<ContextMenu> menuProperty() {
		return this.menu;
	}

	public final ContextMenu getMenu() {
		return this.menuProperty().get();
	}

	public final void setMenu(final ContextMenu menu) {
		this.menuProperty().set(menu);
	}

	public void setContextMenu(ContextMenu menu) {
		this.setMenu(menu);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 13.
	 * @return
	 */
	public ObservableValue<String> contentProperty() {
		return codeArea.textProperty();
	}

}
