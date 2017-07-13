/**
 * 
 */
package it.polimi.ingsw.cg25.quickactions;

import static org.junit.Assert.*;


import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.actions.ChangePermitQuickAction;
import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.CannotSetupActionException;
import it.polimi.ingsw.cg25.exceptions.NoCardsException;
import it.polimi.ingsw.cg25.exceptions.NotEnoughAssistantsException;
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
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

/**
 * @author Davide
 *
 */
public class ChangePermitQuickActionTest {

	private static PocketCD4 pocket1;
	public static MatchCD4 model;
	public static ModelProxy proxy;
	public static PlayerCD4 player1;
	public static PlayerCD4 player2;
	public static PlayerCD4 player3;
	public static PlayerCD4 player4;
	public static int unemployedNumber;
	static ChangePermitQuickAction c;

	@Before
	public void setUp() throws Exception {
		BoardFactory factory = new BoardFactory(new FileReader("src/test/resources/nobilityCellsFULL.txt"),
				new FileReader("src/test/resources/politicsFULL.txt"),
				new FileReader("src/test/resources/citiesFULL.txt"), new FileReader("src/test/resources/graphFULL.txt"),
				new FileReader("src/test/resources/kingFULL.txt"),
				new FileReader("src/test/resources/regionsFULL.txt"));
		proxy = new ModelProxy();
		model = new MatchCD4(factory.getBoard(), proxy,false,10);
		pocket1 = new PocketCD4(new Coin(100), new Assistant(100), new NobilityRank(1), new VictoryPoint(0));
		PocketCD4 pocket2 = new PocketCD4(new Coin(100), new Assistant(100), new NobilityRank(1), new VictoryPoint(0));
		player1 = new PlayerCD4(1, "gio", HSBColor.getNDifferent(2).get(0), model, pocket1);
		player2 = new PlayerCD4(0, "nicolo", HSBColor.getNDifferent(2).get(1), model, pocket2);
		player3 = new PlayerCD4(1, "dado", HSBColor.getNDifferent(2).get(0), model, pocket1);
		player4 = new PlayerCD4(0, "marco", HSBColor.getNDifferent(2).get(1), model, pocket2);
		player4.setStatus(true);
		player1.setStatus(true);
		player3.setStatus(true);
		player2.setStatus(true);
		model.addPlayer(player1);
		model.addPlayer(player2);
		model.addPlayer(player3);
		model.addPlayer(player4);
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
		
		c = new ChangePermitQuickAction(model);
		//model.getCurrentPlayer().getPocket().addPocketable(new Assistant(20));
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		model.getLogger().close();
	}


	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.actions.ChangePermitQuickAction#doAction()}.
	 * 
	 * @throws CannotSetupActionException
	 * @throws NotEnoughAssistantsException
	 * @throws CannotPerformActionException
	 */
	@Test(expected = CannotPerformActionException.class)
	public void testDoActionWithNoAssistants()
			throws CannotSetupActionException, NotEnoughAssistantsException, CannotPerformActionException {
		model.getCurrentPlayer().getPocket()
				.subPocketable(new Assistant(model.getCurrentPlayer().getPocket().getAssistants().getSupply()));
		c.setup();
		c.getInteraction(0).registerReply("0");
		c.sendReply(c.getInteraction(0));
		c.doAction();
	}

	
	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.actions.ChangePermitQuickAction#doAction()}.
	 * @throws NoCardsException 
	 * @throws CannotSetupActionException 
	 * @throws CannotPerformActionException 
	 * 
	 */
	@Test(expected = CannotPerformActionException.class)
	public void testDoActionWithNoCards() throws NoCardsException, CannotSetupActionException, CannotPerformActionException{
		while(model.getBoard().getRegions().get(1).getFaceUpPermits().size()>0)
			model.getBoard().getRegions().get(1).getPermit(0);
		// Permit's slots now are empty
		c.setup();
		c.getInteraction(0).registerReply("1");
		c.sendReply(c.getInteraction(0));
		c.doAction();
	}
	
	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.actions.ChangePermitQuickAction#doAction()}.
	 */
	@Test (expected = CannotPerformActionException.class)
	public void testDoActionWithNoAvailableQuickActions() throws CannotPerformActionException {
		while(model.getRemainingQuickActions()!=0)
			try {
				model.useQuickAction();
			} catch (CannotPerformActionException e) {
				fail();
			}
		try {
			c.setup();
		} catch (CannotSetupActionException e) {
			fail();
		}
		c.getInteraction(0).registerReply("0");
		c.sendReply(c.getInteraction(0));
		c.doAction();
	}
	
	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.actions.ChangePermitQuickAction#doAction()}.
	 */
	@Test (expected = CannotPerformActionException.class)
	public void testDoActionWithWrongDTO() throws CannotPerformActionException {
		try {
			c.setup();
		} catch (CannotSetupActionException e) {
			fail();
		}
		c.getInteraction(0).registerReply("0");
		c.sendReply(c.getInteraction(0));
		model.getBoard().getRegions().clear();
		c.doAction();
	}
	
	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.actions.ChangePermitQuickAction#doAction()}.
	 */
	@Test
	public void testDoAction() {
		List<PermitCard> prevPermits = new ArrayList<>(model.getBoard().getRegions().get(0).getFaceUpPermits());
		int remainingQA = model.getRemainingQuickActions();
		int prevAssistants = model.getCurrentPlayer().getPocket().getAssistants().getSupply();
		int prevCoins = model.getCurrentPlayer().getPocket().getCoins().getSupply();
		int prevNobility = model.getCurrentPlayer().getPocket().getNobilityRank().getSupply();
		int prevVictory = model.getCurrentPlayer().getPocket().getVictoryPoints().getSupply();
		try {
			c.setup();
			c.getInteraction(0).registerReply("0");
			c.sendReply(c.getInteraction(0));
			c.doAction();
		} catch (CannotSetupActionException e) {
			fail("Should be possible to setup such an action");
		} catch (CannotPerformActionException e) {
			fail("Should be possible to do such an action");
		}
		model.getBoard().getRegions().get(0).getFaceUpPermits().forEach(new Consumer<PermitCard>() {
			@Override
			public void accept(PermitCard t) {
				assertTrue("Permits are not changing", !prevPermits.contains(t));
			}
		});

		assertTrue("Counter remaining QuickAction error", model.getRemainingQuickActions() == remainingQA - 1);
		
		assertEquals("Player is not paying correctly for doing the action", prevAssistants -1,
				model.getCurrentPlayer().getPocket().getAssistants().getSupply());
		assertEquals("Wrong payment",prevCoins,model.getCurrentPlayer().getPocket().getCoins().getSupply());
		assertEquals("Wrong payment",prevNobility,model.getCurrentPlayer().getPocket().getNobilityRank().getSupply());
		assertEquals("Wrong payment",prevVictory,model.getCurrentPlayer().getPocket().getVictoryPoints().getSupply());
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.actions.ChangePermitQuickAction#setup()}.
	 * 
	 * @throws CannotSetupActionException
	 */
	@Test(expected = CannotSetupActionException.class)
	public void testSetupWithNoRegions() throws CannotSetupActionException {
		c.setup(null);
	}
	
	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.actions.ChangePermitQuickAction#setup()}.
	 * @throws CannotSetupActionException 
	 * @throws CannotPerformActionException 
	 */
	@Test
	public void testSetupDuringAction() throws CannotSetupActionException, CannotPerformActionException {
		c.setup();
		c.getInteraction(0).registerReply("0");
		c.sendReply(c.getInteraction(0));
		c.setup();
		c.doAction();
		return;
	}
	
	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.actions.ChangePermitQuickAction#setup()}.
	 * @throws CannotSetupActionException 
	 */
	@Test
	public void testSetup() throws CannotSetupActionException {
		c.setup();
		assertEquals("One interatcion expected", 1, c.getInteractions().size());
	}
	
	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.actions.ChangePermitQuickAction#setup()}.
	 * @throws CannotSetupActionException 
	 */
	@Test
	public void testSetupParameter() throws CannotSetupActionException {
		c.setup(model.getBoard().getRegions());
		assertEquals("One interatcion expected", 1, c.getInteractions().size());
	}
	

}
