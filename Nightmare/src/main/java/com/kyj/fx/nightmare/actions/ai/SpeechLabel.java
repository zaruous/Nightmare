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
public class SpeechLabel extends DefaultLabel implements PlaySoundAble {

	private byte[] record;
	private Path tempFilePath;
	private SpeechResponseModelDVO responseMode;
	public SpeechLabel(String send, byte[] record) {
		super(send);
		this.record = record;
	}

	@Deprecated
	public SpeechLabel(String send, Node graphic, byte[] record) {
		super(send, graphic);
		this.record = record;
	}
	
	public SpeechLabel(SpeechResponseModelDVO responseMode, Node graphic, byte[] record) {
		super(responseMode.getText(), graphic);
		this.record = record;
		this.responseMode = responseMode;
	}
	
	public SpeechLabel(SpeechResponseModelDVO responseMode, Node graphic, Path tempFilePath) {
		super(responseMode.getText(), graphic);
		this.tempFilePath = tempFilePath;
		this.responseMode = responseMode;
	}
	@Deprecated
	public SpeechLabel(String send, Node graphic, Path tempFilePath) {
		super(send, graphic);
		this.tempFilePath = tempFilePath;
	}
	@Deprecated
	public SpeechLabel(String send, Path tempFilePath) {
		super(send);
		this.tempFilePath = tempFilePath;
	}

	public SpeechResponseModelDVO getResponseMode() {
		return responseMode;
	}

	public void setResponseMode(SpeechResponseModelDVO responseMode) {
		this.responseMode = responseMode;
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
