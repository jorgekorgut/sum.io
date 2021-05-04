package server.inteligence;

import java.util.ArrayList;

import common.environment.Player;

public class CentralizeState extends AbstractFSMState
{

	public CentralizeState(PlayerBot player) 
	{
		super(player);
	}

	@Override
	public void updateState() 
	{
		//Centralize
		double squareMaxDist = Math.pow(player.getBotViewRange(), 2);
		
		double wishPosX = 0 ;
		double wishPosY = 0 ;
		
		double directionX = wishPosX - player.getX();
		double directionY = wishPosY - player.getY();
		
		if(Math.pow(player.getX(),2)+Math.pow(player.getY(), 2)<Math.pow(200, 2))
		{
			this.exitState();
			player.getFiniteStateMachine().enterState(new IdleState(player));
		}
		
		player.moveTo(directionX,directionY);
		
	}

}
