package server;

import java.util.LinkedList;

import common.communication.ActionPack;
import common.communication.SyncPack;
import server.communication.CommsHandler;
import server.environment.EnvironmentHandler;
import server.inteligence.InteligenceBrain;


public class LaunchServer 
{
	private CommsHandler commsHandler;
	private EnvironmentHandler environmentHandler;
	
	public LaunchServer()
	{	
		int port = 8000;
		commsHandler = new CommsHandler(port,this);
		commsHandler.start();
	}
	
	public EnvironmentHandler getEnvironmentHandler() {return environmentHandler;}
	public CommsHandler getCommsHandler() {return commsHandler;}
	
	public void launchGame(LinkedList<String> playerList)
	{
		environmentHandler = new EnvironmentHandler(this);
		environmentHandler.startEnvironnment();
		for(String s : playerList)
		{
			environmentHandler.connectPlayer(s);
		}
		commsHandler.startUpdateTimer();
	}

	public void killGameEnvironment() 
	{
		commsHandler.stopUpdateTimer();
		commsHandler.generateSyncPack();
		environmentHandler.kill();
	}
}
