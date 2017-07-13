package it.polimi.ingsw.cg25.dashboard;

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
public class EmporiumTest {

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
		
		color = new HSBColor(220, 100, 100, "color");
		
		cityColorRew = new ArrayList<>();
		cityColorRew.add(new CoinBonus(2));
		cityColorRew.add(new VictoryPointBonus(1));
		
		cityColor= new CityColor(color, cityColorRew);
		
		cityBonus = new ArrayList<>();
		cityBonus.add(new AssistantBonus(10));
		cityBonus.add(new NobilityPointBonus(5));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ShouldNotBePossibleToCreateAnEmporiumWithoutAPlayer() {
		City c = new City("Varese", cityColor, cityBonus);
		Emporium emp = new Emporium(null, c);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ShouldNotBePossibleToCreateAnEmporiumWithoutACity() {
		City c = new City("Varese", cityColor, cityBonus);
		Emporium emp = new Emporium(player, null);
	}
	
	@Test
	public void testGetOwner() {
		City c = new City("Varese", cityColor, cityBonus);
		Emporium emp = new Emporium(player, c);
		assertEquals(player, emp.getOwner());
	}
	
	@Test
	public void testGetCity() {
		City c = new City("Mantova", cityColor, cityBonus);
		Emporium emp = new Emporium(player, c);
		assertEquals(c, emp.getCity());
	}
	
	@After
	public void teardown() {
		model.getLogger().close();
	}
	
}
