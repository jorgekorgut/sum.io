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

public class AnimationObject extends GameObject
{
	private BufferedImage[] animation;
	private int numberOfFrames;
	private int animationCount = 0;
	private GameObject parent = null;
	private GameObject client = null;
	private String animationType;
	
	private Timer updateTimer;
	private int updateRate;
	private int duration = 0;
	
	private int time = 0;
	
	public AnimationObject (String animationType,BufferedImage animationImage, int numberOfFrames,int tileWidth,int tileHeight)
	{
		super("null",0,0,tileWidth,tileHeight,1000);
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
		this.updateTimer = ao.getUpdateTimer();
		this.updateRate = speed;
		this.isAwake = ao.isAwake();
		setupUpdateTimer();
	}
	
	public BufferedImage[] getAnimation() {return animation;}
	public GameObject getParent() {return parent;}
	public String getAnimationType() {return animationType;}
	public int getNumberOfFrames() {return numberOfFrames;}
	public GameObject getClient() {return client;}
	public void setClient(GameObject go) {this.client = go;}  
	public Timer getUpdateTimer() {return updateTimer;}
	
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
		if(!updateTimer.isRunning())
		{
			updateTimer.start();
		}
	}
	
	private void setupUpdateTimer()
	{
		updateTimer = new Timer(updateRate,
							new ActionListener() {
								public void actionPerformed(ActionEvent ae)
								{
									animationCount++;
									if(animationCount > numberOfFrames-1)
									{
										animationCount =0;
									}
									if(time*updateRate > duration)
									{
										sleepObject();
										updateTimer.stop();
									}
									time++;
								}
							});
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

	public void stopTimer() 
	{
		updateTimer.stop();
	}

}
