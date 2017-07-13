package it.polimi.ingsw.cg25.bonus;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.model.dashboard.bonus.AssistantBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.BonusConcreteCreator;
import it.polimi.ingsw.cg25.model.dashboard.bonus.BonusCreator;
import it.polimi.ingsw.cg25.model.dashboard.bonus.CityBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.CoinBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.DrawPoliticsCardBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.MainActionBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.NobilityPointBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.PermitCardBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.ReusePermitBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.VictoryPointBonus;

public class BonusConcreteCreatorTest {

	private BonusCreator bc;
	
	@Before
	public void setUp() {
		bc = new BonusConcreteCreator();
	}
	
	@Test
	public void testCreateBonus() {
		assertEquals(CoinBonus.class, bc.createBonus("CoinBonus", "10").getClass());
		assertEquals(AssistantBonus.class, bc.createBonus("AssistantBonus", "10").getClass());
		assertEquals(CityBonus.class, bc.createBonus("CityBonus", "10").getClass());
		assertEquals(DrawPoliticsCardBonus.class, bc.createBonus("DrawPoliticsCardBonus", "10").getClass());
		assertEquals(MainActionBonus.class, bc.createBonus("MainActionBonus", "10").getClass());
		assertEquals(NobilityPointBonus.class, bc.createBonus("NobilityPointBonus", "10").getClass());
		assertEquals(PermitCardBonus.class, bc.createBonus("PermitCardBonus", "10").getClass());
		assertEquals(ReusePermitBonus.class, bc.createBonus("ReusePermitBonus", "10").getClass());
		assertEquals(VictoryPointBonus.class, bc.createBonus("VictoryPointBonus", "10").getClass());
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testShouldNotBePossibleToCreateAnUndefinedBonus() {
		bc.createBonus("UndefinedBonus", "5");
	}
	
}
