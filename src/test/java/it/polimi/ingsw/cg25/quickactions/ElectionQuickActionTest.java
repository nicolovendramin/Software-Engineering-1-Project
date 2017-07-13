/**
 * 
 */
package it.polimi.ingsw.cg25.quickactions;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.activity.InvalidActivityException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.actions.ElectionQuickAction;
import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.NotEnoughAssistantsException;
import it.polimi.ingsw.cg25.model.Assistant;
import it.polimi.ingsw.cg25.model.Coin;
import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.NobilityRank;
import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.PocketCD4;
import it.polimi.ingsw.cg25.model.VictoryPoint;
import it.polimi.ingsw.cg25.model.dashboard.Councelor;
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
public class ElectionQuickActionTest {
	private static PocketCD4 pocket1;
	public static MatchCD4 model;
	public static ModelProxy proxy;
	public static PlayerCD4 player1;
	public static PlayerCD4 player2;
	public static PlayerCD4 player3;
	public static PlayerCD4 player4;
	ElectionQuickAction e;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		BoardFactory factory = new BoardFactory(new FileReader("src/test/resources/nobilityCells.txt"),
				new FileReader("src/test/resources/politics.txt"), new FileReader("src/test/resources/cities.txt"),
				new FileReader("src/test/resources/graph.txt"), new FileReader("src/test/resources/king.txt"),
				new FileReader("src/test/resources/regions.txt"));
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

		e = new ElectionQuickAction(model);
		// model.getCurrentPlayer().getPocket().addPocketable(new Assistant(1));
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
	 * {@link it.polimi.ingsw.cg25.actions.ElectionQuickAction#doAction()}.
	 * 
	 * @throws InvalidActivityException
	 */
	@Test
	public void testDoAction() throws InvalidActivityException {
		// Council 0 = King's Council !
		e.setup();
		e.getInteraction(0).registerReply("0");
		e.sendReply(e.getInteraction(0));
		e.getInteraction(1).registerReply("0");
		e.sendReply(e.getInteraction(1));
		int unempC = model.getBoard().getUnemployedCouncelors().size();
		int remQuick = model.getRemainingQuickActions();
		List<Councelor> copyCouncil = new ArrayList<>();
		copyCouncil.addAll(model.getBoard().getKingCouncil().getCouncelors());
		Councelor councelor = model.getBoard().getUnemployedCouncelors().get(0);
		int counter = Collections.frequency(model.getBoard().getUnemployedCouncelors(), councelor);
		int prevAssistants = model.getCurrentPlayer().getPocket().getAssistants().getSupply();
		int prevCoins = model.getCurrentPlayer().getPocket().getCoins().getSupply();
		int prevNobility = model.getCurrentPlayer().getPocket().getNobilityRank().getSupply();
		int prevVictory = model.getCurrentPlayer().getPocket().getVictoryPoints().getSupply();
		try {
			e.doAction();
		} catch (CannotPerformActionException e1) {
			fail("Should be possible to do such an action");
		}
		assertTrue("Number of unemployed councelors is different",
				unempC == model.getBoard().getUnemployedCouncelors().size());

		if (copyCouncil.get(0).equals(councelor))
			assertEquals("Incorrect occurrency of councelor", counter,
					Collections.frequency(model.getBoard().getUnemployedCouncelors(), councelor));
		else
			assertEquals("Incorrect occurrency of councelor", counter - 1,
					Collections.frequency(model.getBoard().getUnemployedCouncelors(), councelor));
		assertEquals("Counter remaining QuickAction error", remQuick - 1, model.getRemainingQuickActions());
		assertEquals("Player is not paying correctly for doing the action", prevAssistants - 1,
				model.getCurrentPlayer().getPocket().getAssistants().getSupply());
		assertEquals("Wrong payment",prevCoins,model.getCurrentPlayer().getPocket().getCoins().getSupply());
		assertEquals("Wrong payment",prevNobility,model.getCurrentPlayer().getPocket().getNobilityRank().getSupply());
		assertEquals("Wrong payment",prevVictory,model.getCurrentPlayer().getPocket().getVictoryPoints().getSupply());
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.actions.ElectionQuickAction#doAction()}.
	 * 
	 * @throws NotEnoughAssistantsException
	 * @throws CannotPerformActionException
	 */
	@Test(expected = CannotPerformActionException.class)
	public void testDoActionWithNoAssistants() throws NotEnoughAssistantsException, CannotPerformActionException {
		e.setup();
		e.getInteraction(0).registerReply("0");
		e.sendReply(e.getInteraction(0));
		e.getInteraction(1).registerReply("0");
		e.sendReply(e.getInteraction(1));

		model.getCurrentPlayer().getPocket()
				.subPocketable(new Assistant(model.getCurrentPlayer().getPocket().getAssistants().getSupply()));
		e.doAction();

	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.actions.ElectionQuickAction#doAction()}.
	 * 
	 * @throws CannotPerformActionException
	 */
	@Test(expected = CannotPerformActionException.class)
	public void testDoActionWithWrongDTO() throws CannotPerformActionException {
		// Council 0 = King's Council !
		e.setup();
		e.getInteraction(0).registerReply("0");
		e.sendReply(e.getInteraction(0));
		e.getInteraction(1).registerReply("0");
		e.sendReply(e.getInteraction(1));
		model.getBoard().getUnemployedCouncelors().clear();
		e.doAction();
	}
	
	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.actions.ElectionQuickAction#doAction()}.
	 * @throws CannotPerformActionException 
	 * 
	 */
	@Test (expected = CannotPerformActionException.class)
	public void testDoActionWithNoAvailableQuickActions() throws CannotPerformActionException {
		while(model.getRemainingQuickActions()!=0)
			try {
				model.useQuickAction();
			} catch (CannotPerformActionException e) {
				fail();
			}
		e.setup();
		e.getInteraction(0).registerReply("0");
		e.sendReply(e.getInteraction(0));
		e.getInteraction(1).registerReply("0");
		e.sendReply(e.getInteraction(1));
		e.doAction();
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.actions.ElectionQuickAction#setup()}.
	 */
	@Test
	public void testSetupNormal() {
		e.setup();
		assertEquals("Two interatcions expected", 2, e.getInteractions().size());
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.actions.ElectionQuickAction#setup()}.
	 */
	@Test
	public void testSetupDuringAction() {
		e.setup();
		e.getInteraction(0).registerReply("0");
		e.sendReply(e.getInteraction(0));
		e.setup();
		e.getInteraction(1).registerReply("0");
		e.sendReply(e.getInteraction(1));
		try {
			e.doAction();
		} catch (CannotPerformActionException e1) {
			fail("Calling setup during the action should not stop it");
		}
		return;
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.actions.ElectionQuickAction#setup(java.util.List, java.util.List)}
	 * .
	 */
	@Test
	public void testSetupListOfCouncelorListOfCouncil() {
		e.setup(model.getBoard().getUnemployedCouncelors(), Arrays.asList(model.getBoard().getRegions().get(0).getCouncil(),model.getBoard().getRegions().get(1).getCouncil()));
		assertEquals("Two interatcions expected", 2, e.getInteractions().size());
	}

}
