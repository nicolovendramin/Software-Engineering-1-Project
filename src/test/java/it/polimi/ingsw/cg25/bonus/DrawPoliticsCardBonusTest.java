package it.polimi.ingsw.cg25.bonus;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.FileReader;

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
import it.polimi.ingsw.cg25.model.dashboard.bonus.DrawPoliticsCardBonus;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("unused")
public class DrawPoliticsCardBonusTest {

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
	public void testDrawPoliticsCardBonusWith0CardsToDraw() {
		DrawPoliticsCardBonus b = new DrawPoliticsCardBonus(0);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testDrawPoliticsCardBonusWithLessThan0CardsToDraw() {
		DrawPoliticsCardBonus b = new DrawPoliticsCardBonus(-2);
	}
	
	@Test
	public void testGetNumberOfCardsToDraw() {
		DrawPoliticsCardBonus b = new DrawPoliticsCardBonus(9);
		assertEquals(9, b.getNumOfCardsToDraw());
	}
	
	@Test
	public void testAcquireBonus() {
		new DrawPoliticsCardBonus(12).acquireBonus(player);
		assertEquals(12, player.getPoliticsCards().size());
	}
	
	@Test
	public void testToString() {
		assertEquals("Cards To Draw -> 1", new DrawPoliticsCardBonus(1).toString());
	}
	
	@After
	public void teardown() {
		model.getLogger().close();
	}
	
}
