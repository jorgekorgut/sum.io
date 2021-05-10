package client.lobby;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import client.MainClient;
import client.engine.ImageCache;
import client.engine.InputHandler;
import client.engine.KeyboardListener;
import client.engine.ScreenRender;
import client.engine.UserInterface;
import client.engine.Window;
import client.engine.animation.AnimationHandler;
import client.engine.sound.AudioMaster;
import common.communication.LobbyPack;
import common.environment.Player;

/*
 *  LobbyHandler set up the visual support to manage the multiplayer, add bots and start the game.
 */

public class LobbyHandler{
	
	//FIXME: Hard coded
	public final static int BORDER_WIDTH = 7;
	public final static int BORDER_HEIGHT = 30;
	private final int NUMBERMAX_PLAYERS = 12;
	
	private MainClient callback;
	private JPanel mainPanel;
	private ArrayList<JPanel> panelsList;
	private String player;
	private String playerSkin;
	private LobbyPack lobbyPack;
	
	private JFrame window;
	private Hashtable<String,BufferedImage> imageMap = null;
	
	private boolean isLobbyActive = false;
	
	public LobbyHandler(MainClient callback, String title, int w, int h)
	{
		isLobbyActive = true;
		this.callback = callback;
		imageMap = callback.getImageCache().getImageMap();
		
		window = new JFrame(title);
		lobbyPack = new LobbyPack();
		window.setSize(w,h);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocationRelativeTo(null);
		
		setupMainPanel();
		setupPlayerPanel();
		window.add(mainPanel);
		window.setVisible(true);
		
		callback.getAudioMaster().playSound(AudioMaster.START_MUSIC_REFERENCE,2f);
	}
	
	public LobbyHandler(MainClient callback, JFrame jFrame)
	{
		isLobbyActive = true;
		this.callback = callback;
		this.window = jFrame;
		lobbyPack = new LobbyPack();
		imageMap = callback.getImageCache().getImageMap();
		
		setupMainPanel();
		setupPlayerPanel();
		window.add(mainPanel);
		window.requestFocus();
		window.revalidate();
		window.repaint();
		
		callback.getAudioMaster().playSound(AudioMaster.START_MUSIC_REFERENCE,-1f);
	}
	
	public LobbyPack getLobbyPack() {return lobbyPack;}
	public JFrame getJFrame(){return window;}
	public String getPlayer() {return player;}
	public Hashtable<String,BufferedImage> getImageMap(){return imageMap;}
	public void setPlayer(String player) {this.player = player;}
	public void setPlayerSkin(String playerSkin) {this.playerSkin = playerSkin;}
	public String getPlayerSkin() {return playerSkin;}
	
	private void setupMainPanel()
	{
		mainPanel = new JPanel(new GridLayout(8,2,20,10));
	}
	
	private void setupPlayerPanel()
	{
		panelsList = new ArrayList<JPanel>(NUMBERMAX_PLAYERS);
		
		for(int i = 0;i<2;i++ )
		{
			JPanel empty = new JPanel();
			panelsList.add(empty);
			mainPanel.add(empty);
		}
		
		for(int i = 0; i< NUMBERMAX_PLAYERS;i++)
		{
			PlayerPanel pp = new PlayerPanel(this);
			panelsList.add(pp);
			mainPanel.add(pp);
		}
		
		JPanel empty = new JPanel();
		panelsList.add(empty);
		mainPanel.add(empty);
		
		setupStartButton();
	}
	
	private void setupStartButton()
	{
		//Avoid to create StartButton if he is not the server creator.
		if(callback.getLaunchServer() == null)
		{
			return;
		}
		LobbyPanel start = new LobbyPanel(this);
		start.setPalet(1);
		
		JButton startBt = new JButton("START");
		startBt.setFont(LobbyPanel.FONT);
		startBt.setContentAreaFilled(false);
		
		//TODO: AddSound when the player interact with the button
		startBt.addMouseListener(new MouseListener() 
										{
											@Override
											public void mouseClicked(MouseEvent e) {
												onStart();
											}
	
											@Override
											public void mousePressed(MouseEvent e) {}
	
											@Override
											public void mouseReleased(MouseEvent e) {}
	
											@Override
											public void mouseEntered(MouseEvent e) 
											{
												
											}
	
											@Override
											public void mouseExited(MouseEvent e) 
											{
												
											}
										}
								 );
		
		start.add(startBt);
		
		panelsList.add(start);
		mainPanel.add(start);
	}
	
	public void addBotToLobby()
	{
		callback.getAudioMaster().playSound(AudioMaster.SELECTED_REFERENCE, -1f);
		if(lobbyPack.getPlayerList().size()<12)
		{
			int botCount = 0;
			for(String ps: lobbyPack.getPlayerList())
			{
				ps.startsWith(". BOT");
				botCount++;
			}
			lobbyPack.addPlayer(".BOT " + botCount);
			lobbyPack.addPlayerSkin("bot");
			callback.getCommsHandler().sendPack(lobbyPack);
		}
	}
	
	public void syncLobby(LobbyPack lPack) 
	{
		if(!isLobbyActive)
		{
			callback.getAudioMaster().stopSound(AudioMaster.END_MUSIC_REFERENCE);
			callback.getEnvironmentHandler().getFenetreGagnant().dispose();
			callback.returnLobby();
		}
		
		lobbyPack = lPack;
		LinkedList<String> playerList = lobbyPack.getPlayerList();
		ArrayList<String> playerSkinList = lobbyPack.getPlayerSkinList();
		int index = 2;
		for(String s:playerList)
		{
			((PlayerPanel)panelsList.get(index)).setName(s);
			((PlayerPanel)panelsList.get(index)).disableButton();
			if(s.startsWith(".BOT"))
			{
				((PlayerPanel)panelsList.get(index)).setImage(imageMap.get("bot"));
			}
			else
			{
				((PlayerPanel)panelsList.get(index)).setImage(imageMap.get(playerSkinList.get(index-2)));
			}
			index++;
		}
		for(int i = index; i < NUMBERMAX_PLAYERS+4-index; i++ )
		{
			panelsList.get(i).setName("< EMPTY >");
			((PlayerPanel)panelsList.get(index)).enableButton();
			((PlayerPanel)panelsList.get(index)).setImage(null);
		}
		
		if(lobbyPack.getStartFlag())
		{
			callback.launchGame();
		}
	}
	
	public void onStart()
	{	
		lobbyPack.setStartFlag(true);
		callback.getCommsHandler().sendPack(lobbyPack);
	}
	
	public void onPlayerConnectServer(String name,String skinName)
	{
		this.player = name;
		this.playerSkin = skinName;
		lobbyPack.addPlayer(name);
		lobbyPack.addPlayerSkin(skinName);
		callback.getCommsHandler().connectNetwork();
		callback.getCommsHandler().sendPack(lobbyPack);
	}

	public void killAll() 
	{
		isLobbyActive = false;
		window.remove(mainPanel);
	}
}
