package common.communication;

import java.util.ArrayList;
import common.environment.GameObject;
import common.environment.Platform;
import common.environment.Player;

/*
 * This class is responsible to send to the client the informations necessary to 
 * synchronize the server environment.
 */

public class SyncPack implements java.io.Serializable
{
	private ArrayList<Player> playerMap;
	private ArrayList<GameObject> interactableObjects;
	private Player player; 
	private Platform platform;
	
	private int playersCount;
	
	private String winner;
	
	public SyncPack()
	{
		playerMap = new ArrayList<Player>();
		interactableObjects = new ArrayList<GameObject>();
	}
	
	public SyncPack(SyncPack sPack)
	{
		this.platform = sPack.getPlatform();
		this.player = new Player(sPack.getPlayer());
		this.playersCount = sPack.getPlayersCount();
		this.playerMap = (ArrayList<Player>) sPack.getPlayerMap().clone();
		this.interactableObjects = (ArrayList<GameObject>) sPack.getInteractableObjects().clone();
		this.setWinner(sPack.getWinner());
	}
	
	public String getWinner() {return this.winner;}

	public void setPlayersRemainingCount(int count) {this.playersCount = count;}
	
	public ArrayList<Player> getPlayerMap() {return playerMap;}
	public Player getPlayer() {return player;}
	public Platform getPlatform() {return platform;}
	public int getPlayersCount() {return playersCount;}
	public ArrayList<GameObject> getInteractableObjects() {return interactableObjects;}
	
	public void setPersonalPlayer(Player player)
	{
		this.player = player;
	}
	
	public void setPlatform(Platform platform) 
	{
		this.platform = platform;
	}

	public void setPlayerMap(ArrayList<Player> onlyPlayerInstance) 
	{
		playerMap = onlyPlayerInstance;
	}
	
	public void setInteractableObject(ArrayList<GameObject> interactableObjects) 
	{
		this.interactableObjects = interactableObjects;
	}

	public void addInteractableObject(GameObject interactableObjects) 
	{
		this.interactableObjects.add(interactableObjects);
	}

	public void setWinner(String winner) 
	{
		this.winner = winner;
	}
}
