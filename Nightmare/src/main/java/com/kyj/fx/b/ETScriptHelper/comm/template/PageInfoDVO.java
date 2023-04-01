/********************************
 *	프로젝트 : api-service
 *	패키지   : beans.common
 *	작성일   : 2020. 7. 30.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.comm.template;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class PageInfoDVO implements Pageable {

	private int currentPage = 1;

	private int pageSize = 500;

	private int totalPage = 1;

	@Override
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;

	}

	@Override
	public int getCurrentPage() {
		return this.currentPage;
	}

	@Override
	public int getTotalPage() {
		return totalPage;
	}

	@Override
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
