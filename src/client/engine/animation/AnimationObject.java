package client.engine.animation;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.TileObserver;

import javax.swing.JFrame;
import javax.swing.Timer;

import common.environment.GameObject;
import common.environment.Player;
import client.environment.EnvironmentHandler;

public class AnimationObject extends GameObject
{
	private BufferedImage[] animation;
	private int numberOfFrames;
	private int animationCount = 0;
	private GameObject parent = null;
	private GameObject client = null;
	private String animationType;
	
	private int updateRate;
	private int duration = 0;
	private int currentTime =0;
	private int absoluteAnimationCount = 0;
	
	public AnimationObject (String animationType,BufferedImage animationImage, int numberOfFrames,int tileWidth,int tileHeight)
	{
		super("null",0,0,tileWidth,tileHeight,EnvironmentHandler.PRIORITYRENDER_ANIMATION);
		this.numberOfFrames = numberOfFrames;
		this.animation = new BufferedImage[numberOfFrames];
		animationImage = resizeImg(animationImage,tileWidth*numberOfFrames,tileHeight);
		
		for(int i = 0; i< numberOfFrames; i++)
		{
			animation[i] = animationImage.getSubimage(tileWidth*i, 0, tileWidth, tileHeight);
		}
		
		this.animationType = animationType;
	}
	
	public AnimationObject(AnimationObject ao, GameObject parent,int speed, int duration)
	{
		super(ao.getName(),ao.getX(),ao.getY(),ao.getWidth(),ao.getHeight(),ao.getRenderingPriority());
		this.duration = duration;
		this.animation = ao.getAnimation();
		this.client = ao.getClient();
		this.parent = parent;
		this.numberOfFrames = ao.getNumberOfFrames();
		this.animationType = ao.getAnimationType();
		this.updateRate = speed;
		this.isAwake = ao.isAwake();
		this.currentTime = ao.getCurrentTime();
		
	}
	
	public BufferedImage[] getAnimation() {return animation;}
	public GameObject getParent() {return parent;}
	public String getAnimationType() {return animationType;}
	public int getNumberOfFrames() {return numberOfFrames;}
	public GameObject getClient() {return client;}
	public void setClient(GameObject go) {this.client = go;} 
	public int getCurrentTime() {return currentTime;}
	public int getUpdateRate() {return updateRate;}
	
	@Override
	public boolean equals(Object o)
	{
		boolean statement = super.equals(o);
		if(!(o instanceof AnimationObject))
		{
			return false;
		}
		
		AnimationObject current = (AnimationObject)o;
		
		if(current.getParent() == null)
		{
			return false;
		}
		
		if(current.getAnimationType().equals(getAnimationType()) && current.getParent().equals(getParent()))
		{
			return true;
		}
		
		return statement;
	}
	
	public void updatePos(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void launchAnimation(int x,int y)
	{	
		this.x = x;
		this.y = y;
		setState(true);
		
		currentTime = (int) System.currentTimeMillis();
		
		//FIXME: Find a better way to animate
	}
	
	public void update()
	{
		int deltaTime = (int)System.currentTimeMillis()- currentTime;
		
		if(deltaTime > updateRate*(absoluteAnimationCount+1))
		{
			absoluteAnimationCount++;
			animationCount++;
			
			if(animationCount >= numberOfFrames)
			{
				animationCount = 0;
			}
		}
		
		if(deltaTime > duration)
		{
			sleepObject();
		}
	}
	
	public void setParent(GameObject parent) {this.parent = parent;}
	
	
	public void draw(Graphics g)
	{	
		BufferedImage currentImage = animation[animationCount];
		
		int posX = (int) (getX() - currentImage.getWidth()/2 - client.getX());
		int posY = (int) (getY() - currentImage.getHeight()/2 - client.getY());
		
		if(isAbsolutePath())
		{
			posX += client.getX();
			posY += client.getY();
		}
		
		if(isAwake)
		{
			g.drawImage(currentImage,
					posX, 
					posY, 
					currentImage.getWidth(),
					currentImage.getHeight(),
					null);
		}
	}
	
	//https://stackoverflow.com/questions/9417356/bufferedimage-resize
	private BufferedImage resizeImg(BufferedImage img, int newW, int newH) 
	{ 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}
}
