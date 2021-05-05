package client;

import client.communication.CommsHandler;
import client.engine.EngineHandler;
import client.engine.ImageCache;
import client.environment.EnvironmentHandler;
import client.lobby.LobbyHandler;
import client.sound.AudioMaster;
import client.startmenu.DebutJeu;
import common.communication.LobbyPack;
import server.LaunchServer;

public class MainClient 
{
	public static void main(String[] args) 
	{
		/*
		if(args.length != 3)
		{
			System.out.println("Please enter the IP to the server (yours) followed by the port (if you didn't change it is 8000)");
			System.out.println("For example: java localhost 8000");
			return;
		}*/
		
		new MainClient();
	}
	/*
	 * ATRIBUTS
	 */
	private CommsHandler commsHandler;
	private EngineHandler engineHandler;
	private EnvironmentHandler environmentHandler;
	private LobbyHandler lobbyHandler;
	private LaunchServer launchServer;
	private ImageCache imageCache;
	private AudioMaster audioMaster;
	private DebutJeu debutJeu;
	
	public MainClient()
	{
		imageCache = new ImageCache();
		debutJeu = new DebutJeu(this);
		
		/*
		switch(args[2])
		{
		case "1":
			playerName = "loslo";
			onCreateServer(playerName,ip,port);
			break;
		case "2":
			playerName = "loslo2";
			onConnectServer(playerName, ip, port);
			break;
		case "3":
			playerName = "loslo3";
			onConnectServer(playerName, ip, port);
			break;
		}
		*/
	}
	
	public EnvironmentHandler getEnvironmentHandler() {return environmentHandler;}
	public ImageCache getImageCache() {return imageCache;}
	public CommsHandler getCommsHandler() {return commsHandler;}
	public EngineHandler getEngineHandler() {return engineHandler;}
	public LobbyHandler getLobbyHandler() {return lobbyHandler;}
	public LaunchServer getLaunchServer() {return launchServer;}
	public void setLobbyHandler(LobbyHandler lobbyHandler) {this.lobbyHandler = lobbyHandler;}
	public AudioMaster getAudioMaster() {return audioMaster;}
	

	public void onCreateServer(String playerName, String ip,int port)
	{
		launchServer = new LaunchServer();
		connectPlayer(playerName,ip,port);
	}
	
	public void onConnectServer(String playerName, String ip,int port)
	{
		connectPlayer(playerName,ip,port);
	}
	
	public void connectPlayer(String playerName, String ip,int port)
	{
		audioMaster = new AudioMaster();
		commsHandler = new CommsHandler(ip,port,this);
		lobbyHandler = new LobbyHandler(this,debutJeu);
		lobbyHandler.onPlayerConnectServer(playerName);
	}
	
	public void returnLobby()
	{	
		engineHandler.killAll();
		environmentHandler.killAll();
		String playerName = lobbyHandler.getPlayer();
		lobbyHandler = new LobbyHandler(this,engineHandler.getWindow().getJFrame());
		lobbyHandler.setPlayer(playerName);
		commsHandler.sendLobbyPack(new LobbyPack());
	}
	
	public void launchGame()
	{
		//FIXME: Find a better way
		if(launchServer != null)
		{
			launchServer.launchGame(lobbyHandler.getLobbyPack().getPlayerList());
		}
		engineHandler = new EngineHandler(this,lobbyHandler.getJFrame());
		environmentHandler = new EnvironmentHandler(this);
	}
	
}
