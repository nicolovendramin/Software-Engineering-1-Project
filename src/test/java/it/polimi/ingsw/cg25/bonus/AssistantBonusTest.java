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
import it.polimi.ingsw.cg25.model.dashboard.bonus.AssistantBonus;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("unused")
public class AssistantBonusTest {
	
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
	public void testAssistantBonusWith0Assistants() {
		AssistantBonus b = new AssistantBonus(0);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAssistantBonusWithLessThan0Assistants() {
		AssistantBonus b = new AssistantBonus(-1);
	}
	
	@Test
	public void testGetNumberOfAssistants() {
		AssistantBonus b = new AssistantBonus(5);
		assertEquals(5, b.getNumberOfAssistants());
	}
	
	@Test
	public void testAcquireBonus() {
		new AssistantBonus(9).acquireBonus(player);
		assertEquals(9, player.getPocket().getAssistants().getSupply());
	}
	
	@Test
	public void testToString() {
		assertEquals("Assistants -> 4", new AssistantBonus(4).toString());
	}
	
	@After
	public void teardown() {
		model.getLogger().close();
	}
	
}
