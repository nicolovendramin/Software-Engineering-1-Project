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
import it.polimi.ingsw.cg25.model.dto.DTOPlayerCD4;
import it.polimi.ingsw.cg25.model.dto.DTOPocketCD4;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

public class DTOPlayerCD4Test {

	private MatchCD4 model;
	private ModelProxy proxy;
	private PlayerCD4 player;
	private PlayerCD4 player2;
	private DTOPlayerCD4 dtoPlayer;
	private DTOPlayerCD4 dtoPlayer2;
	private BoardFactory factory;
	private PocketCD4 pocket;
	private PocketCD4 pocket2;
	
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
		//Init pockets
		pocket = new PocketCD4(new Coin(1), new Assistant(0), new NobilityRank(5), new VictoryPoint(4));
		pocket2 = new PocketCD4(new Coin(2), new Assistant(2), new NobilityRank(2), new VictoryPoint(2));
		player = new PlayerCD4(1, "Gio", HSBColor.getNDifferent(1).get(0), model, pocket);
		player2 = new PlayerCD4(2, "Nick", HSBColor.getNDifferent(2).get(1), model, pocket2);
		
		dtoPlayer = new DTOPlayerCD4(player);
		dtoPlayer2 = new DTOPlayerCD4(player2);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ShouldNotBePossibleToCreateADTOPlayerWithoutAValidPlayer() {
		new DTOPlayerCD4(null);
	}
	
	@Test
	public void testConvertAll() {
		List<PlayerCD4> players = new ArrayList<>();
		players.add(player);
		players.add(player2);
		List<DTOPlayerCD4> dtoPlayers = DTOPlayerCD4.convertAll(players);
		assertEquals(dtoPlayers.get(0).getClass(), DTOPlayerCD4.class);
		assertEquals(dtoPlayers.get(1).getClass(), DTOPlayerCD4.class);
		assertEquals("Gio", dtoPlayers.get(0).getName());
		assertEquals("Nick", dtoPlayers.get(1).getName());
		assertTrue(dtoPlayers.get(0).equals(dtoPlayer));
		assertTrue(dtoPlayers.get(1).equals(dtoPlayer2));
	}
	
	@Test
	public void testEquals() {
		//Compare a DTOPlayerCD4 to a PlayerCD4
		assertTrue(dtoPlayer.equals(player));
		//Compare a DTOPlayerCD4 to another DTOPlayerCD4
		assertFalse(dtoPlayer.equals(dtoPlayer2));
		assertTrue(dtoPlayer.equals(new DTOPlayerCD4(player)));
		//null
		assertFalse(dtoPlayer.equals(null));
		//Different types
		assertFalse(dtoPlayer.equals(new Assistant(10)));
	}
	
	@Test
	public void testHashCode() {
		assertTrue(dtoPlayer.hashCode() == dtoPlayer.hashCode());
		assertFalse(dtoPlayer.hashCode() == dtoPlayer2.hashCode());
	}
	
	@Test
	public void testGetName() {
		assertEquals("Gio", dtoPlayer.getName());
	}
	
	@Test
	public void testGetPocket() {
		assertEquals(DTOPocketCD4.class, dtoPlayer.getPocket().getClass());
		assertEquals(0, dtoPlayer.getPocket().getAssistants());
		assertEquals(1, dtoPlayer.getPocket().getCoins());
		assertEquals(5, dtoPlayer.getPocket().getNobilityRank());
		assertEquals(4, dtoPlayer.getPocket().getVictoryPoints());
	}
	
	@Test
	public void testGetHand() {
		assertEquals(0, dtoPlayer.getHand().size());
	}
	
	@Test
	public void testGetPeritsToBeUsed() {
		assertEquals(0, dtoPlayer.getPermitsToBeUsed().size());
	}
	
	@Test
	public void testGetUsedPermits() {
		assertEquals(0, dtoPlayer.getUsedPermits().size());
	}
	
	@Test
	public void testGetPlayerColor() {
		assertEquals(HSBColor.class, dtoPlayer.getPlayerColor().getClass());
		assertEquals(HSBColor.getNDifferent(1).get(0), dtoPlayer.getPlayerColor());
	}
	
	@After
	public void teardown() {
		model.getLogger().close();
	}
	
}
