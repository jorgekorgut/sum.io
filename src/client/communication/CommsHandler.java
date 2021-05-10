package client.communication;

import client.*;
import client.environment.EnvironmentHandler;
import client.lobby.LobbyHandler;
import common.communication.SyncPack;
import common.communication.ActionPack;
import common.communication.LobbyPack;

/*
 * This class is responsible to create a Network and controls information flux received and sent
 */

public class CommsHandler {
	
	private String serverIP;
	private int serverPort;
	private Network network;
	private MainClient callback;
	
	public CommsHandler(String serverIP, int serverPort, MainClient callback)
	{
		this.serverIP= serverIP;
		this.serverPort = serverPort;
		this.callback = callback;
		
		//TODO: Visual to this information
		System.out.println("Connecting to: "+ serverIP + ":" + serverPort);
		network = new Network(this);
	}
	
	public MainClient getMainClient() {return callback;}
	
	public void receiveSyncPack(SyncPack syncPack)
	{		
		EnvironmentHandler evironmentHandler = callback.getEnvironmentHandler();
		if(evironmentHandler != null)
		{
			evironmentHandler.syncClient(syncPack);
		}
	}
	
	public void receiveLobbyPack(LobbyPack lPack)
	{
		LobbyHandler lobbyHandler = callback.getLobbyHandler();
		
		if(lobbyHandler != null)
		{
			lobbyHandler.syncLobby(lPack);
		}
	}
	
	public void connectNetwork()
	{
		network.connect(serverIP,serverPort);
	}
	
	public void sendPack(Object pack)
	{
		network.sendPack(pack);
	}
}

