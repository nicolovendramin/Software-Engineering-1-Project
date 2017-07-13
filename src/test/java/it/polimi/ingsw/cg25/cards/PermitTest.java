/**
 * 
 */
package it.polimi.ingsw.cg25.cards;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.exceptions.NoCardsException;
import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.CoinBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.NobilityPointBonus;
import it.polimi.ingsw.cg25.model.dashboard.cards.PermitCard;
import it.polimi.ingsw.cg25.model.dashboard.cards.PermitDeck;
import it.polimi.ingsw.cg25.model.dashboard.topological.City;
import it.polimi.ingsw.cg25.model.dashboard.topological.CityColor;

/**
 * @author Davide
 *
 */
public class PermitTest {

	private ArrayList<City> validCities;
	private ArrayList<Bonus> bonuses;
	private List<PermitCard> permitCards;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		permitCards = new ArrayList<>();
		
		bonuses = new ArrayList<>();
		bonuses.add(new CoinBonus(5));
		bonuses.add(new NobilityPointBonus(10));

		validCities = new ArrayList<>();
		validCities.add(new City("Milan", new CityColor(new HSBColor(26, 68, 67), bonuses), bonuses));
		validCities.add(new City("Rome", new CityColor(new HSBColor(80, 76, 90), bonuses), bonuses));
	}

	@Test
	public void permitCardtest() {

		// Testing exceptions
		try {
			new PermitCard(new ArrayList<City>(), bonuses);
			fail("Ooops: a Permit Card with no cities?!");
		} catch (IllegalArgumentException e) {
		}

		try {
			new PermitCard(validCities, new ArrayList<Bonus>());
			fail("Ooops: a Permit Card with no bonuses?!");
		} catch (IllegalArgumentException e) {
		}

		PermitCard card = new PermitCard(validCities, bonuses);

		/* test takeAllBonuses() */

		assertTrue("Ooops: Valid cities are not the same",
				validCities.containsAll(card.getValidCities()) && card.getValidCities().containsAll(validCities));
		assertTrue("Ooops: Bonuses are not thesame",
				bonuses.containsAll(card.getBonuses()) && card.getBonuses().containsAll(bonuses));
		
		assertEquals("Cities letters error.","M, R", card.getCitiesFirstLetters());
		
		for (City vc : validCities) {
			assertTrue("Ooops: A valid city is marked as not valid by the Permit Card.",card.isValid(vc));
		}
		
	}
	
	@Test
	public void permitDeckTest() throws NoCardsException{
		bonuses.add(new CoinBonus(100));
		this.permitCards.add(new PermitCard(validCities,bonuses));
		bonuses.add(new NobilityPointBonus(200));
		this.permitCards.add(new PermitCard(validCities, bonuses));
		PermitDeck d = new PermitDeck(permitCards);
		// 2 carte nel mazzo
		assertTrue("First card different",permitCards.get(0).equals(d.drawCard()));
		assertTrue("Second card different",permitCards.get(1).equals(d.drawCard()));
		try {
			d.drawCard();
			fail("Ooops: more cards in the deck?!");
		} catch (NoCardsException e) {
		}
	}

}
