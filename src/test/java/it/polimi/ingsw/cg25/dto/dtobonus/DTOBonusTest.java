package it.polimi.ingsw.cg25.dto.dtobonus;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.model.Assistant;
import it.polimi.ingsw.cg25.model.dashboard.bonus.AssistantBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.CityBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.CoinBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.DrawPoliticsCardBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.MainActionBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.NobilityPointBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.PermitCardBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.ReusePermitBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.VictoryPointBonus;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOAssistantBonus;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOBonus;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOCityBonus;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOCoinBonus;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTODrawPoliticsCardBonus;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOMainActionBonus;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTONobilityPointBonus;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOPermitCardBonus;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOReusePermitBonus;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOVictoryPointBonus;
import junit.framework.TestCase;

/**
 * 
 * @author Giovanni
 *
 */
public class DTOBonusTest extends TestCase {

	private List<DTOBonus> DTObonus;
	private List<Bonus> bonus;
	
	@Before
	public void setUp() {
		bonus = new ArrayList<>();
		bonus.add(new CoinBonus(10));
		bonus.add(new AssistantBonus(9));
		bonus.add(new CityBonus(5));
		bonus.add(new DrawPoliticsCardBonus(2));
		bonus.add(new MainActionBonus(1));
		bonus.add(new NobilityPointBonus(2));
		bonus.add(new PermitCardBonus(4));
		bonus.add(new ReusePermitBonus(8));
		bonus.add(new VictoryPointBonus(9));
	}
	
	@Test
	public void testDTOBonusConversion() {
		DTObonus = DTOBonus.convertAll(bonus);
		//Size test
		assertEquals(9, DTObonus.size());
		//Quantity test
		assertEquals(10, DTObonus.get(0).getBnsQnty());
		assertEquals(9, DTObonus.get(1).getBnsQnty());
		assertEquals(5, DTObonus.get(2).getBnsQnty());
		assertEquals(2, DTObonus.get(3).getBnsQnty());
		assertEquals(1, DTObonus.get(4).getBnsQnty());
		assertEquals(2, DTObonus.get(5).getBnsQnty());
		assertEquals(4, DTObonus.get(6).getBnsQnty());
		assertEquals(8, DTObonus.get(7).getBnsQnty());
		assertEquals(9, DTObonus.get(8).getBnsQnty());
		//Dynamic type test
		assertEquals(DTOCoinBonus.class, DTObonus.get(0).getClass());
		assertEquals(DTOAssistantBonus.class, DTObonus.get(1).getClass());
		assertEquals(DTOCityBonus.class, DTObonus.get(2).getClass());
		assertEquals(DTODrawPoliticsCardBonus.class, DTObonus.get(3).getClass());
		assertEquals(DTOMainActionBonus.class, DTObonus.get(4).getClass());
		assertEquals(DTONobilityPointBonus.class, DTObonus.get(5).getClass());
		assertEquals(DTOPermitCardBonus.class, DTObonus.get(6).getClass());
		assertEquals(DTOReusePermitBonus.class, DTObonus.get(7).getClass());
		assertEquals(DTOVictoryPointBonus.class, DTObonus.get(8).getClass());
	}
	
	@Test
	public void testDTOBonusEquals() {
		DTObonus = DTOBonus.convertAll(bonus);
		//Create a bonus and the corresponding DTOBonus 
		Bonus normalBonus = new CoinBonus(5);
		DTOBonus dtoBonus = new DTOCoinBonus((CoinBonus)normalBonus);
		DTOBonus dtoBonus1 = new DTOCoinBonus((CoinBonus)normalBonus);
		//Equals tra un Bonus e un DTOBonus
		assertTrue(dtoBonus.equals(normalBonus));
		//Equals tra un DTOBonus e un altro
		assertTrue(dtoBonus.equals(dtoBonus1));
		//Equals on lists
		assertTrue(DTObonus.equals(bonus));
		//null
		assertFalse(dtoBonus.equals(null));
		//Different types
		assertFalse(dtoBonus1.equals(new Assistant(2)));
	}
	
	@Test
	public void testHasCode() {
		DTOBonus dtoBonus = new DTOCoinBonus(new CoinBonus(2));
		DTOBonus dtoBonus1 = new DTOAssistantBonus(new AssistantBonus(4));
		
		assertTrue(dtoBonus.hashCode() == dtoBonus.hashCode());
		assertFalse(dtoBonus.hashCode() == dtoBonus1.hashCode());
	}
	
	@Test
	public void testConvert() {
		DTObonus = new ArrayList<>();
		bonus.forEach(b -> DTObonus.add(DTOBonus.convert(b)));
		assertTrue(DTObonus.equals(bonus));
	}
	
}
