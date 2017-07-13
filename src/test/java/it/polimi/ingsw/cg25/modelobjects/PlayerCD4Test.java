package it.polimi.ingsw.cg25.modelobjects;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.exceptions.CannotCreateGameException;
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
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

/**
 * 
 * @author Giovanni
 *
 */
public class PlayerCD4Test {

	private MatchCD4 model;
	private ModelProxy proxy;
	private BoardFactory factory;
	
	private PlayerCD4 player;
	private PocketCD4 pocket;
	
	@Before
	public void setUp() throws FileNotFoundException, CannotCreateGameException {
		factory = new BoardFactory(new FileReader("src/test/resources/nobilityCellsFULL.txt"),
				new FileReader("src/test/resources/politicsFULL.txt"), 
				new FileReader("src/test/resources/citiesFULL.txt"),
				new FileReader("src/test/resources/graphFULL.txt"), 
				new FileReader("src/test/resources/kingFULL.txt"),
				new FileReader("src/test/resources/regionsFULL.txt"));
		this.proxy = new ModelProxy();
		this.model = new MatchCD4(factory.getBoard(), this.proxy,false,10);
		//Init pocket
		pocket = new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(0), new VictoryPoint(0));
		player = new PlayerCD4(1, "Gio", HSBColor.getNDifferent(1).get(0), model, pocket);
	}
	
	@Test(expected=NullPointerException.class)
	public void cantCreateAPlayerWithoutAName() {
		new PlayerCD4(1, null, HSBColor.getNDifferent(1).get(0), model, pocket);
	}
	
	@Test(expected=NullPointerException.class)
	public void cantCreateAPlayerWithoutAColor() {
		new PlayerCD4(1, "Nick", null, model, pocket);
	}
	
	@Test(expected=NullPointerException.class)
	public void cantCreateAPlayerWithoutAMatch() {
		new PlayerCD4(1, "Nick", HSBColor.getNDifferent(1).get(0), null, pocket);
	}
	
	@Test(expected=NullPointerException.class)
	public void cantCreateAPlayerWithoutAPocket() {
		new PlayerCD4(1, "Nick", HSBColor.getNDifferent(1).get(0), model, null);
	}
	
	@Test
	public void testAddMainAction() {
		player.addMainAction(1);
		assertEquals(2, model.getRemainingMainActions());
		player.addMainAction(0);
		assertEquals(2, model.getRemainingMainActions());
	}
	
	@Test
	public void testAddPoliticsCard() throws NoCardsException {
		assertEquals(0, player.getPoliticsCards().size());
		PoliticsCard cardToAdd = model.getBoard().getPoliticsDeck().drawCard();
		player.addPoliticsCard(cardToAdd);
		//Check the player has cardToAdd
		assertEquals(1, player.getPoliticsCards().size());
		assertEquals(player.getPoliticsCards().get(0).getParty().getIsJolly(), cardToAdd.getParty().getIsJolly());
		assertTrue(player.getPoliticsCards().get(0).getParty().getColor().equals(cardToAdd.getParty().getColor()));
	}
	
	@Test
	public void testGetPocket() {
		assertEquals(PocketCD4.class, player.getPocket().getClass());
		//Check the pocket is the same as the one at startup
		assertEquals(0, player.getPocket().getCoins().getSupply());
		assertEquals(0, player.getPocket().getAssistants().getSupply());
		assertEquals(0, player.getPocket().getNobilityRank().getSupply());
		assertEquals(0, player.getPocket().getVictoryPoints().getSupply());
	}
	
	@Test
	public void testGetPoliticsCard() throws NoCardsException {
		// At startup the hand is empty
		assertEquals(0, player.getPoliticsCards().size());
		PoliticsCard cardToAdd = model.getBoard().getPoliticsDeck().drawCard();
		player.addPoliticsCard(cardToAdd);
		assertEquals(1, player.getPoliticsCards().size());
		assertEquals(PoliticsCard.class, player.getPoliticsCards().get(0).getClass());
	}
	
	@Test
	public void testGetCurrentMatch() {
		assertEquals(MatchCD4.class, player.getCurrentMatch().getClass());
	}
	
	@Test
	public void testDiscardPolitics() {
		PoliticsCard card1 = new PoliticsCard(new Party(HSBColor.getNDifferent(3).get(0), false));
		PoliticsCard card2 = new PoliticsCard(new Party(HSBColor.getNDifferent(3).get(1), false));
		PoliticsCard card3 = new PoliticsCard(new Party(HSBColor.getNDifferent(3).get(2), false));
		PoliticsCard card4 = new PoliticsCard(new Party(HSBColor.getNDifferent(3).get(0), true));
		
		player.addPoliticsCard(card1);
		player.addPoliticsCard(card2);
		player.addPoliticsCard(card3);
		player.addPoliticsCard(card4);
		
		assertEquals(4, player.getPoliticsCards().size());
		player.discardPolitics(card4);
		assertEquals(3, player.getPoliticsCards().size());
		assertTrue(card1.getParty().getColor().equals(player.getPoliticsCards().get(0).getParty().getColor()) &&
				card1.getParty().getIsJolly() == player.getPoliticsCards().get(0).getParty().getIsJolly());
		assertTrue(card2.getParty().getColor().equals(player.getPoliticsCards().get(1).getParty().getColor()) &&
				card2.getParty().getIsJolly() == player.getPoliticsCards().get(1).getParty().getIsJolly());
		assertTrue(card3.getParty().getColor().equals(player.getPoliticsCards().get(2).getParty().getColor()) &&
				card3.getParty().getIsJolly() == player.getPoliticsCards().get(2).getParty().getIsJolly());
		
		player.discardPolitics(card1);
		assertEquals(2, player.getPoliticsCards().size());
		assertTrue(card2.getParty().getColor().equals(player.getPoliticsCards().get(0).getParty().getColor()) &&
				card2.getParty().getIsJolly() == player.getPoliticsCards().get(0).getParty().getIsJolly());
		assertTrue(card3.getParty().getColor().equals(player.getPoliticsCards().get(1).getParty().getColor()) &&
				card3.getParty().getIsJolly() == player.getPoliticsCards().get(1).getParty().getIsJolly());
	}
	
	@Test
	public void testSetStatusAndisActive() {
		player.setStatus(true);
		assertTrue(player.isActive());
		
		player.setStatus(false);
		assertFalse(player.isActive());
	}
	
	@Test
	public void testGetUserID() {
		assertEquals(1, player.getUserID());
	}
	
	@Test(expected=OneEmporiumOnlyException.class)
	public void CantBuildMoreThanOneEmporiumInACity() throws OneEmporiumOnlyException {
		City arkon = model.getBoard().getCities().get(0);
		//Build an emporium in Arkon
		player.buildEmporium(arkon);
		//This triggers the exception
		player.buildEmporium(arkon);
	}
	
	@Test
	public void testBuildEmporiumConnectedCities() throws OneEmporiumOnlyException {
		City arkon = model.getBoard().getCities().get(0);
		City castrum = model.getBoard().getCities().get(5);
		PocketCD4 pocketTemp = new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(0), new VictoryPoint(0));
		//He is used to compare his pocket to player's pocket
		PlayerCD4 playerTemp = new PlayerCD4(2, "Nick", HSBColor.getNDifferent(2).get(1), model, pocketTemp);
		//Build an emporium in Arkon
		player.buildEmporium(arkon);
		arkon.takeAllBonuses(playerTemp);
		player.buildEmporium(castrum);
		arkon.takeAllBonuses(playerTemp);
		castrum.takeAllBonuses(playerTemp);
		
		// Checks for 3 victory points if player has more emporium than required for win the game
		if(player.getPlayerEmporiums().size() >= player.getCurrentMatch().numberOfEmporiumsToWin)
			playerTemp.getPocket().addPocketable(new VictoryPoint(3));
		
		assertEquals(playerTemp.getPocket().getCoins().getSupply(), player.getPocket().getCoins().getSupply());
		assertEquals(playerTemp.getPocket().getAssistants().getSupply(), player.getPocket().getAssistants().getSupply());
		assertEquals(playerTemp.getPocket().getNobilityRank().getSupply(), player.getPocket().getNobilityRank().getSupply());
		assertEquals(playerTemp.getPocket().getVictoryPoints().getSupply(), player.getPocket().getVictoryPoints().getSupply());
		
	}
	
	@Test
	public void testBuildEmporiumCitiesOfARegion() throws OneEmporiumOnlyException {
		City arkon = model.getBoard().getCities().get(0);
		City castrum = model.getBoard().getCities().get(5);
		City esti = model.getBoard().getCities().get(6);
		City burgen = model.getBoard().getCities().get(9);
		City dorful = model.getBoard().getCities().get(12);
		
		PocketCD4 pocketTemp = new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(0), new VictoryPoint(0));
		//He is used to compare his pocket to player's pocket
		PlayerCD4 playerTemp = new PlayerCD4(2, "Nick", HSBColor.getNDifferent(2).get(1), model, pocketTemp);
		//I'm going to build an emporium in all the cities of Sea region
		//Build an emporium in Arkon
		player.buildEmporium(arkon);
		arkon.takeAllBonuses(playerTemp);
		//Build an emporium in Castrum
		player.buildEmporium(castrum);
		arkon.takeAllBonuses(playerTemp);
		castrum.takeAllBonuses(playerTemp);
		//Build an emporium in Esti
		player.buildEmporium(esti);
		esti.takeAllBonuses(playerTemp);
		//Build an emporium in Burgen
		player.buildEmporium(burgen);
		esti.takeAllBonuses(playerTemp);
		burgen.takeAllBonuses(playerTemp);
		//Build an emporium in Dorful
		player.buildEmporium(dorful);
		arkon.takeAllBonuses(playerTemp);
		castrum.takeAllBonuses(playerTemp);
		dorful.takeAllBonuses(playerTemp);
		//Sea has a reward of 5 victory points
		playerTemp.getPocket().addPocketable(new VictoryPoint(5));
		//The first reward card has 25 victory points on it
		playerTemp.getPocket().addPocketable(new VictoryPoint(25));
		
		// Checks for 3 victory points if player has more emporium than required for win the game
		if(player.getPlayerEmporiums().size() >= player.getCurrentMatch().numberOfEmporiumsToWin)
			playerTemp.getPocket().addPocketable(new VictoryPoint(3));
		
		assertEquals(playerTemp.getPocket().getCoins().getSupply(), player.getPocket().getCoins().getSupply());
		assertEquals(playerTemp.getPocket().getAssistants().getSupply(), player.getPocket().getAssistants().getSupply());
		assertEquals(playerTemp.getPocket().getNobilityRank().getSupply(), player.getPocket().getNobilityRank().getSupply());
		assertEquals(playerTemp.getPocket().getVictoryPoints().getSupply(), player.getPocket().getVictoryPoints().getSupply());
	}
	
	@Test
	public void testBuildEmporiumCitiesOfAColor() throws OneEmporiumOnlyException {
		City dorful = model.getBoard().getCities().get(12);
		City merkatim = model.getBoard().getCities().get(13);
		
		PocketCD4 pocketTemp = new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(0), new VictoryPoint(0));
		//He is used to compare his pocket to player's pocket
		PlayerCD4 playerTemp = new PlayerCD4(2, "Nick", HSBColor.getNDifferent(2).get(1), model, pocketTemp);
		//I'm going to build an emporium in all the cities of color group number 3
		//Build an emporium in Dorful
		player.buildEmporium(dorful);
		dorful.takeAllBonuses(playerTemp);
		//Build an emporium in Merkatim
		player.buildEmporium(merkatim);
		merkatim.takeAllBonuses(playerTemp);
		//Third color has a reward of 8 victory points
		playerTemp.getPocket().addPocketable(new VictoryPoint(5));
		//The first reward card has 25 victory points on it
		playerTemp.getPocket().addPocketable(new VictoryPoint(25));
		
		// Checks for 3 victory points if player has more emporium than required for win the game
		if(player.getPlayerEmporiums().size() >= player.getCurrentMatch().numberOfEmporiumsToWin)
			playerTemp.getPocket().addPocketable(new VictoryPoint(3));
		
		assertEquals(playerTemp.getPocket().getCoins().getSupply(), player.getPocket().getCoins().getSupply());
		assertEquals(playerTemp.getPocket().getAssistants().getSupply(), player.getPocket().getAssistants().getSupply());
		assertEquals(playerTemp.getPocket().getNobilityRank().getSupply(), player.getPocket().getNobilityRank().getSupply());
		assertEquals(playerTemp.getPocket().getVictoryPoints().getSupply(), player.getPocket().getVictoryPoints().getSupply());
	}
	
	@Test
	public void testGetPermitsToBeUsed() throws NoCardsException {
		player.getPermitsToBeUsed().add(model.getBoard().getRegions().get(0).getPermit(0));
		assertEquals(1, player.getPermitsToBeUsed().size());
		player.getPermitsToBeUsed().add(model.getBoard().getRegions().get(0).getPermit(0));
		assertEquals(2, player.getPermitsToBeUsed().size());
	}
	
	@Test
	public void testGetUsedPermits() throws NoCardsException {
		player.getUsedPermits().add(model.getBoard().getRegions().get(0).getPermit(0));
		assertEquals(1, player.getUsedPermits().size());
		player.getUsedPermits().add(model.getBoard().getRegions().get(0).getPermit(0));
		assertEquals(2, player.getUsedPermits().size());
	}
	
	@After
	public void teardown() {
		model.getLogger().close();
	}
	
}
