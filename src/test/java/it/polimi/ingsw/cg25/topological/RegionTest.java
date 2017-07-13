package it.polimi.ingsw.cg25.topological;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.exceptions.CannotCreateGameException;
import it.polimi.ingsw.cg25.exceptions.NoCardsException;
import it.polimi.ingsw.cg25.model.dashboard.Council;
import it.polimi.ingsw.cg25.model.dashboard.bonus.AssistantBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.BonusConcreteCreator;
import it.polimi.ingsw.cg25.model.dashboard.bonus.BonusCreator;
import it.polimi.ingsw.cg25.model.dashboard.bonus.CoinBonus;
import it.polimi.ingsw.cg25.model.dashboard.cards.PermitCard;
import it.polimi.ingsw.cg25.model.dashboard.cards.PermitDeck;
import it.polimi.ingsw.cg25.model.dashboard.cards.RewardCard;
import it.polimi.ingsw.cg25.model.dashboard.topological.City;
import it.polimi.ingsw.cg25.model.dashboard.topological.Region;
import it.polimi.ingsw.cg25.parsing.CitiesParser;
import it.polimi.ingsw.cg25.parsing.PoliticsParser;
import it.polimi.ingsw.cg25.parsing.RegionParser;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("unused")
public class RegionTest {

	private InputStreamReader inStrRdrRg;
	private InputStreamReader inStrRdrCity;
	private InputStreamReader inStrRdrPol;
	private PoliticsParser polPar;
	private CitiesParser cPar;
	private RegionParser rgPar;
	private BonusCreator creator;
	
	private Region regionToTestException;
	
	//Region's attributes
	private String name;
	private List<City> cities;
	private PermitDeck permitTiles;
	private Council council;
	private RewardCard bonusCard;
	private int numFacedUpPermits;
	
	private List<PermitCard> cards;
	
	/*
	 * In this test I'm gonna create regions by using regions parser and
	 * then a foo region which will be a copy of one of the above regions
	 * in order to trigger exceptions.
	 * After that there will be tests for regions's method
	 */
	
	@Before
	public void setUp() throws IOException, CannotCreateGameException {
		creator = new BonusConcreteCreator();
		//Here I'm gonna used a reduced file
		inStrRdrCity = new InputStreamReader(new FileInputStream("src/test/resources/cities.txt"));
		inStrRdrPol = new InputStreamReader(new FileInputStream("src/test/resources/politics.txt"));
		inStrRdrRg = new InputStreamReader(new FileInputStream("src/test/resources/regions.txt"));
		polPar = new PoliticsParser();
		polPar.parseLineByLine(inStrRdrPol);
		cPar = new CitiesParser(creator);
		cPar.parseLineByLine(inStrRdrCity);
		rgPar = new RegionParser(cPar.getCities(), creator, polPar.getCouncelors());
		//Create regions according to the files
		rgPar.parseLineByLine(inStrRdrRg);
		regionToTestException = rgPar.getRegions().get(0);
		
		//Init region's attributes
		this.name = regionToTestException.getName();
		this.cities = regionToTestException.getCities();
		//Create a permit deck cause it's unaccessible from region
		cards = new ArrayList<>();
		Bonus b = new CoinBonus(2);
		List<Bonus> bs = new ArrayList<>();
		bs.add(b);
		cards.add(new PermitCard(regionToTestException.getCities(), bs));
		cards.add(new PermitCard(regionToTestException.getCities(), bs));
		this.permitTiles = new PermitDeck(cards);
		this.council = regionToTestException.getCouncil();
		this.bonusCard = regionToTestException.getBonusCard();
		this.numFacedUpPermits = 2;
	}
	
	@Test(expected=NullPointerException.class)
	public void testNameNullPointerException() {
		Region r = new Region(null, this.cities, this.permitTiles, this.council, this.bonusCard, this.numFacedUpPermits);
	}
	
