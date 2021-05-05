package common.communication;

import java.util.LinkedList;

import common.environment.Player;

public class ActionPack implements java.io.Serializable
{
	private LinkedList<PlayerAction> playerActionList = null;
	private Player player;
	
	public ActionPack(Player player)
	{
		playerActionList = new LinkedList<PlayerAction>();
		this.player = player;
	}
	
	public ActionPack(ActionPack aPack)
	{
		this.playerActionList = (LinkedList<PlayerAction>) aPack.getPlayerActionList().clone();
		this.player = new Player(aPack.getPlayer());
	}
	
	public Player getPlayer() {return player;}
	public LinkedList<PlayerAction> getPlayerActionList(){return playerActionList;}
	
	public void setPlayer(Player player) {this.player = player;}
	
	//If we add the same PlayerAction two times we remove him from the keyboard
	public void updateAction(PlayerAction action)
	{
		if(!playerActionList.contains(action))
		{
			playerActionList.add(action);
		}
		else
		{
			playerActionList.remove(action);
		}
	}
	
	public String toString()
	{
		String res ="";
		res+= playerActionList.toString();
		return res;
	}
	
	public void reset()
	{
		playerActionList.clear();
	}
	
	public boolean isEmpty() 
	{	
		return this.playerActionList.isEmpty();
	}
}
