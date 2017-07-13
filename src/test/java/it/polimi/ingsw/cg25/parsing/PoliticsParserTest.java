package it.polimi.ingsw.cg25.parsing;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.dashboard.Councelor;
import it.polimi.ingsw.cg25.model.dashboard.cards.PoliticsCard;

public class PoliticsParserTest {

	private InputStreamReader inStrRdr;
	private PoliticsParser polPar;
	
	@Before
	public void setUp() throws IOException {
		//Here I'm gonna used a reduced file
		inStrRdr = new InputStreamReader(new FileInputStream("src/test/resources/politics.txt"));
		polPar = new PoliticsParser();
	}
	
	@Test(expected=IOException.class)
	public void testIOExceptionWithAFooInputStreamReader() throws IOException {
		InputStreamReader fooInStrRdr = new InputStreamReader(new FileInputStream("wrong/path/politics.txt"));
		polPar.parseLineByLine(fooInStrRdr);
	}
	
	/**
	 * The test assure that parsing is correct using politics.txt
	 * @throws IOException
	 */
	@Test
	public void testParseLineByLine() throws IOException {
		polPar.parseLineByLine(inStrRdr);
		//Crea lista di 6 colori
		List<HSBColor> colors = HSBColor.getNDifferent(6);
		for(int i = 0; i < colors.size(); i++) {
			//Java 8 - controlla numero carte politiche di ogni colore
			long count = polPar.getPoliticsDeck().stream().filter(c -> c.getParty().getColor().equals(colors.get(0))).count();
			assertEquals(13, count);
		}
		
		//Controlla numero carte jolly
		long count = polPar.getPoliticsDeck().stream().filter(c -> c.getParty().getIsJolly()).count();
		assertEquals(12, count);
	}
	
	@Test
	public void testGetCouncelors() throws IOException {
		polPar.parseLineByLine(inStrRdr);
		//Controlla numero consiglieri
		assertEquals(24, polPar.getCouncelors().size());
		//Controlla tipo consiglieri
		polPar.getCouncelors().forEach(counc -> assertEquals(Councelor.class, counc.getClass()));
	}
	
	@Test
	public void testGetPoliticsDeck() throws IOException {
		polPar.parseLineByLine(inStrRdr);
		//Numero carte politiche
		assertEquals(90, polPar.getPoliticsDeck().size());
		//Controlla tipo consiglieri
		polPar.getPoliticsDeck().forEach(pc -> assertEquals(PoliticsCard.class, pc.getClass()));
	}
}
