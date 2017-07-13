/**
 * 
 */
package it.polimi.ingsw.cg25.dto;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.dashboard.bonus.AssistantBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.CoinBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.MainActionBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.NobilityPointBonus;
import it.polimi.ingsw.cg25.model.dashboard.cards.PermitCard;
import it.polimi.ingsw.cg25.model.dashboard.topological.City;
import it.polimi.ingsw.cg25.model.dashboard.topological.CityColor;
import it.polimi.ingsw.cg25.model.dto.DTOCity;
import it.polimi.ingsw.cg25.model.dto.DTOPermitCard;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOBonus;

/**
 * @author Davide
 *
 */
public class DTOPermitCardTest {
	static List<PermitCard> permits;
	List<DTOPermitCard> converted;
	static List<PermitCard> junk;
	static List<HSBColor> colors;
	static List<CityColor> cityColors;
	static List<City> cities;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		permits = new ArrayList<>();
		junk = new ArrayList<>();

		colors = HSBColor.getNDifferent(5);
		cities = new ArrayList<>();
		cityColors = new ArrayList<>();
		for (HSBColor c : colors)
			cityColors.add(new CityColor(c, Arrays.asList(new CoinBonus(1))));

		cities.add(new City("Milan", cityColors.get(0), Arrays.asList(new CoinBonus(11))));
		cities.add(new City("Rome", cityColors.get(1), Arrays.asList(new AssistantBonus(10), new MainActionBonus(1))));
		cities.add(new City("Venice", cityColors.get(0), Arrays.asList(new CoinBonus(11))));
		cities.add(new City("London", cityColors.get(2), Arrays.asList(new CoinBonus(12))));

		permits.add(
				new PermitCard(Arrays.asList(cities.get(0), cities.get(1)), Arrays.asList(new NobilityPointBonus(5))));
		permits.add(
				new PermitCard(Arrays.asList(cities.get(0), cities.get(2)), Arrays.asList(new NobilityPointBonus(6))));
		permits.add(new PermitCard(Arrays.asList(cities.get(1)), Arrays.asList(new CoinBonus(5))));

