/**
 * 
 */
package it.polimi.ingsw.cg25.trade;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import it.polimi.ingsw.cg25.exceptions.CannotPassException;
import it.polimi.ingsw.cg25.model.Assistant;
import it.polimi.ingsw.cg25.model.Coin;
import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.NobilityRank;
import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.PocketCD4;
import it.polimi.ingsw.cg25.model.VictoryPoint;
import it.polimi.ingsw.cg25.model.dashboard.Party;
import it.polimi.ingsw.cg25.model.dashboard.bonus.AssistantBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.CoinBonus;
import it.polimi.ingsw.cg25.model.dashboard.cards.PermitCard;
import it.polimi.ingsw.cg25.model.dashboard.cards.PoliticsCard;
import it.polimi.ingsw.cg25.model.dashboard.topological.City;
import it.polimi.ingsw.cg25.model.trade.Market;
import it.polimi.ingsw.cg25.model.trade.ProductBonus;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;


/**
 * <b>Nota Bene</b><ul>
 * <li>Market is never opened with only one player (match ends first)
 * </ul>
 * @author Davide
 *
 */
public class MarketTest {
	private Market market;
	private MatchCD4 match;
	private ModelProxy proxy;
	public static List<PlayerCD4> players;
	public List<PlayerCD4> matchPlayers;
	private static List<HSBColor> colors = HSBColor.getNDifferent(5);
	public static PlayerCD4 player1;
	public static PlayerCD4 player2;
	public static PlayerCD4 player3;
	public static PlayerCD4 player4;
	private PocketCD4 pocket1;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Begin of Match initialization
		BoardFactory factory = new BoardFactory(new FileReader("src/test/resources/nobilityCellsFULL.txt"),
				new FileReader("src/test/resources/politicsFULL.txt"),
				new FileReader("src/test/resources/citiesFULL.txt"), new FileReader("src/test/resources/graphFULL.txt"),
				new FileReader("src/test/resources/kingFULL.txt"),
				new FileReader("src/test/resources/regionsFULL.txt"));
		proxy = new ModelProxy();
		match = new MatchCD4(factory.getBoard(), proxy,true,10);
		pocket1 = new PocketCD4(new Coin(100), new Assistant(100), new NobilityRank(1), new VictoryPoint(0));
		PocketCD4 pocket2 = new PocketCD4(new Coin(100), new Assistant(100), new NobilityRank(1), new VictoryPoint(0));
		player1 = new PlayerCD4(0, "gio", colors.get(0), match, pocket1);
		player2 = new PlayerCD4(1, "nicolo", colors.get(1), match, pocket2);
		player3 = new PlayerCD4(2, "dado", colors.get(2), match, pocket1);
		player1.setStatus(true);
		player3.setStatus(true);
		player2.setStatus(true);
		players = Arrays.asList(player1,player2,player3);
		for(PlayerCD4 p : players)
			match.addPlayer(p);
		match.getCurrentPlayer().addPoliticsCard(new PoliticsCard(new Party(new HSBColor(0, 0, 0), true)));
		match.getCurrentPlayer().addPoliticsCard(new PoliticsCard(new Party(new HSBColor(0, 0, 0), true)));
		match.getCurrentPlayer().addPoliticsCard(new PoliticsCard(new Party(new HSBColor(0, 0, 0), true)));
		match.getCurrentPlayer().addPoliticsCard(new PoliticsCard(new Party(new HSBColor(0, 0, 0), true)));
		match.getCurrentPlayer().addPoliticsCard(new PoliticsCard(new Party(new HSBColor(0, 0, 0), true)));
		match.getCurrentPlayer().addPoliticsCard(new PoliticsCard(new Party(new HSBColor(0, 0, 0), true)));
		match.getCurrentPlayer().addPoliticsCard(new PoliticsCard(new Party(new HSBColor(0, 0, 0), true)));
		match.getCurrentPlayer().addPoliticsCard(new PoliticsCard(new Party(new HSBColor(0, 0, 0), true)));
		List<City> cities = new ArrayList<>();
		List<Bonus> bonuses = new ArrayList<>();
		bonuses.add(new CoinBonus(19));
		cities.add(match.getBoard().getCities().get(0));
		match.getCurrentPlayer().getPermitsToBeUsed().add(new PermitCard(cities, bonuses));
		List<Bonus> bonuses2 = new ArrayList<>(bonuses);
		bonuses2.add(new AssistantBonus(20));
		match.getCurrentPlayer().getUsedPermits().add(new PermitCard(cities, bonuses2));
		matchPlayers = new ArrayList<>(match.getPlayers());
		// End of Match initialization
		
