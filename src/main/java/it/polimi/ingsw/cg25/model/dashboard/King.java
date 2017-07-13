package it.polimi.ingsw.cg25.model.dashboard;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import it.polimi.ingsw.cg25.model.BoardCD4;
import it.polimi.ingsw.cg25.model.dashboard.topological.City;

/**
 * 
 * @author Giovanni
 *
 */
public class King {

	/**
	 * The game board
	 */
	private final BoardCD4 board;
	/**
	 * The king's current city
	 */
	private City currentCity;
	
	/**
	 * King class constructor
	 * @param board the reference to the game board
	 * @param currentCity the king's city
	 * @exception IllegalArgumentException when the board or the current city is null
	 */
	public King(BoardCD4 board, City currentCity) {
		if(currentCity == null)
			throw new IllegalArgumentException("The king's city can't be null!");
		if(board == null)
			throw new IllegalArgumentException("What's the board the king belongs to?");
		this.board = board;
		this.currentCity = currentCity;
	}
	
	/**
	 * Calculate the amount of coins to pay to move the king from his current city to another
	 * @param destCity the king's destination city
	 * @param player the player who wants to perform the action
	 * @return the amount of coins to pay to move the king
	 */
	public int moveKing(City destCity) {
		DijkstraShortestPath<City, DefaultEdge> dShortPath = 
				new DijkstraShortestPath<City, DefaultEdge>(board.getRoadNetwork(), currentCity, destCity);
		
		// getPathLength returns a double
		return ((int) dShortPath.getPathLength())*2;
	}

	/**
	 * @return the king's current city
	 */
	public City getCurrentCity() {
		return currentCity;
	}
	
	/**
	 * Set the new king's city
	 * @param destCity the city where the king has to move
	 */
	public void setKingCity(City destCity) {
		this.currentCity = destCity;
	}
	
}
