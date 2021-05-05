package client.communication;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import client.engine.InputHandler;
import common.communication.ActionPack;
import common.communication.LobbyPack;
import common.communication.SyncPack;

public class Network {

	//use to call the client for updates
	private CommsHandler callback;
	private Socket socket;
	private ObjectOutputStream objectOutput = null;
	private ListeningThread listenerThread;
	
	public Network(CommsHandler callback)
	{
		this.callback = callback;
	}
	
	public void connect(String ip, int port)
	{
		 try 
		 {
			 socket = new Socket(ip,port);
			 listenerThread = new ListeningThread(socket, callback, this);
			 objectOutput = new ObjectOutputStream(socket.getOutputStream());
		 }
		 catch (UnknownHostException e) 
		 {
			 System.err.println("Unknown host: " + ip);
			 System.exit(1);
		 }
		 catch(IOException e)
		 {
			 e.printStackTrace();
	         System.exit(1);
		 }
	}
	
	public void close()
	{
		try
		{
			objectOutput.close();
			socket.close();	
		}
		catch(IOException e)
		{
			 e.printStackTrace();
	         System.exit(1);
		}
	}

	public void sendPack(Object pack) 
	{
		if(socket.isClosed())
		{
			System.out.println("NetWork: Socket is closed");
			return;
		}
		
		if(pack instanceof LobbyPack)
		{
			pack = new LobbyPack((LobbyPack)pack);
		}
		if(pack instanceof SyncPack)
		{
			pack = new SyncPack((SyncPack)pack);
		}
		if(pack instanceof ActionPack)
		{
			pack = new ActionPack((ActionPack)pack);
		}
		
		try 
		{
			//FIXME: Problem with the array that doesn't actualize when it is serialized, fixed by adding a new LobbyPack that is a clone from the before.
			if(pack != null)
			{
				objectOutput.writeUnshared(pack);
			}
			
		}
		catch(IOException e)
		{
			e.printStackTrace();
			close();
		}
	}
}
