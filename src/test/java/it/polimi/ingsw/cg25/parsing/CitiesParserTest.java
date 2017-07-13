package it.polimi.ingsw.cg25.parsing;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.model.dashboard.bonus.AssistantBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.BonusConcreteCreator;
import it.polimi.ingsw.cg25.model.dashboard.bonus.BonusCreator;
import it.polimi.ingsw.cg25.model.dashboard.bonus.CoinBonus;
import it.polimi.ingsw.cg25.model.dashboard.topological.City;

public class CitiesParserTest {

	private InputStreamReader inStrRdr;
	private CitiesParser cPar;
	private BonusCreator creator;
	
	@Before
	public void setUp() throws IOException {
		creator = new BonusConcreteCreator();
		//Here I'm gonna used a reduced file
		inStrRdr = new InputStreamReader(new FileInputStream("src/test/resources/cities.txt"));
		cPar = new CitiesParser(creator);
	}
	
	@Test(expected=IOException.class)
	public void testIOExceptionWithAFooInputStreamReader() throws IOException {
		InputStreamReader fooInStrRdr = new InputStreamReader(new FileInputStream("wrong/path/cities.txt"));
		cPar.parseLineByLine(fooInStrRdr);
	}
	
	@Test
	public void testParseLineByLine() throws IOException {
		cPar.parseLineByLine(inStrRdr);
		//Check names
		assertEquals("Milano", cPar.getCities().get(0).getName());
		assertEquals("Torino", cPar.getCities().get(1).getName());
		//Check Milano color and bonus
		assertEquals("Black", cPar.getCities().get(0).getColor().getColor().printTag());
		assertEquals(1, cPar.getCities().get(0).getCityBonus().size());
		cPar.getCities().get(0).getCityBonus().forEach(bonus -> {
			if(bonus instanceof CoinBonus) {
				CoinBonus cb = (CoinBonus) bonus;
				assertEquals(4, cb.getNumberOfCoins());
			}
			else {
				AssistantBonus cb = (AssistantBonus) bonus;
			assertEquals(1, cb.getNumberOfAssistants());
			}
		});
		assertEquals(2, cPar.getCities().get(0).getColor().getColorReward().size());
		cPar.getCities().get(0).getColor().getColorReward().forEach(bonus -> {
			if(bonus instanceof CoinBonus) {
				CoinBonus cb = (CoinBonus) bonus;
				assertEquals(2, cb.getNumberOfCoins());
			}
			else {
				AssistantBonus cb = (AssistantBonus) bonus;
			assertEquals(2, cb.getNumberOfAssistants());
			}
		});
		
		//Check Torino color and bonus (N.B.: in a set of 4 colors, the third is red
		assertEquals("Red", cPar.getCities().get(1).getColor().getColor().printTag());
		assertEquals(1, cPar.getCities().get(1).getCityBonus().size());
		cPar.getCities().get(1).getCityBonus().forEach(bonus -> {
			if(bonus instanceof CoinBonus) {
				CoinBonus cb = (CoinBonus) bonus;
				assertEquals(4, cb.getNumberOfCoins());
			}
			else {
				AssistantBonus cb = (AssistantBonus) bonus;
				assertEquals(1, cb.getNumberOfAssistants());
			}
		});
		assertEquals(2, cPar.getCities().get(1).getColor().getColorReward().size());
		cPar.getCities().get(1).getColor().getColorReward().forEach(bonus -> {
			if(bonus instanceof CoinBonus) {
				CoinBonus cb = (CoinBonus) bonus;
				assertEquals(3, cb.getNumberOfCoins());
			}
			else {
				AssistantBonus cb = (AssistantBonus) bonus;
				assertEquals(3, cb.getNumberOfAssistants());
			}
		});
	}
	
	@Test
	public void testGetCities() throws IOException {
		cPar.parseLineByLine(inStrRdr);
		assertEquals(2, cPar.getCities().size());
		cPar.getCities().forEach(c -> assertEquals(c.getClass(), City.class));
	}
	
}
