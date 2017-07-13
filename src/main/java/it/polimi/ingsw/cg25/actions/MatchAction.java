package it.polimi.ingsw.cg25.actions;

import it.polimi.ingsw.cg25.model.MatchCD4;

/**
 * 
 * @author nicolo
 *
 */
public abstract class MatchAction extends Action {

	/**
	 * A reference to the current match related to the match action
	 */
	private MatchCD4 match;
	
	/**
	 * Initialize a match action
	 * @param model is the MatchCD4 the action is referred
	 * @throws IllegalArgumentException when the model is null
	 */
	public MatchAction(MatchCD4 model) {
		super();
		if(model==null)
			throw new IllegalArgumentException("The model cannot be null!");
		this.match = model;
	}
	
	/**
	 * Returns the match of the action
	 */
	@Override
	public MatchCD4 getModel(){
		return this.match;
	}

}
