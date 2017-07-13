package it.polimi.ingsw.cg25.dashboard;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.exceptions.NoGoodCardsException;
import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.dashboard.Councelor;
import it.polimi.ingsw.cg25.model.dashboard.Council;
import it.polimi.ingsw.cg25.model.dashboard.Party;
import it.polimi.ingsw.cg25.model.dashboard.cards.PoliticsCard;
import it.polimi.ingsw.cg25.parsing.PoliticsParser;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("unused")
public class CouncilTest {

	private InputStreamReader inStrRdr;
	private PoliticsParser polPar;
	
	@Before
	public void setUp() throws IOException {
		inStrRdr = new InputStreamReader(new FileInputStream("src/test/resources/politics.txt"));
		polPar = new PoliticsParser();
		polPar.parseLineByLine(inStrRdr);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ShouldNotBePossibleToCreateACouncilWithoutAValidLocation() {
		Queue<Councelor> councilQueue = new LinkedList<>();
		councilQueue.add(polPar.getCouncelors().get(0));
		councilQueue.add(polPar.getCouncelors().get(1));
		councilQueue.add(polPar.getCouncelors().get(2));
		councilQueue.add(polPar.getCouncelors().get(3));
		Council coun = new Council(councilQueue, null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ShouldNotBePossibleToCreateACouncilWithoutAValidQueue() {
		Council coun = new Council(null, "Sea");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ShouldNotBePossibleToCreateACouncilWithoutAnEmptyQueue() {
		Queue<Councelor> councilQueue = new LinkedList<>();
		Council coun = new Council(councilQueue, "Sea");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ShouldNotBePossibleToCreateACouncilWithoutAValidQueueOf4Councelors() {
		Queue<Councelor> councilQueue = new LinkedList<>();
		councilQueue.add(polPar.getCouncelors().get(0));
		councilQueue.add(polPar.getCouncelors().get(1));
		councilQueue.add(polPar.getCouncelors().get(2));
		councilQueue.add(polPar.getCouncelors().get(3));
		councilQueue.add(polPar.getCouncelors().get(4));
		Council coun = new Council(councilQueue, "Sea");
	}
	
	@Test
	public void testElectCouncelor() {
		Queue<Councelor> councilQueue = new LinkedList<>();
		councilQueue.add(polPar.getCouncelors().get(0));
		councilQueue.add(polPar.getCouncelors().get(1));
		councilQueue.add(polPar.getCouncelors().get(2));
		councilQueue.add(polPar.getCouncelors().get(3));
		Council coun = new Council(councilQueue, "Sea");
		//Elect a new councelor
		Councelor delCoun = coun.electCouncelor(polPar.getCouncelors().get(4));
		assertTrue(delCoun.getParty().sameParty(polPar.getCouncelors().get(0).getParty()));
		//Check council queue size
		assertEquals(4, coun.getCouncelors().size());
	}
	
	@Test
	public void testGetCouncelor() {
		Queue<Councelor> councilQueue = new LinkedList<>();
		councilQueue.add(polPar.getCouncelors().get(0));
		councilQueue.add(polPar.getCouncelors().get(1));
		councilQueue.add(polPar.getCouncelors().get(2));
		councilQueue.add(polPar.getCouncelors().get(3));
		Council coun = new Council(councilQueue, "Sea");
		
		assertEquals(4, coun.getCouncelors().size());
		
		coun.getCouncelors().forEach(c -> assertEquals(Councelor.class, c.getClass()));
	}
	
	@Test
	public void testGetLocation () {
		Queue<Councelor> councilQueue = new LinkedList<>();
		councilQueue.add(polPar.getCouncelors().get(0));
		councilQueue.add(polPar.getCouncelors().get(1));
		councilQueue.add(polPar.getCouncelors().get(2));
		councilQueue.add(polPar.getCouncelors().get(3));
		Council coun = new Council(councilQueue, "Sea");
		
		assertTrue("Sea".equals(coun.getLocation()));
	}
	
	@Test(expected=NoGoodCardsException.class)
	public void testGoodHandException() throws NoGoodCardsException {
		Queue<Councelor> councilQueue = new LinkedList<>();
		councilQueue.add(polPar.getCouncelors().get(0));
		councilQueue.add(polPar.getCouncelors().get(1));
		councilQueue.add(polPar.getCouncelors().get(2));
		councilQueue.add(polPar.getCouncelors().get(3));
		Council coun = new Council(councilQueue, "Sea");
		
		List<PoliticsCard> hand = new ArrayList<>();
		
		coun.goodHand(hand);
	}
	
	@Test
	public void testGoodHand() throws NoGoodCardsException {
		Queue<Councelor> councilQueue = new LinkedList<>();
		councilQueue.add(polPar.getCouncelors().get(0));
		councilQueue.add(polPar.getCouncelors().get(1));
		councilQueue.add(polPar.getCouncelors().get(2));
		councilQueue.add(polPar.getCouncelors().get(3));
		Council coun = new Council(councilQueue, "Sea");
		
		List<PoliticsCard> hand = new ArrayList<>();
		//TEST 1 CARD
		//Same party as councelor 0
		hand.add(new PoliticsCard(polPar.getCouncelors().get(0).getParty()));
		assertEquals(10, coun.goodHand(hand));
		//TEST 2 CARDS
		hand = new ArrayList<>();
		hand.add(new PoliticsCard(polPar.getCouncelors().get(0).getParty()));
		hand.add(new PoliticsCard(polPar.getCouncelors().get(2).getParty()));
		assertEquals(7, coun.goodHand(hand));
		//TEST 2 CARDS + 2 JOLLY
		hand = new ArrayList<>();
		hand.add(new PoliticsCard(polPar.getCouncelors().get(0).getParty()));
		hand.add(new PoliticsCard(polPar.getCouncelors().get(2).getParty()));
		hand.add(new PoliticsCard(new Party(new HSBColor(100, 100, 100), true)));
		hand.add(new PoliticsCard(new Party(new HSBColor(100, 100, 100), true)));
		assertEquals(2, coun.goodHand(hand));
		//TEST 3 CARDS
		hand = new ArrayList<>();
		hand.add(new PoliticsCard(polPar.getCouncelors().get(0).getParty()));
		hand.add(new PoliticsCard(polPar.getCouncelors().get(2).getParty()));
		hand.add(new PoliticsCard(polPar.getCouncelors().get(1).getParty()));
		assertEquals(4, coun.goodHand(hand));
		//TEST 3 CARDS + 1 Jolly
		hand = new ArrayList<>();
		hand.add(new PoliticsCard(polPar.getCouncelors().get(0).getParty()));
		hand.add(new PoliticsCard(new Party(new HSBColor(100, 100, 100), true)));
		hand.add(new PoliticsCard(polPar.getCouncelors().get(2).getParty()));
		hand.add(new PoliticsCard(polPar.getCouncelors().get(1).getParty()));
		assertEquals(1, coun.goodHand(hand));
		//TEST 4 CARDS
		hand = new ArrayList<>();
		hand.add(new PoliticsCard(polPar.getCouncelors().get(0).getParty()));
		hand.add(new PoliticsCard(polPar.getCouncelors().get(2).getParty()));
		hand.add(new PoliticsCard(polPar.getCouncelors().get(1).getParty()));
		hand.add(new PoliticsCard(polPar.getCouncelors().get(3).getParty()));
		assertEquals(0, coun.goodHand(hand));
		//TEST 4 JOLLY CARDS
		hand = new ArrayList<>();
		hand.add(new PoliticsCard(new Party(new HSBColor(100, 100, 100), true)));
		hand.add(new PoliticsCard(new Party(new HSBColor(100, 100, 100), true)));
		hand.add(new PoliticsCard(new Party(new HSBColor(100, 100, 100), true)));
		hand.add(new PoliticsCard(new Party(new HSBColor(100, 100, 100), true)));
		assertEquals(4, coun.goodHand(hand));
	}
	
}
