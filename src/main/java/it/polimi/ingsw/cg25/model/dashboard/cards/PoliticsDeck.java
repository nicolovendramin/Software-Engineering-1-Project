/**
 * 
 */
package it.polimi.ingsw.cg25.model.dashboard.cards;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg25.exceptions.NoCardsException;

/**
 * @author Davide
 *
 */
public class PoliticsDeck extends Deck<PoliticsCard> {

	/**
	 * Pile for discarded cards
	 */
	private List<PoliticsCard> discardPile = new ArrayList<>();

	/**
	 * Creates a new Deck of Politics Card
	 * @param cards List<{@link PoliticsCard}> the cards
	 */
	public PoliticsDeck(List<PoliticsCard> cards) {
		super(cards,true);
	}

	/**
	 * Discard a Politics Card
	 * 
	 * @param card
	 *            {@link PoliticsCard} : the card to be discarded
	 */
	public void discardCard(PoliticsCard card) {
		if(card == null)
			throw new IllegalArgumentException("Null card given");
		discardPile.add(card);
	}

	/**
	 * Draw a Politics Card, shuffling the discard pile and re-enqueuing if
	 * necessary and possible
	 * 
	 * @return {@link PoliticsCard} : the drawn card
	 * @throws NoCardsException
	 *             if deck and discard pile are both empty
	 */
	@Override

	public PoliticsCard drawCard() throws NoCardsException {
		// If draw pile is not empty draw a card normally
		if (!this.drawPile.isEmpty())
			return drawPile.poll();

		// If draw pile and discard pile are both empty throw exception
		if (discardPile.isEmpty())
			throw new NoCardsException();

		// If draw pile is empty and discard pile is not empty
		// Shuffle and enqueue a copy of the discard pile
		// Then clear the discard pile and draw a card normally

		ArrayList<PoliticsCard> discarded = new ArrayList<>(discardPile);

		this.enqueue(this.shuffle(discarded));
		discardPile.clear();

		return this.drawCard();
	}

}
