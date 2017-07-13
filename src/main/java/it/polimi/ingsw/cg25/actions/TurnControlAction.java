package it.polimi.ingsw.cg25.actions;

import it.polimi.ingsw.cg25.gamegenerics.ActionBasedGame;
import it.polimi.ingsw.cg25.gamegenerics.Player;
import it.polimi.ingsw.cg25.gamegenerics.TurnBasedGame;

/**
 * 
 * @author nicolo
 *
 * @param <T> the type of turn based game of the model of this action 
 */
public abstract class TurnControlAction<T extends TurnBasedGame<? extends Player>  & ActionBasedGame> extends Action{
	
	/**
	 * The model of the action
	 */
	private T model;
	
	/**
	 * Simple constructor
	 * @param model the model of the action
	 */
	public TurnControlAction(T model){
		if(model==null)	
			throw new IllegalArgumentException("The match reference cannot be null");
		this.model = model;
	}
	
	/**
	 * Returns the model of the action.
	 */
	public T getModel(){
		return this.model;
	}
	
}
