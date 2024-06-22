/**
 * 
 */
package com.kyj.fx.nightmare.actions.ai;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.comm.Message;
import com.kyj.fx.nightmare.comm.ValueUtil;

/**
 * 마이크 세팅
 */
public class MixerSettings {
	private static final Logger LOGGER = LoggerFactory.getLogger(MixerSettings.class);

	protected Mixer mixer;
	// "마이크(2- H710)"

	/**
	 * 설정에 저장된 마이크 설정명
	 * @return
	 */
	public String getConfigedMixerName() {return loadSettings();}
	
	public void load() throws MixerNotFound{
		String name = loadSettings();
		if(ValueUtil.isEmpty(name))
		{
			throw new MixerNotFound(Message.getInstance().getMessage("MixerNotFound"));
		}
		
		Mixer.Info[] mixerInfos = getMixers();
		LOGGER.debug("사용 가능한 믹서 목록:");

		if (mixerInfos.length == 0)
			throw new MixerNotFound("사용가능한 마이크가 존재하지않습니다.");

		Mixer.Info info = null;
		for (int i = 0; i < mixerInfos.length; i++) {
			mixer = AudioSystem.getMixer(mixerInfos[i]);
			LOGGER.debug(i + ": " + mixerInfos[i].getName() + " - " + mixerInfos[i].getDescription());
			if (name.equals(mixerInfos[i].getName())) {
				info = mixerInfos[i];
				break;
			}
		}
		if(info == null)
			throw new MixerNotFound("사용가능한 마이크가 존재하지않습니다.");
		
		createSettings(info);
	}

	private String loadSettings() {
		
		try {
			Path of = Path.of(".mixerSettings");
			if(!of.toFile().exists())
			{
				try {
					Files.writeString(of, "");
				} catch (IOException e) {
					LOGGER.debug(ValueUtil.toString(e));
				}
			}
			return Files.readString(of);
		} catch (IOException e) {
			LOGGER.debug(ValueUtil.toString(e));
		}
		return "";
	}
	
	public void createSettings(Info info) {
		Path of = Path.of(".mixerSettings");
		try {
			Files.writeString(of, info.getName());
		} catch (IOException e) {
			LOGGER.debug(ValueUtil.toString(e));
		}
	}

	public final Mixer getMixer() {
		return mixer;
	}

	public Info[] getMixers() {
		Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
		return mixerInfos;
	}

}
