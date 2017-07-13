package it.polimi.ingsw.cg25.mainactions;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.actions.PermitAction;
import it.polimi.ingsw.cg25.exceptions.CannotCreateGameException;
import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.NoCardsException;
import it.polimi.ingsw.cg25.exceptions.NotEnoughCoinException;
import it.polimi.ingsw.cg25.model.Assistant;
import it.polimi.ingsw.cg25.model.Coin;
import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.NobilityRank;
import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.PocketCD4;
import it.polimi.ingsw.cg25.model.VictoryPoint;
import it.polimi.ingsw.cg25.model.dashboard.Party;
import it.polimi.ingsw.cg25.model.dashboard.cards.PermitCard;
import it.polimi.ingsw.cg25.model.dashboard.cards.PoliticsCard;
import it.polimi.ingsw.cg25.model.dashboard.topological.Region;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

/**
 * 
 * @author Giovanni
 *
 */
public class PermitActionTest {
	
	private MatchCD4 model;
	private ModelProxy proxy;
	private BoardFactory factory;
	
	private PlayerCD4 player1;
	private PlayerCD4 player2;
	
	@Before
	public void setUp() throws FileNotFoundException, CannotCreateGameException, NoCardsException {
		factory = new BoardFactory(new FileReader("src/test/resources/nobilityCellsFULL.txt"),
				new FileReader("src/test/resources/politicsFULL.txt"), 
				new FileReader("src/test/resources/citiesFULL.txt"),
				new FileReader("src/test/resources/graphFULL.txt"), 
				new FileReader("src/test/resources/kingFULL.txt"),
				new FileReader("src/test/resources/regionsFULL.txt"));
		this.proxy = new ModelProxy();
		this.model = new MatchCD4(factory.getBoard(), this.proxy,false,10);
		//Init pockets and players
		PocketCD4 pocket1 = new PocketCD4(new Coin(100), new Assistant(50), new NobilityRank(0), new VictoryPoint(0));
		player1 = new PlayerCD4(1, "Gio", HSBColor.getNDifferent(2).get(0), model, pocket1);
		PocketCD4 pocket2 = new PocketCD4(new Coin(100), new Assistant(50), new NobilityRank(0), new VictoryPoint(0));
		player2 = new PlayerCD4(1, "FakePlayer", HSBColor.getNDifferent(2).get(1), model, pocket2);
		model.addPlayer(player1);
		model.addPlayer(player2);
		//Init hands (all jolly)
		for(int i = 0; i < 6; i++) {
			player1.addPoliticsCard(new PoliticsCard(new Party(new HSBColor(0,0,0),true)));
			player2.addPoliticsCard(new PoliticsCard(new Party(new HSBColor(0,0,0),true)));
		}
	}
	
	@Test(expected=CannotPerformActionException.class)
	public void testDoActionElementNotFoundException() throws CannotPerformActionException {
		PermitAction action = new PermitAction(model);
		action.setup();
		//Select region 1
		action.getInteraction(0).registerReply("1");
		//Getting interaction contents
		action.sendReply(action.getInteraction(0));
		//4 jolly to satisfy the Council - fill the interaction
		action.getInteraction(1).registerReply("0");
		action.sendReply(action.getInteraction(1));
		//Select the second permit
		action.getInteraction(2).registerReply("1");
		action.sendReply(action.getInteraction(2));
		
		assertEquals(0, player1.getPermitsToBeUsed().size());
		
		//Remove all player's cards
		player1.getPoliticsCards().clear();
		//This triggers the exception while decoding
		action.doAction();
	}
	
	@Test(expected=CannotPerformActionException.class)
	public void testDoActionNoGoodCardsException() throws CannotPerformActionException {
		//Remove all player's cards
		player1.getPoliticsCards().clear();
		//Add totally random card with a different color
		player1.getPoliticsCards().add(new PoliticsCard(new Party(new HSBColor(10, 20, 30, "fake"), false)));
		
		PermitAction action = new PermitAction(model);
		action.setup();
		//Select region 2
		action.getInteraction(0).registerReply("2");
		//Getting interaction contents
		action.sendReply(action.getInteraction(0));
		//4 jolly to satisfy the Council - fill the interaction
		action.getInteraction(1).registerReply("0");
		action.sendReply(action.getInteraction(1));
		//Select the first permit
		action.getInteraction(2).registerReply("1");
		action.sendReply(action.getInteraction(2));
		
		assertEquals(0, player1.getPermitsToBeUsed().size());
		
		//This triggers the exception while decoding
		action.doAction();
	}
	
