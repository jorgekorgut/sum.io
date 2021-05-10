package common.communication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

/*
 * This class compact the informations to synchronize the client with lobby server.
 */

public class LobbyPack implements Serializable
{
	private LinkedList<String> playerList;
	private ArrayList<String> playerSkinList;
	private String player;
	private String skinPlayer;
	
	private boolean startFlag = false;
	public LobbyPack ()
	{
		playerList = new LinkedList<String>();
		playerSkinList = new ArrayList<String>(12);
	}
	
	public LobbyPack(LobbyPack lPack)
	{
		this.playerList = (LinkedList<String>) lPack.getPlayerList().clone();
		this.playerSkinList = (ArrayList<String>) lPack.getPlayerSkinList().clone();
		this.player = lPack.getPlayer();
		this.skinPlayer =  lPack.getPlayerSkin();
		this.startFlag = lPack.getStartFlag();
	}
	
	public void addToPlayerList(String playerName)
	{
		this.playerList.add(playerName);
	}
	public void addSkinPlayerToList(String playerSkin)
	{
		this.playerSkinList.add(playerSkin);
	}
	
	public LinkedList<String> getPlayerList(){return playerList;}
	public String getPlayer() {return player;}
	public boolean getStartFlag() {return startFlag;}
	public ArrayList<String> getPlayerSkinList(){return playerSkinList;}
	public void setStartFlag(boolean startFlag) {this.startFlag = startFlag;}
	public String getPlayerSkin() {return skinPlayer;}
	
	public void addPlayer(String name) 
	{
		player = name;
	}
	
	public void addPlayerSkin(String skinPlayer)
	{
		this.skinPlayer = skinPlayer;
	}
}
