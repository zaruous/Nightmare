/**
 * 
 */
package com.kyj.fx.nightmare.ui.stock;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * 
 */
public class StockDataMasterDVO {
	@SerializedName("OutBlock_1")
	private List<StockDataItemDVO> outBlock1;
	@SerializedName("CURRENT_DATETIME")
	private String currentDatetime;

	public List<StockDataItemDVO> getOutBlock1() {
		return outBlock1;
	}

	public void setOutBlock1(List<StockDataItemDVO> outBlock1) {
		this.outBlock1 = outBlock1;
	}

	public String getCurrentDatetime() {
		return currentDatetime;
	}

	public void setCurrentDatetime(String currentDatetime) {
		this.currentDatetime = currentDatetime;
	}

}
