/**
 * 
 */
package it.polimi.ingsw.cg25.cards;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.exceptions.NoCardsException;
import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.dashboard.Party;
import it.polimi.ingsw.cg25.model.dashboard.cards.*;

/**
 * @author Davide
 *
 */

public class PoliticsTest {

	private PoliticsDeck pDeck;
	private List<PoliticsCard> cards;
	private List<PoliticsCard> tempCards;
	private int cardsCount;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.cards = new ArrayList<>();
		this.tempCards = new ArrayList<>();
		cards.add(new PoliticsCard(new Party(new HSBColor(26, 68, 67), false)));
		cards.add(new PoliticsCard(new Party(new HSBColor(325, 81, 67), false)));
		cards.add(new PoliticsCard(new Party(new HSBColor(150, 100, 67), false)));
		cards.add(new PoliticsCard(new Party(new HSBColor(30, 100, 40), false)));
		cards.add(new PoliticsCard(new Party(new HSBColor(26, 100, 73), false)));
		
		this.pDeck = new PoliticsDeck(cards);
		this.cardsCount = cards.size();
	}
	
	@Test
	public void partyTest(){
		PoliticsCard c = new PoliticsCard(new Party(new HSBColor(26, 68, 67), false));
		assertTrue("Ooops: Party equals fails", c.getParty().sameParty(new Party(new HSBColor(26, 68, 67), false)));		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void emptyDeckTest(){
		new PoliticsDeck(new ArrayList<PoliticsCard>());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void nullCardsTest(){
		new PoliticsDeck(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void nullCards2Test(){
		new PoliticsDeck(Arrays.asList(cards.get(0),null,cards.get(1)));
	}
	
	@Test
	public void politicsDeckTest() throws NoCardsException {
		PoliticsCard drawed;
		
		for (int i = 0; i < this.cardsCount; i++) {
			drawed = this.pDeck.drawCard();
			assertTrue("Ooops: Drawed a card that has never been passed",cards.contains(drawed));
			cards.remove(drawed);
			tempCards.add(drawed);
		}
		
		// All cards should have been drawn now
		assertTrue("Ooops: some cards have not been drawed",cards.size()==0);
		
		// Deck should be empty now
		try {
			pDeck.drawCard();
			fail("Ooops: extra cards in the deck");
		} catch (NoCardsException e) {
		}
		
		for (PoliticsCard c : tempCards) {
			this.pDeck.discardCard(c);
		}
		
		// Drawing a card = shuffling and re-enqueuing
		for (int i = 0; i < this.cardsCount; i++) {
			drawed = this.pDeck.drawCard();
			assertTrue("Ooops: After re-enqueuing, drawed a card that has never been passed",tempCards.contains(drawed));
		}
		
		
		
	}

}