	@Test(expected=CannotPerformActionException.class)
	public void testDoActionNotEnoughCoinException() throws CannotPerformActionException, NotEnoughCoinException {
		PermitAction action = new PermitAction(model);
		action.setup();
		//Select region 0
		action.getInteraction(0).registerReply("0");
		//Getting interaction contents
		action.sendReply(action.getInteraction(0));
		//4 jolly to satisfy the Council - fill the interaction
		action.getInteraction(1).registerReply("0,1,2,3");
		action.sendReply(action.getInteraction(1));
		//Select the first permit
		action.getInteraction(2).registerReply("0");
		action.sendReply(action.getInteraction(2));
		
		assertEquals(0, player1.getPermitsToBeUsed().size());
		
		//Remove all player's coins, now player1 has 0 coins
		player1.getPocket().subPocketable(new Coin(100));
		//This triggers the exception
		action.doAction();
		
		assertEquals(0, player1.getPocket().getCoins().getSupply());
	}
	
	@Test
	public void testToString() {
		PermitAction action = new PermitAction(model);
		assertEquals("Influence a Council to draw one of the face-up permits of its Region", action.toString());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCantCreatePermitActionWithoutAModel() {
		new PermitAction(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetupForbiddenWithNullLists() {
		PermitAction action = new PermitAction(model);
		//This triggers the exception
		action.setup(null, null, null);;
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetupForbiddenWithEmptyLists() {
		PermitAction action = new PermitAction(model);
		//This triggers the exception
		action.setup(new ArrayList<PoliticsCard>(), new ArrayList<PermitCard>(), new ArrayList<Region>());
	}
	
	@Test
	public void testSetupInteractionSize() {
		PermitAction action = new PermitAction(model);
		action.setup();
		assertEquals(3, action.getInteractions().size());
	}
	
	@Test
	public void testHasNext() {
		PermitAction action = new PermitAction(model);
		//No next interaction before setting up
		assertFalse(action.hasNext());
		//Next Interaction after setting up
		action.setup();
		assertTrue(action.hasNext());
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testNoNextWithNoSetup() {
		PermitAction action = new PermitAction(model);
		action.next();
	}
	
	@Test
	public void testDoAction() throws CannotPerformActionException, NotEnoughCoinException {
		PermitAction action = new PermitAction(model);
		action.setup();
		//Select region 0
		action.getInteraction(0).registerReply("0");
		//Getting interaction contents
		action.sendReply(action.getInteraction(0));
		//4 jolly to satisfy the Council - fill the interaction
		action.getInteraction(1).registerReply("0,1,2,3");
		action.sendReply(action.getInteraction(1));
		//Select the first permit
		action.getInteraction(2).registerReply("0");
		action.sendReply(action.getInteraction(2));
		
		//This call must not have any effects
		action.setup();
		
		assertEquals(0, player1.getPermitsToBeUsed().size());
		
		//Set player 2 as player1 after do Action
		model.getBoard().getRegions().get(0).getFaceUpPermits().get(0).takeAllBonuses(player2);
		//Subtract 4 coins cause cards are jolly
		player2.getPocket().subPocketable(new Coin(4));
		
		action.doAction();
		//Some checks: expected vs reality
		assertEquals(1, player1.getPermitsToBeUsed().size());
		assertEquals(player2.getPocket().getCoins().getSupply(), player1.getPocket().getCoins().getSupply());
		assertEquals(player2.getPocket().getAssistants().getSupply(), player1.getPocket().getAssistants().getSupply());
		assertEquals(player2.getPocket().getNobilityRank().getSupply(), player1.getPocket().getNobilityRank().getSupply());
		assertEquals(player2.getPocket().getVictoryPoints().getSupply(), player1.getPocket().getVictoryPoints().getSupply());
	}

	@Test
	public void testNext() {
		PermitAction action = new PermitAction(model);
		action.setup();
		//Region
		action.next().registerReply("0");
		action.sendReply(action.next());
		//Cards
		action.next().registerReply("0");
		action.sendReply(action.next());
		//Check 7 lines (1 question, 6 permits) before overrided next
		String[] lines = action.getInteraction(2).printOptions().split("\r\n|\r|\n");
		assertEquals(7, lines.length);
		//Permit
		action.next().registerReply("0");
		//Check 3 lines after executing next
		String[] lines2 = action.getInteraction(2).printOptions().split("\r\n|\r|\n");
		assertEquals(3, lines2.length);
		action.sendReply(action.next());
	}
	
	@After
	public void teardown() {
		model.getLogger().close();
	}
	
}