	@Test(expected=NullPointerException.class)
	public void testCitiesNullPointerException() {
		Region r = new Region(this.name, null, this.permitTiles, this.council, this.bonusCard, this.numFacedUpPermits);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCitiesIllegalArgumentException() {
		List<City> cts = new ArrayList<>();
		Region r = new Region(this.name, cts, this.permitTiles, this.council, this.bonusCard, this.numFacedUpPermits);
	}
	
	@Test(expected=NullPointerException.class)
	public void testPermitTilesNullPointerException() {
		Region r = new Region(this.name, this.cities, null, this.council, this.bonusCard, this.numFacedUpPermits);
	}
	
	@Test(expected=NullPointerException.class)
	public void testCouncilNullPointerException() {
		Region r = new Region(this.name, this.cities, this.permitTiles, null, this.bonusCard, this.numFacedUpPermits);
	}
	
	@Test(expected=NullPointerException.class)
	public void testBonusCardNullPointerException() {
		Region r = new Region(this.name, this.cities, this.permitTiles, this.council, null, this.numFacedUpPermits);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNumFacedUpPermitsIllegalArgumentException() {
		Region r = new Region(this.name, this.cities, this.permitTiles, this.council, this.bonusCard, 0);
	}
	
	@Test
	public void testGetName() {
		Region r = new Region(this.name, this.cities, this.permitTiles, this.council, this.bonusCard, this.numFacedUpPermits);
		assertEquals("mare", r.getName());
	}
	
	@Test
	public void testGetCities() {
		Region r = new Region(this.name, this.cities, this.permitTiles, this.council, this.bonusCard, this.numFacedUpPermits);
		r.getCities().forEach(c -> assertEquals(City.class, c.getClass()));
		assertEquals(1, r.getCities().size());
	}
	
	@Test
	public void testGetCouncil() {
		Region r = new Region(this.name, this.cities, this.permitTiles, this.council, this.bonusCard, this.numFacedUpPermits);
		assertEquals(Council.class, r.getCouncil().getClass());
		assertEquals(4, r.getCouncil().getCouncelors().size());
	}
	
	@Test
	public void testGetBonusCard() {
		Region r = new Region(this.name, this.cities, this.permitTiles, this.council, this.bonusCard, this.numFacedUpPermits);
		assertEquals(2, r.getBonusCard().getBonuses().size());
		assertEquals(CoinBonus.class, r.getBonusCard().getBonuses().get(0).getClass());
		CoinBonus cb = (CoinBonus) r.getBonusCard().getBonuses().get(0);
		assertEquals(3, cb.getNumberOfCoins());
		assertEquals(AssistantBonus.class, r.getBonusCard().getBonuses().get(1).getClass());
		AssistantBonus ab = (AssistantBonus) r.getBonusCard().getBonuses().get(1);
		assertEquals(1, ab.getNumberOfAssistants());
	}
	
	@Test
	public void testGetFaceUpPermits() {
		Region r = new Region(this.name, this.cities, this.permitTiles, this.council, this.bonusCard, this.numFacedUpPermits);
		assertEquals(2, r.getFaceUpPermits().size());
		assertTrue(cards.equals(r.getFaceUpPermits()));
	}
	
	@Test
	public void testGetPermit() throws NoCardsException {
		Region r = new Region(this.name, this.cities, this.permitTiles, this.council, this.bonusCard, this.numFacedUpPermits);
		PermitCard returnedCard = r.getPermit(0);
		//Check card
		assertTrue(returnedCard.equals(cards.get(0)));
		//Test number of faced up permits
		assertEquals(1, r.getFaceUpPermits().size());
	}
	
	@Test(expected=NoCardsException.class)
	public void testGetPermitWithNoCardsException() throws NoCardsException {
		Region r = new Region(this.name, this.cities, this.permitTiles, this.council, this.bonusCard, this.numFacedUpPermits);
		//Only 2 permits and all faced up
		r.getPermit(0);
		r.getPermit(0);
		//This call triggers the exception
		r.getPermit(0);
	}
	
	@Test
	public void testChangeFaceUpPermits() throws NoCardsException {
		Region r = new Region(this.name, this.cities, this.permitTiles, this.council, this.bonusCard, this.numFacedUpPermits);
		List<PermitCard> faceUpCopy = new ArrayList<>(r.getFaceUpPermits());
		r.changeFaceUpPermits();
		assertEquals(2, r.getFaceUpPermits().size());
		assertTrue(r.getFaceUpPermits().equals(faceUpCopy));
	}
	
	@Test(expected=NoCardsException.class)
	public void testChangeFaceUpPermitsWithNoCardsException() throws NoCardsException {
		Region r = new Region(this.name, this.cities, this.permitTiles, this.council, this.bonusCard, this.numFacedUpPermits);
		r.getPermit(0);
		//There is only a permit and it's faced up
		//This call triggers NoCardsException exception
		r.changeFaceUpPermits();
	}
	
}