		market = new Market(match, proxy);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.trade.Market#Market(it.polimi.ingsw.cg25.model.MatchCD4, it.polimi.ingsw.cg25.proxies.ModelProxy)}.
	 */
	@Test (expected = NullPointerException.class)
	public void testMarketWithNullParameter() {
		new Market(null,proxy);
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.trade.Market#Market(it.polimi.ingsw.cg25.model.MatchCD4, it.polimi.ingsw.cg25.proxies.ModelProxy)}.
	 */
	@Test (expected = NullPointerException.class)
	public void testMarketWithNullParameter2() {
		new Market(match,null);
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.trade.Market#Market(it.polimi.ingsw.cg25.model.MatchCD4, it.polimi.ingsw.cg25.proxies.ModelProxy)}.
	 */
	@Test (expected = NullPointerException.class)
	public void testMarketWithNullParameter3() {
		new Market(null,null);
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.trade.Market#Market(it.polimi.ingsw.cg25.model.MatchCD4, it.polimi.ingsw.cg25.proxies.ModelProxy)}.
	 */
	@Test
	public void testMarketClosedWhenCreated() {
		assertFalse("Market is created opened",market.isOpen());
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.trade.Market#getPlayers()}.
	 */
	@Test
	public void atFirstPlayersShouldBeEmpty() {
		assertTrue("Players list not empty at market's creation",market.getPlayers().isEmpty());
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.trade.Market#openMarket()}.
	 */
	@Test
	public void testOpenMarket() {
		market.openMarket();
		assertTrue("Market is not opening",market.isOpen());
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.trade.Market#openMarket()}.
	 */
	@Test
	public void testFirstPhaseAtOpening() {
		market.openMarket();
		assertEquals("Market opening in second phase",true,market.getPhase());
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.trade.Market#openMarket()}.
	 */
	@Test
	public void testOnlyActivePlayer() {
		player2.setStatus(false);
		player4 = new PlayerCD4(3, "megan", colors.get(3), match, pocket1);
		player4.setStatus(true);
		player4.setExcluded(true);
		match.addPlayer(player4);
		market.openMarket();
		assertEquals("Wrong players' number",3,market.getPlayers().size());		
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.trade.Market#openMarket()}.
	 */
	@Test
	public void testAllPlayerAddedSameOrder() {
		market.openMarket();
		assertEquals("Wrong players' number",matchPlayers.size(),market.getPlayers().size());
		for(int i=0;i<matchPlayers.size();i++)
			assertEquals("Wrong players' order (phase 1)",matchPlayers.get(i).getName(),market.getPlayers().get(i).getName());
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.trade.Market#openMarket()}.
	 */
	@Test
	public void testEmptyOnSale() {
		market.openMarket();
		assertTrue("OnSale not empty at opening",market.getProducts().isEmpty());
	}
	

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.trade.Market#getCurrentPlayer()}.
	 */
	@Test
	public void testGetCurrentPlayer() {
		market.openMarket();
		assertEquals("getCurrentPlayer fails",matchPlayers.get(0).getName(),market.getCurrentPlayer().getName());
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.trade.Market#beginTurn()}.
	 */
	@Ignore
	public void testBeginTurn() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.trade.Market#endTurn()}.
	 * @throws CannotPassException 
	 */
	@Test
	public void testEndTurnRound() throws CannotPassException {
		market.openMarket();
		market.endTurn();
		// All 3 players are active
		assertEquals("Incrementing round counter when not necessary",0,market.getRoundNumber());
	}
	

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.trade.Market#endTurn()}.
	 * @throws CannotPassException 
	 */
	@Test
	public void testEndTurnLastPlayerOfRound() throws CannotPassException {
		market.openMarket();
		// All 3 players are active
		// Passing till last player
		for(int i =0; i<matchPlayers.size();i++){
			market.endTurn();
		}
		// NB: next player is random
		assertEquals("Not Incrementing round counter when necessary",1,market.getRoundNumber());
		assertFalse("Not changing phase",market.getPhase());
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.trade.Market#nextPlayer()}.
	 */
	@Test
	public void testNextPlayerNormal() {
		market.openMarket();
		assertEquals("nextPlayer fails",matchPlayers.get(1).getName(),market.nextPlayer().getName());
		assertTrue("market closed when not needed",market.isOpen());
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.trade.Market#nextPlayer()}.
	 */
	@Test
	public void testNextPlayerInactive() {
		market.openMarket();
		player2.setStatus(false);
		assertEquals("nextPlayer fails",matchPlayers.get(2).getName(),market.nextPlayer().getName());
		assertTrue("market closed when not needed",market.isOpen());
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.trade.Market#nextPlayer()}.
	 */
	@Test
	public void testNextPlayerAllButOneInactive() {
		market.openMarket();
		player2.setStatus(false);
		player3.setStatus(false);
		market.nextPlayer();
		assertFalse("market not closed when all players but one inactive",market.isOpen());
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.trade.Market#canPass()}.
	 */
	@Test
	public void testCanPass() {
		// Everyone can always pass (when proposed)
		assertTrue(market.canPass());
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.trade.Market#hasWon()}.
	 */
	@Test
	public void testHasWon() {
		// No one can win
		assertFalse(market.hasWon());
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.trade.Market#getRoundNumber()}.
	 */
	@Test
	public void testGetRoundNumber() {
		market.openMarket();
		assertEquals("round number error",0,market.getRoundNumber());
		market.endRound();
		assertEquals("round number error",1,market.getRoundNumber());
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.trade.Market#endRound()}.
	 * @throws CannotPassException 
	 */
	@Test
	public void testEndRound() throws CannotPassException {
		market.openMarket();
		for(int i=0;i<market.getPlayers().size();i++)
			market.endTurn();
		// round ended
		assertFalse("not changing phase",market.getPhase());
		assertTrue("market closed when not needed",market.isOpen());
		for(int i=0;i<market.getPlayers().size();i++)
			market.endTurn();
		// round ended
		assertFalse("market not closed",market.isOpen());
		
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.trade.Market#setOrder(java.util.List)}.
	 */
	@Ignore
	public void testSetOrder() {
		fail("Not yet implemented");
	}


	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.trade.Market#addProduct(it.polimi.ingsw.cg25.model.trade.Product)}.
	 */
	@Test
	public void testAddProduct() {
		ProductBonus p = new ProductBonus(5, player1, Arrays.asList(new AssistantBonus(1)), 1);
		market.addProduct(p);
		assertTrue("Product not added",market.getProducts().contains(p));
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.trade.Market#addProduct(it.polimi.ingsw.cg25.model.trade.Product)}.
	 */
	@Test (expected=NullPointerException.class)
	public void testAddNullProduct() {
		market.addProduct(null);
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.trade.Market#removeProduct(it.polimi.ingsw.cg25.model.trade.Product)}.
	 */
	@Test
	public void testRemoveProduct() {
		ProductBonus p = new ProductBonus(5, player1, Arrays.asList(new AssistantBonus(1)), 1);
		market.addProduct(p);
		market.removeProduct(p);
		assertFalse("Product not removed",market.getProducts().contains(p));
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.trade.Market#getAvailableActions()}.
	 */
	@Ignore
	public void testGetAvailableActions() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.trade.Market#getStartingAction()}.
	 */
	@Ignore
	public void testGetStartingAction() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.trade.Market#receiveAction(it.polimi.ingsw.cg25.actions.Action)}.
	 */
	@Ignore
	public void testReceiveAction() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.trade.Market#getNextProductTag()}.
	 */
	@Test
	public void testGetNextProductTag() {
		market.openMarket();
		assertEquals("Wrong tag starting number",0,market.getNextProductTag());
		assertEquals("Wrong tag",1,market.getNextProductTag());
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.trade.Market#closeMarket()}.
	 */
	@Test
	public void testCloseMarket() {
		market.openMarket();
		ProductBonus p = new ProductBonus(5, player1, Arrays.asList(new AssistantBonus(1)), 1);
		market.addProduct(p);
		market.closeMarket();
		assertFalse("market not closed",market.isOpen());
		assertTrue("not giving back products when closing market",market.getProducts().isEmpty());
	}

}
