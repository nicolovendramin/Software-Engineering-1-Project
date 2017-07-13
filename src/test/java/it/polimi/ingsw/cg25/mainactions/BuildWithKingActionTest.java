package it.polimi.ingsw.cg25.mainactions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.actions.AskQuantityInteraction;
import it.polimi.ingsw.cg25.actions.BuildWithKingAction;
import it.polimi.ingsw.cg25.actions.Interaction;
import it.polimi.ingsw.cg25.actions.SingleChoiceInteraction;
import it.polimi.ingsw.cg25.exceptions.CannotCreateGameException;
import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.CannotSetupActionException;
import it.polimi.ingsw.cg25.exceptions.NoCardsException;
import it.polimi.ingsw.cg25.exceptions.NotEnoughAssistantsException;
import it.polimi.ingsw.cg25.exceptions.NotEnoughCoinException;
import it.polimi.ingsw.cg25.exceptions.OneEmporiumOnlyException;
import it.polimi.ingsw.cg25.model.Assistant;
import it.polimi.ingsw.cg25.model.Coin;
import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.NobilityRank;
import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.PocketCD4;
import it.polimi.ingsw.cg25.model.VictoryPoint;
import it.polimi.ingsw.cg25.model.dashboard.Party;
import it.polimi.ingsw.cg25.model.dashboard.cards.PoliticsCard;
import it.polimi.ingsw.cg25.model.dashboard.topological.City;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

public class BuildWithKingActionTest {

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
		this.model = new MatchCD4(factory.getBoard(), this.proxy, false,10);
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
	public void testDoAction() throws NotEnoughCoinException, CannotPerformActionException, 
	CannotSetupActionException, OneEmporiumOnlyException, NotEnoughAssistantsException {
		
		//Player2 has an emporium in Indur
		player2.buildEmporium(model.getBoard().getCities().get(10));
		//to compare with player1's pocket
		player2.getPocket().subPocketable(new Assistant(1));
		
		BuildWithKingAction action = new BuildWithKingAction(model);
		action.setup();
		//Select cards to satisfy the council
		action.getInteraction(0).registerReply("0,1");
		//Expected 9 coins to pay
		action.sendReply(action.getInteraction(0));
		//Graden - Indur: cost 4
		action.getInteraction(1).registerReply("10");
		action.sendReply(action.getInteraction(1));
		
		//This must not have any effects
		action.setup();
		
		//Subtract 4 coins cause cards are jolly
		player2.getPocket().subPocketable(new Coin(9+4));
		
		// Player1 and Player2 earn both the 3 victory point "reward"
		// if the number of their emporiums is required for win the game
		
		action.doAction();
		//Some checks: expected vs reality
		assertEquals(2, model.getBoard().getCities().get(10).getNumEmp());
		assertEquals(player2.getPocket().getCoins().getSupply(), player1.getPocket().getCoins().getSupply());
		assertEquals(player2.getPocket().getAssistants().getSupply(), player1.getPocket().getAssistants().getSupply());
		assertEquals(player2.getPocket().getNobilityRank().getSupply(), player1.getPocket().getNobilityRank().getSupply());
		assertEquals(player2.getPocket().getVictoryPoints().getSupply(), player1.getPocket().getVictoryPoints().getSupply());
	}
	
	@Test(expected=CannotPerformActionException.class)
	public void testDoActionElementNotFoundException() throws CannotPerformActionException, CannotSetupActionException {
		BuildWithKingAction action = new BuildWithKingAction(model);
		action.setup();
		//Select cards to satisfy the council
		action.getInteraction(0).registerReply("0,1");
		//Expected 9 coins to pay
		action.sendReply(action.getInteraction(0));
		//Graden - Indur: cost 4
		action.getInteraction(1).registerReply("10");
		action.sendReply(action.getInteraction(1));
		
		//Remove all player's cards
		player1.getPoliticsCards().clear();
		//This triggers the exception while decoding
		action.doAction();
	}
	
	@Test(expected=CannotPerformActionException.class)
	public void testCannotBuildMoreThan10Emporiums() throws CannotSetupActionException, CannotPerformActionException, OneEmporiumOnlyException {
		BuildWithKingAction action = new BuildWithKingAction(model);
		action.setup();
		//Select cards to satisfy the council
		action.getInteraction(0).registerReply("0,1");
		//Expected 9 coins to pay
		action.sendReply(action.getInteraction(0));
		//Graden - Indur: cost 4
		action.getInteraction(1).registerReply("10");
		action.sendReply(action.getInteraction(1));
		for(int i = 0; i < 10; i++)
			player1.buildEmporium(model.getBoard().getCities().get(i));
		//This triggers the exception while trying to
		//build the 11th emporium
		action.doAction();
	}
	
	@Test(expected=CannotPerformActionException.class)
	public void testDoActionNotEnoughCoinsException() throws CannotSetupActionException, NotEnoughCoinException, CannotPerformActionException {
		BuildWithKingAction action = new BuildWithKingAction(model);
		action.setup();
		//Select cards to satisfy the council
		action.getInteraction(0).registerReply("0,1");
		//Expected 9 coins to pay
		action.sendReply(action.getInteraction(0));
		//Graden - Indur: cost 4
		action.getInteraction(1).registerReply("10");
		action.sendReply(action.getInteraction(1));
		//Player1 has 4 coins and can't perform the action
		player1.getPocket().subPocketable(new Coin(90));
		//This triggers the exception
		action.doAction();
	}
	
