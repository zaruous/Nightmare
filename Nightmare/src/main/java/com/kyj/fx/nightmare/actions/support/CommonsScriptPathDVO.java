/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper
 *	작성일   : 2021. 7. 6.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.actions.support;

import com.kyj.fx.nightmare.comm.AbstractDVO;
import com.kyj.fx.nightmare.ui.grid.ColumnVisible;
import com.kyj.fx.nightmare.ui.grid.ColumnWidth;
import com.kyj.fx.nightmare.ui.grid.UseCommonClick;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
@UseCommonClick(false)
public class CommonsScriptPathDVO extends AbstractDVO {

	@ColumnWidth(200)
	private StringProperty filePath = new SimpleStringProperty();
	@ColumnVisible(false)
	private StringProperty description = new SimpleStringProperty();
	@ColumnVisible(false)
	private StringProperty code = new SimpleStringProperty();
	@ColumnWidth(300)
	private StringProperty fileFullPath = new SimpleStringProperty();

	public final StringProperty filePathProperty() {
		return this.filePath;
	}

	public final String getFilePath() {
		return this.filePathProperty().get();
	}

	public final void setFilePath(final String filePath) {
		this.filePathProperty().set(filePath);
	}

	public final StringProperty descriptionProperty() {
		return this.description;
	}

	public final String getDescription() {
		return this.descriptionProperty().get();
	}

	public final void setDescription(final String description) {
		this.descriptionProperty().set(description);
	}

	public final StringProperty codeProperty() {
		return this.code;
	}

	public final String getCode() {
		return this.codeProperty().get();
	}

	public final void setCode(final String code) {
		this.codeProperty().set(code);
	}

	public final StringProperty fileFullPathProperty() {
		return this.fileFullPath;
	}

	public final String getFileFullPath() {
		return this.fileFullPathProperty().get();
	}

	public final void setFileFullPath(final String fileFullPath) {
		this.fileFullPathProperty().set(fileFullPath);
	}

}
