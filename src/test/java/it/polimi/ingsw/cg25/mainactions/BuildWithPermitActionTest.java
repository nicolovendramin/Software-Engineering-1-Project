package it.polimi.ingsw.cg25.mainactions;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.actions.BuildWithPermitAction;
import it.polimi.ingsw.cg25.actions.SingleChoiceInteraction;
import it.polimi.ingsw.cg25.exceptions.CannotCreateGameException;
import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.CannotSetupActionException;
import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.exceptions.NoCardsException;
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
import it.polimi.ingsw.cg25.model.dto.DTOCity;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

public class BuildWithPermitActionTest {

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

	@SuppressWarnings("unchecked")
	@Test
	public void testDoAction() throws CannotSetupActionException, 
	NoCardsException, CannotPerformActionException, ElementNotFoundException {
		
		//player1 must have at least a permit
		player1.getPermitsToBeUsed().add(model.getBoard().getRegions().get(0).getPermit(0));
		
		BuildWithPermitAction action = new BuildWithPermitAction(model);
		action.setup();
		//Select permit card
		action.getInteraction(0).registerReply("0");
		action.sendReply(action.getInteraction(0));
		//Select valid city
		//Show only valid cities
		action.next();
		//Select the first
		action.getInteraction(1).registerReply("0");
		action.sendReply(action.getInteraction(1));
		
		//This must not have any effects
		action.setup();

		City validCity = ((SingleChoiceInteraction<DTOCity>)action.getInteraction(1)).getChoices().get(0).decode(model.getBoard().getCities());
		// player1.getPermitsToBeUsed().get(0).getValidCities().get(0);
		validCity.takeAllBonuses(player2);
		
		action.doAction();
		
		// Checks for 3 victory points if player has more emporium than required for win the game
		if(player1.getPlayerEmporiums().size() >= player1.getCurrentMatch().numberOfEmporiumsToWin)
			player2.getPocket().addPocketable(new VictoryPoint(3));
		
		assertEquals(1, validCity.getNumEmp());
		assertEquals(player2.getPocket().getCoins().getSupply(), player1.getPocket().getCoins().getSupply());
		assertEquals(player2.getPocket().getAssistants().getSupply(), player1.getPocket().getAssistants().getSupply());
		assertEquals(player2.getPocket().getNobilityRank().getSupply(), player1.getPocket().getNobilityRank().getSupply());
		assertEquals(player2.getPocket().getVictoryPoints().getSupply(), player1.getPocket().getVictoryPoints().getSupply());
		
		assertEquals(1, player1.getUsedPermits().size());
		assertEquals(0, player1.getPermitsToBeUsed().size());
	}

	@Test(expected=CannotPerformActionException.class)
	public void testDoActionDecodeError() throws NoCardsException, CannotSetupActionException, CannotPerformActionException {
		//player1 must have at least a permit
		player1.getPermitsToBeUsed().add(model.getBoard().getRegions().get(0).getPermit(0));
				
		BuildWithPermitAction action = new BuildWithPermitAction(model);
		action.setup();
		//Select permit card
		action.getInteraction(0).registerReply("0");
		action.sendReply(action.getInteraction(0));
		//Select valid city
		//Show only valid cities
		action.next();
		//Select the first
		action.getInteraction(1).registerReply("0");
		action.sendReply(action.getInteraction(1));
		
		//Empty permits to be used
		player1.getPermitsToBeUsed().clear();
		//This triggers the exception
		action.doAction();
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected=IllegalArgumentException.class)
	public void testDoActionOneEmporiumOnlyException() throws CannotPerformActionException, 
	NoCardsException, CannotSetupActionException, OneEmporiumOnlyException {
		//player1 must have at least a permit
		player1.getPermitsToBeUsed().add(model.getBoard().getRegions().get(0).getPermit(0));
		City validCity = player1.getPermitsToBeUsed().get(0).getValidCities().get(0);
		//player1 already has an emporium
		player1.buildEmporium(validCity);
		
		BuildWithPermitAction action = new BuildWithPermitAction(model);
		action.setup();
		//Select permit card
		action.getInteraction(0).registerReply("0");
		action.sendReply(action.getInteraction(0));
		//Select valid city
		//Show only valid cities
		action.next();
		//Select the first
		action.getInteraction(1).registerReply(Integer.toString(((SingleChoiceInteraction<DTOCity>) action.getInteraction(1)).getChoices().indexOf(new DTOCity(validCity)))
);
		action.sendReply(action.getInteraction(1));
		
		//This triggers the exception
		action.doAction();
	}
	
