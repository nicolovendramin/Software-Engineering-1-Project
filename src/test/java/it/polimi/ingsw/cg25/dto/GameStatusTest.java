package it.polimi.ingsw.cg25.dto;

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
import it.polimi.ingsw.cg25.model.dto.DTOBoardCD4;
import it.polimi.ingsw.cg25.model.dto.DTOPlayerCD4;
import it.polimi.ingsw.cg25.model.dto.GameStatus;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

@SuppressWarnings("unused")
public class GameStatusTest {

	private MatchCD4 model;
	private ModelProxy proxy;
	private BoardFactory factory;
	
	private PlayerCD4 player;
	private PlayerCD4 player2;
	private PocketCD4 pocket;
	private PocketCD4 pocket2;
	
	@Before
	public void setUp() throws FileNotFoundException, CannotCreateGameException {
		factory = new BoardFactory(new FileReader("src/test/resources/nobilityCellsFULL.txt"),
				new FileReader("src/test/resources/politicsFULL.txt"), 
				new FileReader("src/test/resources/citiesFULL.txt"),
				new FileReader("src/test/resources/graphFULL.txt"), 
				new FileReader("src/test/resources/kingFULL.txt"),
				new FileReader("src/test/resources/regionsFULL.txt"));
		this.proxy = new ModelProxy();
		//Create a match
		this.model = new MatchCD4(factory.getBoard(), this.proxy,false,10);
		//Init pockets
		pocket = new PocketCD4(new Coin(10), new Assistant(0), new NobilityRank(0), new VictoryPoint(0));
		pocket2 = new PocketCD4(new Coin(10), new Assistant(0), new NobilityRank(0), new VictoryPoint(0));
		player = new PlayerCD4(1, "Gio", HSBColor.getNDifferent(1).get(0), model, pocket);
		player2 = new PlayerCD4(1, "Nick", HSBColor.getNDifferent(1).get(0), model, pocket2);
		
		model.addPlayer(player);
		model.addPlayer(player2);
	}
	
	@Test(expected=NullPointerException.class)
	public void testGameStatusWithoutAMatch() {
		GameStatus gs = new GameStatus(null, player);
	}
	
	@Test(expected=NullPointerException.class)
	public void testGameStatusWithoutARequestingPlayer() {
		GameStatus gs = new GameStatus(model, null);
	}
	
	@Test
	public void testGetBoard() {
		GameStatus gs = new GameStatus(model, player);
		assertEquals(DTOBoardCD4.class, gs.getBoard().getClass());
	}
	
	@Test
	public void testGetRequestingPlayer() {
		GameStatus gs = new GameStatus(model, player);
		assertTrue(gs.getRequestingPlayer().equals(new DTOPlayerCD4(player)));
	}
	
	@Test
	public void testGetOtherPlayers() {
		GameStatus gs = new GameStatus(model, player);
		List<DTOPlayerCD4> otherPlayers = new ArrayList<>();
		otherPlayers.add(new DTOPlayerCD4(player2));
		
		assertTrue(gs.getOtherPlayers().equals(otherPlayers));
	}
	
	@Test
	public void testIsRunning() {
		GameStatus gs = new GameStatus(model, player);
		assertTrue(gs.isRunning() == model.isRunning());
	}
	
	@Test
	public void testEmptyHighScorersListIfMatchIsRunning() {
		GameStatus gs = new GameStatus(model, player);
		assertTrue(gs.getHighScorers().isEmpty());
	}
	
	@Test
	public void testToStringIsNotNull() {
		GameStatus gs = new GameStatus(model, player);
		assertTrue(!(gs.toString() == null));
	}
	
	@Test
	public void testToStringIsNotEmpty() {
		GameStatus gs = new GameStatus(model, player);
		assertTrue(!(gs.toString().equals("")));
	}
	
	@After
	public void teardown() {
		model.getLogger().close();
	}
	
}
