package it.polimi.ingsw.cg25.bonusactions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.actions.ReusePermitBonusAction;
import it.polimi.ingsw.cg25.exceptions.CannotCreateGameException;
import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.CannotSetupActionException;
import it.polimi.ingsw.cg25.exceptions.NoCardsException;
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
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

public class ReusePermitBonusActionTest {

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
		
		//player1 has 1 used permit
		player1.getUsedPermits().add(model.getBoard().getRegions().get(0).getPermit(0));
	}
	
	@Test(expected=CannotSetupActionException.class)
	public void testNoUsedPermits() throws CannotSetupActionException {
		player1.getUsedPermits().clear();
		
		ReusePermitBonusAction action = new ReusePermitBonusAction(model);
		//This triggers the exception
		action.setup();
	}
	
	@Test
	public void testSetupInteractionSize() throws CannotSetupActionException {
		ReusePermitBonusAction action = new ReusePermitBonusAction(model);
		action.setup();
		assertEquals(1, action.getInteractions().size());
	}
	
	@Test(expected=NullPointerException.class)
	public void testSetupProblemNullListOfOptions() throws CannotSetupActionException {
		ReusePermitBonusAction action = new ReusePermitBonusAction(model);
		action.setup(1, null);
	}
	
	@Test
	public void testHasNext() throws CannotSetupActionException {
		ReusePermitBonusAction action = new ReusePermitBonusAction(model);
		//No next interaction before setting up
		assertFalse(action.hasNext());
		//Next Interaction after setting up
		action.setup();
		assertTrue(action.hasNext());
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testNoNextWithNoSetup() {
		ReusePermitBonusAction action = new ReusePermitBonusAction(model);
		action.next();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCantCreateReusePermitBonusActionWithoutAModel() {
		new ReusePermitBonusAction(null);
	}

	@Test
	public void testDoAction() throws CannotSetupActionException, CannotPerformActionException {
		ReusePermitBonusAction action = new ReusePermitBonusAction(model);
		
		action.setup(1, player1.getUsedPermits());
		//Choose the first used permit card
		action.getInteraction(0).registerReply("0");
		action.sendReply(action.getInteraction(0));
	
		//This call must not have any effects
		action.setup();
		
		assertEquals(1, player1.getUsedPermits().size());
		//This triggers the exception
		action.doAction();
		
		assertEquals(1, player1.getUsedPermits().size());
		
		player1.getUsedPermits().get(0).takeAllBonuses(player2);
		assertEquals(player2.getPocket().getCoins().getSupply(), player1.getPocket().getCoins().getSupply());
		assertEquals(player2.getPocket().getAssistants().getSupply(), player1.getPocket().getAssistants().getSupply());
		assertEquals(player2.getPocket().getNobilityRank().getSupply(), player1.getPocket().getNobilityRank().getSupply());
		assertEquals(player2.getPocket().getVictoryPoints().getSupply(), player1.getPocket().getVictoryPoints().getSupply());
	}
	
}
