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

public class KingParserTest {

	private InputStreamReader inStrRdr;
	private InputStreamReader inStrRdrKing;
	private CitiesParser cPar;
	private KingParser kingPar;
	private BonusCreator creator;
	
	@Before
	public void setUp() throws IOException {
		creator = new BonusConcreteCreator();
		//Here I'm gonna used a reduced file
		inStrRdr = new InputStreamReader(new FileInputStream("src/test/resources/cities.txt"));
		inStrRdrKing = new InputStreamReader(new FileInputStream("src/test/resources/king.txt"));
		cPar = new CitiesParser(creator);
		cPar.parseLineByLine(inStrRdr);
		kingPar = new KingParser(cPar.getCities(), creator);
	}
	
	@Test(expected=IOException.class)
	public void testIOExceptionWithAFooInputStreamReader() throws IOException {
		InputStreamReader fooInStrRdr = new InputStreamReader(new FileInputStream("wrong/path/king.txt"));
		kingPar.parseLineByLine(fooInStrRdr);
	}
	
	@Test
	public void testParseLineByLine() throws IOException {
		kingPar.parseLineByLine(inStrRdrKing);
		//Check king's city
		assertTrue(kingPar.getKingCity().equals(cPar.getCities().get(0)));
		//Check king's rewards
		for(int i = 0; i < 4; i++) {
			CoinBonus b = (CoinBonus) kingPar.getKingRewards().get(i).getBonuses().get(0);
			assertEquals(i+1, b.getNumberOfCoins());
			
			AssistantBonus b2 = (AssistantBonus) kingPar.getKingRewards().get(i).getBonuses().get(1);
			assertEquals(i+1, b2.getNumberOfAssistants());
		}
	}
	
}
