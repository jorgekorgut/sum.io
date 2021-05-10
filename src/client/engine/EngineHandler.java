package client.engine;

import javax.swing.JFrame;
import client.MainClient;
import client.engine.animation.AnimationHandler;
import common.communication.ActionPack;
import common.environment.ActionHandler;

/*
 *  This class is responsible to be the head of every other client side Handlers
 */

public class EngineHandler {

	private MainClient callback;
	private InputHandler inputHandler;
	private ScreenRender screenRender;
	private AnimationHandler animationHandler;
	private Window window;
	private UserInterface userInterface;
	
	//FIXME: It is not the right spot.
	private KeyboardListener keyboardListener;
	
	public EngineHandler(MainClient callback, JFrame jFrame)
	{
		this.callback = callback;
		this.window = new Window(this,callback.getLobbyHandler(),jFrame);
		
		screenRender = new ScreenRender(this,window.getJFrame().getBufferStrategy());
		inputHandler = new InputHandler(this);
		
		this.window.getJFrame().requestFocus();
		keyboardListener = new KeyboardListener(inputHandler);
		window.getJFrame().addKeyListener(keyboardListener);
		this.animationHandler = new AnimationHandler(this);
		userInterface = new UserInterface(this);
	}
	
	public ScreenRender getScreenRender() {return screenRender;}
	public MainClient getMainClient() {return callback;}
	public InputHandler getInputHandler() { return inputHandler;}
	public Window getWindow() { return window;}
	public UserInterface getUserInterface() {return userInterface;}
	public AnimationHandler getAnimationHandler() {return animationHandler;}
	
	public void sendActionPack(ActionPack aPack)
	{
		ActionHandler.doPlayerAction(aPack);
		callback.getCommsHandler().sendPack(aPack);
	}

	public void killAll() 
	{
		window.getJFrame().removeKeyListener(keyboardListener);
		screenRender.killAll();
		inputHandler.killAll();
		animationHandler.killAll();
	}
}
