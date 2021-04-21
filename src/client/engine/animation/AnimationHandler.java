package client.engine.animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import client.engine.EngineHandler;
import common.environment.CircleColider;
import common.environment.GameObject;

public class AnimationHandler 
{
	public static final String COLISION = "collisionanimation";
	public static final String START = "startanimation";
	private EngineHandler callback;
	private HashMap<String,AnimationObject> animationMap;
	
	private ArrayList<AnimationObject> inProcess;
	
	public AnimationHandler (EngineHandler callback)
	{
		this.callback = callback;
		inProcess = new ArrayList<AnimationObject>();
		animationMap = new HashMap<String,AnimationObject>();
		animationMap.put(COLISION,new AnimationObject(COLISION,callback.getScreenRender().getImageMap().get(COLISION),6,120,120));
		animationMap.put(START,new AnimationObject(START,callback.getScreenRender().getImageMap().get(START),4,200,200));
	}
	
	//How to iterate an HashMap ?
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
				inProcess.get(i).stopTimer();
				callback.getScreenRender().removeToRender(inProcess.get(i));
				inProcess.remove(i);
				i--;
			}
			//Update positions
			/*
			Iterator<AnimationObject> iter = inProcess.iterator();
			while(iter.hasNext())
			{
				AnimationObject ao = iter.next();
			}
			*/
			
		}
	}
	
	public void onEnvironmentStart(GameObject player)
	{
		AnimationObject currentAnimation2 = new AnimationObject(animationMap.get(START),player,1050,3000);
		callback.getScreenRender().addToRender(currentAnimation2);
		int posX = callback.getWindow().getJFrame().getWidth()/2;
		int posY = callback.getWindow().getJFrame().getHeight()/2;
		currentAnimation2.setAbsolute(true);
		currentAnimation2.launchAnimation(posX, posY-player.getHeight()*2);
	}
	
	public void onColision(GameObject colisionObject, GameObject player)
	{
		
		int posX = (int)(colisionObject.getX()+callback.getWindow().getJFrame().getWidth()/2);
		int posY = (int)(colisionObject.getY()+callback.getWindow().getJFrame().getHeight()/2);
		
		AnimationObject currentAnimation = new AnimationObject(animationMap.get(COLISION),colisionObject,110,500);
		
		if(!inProcess.contains(currentAnimation))
		{
			inProcess.add(currentAnimation);
			callback.getScreenRender().addToRender(currentAnimation);
			currentAnimation.launchAnimation(posX,posY);
		}
	}
}
