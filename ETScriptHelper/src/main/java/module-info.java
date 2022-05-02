module ETHelper {
	
	exports com.kyj.fx.b.ETScriptHelper;
	
	requires javafx.controls;
	requires javafx.base;
	requires javafx.fxml;
	requires javafx.graphics;
	requires org.apache.commons.io;
	requires java.sql;
	requires spring.jdbc;
	requires spring.core;
	requires spring.tx;
	requires spring.beans;
	requires slf4j.api;
	requires org.controlsfx.controls;
	requires dom4j;
	requires org.apache.commons.codec;
	requires velocity.engine.core;
	requires tomcat.jdbc;
	requires com.google.gson;
	requires org.apache.commons.lang3;

	requires java.prefs;
	requires java.scripting;
	requires java.desktop;
	requires org.apache.httpcomponents.httpcore;
	requires org.apache.httpcomponents.httpclient;
	requires org.fxmisc.richtext;
	requires reactfx;
	requires org.apache.poi.ooxml;
	
	
	opens com.kyj.fx.b.ETScriptHelper to javafx.graphics, javafx.fxml;
	opens com.kyj.fx.b.ETScriptHelper.actions.frame to javafx.fxml;
	opens com.kyj.fx.b.ETScriptHelper.actions.ec.ec to javafx.fxml;
	opens com.kyj.fx.b.ETScriptHelper.actions.ec.eq.states to javafx.fxml;
	opens com.kyj.fx.b.ETScriptHelper.actions.ec.ec.scripts to javafx.fxml;
	opens com.kyj.fx.b.ETScriptHelper.actions.ec.ec.rule  to javafx.fxml;
	opens com.kyj.fx.b.ETScriptHelper.actions.ec.ec.group to javafx.fxml;
	opens com.kyj.fx.b.ETScriptHelper.actions.ec.ec.par to javafx.fxml, javafx.base;
	
	exports com.kyj.fx.b.ETScriptHelper.actions.ec.ec.par to javafx.fxml;
	exports com.kyj.fx.b.ETScriptHelper.grid to  spring.beans;
	exports com.kyj.fx.b.ETScriptHelper.actions.ec.ec to spring.beans;
	exports com.kyj.fx.b.ETScriptHelper.actions.ec.ec.events to spring.beans;
}