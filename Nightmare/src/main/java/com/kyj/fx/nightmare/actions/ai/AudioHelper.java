package com.kyj.fx.nightmare.actions.ai;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.comm.ValueUtil;

public class AudioHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(AudioHelper.class);
	AudioRecorder recorder = new AudioRecorder();
	private ByteArrayOutputStream out = new ByteArrayOutputStream();
	private int recordFlag;
	

	public Mixer getMixer() {
		return recorder.getMixer();
	}

	public void setMixer(Mixer mixer) {
		recorder.setMixer(mixer);
	}

	public void start() throws IOException {

		if (ValueUtil.isEmpty(recorder.getMixer()))
			throw new RuntimeException("마이크 설정이 없습니다. mixerName을 정의해주세요.");

		out = new ByteArrayOutputStream();
		// 녹음 시작
		try {
			recorder.startRecording();
			recordFlag = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public byte[] stop() {

		// 녹음 중지 및 데이터 가져오기
		byte[] audioData = recorder.stopRecording();
		// 녹음 데이터 파일로 저장
		try (ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
				AudioInputStream audioInputStream = new AudioInputStream(bais, recorder.getAudioFormat(),
						audioData.length / recorder.getAudioFormat().getFrameSize())) {
			AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, out);
			System.out.println("Recording stopped and saved.");
			recordFlag = 0;
			return data = out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean isRecording() {
		return recordFlag == 1;
	}

	byte[] data;

	public byte[] getData() {
		return data;
	}
	
	/**
	 * @param audioData
	 * @param out
	 * @throws Exception
	 */
	public static void writeWav(byte[] audioData, OutputStream out) throws Exception {
//		AudioRecorder recorder = new AudioRecorder();
		// 녹음 데이터 파일로 저장
		AudioFormat audioFormat = new AudioFormat(16000, 16, 2, true, true);
		try (ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
				AudioInputStream audioInputStream = new AudioInputStream(bais, audioFormat,
						audioData.length / audioFormat.getFrameSize())) {
			AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, out);
			LOGGER.debug("Recording stopped and saved.");
		}
	}
	
//
//	public static void main(String[] args) {
//
//		// 사용 가능한 믹서를 나열
//		
//		Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
//		System.out.println("사용 가능한 믹서 목록:");
//		Mixer mixer = AudioSystem.getMixer(mixerInfos[20]);
//		for (int i = 0; i < mixerInfos.length; i++) {
//			LOGGER.debug(i + ": " + mixerInfos[i].getName() + " - " + mixerInfos[i].getDescription());
//			if("마이크(2- H710)".equals(mixerInfos[i].getName()))
//			{
//				mixer = AudioSystem.getMixer(mixerInfos[i]);
//				break;
//			}
//		}
////		Mixer mixer = AudioSystem.getMixer(mixerInfos[20]);
//
//		AudioRecorder recorder = new AudioRecorder();
//		// 녹음 파일 경로 설정
//		String filePath = "recorded_audio.wav";
//		File file = new File(filePath);
//
//		try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
//			// 녹음 시작
//			recorder.startRecording(mixer);
//			LOGGER.debug("Recording started... Press Enter to stop.");
//
//			try {
//				// 사용자 입력 대기
//				System.in.read();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//			// 녹음 중지 및 데이터 가져오기
//			byte[] audioData = recorder.stopRecording();
//
//			// 녹음 데이터 파일로 저장
//			try (ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
//					AudioInputStream audioInputStream = new AudioInputStream(bais, recorder.getAudioFormat(),
//							audioData.length / recorder.getAudioFormat().getFrameSize())) {
//				AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, fileOutputStream);
//				LOGGER.debug("Recording stopped and saved.");
//			}
//
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//	}
	
	class AudioRecorder {

		// 타겟 데이터 라인
		private TargetDataLine targetLine;
		private ByteArrayOutputStream out;
		private Thread recordingThread;
		private boolean recording = false;
		private AudioFormat format;
		private Mixer mixer;

		public Mixer getMixer() {
			return mixer;
		}

		public void setMixer(Mixer mixer) {
			this.mixer = mixer;
		}

		public AudioRecorder() {
			// 오디오 형식 설정
			this.format = new AudioFormat(16000, 16, 2, true, true);
		}

		// 오디오 형식 가져오기
		public AudioFormat getAudioFormat() {
			return this.format;
		}

		// 녹음 시작
		public void startRecording() throws Exception {

			// 데이터 라인 정보 가져오기
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

			// 데이터 라인 열기
			if (!AudioSystem.isLineSupported(info)) {
				System.err.println("Line not supported");
				System.exit(0);
			}

			targetLine = (TargetDataLine) mixer.getLine(info);
//				targetLine = (TargetDataLine) AudioSystem.getLine(info);
			targetLine.open(format);
			targetLine.start();
			recording = true;
			out = new ByteArrayOutputStream();

			// 새로운 스레드에서 녹음
			recordingThread = new Thread(() -> {
				byte[] buffer = new byte[1024];
				while (recording) {
					int bytesRead = targetLine.read(buffer, 0, buffer.length);
					if (bytesRead > 0) {
						out.write(buffer, 0, bytesRead);
					}
				}
			});
			recordingThread.start();

		}

		// 녹음 중지 및 데이터 반환
		public byte[] stopRecording() {
			recording = false;
			if (targetLine != null) {
				targetLine.stop();
				targetLine.close();
			}
			try {
				if (recordingThread != null) {
					recordingThread.join();
				}
				out.flush();
				return out.toByteArray();
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
}

