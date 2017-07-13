package it.polimi.ingsw.cg25.dto;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.dashboard.bonus.AssistantBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.CityBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.CoinBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.DrawPoliticsCardBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.NobilityPointBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.VictoryPointBonus;
import it.polimi.ingsw.cg25.model.dashboard.topological.CityColor;
import it.polimi.ingsw.cg25.model.dto.DTOCityColor;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTODrawPoliticsCardBonus;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTONobilityPointBonus;

@SuppressWarnings("unused")
public class DTOCityColorTest {

	private CityColor color1;
	private CityColor color2;
	private CityColor color3;
	
	@Before
	public void setUp() {
		List<Bonus> bonus1 = new ArrayList<>();
		bonus1.add(new CoinBonus(5));
		bonus1.add(new AssistantBonus(2));
		color1 = new CityColor(HSBColor.getNDifferent(3).get(0), bonus1);
		
		List<Bonus> bonus2 = new ArrayList<>();
		bonus2.add(new CityBonus(8));
		bonus2.add(new VictoryPointBonus(4));
		color2 = new CityColor(HSBColor.getNDifferent(3).get(1), bonus2);
		
		List<Bonus> bonus3 = new ArrayList<>();
		bonus3.add(new NobilityPointBonus(1));
		bonus3.add(new DrawPoliticsCardBonus(9));
		color3 = new CityColor(HSBColor.getNDifferent(3).get(2), bonus3);
	}
	
	@Test(expected=NullPointerException.class)
	public void testCityColorNullPointerException() {
		DTOCityColor cc = new DTOCityColor(null);
	}
	
	@Test
	public void testGetCityColor() {
		DTOCityColor cc1 = new DTOCityColor(color1);
		DTOCityColor cc2 = new DTOCityColor(color2);
		assertTrue(cc1.getCityColor().equals(color1.getColor()));
		assertTrue(cc2.getCityColor().equals(color2.getColor()));
	}
	
	@Test
	public void testGetColorReward() {
		DTOCityColor cc3 = new DTOCityColor(color3);
		assertEquals(2, cc3.getColorReward().size());
		assertEquals(DTONobilityPointBonus.class, cc3.getColorReward().get(0).getClass());
		assertEquals(DTODrawPoliticsCardBonus.class, cc3.getColorReward().get(1).getClass());
		assertEquals(1, cc3.getColorReward().get(0).getBnsQnty());
		assertEquals(9, cc3.getColorReward().get(1).getBnsQnty());
	}
	
}
