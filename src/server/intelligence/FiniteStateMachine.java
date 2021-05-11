package server.intelligence;

/*
 * This class handles the different intelligence bot states.
 */

public class FiniteStateMachine
{
	private AbstractFSMState startingState;
	private AbstractFSMState currentState;
	
	public FiniteStateMachine (AbstractFSMState startingState)
	{
		currentState = null;
		this.startingState = startingState;
		start();
	}
	
	public void start()
	{
		if(startingState != null)
		{
			enterState(startingState);
		}
	}
	
	public void update()
	{
		if(currentState != null)
		{
			currentState.updateState();
		}
	}
	
	public void enterState(AbstractFSMState nextState)
	{
		if(nextState == null)
		{
			return;
		}
		
		currentState = nextState;
		currentState.enterState();
	}
	
	public String toString()
	{
		String res = currentState.getClass().getName();
		return res;
	}
}
