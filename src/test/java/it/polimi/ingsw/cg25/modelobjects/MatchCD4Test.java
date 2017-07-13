/**
 * 
 */
package it.polimi.ingsw.cg25.modelobjects;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.polimi.ingsw.cg25.exceptions.CannotPassException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.EndGameException;
import it.polimi.ingsw.cg25.model.Assistant;
import it.polimi.ingsw.cg25.model.Coin;
import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.NobilityRank;
import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.PocketCD4;
import it.polimi.ingsw.cg25.model.VictoryPoint;
import it.polimi.ingsw.cg25.model.dashboard.Emporium;
import it.polimi.ingsw.cg25.model.dashboard.Party;
import it.polimi.ingsw.cg25.model.dashboard.bonus.AssistantBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.CoinBonus;
import it.polimi.ingsw.cg25.model.dashboard.cards.PermitCard;
import it.polimi.ingsw.cg25.model.dashboard.cards.PoliticsCard;
import it.polimi.ingsw.cg25.model.dashboard.topological.City;
import it.polimi.ingsw.cg25.model.trade.Market;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

/**
 * @author Davide
 *
 */
public class MatchCD4Test {

	private static PocketCD4 pocket1;
	public static MatchCD4 model;
	public static ModelProxy proxy;
	public static List<PlayerCD4> players;
	public List<PlayerCD4> matchPlayers;
	private static List<HSBColor> colors = HSBColor.getNDifferent(5);
	public static PlayerCD4 player1;
	public static PlayerCD4 player2;
	public static PlayerCD4 player3;
	public static PlayerCD4 player4;
	private static BoardFactory factory;
	public static int unemployedNumber;
	
	public void win(PlayerCD4 p){
		for(int i=0; i<model.numberOfEmporiumsToWin;i++)
			p.getPlayerEmporiums().add(new Emporium(p, factory.getBoard().getCities().get(0)));
	}
	
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
		factory = new BoardFactory(new FileReader("src/test/resources/nobilityCellsFULL.txt"),
				new FileReader("src/test/resources/politicsFULL.txt"),
				new FileReader("src/test/resources/citiesFULL.txt"), new FileReader("src/test/resources/graphFULL.txt"),
				new FileReader("src/test/resources/kingFULL.txt"),
				new FileReader("src/test/resources/regionsFULL.txt"));
		proxy = new ModelProxy();
		model = new MatchCD4(factory.getBoard(), proxy,true,10);
		pocket1 = new PocketCD4(new Coin(100), new Assistant(100), new NobilityRank(1), new VictoryPoint(0));
		PocketCD4 pocket2 = new PocketCD4(new Coin(100), new Assistant(100), new NobilityRank(1), new VictoryPoint(0));

		player1 = new PlayerCD4(0, "gio", colors.get(0), model, pocket1);
		player2 = new PlayerCD4(1, "nicolo", colors.get(1), model, pocket2);
		player3 = new PlayerCD4(2, "dado", colors.get(2), model, pocket1);
		player1.setStatus(true);
		player3.setStatus(true);
		player2.setStatus(true);
		players = Arrays.asList(player1,player2,player3);
		for(PlayerCD4 p : players)
			model.addPlayer(p);
		unemployedNumber = model.getBoard().getUnemployedCouncelors().size();
		model.getCurrentPlayer().addPoliticsCard(new PoliticsCard(new Party(new HSBColor(0, 0, 0), true)));
		model.getCurrentPlayer().addPoliticsCard(new PoliticsCard(new Party(new HSBColor(0, 0, 0), true)));
		model.getCurrentPlayer().addPoliticsCard(new PoliticsCard(new Party(new HSBColor(0, 0, 0), true)));
		model.getCurrentPlayer().addPoliticsCard(new PoliticsCard(new Party(new HSBColor(0, 0, 0), true)));
		model.getCurrentPlayer().addPoliticsCard(new PoliticsCard(new Party(new HSBColor(0, 0, 0), true)));
		model.getCurrentPlayer().addPoliticsCard(new PoliticsCard(new Party(new HSBColor(0, 0, 0), true)));
		model.getCurrentPlayer().addPoliticsCard(new PoliticsCard(new Party(new HSBColor(0, 0, 0), true)));
		model.getCurrentPlayer().addPoliticsCard(new PoliticsCard(new Party(new HSBColor(0, 0, 0), true)));
		List<City> cities = new ArrayList<>();
		List<Bonus> bonuses = new ArrayList<>();
		bonuses.add(new CoinBonus(19));
		cities.add(model.getBoard().getCities().get(0));
		model.getCurrentPlayer().getPermitsToBeUsed().add(new PermitCard(cities, bonuses));
		List<Bonus> bonuses2 = new ArrayList<>(bonuses);
		bonuses2.add(new AssistantBonus(20));
		model.getCurrentPlayer().getUsedPermits().add(new PermitCard(cities, bonuses2));
		
