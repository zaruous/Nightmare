/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper
 *	작성일   : 2021. 7. 6.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

import org.apache.commons.io.DirectoryWalker;

import com.kyj.fx.nightmare.actions.support.CommonsScriptPathDVO;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class DirWalker extends DirectoryWalker<CommonsScriptPathDVO> {

	private File rootFile;

	public File getRootFile() {
		return rootFile;
	}

	public void setRootFile(File rootFile) {
		this.rootFile = rootFile;
	}

	@Override
	protected boolean handleDirectory(File directory, int depth, Collection<CommonsScriptPathDVO> results) throws IOException {

		File[] listFiles = directory.listFiles();
		for (File f : listFiles) {

			if (f.isFile())
				handleFile(f, depth, results);

			if (f.isDirectory()) {
				handleDirectory(f, depth + 1, results);
			}
		}

		return true;
	}

	@Override
	protected void handleFile(File file, int depth, Collection<CommonsScriptPathDVO> results) throws IOException {

		if (depth > 7)
			throw new CancelException(file, depth);

		if (file.getName().toUpperCase().endsWith("VBS")) {
			CommonsScriptPathDVO d = new CommonsScriptPathDVO();

			// Paths.get(rootFile.toPath());
			// FileUtil.toRelativizeForGagoyle(file)
			Path relativize = rootFile.toPath().relativize(file.toPath());
			d.setFilePath(relativize.toString());
			d.setDescription("");
			d.setFileFullPath(file.getAbsolutePath());
			results.add(d);
		}

	}

}
