/**
 * 
 */
package it.polimi.ingsw.cg25.quickactions;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.actions.HireAssistantQuickAction;
import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
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
public class HireAssistantQuickActionTest {
	private static PocketCD4 pocket1;
	public static MatchCD4 model;
	public static ModelProxy proxy;
	public static PlayerCD4 player1;
	public static PlayerCD4 player2;
	public static PlayerCD4 player3;
	public static PlayerCD4 player4;
	HireAssistantQuickAction h;

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

		h = new HireAssistantQuickAction(model);
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
	 * {@link it.polimi.ingsw.cg25.actions.HireAssistantQuickAction#doAction()}.
	 */
	@Test
	public void testDoAction() {
		int remQuick = model.getRemainingQuickActions();
		int prevAssistants = model.getCurrentPlayer().getPocket().getAssistants().getSupply();
		int prevCoins = model.getCurrentPlayer().getPocket().getCoins().getSupply();
		int prevNobility = model.getCurrentPlayer().getPocket().getNobilityRank().getSupply();
		int prevVictory = model.getCurrentPlayer().getPocket().getVictoryPoints().getSupply();
		try {
			h.doAction();
		} catch (CannotPerformActionException e) {
			fail("Should be possible to run such an action");
		}
		assertEquals("Player is not paying correctly for doing the action", prevCoins - 3,
				model.getCurrentPlayer().getPocket().getCoins().getSupply());
		assertEquals("Counter remaining QuickAction error", remQuick - 1, model.getRemainingQuickActions());
		assertEquals("Not giving an assistant to the player", prevAssistants + 1,
				model.getCurrentPlayer().getPocket().getAssistants().getSupply());
		assertEquals("Wrong payment",prevNobility,model.getCurrentPlayer().getPocket().getNobilityRank().getSupply());
		assertEquals("Wrong payment",prevVictory,model.getCurrentPlayer().getPocket().getVictoryPoints().getSupply());
	}
	
	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.actions.HireAssistantQuickAction#doAction()}.
	 * @throws CannotPerformActionException 
	 */
	@Test (expected = CannotPerformActionException.class)
	public void testDoActionWithNoCoins() throws CannotPerformActionException {
		while(model.getCurrentPlayer().getPocket().getCoins().getSupply() > 2)
			try {
				model.getCurrentPlayer().getPocket().subPocketable(new Coin(1));
			} catch (NotEnoughCoinException e) {
				fail();
			}
		h.doAction();
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.actions.HireAssistantQuickAction#doAction()}.
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
		h.doAction();
	}

}
