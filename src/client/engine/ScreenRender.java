package client.engine;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import common.environment.GameObject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import java.io.File;
import javax.imageio.ImageIO;

import client.engine.animation.AnimationObject;

public class ScreenRender{
	
	private static int windowWidth;
	private static int windowHeight;
	
	private static int originX= 0;
	private static int originY= 0;
	
	private BufferStrategy bufferStrategy;
	private PriorityBlockingQueue<GameObject> renderingQueue;
	//FIXME: try to find something better in the future.
	private Hashtable<String,BufferedImage> imageMap = null;
	
	private final int TEORICAL_FPS = 100;
	private int realFps = TEORICAL_FPS;
	private double oldT = 0;
	
	private RenderThread renderThread;
	private ImageCache imageCache;
	private EngineHandler callback;
	
	public ScreenRender(EngineHandler callback, BufferStrategy bufferStrategy)
	{
		this.bufferStrategy = bufferStrategy;

		this.callback = callback;
		this.imageCache = callback.getMainClient().getImageCache();
		
		renderingQueue = new PriorityBlockingQueue<GameObject>();
		imageMap = imageCache.getImageMap();
		
		//loadImages();
		
		renderThread = new RenderThread(this, TEORICAL_FPS);
		renderThread.start();
	}
	
	public Hashtable<String,BufferedImage> getImageMap(){return imageMap;}
	
	public void setOrigin(int x,int y) 
	{
		ScreenRender.originX= x; 
		ScreenRender.originY = y;
	}
	
	public void addToRender(GameObject go)
	{
		//Replace contains method by this explicit loop to take advantage of it and update X and Y position.		
		for(GameObject g : renderingQueue) 
		{	
		if(g.equals(go)) 
			{
				g.setState(go.isAwake());
				g.setX(go.getX());
				g.setY(go.getY());
				return;
			}
		}
		renderingQueue.add(go);
	}
	
	public void addToRender(ArrayList<GameObject> goList)
	{
		if(goList == null)
		{
			return;
		}
		
		for(GameObject go : goList )
		{
			addToRender(go);
		}
	}
	
	public void clearRender()
	{
		renderingQueue.clear();
	}
	
	public boolean removeToRender(GameObject go)
	{
		if(renderingQueue.remove(go))
		{
			return true;
		}
		System.out.println("Error: Rendering queue does not contains that object");	
		return false;
	}
	
	@Override
	public String toString()
	{
		String res = "";
		res += "Size: " + renderingQueue.size() + "\n";
		for(GameObject go : renderingQueue)
		{
			res += go.toString();
		}
		return res;
	}
	
	public int getFpsCount(){return realFps;}
	
	public void draw()
	{
		if(bufferStrategy == null)
		{
			return;
		}
		//TODO: Get a better way to render
		double t1 = System.currentTimeMillis();
		do 
		{
			do 
			{
				Graphics g = bufferStrategy.getDrawGraphics();
				
				windowHeight = callback.getWindow().getJFrame().getHeight();
				windowWidth = callback.getWindow().getJFrame().getWidth();
				
				g.clearRect(0, 0, windowWidth, windowHeight);
				
				GameObject[] renderingArray = renderingQueue.toArray(new GameObject[renderingQueue.size()]);
				
				Arrays.sort(renderingArray);
				
				for(GameObject go: renderingArray)
				{
					if(!go.isAwake())
					{
						continue;
					}
					
					if(go instanceof AnimationObject)
					{
						((AnimationObject)go).draw(g);
					}
					else if(go instanceof LabelObject)
					{
						if(((LabelObject) go).getRatioCutted() < 1)
						{
							drawGameObject(g, go, ((LabelObject) go).getRatioCutted());
						}
						else
						{
							drawGameObject(g, go);
						}
						
						((LabelObject)go).draw(g);
					}
					else
					{
						drawGameObject(g,go);
					}
				}
				g.dispose();
				
				
			}
			while(bufferStrategy.contentsRestored());
			
			bufferStrategy.show();
			
		}
		while(bufferStrategy.contentsLost());
		
		realFps = (int) (1000/(t1-oldT));
		oldT=t1;
	}
	
	private void drawGameObject(Graphics g, GameObject go)
	{
		double ratioCutted = 1;
		drawGameObject(g,go,ratioCutted);
	}
	
	private BufferedImage getImageCutted(GameObject go, double ratioCutted)
	{
		BufferedImage currentImage = imageMap.get(go.getName());
		if(ratioCutted == 0)
		{
			return null;
		}
		if(ratioCutted == 1)
		{
			return currentImage;
		}
		
		return cutImage(currentImage, ratioCutted);
		
	}
	
	private BufferedImage cutImage(BufferedImage currentImage,double ratioCutted)
	{
		currentImage = currentImage.getSubimage(0,0,(int)(currentImage.getWidth()*ratioCutted),currentImage.getHeight());
		return currentImage;
	}
	
	private void drawGameObject(Graphics g, GameObject go, double ratioCutted)
	{
		BufferedImage currentImage = getImageCutted(go,ratioCutted);
		
		if(currentImage == null && !go.getName().equals("null"))
		{
			currentImage = imageMap.get("noTexture");
		}
		
		double tempPosX = 0;
		double tempPosY = 0;
		
		double iX = 0;
		double iY = 0;
		
		if (go.isAbsolutePath())
		{
			tempPosX = go.getX();
			tempPosY = go.getY();
			iX = tempPosX- go.getWidth()/2.0;
			iY = tempPosY- go.getHeight()/2.0;
		}
		else
		{
			double[] coords = relativeToAbsolute(go.getX(), go.getY(), go.getWidth(), go.getHeight());
			
			iX = coords[0];
			iY = coords[1];
			/*
			tempPosX = go.getX() - originX;
			tempPosY = go.getY() - originY;
			iX = windowWidth/2.0 + tempPosX - go.getWidth()/2.0;
			iY = windowHeight/2.0 + tempPosY - go.getHeight()/2.0;*/
		}
		
		g.drawImage(
				currentImage,
				(int)iX,
				(int)iY,
				(int)(go.getWidth()*ratioCutted),
				(int)go.getHeight(),
				null);
	}
	
	public static double[] relativeToAbsolute(double x,double y, int width, int height)
	{
		double tempPosX = x - originX;
		double tempPosY = y - originY;
		double iX = windowWidth/2.0 + tempPosX - width/2.0;
		double iY = windowHeight/2.0 + tempPosY - height/2.0;
		
		return new double[] {iX,iY};
	}

	public void killAll() 
	{
		renderThread.kill();
		renderingQueue.clear();
	}
}

