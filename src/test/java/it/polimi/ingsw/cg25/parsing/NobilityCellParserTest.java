package it.polimi.ingsw.cg25.parsing;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.model.dashboard.NobilityCell;
import it.polimi.ingsw.cg25.model.dashboard.bonus.AssistantBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.BonusConcreteCreator;
import it.polimi.ingsw.cg25.model.dashboard.bonus.BonusCreator;
import it.polimi.ingsw.cg25.model.dashboard.bonus.CoinBonus;

public class NobilityCellParserTest {

	private InputStreamReader inStrRdr;
	private NobilityCellParser nobCellPar;
	private BonusCreator creator;
	
	@Before
	public void setUp() throws IOException {
		creator = new BonusConcreteCreator();
		//Here I'm gonna used a reduced file
		inStrRdr = new InputStreamReader(new FileInputStream("src/test/resources/nobilityCells.txt"));
		nobCellPar = new NobilityCellParser(creator);
	}
	
	@Test(expected=IOException.class)
	public void testIOExceptionWithAFooInputStreamReader() throws IOException {
		InputStreamReader fooInStrRdr = new InputStreamReader(new FileInputStream("wrong/path/nobilityCells.txt"));
		nobCellPar.parseLineByLine(fooInStrRdr);
	}
	
	@Test
	public void testParseLineByLine() throws IOException {
		nobCellPar.parseLineByLine(inStrRdr);
		//Check Id
		nobCellPar.getNobilityCells().forEach(nc -> assertEquals(nobCellPar.getNobilityCells().indexOf(nc) + 1, nc.getId()));
		//Check cells
		for(int i = 0; i < 4; i++) {
			//Check bonus List size
			assertEquals(2, nobCellPar.getNobilityCells().get(i).getBonusList().size());
			//Check first bonus
			assertEquals(nobCellPar.getNobilityCells().get(i).getBonusList().get(0).getClass(), CoinBonus.class);
			CoinBonus b = (CoinBonus) nobCellPar.getNobilityCells().get(i).getBonusList().get(0);
			assertEquals(i+1, b.getNumberOfCoins());
			//Check second bonus
			assertEquals(nobCellPar.getNobilityCells().get(i).getBonusList().get(1).getClass(), AssistantBonus.class);
			AssistantBonus b2 = (AssistantBonus) nobCellPar.getNobilityCells().get(i).getBonusList().get(1);
			assertEquals(i+1, b2.getNumberOfAssistants());
		}
	}
	
	@Test
	public void testGetNobilityCells() throws IOException {
		nobCellPar.parseLineByLine(inStrRdr);
		//Check type and quantity
		assertEquals(4, nobCellPar.getNobilityCells().size());
		nobCellPar.getNobilityCells().forEach(nc -> assertEquals(nc.getClass(), NobilityCell.class));;
	}
}