	@Test(expected=CannotPerformActionException.class)
	public void testDoActionNotEnoughAssistantsException() throws CannotSetupActionException, 
		NotEnoughCoinException, CannotPerformActionException, OneEmporiumOnlyException, NotEnoughAssistantsException {
		
		//Player2 has an emporium in Indur
		player2.buildEmporium(model.getBoard().getCities().get(10));
		
		BuildWithKingAction action = new BuildWithKingAction(model);
		action.setup();
		//Select cards to satisfy the council
		action.getInteraction(0).registerReply("0,1");
		//Expected 9 coins to pay
		action.sendReply(action.getInteraction(0));
		//Graden - Indur: cost 4
		action.getInteraction(1).registerReply("10");
		action.sendReply(action.getInteraction(1));
		
		//player1 has no Assistants
		player1.getPocket().subPocketable(new Assistant(50));
		
		//This triggers the exception
		action.doAction();
	}
	
	@Test(expected=CannotPerformActionException.class)
	public void testOnlyOneEmporium() throws NotEnoughCoinException, 
	CannotPerformActionException, OneEmporiumOnlyException, CannotSetupActionException {
			
		//Player1 already has an emporium in Indur
		player1.buildEmporium(model.getBoard().getCities().get(10));
			
		BuildWithKingAction action = new BuildWithKingAction(model);
		action.setup();
		//Select cards to satisfy the council
		action.getInteraction(0).registerReply("0,1");
		//Expected 9 coins to pay
		action.sendReply(action.getInteraction(0));
		//Graden - Indur: cost 4
		action.getInteraction(1).registerReply("10");
		action.sendReply(action.getInteraction(1));
			
		//This triggers the exception
		//Try to build a second emporium in Indur
		action.doAction();
	}

	@Test(expected=CannotPerformActionException.class)
	public void testDoActionNoGoodCardsException() throws NotEnoughCoinException, 
	CannotPerformActionException, OneEmporiumOnlyException, CannotSetupActionException {
		//player1 has a custom, non-matchable card
		player1.getPoliticsCards().clear();
		player1.addPoliticsCard(new PoliticsCard(new Party(new HSBColor(10, 10, 10, "fakeColor"), false)));
			
		BuildWithKingAction action = new BuildWithKingAction(model);
		action.setup();
		//Select cards to satisfy the council
		action.getInteraction(0).registerReply("0");
		action.sendReply(action.getInteraction(0));
		//Graden - Indur: cost 4
		action.getInteraction(1).registerReply("10");
		action.sendReply(action.getInteraction(1));
			
		//This triggers the exception
		action.doAction();
	}
	
	@Test(expected=CannotSetupActionException.class)
	public void testCannotSetupWithEmptyLists() throws CannotSetupActionException {
		BuildWithKingAction action = new BuildWithKingAction(model);
		action.setup(new ArrayList<PoliticsCard>(), new ArrayList<City>());
	}
	
	@Test(expected=CannotSetupActionException.class)
	public void testCannotSetupWithNullLists() throws CannotSetupActionException {
		BuildWithKingAction action = new BuildWithKingAction(model);
		action.setup(null, null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testBuildWithKingActionWithNullModel() {
		new BuildWithKingAction(null);
	}

	@Test
	public void testToString() {
		assertEquals("Influence the Kings council and move the King to the city where "
				+ "you want to build paying 2 coins for every link used", new BuildWithKingAction(model).toString());
	}

	@Test
	public void testSetupListOfPoliticsCardListOfCity() throws CannotSetupActionException {
		BuildWithKingAction action = new BuildWithKingAction(model);
		action.setup(model.getCurrentPlayer().getPoliticsCards(), model.getBoard().getCities());
		assertEquals(2, action.getInteractions().size());
	}

	@Test
	public void testHasNext() throws CannotSetupActionException {
		BuildWithKingAction action = new BuildWithKingAction(model);
		assertFalse("If not setup the action has not next interactions", action.hasNext());
		action.setup();
		assertTrue("After the setup the interaction has next", action.hasNext());
	}

	@Test(expected=NoSuchElementException.class)
	public void testNoNextIfTheActionIsNotSetup() {
		BuildWithKingAction action = new BuildWithKingAction(model);
		action.next();
	}
	
	@Test
	public void testNext() throws CannotSetupActionException{
		BuildWithKingAction action = new BuildWithKingAction(model);
		action.setup();
		assertTrue(action.next() == action.getInteraction(0));
	}

	@Test
	public void testSetInteraction() {
		BuildWithKingAction action = new BuildWithKingAction(model);
		List<String> strings = new ArrayList<>();
		strings.add("Test");
		strings.add("Test2");
		action.setInteraction(new SingleChoiceInteraction<String>("StringTest",strings));
		assertTrue(action.hasNext());
	}

	@Test
	public void testSendReplyWithWrongInteraction() throws CannotSetupActionException {
		BuildWithKingAction action = new BuildWithKingAction(model);
		action.setup();
		List<String> strings = new ArrayList<>();
		strings.add("Test");
		strings.add("Test2");
		Interaction i = new SingleChoiceInteraction<>(strings);
		Interaction previousInteraction = action.next();
		action.sendReply(i);
		assertTrue("Shouldn't change interaction until you reply with the right interaction",previousInteraction == action.next());
		action.sendReply(action.getInteraction(0));
		assertFalse("Should change interaction as soon as you reply with the right interaction",previousInteraction == action.next());
	}

	@Test
	public void testGetInteraction() {
		BuildWithKingAction action = new BuildWithKingAction(model);
		Interaction interaction = new AskQuantityInteraction();
		action.setInteraction(interaction);
		assertTrue(action.getInteractions().get(0)==interaction);
		assertTrue(action.getInteraction(0)==interaction);
	}
	
	@After
	public void teardown() {
		model.getLogger().close();
	}

}