package common.environment;

/*
 * This class represents the boost item.
 */

public class CollectableBoost extends GameObject implements CircleCollider
{
	private int boostGain = 50;
	private boolean flagColision = false;
	
	public CollectableBoost(String name, double x, double y, int width, int height) 
	{
		super(name, x, y, width, height,1000);
	}

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
	public int getRadiusColider() 
	{
		return width/2;
	}
	
	@Override
	public boolean getFlagCollision()
	{
		return flagColision;
	}
	
	@Override
	public void setFlagCollision(boolean value) 
	{
		flagColision = value;
	}

	@Override
	public void onCollision(CircleCollider obj2) 
	{
		if(obj2 instanceof Player)
		{
			((Player)obj2).addBoost(boostGain);
			flagColision = true;
			sleepObject();
		}
	}
}
