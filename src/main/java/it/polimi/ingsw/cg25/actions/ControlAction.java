package it.polimi.ingsw.cg25.actions;

import it.polimi.ingsw.cg25.gamegenerics.ActionBasedGame;
/**
 * 
 * @author nicolo
 *
 * @param <T> is the Type of the ActionBasedGame 
 */
public abstract class ControlAction<T extends ActionBasedGame> extends Action{

	/**
	 * The model of the action
	 */
	private T model;
	
	public ControlAction(T model) {
		super();
		if(model == null)
			throw new IllegalArgumentException("The model of the action cannot be null");
		this.model = model;
	}

	@Override
	public T getModel(){
		return this.model;
	}
}
