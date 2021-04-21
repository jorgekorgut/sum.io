package common.environment;

public class Platform extends GameObject implements CircleColider
{
	private boolean flagColision = false;
	
	public Platform(String name, int x, int y, int width, int height, int renderingPriority) 
	{
		super(name, x, y, width, height, renderingPriority);
	}

	@Override
	public boolean getFlagCollision() {return flagColision;}
	@Override
	public void setFlagCollision(boolean value) {flagColision = value;}
	
	@Override
	public int getRadiusColider() 
	{
		return width/2;
	}
	
	@Override
	public void onColision(CircleColider obj2) 
	{
		
	}

}
