/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.util
 *	작성일   : 2016. 03. 26. 수정
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.nightmare.comm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.BiFunction;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);

	/**
	 * 2014. 10. 3. KYJ
	 *
	 * @return
	 * @throws IOException
	 * @처리내용 : 엑셀파일을 생성한다. xlsx(최신)
	 */
	public static Workbook createNewWorkBookXlsx() throws IOException {
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
		return xssfWorkbook;
	}

	public static Workbook readXlsx(File filePath) throws IOException {
		return new XSSFWorkbook(new FileInputStream(filePath));
	}

	public static Workbook readXlsx(String filePath) throws IOException {
		return readXlsx(new File(filePath));
	}

	public static FileOutputStream getFileOutputStream(String fileName, String fileFormat)
			throws FileNotFoundException {
		return new FileOutputStream(fileName.concat("." + fileFormat));
	}

	public static Cell createCell(Sheet sheet, Object str, int row, int column) throws Exception {
		Row row2 = sheet.getRow(row);

		if (row2 == null) {
			row2 = sheet.createRow(row);
		}
		Cell createCell = null;
		try {
			createCell = row2.getCell(column);
			if (createCell == null) {
				createCell = row2.createCell(column);
			}
		} catch (NullPointerException e) {
			createCell = row2.createCell(column);
		}

		if (str instanceof String) {
			if (((String) str).length() >= 32767)
				createCell.setCellValue(new XSSFRichTextString(str.toString()));
			else
				createCell.setCellValue((String) str);
		} else if (str instanceof Integer) {
			createCell.setCellValue((Integer) str);
		} else if (str instanceof Double) {
			createCell.setCellValue((Double) str);
		} else if (str instanceof Float) {
			createCell.setCellValue((Float) str);
		} else if (str == null) {
			createCell.setCellValue("");
		} else {
			createCell.setCellValue(str.toString());
//			throw new Exception("뭘 입력하신겁니까?");
		}

		return createCell;
	}

	/**
	 * 특정셀에 코멘트를 추가한다.
	 *
	 * @param sheet
	 * @param cell
	 * @param commentText
	 * @return
	 */
	public static void addComment(Sheet sheet, Cell cell, String commentText) {
		XSSFDrawing patr = (XSSFDrawing) sheet.createDrawingPatriarch();
		Comment comment = patr.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
		comment.setString(new XSSFRichTextString(commentText));
		cell.setCellComment(comment);
	}

	/**
	 * 엑셀파일을 K로 컨버트
	 *
	 * @param selectFile
	 * @param convert
	 * @return
	 * @throws Exception
	 */
	public static <K> K toK(File selectFile, BiFunction<File, Workbook, K> convert) throws Exception {
		Workbook xlsx = readXlsx(selectFile);
		return convert.apply(selectFile, xlsx);
	}

}
