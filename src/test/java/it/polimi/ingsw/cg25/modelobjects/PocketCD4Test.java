package it.polimi.ingsw.cg25.modelobjects;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polimi.ingsw.cg25.exceptions.NotEnoughAssistantsException;
import it.polimi.ingsw.cg25.exceptions.NotEnoughCoinException;
import it.polimi.ingsw.cg25.model.Assistant;
import it.polimi.ingsw.cg25.model.Coin;
import it.polimi.ingsw.cg25.model.NobilityRank;
import it.polimi.ingsw.cg25.model.PocketCD4;
import it.polimi.ingsw.cg25.model.VictoryPoint;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("unused")
public class PocketCD4Test {

	@Test(expected=IllegalArgumentException.class)
	public void ShouldNotBePossibleToCreateAPocketWithoutACoinObject() {
		PocketCD4 pocket = new PocketCD4(null, new Assistant(10), new NobilityRank(5), new VictoryPoint(2));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ShouldNotBePossibleToCreateAPocketWithoutAnAssistantObject() {
		PocketCD4 pocket = new PocketCD4(new Coin(15), null, new NobilityRank(5), new VictoryPoint(2));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ShouldNotBePossibleToCreateAPocketWithoutANobilityRankObject() {
		PocketCD4 pocket = new PocketCD4(new Coin(15), new Assistant(30), null, new VictoryPoint(2));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ShouldNotBePossibleToCreateAPocketWithoutAVictoryPointObject() {
		PocketCD4 pocket = new PocketCD4(new Coin(15), new Assistant(15), new NobilityRank(5), null);
	}
	
	@Test
	public void testAddPocketable() {
		PocketCD4 pocket = new PocketCD4(new Coin(15), new Assistant(4), new NobilityRank(5), new VictoryPoint(2));
		//Coins test
		pocket.addPocketable(new Coin(1));
		assertEquals(16, pocket.getCoins().getSupply());
		//Assistants test
		pocket.addPocketable(new Assistant(2));
		assertEquals(6, pocket.getAssistants().getSupply());
		//Nobility rank test
		pocket.addPocketable(new NobilityRank(5));
		assertEquals(10, pocket.getNobilityRank().getSupply());
		//Victory points test
		pocket.addPocketable(new VictoryPoint(4));
		assertEquals(6, pocket.getVictoryPoints().getSupply());
	}
	
	@Test
	public void testSubPocketable() throws NotEnoughCoinException, NotEnoughAssistantsException {
		PocketCD4 pocket = new PocketCD4(new Coin(10), new Assistant(6), new NobilityRank(8), new VictoryPoint(0));
		//Coins test
		pocket.subPocketable(new Coin(5));
		assertEquals(5, pocket.getCoins().getSupply());
		//Assistants test
		pocket.subPocketable(new Assistant(2));
		assertEquals(4, pocket.getAssistants().getSupply());
	}
	
	@Test(expected=NotEnoughCoinException.class)
	public void testSubPocketableCoinException() throws NotEnoughCoinException {
		PocketCD4 pocket = new PocketCD4(new Coin(10), new Assistant(6), new NobilityRank(8), new VictoryPoint(0));
		//Coins test
		pocket.subPocketable(new Coin(11));
	}
	
	@Test(expected=NotEnoughAssistantsException.class)
	public void testSubPocketableAssistantException() throws NotEnoughAssistantsException {
		PocketCD4 pocket = new PocketCD4(new Coin(10), new Assistant(6), new NobilityRank(8), new VictoryPoint(0));
		//Coins test
		pocket.subPocketable(new Assistant(15));
	}
	
	@Test
	public void testGetCoins() {
		PocketCD4 pocket = new PocketCD4(new Coin(10), new Assistant(6), new NobilityRank(8), new VictoryPoint(0));
		assertEquals(10, pocket.getCoins().getSupply());
		assertEquals(Coin.class, pocket.getCoins().getClass());
	}
	
	@Test
	public void testGetAssistants() {
		PocketCD4 pocket = new PocketCD4(new Coin(10), new Assistant(6), new NobilityRank(8), new VictoryPoint(0));
		assertEquals(6, pocket.getAssistants().getSupply());
		assertEquals(Assistant.class, pocket.getAssistants().getClass());
	}
	
	@Test
	public void testGetNobilityRank() {
		PocketCD4 pocket = new PocketCD4(new Coin(10), new Assistant(6), new NobilityRank(8), new VictoryPoint(0));
		assertEquals(8, pocket.getNobilityRank().getSupply());
		assertEquals(NobilityRank.class, pocket.getNobilityRank().getClass());
	}
	
	@Test
	public void testGetVictoryPoints() {
		PocketCD4 pocket = new PocketCD4(new Coin(10), new Assistant(6), new NobilityRank(8), new VictoryPoint(0));
		assertEquals(0, pocket.getVictoryPoints().getSupply());
		assertEquals(VictoryPoint.class, pocket.getVictoryPoints().getClass());
	}
	
}
