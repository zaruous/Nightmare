open module ETHelper {

	requires transitive spring.jdbc;
	requires spring.core;
	requires transitive spring.tx;
	requires spring.beans;

	requires javafx.controls;
	requires javafx.base;
	requires javafx.swing;
	// The type FXMLLoader from module javafx.fxml may not be accessible to
	// clients due to missing 'requires transitive'

	requires transitive javafx.fxml;
	// The type Stage from module javafx.graphics may not be accessible to
	// clients due to missing 'requires transitive'
	requires transitive javafx.graphics;
	requires org.apache.commons.io;
	requires java.sql;

	//requires slf4j.api;
	requires org.slf4j;
	
	requires logback.classic;

	requires org.controlsfx.controls;
	requires transitive dom4j;
//	requires jaxen;
	requires org.apache.commons.codec;
	requires velocity.engine.core;

	requires com.google.gson;
	requires org.apache.commons.lang3;

	requires java.prefs;
	requires java.scripting;
	requires java.desktop;
	requires org.apache.httpcomponents.httpcore;
	requires org.apache.httpcomponents.httpclient;
	requires org.fxmisc.richtext;
	requires SparseBitSet;

	// requires reactfx;

	requires org.apache.poi.ooxml;
	// requires com.microsoft.sqlserver.jdbc;
	requires java.management;
	requires java.xml.bind;
	requires java.xml;
	requires com.zaxxer.hikari;
	requires org.apache.commons.text;
	
	
	
	exports com.kyj.fx.nightmare;
	exports com.kyj.fx.nightmare.actions.comm.core;
	exports com.kyj.fx.nightmare.actions.deploy;
	exports com.kyj.fx.nightmare.actions.ec.ec;
	exports com.kyj.fx.nightmare.actions.ec.ec.events;
	exports com.kyj.fx.nightmare.actions.ec.ec.group;
	exports com.kyj.fx.nightmare.actions.ec.ec.par;
	exports com.kyj.fx.nightmare.actions.ec.ec.rule;
	exports com.kyj.fx.nightmare.actions.ec.ec.scripts;
	exports com.kyj.fx.nightmare.actions.ec.eq.eventform;
	exports com.kyj.fx.nightmare.actions.ec.eq.states;
	exports com.kyj.fx.nightmare.actions.frame;
	exports com.kyj.fx.nightmare.actions.support;
	exports com.kyj.fx.nightmare.comm;
	exports com.kyj.fx.nightmare.comm.service;
	exports com.kyj.fx.nightmare.eqtree;
	exports com.kyj.fx.nightmare.grid;
	exports com.kyj.fx.fxloader;

}