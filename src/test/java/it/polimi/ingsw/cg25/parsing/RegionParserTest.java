package it.polimi.ingsw.cg25.parsing;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.exceptions.CannotCreateGameException;
import it.polimi.ingsw.cg25.model.dashboard.bonus.AssistantBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.BonusConcreteCreator;
import it.polimi.ingsw.cg25.model.dashboard.bonus.BonusCreator;
import it.polimi.ingsw.cg25.model.dashboard.bonus.CoinBonus;

public class RegionParserTest {

	private InputStreamReader inStrRdrRg;
	private InputStreamReader inStrRdrCity;
	private InputStreamReader inStrRdrPol;
	private PoliticsParser polPar;
	private CitiesParser cPar;
	private RegionParser rgPar;
	private BonusCreator creator;
	
	@Before
	public void setUp() throws IOException {
		creator = new BonusConcreteCreator();
		//Here I'm gonna used a reduced file
		inStrRdrCity = new InputStreamReader(new FileInputStream("src/test/resources/cities.txt"));
		inStrRdrPol = new InputStreamReader(new FileInputStream("src/test/resources/politics.txt"));
		inStrRdrRg = new InputStreamReader(new FileInputStream("src/test/resources/regions.txt"));
		polPar = new PoliticsParser();
		polPar.parseLineByLine(inStrRdrPol);
		cPar = new CitiesParser(creator);
		cPar.parseLineByLine(inStrRdrCity);
		rgPar = new RegionParser(cPar.getCities(), creator, polPar.getCouncelors());
	}
	
	@Test(expected=IOException.class)
	public void testIOExceptionWithAFooInputStreamReader() throws IOException, CannotCreateGameException {
		InputStreamReader fooInStrRdr = new InputStreamReader(new FileInputStream("wrong/path/regions.txt"));
		rgPar.parseLineByLine(fooInStrRdr);
	}
	
	@Test
	public void testParseLineByLine() throws IOException, CannotCreateGameException {
		rgPar.parseLineByLine(inStrRdrRg);
		//Testa numero regioni
		assertEquals(2, rgPar.getRegions().size());
		//Testa numero faced up permit regioni
		rgPar.getRegions().forEach(r -> assertEquals(2, r.getFaceUpPermits().size()));
		//Testa nome regioni
		assertEquals("mare", rgPar.getRegions().get(0).getName());
		assertEquals("montagna", rgPar.getRegions().get(1).getName());
		//Testa numero di città delle regioni e nome città
		assertEquals(1, rgPar.getRegions().get(0).getCities().size());
		assertEquals("Milano", rgPar.getRegions().get(0).getCities().get(0).getName());
		assertEquals(1, rgPar.getRegions().get(1).getCities().size());
		assertEquals("Torino", rgPar.getRegions().get(1).getCities().get(0).getName());
		//Testa unemployed councelors:
		//2 regioni = 2 consigli + consiglio del re = 12 consiglieri -> 24 - 12 = 12 unemployed
		assertEquals(12, rgPar.getCouncelors().size());
		//Test king's council
		assertEquals("King's Council", rgPar.getKingCouncil().getLocation());
		assertEquals(4, rgPar.getKingCouncil().getCouncelors().size());
		//Test bonus card
		assertEquals(2, rgPar.getRegions().get(0).getBonusCard().getBonuses().size());
		//CoinBonus 3 | AssistantBonus 1 in the first region
		assertEquals(rgPar.getRegions().get(0).getBonusCard().getBonuses().get(0).getClass(), CoinBonus.class);
		CoinBonus cb = (CoinBonus) rgPar.getRegions().get(0).getBonusCard().getBonuses().get(0);
		assertEquals(3, cb.getNumberOfCoins());
		assertEquals(rgPar.getRegions().get(0).getBonusCard().getBonuses().get(1).getClass(), AssistantBonus.class);
		AssistantBonus ab = (AssistantBonus) rgPar.getRegions().get(0).getBonusCard().getBonuses().get(1);
		assertEquals(1, ab.getNumberOfAssistants());
		//AssistantBonus 2 in the second region
		assertEquals(rgPar.getRegions().get(1).getBonusCard().getBonuses().get(0).getClass(), AssistantBonus.class);
		AssistantBonus ab2 = (AssistantBonus) rgPar.getRegions().get(1).getBonusCard().getBonuses().get(0);
		assertEquals(2, ab2.getNumberOfAssistants());
		//Test permits
		//First region
		rgPar.getRegions().get(0).getFaceUpPermits().forEach(permit -> assertEquals("Milano", permit.getValidCities().get(0).getName()));
		//ATTENZIONE: le permit vengono mischiate
		for(int i = 0; i < 2; i++) {
			if(rgPar.getRegions().get(0).getFaceUpPermits().get(i).getBonuses().get(0) instanceof CoinBonus) {
				CoinBonus cbPermit = (CoinBonus) rgPar.getRegions().get(0).getFaceUpPermits().get(i).getBonuses().get(0);
				assertEquals(1, cbPermit.getNumberOfCoins());
			}
			else {
				AssistantBonus abPermit = (AssistantBonus) rgPar.getRegions().get(0).getFaceUpPermits().get(i).getBonuses().get(0);
				assertEquals(2, abPermit.getNumberOfAssistants());
			}
		}
		
		//Second region
		rgPar.getRegions().get(1).getFaceUpPermits().forEach(permit -> assertEquals("Torino", permit.getValidCities().get(0).getName()));
		//ATTENZIONE: le permit vengono mischiate
		for(int i = 0; i < 2; i++) {
			if(rgPar.getRegions().get(1).getFaceUpPermits().get(i).getBonuses().get(0) instanceof CoinBonus) {
				CoinBonus cbPermit = (CoinBonus) rgPar.getRegions().get(1).getFaceUpPermits().get(i).getBonuses().get(0);
				assertEquals(1, cbPermit.getNumberOfCoins());
			}
			else {
				AssistantBonus abPermit = (AssistantBonus) rgPar.getRegions().get(1).getFaceUpPermits().get(i).getBonuses().get(0);
				assertEquals(2, abPermit.getNumberOfAssistants());
			}
		}
	}
	
}
