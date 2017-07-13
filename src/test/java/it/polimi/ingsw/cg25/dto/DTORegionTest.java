package it.polimi.ingsw.cg25.dto;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.exceptions.CannotCreateGameException;
import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.dashboard.bonus.NobilityPointBonus;
import it.polimi.ingsw.cg25.model.dashboard.topological.Region;
import it.polimi.ingsw.cg25.model.dto.DTORegion;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

public class DTORegionTest {

	private MatchCD4 model;
	private ModelProxy proxy;
	private BoardFactory factory;
	
	@Before
	public void setUp() throws FileNotFoundException, CannotCreateGameException {
		factory = new BoardFactory(new FileReader("src/test/resources/nobilityCellsFULL.txt"),
				new FileReader("src/test/resources/politicsFULL.txt"), 
				new FileReader("src/test/resources/citiesFULL.txt"),
				new FileReader("src/test/resources/graphFULL.txt"), 
				new FileReader("src/test/resources/kingFULL.txt"),
				new FileReader("src/test/resources/regionsFULL.txt"));
		this.proxy = new ModelProxy();
		this.model = new MatchCD4(factory.getBoard(), this.proxy,false,10);
	}
	
	@Test(expected=NullPointerException.class)
	public void testDTORegionNullPointerException() {
		new DTORegion(null);
	}
	
	@Test
	public void testHashCode() {
		Region r1 = model.getBoard().getRegions().get(0);
		Region r2 = model.getBoard().getRegions().get(1);
		DTORegion dtor1 = new DTORegion(r1);
		DTORegion dtor2 = new DTORegion(r2);
		
		assertFalse(dtor1.hashCode() == dtor2.hashCode());
		assertTrue(dtor1.hashCode() == dtor1.hashCode());
	}
	
	@Test
	public void testEquals() {
		Region r1 = model.getBoard().getRegions().get(0);
		Region r2 = model.getBoard().getRegions().get(1);
		DTORegion dtor1 = new DTORegion(r1);
		DTORegion dtor2 = new DTORegion(r2);
		//Null
		assertFalse(dtor1.equals(null));
		//DTORegion and normal region
		assertFalse(dtor1.equals(r2));
		assertTrue(dtor1.equals(r1));
		//Two DTORegion objects
		assertFalse(dtor1.equals(dtor2));
		assertTrue(dtor1.equals(dtor1));
		//Different types of object
		assertFalse(dtor1.equals(new NobilityPointBonus(10)));
	}
	
	@Test
	public void testConvertAll() {
		List<DTORegion> converted = DTORegion.convertAll(model.getBoard().getRegions());
		for(int i = 0; i < 3; i++)
			assertTrue(converted.get(i).equals(model.getBoard().getRegions().get(i)));
	}
	
	@Test
	public void testDecode() throws ElementNotFoundException {
		List<DTORegion> converted = DTORegion.convertAll(model.getBoard().getRegions());
		Region decoded = converted.get(1).decode(model.getBoard().getRegions());
		assertTrue(converted.get(1).equals(decoded));
	}
	
	@Test(expected=ElementNotFoundException.class)
	public void testCantDecodeADTORegion() throws ElementNotFoundException {
		List<DTORegion> converted = DTORegion.convertAll(model.getBoard().getRegions());
		model.getBoard().getRegions().remove(1);
		//Try to decode region in position 1
		converted.get(1).decode(model.getBoard().getRegions());
	}
	
	@Test
	public void testGetName() {
		DTORegion converted = new DTORegion(model.getBoard().getRegions().get(0));
		assertEquals("Sea", converted.getName());
	}
	
	@Test
	public void testGetCities() {
		DTORegion converted = new DTORegion(model.getBoard().getRegions().get(0));
		assertTrue(converted.getCities().equals(model.getBoard().getRegions().get(0).getCities()));
	}
	
	@Test
	public void testGetCouncil() {
		DTORegion converted = new DTORegion(model.getBoard().getRegions().get(0));
		assertTrue(converted.getCouncil().equals(model.getBoard().getRegions().get(0).getCouncil()));
	}
	
	@Test
	public void testGetFaceUpPermits() {
		DTORegion converted = new DTORegion(model.getBoard().getRegions().get(0));
		assertTrue(converted.getFaceUpPermits().equals(model.getBoard().getRegions().get(0).getFaceUpPermits()));
	}
	
	@Test
	public void testGetBonusCard() {
		DTORegion converted = new DTORegion(model.getBoard().getRegions().get(0));
		assertTrue(converted.getBonusCard().equals(model.getBoard().getRegions().get(0).getBonusCard()));
	}
	
	@Test
	public void testToStringIsNotNull() {
		DTORegion converted = new DTORegion(model.getBoard().getRegions().get(0));
		assertFalse(converted.toString() == null);
	}
	
	@Test
	public void testToStringIsNotEmpty() {
		DTORegion converted = new DTORegion(model.getBoard().getRegions().get(0));
		assertFalse(converted.toString().equals(""));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConvertAllWithNullListOfRegions() {
		DTORegion.convertAll(null);
	}
	
	@After
	public void teardown() {
		model.getLogger().close();
	}
	
}
