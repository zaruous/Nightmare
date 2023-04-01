/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.actions.comm
 *	작성일   : 2021. 11. 25.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.comm.core;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.comm.AbstractDVO;
import com.kyj.fx.nightmare.comm.DialogUtil;
import com.kyj.fx.nightmare.comm.FxExcelUtil;
import com.kyj.fx.nightmare.comm.FxUtil;
import com.kyj.fx.nightmare.comm.Message;
import com.kyj.fx.nightmare.comm.StageStore;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.comm.service.XMLUtils;

import javafx.scene.control.TableView;

/**
 * 엑셀과 관련된 내용 처리하기위한 대상 테이이블 뷰를 리턴함 <br/>
 * 
 * @author KYJ (callakrsos@naver.com)
 *
 */
public interface OnExcelTableViewList {

	static final Logger LOGGER = LoggerFactory.getLogger(OnExcelTableViewList.class);

	/**
	 * 데이터 처리 대상이되는 테이블 뷰를 리턴함 <br/>
	 * 테이블 뷰가 없을 경우 exportExcel 함수를 오버라이딩하여 별도 구현할것<br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 25.
	 * @return
	 */
	public <T extends AbstractDVO> List<TableView<T>> excelTableList();

	/**
	 * excel import 기능 수행<br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 26.
	 * @param excelFile
	 */
	public default void importExcel(File excelFile) {
		String readExcelToXml = "";
		try {
			readExcelToXml = FxExcelUtil.readExcelToXml(excelFile);
			Document load = XMLUtils.load(readExcelToXml);
			importExcel(excelFile, load);
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
			// %IExcelTableViewList_000001 엑셀 처리 과정중 에러 발생
			DialogUtil.showExceptionDailog(StageStore.getPrimaryStage(), e, Message.getInstance().getMessage("%IExcelTableViewList_000001"),
					ValueUtil.toString(e));
		}
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 29.
	 * @param doc
	 */
	public void importExcel(File fromFile, Document doc);

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 11. 29.
	 * @param out
	 * @return
	 */
	public default boolean exportExcel(File out) {
		List<TableView<AbstractDVO>> excelTableList = excelTableList();
		if (excelTableList == null)
			return false;
		try {
			FxUtil.exportExcelFile(out, excelTableList);
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
			return false;
		}
		return true;
	}

	/**
	 * 탭 컨텐츠에 할당된 버튼인 Excel Export 을 활성화 할지 유무 <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 9.
	 * @return
	 */
	public default boolean enableExcelExportButton() {
		return false;
	}

	/**
	 * 탭 컨텐츠에 할당된 버튼인 Excel Import 을 활성화 할지 유무 <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 12. 9.
	 * @return
	 */
	public default boolean enableExcelImportButton() {
		return false;
	}
}
