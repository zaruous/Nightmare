/**
 * 
 */
package com.kyj.fx.nightmare.ui.notebook;

import java.util.List;

import javax.script.ScriptException;

import com.kyj.fx.nightmare.comm.engine.GroovyScriptEngine;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.VBox;

/**
 * 
 */
public class NoteBookItem {

	private VBox vbResult;
	private StringProperty title = new SimpleStringProperty();
	private GroovyScriptEngine groovyScriptEngine;

	public NoteBookItem(String title) {
		vbResult = new VBox();
		this.setTitle(title);
		groovyScriptEngine = new GroovyScriptEngine(false);
		List<String> scripts = groovyScriptEngine.loadScripts();
		scripts.forEach(script ->{
			try {
				groovyScriptEngine.eval(script);
				System.out.println(script);
			} catch (ScriptException e) {
				e.printStackTrace();
			}
		});
		
	}
	
	/**
	 * @param script
	 * @return 
	 * @throws ScriptException
	 */
	public Object run(String script) {
		try {
			return groovyScriptEngine.eval(script);
		} catch (ScriptException e) {
			return e;
		}
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

	public VBox getVbResult() {
		return vbResult;
	}

	public void setVbResult(VBox vbResult) {
		this.vbResult = vbResult;
	}

}