		junk.add(new PermitCard(Arrays.asList(cities.get(2), cities.get(1)), Arrays.asList(new CoinBonus(5))));
		junk.add(new PermitCard(Arrays.asList(cities.get(0), cities.get(2)), Arrays.asList(new CoinBonus(5))));
		junk.add(new PermitCard(Arrays.asList(cities.get(3), cities.get(0)), Arrays.asList(new NobilityPointBonus(5))));
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPermitCard#decodeAll(java.util.List, java.util.List)}
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPermitCard#convertAll(java.util.List)}
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPermitCard#decode(java.util.List)}
	 * ..
	 */
	@Test
	public void testEncodingDecoding() {
		List<PermitCard> decoded = new ArrayList<>();
		converted = DTOPermitCard.convertAll(permits);
		List<PermitCard> candidates = new ArrayList<>();
		candidates.addAll(permits);
		candidates.addAll(junk);
		Collections.shuffle(candidates);
		try {
			decoded = DTOPermitCard.decodeAll(converted, candidates);
		} catch (ElementNotFoundException e) {
			fail("DTOPermit: decoding failed, element not found");
		}
		// Exactly same reference
		for (int i = 0; i < decoded.size(); i++)
			assertTrue("DTOPermit: decoding failed", permits.get(i) == decoded.get(i));
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPermitCard#decodeAll(java.util.List, java.util.List)}
	 * 
	 * @throws ElementNotFoundException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNullParameterDecoding() throws ElementNotFoundException {
		DTOPermitCard.decodeAll(null, new ArrayList<PermitCard>());
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPermitCard#decodeAll(java.util.List, java.util.List)}
	 * 
	 * @throws ElementNotFoundException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNullParameterDecoding2() throws ElementNotFoundException {
		DTOPermitCard.decodeAll(new ArrayList<DTOPermitCard>(), null);
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPermitCard#decodeAll(java.util.List, java.util.List)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNullParameterEncoding() {
		DTOPermitCard.convertAll(null);
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPermitCard#decodeAll(java.util.List, java.util.List)}
	 * .
	 */
	@Test
	public void testEmptyParameterEncoding() {
		assertTrue(DTOPermitCard.convertAll(new ArrayList<PermitCard>()).isEmpty());
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPermitCard#decode(java.util.List)}
	 * .
	 * 
	 * @throws ElementNotFoundException
	 */
	@Test(expected = ElementNotFoundException.class)
	public void testElementNotFoundDecode() throws ElementNotFoundException {
		DTOPermitCard dto = new DTOPermitCard(permits.get(0));
		dto.decode(junk);
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPermitCard#getBonuses()}.
	 */
	@Test
	public void testGetBonuses() {
		for (PermitCard perm : permits) {
			List<DTOBonus> dtoB = DTOBonus.convertAll(perm.getBonuses());
			DTOPermitCard dtoP = new DTOPermitCard(perm);
			assertTrue("DTOPermit: getBonuses() returned different bonuses", dtoB.equals(dtoP.getBonuses()));
		}
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPermitCard#getCities()}.
	 */
	@Test
	public void testGetCities() {
		for (PermitCard perm : permits) {
			List<DTOCity> dtoC = DTOCity.convertAll(perm.getValidCities());
			DTOPermitCard dtoP = new DTOPermitCard(perm);
			assertTrue("DTOPermit: getCities() returned different cities", dtoC.equals(dtoP.getCities()));
		}
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPermitCard#equals(java.lang.Object)}
	 * .
	 */
	@Test
	public void testEqualsWithDTOSameCitiesButDifferentBonuses() {
		PermitCard p = new PermitCard(Arrays.asList(cities.get(0), cities.get(2)),
				Arrays.asList(new NobilityPointBonus(6)));
		DTOPermitCard dto = new DTOPermitCard(p);

		assertFalse("DTOPermit: equals with DTO error", dto.equals(new DTOPermitCard(
				new PermitCard(Arrays.asList(cities.get(0), cities.get(2)), Arrays.asList(new CoinBonus(5))))));

		assertTrue("DTOPermit: equals with DTO error", dto.equals(new DTOPermitCard(p)));
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPermitCard#equals(java.lang.Object)}
	 * .
	 */
	@Test
	public void testEqualsWithDTOSameBunosesButDifferentCities() {
		PermitCard p = new PermitCard(Arrays.asList(cities.get(0), cities.get(1)),
				Arrays.asList(new NobilityPointBonus(5)));
		DTOPermitCard dto = new DTOPermitCard(p);

		assertFalse("DTOPermit: equals with DTO error", dto.equals(new DTOPermitCard(
				new PermitCard(Arrays.asList(cities.get(3), cities.get(0)), Arrays.asList(new NobilityPointBonus(5))))));

		assertTrue("DTOPermit: equals with DTO error", dto.equals(new DTOPermitCard(p)));
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPoliticsCard#equals(java.lang.Object)}
	 * .
	 */
	@Test
	public void testEqualsWithPermit() {
		DTOPermitCard dto = new DTOPermitCard(permits.get(1));
		for (PermitCard p : junk)
			assertFalse("DTOPermitCard: equals with Permit error " + p.toString(), dto.equals(p));
		assertTrue("DTOPermitCard: equals with Permit error", dto.equals(permits.get(1)));
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPermitCard#equals(java.lang.Object)}
	 * .
	 */
	@Test
	public void testEqualsWithNull() {
		DTOPermitCard dto = new DTOPermitCard(permits.get(0));
		assertFalse("DTOPermitCard: equals with null failed", dto.equals(null));
	}

	/**
	 * Test method for
	 * {@link it.polimi.ingsw.cg25.model.dto.DTOPermitCard#equals(java.lang.Object)}
	 * .
	 */
	@Test
	public void testEqualsWithJunkStuff() {
		DTOPermitCard dto = new DTOPermitCard(permits.get(0));
		assertFalse("DTOPermitCard: equals with junk stuff", dto.equals("JunkString!!!1!"));
	}

}
