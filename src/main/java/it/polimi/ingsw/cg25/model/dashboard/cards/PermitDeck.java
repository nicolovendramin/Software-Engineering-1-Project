/**
 * 
 */
package it.polimi.ingsw.cg25.model.dashboard.cards;

import java.util.List;

/**
 * @author Davide
 *
 */
public class PermitDeck extends Deck<PermitCard> {
	/**
	 * Creates a new Deck of Permit Cards
	 * @param cards List<{@link PermitCard}> the cards
	 */
	public PermitDeck(List<PermitCard> cards) {
		super(cards,true);
	}
	
}
