package it.polimi.ingsw.cg25.mainactions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.actions.DisplayInteraction;
import it.polimi.ingsw.cg25.actions.ElectionAction;
import it.polimi.ingsw.cg25.exceptions.CannotCreateGameException;
import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.NoCardsException;
import it.polimi.ingsw.cg25.model.Assistant;
import it.polimi.ingsw.cg25.model.Coin;
import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.NobilityRank;
import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.PocketCD4;
import it.polimi.ingsw.cg25.model.VictoryPoint;
import it.polimi.ingsw.cg25.model.dashboard.Councelor;
import it.polimi.ingsw.cg25.model.dashboard.Council;
import it.polimi.ingsw.cg25.model.dashboard.Party;
import it.polimi.ingsw.cg25.model.dashboard.cards.PoliticsCard;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

public class ElectionActionTest {

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
	
	@Test
	public void testDoAction() throws CannotPerformActionException {
		ElectionAction action = new ElectionAction(model);
		action.setup();
		//Select a councelor
		action.getInteraction(0).registerReply("0");
		action.sendReply(action.getInteraction(0));
		//Select a council - Sea
		action.getInteraction(1).registerReply("0");
		action.sendReply(action.getInteraction(1));
		
		//This call must not have any effects
		action.setup();
		
		assertEquals(8, model.getBoard().getUnemployedCouncelors().size());
		
		//player2 gains 4 coins for comparison with player1
		player2.getPocket().addPocketable(new Coin(4));
		
		action.doAction();
		
		//Some checks: expected vs reality
		assertEquals(8, model.getBoard().getUnemployedCouncelors().size());
		//I suppose electCouncelor works
		assertEquals(4, model.getBoard().getRegions().get(0).getCouncil().getCouncelors().size());
		
		assertEquals(player2.getPocket().getCoins().getSupply(), player1.getPocket().getCoins().getSupply());
		assertEquals(player2.getPocket().getAssistants().getSupply(), player1.getPocket().getAssistants().getSupply());
		assertEquals(player2.getPocket().getNobilityRank().getSupply(), player1.getPocket().getNobilityRank().getSupply());
		assertEquals(player2.getPocket().getVictoryPoints().getSupply(), player1.getPocket().getVictoryPoints().getSupply());
	}
	
	@Test(expected=CannotPerformActionException.class)
	public void testDecodeFails() throws CannotPerformActionException {
		ElectionAction action = new ElectionAction(model);
		action.setup();
		//Select a councelor
		action.getInteraction(0).registerReply("0");
		action.sendReply(action.getInteraction(0));
		//Select a council - Sea
		action.getInteraction(1).registerReply("0");
		action.sendReply(action.getInteraction(1));
		
		//Clear unemployed List
		model.getBoard().getUnemployedCouncelors().clear();
		
		action.doAction();
	}
	
	@Test
	public void testToString() {
		assertEquals("Elect a councelor in one of the councils and get 4 coins", 
				new ElectionAction(model).toString());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCantCreateElectionActionWithoutAModel() {
		new ElectionAction(null);
	}
	
	@Test(expected=NullPointerException.class)
	public void testSetupForbiddenWithNullLists() {
		ElectionAction action = new ElectionAction(model);
		//This triggers the exception
		action.setup(null, null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetupForbiddenWithEmptyLists() {
		ElectionAction action = new ElectionAction(model);
		//This triggers the exception
		action.setup(new ArrayList<Councelor>(), new ArrayList<Council>());
	}
	
	@Test
	public void testSetupInteractionSize() {
		ElectionAction action = new ElectionAction(model);
		action.setup();
		assertEquals(2, action.getInteractions().size());
	}
	
	@Test
	public void testHasNext() {
		ElectionAction action = new ElectionAction(model);
		//No next interaction before setting up
		assertFalse(action.hasNext());
		//Next Interaction after setting up
		action.setup();
		assertTrue(action.hasNext());
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testNoNextWithNoSetup() {
		ElectionAction action = new ElectionAction(model);
		action.next();
	}
	
	@Test
	public void abortAction(){
		ElectionAction action = new ElectionAction(model);
		action.setup();
		assertFalse(action.isAborted());
		assertFalse(0==action.getInteractions().size());
		assertTrue(action.hasNext());
		action.sendReply(new DisplayInteraction<String>("[[Abort]]","[[Abort]]"));
		assertFalse(action.hasNext());
		assertEquals(0,action.getInteractions().size());
		assertTrue(action.isAborted());
	}
	
	@After
	public void teardown() {
		model.getLogger().close();
	}
	
}
