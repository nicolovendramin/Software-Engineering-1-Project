package it.polimi.ingsw.cg25.dto;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.exceptions.CannotCreateGameException;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.dto.DTOBoardCD4;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

public class DTOBoardCD4GraphTest {

	private MatchCD4 model;
	private ModelProxy proxy;
	private BoardFactory factory;
	
	@Before
	public void setUp() throws FileNotFoundException, CannotCreateGameException {
		factory = new BoardFactory(new FileReader("src/test/resources/nobilityCells.txt"),
				new FileReader("src/test/resources/politics.txt"), 
				new FileReader("src/test/resources/cities.txt"),
				new FileReader("src/test/resources/graph.txt"), 
				new FileReader("src/test/resources/king.txt"),
				new FileReader("src/test/resources/regions.txt"));
		this.proxy = new ModelProxy();
		this.model = new MatchCD4(factory.getBoard(), this.proxy,false,10);
	}
	
	@Test
	public void testGetRoadNetwork() {
		DTOBoardCD4 dtoBoard = new DTOBoardCD4(model.getBoard());
		//Check the dto graph contains the same vertexes and links of the original graph
		//There are only two cities in the reduced configuration files
		assertTrue(dtoBoard.getRoadNetwork().containsVertex(dtoBoard.getCities().get(0)));
		assertTrue(dtoBoard.getRoadNetwork().containsVertex(dtoBoard.getCities().get(1)));
		assertTrue(dtoBoard.getRoadNetwork().containsEdge(dtoBoard.getCities().get(0), 
				dtoBoard.getCities().get(1)));
	}
	
}
