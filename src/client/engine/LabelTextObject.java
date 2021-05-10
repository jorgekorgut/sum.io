package client.engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

/*
 * This class generates a text centralized in the LabelObject
 */

public class LabelTextObject extends LabelObject
{
	private String labelText;
	private int fontSize;
	private Font font;
	private Color fontColor;

	public LabelTextObject(ScreenRender screenRender,String background, int x, int y, int width, int height, int fontSize) 
	{
		super(screenRender,background,x, y, width, height);
		this.fontSize = fontSize;
		labelText = "";
		font = new Font("Verdana", Font.BOLD, this.fontSize);
		fontColor = new Color(14,44,83);
	}
	
	public void changeColor(int red,int green, int blue)
	{
		fontColor = new Color(red, green, blue);
	}
	
	@Override
	public void draw(Graphics g)
	{
		g.setColor(fontColor);
		
		FontMetrics metrics = g.getFontMetrics(font);
		
		double newX = 0;
		double newY = 0;
		
		if(isAbsolutePath())
		{
			newX = x - metrics.stringWidth(labelText)/2;
			newY = y + metrics.getHeight()/4;
		}
		else
		{	
			//FIXME: Problem with coordinates!
			double[] coords = ScreenRender.relativeToAbsolute(x+width/2, y+height/2, width, height);
			newX = (int)coords[0]- metrics.stringWidth(labelText)/2;
			newY = (int)coords[1]+ metrics.getHeight()/4;
		}
		
		g.setFont(font); 
		g.drawString(labelText,(int)newX,(int)newY);
	}
	
	public void updateText(String text)
	{
		labelText = text;
	}
}
