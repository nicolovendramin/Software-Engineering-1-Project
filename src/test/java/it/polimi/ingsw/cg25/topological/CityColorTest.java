package it.polimi.ingsw.cg25.topological;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.exceptions.CannotCreateGameException;
import it.polimi.ingsw.cg25.model.Assistant;
import it.polimi.ingsw.cg25.model.Coin;
import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.NobilityRank;
import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.PocketCD4;
import it.polimi.ingsw.cg25.model.VictoryPoint;
import it.polimi.ingsw.cg25.model.dashboard.bonus.AssistantBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.CoinBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.NobilityPointBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.VictoryPointBonus;
import it.polimi.ingsw.cg25.model.dashboard.topological.CityColor;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("unused")
public class CityColorTest {

	private BoardFactory factory;
	
	private MatchCD4 model;
	private ModelProxy proxy;
	private PocketCD4 pocket;
	private PlayerCD4 player;
	
	private HSBColor color;
	private HSBColor color2;
	
	private List<Bonus> b;
	private List<Bonus> b2;
	
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
		//Init pocket
		pocket = new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(0), new VictoryPoint(0));
		player = new PlayerCD4(1, "Gio", HSBColor.getNDifferent(1).get(0), model, pocket);
		
		color = new HSBColor(100, 100, 100, "color");
		color2 = new HSBColor(200, 60, 90, "color2");
		
		b = new ArrayList<>();
		b.add(new CoinBonus(10));
		b.add(new AssistantBonus(2));
		
		b2 = new ArrayList<>();
		b2.add(new VictoryPointBonus(5));
		b2.add(new NobilityPointBonus(4));
	}
	
	@Test(expected=NullPointerException.class)
	public void ShouldNotBePossibleToCreateACityColorWithoutAHSBColor() {
		CityColor cc = new CityColor(null, b);
	}
	
	@Test(expected=NullPointerException.class)
	public void ShouldNotBePossibleToCreateACityColorWithoutAListOfBonuses() {
		CityColor cc = new CityColor(color, null);
	}
	
	@Test
	public void testGetColor() {
		CityColor cc = new CityColor(color, b);
		assertTrue(cc.getColor().equals(color));
		
		CityColor cc2 = new CityColor(color2, b2);
		assertTrue(cc2.getColor().equals(color2));
		
		assertFalse(cc.getColor().equals(cc2.getColor()));
	}
	
	@Test
	public void testGetColorReward() {
		CityColor cc = new CityColor(color, b);
		assertEquals(2, cc.getColorReward().size());
		assertEquals(CoinBonus.class, cc.getColorReward().get(0).getClass());
		assertEquals(AssistantBonus.class, cc.getColorReward().get(1).getClass());
	}
	
	@Test
	public void testTakeAllBonuses() {
		CityColor cc2 = new CityColor(color2, b2);
		cc2.takeAllBonuses(player);
		assertEquals(5, player.getPocket().getVictoryPoints().getSupply());
		assertEquals(4, player.getPocket().getNobilityRank().getSupply());
		//Check b2 is empty
		assertTrue(cc2.getColorReward().isEmpty());
	}
	
	@After
	public void teardown() {
		model.getLogger().close();
	}
	
}
