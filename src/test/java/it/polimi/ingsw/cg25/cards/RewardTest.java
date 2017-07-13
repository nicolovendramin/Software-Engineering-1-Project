/**
 * 
 */
package it.polimi.ingsw.cg25.cards;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.model.dashboard.bonus.AssistantBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.CoinBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.NobilityPointBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.VictoryPointBonus;
import it.polimi.ingsw.cg25.model.dashboard.cards.KingRewardsDeck;
import it.polimi.ingsw.cg25.model.dashboard.cards.RewardCard;

/**
 * @author Davide
 *
 */
public class RewardTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void bonusTest() {
		try {
			new NobilityPointBonus(-1);
			fail("Ooops: Nobility point bonus < 0");
		} catch (IllegalArgumentException e) {
		}

		try {
			new VictoryPointBonus(-1);
			fail("Ooops: Victory point bonus < 0");
		} catch (IllegalArgumentException e) {
		}
		
		try {
			new AssistantBonus(-1);
			fail("Ooops: Assistant bonus < 0");
		} catch (IllegalArgumentException e) {
		}
		
		try {
			new CoinBonus(-1);
			fail("Ooops: Coin bonus < 0");
		} catch (IllegalArgumentException e) {
		}


	}

	@Test
	public void rewardCardTest() {

		List<Bonus> bonuses = new ArrayList<>();
		
		try {
			new RewardCard(bonuses);
			fail("Ooops: empty bonuses list");
		} catch (IllegalArgumentException e) {
		}
		
		
		bonuses.add(new NobilityPointBonus(5));
		bonuses.add(new VictoryPointBonus(3));
		bonuses.add(new AssistantBonus(7));
		bonuses.add(new CoinBonus(8));

		RewardCard card = new RewardCard(bonuses);
		
		List<Bonus> cardBonuses = card.getBonuses();
		Bonus b;
		
		for(Iterator<Bonus> it = cardBonuses.iterator(); it.hasNext();){
			b = it.next();
			assertTrue("Card returned an unpassed bonus",bonuses.contains(b));
			it.remove();
		}
		assertTrue("Some bonuses are not returned by the card",bonuses.isEmpty());

	}
	
	@Test
	public void KingsRewardDeckTest(){
		List<RewardCard> cards = new ArrayList<>();
		List<Bonus> b = new ArrayList<>();
		b.add(new CoinBonus(500));
		cards.add(new RewardCard(b));
		//KingRewardsDeck d = new KingRewardsDeck(cards);
		new KingRewardsDeck(cards);
		// Abstract Deck's extra methods are tested in PoliticsTest
	}

}
