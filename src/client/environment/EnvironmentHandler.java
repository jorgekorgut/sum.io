package client.environment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.Timer;

import client.MainClient;
import common.communication.SyncPack;
import common.environment.CollectableBoost;
import common.environment.GameObject;
import common.environment.Platform;
import common.environment.Player;
import client.engine.EngineHandler;
import client.engine.LabelTextObject;
import client.engine.ScreenRender;
import client.engine.winwindow.FenetreGagnant;
import client.lobby.LobbyHandler;
import client.sound.AudioMaster;

/*
 * This class is responsible to receive the SyncPack and update the client conditions.
 */

public class EnvironmentHandler 
{
	public static final int PRIORITYRENDER_UI = 2000;
	public static final int PRIORITYRENDER_PLAYER = 1000;
	public static final int PRIORITYRENDER_NAME = 1100;
	public static final int PRIORITYRENDER_ANIMATION = 1200;
	
	public static final int FINAL_TIME = 3000;
	
	private ArrayList<Player> playerMap;
	private ArrayList<GameObject> interactableObjects;
	private Platform platform;
	private Player player;
	private MainClient callback;
	private boolean[] acienColisionState;
	private FenetreGagnant fenetreGagnant;
	private boolean flagWin;
	
	private int playersCount = 0;
	
	public EnvironmentHandler(MainClient callback)
	{
		flagWin = false;
		playerMap = new ArrayList<Player>();
		interactableObjects = new ArrayList<GameObject>();
		
		acienColisionState = new boolean[12];
		this.callback = callback;
	}

	public int getRemainingPlayers(){return playersCount;}
	public Player getPlayerClient() {return player;}
	public ArrayList<Player> getPlayerMap(){return playerMap;}
	public JFrame getFenetreGagnant() {return fenetreGagnant;}
	
	private void updateEnvironment()
	{	
		for(int i =0; i< playerMap.size(); i++)
		{
			Player p = playerMap.get(i);
			if(!p.isAwake())
			{
				continue;
			}
			if(p.getFlagCollision())
			{
				if(!acienColisionState[i])
				{
					callback.getEngineHandler().getAnimationHandler().onColision(p,player);
					
					double distance = Math.sqrt(p.squareDistanceTo(player));
					
					double volume = AudioMaster.MUTE_VALUE;
					
					if(distance >= 0 && distance < AudioMaster.HEAR_RANGE)
					{
						volume =  (distance/AudioMaster.HEAR_RANGE) * AudioMaster.MUTE_VALUE;	
					}
					callback.getAudioMaster().playSound("collision",(float)volume);
					acienColisionState[i] = true;
				}
			}
			else
			{
				acienColisionState[i] = false;
			}
			
		}
		callback.getEngineHandler().getAnimationHandler().update();
		callback.getEngineHandler().getUserInterface().update();
	}
	
	private void onEnvironmentStart()
	{
		callback.getAudioMaster().stopSound(AudioMaster.START_MUSIC_REFERENCE);
		callback.getEngineHandler().getAnimationHandler().onEnvironmentStart(player);
	}
	
	public void syncClient(SyncPack sPack)
	{
		EngineHandler engineHandler = callback.getEngineHandler();
		
		if(engineHandler == null)
		{
			return;
		}
		
		this.playerMap = sPack.getPlayerMap();
		
		if(this.playerMap == null)
		{
			return;
		}
		
		this.playersCount = sPack.getPlayersCount();
		
		//the current client player is set in this part of the code.
		if(player == null)
		{
			
			for(int i = 0; i<playerMap.size(); i++)
			{
				Player p = playerMap.get(i);
				
				if(p.getPlayerIP().equals(callback.getLobbyHandler().getPlayer()))
				{
					
					player = p;
					callback.getEngineHandler().getInputHandler().setPlayer(p);
					callback.getEngineHandler().getAnimationHandler().setClient(p);
				}
			}
			if(player != null)
			{
				onEnvironmentStart();
			}
		}
		//Update playerState.
		for(int i = 0; i<playerMap.size(); i++)
		{
			Player p = playerMap.get(i);
			
			if(p.equals(player))
			{
				player = p;
			}
			engineHandler.getScreenRender().addToRender((GameObject)p);
		}
		
		//Platform setup in client side
		if(platform == null)
		{
			this.platform = sPack.getPlatform();
			engineHandler.getScreenRender().addToRender((GameObject)this.platform);
		}
		
		//Center the player and make the things move as the player moves.
		if(player != null)
		{
			callback.getEngineHandler().getScreenRender().setOrigin((int)player.getX(),(int)player.getY());
		}
		

		interactableObjects = sPack.getInteractableObjects();
		engineHandler.getScreenRender().addToRender(interactableObjects);
	
		updateEnvironment();	
		
		if(sPack.getWinner() != null && !flagWin)
		{
			flagWin = true;
			onGameFinished(sPack.getWinner());
		}
	}
	
	private void onGameFinished(String winner)
	{
		Player winnerPlayer = null;
		for(Player p: playerMap)
		{
			if(p.getPlayerIP().equals(winner))
			{
				winnerPlayer = p;
			}
		}
		
		LabelTextObject win = new LabelTextObject(callback.getEngineHandler().getScreenRender(),"background3",
				callback.getEngineHandler().getWindow().getJFrame().getWidth()/2,
				callback.getEngineHandler().getWindow().getJFrame().getHeight()/2,
				400,
				100,
				20);
		win.updateText(winner + " won the match!");
		
		callback.getEngineHandler().getScreenRender().addToRender(win);
		callback.getEngineHandler().getAnimationHandler().onGameFinished();
		
		callback.getAudioMaster().playSound(AudioMaster.END_MUSIC_REFERENCE, 1f);
		

		new Thread( new Runnable() {
			
			public void run()  {
			double time0 = System.currentTimeMillis();
			while(true)
			{
				double time = System.currentTimeMillis();
				double deltaTime = time - time0;
				
				if(deltaTime >= FINAL_TIME)
				{
					break;
				}
			}
			fenetreGagnant = new FenetreGagnant(callback, winner);
			}
		}).start();
	}
}