package it.polimi.ingsw.cg25.modelobjects;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.exceptions.CannotCreateGameException;
import it.polimi.ingsw.cg25.model.BoardCD4;
import it.polimi.ingsw.cg25.model.dashboard.Councelor;
import it.polimi.ingsw.cg25.model.dashboard.Council;
import it.polimi.ingsw.cg25.model.dashboard.King;
import it.polimi.ingsw.cg25.model.dashboard.NobilityCell;
import it.polimi.ingsw.cg25.model.dashboard.cards.KingRewardsDeck;
import it.polimi.ingsw.cg25.model.dashboard.cards.PoliticsDeck;
import it.polimi.ingsw.cg25.model.dashboard.topological.City;
import it.polimi.ingsw.cg25.model.dashboard.topological.Region;
import it.polimi.ingsw.cg25.parsing.BoardFactory;

@SuppressWarnings("unused")
public class BoardCD4Test {

	private InputStreamReader inStrRdr;
	private InputStreamReader inStrRdr2;
	private InputStreamReader inStrRdr3;
	private InputStreamReader inStrRdr4;
	private InputStreamReader inStrRdr5;
	private InputStreamReader inStrRdr6;
	
	private List<Region> regions;
	private Council kingCouncil;
	private PoliticsDeck politicsDeck;
	private KingRewardsDeck kingTiles;
	private List<NobilityCell> nobilityTrack;
	private List<Councelor> unemployedCouncelors;
	private City kingCity;
	private List<City> cities;
	private UndirectedGraph<City, DefaultEdge> roadNetwork;
	
	@Before
	public void setUp() throws FileNotFoundException {
		inStrRdr = new InputStreamReader(new FileInputStream("src/test/resources/nobilityCells.txt"));
		inStrRdr2 = new InputStreamReader(new FileInputStream("src/test/resources/king.txt"));
		inStrRdr3 = new InputStreamReader(new FileInputStream("src/test/resources/cities.txt"));
		inStrRdr4 = new InputStreamReader(new FileInputStream("src/test/resources/regions.txt"));
		inStrRdr5 = new InputStreamReader(new FileInputStream("src/test/resources/politics.txt"));
		inStrRdr6 = new InputStreamReader(new FileInputStream("src/test/resources/graph.txt"));
	}
	
	@Test
	public void testGetBoard() throws CannotCreateGameException {
		BoardFactory boardF = new BoardFactory(inStrRdr, inStrRdr5, inStrRdr3, inStrRdr6, inStrRdr2, inStrRdr4);
		assertNotNull(boardF.getBoard());
		assertEquals(BoardCD4.class, boardF.getBoard().getClass());
	}
	
	@Test
	public void testGetRegions() throws CannotCreateGameException {
		BoardFactory boardF = new BoardFactory(inStrRdr, inStrRdr5, inStrRdr3, inStrRdr6, inStrRdr2, inStrRdr4);
		boardF.getBoard().getRegions().forEach(r -> assertEquals(Region.class, r.getClass()));
		assertEquals(2, boardF.getBoard().getRegions().size());
	}
	
