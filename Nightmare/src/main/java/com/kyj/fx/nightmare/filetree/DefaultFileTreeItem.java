/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.filetree
 *	작성일   : 2023. 4. 1.
 *	작성자   : (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare.filetree;

import java.nio.file.Files;
import java.nio.file.Path;

import javafx.scene.control.TreeItem;

/**
 * @author (zaruous@naver.com)
 *
 */
public class DefaultFileTreeItem extends TreeItem<Path> {

	public DefaultFileTreeItem() {
		super();
	}

	public DefaultFileTreeItem(Path value) {
		super(value);
	}

	@Override
	public boolean isLeaf() {
		return Files.isRegularFile(getValue());
	}

}
