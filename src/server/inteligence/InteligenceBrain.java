package server.inteligence;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.Timer;

import common.communication.PlayerAction;
import common.environment.ActionHandler;
import common.environment.Player;
import server.environment.EnvironmentHandler;

public class InteligenceBrain
{

	private ArrayBlockingQueue<PlayerBot> bots;
	private EnvironmentHandler callback;
	private ArrayList<Player> playerMap;
	
	private UpdateThread updateThread;
	private int updateRate = 20;
	
	private static int botCount = 0;
	
	
	public InteligenceBrain(EnvironmentHandler callback)
	{
		bots = new ArrayBlockingQueue<PlayerBot>(12);
		this.callback = callback;
		updateThread = new UpdateThread(this,updateRate);
		updateThread.start();
	}
	
	public EnvironmentHandler getEnvironmentHandler() {return callback;}
	public  ArrayList<PlayerBot> getBots()
	{
		ArrayList<PlayerBot> arrayTemp = new ArrayList<PlayerBot>(12);
		for(PlayerBot p:bots)
		{
			arrayTemp.add(p);
		}
		return arrayTemp;
	}
	
	public void createBot(int botX,int botY)
	{
		PlayerBot pb = new PlayerBot(this,"bot",botX,botY,50,50,botCount);
		bots.add(pb);
		callback.connectPlayer(pb);
		botCount++;
	}
	
	public void updateBotAction()
	{
		for(PlayerBot p : bots)
		{
			p.updateAction();
		}
	}
	
	public void killAll()
	{
		updateThread.kill();
	}
}
