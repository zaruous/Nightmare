/**
 * 
 */
package com.kyj.fx.websocket;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class DataBody {
	private String eventName;
	private String html; private String text;
	private String location;
	private List<String> iframeSrcs = new ArrayList<>();
	private String storageData;
	

	public String getStorageData() {
		return storageData;
	}

	public void setStorageData(String storageData) {
		this.storageData = storageData;
	}

	public List<String> getIframeSrcs() {
		return iframeSrcs;
	}

	public void setIframeSrcs(List<String> iframeSrcs) {
		this.iframeSrcs = iframeSrcs;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "DataBody [eventName=" + eventName + ", html=" + html + ", text=" + text + ", location=" + location
				+ ", iframeSrcs=" + iframeSrcs + ", storageData=" + storageData + "]";
	}

	

	

	

}
