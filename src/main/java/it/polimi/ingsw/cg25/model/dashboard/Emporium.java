package it.polimi.ingsw.cg25.model.dashboard;

import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.dashboard.topological.City;

/**
 * 
 * @author Giovanni
 *
 */
public class Emporium {

	/**
	 * The owner of the emporium
	 */
	private final PlayerCD4 owner;
	/**
	 * The city where the emporium has been built
	 */
	private final City city;
	
	/**
	 * Emporium class constructor
	 * @param owner the player owner of the emporium
	 * @param city the city where to build the emporium
	 * @exception IllegalArgumentException when the owner or the city is null
	 */
	public Emporium(PlayerCD4 owner, City city) {
		if(owner == null)
			throw new IllegalArgumentException("You can't create an emporium without an owner!");
		if(city == null)
			throw new IllegalArgumentException("You can't create an emporium without a city!");
		this.owner = owner;
		this.city = city;
	}

	/**
	 * @return the emporium's owner
	 */
	public PlayerCD4 getOwner() {
		return owner;
	}

	/**
	 * @return the city where the emporium is located
	 */
	public City getCity() {
		return city;
	}
	
}
