package it.polimi.ingsw.cg25.model.dashboard.cards;

import it.polimi.ingsw.cg25.model.dashboard.Party;
import it.polimi.ingsw.cg25.model.trade.Sellable;

/**
 * @author Davide
 *
 */
public class PoliticsCard implements Card,Sellable{
	
	/**
	 * {@link Party} of the card
	 */
	private Party party;
	
	/**
	 * Create a new Politics Card
	 * @param party: {@link Party} the party of the card
	 */
	public PoliticsCard(Party party){
		this.party = party;
	}

	/**
	 * @return {@link Party} the party
	 */
	public Party getParty() {
		return party;
	}

	/** 
	 * Print the party
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Politics Card: Party = ");
		if(this.getParty().getIsJolly())
			sb.append("Jolly");
		else sb.append(this.getParty().getColor().printTag());
		return sb.toString();
	}
}
