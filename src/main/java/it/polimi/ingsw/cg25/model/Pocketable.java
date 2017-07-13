package it.polimi.ingsw.cg25.model;

/**
 * 
 * @author Giovanni
 *
 */
public abstract class Pocketable {

	/**
	 * The quantity of the stuff included in a Pocketable object
	 */
	private final int supply;
	
	/**
	 * Pocketable class constructor
	 * @param supply the quantity of the stuff that will be included in the
	 * current Pocketable object
	 */
	public Pocketable(int supply) {
		this.supply = supply;
	}

	/**
	 * @return the supply quantity
	 */
	public int getSupply() {
		return supply;
	}
	
}
