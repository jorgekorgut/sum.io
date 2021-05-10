package client.engine.animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import client.engine.EngineHandler;
import common.environment.CircleCollider;
import common.environment.GameObject;

/*
 * This class is responsible of handling and launch the animations.
 */

public class AnimationHandler 
{
	public static final String COLISION = "collisionanimation";
	public static final String START = "startanimation";
	public static final String END = "endanimation";
	private EngineHandler callback;
	private HashMap<String,AnimationObject> animationMap;
	private ArrayList<AnimationObject> inProcess;

	public AnimationHandler (EngineHandler callback)
	{
		this.callback = callback;
		inProcess = new ArrayList<AnimationObject>();
		animationMap = new HashMap<String,AnimationObject>();
		
		//FIXME: Hardcoded
		animationMap.put(COLISION,new AnimationObject(COLISION,callback.getScreenRender().getImageMap().get(COLISION),6,120,120));
		animationMap.put(START,new AnimationObject(START,callback.getScreenRender().getImageMap().get(START),4,200,200));
		animationMap.put(END,new AnimationObject(END,callback.getScreenRender().getImageMap().get(END),19,300,300));
	}
	
	//This method set the current client for calculates the right position to launch the animation.
	public void setClient(GameObject go)
	{
		for (AnimationObject ao : animationMap.values()) 
		{
		   ao.setClient(go);
		}
	}
	
	public EngineHandler getEngineHandler() {return callback;}
	
	public void update()
	{
		
		for(int i = 0; i<inProcess.size(); i++)
		{	
			if(!inProcess.get(i).isAwake())
			{
				callback.getScreenRender().removeToRender(inProcess.get(i));
				inProcess.remove(i);
				i--;
			}
			else
			{
				inProcess.get(i).update();
			}
		}
	}
	
	public void onEnvironmentStart(GameObject player)
	{
		AnimationObject currentAnimation2 = new AnimationObject(animationMap.get(START),player,3000/4,3000);
		inProcess.add(currentAnimation2);
		callback.getScreenRender().addToRender(currentAnimation2);
		int posX = callback.getWindow().getJFrame().getWidth()/2;
		int posY = callback.getWindow().getJFrame().getHeight()/2;
		currentAnimation2.setAbsolute(true);
		currentAnimation2.launchAnimation(posX, posY-player.getHeight()*2);
	}
	
	public void onGameFinished()
	{
		int posX = callback.getWindow().getJFrame().getWidth()/2;
		int posY = callback.getWindow().getJFrame().getHeight()/2;
		AnimationObject currentAnimation3 = new AnimationObject(animationMap.get(END),new GameObject("null",0,0,0,0),1500/19+10,7000);
		inProcess.add(currentAnimation3);
		callback.getScreenRender().addToRender(currentAnimation3);
		currentAnimation3.setAbsolute(true);
		currentAnimation3.launchAnimation(posX+300, posY);
		
		new Thread( new Runnable() {
			
			public void run()
			{
				
				double time0 = System.currentTimeMillis();
				
				while(currentAnimation3.isAwake())
				{
					double time = System.currentTimeMillis();
					double deltaTime = time - time0;
					
					if(deltaTime >= currentAnimation3.getUpdateRate())
					{
						currentAnimation3.update();
						time0 = time;
					}
				}
		}}).start();
	}
	
	public void onCollision(GameObject colisionObject, GameObject player)
	{
		
		int posX = (int)(colisionObject.getX()+callback.getWindow().getJFrame().getWidth()/2);
		int posY = (int)(colisionObject.getY()+callback.getWindow().getJFrame().getHeight()/2);
		
		AnimationObject currentAnimation = new AnimationObject(animationMap.get(COLISION),colisionObject,100,500);
		
		if(!inProcess.contains(currentAnimation))
		{
			
			inProcess.add(currentAnimation);
			callback.getScreenRender().addToRender(currentAnimation);
			currentAnimation.launchAnimation(posX,posY);
		}
	}

	public void killAll() 
	{
		inProcess.clear();
		animationMap.clear();	
		callback = null;
		animationMap = null;
		inProcess = null;
	}
}
