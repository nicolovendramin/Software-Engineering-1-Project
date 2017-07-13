package it.polimi.ingsw.cg25.model.dashboard;

/**
 * 
 * @author Giovanni
 *
 */
public class Councelor {

	/**
	 * The party of the councelor
	 */
	private final Party party;
	
	/**
	 * Councelor class constructor
	 * @param party the party of the councelor
	 * @exception IllegalArgumentException when party is null
	 */
	public Councelor(Party party) {
		if(party == null)
			throw new IllegalArgumentException("You can't create a Councelor without a party!");
		this.party = party;
	}

	/**
	 * @return the party of the councelor
	 */
	public Party getParty() {
		return party;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((party == null) ? 0 : party.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Councelor other = (Councelor) obj;
		return this.party.sameParty(other.getParty());
	}
	
}