		matchPlayers = new ArrayList<>(model.getPlayers());
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		model.getLogger().close();
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#MatchCD4(it.polimi.ingsw.cg25.model.BoardCD4, it.polimi.ingsw.cg25.proxies.ModelProxy)}.
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testMatchCD4NullBoardFactory() {
		new MatchCD4(null, new ModelProxy(),false,10);
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#MatchCD4(it.polimi.ingsw.cg25.model.BoardCD4, it.polimi.ingsw.cg25.proxies.ModelProxy)}.
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testMatchCD4NullModelProxy() {
		new MatchCD4(factory.getBoard(), null,false,10);
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#MatchCD4(it.polimi.ingsw.cg25.model.BoardCD4, it.polimi.ingsw.cg25.proxies.ModelProxy)}.
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testMatchCD4NullParameters() {
		new MatchCD4(null, null,false,10);
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#MatchCD4(it.polimi.ingsw.cg25.model.BoardCD4, it.polimi.ingsw.cg25.proxies.ModelProxy)}.
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testMatchCD4NullParameters2() {
		new MatchCD4(null, null,false,10,null);
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#addMainAction(int)}.
	 * @throws CannotPerformActionException 
	 */
	@Test
	public void testAdd_Use_GetRemainingMainAction() throws CannotPerformActionException {
		// Starting with 1 main action
		assertEquals("Not starting with 1 available main action",1,model.getRemainingMainActions());
		model.addMainAction(0);
		assertEquals("Main Action counter fails",1+0,model.getRemainingMainActions());
		model.useMainAction();
		assertEquals("Main Action counter fails",1-1,model.getRemainingMainActions());
		model.addMainAction(1);
		assertEquals("Main Action counter fails",0+1,model.getRemainingMainActions());
		model.addMainAction(2);
		assertEquals("Main Action counter fails",1+2,model.getRemainingMainActions());
		while(model.getRemainingMainActions() > 0)
			model.useMainAction();
		try{
			model.useMainAction();
			fail("Action counter fails (becomes negative)");
		}catch(CannotPerformActionException e){}
		
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#useQuickAction()}.
	 * @throws CannotPerformActionException 
	 */
	@Test
	public void testUse_And_GetRemainingQuickAction() throws CannotPerformActionException {
		// Starting with 1 quick action
		assertEquals("Not starting with 1 available quick action",1,model.getRemainingQuickActions());
		model.useQuickAction();
		assertEquals("Quick Action counter fails",1-1,model.getRemainingQuickActions());
		// Now counter is 0
		try{
			model.useQuickAction();
			fail("Quick Action counter fails (becomes negative)");
		}catch(CannotPerformActionException e){}
	}


	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#informPlayer(it.polimi.ingsw.cg25.model.PlayerCD4)}.
	 */
	@Ignore
	public void testInformPlayer() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#getBoard()}.
	 */
	@Test 
	public void testGetBoard() {
		// Exactly same reference
		assertTrue("",model.getBoard() == factory.getBoard());
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#receiveAction(it.polimi.ingsw.cg25.actions.Action)}.
	 */
	@Ignore
	public void testReceiveAction() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#beginTurn()}.
	 * @throws CannotPerformActionException 
	 * @throws CannotPassException 
	 */
	@Test
	public void testBeginTurn() throws CannotPerformActionException, CannotPassException {
		int prevPolCardsCounter = model.getCurrentPlayer().getPoliticsCards().size();
		model.beginTurn();

		assertEquals("Not setting the Main action counter to 1 when when the turn starts",1,model.getRemainingMainActions());
		assertEquals("Not setting the Quick action counter to 1 when when the turn starts",1,model.getRemainingQuickActions());
		assertTrue("Match is not running after begin turn",model.isRunning());
		assertEquals("Not giving one card when the turn starts",prevPolCardsCounter+1,model.getCurrentPlayer().getPoliticsCards().size());
		
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#broadCastGameStatus()}.
	 */
	@Ignore
	public void testBroadCastGameStatus() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#endTurn()}.
	 * @throws CannotPerformActionException 
	 * @throws CannotPassException 
	 */
	@Test
	public void testEndTurnNormally() throws CannotPerformActionException, CannotPassException {
		// All 3 players are active
		int prevPlayerIndex = matchPlayers.indexOf(model.getCurrentPlayer());
		model.useMainAction();
		model.endTurn();
		assertEquals("Wrong player after endTurn()",(prevPlayerIndex + 1) % matchPlayers.size(),matchPlayers.indexOf(model.getCurrentPlayer()));
		// First plater passing: not ending a round
		assertEquals("Incrementing round counter when not necessary",0,model.getRoundNumber());
	}
	

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#endTurn()}.
	 * @throws CannotPerformActionException 
	 * @throws CannotPassException 
	 */
	@Test
	public void TestEndTurnLastPlayerOfTheRound() throws CannotPerformActionException, CannotPassException{
		// All 3 players are active
		// Passing till last player
		for(int i =0; i<matchPlayers.size()-1;i++){
			model.beginTurn();
			model.useMainAction();
			model.endTurn();
		}
		model.endTurn();
		assertEquals("Wrong player after endTurn()",0,matchPlayers.indexOf(model.getCurrentPlayer()));
		// First plater passing: not ending a round
		assertEquals("Not Incrementing round counter when necessary",1,model.getRoundNumber());
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#nextPlayer()()}.
	 * @throws EndGameException 
	 */
	@Test
	public void TestNextPlayerWithInactivePlayers() throws EndGameException{
				
		// Player 2 becomes inactive
		player2.setStatus(false);
		assertEquals("Wrong player after endTurn with inactive players",model.getPlayers().get(3-1).getName(),model.nextPlayer().getName());
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#nextPlayer()()}.
	 * @throws EndGameException 
	 */
	@Test
	public void TestNextPlayerWithExcludedPlayers() throws EndGameException{
				
		// Player 2 becomes inactive
		player2.setExcluded(true);
		assertEquals("Wrong player after endTurn with inactive players",model.getPlayers().get(3-1).getName(),model.nextPlayer().getName());
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#endTurn()()}.
	 * @throws EndGameException 
	 * @throws CannotPassException 
	 * @throws CannotPerformActionException 
	 */
	@Test
	public void testEndTurnWithOnlyOnePlayer() throws CannotPassException, CannotPerformActionException {
		MatchCD4 match = new MatchCD4(factory.getBoard(), proxy,true,10);
		assertTrue("Match is not running",match.isRunning());
		match.addPlayer(player1);
		match.useMainAction();
		match.endTurn();
		assertFalse("Match is not ending correctly",match.isRunning());
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#endRound()}.
	 * @throws EndGameException 
	 * @throws CannotPassException 
	 * @throws CannotPerformActionException 
	 */
	@Test
	public void testEndRoundMarketOpening() throws EndGameException {
		assertFalse("Market should be closed at the beginning of the match",model.getMarket().isOpen());
		model.endRound();
		assertTrue("Market should be opened after endRound",model.getMarket().isOpen());
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#endRound()}.
	 * @throws EndGameException 
	 * @throws CannotPassException 
	 * @throws CannotPerformActionException 
	 */
	@Test
	public void testEndRoundWithOutMarket() throws EndGameException, CannotPerformActionException, CannotPassException {
		
		MatchCD4 match = new MatchCD4(factory.getBoard(), proxy,false,10);
		List<PocketCD4> pockets = Arrays.asList(
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(0), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(2), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(2), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(4), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(1), new NobilityRank(4), new VictoryPoint(0))
				);
		players = Arrays.asList(
				new PlayerCD4(2, "B_Nik", colors.get(1), match, pockets.get(1)),
				new PlayerCD4(4, "D_Megan", colors.get(3), match, pockets.get(3)),
				new PlayerCD4(3, "C_Dado", colors.get(2), match, pockets.get(2)),
				new PlayerCD4(1, "A_Gio", colors.get(0), match, pockets.get(0)),
				new PlayerCD4(5, "E_Lena", colors.get(4), match, pockets.get(4))
				);
		for(PlayerCD4 p : players){
			p.setStatus(true);
			match.addPlayer(p);
		}
		
		for(int i=0;i<match.getPlayers().size();i++){
			match.beginTurn();
			match.useMainAction();
			match.endTurn();
		}

		//WITHOUT MARKET!
		
		assertEquals("Round counter fails",1,match.getRoundNumber());
		assertEquals("Wrong player's turn",match.getPlayers().get(0).getName(),match.getCurrentPlayer().getName());
		assertTrue("Match is not running",match.isRunning());
		
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#canPass()}.
	 */
	@Test
	public void testCantPassAfterNoActions() {
		assertFalse("Why I can pass after no actions?",model.canPass());
	}	
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#canPass()}.
	 */
	@Test
	public void testCantPassWith2orMoreMainActionsAvail() {
		model.addMainAction(2);
		assertFalse("Why I can pass with positive remaining action's counter?",model.canPass());
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#canPass()}.
	 * @throws CannotPerformActionException 
	 */
	@Test
	public void testCantPassAfterQuickActions() throws CannotPerformActionException {
		model.useQuickAction();
		assertFalse("Why I can pass after a quick action?",model.canPass());
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#canPass()}.
	 * @throws CannotPerformActionException 
	 */
	@Test
	public void testCanPassAfterMainActions() throws CannotPerformActionException {
		model.useMainAction();
		assertTrue("I could be able to pass after a main action",model.canPass());
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#hasWon()}.
	 */
	@Test
	public void testNotHasWonWhenStartingMatch() {
		assertFalse("I should not have already won?",model.hasWon());
		win(model.getCurrentPlayer());
		assertTrue("I should have won",model.hasWon());
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#whoWon()}.
	 */
	@Ignore
	public void testWhoWon() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#getRoundNumber()}.
	 * @throws EndGameException 
	 */
	@Test
	public void testRoundNumber() throws EndGameException {
		assertEquals("Round counter fails",0,model.getRoundNumber());
		model.endRound();
		assertEquals("Round counter fails",1,model.getRoundNumber());
		
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4}.
	 * @throws EndGameException 
	 * @throws CannotPerformActionException 
	 * @throws CannotPassException 
	 */
	@Test 	
	public void testIfWonExcluded() throws CannotPerformActionException, CannotPassException {
		win(model.getCurrentPlayer());
		model.useMainAction();
		model.endTurn();
		assertTrue("Why who won is now inactive?",model.getPlayers().get(0).isActive());
		assertTrue("Why who won is not excluded?",model.getPlayers().get(0).isExcluded());
		assertFalse("Setting up last round in the middle of the round when someone wins",model.isLastRound());	
	}
	
	@Test
	public void testIfWonLastPlayerCheckLastRound() throws CannotPerformActionException, CannotPassException{
		// Pass till last player
		for(int i=0;i<matchPlayers.size()-1;i++){
			model.beginTurn();
			model.useMainAction();
			model.endTurn();
		}
		model.beginTurn();
		win(model.getCurrentPlayer());;
		model.useMainAction();
		model.endTurn();
		assertTrue("Not setting up last round when someone wins",model.isLastRound());	
		
	}
	
	@Test
	public void testIfWonPlayedLastRound() throws CannotPerformActionException, CannotPassException{
		model.beginTurn();
		// First player wins
		win(model.getCurrentPlayer());
		model.useMainAction();
		model.endTurn();
		// Pass till last player
		for(int i=1;i<matchPlayers.size()-1;i++){
			model.beginTurn();
			model.useMainAction();
			model.endTurn();
		}
		model.beginTurn();
		model.useMainAction();
		model.endTurn();
		assertTrue("Not setting up last round when someone wins",model.isLastRound());	
		assertTrue("Match is not running when someone wins, during last found",model.isRunning());
		assertTrue("Market not opened when someone wins, before last round",model.getMarket().isOpen());
		assertEquals("Wrong player DURING last round at first turn",model.getPlayers().get(1).getName(),model.getCurrentPlayer().getName());
		// Pass till last player
		for(int i=0;i<matchPlayers.size()-2;i++){
			model.beginTurn();
			model.useMainAction();
			model.endTurn();
		}
		model.beginTurn();
		model.useMainAction();
		model.endTurn();
		assertFalse("Match is not ending correctly when someone wins, after last found",model.isRunning());
		assertTrue("Last round is not staying up after match's end the match",model.isLastRound());
	}
	
	@Test
	public void testIfFirstWonPlayedLastRoundTwoPlayers() throws CannotPerformActionException, CannotPassException{
		model.getPlayers().remove(model.getPlayers().size()-1);
		matchPlayers = model.getPlayers();
		model.beginTurn();
		// First player wins
		win(model.getCurrentPlayer());
		model.useMainAction();
		model.endTurn();
		// Pass till last player
		for(int i=1;i<matchPlayers.size()-1;i++){
			model.beginTurn();
			model.useMainAction();
			model.endTurn();
		}
		model.beginTurn();
		model.useMainAction();
		model.endTurn();
		assertTrue("Not setting up last round when someone wins",model.isLastRound());	
		assertTrue("Match is not running when someone wins, during last found",model.isRunning());
		assertTrue("Market not opened when someone wins, before last round",model.getMarket().isOpen());
		assertEquals("Wrong player DURING last round at first turn",model.getPlayers().get(1).getName(),model.getCurrentPlayer().getName());
		// Pass till last player
		for(int i=0;i<matchPlayers.size()-2;i++){
			model.beginTurn();
			model.useMainAction();
			model.endTurn();
		}
		model.beginTurn();
		model.useMainAction();
		model.endTurn();
		assertFalse("Match is not ending correctly when someone wins, after last found",model.isRunning());
		assertTrue("Last round is not staying up after match's end the match",model.isLastRound());
	}
	
	@Test
	public void testIfSecondWonPlayedLastRoundTwoPlayers() throws CannotPerformActionException, CannotPassException{
		model.getPlayers().remove(model.getPlayers().size()-1);
		matchPlayers = model.getPlayers();
		model.beginTurn();
		model.useMainAction();
		model.endTurn();
		// Pass till last player
		for(int i=1;i<matchPlayers.size()-1;i++){
			model.beginTurn();
			model.useMainAction();
			model.endTurn();
		}
		model.beginTurn();
		model.useMainAction();
		// Second player wins
		win(model.getCurrentPlayer());
		model.endTurn();
		assertTrue("Not setting up last round when someone wins",model.isLastRound());	
		assertTrue("Match is not running when someone wins, during last found",model.isRunning());
		assertTrue("Market not opened when someone wins, before last round",model.getMarket().isOpen());
		assertEquals("Wrong player DURING last round at first turn",model.getPlayers().get(0).getName(),model.getCurrentPlayer().getName());
		// Pass till last player
		for(int i=0;i<matchPlayers.size()-2;i++){
			model.beginTurn();
			model.useMainAction();
			model.endTurn();
		}
		model.beginTurn();
		model.useMainAction();
		model.endTurn();
		assertFalse("Match is not ending correctly when someone wins, after last found",model.isRunning());
		assertTrue("Last round is not staying up after match's end the match",model.isLastRound());
	}
	
	@Test
	public void testIfWonWithInactivePlayers() throws CannotPerformActionException, CannotPassException{
		// Player 1 becomes inactive
		model.beginTurn();
		player1.setStatus(false);
		model.useMainAction();
		model.endTurn();
		
		// Second player wins
		model.beginTurn();
		win(model.getCurrentPlayer());
		model.useMainAction();
		model.endTurn();
		
		// Pass till last player
		for(int i=2;i<matchPlayers.size()-1;i++){
			model.beginTurn();
			model.useMainAction();
			model.endTurn();
		}
		
		model.beginTurn();
		model.useMainAction();
		model.endTurn();
		
		assertTrue("Not setting up last round when someone wins",model.isLastRound());	
		assertTrue("Match is not running when someone wins, during last found",model.isRunning());
		assertTrue("Market not opened when someone wins, before last round",model.getMarket().isOpen());
		assertEquals("Wrong player DURING last round at first turn",model.getPlayers().get(3-1).getName(),model.getCurrentPlayer().getName());
		
		model.beginTurn();
		model.useMainAction();
		model.endTurn();
		
		assertFalse("Match is not ending correctly when someone wins, after last found",model.isRunning());
		assertTrue("Last round is not staying up after match's end the match",model.isLastRound());
	}
	
	@Test
	public void testIfMoreThanOnePlayerWon() throws CannotPerformActionException, CannotPassException{

		// Player 1 wins
		model.beginTurn();
		win(model.getCurrentPlayer());
		model.useMainAction();
		model.endTurn();
		// Pass till last player
		for(int i=1;i<matchPlayers.size()-1;i++){
			model.beginTurn();
			model.useMainAction();
			model.endTurn();
		}
		// Last Player (3) wins
		model.beginTurn();
		win(model.getCurrentPlayer());	
		model.useMainAction();
		model.endTurn();
		assertTrue("Not setting up last round when someone wins",model.isLastRound());	
		assertTrue("Match is not running when someone wins, during last found",model.isRunning());
		assertTrue("Market not opened when someone wins, before last round",model.getMarket().isOpen());
		assertEquals("Wrong player DURING last round at first turn",model.getPlayers().get(2-1).getName(),model.getCurrentPlayer().getName());
		
		model.beginTurn();
		model.useMainAction();
		model.endTurn();
		
		assertFalse("Match is not ending correctly when someone wins, after last found",model.isRunning());
		assertTrue("Last round is not staying up after match's end the match",model.isLastRound());
	}
	
	@Test
	public void testIfAllPlayersWon() throws CannotPerformActionException, CannotPassException{
		// Every player plays and wins
		for(int i=0;i<matchPlayers.size();i++){
			model.beginTurn();
			win(model.getCurrentPlayer());
			model.useMainAction();
			model.endTurn();
		}
		assertFalse("Market is opened when everyone win",model.getMarket().isOpen());
		assertFalse("Setting up last round when everyone win",model.isLastRound());	
		assertFalse("Match is not running when someone wins, during last found",model.isRunning());
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#setOrder(java.util.List)}.
	 */
	@Test
	public void testSetOrder() {
		assertEquals("Players size is different",players.size(),model.getPlayers().size());
		// TODO: check if players are the same
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#getCurrentPlayer()}.
	 * @throws CannotPerformActionException 
	 * @throws CannotPassException 
	 */
	@Test
	public void testGetCurrentPlayer() throws CannotPerformActionException, CannotPassException {
		assertTrue("getCurrentPlayer error",model.getCurrentPlayer()==matchPlayers.get(0));
		model.beginTurn();
		model.useMainAction();
		model.endTurn();
		assertTrue("getCurrentPlayer error",model.getCurrentPlayer()==matchPlayers.get(1));
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#addPlayer(it.polimi.ingsw.cg25.model.PlayerCD4)}.
	 */
	@Test
	public void testAddPlayerPlayerCD4() {
		int playerCounter = model.getPlayers().size();
		player4 = new PlayerCD4(0, "megan", colors.get(4), model, pocket1);
		model.addPlayer(player4);
		assertEquals("addPlayer: size error",playerCounter+1,model.getPlayers().size());
		assertEquals("addPlayer: player error",player4.getName(),model.getPlayers().get(playerCounter).getName());
	}
	
	@Test
	public void testHasMarket(){
		MatchCD4 match = new MatchCD4(factory.getBoard(), proxy,true,10);
		assertTrue("hasMarket error",match.hasMarket());
		
		MatchCD4 match2 = new MatchCD4(factory.getBoard(), proxy,false,10);
		assertFalse("hasMarket error",match2.hasMarket());
	}
	
	@Test
	public void testWithOutMarketEndTurn() throws CannotPerformActionException, CannotPassException{
		MatchCD4 match = new MatchCD4(factory.getBoard(), proxy,false,10);
		match.addPlayer(player1);
		match.addPlayer(player2);
		
		//Player 1 pass
		model.beginTurn();
		model.useMainAction();
		model.endTurn();
		
		//Player 2 pass
		model.beginTurn();
		model.useMainAction();
		model.endTurn();
		
		assertTrue("Market exixts when it must not",match.getMarket()==null);
		assertEquals("Something went wrong with turn after unexisting market session",matchPlayers.get(0).getName(),match.getCurrentPlayer().getName());
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#getMarket()}.
	 */
	@Test
	public void testGetMarket() {
		assertTrue("GetMarket class missmatch",model.getMarket().getClass() == Market.class);
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#actionStackIsEmpty()}.
	 */
	@Ignore
	public void testActionStackIsEmpty() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#pushAction(it.polimi.ingsw.cg25.actions.Action)}.
	 */
	@Ignore
	public void testPushAction() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#popAction()}.
	 */
	@Ignore
	public void testPopAction() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#getActionsStack()}.
	 */
	@Ignore
	public void testGetActionsStack() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#setActionsStack(java.util.Deque)}.
	 */
	@Ignore
	public void testSetActionsStack() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#getAvailableActions()}.
	 */
	@Ignore
	public void testGetAvailableActions() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#getStartingAction()}.
	 */
	@Ignore
	public void testGetStartingAction() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#getLogger()}.
	 */
	@Ignore
	public void testGetLogger() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#getStatus(int)}.
	 */
	@Ignore
	public void testGetStatus() {
		fail("Not yet implemented");
	}


	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#whoWon()}.
	 */
	@Test
	public void winningPointsWithNoRepeatedScore() {
		MatchCD4 match = new MatchCD4(factory.getBoard(), proxy,true,10);
		List<PocketCD4> pockets = Arrays.asList(
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(0), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(1), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(2), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(3), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(4), new VictoryPoint(0))
				);
		players = Arrays.asList(
				new PlayerCD4(4, "D_Megan", colors.get(3), match, pockets.get(3)),
				new PlayerCD4(1, "A_Gio", colors.get(0), match, pockets.get(0)),
				new PlayerCD4(5, "E_Lena", colors.get(4), match, pockets.get(4)),
				new PlayerCD4(2, "B_Nik", colors.get(1), match, pockets.get(1)),
				new PlayerCD4(3, "C_Dado", colors.get(2), match, pockets.get(2))
				);
		for(PlayerCD4 p : players){
			p.setStatus(true);
			match.addPlayer(p);
		}
		
		List<PlayerCD4> score = match.whoWon();
		
		// + 3 Victory Points because all players have the same (zero) permit cards
		
		assertTrue("Error no repeated, 0+3 extra points expected",match.getPlayers().get(1).getPocket().getVictoryPoints().getSupply()==0+3);
		assertTrue("Error no repeated, 0+3 extra points expected",match.getPlayers().get(3).getPocket().getVictoryPoints().getSupply()==0+3);
		assertTrue("Error no repeated, 0+3 extra points expected",match.getPlayers().get(4).getPocket().getVictoryPoints().getSupply()==0+3);
		
		assertTrue("Error no repeated, 2+3 extra points expected",match.getPlayers().get(0).getPocket().getVictoryPoints().getSupply()==2+3);
		assertTrue("Error no repeated, 5+3 extra points expected",match.getPlayers().get(2).getPocket().getVictoryPoints().getSupply()==5+3);
		
		
		assertEquals("Wrong scoreboard","E_Lena",score.get(0).getName());
		assertEquals("Wrong scoreboard","D_Megan",score.get(1).getName());
		assertEquals("Wrong scoreboard","A_Gio",score.get(2).getName());
		assertEquals("Wrong scoreboard","B_Nik",score.get(3).getName());;
		assertEquals("Wrong scoreboard","C_Dado",score.get(4).getName());
		
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#whoWon()}.
	 */
	@Test
	public void winningPointsWithRepeatedNobFirst() {
		MatchCD4 match = new MatchCD4(factory.getBoard(), proxy,true,10);
		List<PocketCD4> pockets = Arrays.asList(
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(0), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(1), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(4), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(4), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(1), new NobilityRank(4), new VictoryPoint(0))
				);
		players = Arrays.asList(
				new PlayerCD4(1, "A_Gio", colors.get(0), match, pockets.get(0)),
				new PlayerCD4(5, "E_Lena", colors.get(4), match, pockets.get(4)),
				new PlayerCD4(2, "B_Nik", colors.get(1), match, pockets.get(1)),
				new PlayerCD4(4, "D_Megan", colors.get(3), match, pockets.get(3)),
				new PlayerCD4(3, "C_Dado", colors.get(2), match, pockets.get(2))
				);
		for(PlayerCD4 p : players){
			p.setStatus(true);
			match.addPlayer(p);
		}
		
		// 1 Assistant to E_Lena resolves the ex aequo
		// + 3 Victory Points because all players have the same (zero) permit cards
		
		List<PlayerCD4> score = match.whoWon();
		
		assertTrue("Error repeated Nob first, 0+3 extra points expected",match.getPlayers().get(0).getPocket().getVictoryPoints().getSupply()==0+3);
		assertTrue("Error repeated Nob first, 0+3 extra points expected",match.getPlayers().get(2).getPocket().getVictoryPoints().getSupply()==0+3);
		
		assertTrue("Error repeated Nob first, 5 extra points expected",match.getPlayers().get(4).getPocket().getVictoryPoints().getSupply()==5+3);
		assertTrue("Error repeated Nob first, 5 extra points expected",match.getPlayers().get(3).getPocket().getVictoryPoints().getSupply()==5+3);
		assertTrue("Error repeated Nob first, 5+1 extra points expected",match.getPlayers().get(1).getPocket().getVictoryPoints().getSupply()==5+3+1);
		
		assertEquals("Wrong scoreboard",match.getPlayers().get(1).getName(),score.get(0).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(3).getName(),score.get(1).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(4).getName(),score.get(2).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(0).getName(),score.get(3).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(2).getName(),score.get(4).getName());
		
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#whoWon()}.
	 */
	@Test
	public void winningPointsWithRepeatedNobSecond() {
		MatchCD4 match = new MatchCD4(factory.getBoard(), proxy,true,10);
		List<PocketCD4> pockets = Arrays.asList(
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(0), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(1), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(2), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(2), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(4), new VictoryPoint(0))
				);
		players = Arrays.asList(
				new PlayerCD4(2, "B_Nik", colors.get(1), match, pockets.get(1)),
				new PlayerCD4(1, "A_Gio", colors.get(0), match, pockets.get(0)),
				new PlayerCD4(5, "E_Lena", colors.get(4), match, pockets.get(4)),
				new PlayerCD4(3, "C_Dado", colors.get(2), match, pockets.get(2)),
				new PlayerCD4(4, "D_Megan", colors.get(3), match, pockets.get(3))
				);
		for(PlayerCD4 p : players){
			p.setStatus(true);
			match.addPlayer(p);
		}

		// + 3 Victory Points because all players have the same (zero) permit cards
		
		List<PlayerCD4> score = match.whoWon();
		
		assertTrue("Error repeated Nob second, 0 extra points expected",match.getPlayers().get(1).getPocket().getVictoryPoints().getSupply()==0+3);
		assertTrue("Error repeated Nob second, 0 extra points expected",match.getPlayers().get(0).getPocket().getVictoryPoints().getSupply()==0+3);
		
		assertTrue("Error repeated Nob second, 2 extra points expected",match.getPlayers().get(3).getPocket().getVictoryPoints().getSupply()==2+3);
		assertTrue("Error repeated Nob second, 2 extra points expected",match.getPlayers().get(4).getPocket().getVictoryPoints().getSupply()==2+3);
		assertTrue("Error repeated Nob second, 5 extra points expected",match.getPlayers().get(2).getPocket().getVictoryPoints().getSupply()==5+3);
		
		assertEquals("Wrong scoreboard",match.getPlayers().get(2).getName(),score.get(0).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(3).getName(),score.get(1).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(4).getName(),score.get(2).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(0).getName(),score.get(3).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(1).getName(),score.get(4).getName());
		
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#whoWon()}.
	 */
	@Test
	public void winningPointsWithRepeatedNobFirstAndSecond() {
		MatchCD4 match = new MatchCD4(factory.getBoard(), proxy,true,10);
		List<PocketCD4> pockets = Arrays.asList(
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(0), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(2), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(2), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(1), new NobilityRank(4), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(4), new VictoryPoint(0))
				);
		players = Arrays.asList(
				new PlayerCD4(2, "B_Nik", colors.get(1), match, pockets.get(1)),
				new PlayerCD4(4, "D_Megan", colors.get(3), match, pockets.get(3)),
				new PlayerCD4(3, "C_Dado", colors.get(2), match, pockets.get(2)),
				new PlayerCD4(1, "A_Gio", colors.get(0), match, pockets.get(0)),
				new PlayerCD4(5, "E_Lena", colors.get(4), match, pockets.get(4))
				);
		for(PlayerCD4 p : players){
			p.setStatus(true);
			match.addPlayer(p);
		}
		
		// 1 Assistant to D_Megan resolves the ex aequo
		// + 3 Victory Points because all players have the same (zero) permit cards
		
		List<PlayerCD4> score = match.whoWon();

		assertTrue("Error repeated Nob first & second, 0 extra points expected",match.getPlayers().get(3).getPocket().getVictoryPoints().getSupply()==0+3);
		assertTrue("Error repeated Nob first & second, 0 extra points expected",match.getPlayers().get(0).getPocket().getVictoryPoints().getSupply()==0+3);
		assertTrue("Error repeated Nob first & second, 0 extra points expected",match.getPlayers().get(2).getPocket().getVictoryPoints().getSupply()==0+3);
		
		assertTrue("Error repeated Nob first & second, 5+1 extra points expected",match.getPlayers().get(1).getPocket().getVictoryPoints().getSupply()==5+3+1);
		assertTrue("Error repeated Nob first & second, 5 extra points expected",match.getPlayers().get(4).getPocket().getVictoryPoints().getSupply()==5+3);

		
		assertEquals("Wrong scoreboard",match.getPlayers().get(1).getName(),score.get(0).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(4).getName(),score.get(1).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(0).getName(),score.get(2).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(2).getName(),score.get(3).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(3).getName(),score.get(4).getName());
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#whoWon()}.
	 */
	@Test
	public void winningPointsOnlyOnce() {
		MatchCD4 match = new MatchCD4(factory.getBoard(), proxy,true,10);
		List<PocketCD4> pockets = Arrays.asList(
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(0), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(2), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(2), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(4), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(1), new NobilityRank(4), new VictoryPoint(0))
				);
		players = Arrays.asList(
				new PlayerCD4(2, "B_Nik", colors.get(1), match, pockets.get(1)),
				new PlayerCD4(4, "D_Megan", colors.get(3), match, pockets.get(3)),
				new PlayerCD4(3, "C_Dado", colors.get(2), match, pockets.get(2)),
				new PlayerCD4(1, "A_Gio", colors.get(0), match, pockets.get(0)),
				new PlayerCD4(5, "E_Lena", colors.get(4), match, pockets.get(4))
				);
		for(PlayerCD4 p : players){
			p.setStatus(true);
			match.addPlayer(p);
		}

		List<PlayerCD4> score = match.whoWon();

		// 1 Assistant to E_Lena resolves the ex aequo
		// + 3 Victory Points because all players have the same (zero) permit cards
		
		assertTrue("Error repeated Nob first & second, 0 extra points expected",match.getPlayers().get(3).getPocket().getVictoryPoints().getSupply()==0+3);
		assertTrue("Error repeated Nob first & second, 0 extra points expected",match.getPlayers().get(0).getPocket().getVictoryPoints().getSupply()==0+3);
		assertTrue("Error repeated Nob first & second, 0 extra points expected",match.getPlayers().get(2).getPocket().getVictoryPoints().getSupply()==0+3);
		
		assertTrue("Error repeated Nob first & second, 5 extra points expected",match.getPlayers().get(1).getPocket().getVictoryPoints().getSupply()==5+3);
		assertTrue("Error repeated Nob first & second, 5+1 extra points expected",match.getPlayers().get(4).getPocket().getVictoryPoints().getSupply()==5+3+1);
		
		assertEquals("Wrong scoreboard",match.getPlayers().get(4).getName(),score.get(0).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(1).getName(),score.get(1).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(0).getName(),score.get(2).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(2).getName(),score.get(3).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(3).getName(),score.get(4).getName());
		
		score = match.whoWon();
		
		assertTrue("Error repeated Nob first & second, 0 extra points expected",match.getPlayers().get(3).getPocket().getVictoryPoints().getSupply()==0+3);
		assertTrue("Error repeated Nob first & second, 0 extra points expected",match.getPlayers().get(0).getPocket().getVictoryPoints().getSupply()==0+3);
		assertTrue("Error repeated Nob first & second, 0 extra points expected",match.getPlayers().get(2).getPocket().getVictoryPoints().getSupply()==0+3);
		
		assertTrue("Error repeated Nob first & second, 5 extra points expected",match.getPlayers().get(1).getPocket().getVictoryPoints().getSupply()==5+3);
		assertTrue("Error repeated Nob first & second, 5+1 extra points expected",match.getPlayers().get(4).getPocket().getVictoryPoints().getSupply()==5+3+1);
		
		assertEquals("Wrong scoreboard",match.getPlayers().get(4).getName(),score.get(0).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(1).getName(),score.get(1).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(0).getName(),score.get(2).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(2).getName(),score.get(3).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(3).getName(),score.get(4).getName());
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#whoWon()}.
	 */
	@Test
	public void winningExAequoForAssistants() {
		MatchCD4 match = new MatchCD4(factory.getBoard(), proxy,true,10);
		List<PocketCD4> pockets = Arrays.asList(
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(0), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(2), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(2), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(1), new NobilityRank(4), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(1), new NobilityRank(4), new VictoryPoint(0))
				);
		players = Arrays.asList(
				new PlayerCD4(2, "B_Nik", colors.get(1), match, pockets.get(1)),
				new PlayerCD4(4, "D_Megan", colors.get(3), match, pockets.get(3)),
				new PlayerCD4(3, "C_Dado", colors.get(2), match, pockets.get(2)),
				new PlayerCD4(1, "A_Gio", colors.get(0), match, pockets.get(0)),
				new PlayerCD4(5, "E_Lena", colors.get(4), match, pockets.get(4))
				);
		for(PlayerCD4 p : players){
			p.setStatus(true);
			match.addPlayer(p);
		}
		
		players.get(4).getPoliticsCards().add(new PoliticsCard(new Party(colors.get(0),true)));

		List<PlayerCD4> score = match.whoWon();

		// 1 Assistant to E_Lena, 1 Assistant to D_megan, 1 Politics Card to E_Lena resolves the ex aequo
		// + 3 Victory Points because all players have the same (zero) permit cards
		
		assertTrue("Error repeated Nob first & second, 0 extra points expected",match.getPlayers().get(3).getPocket().getVictoryPoints().getSupply()==0+3);
		assertTrue("Error repeated Nob first & second, 0 extra points expected",match.getPlayers().get(0).getPocket().getVictoryPoints().getSupply()==0+3);
		assertTrue("Error repeated Nob first & second, 0 extra points expected",match.getPlayers().get(2).getPocket().getVictoryPoints().getSupply()==0+3);
		
		assertTrue("Error repeated Nob first & second, 5 extra points expected",match.getPlayers().get(1).getPocket().getVictoryPoints().getSupply()==5+3);
		assertTrue("Error repeated Nob first & second, 5+1 extra points expected",match.getPlayers().get(4).getPocket().getVictoryPoints().getSupply()==5+3+1);

		assertEquals("Wrong scoreboard",match.getPlayers().get(4).getName(),score.get(0).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(1).getName(),score.get(1).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(0).getName(),score.get(2).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(2).getName(),score.get(3).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(3).getName(),score.get(4).getName());
		
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#whoWon()}.
	 */
	@Test
	public void winningExAequoForPolitics() {
		MatchCD4 match = new MatchCD4(factory.getBoard(), proxy,true,10);
		List<PocketCD4> pockets = Arrays.asList(
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(0), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(2), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(2), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(4), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(1), new NobilityRank(4), new VictoryPoint(0))
				);
		players = Arrays.asList(
				new PlayerCD4(2, "B_Nik", colors.get(1), match, pockets.get(1)),
				new PlayerCD4(4, "D_Megan", colors.get(3), match, pockets.get(3)),
				new PlayerCD4(3, "C_Dado", colors.get(2), match, pockets.get(2)),
				new PlayerCD4(1, "A_Gio", colors.get(0), match, pockets.get(0)),
				new PlayerCD4(5, "E_Lena", colors.get(4), match, pockets.get(4))
				);
		for(PlayerCD4 p : players){
			p.setStatus(true);
			match.addPlayer(p);
		}
		
		players.get(4).getPoliticsCards().add(new PoliticsCard(new Party(colors.get(0),true)));
		players.get(1).getPoliticsCards().add(new PoliticsCard(new Party(colors.get(0),true)));

		List<PlayerCD4> score = match.whoWon();

		// 1 Assistant to E_Lena, 1 Assistant to D_megan, 1 Politics Card to E_Lena resolves the ex aequo
		// + 3 Victory Points because all players have the same (zero) permit cards
		
		assertTrue("Error repeated Nob first & second, 0 extra points expected",match.getPlayers().get(3).getPocket().getVictoryPoints().getSupply()==0+3);
		assertTrue("Error repeated Nob first & second, 0 extra points expected",match.getPlayers().get(0).getPocket().getVictoryPoints().getSupply()==0+3);
		assertTrue("Error repeated Nob first & second, 0 extra points expected",match.getPlayers().get(2).getPocket().getVictoryPoints().getSupply()==0+3);
		
		assertTrue("Error repeated Nob first & second, 5 extra points expected",match.getPlayers().get(1).getPocket().getVictoryPoints().getSupply()==5+3);
		assertTrue("Error repeated Nob first & second, 5+1 extra points expected",match.getPlayers().get(4).getPocket().getVictoryPoints().getSupply()==5+3+1);

		assertEquals("Wrong scoreboard",match.getPlayers().get(4).getName(),score.get(0).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(1).getName(),score.get(1).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(0).getName(),score.get(2).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(2).getName(),score.get(3).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(3).getName(),score.get(4).getName());
		
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#whoWon()}.
	 */
	@Test
	public void winningExAequoGeneral() {
		MatchCD4 match = new MatchCD4(factory.getBoard(), proxy,true,10);
		List<PocketCD4> pockets = Arrays.asList(
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(0), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(2), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(2), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(1), new NobilityRank(4), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(1), new NobilityRank(4), new VictoryPoint(0))
				);
		players = Arrays.asList(
				new PlayerCD4(2, "B_Nik", colors.get(1), match, pockets.get(1)),
				new PlayerCD4(4, "D_Megan", colors.get(3), match, pockets.get(3)),
				new PlayerCD4(3, "C_Dado", colors.get(2), match, pockets.get(2)),
				new PlayerCD4(1, "A_Gio", colors.get(0), match, pockets.get(0)),
				new PlayerCD4(5, "E_Lena", colors.get(4), match, pockets.get(4))
				);
		for(PlayerCD4 p : players){
			p.setStatus(true);
			match.addPlayer(p);
		}
		
		players.get(4).getPoliticsCards().add(new PoliticsCard(new Party(colors.get(0),true)));
		players.get(3).getPoliticsCards().add(new PoliticsCard(new Party(colors.get(0),true)));

		List<PlayerCD4> score = match.whoWon();

		// 1 Assistant to E_Lena, 1 Assistant to D_megan, 1 Politics Card to E_Lena resolves the ex aequo
		// + 3 Victory Points because all players have the same (zero) permit cards
		
		assertTrue("ExAequoGeneral, 0 extra points expected",match.getPlayers().get(3).getPocket().getVictoryPoints().getSupply()==0+3);
		assertTrue("ExAequoGeneral, 0 extra points expected",match.getPlayers().get(0).getPocket().getVictoryPoints().getSupply()==0+3);
		assertTrue("ExAequoGeneral, 0 extra points expected",match.getPlayers().get(2).getPocket().getVictoryPoints().getSupply()==0+3);
		
		int vp1 = match.getPlayers().get(1).getPocket().getVictoryPoints().getSupply();
		int vp2 = match.getPlayers().get(4).getPocket().getVictoryPoints().getSupply();
		String winner, second;
		if(vp1 == 5+3+1){
			assertTrue("ExAequoGeneral error",vp2 == 5+3);
			winner = new String("D_Megan");
			second = new String("E_Lena");
		}else{
			assertTrue("ExAequoGeneral error",vp1 == 5+3 && vp2 == 5+3+1);
			winner = new String("E_Lena");
			second = new String("D_Megan");
		}
		assertEquals("Wrong scoreboard",winner,score.get(0).getName());
		assertEquals("Wrong scoreboard",second,score.get(1).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(0).getName(),score.get(2).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(2).getName(),score.get(3).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(3).getName(),score.get(4).getName());
		
	}
	
	/**
	 * Test method for {@link it.polimi.ingsw.cg25.model.MatchCD4#whoWon()}.
	 */
	@Test
	public void winningInactivePlayersIgnoredInScoreboard() {
		MatchCD4 match = new MatchCD4(factory.getBoard(), proxy,true,10);
		List<PocketCD4> pockets = Arrays.asList(
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(0), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(2), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(2), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(4), new VictoryPoint(0)),
				new PocketCD4(new Coin(0), new Assistant(1), new NobilityRank(4), new VictoryPoint(0))
				);
		players = Arrays.asList(
				new PlayerCD4(2, "B_Nik", colors.get(1), match, pockets.get(1)),
				new PlayerCD4(4, "D_Megan", colors.get(3), match, pockets.get(3)),
				new PlayerCD4(3, "C_Dado", colors.get(2), match, pockets.get(2)),
				new PlayerCD4(1, "A_Gio", colors.get(0), match, pockets.get(0)),
				new PlayerCD4(5, "E_Lena", colors.get(4), match, pockets.get(4))
				);
		for(PlayerCD4 p : players){
			p.setStatus(true);
			match.addPlayer(p);
		}
		
		players.get(4).getPoliticsCards().add(new PoliticsCard(new Party(colors.get(0),true)));
		players.get(1).getPoliticsCards().add(new PoliticsCard(new Party(colors.get(0),true)));
		
		// E_Lena (winner) and C_Dado are inactive
		players.get(4).setStatus(false);
		players.get(2).setStatus(false);
		
		List<PlayerCD4> score = match.whoWon();

		// + 3 Victory Points because all players have the same (zero) permit cards
		
		assertTrue("Error repeated Nob first & second, 0 extra points expected",match.getPlayers().get(3).getPocket().getVictoryPoints().getSupply()==0+3);
		assertTrue("Error repeated Nob first & second, 0 extra points expected",match.getPlayers().get(0).getPocket().getVictoryPoints().getSupply()==0+3);
		assertTrue("Error repeated Nob first & second, 0 extra points expected",match.getPlayers().get(2).getPocket().getVictoryPoints().getSupply()==0+3);
		
		assertTrue("Error repeated Nob first & second, 5 extra points expected",match.getPlayers().get(1).getPocket().getVictoryPoints().getSupply()==5+3);
		assertTrue("Error repeated Nob first & second, 5 extra points expected",match.getPlayers().get(4).getPocket().getVictoryPoints().getSupply()==5+3);

		assertEquals("Wrong scoreboard",match.getPlayers().get(1).getName(),score.get(0).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(0).getName(),score.get(1).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(3).getName(),score.get(2).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(4).getName(),score.get(3).getName());
		assertEquals("Wrong scoreboard",match.getPlayers().get(2).getName(),score.get(4).getName());
		
	}

}
