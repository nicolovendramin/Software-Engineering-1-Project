package it.polimi.ingsw.cg25.dashboard;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.model.Coin;
import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.dashboard.Councelor;
import it.polimi.ingsw.cg25.model.dashboard.Party;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("unused")
public class CouncelorTest {

	private Party p;
	private Party p2;
	
	private HSBColor color;
	private HSBColor color2;
	
	@Before
	public void setUp() {
		color = new HSBColor(250, 90, 80, "color");
		color2 = new HSBColor(140, 30, 40, "color2");
		
		p = new Party(color, false);
		p2 = new Party(color2, false);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ShouldNotBePossibleToCreateACouncelorWithoutAParty() {
		Councelor coun = new Councelor(null);
	}
	
	@Test
	public void testGetParty() {
		Councelor coun = new Councelor(p);
		assertTrue(p.sameParty(coun.getParty()));
	}
	
	/**
	 * True if two councelors are in the same party
	 */
	@Test
	public void testEquals() {
		Councelor coun = new Councelor(p);
		Councelor coun2 = new Councelor(p2);
		Councelor coun3 = new Councelor(p);
		
		assertTrue(coun.equals(coun3));
		assertFalse(coun.equals(coun2));
		//null
		assertFalse(coun.equals(null));
		//Different classes
		assertFalse(coun2.equals(new Coin(4)));
	}
	
	@Test
	public void testHashCode() {
		Councelor coun = new Councelor(p);
		Councelor coun2 = new Councelor(p2);
		
		assertFalse(coun.hashCode() == coun2.hashCode());
		assertTrue(coun.hashCode() == coun.hashCode());
	}
	
}
