/********************************
 *	프로젝트 : api-service
 *	패키지   : beans.common
 *	작성일   : 2020. 8. 5.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.comm.template;

import java.net.URL;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
@XmlRootElement(name = "Template")
public class Template implements Cloneable {

	private String serviceId;
	private String action;

	private Url url;

	private Sql sql;

	private Code code;

	private Descroption description;

	private Version version;

	@XmlElement(name = "Url")
	public Url getUrl() {
		return url;
	}

	public void setUrl(Url url) {
		this.url = url;
	}

	@XmlElement(name = "Sql")
	public Sql getSql() {
		return sql;
	}

	public void setSql(Sql sql) {
		this.sql = sql;
	}

	@XmlElement(name = "Code")
	public Code getCode() {
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	@XmlAttribute
	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	@XmlAttribute
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@XmlElement(name = "Description")
	public Descroption getDescription() {
		return description;
	}

	public void setDescription(Descroption description) {
		this.description = description;
	}

	@XmlElement(name = "Version")
	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

	@Override
	public Template clone() throws CloneNotSupportedException {
		try {
			return TemplateUtil.JaxbUtils.load(this.xml, Template.class);
		} catch (Exception e) {
			throw new CloneNotSupportedException(e.getMessage());
		}
	}

	private String xml;

	public void setXml(String xml) {
		this.xml = xml;
	}

	/**
	 * @param resource
	 * @return
	 * @throws Exception 
	 */
	public static Template load(URL resource) throws Exception {
		return TemplateUtil.GetTemplate(resource);
	}

}
