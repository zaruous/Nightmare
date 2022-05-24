open module ETHelper {

	requires transitive spring.jdbc;
	requires spring.core;
	requires transitive spring.tx;
	requires spring.beans;

	requires javafx.controls;
	requires javafx.base;
	requires javafx.fxml;
	requires javafx.graphics;
	requires org.apache.commons.io;
	requires java.sql;

	requires slf4j.api;
//	requires slf4j.api;
	
	// requires ch.qos.logback.core;
//	requires logback.core;
	requires logback.classic;

	requires org.controlsfx.controls;
	requires dom4j;
	requires org.apache.commons.codec;
	requires velocity.engine.core;
	requires transitive tomcat.jdbc;
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

	exports com.kyj.fx.b.ETScriptHelper;
	exports com.kyj.fx.b.ETScriptHelper.actions.comm.core;
	exports com.kyj.fx.b.ETScriptHelper.actions.deploy;
//	exports com.kyj.fx.b.ETScriptHelper.actions.ec;
	exports com.kyj.fx.b.ETScriptHelper.actions.ec.ec;
	exports com.kyj.fx.b.ETScriptHelper.actions.ec.ec.events;
	exports com.kyj.fx.b.ETScriptHelper.actions.ec.ec.group;
	exports com.kyj.fx.b.ETScriptHelper.actions.ec.ec.par;
	exports com.kyj.fx.b.ETScriptHelper.actions.ec.ec.rule;
	exports com.kyj.fx.b.ETScriptHelper.actions.ec.ec.scripts;
	exports com.kyj.fx.b.ETScriptHelper.actions.ec.eq.eventform;
	exports com.kyj.fx.b.ETScriptHelper.actions.ec.eq.states;
	exports com.kyj.fx.b.ETScriptHelper.actions.frame;
	exports com.kyj.fx.b.ETScriptHelper.actions.support;
	exports com.kyj.fx.b.ETScriptHelper.comm;
	exports com.kyj.fx.b.ETScriptHelper.comm.service;
	exports com.kyj.fx.b.ETScriptHelper.eqtree;
	exports com.kyj.fx.b.ETScriptHelper.grid;
	exports com.kyj.fx.fxloader;

}