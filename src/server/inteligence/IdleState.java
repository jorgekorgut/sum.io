package server.inteligence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import common.environment.GameObject;
import common.environment.PatrolPoint;
import common.environment.Player;

public class IdleState extends AbstractFSMState
{
	
	public IdleState(PlayerBot player)
	{
		super(player);
	}
	
	@Override
	public void updateState() 
	{
		//Change the state of the ui
		ArrayList<Player> playerMap = player.getInteligenceBrain().getEnvironmentHandler().getPlayerMap();
		ArrayList<GameObject> interactableObjects = player.getInteligenceBrain().getEnvironmentHandler().getInteractableObjects();
		LinkedList<PatrolPoint> patrolPoints = player.getInteligenceBrain().getEnvironmentHandler().getPatrolPoints();
		
		
		this.exitState();
		double squareMaxDist = Math.pow(player.getBotViewRange(), 2);
		
		//FIXME: HardCoded
		double min = 5000000;
		//Serching for boost if it is necessary
		if(player.getBoostQuantity()<50)
		{
			GameObject boost = null;
			min = 5000000;
			
			//Changed to normal for due to ConcurrentModificationException
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
				SearchState sState = new SearchState(player,boost);
				this.exitState();
				player.getFiniteStateMachine().enterState(sState);
				return;
			}
		}
		
		//Searching a player
		Player target= null;
		for(Player p : playerMap)
		{
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
			SearchState sState = new SearchState(player,patrolPoint);
			this.exitState();
			player.getFiniteStateMachine().enterState(sState);
			return;
		}
		
	}
}
