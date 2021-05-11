package common.environment;

/*
 * This class represents the Platform of the game.
 */

public class Platform extends GameObject implements CircleCollider
{
	private boolean flagCollision = false;
	
	public Platform(String name, int x, int y, int width, int height, int renderingPriority) 
	{
		super(name, x, y, width, height, renderingPriority);
	}

	@Override
	public boolean getFlagCollision() {return flagCollision;}
	@Override
	public void setFlagCollision(boolean value) {flagCollision = value;}
	
	@Override
	public int getRadiusCollider() 
	{
		return width/2;
	}
	
	@Override
	public void onCollision(CircleCollider obj2) 
	{
		
	}
}
