package client.sound;

// Here im generating a "clip" that can be a music or a sound that ill atribute to a command

import javax.sound.sampled.*;

import client.engine.EngineHandler;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class AudioMaster {
	
	private float musicVolume;
	private float soundVolume;
	
	
	public float getMusicVolume() {
		return musicVolume;
	}

	public void setMusicVolume(float musicVolume) {
		this.musicVolume = musicVolume;
	}

	public float getSoundVolume() {
		return soundVolume;
	}

	public void setSoundVolume(float soundVolume) {
		this.soundVolume = soundVolume;
	}

	private List<AudioClip> audioClips;
	
	public AudioMaster (){
		audioClips = new ArrayList<>();
		musicVolume = 0;
		soundVolume = 0;
	
	}
	
	public void update() {
		audioClips.forEach(audioClip -> audioClip.update());
		
		List.copyOf(audioClips).forEach(audioClip-> {
			if(audioClip.hasFinishedPlaying()) {
				audioClip.cleanUp();
				audioClips.remove(audioClip);
			}
		});
		
	}
	
	
	//For the music track
	
	public void playMusic(String fileName) {
		final Clip clip = getClip(fileName);
		audioClips.add(new MusicClip(clip,this));
		
	}
	
	//For the sound effects 
	public void playSound(String fileName) {
		final Clip clip = getClip(fileName);
		audioClips.add(new SoundClip(clip,this));
		
	}
	
	public Clip getClip(String fileName) {
		//final URL soundFile= AudioMaster.class.getResource("/sounds/"+ fileName);
		//final URL soundFile= AudioMaster.class.getResource(System.getProperty("user.dir") + System.getProperty("file.separator")+"res"+ System.getProperty("file.separator")+"sounds"+ System.getProperty("file.separator")+"isobubbler.wav");
		
		File res = new File(System.getProperty("user.dir") + System.getProperty("file.separator")+"res"+ System.getProperty("file.separator")+"sounds");
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(res);
			
			final Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.setMicrosecondPosition(0);
			return clip;
		} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
		
		return null;
	}
}
