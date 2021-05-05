package server.communication;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import common.communication.LobbyPack;
import common.communication.SyncPack;

public class SendHandler{
	
	private Socket socket;
	private ObjectOutputStream toClient;
	
	public SendHandler(Socket socket)
	{
		this.socket = socket;
		try
		{
			toClient = new ObjectOutputStream(socket.getOutputStream());
		}
		catch(Exception e)
		{
			//e.printStackTrace();
		}
	}
	
	public void sendToClient(Object obj)
	{
		try
		{
			toClient.reset();
			toClient.writeObject(obj);
			 // do not forget the reset, lose data from array list. Why ???
		}
		catch(Exception e){
		}
	}
	
	/*
	//TODO: Some updates to the client in a determinated time.
	public void sendSyncPack(SyncPack sPack)
	{
		try
		{
			toClient.reset();
			//toClient = new ObjectOutputStream(socket.getOutputStream());
			toClient.writeObject(sPack);
			 // do not forget the reset, lose data from array list. Why ???
		}
		catch(Exception e){
		}
	}
	
	public void sendLobbyPack(LobbyPack lPack)
	{
		try
		{
			toClient.reset();
			//toClient = new ObjectOutputStream(socket.getOutputStream());
			toClient.writeObject(lPack);
		}
		catch(Exception e){
		}
	}
	*/
	
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
			
		}
	}
}
