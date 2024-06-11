/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.function.Consumer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.kyj.fx.nightmare.comm.ExecutorDemons;

import javafx.scene.Node;

/**
 * 
 */
public class DefaultLabel implements PlaySoundAble {
	String text;
	Node graphic;
	String tip;
	private byte[] audioData;
	PlayObject playObject;
	private Consumer<DefaultLabel> onPlayStart;
	private Consumer<DefaultLabel> onPlayEnd;

	public DefaultLabel() {
		super();
	}

	public DefaultLabel(String text) {
		super();
		this.text = text;
	}

	public DefaultLabel(String text, Node graphic) {
		this.text = text;
		this.graphic = graphic;
		;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Node getGraphic() {
		return graphic;
	}

	public void setGraphic(Node graphic) {
		this.graphic = graphic;
	}

	public Consumer<DefaultLabel> getOnPlayStart() {
		return onPlayStart;
	}

	public void setOnPlayStart(Consumer<DefaultLabel> onPlayStart) {
		this.onPlayStart = onPlayStart;
	}

	public Consumer<DefaultLabel> getOnPlayEnd() {
		return onPlayEnd;
	}

	public void setOnPlayEnd(Consumer<DefaultLabel> onPlayEnd) {
		this.onPlayEnd = onPlayEnd;
	}

	void readAndPlay() {
		if (getText() == null || getText().isBlank())
			return;

		String text = getText();

		TextToSpeechGptServiceImpl serviceImpl;
		try {
			if (audioData == null) {
				serviceImpl = new TextToSpeechGptServiceImpl();
				audioData = serviceImpl.getData(text);
			}
			playObject = new PlayObject(audioData);

			ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(() -> playObject.run());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		if (playObject != null)
			playObject.shutdown();
	}

	protected class PlayObject extends Thread {
		byte[] audioData;
		SourceDataLine line;

		public PlayObject(byte[] audioData) {
			this.audioData = audioData;
		}

		public boolean isPlaying() {
			return this.line.isRunning();
		}

		public void shutdown() {
			line.stop();
			line.close();
			onPlayEnd();
		}

		public void onPlayStart() {
			if (onPlayStart != null)
				onPlayStart.accept(DefaultLabel.this);
		}

		public void onPlayEnd() {
			if (onPlayEnd != null)
				onPlayEnd.accept(DefaultLabel.this);
		}

		@Override
		public void run() {
			// 예제용 바이트 배열 (WAV 파일 데이터가 있어야 합니다)
			// byte[] audioData = getAudioData(); // 실제 오디오 데이터를 가져오는
			// 방법은 별도
			// 구현
			// 필요

			// 바이트 배열을 오디오 입력 스트림으로 변환
			try (AudioInputStream audioInputStream = getAudioInputStreamFromByteArray(audioData)) {
				// 오디오 포맷 및 라인 정보 가져오기
				AudioFormat format = audioInputStream.getFormat();
				DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

				// 소스 데이터 라인 열기 및 오디오 데이터 재생
				try {
					line = (SourceDataLine) AudioSystem.getLine(info);
					line.open(format);
					line.start();
					onPlayStart();

					byte[] buffer = new byte[4096];
					int bytesRead;
					while ((bytesRead = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
						line.write(buffer, 0, bytesRead);
					}

					// 데이터 전송이 완료되면 라인 종료
					line.drain();
					line.stop();
					onPlayEnd();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					line.close();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

	}

	protected AudioInputStream getAudioInputStreamFromByteArray(byte[] audioData) throws IOException, UnsupportedAudioFileException {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
		return AudioSystem.getAudioInputStream(byteArrayInputStream);
	}

	@Override
	public void playSound() {
		readAndPlay();
	}
}