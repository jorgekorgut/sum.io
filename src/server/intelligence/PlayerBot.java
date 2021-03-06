package server.intelligence;

import java.util.LinkedList;

import common.communication.ActionPack;
import common.communication.PlayerAction;
import common.environment.ActionHandler;
import common.environment.Player;
import server.environment.EnvironmentHandler;

/*
 * This class creates a bot to act like a player.
 */

public class PlayerBot extends Player
{
	private ActionPack aPack;
	private FiniteStateMachine finiteStateMachine;
	private IntelligenceHandler intelligenceHandler;
	private int botViewRange;
	
	public PlayerBot(IntelligenceHandler intelligenceHandler, String name, int x, int y, int width,int height)
	{
		super("bot",x,y,width,height,name,EnvironmentHandler.PRIORITYRENDER_PLAYER);
		aPack = new ActionPack(this);
		this.intelligenceHandler = intelligenceHandler;
		finiteStateMachine = new FiniteStateMachine(new IdleState(this));
		
		botViewRange = (int) (Math.random()*200 + 400); // min 400 max 600
	}
	
	public int getBotViewRange() {return botViewRange;}
	public ActionPack getActionPack() {return aPack;}
	public void setActionPack(ActionPack aPack) {this.aPack = aPack;}
	public FiniteStateMachine getFiniteStateMachine() {return finiteStateMachine;}
	public IntelligenceHandler getInteligenceHandler() {return intelligenceHandler;}

	public void updateAction()
	{
		finiteStateMachine.update();
	}
	
	public void moveTo(double directionX,double directionY)
	{	
		ActionPack aPack = this.getActionPack();
		LinkedList<PlayerAction> playerActionList = aPack.getPlayerActionList();	
		
		if(directionX > 0)
		{
			if(playerActionList.contains(PlayerAction.MOVE_LEFT))
			{
				aPack.updateAction(PlayerAction.MOVE_LEFT);
			}
			if(!playerActionList.contains(PlayerAction.MOVE_RIGHT))
			{
				aPack.updateAction(PlayerAction.MOVE_RIGHT);
			}
		}
		else if(directionX < 0)
		{
			if(playerActionList.contains(PlayerAction.MOVE_RIGHT))
			{
				aPack.updateAction(PlayerAction.MOVE_RIGHT);
			}
			
			if(!playerActionList.contains(PlayerAction.MOVE_LEFT))
			{
				aPack.updateAction(PlayerAction.MOVE_LEFT);
			}
		}
		
		if(directionY < 0)
		{
			if(playerActionList.contains(PlayerAction.MOVE_DOWN))
			{
				aPack.updateAction(PlayerAction.MOVE_DOWN);
			}
			
			if(!playerActionList.contains(PlayerAction.MOVE_UP))
			{
				aPack.updateAction(PlayerAction.MOVE_UP);
			}
		}
		else if(directionY > 0)
		{
			if(playerActionList.contains(PlayerAction.MOVE_UP))
			{
				aPack.updateAction(PlayerAction.MOVE_UP);
			}
			if(!playerActionList.contains(PlayerAction.MOVE_DOWN))
			{
				aPack.updateAction(PlayerAction.MOVE_DOWN);
			}
		}
		ActionHandler.doPlayerAction(aPack);
	}
}
