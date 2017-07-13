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
import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.dashboard.Councelor;
import it.polimi.ingsw.cg25.model.dashboard.Party;
import it.polimi.ingsw.cg25.model.dashboard.bonus.DrawPoliticsCardBonus;
import it.polimi.ingsw.cg25.model.dto.DTOCouncelor;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("unused")
public class DTOCouncelorTest {

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
	public void ShouldNotBePossibleToCreateADTOCouncelorWithoutAValidCouncelor() {
		DTOCouncelor c = new DTOCouncelor(null);
	}
	
	@Test
	public void testGetColor() {
		Councelor normCounc = model.getBoard().getUnemployedCouncelors().get(0);
		DTOCouncelor dtoCounc = new DTOCouncelor(normCounc);
		assertEquals(HSBColor.class, dtoCounc.getColor().getClass());
		assertTrue(dtoCounc.getColor().equals(normCounc.getParty().getColor()));
	}
	
	@Test
	public void testToString() {
		Councelor normCounc = model.getBoard().getUnemployedCouncelors().get(1);
		DTOCouncelor dtoCounc = new DTOCouncelor(normCounc);
		assertEquals("Councelor: Party = " + dtoCounc.getColor().printTag(), dtoCounc.toString());
	}
	
	@Test
	public void testConvertAllQueue() {
		//Test converted councelors
		//To access the queue of councelors... (Sea)
		LinkedList<Councelor> seaBalcony = (LinkedList<Councelor>) model.getBoard().getRegions().get(0).getCouncil().getCouncelors();
		List<DTOCouncelor> converted = DTOCouncelor.convertAll(model.getBoard().getRegions().get(0).getCouncil().getCouncelors());
		assertEquals(seaBalcony.size(), converted.size());
		for(int i = 0; i < converted.size(); i++)
			assertTrue(seaBalcony.get(i).getParty().getColor().equals(converted.get(i).getColor()));
	}
	
	@Test
	public void testConvertAllList() {
		//Test converted councelors
		List<Councelor> unemployed = model.getBoard().getUnemployedCouncelors();
		List<DTOCouncelor> converted = DTOCouncelor.convertAll(model.getBoard().getUnemployedCouncelors());
		assertEquals(unemployed.size(), converted.size());
		for(int i = 0; i < converted.size(); i++)
			assertTrue(unemployed.get(i).getParty().getColor().equals(converted.get(i).getColor()));
	}
	
	@Test
	public void testHashCode() {
		//Ensure the color of the two councelors is different
		DTOCouncelor counc1 = new DTOCouncelor(new Councelor(new Party(HSBColor.getNDifferent(2).get(0), false)));
		DTOCouncelor counc2 = new DTOCouncelor(new Councelor(new Party(HSBColor.getNDifferent(2).get(1), false)));
		
		assertFalse(counc1.hashCode() == counc2.hashCode());
		assertTrue(counc1.hashCode() == counc1.hashCode());
	}
	
	@Test
	public void testEquals() {
		//Ensure the color of the two councelors is different
		Councelor normCounc1 = new Councelor(new Party(HSBColor.getNDifferent(2).get(0), false));
		Councelor normCounc2 = new Councelor(new Party(HSBColor.getNDifferent(2).get(1), false));
		DTOCouncelor dtoCounc1 = new DTOCouncelor(normCounc1);
		DTOCouncelor dtoCounc2 = new DTOCouncelor(normCounc2);
		//Same color of normCounc1
		DTOCouncelor dtoCounc3 = new DTOCouncelor(new Councelor(new Party(HSBColor.getNDifferent(2).get(0), false)));
		//Null
		assertFalse(dtoCounc1.equals(null));
		//Two dto councelors
		assertFalse(dtoCounc1.equals(dtoCounc2));
		assertTrue(dtoCounc1.equals(dtoCounc3));
		//DTO councelor and normal councelor
		assertFalse(dtoCounc1.equals(normCounc2));
		assertTrue(dtoCounc1.equals(normCounc1));
		//Different class
		assertFalse(dtoCounc1.equals(new DrawPoliticsCardBonus(10)));
	}
	
	@Test
	public void testDecode() throws ElementNotFoundException {
		Councelor normCounc1 = new Councelor(new Party(HSBColor.getNDifferent(2).get(0), false));
		Councelor normCounc2 = new Councelor(new Party(HSBColor.getNDifferent(2).get(1), false));
		Councelor normCounc3 = new Councelor(new Party(HSBColor.getNDifferent(2).get(0), false));
		DTOCouncelor dtoCounc1 = new DTOCouncelor(normCounc1);
		DTOCouncelor dtoCounc2 = new DTOCouncelor(normCounc2);
		DTOCouncelor dtoCounc3 = new DTOCouncelor(normCounc3);
		
		List<Councelor> candidates = new ArrayList<>();
		candidates.add(normCounc1);
		candidates.add(normCounc2);
		candidates.add(normCounc3);
		//Compare the party of normCounc1 to the party of the decoded councelor
		Councelor decoded = dtoCounc1.decode(candidates);
		assertTrue(decoded.getParty().sameParty(normCounc1.getParty()));
		decoded = dtoCounc2.decode(candidates);
		assertTrue(decoded.getParty().sameParty(normCounc2.getParty()));
	}
	
	@Test(expected=ElementNotFoundException.class)
	public void testDecodeWithElementNotFoundException() throws ElementNotFoundException {
		Councelor normCounc1 = new Councelor(new Party(HSBColor.getNDifferent(3).get(0), false));
		Councelor normCounc2 = new Councelor(new Party(HSBColor.getNDifferent(3).get(1), false));
		Councelor normCounc3 = new Councelor(new Party(HSBColor.getNDifferent(3).get(0), false));
		Councelor normCounc4 = new Councelor(new Party(HSBColor.getNDifferent(3).get(2), false));
		DTOCouncelor dtoCounc = new DTOCouncelor(normCounc4);
		
		List<Councelor> candidates = new ArrayList<>();
		candidates.add(normCounc1);
		candidates.add(normCounc2);
		candidates.add(normCounc3);
		candidates.add(normCounc3);
		
		//This throws the exception
		dtoCounc.decode(candidates);
	}
	
	@After
	public void teardown() {
		model.getLogger().close();
	}
	
}
