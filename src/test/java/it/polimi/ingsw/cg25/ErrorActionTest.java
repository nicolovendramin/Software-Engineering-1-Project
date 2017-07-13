package it.polimi.ingsw.cg25;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileReader;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.actions.ChooseActionTypeAction;
import it.polimi.ingsw.cg25.actions.ErrorAction;
import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.model.Assistant;
import it.polimi.ingsw.cg25.model.Coin;
import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.NobilityRank;
import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.PocketCD4;
import it.polimi.ingsw.cg25.model.VictoryPoint;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

/**
 * 
 * @author Giovanni
 *
 */
public class ErrorActionTest {

	private PocketCD4 pocket1;
	private PocketCD4 pocket2;
	private MatchCD4 model;
	private ModelProxy proxy;
	private PlayerCD4 player1;
	private PlayerCD4 player2;
	
	@Before
	public void setUp() throws Exception {
		BoardFactory factory = new BoardFactory(new FileReader("src/test/resources/nobilityCells.txt"),
				new FileReader("src/test/resources/politics.txt"),
				new FileReader("src/test/resources/cities.txt"),
				new FileReader("src/test/resources/graph.txt"),
				new FileReader("src/test/resources/king.txt"),
				new FileReader("src/test/resources/regions.txt"));
		proxy = new ModelProxy();
		model = new MatchCD4(factory.getBoard(), proxy, true,10);
		pocket1 = new PocketCD4(new Coin(100),new Assistant(100),new NobilityRank(1),new VictoryPoint(0));
		pocket2 = new PocketCD4(new Coin(100),new Assistant(100),new NobilityRank(1),new VictoryPoint(0));
		player1 = new PlayerCD4(1,"gio",HSBColor.getNDifferent(2).get(0),model,pocket1);
		player2 = new PlayerCD4(2, "nicolo", HSBColor.getNDifferent(2).get(1), model,pocket2);
		player1.setStatus(true);
		player2.setStatus(true);
		model.addPlayer(player1);
		model.addPlayer(player2);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void notNullModel() {
		new ErrorAction<MatchCD4>(null);
	}
	
	@Test
	public void testDoAction() throws CannotPerformActionException {
		ErrorAction<MatchCD4> error = new ErrorAction<MatchCD4>(model);
		assertEquals(error.doAction().toString(), new ChooseActionTypeAction<MatchCD4>(model).toString());
	}
	
	@Test
	public void testSetup() {
		ErrorAction<MatchCD4> error = new ErrorAction<MatchCD4>(model);
		//Set a display interaction with error message
		error.setup();
		assertTrue(error.getInteraction(0).printOptions().equals("Unfortunately an error occurred. Retry\nSend [Action]-anyKey- to go on"));
	}
	
	@Test
	public void testSetUpWithACustomMessage() {
		ErrorAction<MatchCD4> error = new ErrorAction<MatchCD4>(model);
		//Set a display interaction with a custom error message
		error.setup("Errore");
		assertTrue(error.getInteraction(0).printOptions().equals("Errore".concat("\nSend [Action]-anyKey- to go on")));
		//Do nothing
		error.setup();
		//Check nothing has changed
		assertTrue(error.getInteraction(0).printOptions().equals("Errore".concat("\nSend [Action]-anyKey- to go on")));
	}
	
}
