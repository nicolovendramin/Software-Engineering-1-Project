package it.polimi.ingsw.cg25.gamegenerics;

import it.polimi.ingsw.cg25.model.HSBColor;

/**
 * 
 * @author Giovanni
 *
 */
public abstract class Player {

	/**
	 * The name of the player
	 */
	private final String name;
	/**
	 * The color of the player
	 */
	private final HSBColor color;
	
	/**
	 * Player class constructor
	 * @param name the name of the player
	 * @param color the color of the player
	 */
	public Player(String name, HSBColor color) {
		if(name == null)
			throw new NullPointerException("You can't create a player without a name!");
		if(color == null)
			throw new NullPointerException("You can't create a player without a color!");
		
		this.name = name;
		this.color = color; 
	}

	/**
	 * @return the name of the Player
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the color of the player
	 */
	public HSBColor getColor() {
		return color;
	}

}
