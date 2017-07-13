package it.polimi.ingsw.cg25.dto;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.model.Coin;
import it.polimi.ingsw.cg25.model.dashboard.NobilityCell;
import it.polimi.ingsw.cg25.model.dashboard.bonus.AssistantBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.CityBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.CoinBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.VictoryPointBonus;
import it.polimi.ingsw.cg25.model.dto.DTONobilityCell;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOAssistantBonus;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOCoinBonus;

public class DTONobilityCellTest {

	private NobilityCell nc1;
	private NobilityCell nc2;
	
	@Before
	public void setUp() {
		List<Bonus> nc1Bonuses = new ArrayList<>();
		nc1Bonuses.add(new CoinBonus(1));
		nc1Bonuses.add(new AssistantBonus(5));
		nc1 = new NobilityCell(1, nc1Bonuses);
		
		List<Bonus> nc2Bonuses = new ArrayList<>();
		nc2Bonuses.add(new VictoryPointBonus(2));
		nc2Bonuses.add(new CityBonus(9));
		nc2 = new NobilityCell(2, nc2Bonuses);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ShouldNotBePossibleToCreateADTONobilityCellWithoutAValidNobilityCell() {
		new DTONobilityCell(null);
	}
	
	@Test
	public void testConvertAll() {
		List<NobilityCell> nCells = new ArrayList<>();
		nCells.add(nc1);
		nCells.add(nc2);
		List<DTONobilityCell> dtoNC = DTONobilityCell.convertAll(nCells);
		assertEquals(dtoNC.get(0).getClass(), DTONobilityCell.class);
		assertEquals(dtoNC.get(1).getClass(), DTONobilityCell.class);
		assertEquals(1, dtoNC.get(0).getId());
		assertEquals(2, dtoNC.get(1).getId());
		assertEquals(2, dtoNC.get(0).getBonus().size());
		assertEquals(2, dtoNC.get(1).getBonus().size());
	}
	
	@Test
	public void testGetId() {
		assertEquals(1, new DTONobilityCell(nc1).getId());
		assertEquals(2, new DTONobilityCell(nc2).getId());
	}
	
	@Test
	public void testGetBonus() {
		DTONobilityCell dtoNC = new DTONobilityCell(nc1);
		assertEquals(2, dtoNC.getBonus().size());
		assertEquals(DTOCoinBonus.class, dtoNC.getBonus().get(0).getClass());
		assertEquals(DTOAssistantBonus.class, dtoNC.getBonus().get(1).getClass());
		assertEquals(1, dtoNC.getBonus().get(0).getBnsQnty());
		assertEquals(5, dtoNC.getBonus().get(1).getBnsQnty());
	}
	
	@Test
	public void testEquals() {
		DTONobilityCell dtoNC1 = new DTONobilityCell(nc1);
		DTONobilityCell dtoNC2 = new DTONobilityCell(nc2);
		assertTrue(dtoNC1.equals(nc1));
		assertTrue(dtoNC2.equals(nc2));
		assertFalse(dtoNC1.equals(dtoNC2));
		assertTrue(dtoNC1.equals(new DTONobilityCell(nc1)));
		//null
		assertFalse(dtoNC1.equals(null));
		//Different types
		assertFalse(dtoNC1.equals(new Coin(10)));
	}
	
	@Test
	public void testHashCode() {
		DTONobilityCell dtoNC1 = new DTONobilityCell(nc1);
		DTONobilityCell dtoNC2 = new DTONobilityCell(nc2);
		
		assertTrue(dtoNC1.hashCode() == dtoNC1.hashCode());
		assertFalse(dtoNC1.hashCode() == dtoNC2.hashCode());
	}
	
	@Test
	public void testToString() {
		DTONobilityCell dtoNC1 = new DTONobilityCell(nc1);
		
		assertEquals("ID = 1, Bonus = [CoinBonus: 1, AssistantBonus: 5]", dtoNC1.toString());
	}
}
