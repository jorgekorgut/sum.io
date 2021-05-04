package server.inteligence;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.Timer;

import common.communication.PlayerAction;
import common.environment.ActionHandler;
import common.environment.Player;
import server.environment.EnvironmentHandler;

public class InteligenceBrain
{

	private ArrayList<PlayerBot> bots;
	private EnvironmentHandler callback;
	private ArrayList<Player> playerMap;
	
	private UpdateThread updateThread;
	private int updateRate = 20;
	
	private static int botCount = 0;
	
	
	public InteligenceBrain(EnvironmentHandler callback)
	{
		bots = new ArrayList<PlayerBot>();
		this.callback = callback;
		updateThread = new UpdateThread(this,updateRate);
		updateThread.start();
	}
	
	public EnvironmentHandler getEnvironmentHandler() {return callback;}
	public  ArrayList<PlayerBot> getBots(){return bots;}
	
	public void createBot(int botX,int botY)
	{
		PlayerBot pb = new PlayerBot(this,"bot",botX,botY,50,50,botCount);
		bots.add(pb);
		callback.connectPlayer(pb);
		botCount++;
	}
	
	public void updateBotAction()
	{
		for(PlayerBot p : bots )
		{
			p.updateAction();
		}
	}
	
	public void killAll()
	{
		updateThread.kill();
	}
}
