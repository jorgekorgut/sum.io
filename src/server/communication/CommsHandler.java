package server.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

import common.communication.ActionPack;
import common.communication.LobbyPack;
import common.communication.SyncPack;
import common.environment.Player;
import server.LaunchServer;
import server.intelligence.PlayerBot;

/*
 *  CommsHandler receive all the Packs from the client and manage to send them to their specific classes.
 *  In addiction, this class prepare the data to send to all clients.
 */

public class CommsHandler extends Thread
{
	private LaunchServer callback;
	private ServerSocket serverSocket;
	private int port;
	private LinkedList<ClientConnexion> connexions;
	private LinkedList<SendHandler> sendHandler;
	
	private UpdateThread updateThread;
	private int updateRate = 10;
	
	private LobbyPack lobbyPack;
	
	private boolean exit = false;
	
	public CommsHandler(int port, LaunchServer callback)
	{
		this.port = port;
		this.callback = callback;
		lobbyPack = new LobbyPack();
		
	}
	
	public void startUpdateTimer()
	{
		updateThread = new UpdateThread(this, updateRate);
		updateThread.start();
	}
	
	public void stopUpdateTimer()
	{
		if(updateThread!= null)
		{
			updateThread.kill();
		}
	}
	
	public void run()
	{
		try
		{
			serverSocket = new ServerSocket(port);
			connexions = new LinkedList<ClientConnexion>();
			sendHandler = new LinkedList<SendHandler>();
			
			while(!exit)
			{
				System.out.println("Waiting for connexions ...");
				Socket clientSocket = serverSocket.accept(); // blocked until receive something
				
				//Connexion accepted.
				String clientIP = clientSocket.getRemoteSocketAddress().toString();
				System.out.println (" New connectionAccepted from:  " + clientIP);
				
				connexions.add(new ClientConnexion(clientSocket,clientIP,this));
				sendHandler.add(new SendHandler(clientSocket,this));
			
			}
		}
		catch(IOException e)
		{
			System.out.println("Problem to launch the server.");
			System.out.println(e);
			System.exit(1);
		}
	}
	
	
	public void remove(ClientConnexion cnx)
	{
		int index = connexions.indexOf(cnx);
		
		connexions.remove(index);
		sendHandler.remove(index);
	}
	
	//FIXME: We can merge sendSyncPack and sendLobbyPack into an unique method.
	public void sendSyncPack(SyncPack sPack)
	{
		for(SendHandler sHandler : sendHandler)
		{
			sHandler.sendToClient(sPack);
		}
	}
	
	public void sendLobbyPack(LobbyPack lPack)
	{
		for(SendHandler sHandler : sendHandler)
		{
			sHandler.sendToClient(lPack);
		}
	}
	
	public void generateSyncPack()
	{
		if(callback.getEnvironmentHandler() == null)
		{
			return;
		}
		SyncPack sPack = callback.getEnvironmentHandler().getSyncPack();
		
		//We create a new player to avoid serializing things that don't need to be.
		//(to avoid serialize an instance of player bot)
		
		SyncPack newSPack = new SyncPack(sPack);
		ArrayList<Player> playerMap = sPack.getPlayerMap();
		ArrayList<Player> onlyPlayerInstance = new ArrayList<Player>();
		
		if(sPack.getPlayer() instanceof PlayerBot)
		{
			Player p = sPack.getPlayer();
			newSPack.setPersonalPlayer(new Player(p.getName(),p.getX(),p.getY(),p.getWidth(),p.getHeight(),p.getPlayerIP(),p.getRenderingPriority(),p.isAwake(),p.getBoostQuantity(),p.getFlagCollision()));
		}
		
		for(Player p : playerMap)
		{
			if(p instanceof PlayerBot)
			{
				onlyPlayerInstance.add(new Player(p.getName(),p.getX(),p.getY(),p.getWidth(),p.getHeight(),p.getPlayerIP(),p.getRenderingPriority(),p.isAwake(),p.getBoostQuantity(),p.getFlagCollision()));
				continue;
			}
			onlyPlayerInstance.add(p);
		}
		newSPack.setPlayerMap(onlyPlayerInstance);
		sendSyncPack(newSPack);
	}

	public void actionPackReceived(ActionPack aPack)
	{
		callback.getEnvironmentHandler().doPlayerAction(aPack);
	}
	
	public void lobbyPackReceived(LobbyPack lPack)
	{
		if(lPack.getPlayer()!= null)
		{
			lobbyPack.addToPlayerList(lPack.getPlayer());
		}
		if(lPack.getPlayerSkin() != null)
		{
			lobbyPack.addSkinPlayerToList(lPack.getPlayerSkin());
		}
		
		lobbyPack.setStartFlag(lPack.getStartFlag());
		sendLobbyPack(lobbyPack);
	}
}
