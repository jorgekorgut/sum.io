package server.communication;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import common.communication.ActionPack;
import common.communication.LobbyPack;
import common.communication.SyncPack;

public class SendHandler{
	
	private Socket socket;
	private ObjectOutputStream toClient;
	private CommsHandler callback;
	
	public SendHandler(Socket socket,CommsHandler callback)
	{
		this.callback = callback;
		
		this.socket = socket;
		try
		{
			toClient = new ObjectOutputStream(socket.getOutputStream());
		}
		catch(Exception e)
		{
			System.out.println("SendHandler problem!");
			e.printStackTrace();
		}
	}
	
	public void sendToClient(Object pack)
	{
		if(socket.isClosed())
		{
			System.out.println("SendHandler: socket is closed!");
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
			if(pack!= null)
			{
				synchronized(callback)
				{
					toClient.reset();
					toClient.writeObject(pack);
				}
			} 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void close()
	{
		try
		{
			if(socket !=null)
			{
				socket.close();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
