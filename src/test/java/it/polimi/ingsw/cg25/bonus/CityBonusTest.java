package it.polimi.ingsw.cg25.bonus;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.actions.CityBonusAction;
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
import it.polimi.ingsw.cg25.model.dashboard.bonus.CityBonus;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

@SuppressWarnings("unused")
public class CityBonusTest {

	private MatchCD4 model;
	private ModelProxy proxy;
	private PlayerCD4 player;
	private BoardFactory factory;
	
	@Before
	public void setUp() throws FileNotFoundException, CannotCreateGameException {
		factory = new BoardFactory(new FileReader("src/test/resources/nobilityCells.txt"),
				new FileReader("src/test/resources/politics.txt"), 
				new FileReader("src/test/resources/cities.txt"),
				new FileReader("src/test/resources/graph.txt"), 
				new FileReader("src/test/resources/king.txt"),
				new FileReader("src/test/resources/regions.txt"));
		this.proxy = new ModelProxy();
		this.model = new MatchCD4(factory.getBoard(), this.proxy,false,10);
		//Init an empty pocket
		PocketCD4 pocket1 = new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(0), new VictoryPoint(0));
		player = new PlayerCD4(1, "Gio", HSBColor.getNDifferent(1).get(0), model, pocket1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCityBonusWith0CitiesToChoose() {
		CityBonus b = new CityBonus(0);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCityBonusWithLessThan0CitiesToChoose() {
		CityBonus b = new CityBonus(-4);
	}
	
	@Test
	public void testGetNumOfitiesToChoose() {
		CityBonus b = new CityBonus(10);
		assertEquals(10, b.getNumOfCitiesToChoose());
	}
	
	@Test
	public void testToString() {
		assertEquals("Cities To Choose -> 12", new CityBonus(12).toString());
	}
	
	@Test
	public void testAcquireBonus() throws NoCardsException, OneEmporiumOnlyException {
		CityBonus b = new CityBonus(1);
		//Building an emporium in at least a city
		player.buildEmporium(model.getBoard().getCities().get(0));
		b.acquireBonus(player);
		assertEquals(1, model.getActionsStack().size());
		assertEquals(CityBonusAction.class, model.popAction().getClass());
	}
	
	@After
	public void teardown() {
		model.getLogger().close();
	}
	
}
