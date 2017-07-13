/**
 * 
 */
package it.polimi.ingsw.cg25.gamegenerics;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple generic game class
 * @author Davide
 *
 */
public abstract class Match<T extends Player> {

	/**
	 * The game's players
	 * @see Player
	 */
	protected final List<T> players;
	
	/**
	 * Index of current player (referenced to players' list)
	 * @see Match#players
	 */
	protected int currentPlayerIndex;

	/**
	 * Match constructor
	 */
	public Match(){
		this.players = new ArrayList<>();
	}

	/**
	 * @return List<{@link Player}> the players
	 */
	public List<T> getPlayers() {
		return players;
	}

	/**
	 * Gets the current player
	 * @return the current {@link Player}
	 */
	public abstract T getCurrentPlayer();
	
	/**
	 * Adds a player to the match
	 * @param player {@link Player} the player to add
	 */
	public abstract void addPlayer(T player);

}
