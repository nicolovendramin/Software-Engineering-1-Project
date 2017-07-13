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
import it.polimi.ingsw.cg25.model.dashboard.Emporium;
import it.polimi.ingsw.cg25.model.dto.DTOEmporium;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

@SuppressWarnings("unused")
public class DTOEmporiumTest {
	
	private MatchCD4 model;
	private ModelProxy proxy;
	private BoardFactory factory;
	
	private PlayerCD4 player;
	private PocketCD4 pocket;
	private PlayerCD4 player2;
	private PocketCD4 pocket2;

	private Emporium emp1;
	private Emporium emp2;
	private Emporium emp3;
	private Emporium emp4;
	
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
		pocket = new PocketCD4(new Coin(10), new Assistant(0), new NobilityRank(5), new VictoryPoint(4));
		player = new PlayerCD4(1, "Gio", HSBColor.getNDifferent(2).get(0), model, pocket);
		
		pocket2 = new PocketCD4(new Coin(5), new Assistant(0), new NobilityRank(1), new VictoryPoint(10));
		player2 = new PlayerCD4(1, "Nick", HSBColor.getNDifferent(2).get(1), model, pocket);
		
		emp1 = new Emporium(player, model.getBoard().getCities().get(0));
		emp2 = new Emporium(player, model.getBoard().getCities().get(1));
		emp3 = new Emporium(player2, model.getBoard().getCities().get(1));
		emp4 = new Emporium(player2, model.getBoard().getCities().get(0));
	}
	
	@Test(expected=NullPointerException.class)
	public void testDTOEmporiumNullPointerException() {
		DTOEmporium empdto = new DTOEmporium(null);
	}

	@Test
	public void testHashCode() {
		DTOEmporium empdto1 = new DTOEmporium(emp1);
		DTOEmporium empdto2 = new DTOEmporium(emp2);
		DTOEmporium empdto3 = new DTOEmporium(emp3);
		
		assertFalse(empdto1.hashCode() == empdto3.hashCode());
		assertTrue(empdto1.hashCode() == empdto2.hashCode());
	}
	
	@Test
	public void testEquals() {
		DTOEmporium empdto1 = new DTOEmporium(emp1);
		DTOEmporium empdto2 = new DTOEmporium(emp2);
		DTOEmporium empdto3 = new DTOEmporium(emp3);
		DTOEmporium empdto4 = new DTOEmporium(emp4);
		//Null
		assertFalse(empdto1.equals(null));
		//Two DTO emporiums
		assertTrue(empdto1.equals(empdto2));
		assertFalse(empdto1.equals(empdto3));
		//DTO emporium and normal emporium
		assertTrue(empdto1.equals(emp1));
		assertTrue(empdto4.equals(emp4));
		assertFalse(empdto4.equals(emp2));
		//Other kind of object
		assertFalse(empdto1.equals(new Coin(10)));
	}
	
	@Test
	public void testGetColor() {
		DTOEmporium empdto1 = new DTOEmporium(emp1);
		DTOEmporium empdto3 = new DTOEmporium(emp3);
		
		HSBColor color = HSBColor.getNDifferent(2).get(0);
		assertEquals(HSBColor.class, empdto1.getColor().getClass());
		assertTrue(empdto1.getColor().equals(color));
		assertFalse(empdto3.getColor().equals(empdto1.getColor()));
	}
	
	@Test
	public void testConvertAll() {
		List<Emporium> emps = new ArrayList<>();
		emps.add(emp1);
		emps.add(emp2);
		emps.add(emp3);
		emps.add(emp4);
		List<DTOEmporium> dtoEmporiums = DTOEmporium.convertAll(emps);
		
		assertEquals(4, dtoEmporiums.size());
		assertTrue(dtoEmporiums.get(0).getColor().equals(player.getColor()));
		assertTrue(dtoEmporiums.get(1).getColor().equals(player.getColor()));
		assertTrue(dtoEmporiums.get(2).getColor().equals(player2.getColor()));
		assertTrue(dtoEmporiums.get(3).getColor().equals(player2.getColor()));
	}
	
	@After
	public void teardown() {
		model.getLogger().close();
	}
	
}