	@Test(expected=NullPointerException.class)
	public void testRegionsNullPointerException() throws CannotCreateGameException {
		BoardFactory boardF = new BoardFactory(inStrRdr, inStrRdr5, inStrRdr3, inStrRdr6, inStrRdr2, inStrRdr4);
		//Create a BoardCD4 object as a copy of the just created board without an attribute
		//in order to test exceptions
		this.regions = null;
		this.kingCouncil = boardF.getBoard().getKingCouncil();
		this.politicsDeck = boardF.getBoard().getPoliticsDeck();
		this.kingTiles = boardF.getBoard().getKingTiles();
		this.nobilityTrack = boardF.getBoard().getNobilityTrack();
		this.unemployedCouncelors = boardF.getBoard().getUnemployedCouncelors();
		this.kingCity = boardF.getBoard().getKing().getCurrentCity();
		this.cities = boardF.getBoard().getCities();
		this.roadNetwork = boardF.getBoard().getRoadNetwork();

		BoardCD4 board = new BoardCD4(this.regions, this.kingCouncil, this.politicsDeck, this.kingTiles, this.nobilityTrack, 
				this.unemployedCouncelors, this.kingCity, this.cities, this.roadNetwork);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testRegionsIllegalArgumentException() throws CannotCreateGameException {
		BoardFactory boardF = new BoardFactory(inStrRdr, inStrRdr5, inStrRdr3, inStrRdr6, inStrRdr2, inStrRdr4);
		//Create a BoardCD4 object as a copy of the just created board without an attribute
		//in order to test exceptions
		this.regions = new ArrayList<>(); //empty List of regions
		this.kingCouncil = boardF.getBoard().getKingCouncil();
		this.politicsDeck = boardF.getBoard().getPoliticsDeck();
		this.kingTiles = boardF.getBoard().getKingTiles();
		this.nobilityTrack = boardF.getBoard().getNobilityTrack();
		this.unemployedCouncelors = boardF.getBoard().getUnemployedCouncelors();
		this.kingCity = boardF.getBoard().getKing().getCurrentCity();
		this.cities = boardF.getBoard().getCities();
		this.roadNetwork = boardF.getBoard().getRoadNetwork();

		BoardCD4 board = new BoardCD4(this.regions, this.kingCouncil, this.politicsDeck, this.kingTiles, this.nobilityTrack, 
				this.unemployedCouncelors, this.kingCity, this.cities, this.roadNetwork);
	}
	
	@Test
	public void testGetKingCouncil() throws CannotCreateGameException {
		BoardFactory boardF = new BoardFactory(inStrRdr, inStrRdr5, inStrRdr3, inStrRdr6, inStrRdr2, inStrRdr4);
		assertEquals(Council.class, boardF.getBoard().getKingCouncil().getClass());
	}
	
	@Test(expected=NullPointerException.class)
	public void testKingCouncilNullPointerException() throws CannotCreateGameException {
		BoardFactory boardF = new BoardFactory(inStrRdr, inStrRdr5, inStrRdr3, inStrRdr6, inStrRdr2, inStrRdr4);
		//Create a BoardCD4 object as a copy of the just created board without an attribute
		//in order to test exceptions
		this.regions = boardF.getBoard().getRegions();
		this.kingCouncil = null;
		this.politicsDeck = boardF.getBoard().getPoliticsDeck();
		this.kingTiles = boardF.getBoard().getKingTiles();
		this.nobilityTrack = boardF.getBoard().getNobilityTrack();
		this.unemployedCouncelors = boardF.getBoard().getUnemployedCouncelors();
		this.kingCity = boardF.getBoard().getKing().getCurrentCity();
		this.cities = boardF.getBoard().getCities();
		this.roadNetwork = boardF.getBoard().getRoadNetwork();

		BoardCD4 board = new BoardCD4(this.regions, this.kingCouncil, this.politicsDeck, this.kingTiles, this.nobilityTrack, 
				this.unemployedCouncelors, this.kingCity, this.cities, this.roadNetwork);
	}
	
	@Test
	public void testGetPoliticsDeck() throws CannotCreateGameException {
		BoardFactory boardF = new BoardFactory(inStrRdr, inStrRdr5, inStrRdr3, inStrRdr6, inStrRdr2, inStrRdr4);
		assertEquals(PoliticsDeck.class, boardF.getBoard().getPoliticsDeck().getClass());
	}
	
	@Test(expected=NullPointerException.class)
	public void testPoliticsDeckNullPointerException() throws CannotCreateGameException {
		BoardFactory boardF = new BoardFactory(inStrRdr, inStrRdr5, inStrRdr3, inStrRdr6, inStrRdr2, inStrRdr4);
		//Create a BoardCD4 object as a copy of the just created board without an attribute
		//in order to test exceptions
		this.regions = boardF.getBoard().getRegions();
		this.kingCouncil = boardF.getBoard().getKingCouncil();
		this.politicsDeck = null;
		this.kingTiles = boardF.getBoard().getKingTiles();
		this.nobilityTrack = boardF.getBoard().getNobilityTrack();
		this.unemployedCouncelors = boardF.getBoard().getUnemployedCouncelors();
		this.kingCity = boardF.getBoard().getKing().getCurrentCity();
		this.cities = boardF.getBoard().getCities();
		this.roadNetwork = boardF.getBoard().getRoadNetwork();

		BoardCD4 board = new BoardCD4(this.regions, this.kingCouncil, this.politicsDeck, this.kingTiles, this.nobilityTrack, 
				this.unemployedCouncelors, this.kingCity, this.cities, this.roadNetwork);
	}
	
	@Test
	public void testGetKingTiles() throws CannotCreateGameException {
		BoardFactory boardF = new BoardFactory(inStrRdr, inStrRdr5, inStrRdr3, inStrRdr6, inStrRdr2, inStrRdr4);
		assertEquals(KingRewardsDeck.class, boardF.getBoard().getKingTiles().getClass());
	}
	
	@Test(expected=NullPointerException.class)
	public void testKingTilesNullPointerException() throws CannotCreateGameException {
		BoardFactory boardF = new BoardFactory(inStrRdr, inStrRdr5, inStrRdr3, inStrRdr6, inStrRdr2, inStrRdr4);
		//Create a BoardCD4 object as a copy of the just created board without an attribute
		//in order to test exceptions
		this.regions = boardF.getBoard().getRegions();
		this.kingCouncil = boardF.getBoard().getKingCouncil();
		this.politicsDeck = boardF.getBoard().getPoliticsDeck();
		this.kingTiles = null;
		this.nobilityTrack = boardF.getBoard().getNobilityTrack();
		this.unemployedCouncelors = boardF.getBoard().getUnemployedCouncelors();
		this.kingCity = boardF.getBoard().getKing().getCurrentCity();
		this.cities = boardF.getBoard().getCities();
		this.roadNetwork = boardF.getBoard().getRoadNetwork();

		BoardCD4 board = new BoardCD4(this.regions, this.kingCouncil, this.politicsDeck, this.kingTiles, this.nobilityTrack, 
				this.unemployedCouncelors, this.kingCity, this.cities, this.roadNetwork);
	}
	
	@Test
	public void testGetNobilityTrack() throws CannotCreateGameException {
		BoardFactory boardF = new BoardFactory(inStrRdr, inStrRdr5, inStrRdr3, inStrRdr6, inStrRdr2, inStrRdr4);
		boardF.getBoard().getNobilityTrack().forEach(nc -> assertEquals(NobilityCell.class, nc.getClass()));
		assertEquals(4, boardF.getBoard().getNobilityTrack().size());
	}
	
	@Test
	public void testGetNobilityCell() throws CannotCreateGameException {
		BoardFactory boardF = new BoardFactory(inStrRdr, inStrRdr5, inStrRdr3, inStrRdr6, inStrRdr2, inStrRdr4);
		for(int i = 0; i < 4; i++)
			assertEquals(i+1, boardF.getBoard().getNobilityCell(i).getId());
	}
	
	@Test(expected=NullPointerException.class)
	public void testNobilityTrackNullPointerException() throws CannotCreateGameException {
		BoardFactory boardF = new BoardFactory(inStrRdr, inStrRdr5, inStrRdr3, inStrRdr6, inStrRdr2, inStrRdr4);
		//Create a BoardCD4 object as a copy of the just created board without an attribute
		//in order to test exceptions
		this.regions = boardF.getBoard().getRegions();
		this.kingCouncil = boardF.getBoard().getKingCouncil();
		this.politicsDeck = boardF.getBoard().getPoliticsDeck();
		this.kingTiles = boardF.getBoard().getKingTiles();
		this.nobilityTrack = null;
		this.unemployedCouncelors = boardF.getBoard().getUnemployedCouncelors();
		this.kingCity = boardF.getBoard().getKing().getCurrentCity();
		this.cities = boardF.getBoard().getCities();
		this.roadNetwork = boardF.getBoard().getRoadNetwork();

		BoardCD4 board = new BoardCD4(this.regions, this.kingCouncil, this.politicsDeck, this.kingTiles, this.nobilityTrack, 
				this.unemployedCouncelors, this.kingCity, this.cities, this.roadNetwork);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNobilityTrackIllegalArgumentException() throws CannotCreateGameException {
		BoardFactory boardF = new BoardFactory(inStrRdr, inStrRdr5, inStrRdr3, inStrRdr6, inStrRdr2, inStrRdr4);
		//Create a BoardCD4 object as a copy of the just created board without an attribute
		//in order to test exceptions
		this.regions = boardF.getBoard().getRegions();
		this.kingCouncil = boardF.getBoard().getKingCouncil();
		this.politicsDeck = boardF.getBoard().getPoliticsDeck();
		this.kingTiles = boardF.getBoard().getKingTiles();
		this.nobilityTrack = new ArrayList<>();
		this.unemployedCouncelors = boardF.getBoard().getUnemployedCouncelors();
		this.kingCity = boardF.getBoard().getKing().getCurrentCity();
		this.cities = boardF.getBoard().getCities();
		this.roadNetwork = boardF.getBoard().getRoadNetwork();

		BoardCD4 board = new BoardCD4(this.regions, this.kingCouncil, this.politicsDeck, this.kingTiles, this.nobilityTrack, 
				this.unemployedCouncelors, this.kingCity, this.cities, this.roadNetwork);
	}
	
	@Test
	public void testGetUnemployedCouncelors() throws CannotCreateGameException {
		BoardFactory boardF = new BoardFactory(inStrRdr, inStrRdr5, inStrRdr3, inStrRdr6, inStrRdr2, inStrRdr4);
		boardF.getBoard().getUnemployedCouncelors().forEach(c -> assertEquals(Councelor.class, c.getClass()));
	}
	
	@Test(expected=NullPointerException.class)
	public void testUnemployedCouncelorsNullPointerException() throws CannotCreateGameException {
		BoardFactory boardF = new BoardFactory(inStrRdr, inStrRdr5, inStrRdr3, inStrRdr6, inStrRdr2, inStrRdr4);
		//Create a BoardCD4 object as a copy of the just created board without an attribute
		//in order to test exceptions
		this.regions = boardF.getBoard().getRegions();
		this.kingCouncil = boardF.getBoard().getKingCouncil();
		this.politicsDeck = boardF.getBoard().getPoliticsDeck();
		this.kingTiles = boardF.getBoard().getKingTiles();
		this.nobilityTrack = boardF.getBoard().getNobilityTrack();
		this.unemployedCouncelors = null;
		this.kingCity = boardF.getBoard().getKing().getCurrentCity();
		this.cities = boardF.getBoard().getCities();
		this.roadNetwork = boardF.getBoard().getRoadNetwork();

		BoardCD4 board = new BoardCD4(this.regions, this.kingCouncil, this.politicsDeck, this.kingTiles, this.nobilityTrack, 
				this.unemployedCouncelors, this.kingCity, this.cities, this.roadNetwork);
	}
	
	@Test
	public void testGetKing() throws CannotCreateGameException {
		BoardFactory boardF = new BoardFactory(inStrRdr, inStrRdr5, inStrRdr3, inStrRdr6, inStrRdr2, inStrRdr4);
		assertEquals(King.class, boardF.getBoard().getKing().getClass());
		assertEquals("Milano", boardF.getBoard().getKing().getCurrentCity().getName());
	}
	
	@Test(expected=NullPointerException.class)
	public void testKingCityNullPointerException() throws CannotCreateGameException {
		BoardFactory boardF = new BoardFactory(inStrRdr, inStrRdr5, inStrRdr3, inStrRdr6, inStrRdr2, inStrRdr4);
		//Create a BoardCD4 object as a copy of the just created board without an attribute
		//in order to test exceptions
		this.regions = boardF.getBoard().getRegions();
		this.kingCouncil = boardF.getBoard().getKingCouncil();
		this.politicsDeck = boardF.getBoard().getPoliticsDeck();
		this.kingTiles = boardF.getBoard().getKingTiles();
		this.nobilityTrack = boardF.getBoard().getNobilityTrack();
		this.unemployedCouncelors = boardF.getBoard().getUnemployedCouncelors();
		this.kingCity = null;
		this.cities = boardF.getBoard().getCities();
		this.roadNetwork = boardF.getBoard().getRoadNetwork();

		BoardCD4 board = new BoardCD4(this.regions, this.kingCouncil, this.politicsDeck, this.kingTiles, this.nobilityTrack, 
				this.unemployedCouncelors, this.kingCity, this.cities, this.roadNetwork);
	}
	
	@Test
	public void testGetCities() throws CannotCreateGameException {
		BoardFactory boardF = new BoardFactory(inStrRdr, inStrRdr5, inStrRdr3, inStrRdr6, inStrRdr2, inStrRdr4);
		boardF.getBoard().getCities().forEach(ct -> assertEquals(City.class, ct.getClass()));
		assertEquals(2, boardF.getBoard().getCities().size());
	}
	
	@Test(expected=NullPointerException.class)
	public void testCitiesNullPointerException() throws CannotCreateGameException {
		BoardFactory boardF = new BoardFactory(inStrRdr, inStrRdr5, inStrRdr3, inStrRdr6, inStrRdr2, inStrRdr4);
		//Create a BoardCD4 object as a copy of the just created board without an attribute
		//in order to test exceptions
		this.regions = boardF.getBoard().getRegions();
		this.kingCouncil = boardF.getBoard().getKingCouncil();
		this.politicsDeck = boardF.getBoard().getPoliticsDeck();
		this.kingTiles = boardF.getBoard().getKingTiles();
		this.nobilityTrack = boardF.getBoard().getNobilityTrack();
		this.unemployedCouncelors = boardF.getBoard().getUnemployedCouncelors();
		this.kingCity = boardF.getBoard().getKing().getCurrentCity();
		this.cities = null;
		this.roadNetwork = boardF.getBoard().getRoadNetwork();

		BoardCD4 board = new BoardCD4(this.regions, this.kingCouncil, this.politicsDeck, this.kingTiles, this.nobilityTrack, 
				this.unemployedCouncelors, this.kingCity, this.cities, this.roadNetwork);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCitiesIllegalArgumentException() throws CannotCreateGameException {
		BoardFactory boardF = new BoardFactory(inStrRdr, inStrRdr5, inStrRdr3, inStrRdr6, inStrRdr2, inStrRdr4);
		//Create a BoardCD4 object as a copy of the just created board without an attribute
		//in order to test exceptions
		this.regions = boardF.getBoard().getRegions();
		this.kingCouncil = boardF.getBoard().getKingCouncil();
		this.politicsDeck = boardF.getBoard().getPoliticsDeck();
		this.kingTiles = boardF.getBoard().getKingTiles();
		this.nobilityTrack = boardF.getBoard().getNobilityTrack();
		this.unemployedCouncelors = boardF.getBoard().getUnemployedCouncelors();
		this.kingCity = boardF.getBoard().getKing().getCurrentCity();
		this.cities = new ArrayList<>();
		this.roadNetwork = boardF.getBoard().getRoadNetwork();

		BoardCD4 board = new BoardCD4(this.regions, this.kingCouncil, this.politicsDeck, this.kingTiles, this.nobilityTrack, 
				this.unemployedCouncelors, this.kingCity, this.cities, this.roadNetwork);
	}
	
	@Test
	public void testGetRoadNetwork() throws CannotCreateGameException {
		BoardFactory boardF = new BoardFactory(inStrRdr, inStrRdr5, inStrRdr3, inStrRdr6, inStrRdr2, inStrRdr4);
		assertEquals(1, boardF.getBoard().getRoadNetwork().edgeSet().size());
		//Make sure the graph contains the board's cities
		assertTrue(boardF.getBoard().getRoadNetwork().containsVertex(boardF.getBoard().getCities().get(0)));
		assertTrue(boardF.getBoard().getRoadNetwork().containsVertex(boardF.getBoard().getCities().get(1)));
	}
	
	@Test(expected=NullPointerException.class)
	public void testRoadNetworkNullPointerException() throws CannotCreateGameException {
		BoardFactory boardF = new BoardFactory(inStrRdr, inStrRdr5, inStrRdr3, inStrRdr6, inStrRdr2, inStrRdr4);
		//Create a BoardCD4 object as a copy of the just created board without an attribute
		//in order to test exceptions
		this.regions = boardF.getBoard().getRegions();
		this.kingCouncil = boardF.getBoard().getKingCouncil();
		this.politicsDeck = boardF.getBoard().getPoliticsDeck();
		this.kingTiles = boardF.getBoard().getKingTiles();
		this.nobilityTrack = boardF.getBoard().getNobilityTrack();
		this.unemployedCouncelors = boardF.getBoard().getUnemployedCouncelors();
		this.kingCity = boardF.getBoard().getKing().getCurrentCity();
		this.cities = boardF.getBoard().getCities();
		this.roadNetwork = null;

		BoardCD4 board = new BoardCD4(this.regions, this.kingCouncil, this.politicsDeck, this.kingTiles, this.nobilityTrack, 
				this.unemployedCouncelors, this.kingCity, this.cities, this.roadNetwork);
	}
	
}
