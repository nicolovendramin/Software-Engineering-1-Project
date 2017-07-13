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
import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.exceptions.OneEmporiumOnlyException;
import it.polimi.ingsw.cg25.model.Assistant;
import it.polimi.ingsw.cg25.model.Coin;
import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.NobilityRank;
import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.PocketCD4;
import it.polimi.ingsw.cg25.model.VictoryPoint;
import it.polimi.ingsw.cg25.model.dashboard.topological.City;
import it.polimi.ingsw.cg25.model.dto.DTOCity;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("unused")
public class DTOCityTest {

	private MatchCD4 model;
	private ModelProxy proxy;
	private BoardFactory factory;
	
	private PlayerCD4 player;
	private PocketCD4 pocket;
	
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
		pocket = new PocketCD4(new Coin(10), new Assistant(0), new NobilityRank(5), new VictoryPoint(4));
		player = new PlayerCD4(1, "Gio", HSBColor.getNDifferent(1).get(0), model, pocket);
	}
	
	@Test(expected=NullPointerException.class)
	public void ShouldNotBePossibleToCreateADTOCityWithoutAValidCity() {
		DTOCity c = new DTOCity(null);
	}
	
	@Test
	public void testGetColor() {
		DTOCity c = new DTOCity(model.getBoard().getCities().get(0));
		assertEquals(model.getBoard().getCities().get(0).getColor().getColor(), c.getColor().getCityColor());
	}
	
	@Test
	public void testGetName() {
		DTOCity c = new DTOCity(model.getBoard().getCities().get(0));
		assertEquals("Milano", c.getName());
	}
	
	@Test
	public void testGetBonuses() {
		DTOCity c = new DTOCity(model.getBoard().getCities().get(0));
		assertEquals(model.getBoard().getCities().get(0).getCityBonus().size(), c.getBonuses().size());
	}
	
	@Test
	public void testEquals() {
		//Milano
		DTOCity c = new DTOCity(model.getBoard().getCities().get(0));
		//Torino
		DTOCity c2 = new DTOCity(model.getBoard().getCities().get(1));
		//null
		assertFalse(c.equals(null));
		//Different types
		assertFalse(c.equals(new Coin(2)));
		//DTO and normal object
		assertFalse(c.equals(model.getBoard().getCities().get(1)));
		assertTrue(c.equals(model.getBoard().getCities().get(0)));
		//DTO and DTO
		assertFalse(c.equals(c2));
		assertTrue(c.equals(c));
	}
	
	@Test
	public void testHashCode() {
		//Milano
		DTOCity c = new DTOCity(model.getBoard().getCities().get(0));
		//Torino
		DTOCity c2 = new DTOCity(model.getBoard().getCities().get(1));
		
		assertFalse(c.hashCode() == c2.hashCode());
		assertTrue(c.hashCode() == c.hashCode());
	}
	
	@Test
	public void testGetEmporiums() throws OneEmporiumOnlyException {
		player.buildEmporium(model.getBoard().getCities().get(0));
		//Milano with one emporium
		DTOCity c = new DTOCity(model.getBoard().getCities().get(0));
		assertEquals(1, c.getEmporiums().size());
		//Check the emporium belongs to player
		assertTrue(c.getEmporiums().get(0).getColor().equals(player.getColor()));
	}
	
	@Test
	public void testConvertAll() {
		List<DTOCity> dtoCities = DTOCity.convertAll(model.getBoard().getCities());
		assertEquals(2, dtoCities.size());
		assertTrue("Milano".equals(dtoCities.get(0).getName()));
		assertTrue("Torino".equals(dtoCities.get(1).getName()));
	}
	
	@Test
	public void testDecode() throws ElementNotFoundException {
		List<DTOCity> dtoCities = DTOCity.convertAll(model.getBoard().getCities());
		//Decode Milano
		City decoded = dtoCities.get(0).decode(model.getBoard().getCities());
		assertEquals("Milano", decoded.getName());
	}
	
	@Test(expected=ElementNotFoundException.class)
	public void testDecodeWithException() throws ElementNotFoundException {
		List<DTOCity> dtoCities = DTOCity.convertAll(model.getBoard().getCities());
		//Decode Milano, but fails
		City decoded = dtoCities.get(0).decode(new ArrayList<City>());
	}
	
	@Test
	public void testDecodeAll() throws ElementNotFoundException {
		List<DTOCity> dtoCities = DTOCity.convertAll(model.getBoard().getCities());
		//Decode Milano
		List<City> decoded = DTOCity.decodeAll(dtoCities, model.getBoard().getCities());
		assertEquals("Milano", decoded.get(0).getName());
		assertEquals("Torino", decoded.get(1).getName());
		assertEquals(2, decoded.size());
		//Controlla che le città non vengano cancellate dalla decodeAll
		assertEquals(2, model.getBoard().getCities().size());
	}
	
	@Test(expected=ElementNotFoundException.class)
	public void testDecodeAllWithException() throws ElementNotFoundException {
		List<DTOCity> dtoCities = DTOCity.convertAll(model.getBoard().getCities());
		model.getBoard().getCities().remove(0);
		//Decode Milano, but fails because it's not a city anymore
		List<City> decoded = DTOCity.decodeAll(dtoCities, model.getBoard().getCities());
	}
	
	@After
	public void teardown() {
		model.getLogger().close();
	}
	
}
