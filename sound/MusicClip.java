package client.sound;

import javax.sound.sampled.Clip;

import client.engine.EngineHandler;

public class MusicClip extends AudioClip{

	public MusicClip(Clip clip, AudioMaster audioMaster) {
		super(clip, audioMaster);
		
	}

	@Override
	
	protected float getVolume() {
		return audioMaster.getMusicVolume();
	}
	
	

}
