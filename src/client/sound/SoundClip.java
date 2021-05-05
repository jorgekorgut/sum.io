package client.sound;

import javax.sound.sampled.Clip;
import client.engine.EngineHandler;

public class SoundClip extends AudioClip{

	public SoundClip(Clip clip, AudioMaster audioMaster) {
		super(clip, audioMaster);
	}

	@Override
	protected float getVolume() {
		return audioMaster.getMusicVolume();
	}
	

}
