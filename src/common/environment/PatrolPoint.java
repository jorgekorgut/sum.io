package common.environment;

public class PatrolPoint extends GameObject implements CircleColider
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
	public int getRadiusColider() 
	{
		return width/2;
	}

	@Override
	public void onColision(CircleColider obj2)
	{
		if(obj2 instanceof Player)
		{
			flagColision = true;
			sleepObject();
		}
	}
}
