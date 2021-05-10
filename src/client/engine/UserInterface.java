package client.engine;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import client.environment.EnvironmentHandler;
import common.environment.Player;

public class UserInterface 
{
	private final int BORDER_DISTANCE = 20;
	
	private final int PLAYERCOUNT_WIDTH = 200;
	private final int PLAYERCOUNT_HEIGHT = 50;
	private final int PLAYERCOUNT_FONTSIZE = 14;
	
	private final int FPSCOUNT_WIDTH = 50;
	private final int FPSCOUNT_HEIGHT = 25;
	private final int FPSCOUNT_FONTSIZE = 10;
	
	private final int NAME_GAP = 10;
	private final int NAME_WIDTH = 100;
	private final int NAME_HEIGHT = 25;
	private final int NAME_FONTSIZE = 14;
	
	private final int SLIDERBOOST_WIDTH = 200;
	private final int SLIDERBOOST_HEIGHT = 75;
	
	private EngineHandler callback;
	private LabelTextObject playerCount;
	private LabelTextObject fpsCount;
	
	private LinkedList<LabelTextObject> names;
	
	private LabelSliderObject boostLabel;
	
	public UserInterface(EngineHandler callback)
	{
		this.callback = callback;
		ScreenRender sr = callback.getScreenRender();
		playerCount = new LabelTextObject(sr,"background1",
									+PLAYERCOUNT_WIDTH/2 + Window.BORDER_WIDTH + BORDER_DISTANCE,
									+PLAYERCOUNT_HEIGHT/2 + Window.BORDER_HEIGHT + BORDER_DISTANCE,
									PLAYERCOUNT_WIDTH,
									PLAYERCOUNT_HEIGHT, 
									PLAYERCOUNT_FONTSIZE);
		
		fpsCount = new LabelTextObject(sr,"null",
									callback.getWindow().getJFrame().getWidth()-FPSCOUNT_WIDTH/2 - Window.BORDER_WIDTH,
									+FPSCOUNT_HEIGHT/2 + Window.BORDER_HEIGHT,
									FPSCOUNT_WIDTH,
									FPSCOUNT_HEIGHT,
									FPSCOUNT_FONTSIZE);
		
		
		boostLabel = new LabelSliderObject(sr,"speedui",
				"speedslider",
				SLIDERBOOST_WIDTH/2 + Window.BORDER_WIDTH + BORDER_DISTANCE ,
				callback.getWindow().getJFrame().getHeight() - SLIDERBOOST_HEIGHT/2 - Window.BORDER_HEIGHT/2 - BORDER_DISTANCE,
				SLIDERBOOST_WIDTH,
				SLIDERBOOST_HEIGHT);
		
		
		
	}
	
	public void setBoost(double boostRatio)
	{
		boostLabel.setSliderRatio(boostRatio);
	}
	
	public void revalidate()
	{
		fpsCount.revalidate(callback.getWindow().getJFrame().getWidth()-FPSCOUNT_WIDTH/2-Window.BORDER_WIDTH,
							+FPSCOUNT_HEIGHT + Window.BORDER_HEIGHT,
							FPSCOUNT_WIDTH,
							FPSCOUNT_HEIGHT);
		
		boostLabel.revalidate(SLIDERBOOST_WIDTH/2 + Window.BORDER_WIDTH + BORDER_DISTANCE ,
							callback.getWindow().getJFrame().getHeight() - SLIDERBOOST_HEIGHT/2 - Window.BORDER_HEIGHT/2 - BORDER_DISTANCE,
							SLIDERBOOST_WIDTH,
							SLIDERBOOST_HEIGHT);
	}
	
	public void update()
	{
		updatePlayerCount();
		updateFpsCount();
		updatePlayerBoost();
		updateClientsName();
	}
	
	
	private void updatePlayerBoost()
	{
		if(callback.getMainClient().getEnvironmentHandler().getPlayerClient() != null)
		{
			boostLabel.setSliderRatio(callback.getMainClient().getEnvironmentHandler().getPlayerClient().getBoostQuantity()/100.0);
		}
	}
	
	private void updatePlayerCount()
	{
		playerCount.updateText("Players remaining: " + callback.getMainClient().getEnvironmentHandler().getRemainingPlayers());
	}
	
	private void updateFpsCount()
	{
		fpsCount.updateText("FPS: "+ callback.getScreenRender().getFpsCount());
	}
	
	//FIXME: Find another way to implement the names of each client
	private void updateClientsName()
	{
		
		ArrayList<Player> playerList = callback.getMainClient().getEnvironmentHandler().getPlayerMap();
		
		if(playerList!= null && (names == null || playerList.size() != names.size()))
		{
			names = new LinkedList<LabelTextObject>();
			
			int quantity = playerList.size()-names.size();
			
			for(int i = playerList.size()-quantity; i< playerList.size();i++)
			{
				LabelTextObject currentObj= new LabelTextObject(callback.getScreenRender(),
																"null",
																(int)playerList.get(i).getX(),
																(int)playerList.get(i).getY()+playerList.get(i).getHeight()/2+NAME_GAP,
																NAME_WIDTH,
																NAME_HEIGHT,
																NAME_FONTSIZE);
				currentObj.setAbsolute(false);
				currentObj.updateText(playerList.get(i).getPlayerIP());
				currentObj.setRenderingPriority(EnvironmentHandler.PRIORITYRENDER_NAME);
				names.add(currentObj);
				
				//FIXME: Is it a problem ? YES!
				callback.getScreenRender().removeToRender(currentObj);
			}
		}
		
		int index = 0;	
		
		for(LabelTextObject currentName: names)
		{
			if(!playerList.get(index).isAwake())
			{
				currentName.setState(false);
			}
			else
			{
				currentName.translateTo(playerList.get(index).getX(),playerList.get(index).getY()+playerList.get(index).getHeight()/2+NAME_GAP);
				callback.getScreenRender().addToRender(currentName);
			}
			index++;
		}
	}
}
