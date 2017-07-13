/**
 * 
 */
package it.polimi.ingsw.cg25.gamegenerics;

import java.util.List;

import it.polimi.ingsw.cg25.exceptions.CannotPassException;
import it.polimi.ingsw.cg25.exceptions.EndGameException;

/**
 * The turn logic of a game
 * @author Davide
 *
 */
public interface TurnBasedGame<T extends Player> {
	
	/**
	 * Begins a player's turn
	 */
	public void beginTurn();

	/**
	 * Ends a player's turn
	 * @throws CannotPassException if the player can't end his turn
	 */
	public void endTurn() throws CannotPassException;

	/**
	 * Gets the next player
	 * @return {@link Player} the next player
	 * @throws EndGameException if the game is over
	 */
	public T nextPlayer() throws EndGameException;

	/**
	 * Checks if the current player can pass
	 * @return true if can, false otherwise
	 */
	public boolean canPass();

	/**
	 * Checks if the current player has won the game
	 * @return true if he has won, false otherwise
	 */
	public boolean hasWon();

	/**
	 * Get the number of round played
	 * @return the round number
	 */
	public int getRoundNumber();

	/**
	 * Set the players' turn's order 
	 * @param order
	 */
	public void setOrder(List<T> order);

	/**
	 * Ends a complete turns' round
	 * @throws EndGameException if the game is over
	 */
	public void endRound() throws EndGameException;

}
