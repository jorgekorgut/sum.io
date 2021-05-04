package client.sound;

import javax.sound.sampled.Clip;
import client.engine.EngineHandler;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl.Type;



public abstract class AudioClip {
	
	private final Clip clip;
	protected AudioMaster audioMaster;
	
	public AudioClip(Clip clip, AudioMaster audioMaster) {
		this.clip = clip;
		clip.start();
		this.audioMaster = audioMaster;
	}
	
	public void update() {
		final FloatControl control = (FloatControl) clip.getControl(Type.MASTER_GAIN);
		control.setValue(getVolume());	
	}
	
	protected abstract float getVolume();
	
	//r we done? j pense pas
	
	public boolean hasFinishedPlaying() {
		return !clip.isRunning();
		
	}
	public void cleanUp() {
		clip.close();
	}
}
