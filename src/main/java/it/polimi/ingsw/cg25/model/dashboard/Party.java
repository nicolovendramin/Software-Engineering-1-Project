package it.polimi.ingsw.cg25.model.dashboard;

import it.polimi.ingsw.cg25.model.HSBColor;

/**
 * 
 * @author Giovanni
 *
 */
public class Party {

	/**
	 * The HSBColor linked to the Party object
	 */
	private final HSBColor color;
	/**
	 * A boolean attribute that points out if the card is a Jolly or not
	 */
	private final boolean isJolly;
	
	/**
	 * Party class constructor
	 * @param color the HSBColor of the party
	 * @param isJolly true if you want this party to match with any other party
	 * @exception IllegalArgumentException when the color is null
	 */
	public Party(HSBColor color, boolean isJolly) {
		if(color == null)
			throw new IllegalArgumentException("You can't create a Party without a color!");
		this.color = color;
		this.isJolly = isJolly;
	}

	/**
	 * Determine if two PartyColor objects are the same
	 * @param color the PartyColor object to compare
	 * @return true or false
	 */
	public boolean sameParty(Party party) {
		if(this.isJolly || party.isJolly )
			return true;
		else 
			return this.color.equals(party.getColor());
	}
	
	/**
	 * @return the color of this party
	 */
	public HSBColor getColor() {
		return color;
	}

	/**
	 * @return the status of isJolly attribute 
	 */
	public boolean getIsJolly() {
		return isJolly;
	}
	
}
