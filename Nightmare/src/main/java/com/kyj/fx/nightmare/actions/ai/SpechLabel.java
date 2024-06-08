/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

import java.awt.Desktop;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.kyj.fx.nightmare.comm.ExecutorDemons;
import com.kyj.fx.nightmare.comm.ResourceLoader;

import javafx.scene.Node;

/**
 * 
 */
public class SpechLabel extends DefaultLabel implements PlaySoundAble{

	private byte[] record;
	private Path tempFilePath;
	
	public SpechLabel(String send, byte[] record) {
		super(send);
		this.record = record;
	}
	
	public SpechLabel(String send, Node graphic, byte[] record) {
		super(send, graphic);
		this.record = record;
	}
	public SpechLabel(String send, Node graphic, Path tempFilePath) {
		super(send, graphic);
		this.tempFilePath = tempFilePath;
	}
	public SpechLabel(String send, Path tempFilePath) {
		super(send);
		this.tempFilePath = tempFilePath;
	}
	
	void playMyVoid(){
		if(record == null && tempFilePath!=null)
		{
			try {
				record = Files.readAllBytes(tempFilePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(() -> playAsynch(record));
	}
	
//	public void playMyVoid() {
//		if (this.tempFilePath != null) {
//			try {
//				Desktop desktop = Desktop.getDesktop();
//				desktop.open(tempFilePath.toFile());
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			return;
//		}
//
//		if (this.record == null)
//			return;
//		String tmpdir = ResourceLoader.getInstance().get(ResourceLoader.AI_CREATE_WAVE_FILE_DIR, "tmp");
//		Path tempFilePath = Path.of(tmpdir, System.currentTimeMillis() + ".wav");
//		tempFilePath.toFile().getParentFile().mkdirs();
//		try {
//			Files.write(tempFilePath, record);
//			this.tempFilePath = tempFilePath;
//			Desktop desktop = Desktop.getDesktop();
//			desktop.open(tempFilePath.toFile());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	@Override
	public void playSound() {
		playMyVoid();
	}
	
}
