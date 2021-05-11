package server.environment;

import common.environment.CircleCollider;
import common.environment.GameObject;
import common.environment.Player;

/*
 * This class is responsible for creating a target to the bot and do not let him static without move.
 */

public class PatrolPoint extends GameObject implements CircleCollider
{
	
	private boolean flagColision = false;
	
	public PatrolPoint(String name, double x, double y, int width, int height) 
	{
		super(name, x, y, width, height,1000);
	}
	@Override
	public boolean getFlagCollision() {return flagColision;}
	@Override
	public void setFlagCollision(boolean value) {flagColision = value;}
	
	@Override
	public double getX()
	{
		return x;
	}

	@Override
	public double getY() 
	{
		return y;
	}

	@Override
	public int getRadiusCollider() 
	{
		return width/2;
	}

	@Override
	public void onCollision(CircleCollider obj2)
	{
		if(obj2 instanceof Player)
		{
			flagColision = true;
			sleepObject();
		}
	}
}
