/**
 * 
 */
package com.kyj.fx.nightmare.ui.stock;

import com.google.gson.annotations.SerializedName;

/**
 * 
 */
public class StockDataItemDVO {

	// 시작가
	@SerializedName("TDD_OPNPRC")
	private String tddOpnprc;
	// 종가
	@SerializedName("TDD_CLSPRC")
	private String tddClsprc;
	//거래량
	@SerializedName("ACC_TRDVOL")
	private String accTrdvol;
	//회사 코드
	@SerializedName("ISU_SRT_CD")
	private String isuSrtCd;
	//회사명
	@SerializedName("ISU_ABBRV")
	private String isuAbbrv;
	
	@SerializedName("ISU_CD")
	private String isuCd;
	
	@SerializedName("MKT_NM")
	private String mktNm;
	@SerializedName("SECT_TP_NM")
	private String sectTpNm;

	@SerializedName("FLUC_TP_CD")
	private String flucTpCd;
	@SerializedName("CMPPREVDD_PRC")
	private String cmpprevddPrc;
	@SerializedName("FLUC_RT")
	private String flucRt;
	
	
	@SerializedName("TDD_HGPRC")
	private String tddHgprc;
	@SerializedName("TDD_LWPRC")
	private String tddLwprc;
	
	@SerializedName("ACC_TRDVAL")
	private String accTrdval;
	@SerializedName("MKTCAP")
	private String mktcap;
	@SerializedName("LIST_SHRS")
	private String listShrs;
	@SerializedName("MKT_ID")
	private String mktId;

	public String getIsuSrtCd() {
		return isuSrtCd;
	}

	public void setIsuSrtCd(String isuSrtCd) {
		this.isuSrtCd = isuSrtCd;
	}

	public String getIsuCd() {
		return isuCd;
	}

	public void setIsuCd(String isuCd) {
		this.isuCd = isuCd;
	}

	public String getIsuAbbrv() {
		return isuAbbrv;
	}

	public void setIsuAbbrv(String isuAbbrv) {
		this.isuAbbrv = isuAbbrv;
	}

	public String getMktNm() {
		return mktNm;
	}

	public void setMktNm(String mktNm) {
		this.mktNm = mktNm;
	}

	public String getSectTpNm() {
		return sectTpNm;
	}

	public void setSectTpNm(String sectTpNm) {
		this.sectTpNm = sectTpNm;
	}

	public String getTddClsprc() {
		return tddClsprc;
	}

	public void setTddClsprc(String tddClsprc) {
		this.tddClsprc = tddClsprc;
	}

	public String getFlucTpCd() {
		return flucTpCd;
	}

	public void setFlucTpCd(String flucTpCd) {
		this.flucTpCd = flucTpCd;
	}

	public String getCmpprevddPrc() {
		return cmpprevddPrc;
	}

	public void setCmpprevddPrc(String cmpprevddPrc) {
		this.cmpprevddPrc = cmpprevddPrc;
	}

	public String getFlucRt() {
		return flucRt;
	}

	public void setFlucRt(String flucRt) {
		this.flucRt = flucRt;
	}

	public String getTddOpnprc() {
		return tddOpnprc;
	}

	public void setTddOpnprc(String tddOpnprc) {
		this.tddOpnprc = tddOpnprc;
	}

	public String getTddHgprc() {
		return tddHgprc;
	}

	public void setTddHgprc(String tddHgprc) {
		this.tddHgprc = tddHgprc;
	}

	public String getTddLwprc() {
		return tddLwprc;
	}

	public void setTddLwprc(String tddLwprc) {
		this.tddLwprc = tddLwprc;
	}

	public String getAccTrdvol() {
		return accTrdvol;
	}

	public void setAccTrdvol(String accTrdvol) {
		this.accTrdvol = accTrdvol;
	}

	public String getAccTrdval() {
		return accTrdval;
	}

	public void setAccTrdval(String accTrdval) {
		this.accTrdval = accTrdval;
	}

	public String getMktcap() {
		return mktcap;
	}

	public void setMktcap(String mktcap) {
		this.mktcap = mktcap;
	}

	public String getListShrs() {
		return listShrs;
	}

	public void setListShrs(String listShrs) {
		this.listShrs = listShrs;
	}

	public String getMktId() {
		return mktId;
	}

	public void setMktId(String mktId) {
		this.mktId = mktId;
	}

}
