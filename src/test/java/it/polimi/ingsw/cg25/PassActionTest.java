package it.polimi.ingsw.cg25;

import static org.junit.Assert.*;

import java.io.FileReader;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.actions.ChooseActionTypeAction;
import it.polimi.ingsw.cg25.actions.PassAction;
import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.EndGameException;
import it.polimi.ingsw.cg25.model.Assistant;
import it.polimi.ingsw.cg25.model.Coin;
import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.NobilityRank;
import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.PocketCD4;
import it.polimi.ingsw.cg25.model.VictoryPoint;
import it.polimi.ingsw.cg25.model.trade.Market;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

public class PassActionTest {

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
	
	@Test(expected = IllegalArgumentException.class)
	public void notNullModel() {
		new PassAction<MatchCD4>(null);
	}

	@Test
	public void testToString(){
		assertEquals(new PassAction<Market>(model.getMarket()).toString(),"Pass");
	}
	
	@Test(expected=CannotPerformActionException.class)
	public void testPassWhenYouCant() throws CannotPerformActionException{
		PassAction<MatchCD4> modelPass = new PassAction<MatchCD4>(model);
		modelPass.doAction();
	
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testPassWhenYouCanPass() throws CannotPerformActionException, EndGameException{
		PassAction<MatchCD4> modelPass = new PassAction<MatchCD4>(model);
		PlayerCD4 player = model.nextPlayer();
		model.useMainAction();
		ChooseActionTypeAction<MatchCD4> returned = (ChooseActionTypeAction<MatchCD4>) modelPass.doAction();
		assertEquals(returned.toString(), new ChooseActionTypeAction<MatchCD4>(model).toString());
		assertTrue(model.getCurrentPlayer() == player);
	}
	
	@Test
	public void testPassWhenYouCanPassMarket() throws CannotPerformActionException, EndGameException{
		model.getMarket().openMarket();
		PassAction<Market> modelPass = new PassAction<Market>(model.getMarket());
		PlayerCD4 player = model.getMarket().nextPlayer();
		assertEquals(modelPass.doAction().toString(),new ChooseActionTypeAction<Market>(model.getMarket()).toString());
		assertTrue(model.getMarket().getCurrentPlayer()==player);
	}
	
}
