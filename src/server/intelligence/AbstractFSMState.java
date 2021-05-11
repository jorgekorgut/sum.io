package server.intelligence;

/*
 * This class represents the base for a bot state inheritance.
 */

public abstract class AbstractFSMState
{
	private ExecutionState executionState;
	public ExecutionState getExecutionState() {return executionState;}
	protected PlayerBot player;
	public void setExecutionState(ExecutionState executionState) {this.executionState = executionState;}
	
	public AbstractFSMState(PlayerBot player)
	{
		this.player = player;
		executionState = ExecutionState.NONE;
	}
	
	public boolean enterState()
	{
		executionState = ExecutionState.ACTIVE;
		return true;
	}
	 
	public abstract void updateState();
	
	public boolean exitState()
	{
		executionState = ExecutionState.COMPLETED; 
		return true;
	}
	
}
