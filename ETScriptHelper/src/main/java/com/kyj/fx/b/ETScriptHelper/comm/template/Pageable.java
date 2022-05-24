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
public interface Pageable {

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2020. 7. 30.
	 * @param page
	 */
	public void setCurrentPage(int page);

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2020. 7. 30.
	 * @return
	 */
	public int getCurrentPage();

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2020. 7. 30.
	 * @return
	 */
	public int getTotalPage();

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2020. 7. 30.
	 * @param totalPage
	 */
	public void setTotalPage(int totalPage);

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2020. 7. 30.
	 * @return
	 */
	public int getPageSize();

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2020. 7. 30.
	 * @param pageSize
	 */
	public void setPageSize(int pageSize);
}
