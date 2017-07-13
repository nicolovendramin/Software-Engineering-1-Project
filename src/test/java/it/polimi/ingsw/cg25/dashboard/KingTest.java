package it.polimi.ingsw.cg25.dashboard;

import static org.junit.Assert.*;

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
import it.polimi.ingsw.cg25.model.dashboard.King;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("unused")
public class KingTest {

	private BoardFactory factory;
	
	private MatchCD4 model;
	private ModelProxy proxy;
	private PocketCD4 pocket;
	private PlayerCD4 player;
	
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
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ShouldNotBePossibleToCreateAKingWithoutAValidCity() {
		King king = new King(model.getBoard(), null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ShouldNotBePossibleToCreateAKingWithoutAValidBoard() {
		King king = new King(null, model.getBoard().getCities().get(0));
	}
	
	@Test
	public void testMoveKing() {
		//Initial king city: Milano
		King king = new King(model.getBoard(), model.getBoard().getCities().get(0));
		//Final king city: Torino
		//Milano is directly linked to Torino
		assertEquals(2, king.moveKing(model.getBoard().getCities().get(1)));
	}
	
	@Test
	public void testGetCurrentCity() {
		//Initial king city: Torino
		King king = new King(model.getBoard(), model.getBoard().getCities().get(1));
		assertEquals("Torino", king.getCurrentCity().getName());
	}
	
	@Test
	public void setKingCity() {
		//Initial king city: Milano
		King king = new King(model.getBoard(), model.getBoard().getCities().get(0));
		king.setKingCity(model.getBoard().getCities().get(1));
		assertEquals("Torino", king.getCurrentCity().getName());
	}
	
	@After
	public void teardown() {
		model.getLogger().close();
	}
	
}
