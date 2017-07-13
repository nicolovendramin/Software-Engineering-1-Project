package it.polimi.ingsw.cg25.actions;

import it.polimi.ingsw.cg25.exceptions.CannotPassException;
import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.gamegenerics.ActionBasedGame;
import it.polimi.ingsw.cg25.gamegenerics.Player;
import it.polimi.ingsw.cg25.gamegenerics.TurnBasedGame;
/**
 * 
 * @author nicolo
 *
 * @param <T> the type of the model. Must be both an ActionBased and a TurnBasedGame
 */
public class PassAction<T extends ActionBasedGame & TurnBasedGame<? extends Player>> extends TurnControlAction<T>{
	
	/**
	 * PassAction class constructor
	 * @param model the model where the current turn should come to an end
	 */
	public PassAction(T model) {
		super(model);
	}

	/**
	 * Passes the turn. 
	 * @return Return the next Action to be passed to the controller
	 */
	@Override
	public Action doAction() throws CannotPerformActionException {
		try{
			this.getModel().endTurn();
		}
		catch(CannotPassException e){
			throw new CannotPerformActionException(e.getMessage(),e);
		}
		return new ChooseActionTypeAction<T>(this.getModel());
	}
	
	/**
	 * Returns a string representation of the action
	 */
	@Override
	public String toString()
	{
		return "Pass";
	}

}
