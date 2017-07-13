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
import it.polimi.ingsw.cg25.model.dashboard.Emporium;
import it.polimi.ingsw.cg25.model.dashboard.bonus.AssistantBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.CoinBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.NobilityPointBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.VictoryPointBonus;
import it.polimi.ingsw.cg25.model.dashboard.topological.City;
import it.polimi.ingsw.cg25.model.dashboard.topological.CityColor;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("unused")
public class CityTest {

	private BoardFactory factory;
	
	private MatchCD4 model;
	private ModelProxy proxy;
	private PocketCD4 pocket;
	private PlayerCD4 player;
	
	private HSBColor color;
	private CityColor cityColor;
	
	private List<Bonus> cityColorRew;
	private List<Bonus> cityBonus;
	
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
		
		color = new HSBColor(350, 100, 100, "color");
		
		cityColorRew = new ArrayList<>();
		cityColorRew.add(new CoinBonus(10));
		cityColorRew.add(new VictoryPointBonus(2));
		
		cityColor= new CityColor(color, cityColorRew);
		
		cityBonus = new ArrayList<>();
		cityBonus.add(new AssistantBonus(6));
		cityBonus.add(new NobilityPointBonus(9));
	}
	
	@Test(expected=NullPointerException.class)
	public void ShouldNotBePossibleToCreateACityWithoutAName() {
		City c = new City(null, cityColor, cityBonus);
	}
	
	@Test(expected=NullPointerException.class)
	public void ShouldNotBePossibleToCreateACityWithoutACityColor() {
		City c = new City("Milano", null, cityBonus);
	}
	
	@Test(expected=NullPointerException.class)
	public void ShouldNotBePossibleToCreateACityWithoutABonusList() {
		City c = new City("Milano", cityColor, null);
	}
	
	@Test
	public void testGetName() {
		City c = new City("Milano", cityColor, cityBonus);
		assertEquals("Milano", c.getName());
	}
	
	/**
	 * equals method tests only th name of the city
	 */
	@Test
	public void testEquals() {
		City c = new City("Milano", cityColor, cityBonus);
		City c2 = new City("Torino", cityColor, cityBonus);
		
		assertFalse(c.equals(c2));
		assertTrue(c.equals(new City("Milano", cityColor, cityBonus)));
		//null obj
		assertFalse(c.equals(null));
		//Different class
		assertFalse(c.equals(cityColor));
	}
	
	@Test
	public void testHashCode() {
		City c = new City("Milano", cityColor, cityBonus);
		City c2 = new City("Torino", cityColor, cityBonus);
		
		assertFalse(c.hashCode() == c2.hashCode());
		assertTrue(c2.hashCode() == c2.hashCode());
	}
	
	@Test
	public void testTakeAllBonuses() {
		City c = new City("Milano", cityColor, cityBonus);
		c.takeAllBonuses(player);
		assertEquals(6, player.getPocket().getAssistants().getSupply());
		assertEquals(9, player.getPocket().getNobilityRank().getSupply());
		//Check cityBonus is not empty after takeAllBonuses
		assertFalse(c.getCityBonus().isEmpty());
	}
	
	@Test
	public void testGetColor() {
		City c = new City("Milano", cityColor, cityBonus);
		assertEquals(CityColor.class, c.getColor().getClass());
		assertEquals(color, c.getColor().getColor());
		assertEquals(2, c.getColor().getColorReward().size());
	}
	
	@Test
	public void testGetCityBonus() {
		City c = new City("Milano", cityColor, cityBonus);
		assertEquals(2, c.getCityBonus().size());
		assertEquals(AssistantBonus.class, c.getCityBonus().get(0).getClass());
		assertEquals(NobilityPointBonus.class, c.getCityBonus().get(1).getClass());
	}
	
	@Test
	public void testAddEmporium() {
		City c = new City("Milano", cityColor, cityBonus);
		Emporium emp = new Emporium(player, c);
		c.addEmporium(emp);
		assertEquals(1, c.getCityEmporiums().size());
	}
	
	@Test
	public void testGetNumEmp() {
		City c = new City("Milano", cityColor, cityBonus);
		Emporium emp = new Emporium(player, c);
		c.addEmporium(emp);
		assertEquals(1, c.getNumEmp());
	}
	
	@Test
	public void testGetCityEmporiums() {
		City c = new City("Milano", cityColor, cityBonus);
		Emporium emp = new Emporium(player, c);
		c.addEmporium(emp);
		assertEquals(1, c.getCityEmporiums().size());
		assertEquals(Emporium.class, c.getCityEmporiums().get(0).getClass());
	}
	
	@After
	public void teardown() {
		model.getLogger().close();
	}
	
}
