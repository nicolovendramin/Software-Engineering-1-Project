package it.polimi.ingsw.cg25.dto;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.model.Assistant;
import it.polimi.ingsw.cg25.model.Coin;
import it.polimi.ingsw.cg25.model.NobilityRank;
import it.polimi.ingsw.cg25.model.PocketCD4;
import it.polimi.ingsw.cg25.model.VictoryPoint;
import it.polimi.ingsw.cg25.model.dto.DTOPocketCD4;

public class DTOPocketTest {

	private DTOPocketCD4 dtoPocket;
	private PocketCD4 pocket;
	
	@Before
	public void setUp() {
		pocket = new PocketCD4(new Coin(10), new Assistant(5), new NobilityRank(2), new VictoryPoint(9));
		dtoPocket = new DTOPocketCD4(pocket);
	}
	
	@Test
	public void testGetCoins() {
		assertEquals(10, dtoPocket.getCoins());
	}
	
	@Test
	public void testGetAssistants() {
		assertEquals(5, dtoPocket.getAssistants());
	}
	
	@Test
	public void testGetNobilityRank() {
		assertEquals(2, dtoPocket.getNobilityRank());
	}
	
	@Test
	public void testGetVictoryPoints() {
		assertEquals(9, dtoPocket.getVictoryPoints());
	}
}
