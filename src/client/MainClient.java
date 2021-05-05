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
	}
	
	public EnvironmentHandler getEnvironmentHandler() {return environmentHandler;}
	public ImageCache getImageCache() {return imageCache;}
	public CommsHandler getCommsHandler() {return commsHandler;}
	public EngineHandler getEngineHandler() {return engineHandler;}
	public LobbyHandler getLobbyHandler() {return lobbyHandler;}
	public LaunchServer getLaunchServer() {return launchServer;}
	public void setLobbyHandler(LobbyHandler lobbyHandler) {this.lobbyHandler = lobbyHandler;}
	public AudioMaster getAudioMaster() {return audioMaster;}
	

	public void onCreateServer(String playerName,String skinName, String ip,int port)
	{
		launchServer = new LaunchServer();
		connectPlayer(playerName,skinName,ip,port);
	}
	
	public void onConnectServer(String playerName,String skinName, String ip,int port)
	{
		connectPlayer(playerName,skinName,ip,port);
	}
	
	public void connectPlayer(String playerName,String skinName, String ip,int port)
	{
		audioMaster = new AudioMaster();
		commsHandler = new CommsHandler(ip,port,this);
		lobbyHandler = new LobbyHandler(this,debutJeu);
		
		lobbyHandler.onPlayerConnectServer(playerName,skinName);
	}
	
	public void returnLobby()
	{	
		engineHandler.killAll();
		String playerName = lobbyHandler.getPlayer();
		String playerSkin = lobbyHandler.getPlayerSkin();
		lobbyHandler = new LobbyHandler(this,engineHandler.getWindow().getJFrame());
		lobbyHandler.setPlayer(playerName);
		lobbyHandler.setPlayerSkin(playerSkin);
		commsHandler.sendPack(new LobbyPack());
	}
	
	public void launchGame()
	{
		//FIXME: Find a better way
		if(launchServer != null)
		{
			launchServer.launchGame(lobbyHandler.getLobbyPack().getPlayerList(), lobbyHandler.getLobbyPack().getPlayerSkinList());
		}
		engineHandler = new EngineHandler(this,lobbyHandler.getJFrame());
		environmentHandler = new EnvironmentHandler(this);
	}
	
}
