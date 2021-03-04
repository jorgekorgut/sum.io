package common.communication;

import java.util.ArrayList;

import common.environment.GameObject;

public class SyncPack implements java.io.Serializable
{
	private ArrayList<GameObject> playerMap;
	public SyncPack(ArrayList<GameObject> playerMap)
	{
		this.playerMap = (ArrayList<GameObject>)playerMap.clone();
	}
	
	public ArrayList<GameObject> getPlayerMap() {return playerMap;}
}
