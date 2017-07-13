package it.polimi.ingsw.cg25.dto;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.model.dashboard.bonus.AssistantBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.CoinBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.DrawPoliticsCardBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.MainActionBonus;
import it.polimi.ingsw.cg25.model.dashboard.cards.RewardCard;
import it.polimi.ingsw.cg25.model.dto.DTORewardCard;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOBonus;

public class DTORewardCardTest {

	
	static List<RewardCard> reward;
	List<DTORewardCard> converted;
	static List<RewardCard> junk;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		reward = new ArrayList<>();
		junk = new ArrayList<>();
		reward.add(new RewardCard(Arrays.asList(new CoinBonus(15))));
		reward.add(new RewardCard(Arrays.asList(new CoinBonus(15), new AssistantBonus(5))));
		reward.add(new RewardCard(Arrays.asList(new DrawPoliticsCardBonus(2), new CoinBonus(6))));
		reward.add(new RewardCard(Arrays.asList(new DrawPoliticsCardBonus(15),new AssistantBonus(5))));
		junk.add(new RewardCard(Arrays.asList(new CoinBonus(17))));
		junk.add(new RewardCard(Arrays.asList(new AssistantBonus(6),new CoinBonus(15))));
		junk.add(new RewardCard(Arrays.asList(new DrawPoliticsCardBonus(1),new CoinBonus(15))));
		junk.add(new RewardCard(Arrays.asList(new MainActionBonus(1))));
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		converted = new ArrayList<>();
	}

	@After
	public void tearDown() throws Exception {
	}

	
	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTORewardCard#decodeAll(java.util.List, java.util.List)}
	 * {@link it.polimi.ingsw.cg25.model.dto.DTORewardCard#convertAll(java.util.List)}
	 * {@link it.polimi.ingsw.cg25.model.dto.DTORewardCard#decode(java.util.List)}
	 * ..
	 */
	@Test
	public void testEncodingDecoding() {
		List<RewardCard> decoded = new ArrayList<>();
		converted = DTORewardCard.convertAll(reward);
		List<RewardCard> candidates = new ArrayList<>();
		candidates.addAll(reward);
		candidates.addAll(junk);
		Collections.shuffle(candidates);
		try {
			decoded = DTORewardCard.decodeAll(converted, candidates);
		} catch (ElementNotFoundException e) {
			fail("DTOReward: decoding failed, element not found");
		}
		// Exactly same reference
		for (int i = 0; i < decoded.size(); i++)
			assertTrue("DTOReward: decoding failed", reward.get(i) == decoded.get(i));
	}
	
	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTORewardCard#decodeAll(java.util.List, java.util.List)}
	 * @throws ElementNotFoundException 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNullParameterDecoding() throws ElementNotFoundException {
		DTORewardCard.decodeAll(null, new ArrayList<RewardCard>());
	}
	
	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTORewardCard#decodeAll(java.util.List, java.util.List)}
	 * @throws ElementNotFoundException 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNullParameterDecoding2() throws ElementNotFoundException {
		DTORewardCard.decodeAll(new ArrayList<DTORewardCard>(),null);
	}
	
	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTORewardCard#decodeAll(java.util.List, java.util.List)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNullParameterEncoding() {
		DTORewardCard.convertAll(null);
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTORewardCard#decodeAll(java.util.List, java.util.List)}
	 * .
	 */
	@Test
	public void testEmptyParameterEncoding() {
		assertTrue(DTORewardCard.convertAll(new ArrayList<RewardCard>()).isEmpty());
	}
	
	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTORewardCard#decode(java.util.List)}
	 * .
	 * 
	 * @throws ElementNotFoundException
	 */
	@Test(expected = ElementNotFoundException.class)
	public void testElementNotFoundDecode() throws ElementNotFoundException {
		DTORewardCard dto = new DTORewardCard(reward.get(0));
		dto.decode(junk);
	}
	
	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTORewardCard#equals(Object)}
	 * .
	 */
	@Test
	public void testEqualsWithDTO() {
		DTORewardCard dto = new DTORewardCard(reward.get(0));
		assertFalse("DTOReward: equals with DTO error", dto.equals(junk.get(0)));
		assertTrue("DTOReward: equals with DTO error", dto.equals(new DTORewardCard(reward.get(0))));
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTORewardCard#equals(Object)}
	 * .
	 */
	@Test
	public void testEqualsWithReward() {
		DTORewardCard dto = new DTORewardCard(reward.get(0));
		for (RewardCard r : junk)
			assertFalse("DTOReward: equals with Reward error " + r.toString(), dto.equals(r));
		assertTrue("DTOReward: equals with Reward error", dto.equals(reward.get(0)));
	}
	
	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTORewardCard#equals(Object)}
	 * .
	 */
	@Test
	public void testEqualsWithNull() {
		DTORewardCard dto = new DTORewardCard(reward.get(0));
		assertFalse("DTOReward: equals with null failed",dto.equals(null));
	}
	
	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTORewardCard#equals(Object)}
	 * .
	 */
	@Test
	public void testEqualsWithJunkStuff() {
		DTORewardCard dto = new DTORewardCard(reward.get(0));
		assertFalse("DTOPolitics: equals with junk stuff",dto.equals("JunkString!!!1!"));
	}
	

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTORewardCard#getBonuses()}
	 * .
	 */
	@Test
	public void testGetBonuses() {
		for(RewardCard r : reward){
			List<DTOBonus> dtoB = DTOBonus.convertAll(r.getBonuses());
			DTORewardCard dtoR = new DTORewardCard(r);
			assertTrue("DTOReward: getBonuses() returned different bonuses",dtoB.equals(dtoR.getBonuses()));
		}
	}


}
