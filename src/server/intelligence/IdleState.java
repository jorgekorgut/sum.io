package server.intelligence;

import java.util.ArrayList;
import java.util.LinkedList;

import common.environment.GameObject;
import common.environment.Player;
import server.environment.PatrolPoint;

/*
 * This class represents the idle of the bot.
 * The preference of states that the bot will choose are:
 * 		- search for boost
 * 		- search for a player
 * 		- it will patrol (searching for patrolPoints random spawned)
 */

public class IdleState extends AbstractFSMState
{
	
	public IdleState(PlayerBot player)
	{
		super(player);
	}
	
	@Override
	public void updateState() 
	{
		//FIXME: is it correct ?
		//Change the state of the ui
		ArrayList<Player> playerMap = player.getInteligenceHandler().getEnvironmentHandler().getPlayerMap();
		ArrayList<GameObject> interactableObjects = player.getInteligenceHandler().getEnvironmentHandler().getInteractableObjects();
		LinkedList<PatrolPoint> patrolPoints = player.getInteligenceHandler().getEnvironmentHandler().getPatrolPoints();
		
		
		this.exitState();
		double squareMaxDist = Math.pow(player.getBotViewRange(), 2);
		
		double min = Integer.MAX_VALUE;
		//Searching for boost if it is necessary
		if(player.getBoostQuantity()<50)
		{
			GameObject boost = null;
			min = 5000000;
			
			//TODO: Changed to normal for due to ConcurrentModificationException
			for(int i = 0 ; i< interactableObjects.size(); i++)
				{
					GameObject g = interactableObjects.get(i);
					//Test to search for a player that is not himself.
					if(!player.equals(g) && g.isAwake())
					{
						double squareDist = player.squareDistanceTo(g);
						if(squareDist < squareMaxDist && min > squareDist)
						{
							boost = g;
							min = squareDist;
						}
					}
			}
			
			if(boost!=null)
			{
				PatrolState sState = new PatrolState(player,boost);
				this.exitState();
				player.getFiniteStateMachine().enterState(sState);
				return;
			}
		}
		
		//Searching a player
		Player target= null;
		for(int i = 0; i<playerMap.size(); i++)
		{
			Player p = playerMap.get(i);
			min = 5000000;
			//Test to search for a player that is not himself.
			if(!player.equals(p) && p.isAwake())
			{
				
				double squareDist = player.squareDistanceTo(p);
				if(squareDist < squareMaxDist && min > squareDist)
				{
					target = p;
					min = squareDist;
				}
			}
		}
		if(target!=null)
		{
			FollowState fState = new FollowState(player,target);
			this.exitState();
			player.getFiniteStateMachine().enterState(fState);
			return;
		}
		
		
		PatrolPoint patrolPoint = null;
		if(!patrolPoints.isEmpty())
		{
			int index =0;
			patrolPoint = patrolPoints.get(index);
			while(index < patrolPoints.size() && !patrolPoint.isAwake())
			{
				patrolPoint = patrolPoints.get(index);
				index++;
			}
			
		}
		
		if(patrolPoint != null)
		{
			PatrolState sState = new PatrolState(player,patrolPoint);
			this.exitState();
			player.getFiniteStateMachine().enterState(sState);
			return;
		}
		
	}
}