	@Test
	public void testNext() throws CannotSetupActionException, NoCardsException {
		//player1 must have at least a permit
		player1.getPermitsToBeUsed().add(model.getBoard().getRegions().get(0).getPermit(0));
		int numOfValidCities = player1.getPermitsToBeUsed().get(0).getValidCities().size();
		
		BuildWithPermitAction action = new BuildWithPermitAction(model);
		action.setup();
		action.next().registerReply("0");
		//Question + 15 cities
		String[] lines = action.getInteraction(1).printOptions().split("\r\n|\r|\n");
		assertEquals(16, lines.length);
		action.sendReply(action.next());
		action.next().registerReply("0");
		//Question + numOfValidCities
		String[] lines2 = action.getInteraction(1).printOptions().split("\r\n|\r|\n");
		assertEquals(numOfValidCities + 1, lines2.length);
		action.sendReply(action.next());
		
	}

	@Test
	public void testHasNext() throws CannotSetupActionException, NoCardsException {
		//player1 must have at least a permit
		player1.getPermitsToBeUsed().add(model.getBoard().getRegions().get(0).getPermit(0));
		
		BuildWithPermitAction action = new BuildWithPermitAction(model);
		//No next interaction before setting up
		assertFalse(action.hasNext());
		//Next Interaction after setting up
		action.setup();
		assertTrue(action.hasNext());
	}
	
	@Test
	public void testSetupInteractionSize() throws CannotSetupActionException, NoCardsException {
		//player1 must have at least a permit
		player1.getPermitsToBeUsed().add(model.getBoard().getRegions().get(0).getPermit(0));
				
		BuildWithPermitAction action = new BuildWithPermitAction(model);
		action.setup();
		assertEquals(2, action.getInteractions().size());
	}
	
	@Test
	public void testToString() {
		BuildWithPermitAction action = new BuildWithPermitAction(model);
		assertEquals("Use one of the Permits you have in your hand to build an Emporium", action.toString());
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testNoNextWithNoSetup() {
		BuildWithPermitAction action = new BuildWithPermitAction(model);
		action.next();
	}

	@Test(expected=IllegalArgumentException.class)
	public void testBuildWithPermitActionWithNullModel() {
		new BuildWithPermitAction(null);
	}
	
	@Test(expected=CannotSetupActionException.class)
	public void testSetupListOfPermitCardListOfCityEmpty() throws CannotSetupActionException, NoCardsException {
		//player1 must have at least a permit
		player1.getPermitsToBeUsed().add(model.getBoard().getRegions().get(0).getPermit(0));
		BuildWithPermitAction action = new BuildWithPermitAction(model);
		action.setup(player1.getPermitsToBeUsed(), new ArrayList<City>());
	}
	
	@Test(expected=CannotSetupActionException.class)
	public void testSetupListOfPermitCardListOfCityNull() throws CannotSetupActionException, NoCardsException {
		//player1 must have at least a permit
		player1.getPermitsToBeUsed().add(model.getBoard().getRegions().get(0).getPermit(0));
		BuildWithPermitAction action = new BuildWithPermitAction(model);
		action.setup(player1.getPermitsToBeUsed(), null);
	}

	@After
	public void teardown() {
		model.getLogger().close();
	}
	
}
