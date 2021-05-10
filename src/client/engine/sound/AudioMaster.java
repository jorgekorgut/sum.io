package client.engine.sound;

// Here im generating a "clip" that can be a music or a sound that ill atribute to a command

import javax.sound.sampled.*;

import client.engine.EngineHandler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;


public class AudioMaster {
	
	public static final float MUTE_VALUE = -80f;
	public static final int HEAR_RANGE = 2000;
	public static final String START_MUSIC_REFERENCE = "startlobby";
	public static final String END_MUSIC_REFERENCE = "end";
	public static final String SELECTED_REFERENCE = "selected";
	
	private float musicVolume;
	private float soundVolume;
	private Hashtable<String,Clip> audioMap = null;
	
	public AudioMaster (){
		audioMap = new Hashtable<String,Clip>();
		musicVolume = 0;
		soundVolume = 0;
		loadAudio();
	}
	
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
	
	//FIXME: Problem: The thread of the music is running without really need.
	private void loadAudio()
	{
		File res = new File(System.getProperty("user.dir") + System.getProperty("file.separator")+"res"+ System.getProperty("file.separator")+"sounds");
		String[] audioNames = res.list();
		
		try {
			
			for(int i = 0; i< audioNames.length; i++)
			{
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(res + System.getProperty("file.separator") + audioNames[i]));
				Clip clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				clip.setMicrosecondPosition(0);
				
				FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				
				clip.start();
				volume.setValue(MUTE_VALUE);
				clip.setFramePosition(0);
				clip.flush();
				audioMap.put(audioNames[i].substring(0,audioNames[i].length()-4), clip);
			}
			
			
		} catch (IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	//For the sound effects 
	//FIXME: It is not the best way to do it, but it works.
	public void playSound(String name,float volume) {
		
		Clip clip = audioMap.get(name);
		clip.stop();
		clip.setMicrosecondPosition(0);
		
		FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		volumeControl.setValue(volume);
		clip.setMicrosecondPosition(0);
		clip.start();
	}
	
	public void stopSound(String name)
	{
		audioMap.get(name).stop();
	}
	
}
