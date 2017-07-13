package it.polimi.ingsw.cg25.gamegenerics;

import java.util.List;

import it.polimi.ingsw.cg25.actions.Action;
/**
 * 
 * @author nicolo
 *
 */
public interface ActionBasedGame {
	/**
	 * This method is meant to return the available actions for the ActionBasedGame
	 * @return
	 */
	public List<Action> getAvailableActions();
	
	/**
	 * This method returns the action to start the ActionBasedGame
	 * @return
	 */
	public Action getStartingAction();
	
	/**
	 * The method to handle the action received by the model
	 * @param change the action to be applied
	 */
	public void receiveAction(Action change);
	
	/**
	 * Getter for the Game Logger
	 * @return {@link GameLogger} the game logger
	 */
	public GameLogger getLogger();

}
