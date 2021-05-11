package server.intelligence;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import server.environment.EnvironmentHandler;

/*
 * This class holds all the instances of the bots and updates its states.
 */

public class IntelligenceHandler
{

	private ArrayBlockingQueue<PlayerBot> bots;
	private EnvironmentHandler callback;
	
	private UpdateThread updateThread;
	private int updateRate = 20;
	
	public IntelligenceHandler(EnvironmentHandler callback)
	{
		bots = new ArrayBlockingQueue<PlayerBot>(12);
		this.callback = callback;
		updateThread = new UpdateThread(this,updateRate);
		updateThread.start();
	}
	
	public EnvironmentHandler getEnvironmentHandler() {return callback;}
	//FIXME: is it necessary ?
	public  ArrayList<PlayerBot> getBots()
	{
		ArrayList<PlayerBot> arrayTemp = new ArrayList<PlayerBot>(12);
		for(PlayerBot p:bots)
		{
			arrayTemp.add(p);
		}
		return arrayTemp;
	}
	
	public void createBot(int botX,int botY, String botName)
	{
		PlayerBot pb = new PlayerBot(this,botName,botX,botY,50,50);
		bots.add(pb);
		callback.connectPlayer(pb);
	}
	
	public void update()
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
