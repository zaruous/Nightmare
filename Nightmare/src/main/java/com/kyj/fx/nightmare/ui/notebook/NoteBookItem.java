/**
 * 
 */
package com.kyj.fx.nightmare.ui.notebook;

import javax.script.ScriptException;

import com.kyj.fx.nightmare.comm.engine.GroovyScriptEngine;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 
 */
public class NoteBookItem {

	private StringProperty title = new SimpleStringProperty();
	private GroovyScriptEngine groovyScriptEngine;

	public NoteBookItem(String title) {
		this.setTitle(title);
		groovyScriptEngine = new GroovyScriptEngine(false);
	}
	
	/**
	 * @param script
	 * @return 
	 * @throws ScriptException
	 */
	public Object run(String script) throws ScriptException {
		return groovyScriptEngine.eval(script);
	}
	
	
	
	@Override
	public String toString() {
		return this.title.get();
	}

	public final StringProperty titleProperty() {
		return this.title;
	}

	public final String getTitle() {
		return this.titleProperty().get();
	}

	public final void setTitle(final String title) {
		this.titleProperty().set(title);
	}

}
