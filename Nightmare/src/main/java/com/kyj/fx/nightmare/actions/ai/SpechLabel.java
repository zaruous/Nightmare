/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.kyj.fx.nightmare.comm.ExecutorDemons;

import javafx.scene.Node;

/**
 * 
 */
public class SpechLabel extends DefaultLabel implements PlaySoundAble {

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

	PlayObject playObject;

	void playMyVoid() {
		if (record == null && tempFilePath != null) {
			try {
				record = Files.readAllBytes(tempFilePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		playObject = new PlayObject(record);
		ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(() -> playObject.run());
	}

	@Override
	public void stop() {
		if (playObject != null)
			playObject.shutdown();
	}

	@Override
	public void playSound() {
		playMyVoid();
	}

}
