package it.polimi.ingsw.cg25.dashboard;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.dashboard.Party;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("unused")
public class PartyTest {

	private HSBColor color;
	private HSBColor color2;
	private HSBColor color3;
	
	@Before
	public void setUp() {
		color = new HSBColor(100, 50, 30, "Colore di prova");
		color2 = new HSBColor(200, 50, 30, "Colore di prova 2");
		color3 = new HSBColor(300, 50, 30, "Colore di prova 3");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ShouldNotBePossibleToCreateAPartyWithoutAColor() {
		Party party = new Party(null, true);
	}
	
	@Test
	public void testSameParty() {
		Party p1 = new Party(color, true);
		Party p2 = new Party(color2, false);
		Party p3 = new Party(color3, false);
		Party p4 = new Party(color2, false);
		
		//p1 is Jolly
		assertTrue(p1.sameParty(p2));
		assertTrue(p1.sameParty(p3));
		//p2 is different from p3
		assertFalse(p2.sameParty(p3));
		//p4 has the same HSBColor of p2
		assertTrue(p2.sameParty(p4));
	}
	
	@Test
	public void testGetColor() {
		Party p1 = new Party(color, true);
		assertEquals(color, p1.getColor());
		
		Party p2 = new Party(color2, false);
		assertEquals(color2, p2.getColor());
	}
	
	
	@Test
	public void testGetIsJolly() {
		Party p1 = new Party(color, true);
		assertTrue(p1.getIsJolly());
		
		Party p2 = new Party(color2, false);
		assertFalse(p2.getIsJolly());
	}
	
}
