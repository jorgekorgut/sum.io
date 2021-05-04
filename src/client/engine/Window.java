package client.engine;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyListener;
import java.awt.image.renderable.RenderableImage;

import javax.swing.JPanel;

import client.lobby.LobbyHandler;

public class Window{
	
	//FIXME: Hard coded
	public final static int BORDER_WIDTH = 7;
	public final static int BORDER_HEIGHT = 30;
	
	private EngineHandler engHandler;
	private JFrame window;
	
	public Window(EngineHandler engHandler, String title, int w, int h)
	{
		window = new JFrame(title);
		this.engHandler = engHandler;
		
		window.setSize(w,h);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBackground(new Color(142,203,224));
		
		window.setLocationRelativeTo(null);
		
		window.addComponentListener(new ComponentAdapter() 
														{
														public void componentResized(ComponentEvent componentEvent) {
															onWindowResized();
													    }	
														});
		window.setVisible(true);
		window.createBufferStrategy(2);
	}
	
	public Window(EngineHandler engHandler,LobbyHandler lobHandler, JFrame window)
	{
		this.window = window;
		lobHandler.killAll();
		//this.window.removeAll();
		
		this.engHandler = engHandler;
		
		window.setBackground(new Color(142,203,224));
		window.addComponentListener(new ComponentAdapter() 
		{
		public void componentResized(ComponentEvent componentEvent) {
			onWindowResized();
	    }	
		});
		window.createBufferStrategy(2);
	}
	
	public void onWindowResized()
	{
		if(engHandler.getUserInterface() != null)
		{
			engHandler.getUserInterface().revalidate();
		}
	}

	public JFrame getJFrame() {return window;}
}

