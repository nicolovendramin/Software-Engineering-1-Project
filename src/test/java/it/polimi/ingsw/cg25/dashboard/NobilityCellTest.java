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
import it.polimi.ingsw.cg25.model.dashboard.NobilityCell;
import it.polimi.ingsw.cg25.model.dashboard.bonus.AssistantBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.CoinBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.NobilityPointBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.VictoryPointBonus;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("unused")
public class NobilityCellTest {

	private BoardFactory factory;
	
	private MatchCD4 model;
	private ModelProxy proxy;
	private PocketCD4 pocket;
	private PlayerCD4 player;
	
	private List<Bonus> ncRew;
	private List<Bonus> ncRew2;
	
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
		
		ncRew = new ArrayList<>();
		ncRew.add(new CoinBonus(1));
		ncRew.add(new VictoryPointBonus(15));
		
		ncRew2 = new ArrayList<>();
		ncRew2.add(new AssistantBonus(6));
		ncRew2.add(new NobilityPointBonus(8));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ShouldNotBePossibleToCreateANobilityCellWithAnInvalidId() {
		NobilityCell nc = new NobilityCell(-10, ncRew);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ShouldNotBePossibleToCreateANobilityCellWithoutAValidListOfBonuses() {
		NobilityCell nc = new NobilityCell(5, null);
	}
	
	@Test
	public void testGetId() {
		NobilityCell nc = new NobilityCell(10, ncRew);
		assertEquals(10, nc.getId());
	}
	
	@Test
	public void testGetBonusList() {
		NobilityCell nc = new NobilityCell(2, ncRew);
		assertEquals(2, nc.getBonusList().size());
		assertEquals(CoinBonus.class, nc.getBonusList().get(0).getClass());
		assertEquals(VictoryPointBonus.class, nc.getBonusList().get(1).getClass());
	}
	
	@Test
	public void testTakeAllBonuses() {
		NobilityCell nc2 = new NobilityCell(3, ncRew2);
		nc2.takeAllBonuses(player);
		assertEquals(6, player.getPocket().getAssistants().getSupply());
		assertEquals(8, player.getPocket().getNobilityRank().getSupply());
	}
	
	@After
	public void teardown() {
		model.getLogger().close();
	}
	
}
