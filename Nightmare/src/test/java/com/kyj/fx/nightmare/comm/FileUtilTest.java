package com.kyj.fx.nightmare.comm;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDate;
import java.time.ZoneId;

import org.apache.commons.io.file.PathUtils;
import org.apache.commons.io.file.SimplePathVisitor;
import org.junit.Test;

public class FileUtilTest {

	@Test
	public void testCleanDirtyFilesAsynch() throws IOException {
		int days = 0;
		String tmpdir = ResourceLoader.getInstance().get(ResourceLoader.AI_CREATE_WAVE_FILE_DIR, "tmp");
		PathUtils.visitFileTree(new SimplePathVisitor() {

			@Override
			public FileVisitResult visitFile(Path path, BasicFileAttributes attr) throws IOException {
				
				
				FileTime creationTime = attr.creationTime();
				
				LocalDate fileDate = LocalDate.ofInstant(creationTime.toInstant(), ZoneId.systemDefault());
				LocalDate cutoffDate = LocalDate.now().minusDays(days);
				boolean delFlag = fileDate.isBefore(cutoffDate) && path.getFileName().toString().endsWith(".wav");
				System.out.println(path +"\t"+ fileDate+  "\t" + delFlag);
				
				return super.visitFile(path, attr);
			}
			
		}, Path.of(tmpdir).toAbsolutePath());
		
//		FileUtil.cleanDirtyFilesAsynch();
	}

}
