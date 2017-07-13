package it.polimi.ingsw.cg25.parsing;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.model.dashboard.bonus.BonusConcreteCreator;
import it.polimi.ingsw.cg25.model.dashboard.bonus.BonusCreator;

public class GraphParserTest {

	private InputStreamReader inStrRdr;
	private InputStreamReader inStrRdrGraph;
	private CitiesParser cPar;
	private GraphParser graphPar;
	private BonusCreator creator;
	
	@Before
	public void setUp() throws IOException {
		creator = new BonusConcreteCreator();
		//Here I'm gonna used a reduced file
		inStrRdr = new InputStreamReader(new FileInputStream("src/test/resources/cities.txt"));
		inStrRdrGraph = new InputStreamReader(new FileInputStream("src/test/resources/graph.txt"));
		cPar = new CitiesParser(creator);
		cPar.parseLineByLine(inStrRdr);
		graphPar = new GraphParser(cPar.getCities());
	}
	
	@Test(expected=IOException.class)
	public void testIOExceptionWithAFooInputStreamReader() throws IOException {
		InputStreamReader fooInStrRdr = new InputStreamReader(new FileInputStream("wrong/path/graph.txt"));
		graphPar.parseLineByLine(fooInStrRdr);
	}
	
	@Test
	public void testParseLineByLine() throws IOException {
		graphPar.parseLineByLine(inStrRdrGraph);
		//Check that Milano and Torino are present as vertex
		cPar.getCities().forEach(c -> assertTrue(graphPar.getRoadNetwork().containsVertex(c)));
		//Check edge between Milano and Torino
		assertTrue(graphPar.getRoadNetwork().containsEdge(cPar.getCities().get(0), cPar.getCities().get(1)));
	}
	
}
