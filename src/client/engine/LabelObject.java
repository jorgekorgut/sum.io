package client.engine;

import java.awt.Graphics;

import client.environment.EnvironmentHandler;
import common.environment.GameObject;

/*
 * This class is an GameObject extension that was created to be used as an interface.
 */

public class LabelObject extends GameObject
{
	protected ScreenRender screenRender;
	private double ratioCutted = 1;
	
	public LabelObject(ScreenRender screenRender, String background, int x, int y, int width, int height)
	{
		this(screenRender,background,x,y,width,height,0);
	}
	
	public LabelObject(ScreenRender screenRender, String background, int x, int y, int width, int height, int layer)
	{
		super(background,x,y,width,height,EnvironmentHandler.PRIORITYRENDER_UI+layer);
		setAbsolute(true);
		this.screenRender = screenRender;
		screenRender.addToRender(this);
	}
	
	public double getRatioCutted() {return ratioCutted;}
	public void setRatioCutted(double ratioCutted){this.ratioCutted = ratioCutted;}
	
	//Method called by the ScreenRender.
	public void draw(Graphics g)
	{
		
	}

	public void revalidate(int x, int y, int width, int height) 
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
}
