package it.polimi.ingsw.cg25.dto;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.exceptions.CannotCreateGameException;
import it.polimi.ingsw.cg25.exceptions.NoCardsException;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.dto.DTOBoardCD4;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

public class DTOBoardCD4Test {

	private MatchCD4 model;
	private ModelProxy proxy;
	private BoardFactory factory;
	
	@Before
	public void setUp() throws FileNotFoundException, CannotCreateGameException, NoCardsException {
		factory = new BoardFactory(new FileReader("src/test/resources/nobilityCellsFULL.txt"),
				new FileReader("src/test/resources/politicsFULL.txt"), 
				new FileReader("src/test/resources/citiesFULL.txt"),
				new FileReader("src/test/resources/graphFULL.txt"), 
				new FileReader("src/test/resources/kingFULL.txt"),
				new FileReader("src/test/resources/regionsFULL.txt"));
		this.proxy = new ModelProxy();
		this.model = new MatchCD4(factory.getBoard(), this.proxy,false,10);
	}
	
	@Test(expected=NullPointerException.class)
	public void cantCreateADTOBoardWithoutAValidBoard() {
		new DTOBoardCD4(null);
	}
	
	@Test
	public void testGetRegions() {
		DTOBoardCD4 dtoBoard = new DTOBoardCD4(model.getBoard());
		//Check original regions and converted regions
		assertTrue(dtoBoard.getRegions().equals(model.getBoard().getRegions()));
	}
	
	@Test
	public void testGetKingCouncil() {
		DTOBoardCD4 dtoBoard = new DTOBoardCD4(model.getBoard());
		//Check original King's council and converted council
		assertTrue(dtoBoard.getKingCouncil().equals(model.getBoard().getKingCouncil()));
	}
	
	@Test
	public void testGetNobilityTrack() {
		DTOBoardCD4 dtoBoard = new DTOBoardCD4(model.getBoard());
		//Check original nobility track and the converted one
		assertTrue(dtoBoard.getNobilityTrack().equals(model.getBoard().getNobilityTrack()));
	}
	
	@Test
	public void testGetUnemployedCouncelors() {
		DTOBoardCD4 dtoBoard = new DTOBoardCD4(model.getBoard());
		//Check original unemployed councelors and converted councelors
		assertTrue(dtoBoard.getUnemployedCouncelors().equals(model.getBoard().getUnemployedCouncelors()));
	}
	
	@Test
	public void testGetCities() {
		DTOBoardCD4 dtoBoard = new DTOBoardCD4(model.getBoard());
		//Check original cities and converted cities
		assertTrue(dtoBoard.getCities().equals(model.getBoard().getCities()));
	}
	
	@Test
	public void testGetKingCity() {
		DTOBoardCD4 dtoBoard = new DTOBoardCD4(model.getBoard());
		//Check original king city and the converted one
		assertTrue(dtoBoard.getKingCity().equals(model.getBoard().getKing().getCurrentCity()));
	}
	
}
