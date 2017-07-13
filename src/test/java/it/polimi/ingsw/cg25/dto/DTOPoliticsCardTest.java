/**
 * 
 */
package it.polimi.ingsw.cg25.dto;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.dashboard.Party;
import it.polimi.ingsw.cg25.model.dashboard.cards.PoliticsCard;
import it.polimi.ingsw.cg25.model.dto.DTOPoliticsCard;

/**
 * @author Davide
 *
 */
public class DTOPoliticsCardTest {

	static List<PoliticsCard> politics;
	List<DTOPoliticsCard> converted;
	static List<PoliticsCard> junk;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		politics = new ArrayList<>();
		junk = new ArrayList<>();
		politics.add(new PoliticsCard(new Party(new HSBColor(new HSBColor(10, 82, 27), "color1"), true)));
		politics.add(new PoliticsCard(new Party(new HSBColor(new HSBColor(20, 10, 51), "color2"), false)));
		politics.add(new PoliticsCard(new Party(new HSBColor(new HSBColor(80, 80, 25), "color3"), false)));
		politics.add(new PoliticsCard(new Party(new HSBColor(new HSBColor(50, 50, 50), "color4"), true)));
		junk.add(new PoliticsCard(new Party(new HSBColor(new HSBColor(10, 82, 27), "color1"), false)));
		junk.add(new PoliticsCard(new Party(new HSBColor(new HSBColor(96, 10, 51), "color5"), false)));
		junk.add(new PoliticsCard(new Party(new HSBColor(new HSBColor(80, 20, 25), "color6"), false)));
		junk.add(new PoliticsCard(new Party(new HSBColor(new HSBColor(50, 50, 90), "color7"), true)));
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		converted = new ArrayList<>();
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPoliticsCard#decodeAll(java.util.List, java.util.List)}
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPoliticsCard#convertAll(java.util.List)}
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPoliticsCard#decode(java.util.List)}
	 * ..
	 */
	@Test
	public void testEncodingDecoding() {
		List<PoliticsCard> decoded = new ArrayList<>();
		converted = DTOPoliticsCard.convertAll(politics);
		List<PoliticsCard> candidates = new ArrayList<>();
		candidates.addAll(politics);
		candidates.addAll(junk);
		Collections.shuffle(candidates);
		try {
			decoded = DTOPoliticsCard.decodeAll(converted, candidates);
		} catch (ElementNotFoundException e) {
			fail("DTOPolitics: decoding failed, element not found");
		}
		// Exactly same reference
		for (int i = 0; i < decoded.size(); i++)
			assertTrue("DTOPolitics: decoding failed", politics.get(i) == decoded.get(i));
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPoliticsCard#decodeAll(java.util.List, java.util.List)}
	 * 
	 * @throws ElementNotFoundException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNullParameterDecoding() throws ElementNotFoundException {
		DTOPoliticsCard.decodeAll(null, new ArrayList<PoliticsCard>());
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPoliticsCard#decodeAll(java.util.List, java.util.List)}
	 * 
	 * @throws ElementNotFoundException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNullParameterDecoding2() throws ElementNotFoundException {
		DTOPoliticsCard.decodeAll(new ArrayList<DTOPoliticsCard>(), null);
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPoliticsCard#decodeAll(java.util.List, java.util.List)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNullParameterEncoding() {
		DTOPoliticsCard.convertAll(null);
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPoliticsCard#decodeAll(java.util.List, java.util.List)}
	 * .
	 */
	@Test
	public void testEmptyParameterEncoding() {
		assertTrue(DTOPoliticsCard.convertAll(new ArrayList<PoliticsCard>()).isEmpty());
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPoliticsCard#decode(java.util.List)}
	 * .
	 * 
	 * @throws ElementNotFoundException
	 */
	@Test(expected = ElementNotFoundException.class)
	public void testElementNotFoundDecode() throws ElementNotFoundException {
		DTOPoliticsCard dto = new DTOPoliticsCard(politics.get(0));
		dto.decode(junk);
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPoliticsCard#getColor()}.
	 */
	@Test
	public void testGetColor() {
		for (PoliticsCard p : politics)
			assertTrue(new DTOPoliticsCard(p).getColor().equals(p.getParty().getColor()));
		assertFalse(new DTOPoliticsCard(politics.get(1)).getColor().equals(junk.get(1).getParty().getColor()));
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPoliticsCard#isJolly()}.
	 */
	@Test
	public void testIsJolly() {
		for (PoliticsCard p : politics)
			assertTrue(new DTOPoliticsCard(p).isJolly() == p.getParty().getIsJolly());
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPoliticsCard#equals(java.lang.Object)}
	 * .
	 */
	@Test
	public void testEqualsWithDTOSameColorButDifferentJolly() {
		PoliticsCard pol = new PoliticsCard(new Party(new HSBColor(new HSBColor(10, 82, 27), "color1"), true));
		DTOPoliticsCard dto = new DTOPoliticsCard(pol);
		assertFalse("DTOPolitics: equals with DTO error (same color different jolly)",
				dto.equals(new DTOPoliticsCard(new PoliticsCard(new Party(new HSBColor(new HSBColor(10, 82, 27), "color1"), false)))));
		assertTrue("DTOPolitics: equals with DTO error", dto.equals(new DTOPoliticsCard(pol)));
	}
	
	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPoliticsCard#equals(java.lang.Object)}
	 * .
	 */
	@Test
	public void testEqualsWithDTOSameJollyButDifferentColor() {
		PoliticsCard pol = new PoliticsCard(new Party(new HSBColor(new HSBColor(10, 82, 27), "colorA"), true));
		DTOPoliticsCard dto = new DTOPoliticsCard(pol);
		assertFalse("DTOPolitics: equals with DTO error (same jolly different color)",
				dto.equals(new DTOPoliticsCard( new PoliticsCard(new Party(new HSBColor(new HSBColor(20, 40, 35), "colorB"), true)))));
		assertTrue("DTOPolitics: equals with DTO error", dto.equals(new DTOPoliticsCard(pol)));
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPoliticsCard#equals(java.lang.Object)}
	 * .
	 */
	@Test
	public void testEqualsWithPoliticsJolly() {
		DTOPoliticsCard dto = new DTOPoliticsCard(politics.get(0));
		for (PoliticsCard p : junk)
			assertFalse("DTOPolitics: equals with Politics error " + p.toString(), dto.equals(p));
		assertTrue("DTOPolitics: equals with Politics error", dto.equals(politics.get(0)));
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPoliticsCard#equals(java.lang.Object)}
	 * .
	 */
	@Test
	public void testEqualsWithPolitics() {
		DTOPoliticsCard dto = new DTOPoliticsCard(politics.get(1));
		for (PoliticsCard p : junk)
			assertFalse("DTOPolitics: equals with Politics error " + p.toString(), dto.equals(p));
		assertTrue("DTOPolitics: equals with Politics error", dto.equals(politics.get(1)));
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPoliticsCard#equals(java.lang.Object)}
	 * .
	 */
	@Test
	public void testEqualsWithNull() {
		DTOPoliticsCard dto = new DTOPoliticsCard(politics.get(0));
		assertFalse("DTOPolitics: equals with null failed", dto.equals(null));
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPoliticsCard#equals(java.lang.Object)}
	 * .
	 */
	@Test
	public void testEqualsWithJunkStuff() {
		DTOPoliticsCard dto = new DTOPoliticsCard(politics.get(0));
		assertFalse("DTOPolitics: equals with junk stuff", dto.equals("JunkString!!!1!"));
	}

}
