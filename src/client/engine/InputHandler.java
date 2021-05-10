package client.engine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import client.communication.CommsHandler;
import common.communication.ActionPack;
import common.communication.PlayerAction;
import common.environment.Player;

/*
 * This class is responsible to manage different inputs of the client,
 * Notice that the complex way implemented was to provide a base to other kind of players inputs.
 * For example, double space bar to teletransport ...
 */

public class InputHandler
{
	private EngineHandler callback;
	
	private static int NUMBER_OF_KEYS = 500;
	private boolean[] keyStatus;
	private Player himself;
	private ActionPack aPack;

	private UpdateThread updateThread;
	private int inputSpeedRate = 30;
	 
	public InputHandler (EngineHandler engineHandler)
	{
		keyStatus = new boolean[NUMBER_OF_KEYS];
		this.callback = engineHandler;
		aPack = new ActionPack(himself);
		updateThread = new UpdateThread(this, inputSpeedRate);
		updateThread.start();
	}
	
	public void setPlayer(Player player)
	{
		this.himself = player;
		aPack.setPlayer(player);
	}
	
	public void sendActionPack() 
	{
		if(aPack.isEmpty())
		{
			return;
		}
		callback.sendActionPack(aPack);
	}
	
	public void onKeyPressed(int keyId)
	{
		if(!keyStatus[keyId])
		{
			keyStatus[keyId]=true;
			updateActionPack(keyId);
		}
	}
	
	public void onKeyReleased(int keyId)
	{
		if(keyStatus[keyId])
		{
			keyStatus[keyId] = false;
			updateActionPack(keyId);
		}
	}
	
	private ActionPack updateActionPack(int keyId) 
	{
		switch(keyId)
		{
			case 32:
				aPack.updateAction(PlayerAction.ATTACK_BOOST);
			break;
			case 37:
				aPack.updateAction(PlayerAction.MOVE_LEFT);
			break;
			case 38:
				aPack.updateAction(PlayerAction.MOVE_UP);
			break;
			case 39:
				aPack.updateAction(PlayerAction.MOVE_RIGHT);
			break;
			case 40:
				aPack.updateAction(PlayerAction.MOVE_DOWN);
			break;
		}
		return aPack;
	}

	public void killAll() 
	{
		aPack.reset();
		updateThread.kill();
	}
}