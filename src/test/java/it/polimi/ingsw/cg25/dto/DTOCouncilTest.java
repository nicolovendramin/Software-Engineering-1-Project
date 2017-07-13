package it.polimi.ingsw.cg25.dto;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.exceptions.CannotCreateGameException;
import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.model.Assistant;
import it.polimi.ingsw.cg25.model.Coin;
import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.NobilityRank;
import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.PocketCD4;
import it.polimi.ingsw.cg25.model.VictoryPoint;
import it.polimi.ingsw.cg25.model.dashboard.Councelor;
import it.polimi.ingsw.cg25.model.dashboard.Council;
import it.polimi.ingsw.cg25.model.dashboard.bonus.NobilityPointBonus;
import it.polimi.ingsw.cg25.model.dto.DTOCouncelor;
import it.polimi.ingsw.cg25.model.dto.DTOCouncil;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("unused")
public class DTOCouncilTest {

	private MatchCD4 model;
	private ModelProxy proxy;
	private BoardFactory factory;
	
	private PlayerCD4 player;
	private PocketCD4 pocket;
	
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
		//Init pocket
		pocket = new PocketCD4(new Coin(10), new Assistant(0), new NobilityRank(5), new VictoryPoint(4));
		player = new PlayerCD4(1, "Gio", HSBColor.getNDifferent(1).get(0), model, pocket);
	}
	
	@Test(expected=NullPointerException.class)
	public void testDTOCouncilNullPointerException() {
		DTOCouncil c = new DTOCouncil(null);
	}
	
	@Test
	public void testGetLocation() {
		DTOCouncil dtoc1 = new DTOCouncil(model.getBoard().getKingCouncil());
		assertEquals("King's Council", dtoc1.getLocation());
		DTOCouncil dtoc2 = new DTOCouncil(model.getBoard().getRegions().get(0).getCouncil());
		assertEquals("Sea", dtoc2.getLocation());
	}
	
	@Test
	public void testGetCouncelors() {
		DTOCouncil dtoc1 = new DTOCouncil(model.getBoard().getKingCouncil());
		assertEquals(4, dtoc1.getCouncelors().size());
		dtoc1.getCouncelors().forEach(coun -> assertEquals(DTOCouncelor.class, coun.getClass()));
		//To access the queue of councelors...
		LinkedList<Councelor> balcony1 = (LinkedList<Councelor>) model.getBoard().getKingCouncil().getCouncelors();
		//Check colors
		for(int i = 0; i < 4; i++)
			assertTrue(dtoc1.getCouncelors().get(i).getColor().equals(balcony1.get(i).getParty().getColor()));
		
		DTOCouncil dtoc2 = new DTOCouncil(model.getBoard().getRegions().get(2).getCouncil());
		assertEquals(4, dtoc2.getCouncelors().size());
		dtoc2.getCouncelors().forEach(coun -> assertEquals(DTOCouncelor.class, coun.getClass()));
		//To access the queue of councelors...
		LinkedList<Councelor> balcony2 = (LinkedList<Councelor>) model.getBoard().getRegions().get(2).getCouncil().getCouncelors();
		//Check colors
		for(int i = 0; i < 4; i++)
			assertTrue(dtoc2.getCouncelors().get(i).getColor().equals(balcony2.get(i).getParty().getColor()));
	}
	
	@Test
	public void testConvertAll() {
		List<Council> councils = new ArrayList<>();
		//Try to convert Sea and Hill councils
		for(int i = 0; i < 2; i++)
			councils.add(model.getBoard().getRegions().get(i).getCouncil());
		
		List<DTOCouncil> dtoCouncils = DTOCouncil.convertAll(councils);
		assertEquals("Sea", dtoCouncils.get(0).getLocation());
		assertEquals("Hill", dtoCouncils.get(1).getLocation());
		
		//Test councelors color
		//To access the queue of councelors... (Sea)
		LinkedList<Councelor> seaBalcony = (LinkedList<Councelor>) model.getBoard().getRegions().get(0).getCouncil().getCouncelors();
		//Check colors
		for(int i = 0; i < 4; i++)
			assertTrue(dtoCouncils.get(0).getCouncelors().get(i).getColor().equals(seaBalcony.get(i).getParty().getColor()));
		//To access the queue of councelors... (Hill)
		LinkedList<Councelor> hillBalcony = (LinkedList<Councelor>) model.getBoard().getRegions().get(1).getCouncil().getCouncelors();
		//Check colors
		for(int i = 0; i < 4; i++)
			assertTrue(dtoCouncils.get(1).getCouncelors().get(i).getColor().equals(hillBalcony.get(i).getParty().getColor()));
	}
	
	@Test
	public void testHashCode() {
		DTOCouncil dtoc1 = new DTOCouncil(model.getBoard().getKingCouncil());
		DTOCouncil dtoc2 = new DTOCouncil(model.getBoard().getRegions().get(2).getCouncil());
		
		assertFalse(dtoc1.hashCode() == dtoc2.hashCode());
		assertTrue(dtoc1.hashCode() == dtoc1.hashCode());
	}
	
	@Test
	public void testEquals() {
		DTOCouncil dtoc1 = new DTOCouncil(model.getBoard().getKingCouncil());
		DTOCouncil dtoc2 = new DTOCouncil(model.getBoard().getRegions().get(1).getCouncil());
		DTOCouncil dtoc3 = new DTOCouncil(model.getBoard().getRegions().get(2).getCouncil());
		
		//Null
		assertFalse(dtoc1.equals(null));
		//DTOCouncil and Council
		assertTrue(dtoc1.equals(model.getBoard().getKingCouncil()));
		assertFalse(dtoc2.equals(model.getBoard().getRegions().get(2).getCouncil()));
		//Two DTOCouncil objects
		assertTrue(dtoc1.equals(dtoc1));
		assertFalse(dtoc2.equals(dtoc3));
		//Different kinds of objects
		assertFalse(dtoc1.equals(new NobilityPointBonus(9)));
	}
	
	@Test
	public void testDecode() throws ElementNotFoundException {
		List<Council> councils = new ArrayList<>();
		//Sea, Hill and Mountain councils
		for(int i = 0; i < 3; i++)
			councils.add(model.getBoard().getRegions().get(i).getCouncil());
		councils.add(model.getBoard().getKingCouncil());
		//Hill council to be decoded
		DTOCouncil toDecode = new DTOCouncil(model.getBoard().getRegions().get(1).getCouncil());
		
		Council decoded = toDecode.decode(councils);
		assertTrue(decoded.equals(councils.get(1)));
		assertTrue(toDecode.equals(decoded));
		
		toDecode = new DTOCouncil(model.getBoard().getKingCouncil());
		decoded = toDecode.decode(councils);
		assertTrue(decoded.equals(councils.get(3)));
		assertTrue(toDecode.equals(decoded));
	}
	
	@Test(expected=ElementNotFoundException.class)
	public void testDecodeElementNotFoundException() throws ElementNotFoundException {
		//councils contains everything but king's council
		List<Council> councils = new ArrayList<>();
		//Sea, Hill and Mountain councils
		for(int i = 0; i < 3; i++)
			councils.add(model.getBoard().getRegions().get(i).getCouncil());
		
		DTOCouncil toDecode = new DTOCouncil(model.getBoard().getKingCouncil());
		//This triggers the exception
		toDecode.decode(councils);
	}
	
	@Test
	public void testToStringIsNotNull() {
		DTOCouncil converted = new DTOCouncil(model.getBoard().getKingCouncil());
		assertFalse(converted.toString() == null);
	}
	
	@Test
	public void testToStringIsNotEmpty() {
		DTOCouncil converted = new DTOCouncil(model.getBoard().getRegions().get(1).getCouncil());
		assertFalse(converted.toString().equals(""));
	}
	
	@After
	public void teardown() {
		model.getLogger().close();
	}
	
}
