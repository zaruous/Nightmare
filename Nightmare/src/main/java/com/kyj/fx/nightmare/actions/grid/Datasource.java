/**
 * 
 */
package com.kyj.fx.nightmare.actions.grid;

import com.kyj.fx.nightmare.comm.AbstractDVO;
import com.kyj.fx.nightmare.ui.grid.ColumnWidth;
import com.kyj.fx.nightmare.ui.grid.NonEditable;
import com.kyj.fx.nightmare.ui.grid.TableName;

/**
 * 
 */
@TableName("Datasource")
public class Datasource extends AbstractDVO{
	
	@NonEditable
	@ColumnWidth(80)
	private String aliasName;
	@ColumnWidth(120)
	private String driver;
	@ColumnWidth(120)
	private String url;
	private String userId;
	private String userPwd;

	

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

}
