package common.environment;

/*
 * This class enable the collision for a GameObject.
 * 		- say if there is a collision with another circle
 * 		- call onCollision() Method
 */

public interface CircleCollider 
{	
	public abstract double getX();
	public abstract double getY();
	public abstract int getRadiusColider();
	public abstract void onCollision(CircleCollider obj2);
	public boolean getFlagCollision();
	public void setFlagCollision(boolean value);
	
	public default boolean hasCollidedTo(CircleCollider obj2)
	{
		boolean haveColision = false;
		
		double xDist = Math.pow(getX() - obj2.getX(), 2);
		double yDist = Math.pow(getY() - obj2.getY(), 2);
		
		if(xDist + yDist < Math.pow(getRadiusColider() + obj2.getRadiusColider(),2))
		{
			haveColision = !haveColision;
		}
		
		if(haveColision)
		{
			onCollision(obj2);
		}
		
		return haveColision;
	}
}