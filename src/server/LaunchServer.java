package server;

import java.util.ArrayList;
import java.util.LinkedList;

import server.communication.CommsHandler;
import server.environment.EnvironmentHandler;

public class LaunchServer 
{
	private CommsHandler commsHandler;
	private EnvironmentHandler environmentHandler;
	
	public LaunchServer()
	{	
		//FIXME: Choose the port in the start window.
		int port = 8000;
		commsHandler = new CommsHandler(port,this);
		commsHandler.start();
	}
	
	public EnvironmentHandler getEnvironmentHandler() {return environmentHandler;}
	public CommsHandler getCommsHandler() {return commsHandler;}
	
	public void launchGame(LinkedList<String> playerList, ArrayList<String> playerSkinList)
	{
		environmentHandler = new EnvironmentHandler(this);
		environmentHandler.startEnvironnment();
		int index = 0 ;
		for(String s : playerList)
		{
			environmentHandler.connectPlayer(s,playerSkinList.get(index));
			index ++;
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
