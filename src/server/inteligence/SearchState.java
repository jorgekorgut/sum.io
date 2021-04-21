package server.inteligence;

import common.environment.GameObject;

public class SearchState extends AbstractFSMState
{
	private GameObject target;
	
	public SearchState(PlayerBot player, GameObject target) 
	{
		super(player);
		this.target = target;
	}

	@Override
	public void updateState() 
	{
		double wishPosX = target.getX();
		double wishPosY = target.getY();
		
		double directionX = wishPosX - player.getX();
		double directionY = wishPosY - player.getY();
		
		player.moveTo(directionX,directionY);
		
		if(!target.isAwake()|| target == null)
		{
			target = null;
			this.exitState();
			player.getFiniteStateMachine().enterState(new IdleState(player));
			return;
		}
	}
}
