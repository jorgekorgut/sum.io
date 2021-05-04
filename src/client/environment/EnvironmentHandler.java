package client.environment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

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
import client.lobby.LobbyHandler;

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
	
	private int playersCount = 0;
	
	public EnvironmentHandler(MainClient callback)
	{
		playerMap = new ArrayList<Player>();
		interactableObjects = new ArrayList<GameObject>();
		this.callback = callback;
	}

	public int getRemainingPlayers(){return playersCount;}
	public Player getPlayerClient() {return player;}
	public ArrayList<Player> getPlayerMap(){return playerMap;}
	
	private void updateEnvironment()
	{	
		for(Player p: playerMap)
		{
			if(p.isAwake() && p.getFlagCollision())
			{
				callback.getEngineHandler().getAnimationHandler().onColision(p,player);
			}
		}
		callback.getEngineHandler().getAnimationHandler().update();
		callback.getEngineHandler().getUserInterface().update();
	}
	
	private void onEnvironmentStart()
	{
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
			
			for(Player p: playerMap)
			{
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
		for(Player p : playerMap)
		{
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

		
		if(sPack.getWinner() != null)
		{
			onGameFinished(sPack.getWinner());
			return;
		}
	
		updateEnvironment();	
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
			callback.returnLobby();
			}
		}).start();
	}

	public void killAll() 
	{
		interactableObjects.clear();
		playerMap.clear();
		
		playerMap = null;
		interactableObjects = null;
		platform = null;
		player = null;
		callback = null;
	}
}
