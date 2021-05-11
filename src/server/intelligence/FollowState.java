package server.intelligence;

import common.environment.Player;

/*
 * This class follows the player and start to centralize (changes of state) when the bot is close to the border.
 */

public class FollowState extends AbstractFSMState
{
	
	private Player target;
	
	public FollowState(PlayerBot player,Player target)
	{
		super(player);
		this.target = target;
	}
	
	public void setTarget(Player target){}
	
	@Override
	public boolean enterState()
	{
		boolean base = super.enterState();
		
		if(target == null)
		{
			return false;
		}
		return base;
	}
	
	@Override
	public boolean exitState()
	{
		boolean base = super.exitState();
		target = null;
		player.getFiniteStateMachine().enterState(new IdleState(player));
		return base;
	}
	
	@Override
	public void updateState() 
	{
		//aPack.reset();
		double wishPosX = target.getX() + target.getSpeedX();
		double wishPosY = target.getY() + target.getSpeedY();
		
		double directionX = wishPosX - player.getX();
		double directionY = wishPosY - player.getY();
		
		player.moveTo(directionX,directionY);
		
		int attackRange = 100;
		
		//FIXME: HardCoded
		if(Math.pow(player.getX(),2)+Math.pow(player.getY(), 2)>Math.pow(700,2))
		{
			player.getFiniteStateMachine().enterState(new CentralizeState(player));
			target = null;
			return;
		}
		
		if(player.squareDistanceTo(target) < Math.pow(attackRange, 2))
		{
			player.attackBoost();
		}
		//Lose the target when mission accomplished
		if(!target.isAwake() || target == null)
		{
			this.exitState();	
			return;
		}
		
		//Lose the player when the bot is following him
		if(player.squareDistanceTo(target)>Math.pow(player.getBotViewRange(),2))
		{
			this.exitState();
			return;
		}
	}

}
