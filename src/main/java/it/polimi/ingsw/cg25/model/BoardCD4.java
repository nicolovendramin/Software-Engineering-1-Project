package it.polimi.ingsw.cg25.model;

import java.util.List;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import it.polimi.ingsw.cg25.model.dashboard.Councelor;
import it.polimi.ingsw.cg25.model.dashboard.Council;
import it.polimi.ingsw.cg25.model.dashboard.King;
import it.polimi.ingsw.cg25.model.dashboard.NobilityCell;
import it.polimi.ingsw.cg25.model.dashboard.cards.KingRewardsDeck;
import it.polimi.ingsw.cg25.model.dashboard.cards.PoliticsDeck;
import it.polimi.ingsw.cg25.model.dashboard.topological.City;
import it.polimi.ingsw.cg25.model.dashboard.topological.Region;

/**
 * 
 * @author Giovanni
 *
 */
public class BoardCD4 {

	/**
	 * The List of regions linked to the board
	 */
	private final List<Region> regions;
	/**
	 * The king's council
	 */
	private final Council kingCouncil;
	/**
	 * The politics deck
	 */
	private final PoliticsDeck politicsDeck;
	/**
	 * The king reward cards' deck
	 */
	private final KingRewardsDeck kingTiles;
	/**
	 * The List of nobility cells which create the nobility track
	 */
	private final List<NobilityCell> nobilityTrack;
	/**
	 * The List of unemployed councelors
	 */
	private final List<Councelor> unemployedCouncelors;
	/**
	 * The List of cities
	 */
	private final List<City> cities;
	/**
	 * A reference to the king
	 */
	private final King king;
	/**
	 * The undirected graph which represents roads between cities
	 */
	private final UndirectedGraph<City, DefaultEdge> roadNetwork;
	
	/**
	 * BoardCD4 class constructor
	 * @param regions the List of regions to assign to the board
	 * @param kingCouncil the king's council
	 * @param politicsDeck the politics deck
	 * @param kingTiles king's tiles deck
	 * @param nobilityTrack a List of nobility cells
	 * @param unemployedCouncelors a List of unemployed councelors
	 * @param kingCity the starting king's city
	 * @param cities a whole set of cities
	 * @param roadNetwork the graph which describes all the links between the cities
	 */
	public BoardCD4(List<Region> regions, Council kingCouncil, PoliticsDeck politicsDeck, KingRewardsDeck kingTiles,
			List<NobilityCell> nobilityTrack, List<Councelor> unemployedCouncelors, City kingCity, List<City> cities,
			UndirectedGraph<City, DefaultEdge> roadNetwork) {
		if(regions == null)
			throw new NullPointerException("You can't create a board without a List of regions!");
		if(regions.isEmpty())
			throw new IllegalArgumentException("You can't create a board without regions!");
		if(kingCouncil == null)
			throw new NullPointerException("You can't create a board without a King's Council!");
		if(politicsDeck == null)
			throw new NullPointerException("You can't create a board without a politics deck!");
		if(kingTiles == null)
			throw new NullPointerException("You can't create a board without a King's rewards deck!");
		if(nobilityTrack == null)
			throw new NullPointerException("You can't create a board without a List of nobilty cells!");
		if(nobilityTrack.isEmpty())
			throw new IllegalArgumentException("You can't create a board without a nobility track!");
		if(unemployedCouncelors == null)
			throw new NullPointerException("You can't create a board without a List of unemployed councelors!");
		if(kingCity == null)
			throw new NullPointerException("You can't create a board without a King's city!");
		if(cities == null)
			throw new NullPointerException("You can't create a board without a List of cities!");
		if(cities.isEmpty())
			throw new IllegalArgumentException("You can't create a board without at least one city!");
		if(roadNetwork == null)
			throw new NullPointerException("You can't create a board without a graph!");
		
		this.regions = regions;
		this.kingCouncil = kingCouncil;
		this.politicsDeck = politicsDeck;
		this.kingTiles = kingTiles;
		this.nobilityTrack = nobilityTrack;
		this.unemployedCouncelors = unemployedCouncelors;
		
		this.king = new King(this, kingCity);
		
		this.cities = cities;
		this.roadNetwork = roadNetwork;
	}

	/**
	 * @return the collection of regions
	 */
	public List<Region> getRegions() {
		return regions;
	}

	/**
	 * @return the king's council
	 */
	public Council getKingCouncil() {
		return kingCouncil;
	}

	/**
	 * @return the politics deck
	 */
	public PoliticsDeck getPoliticsDeck() {
		return politicsDeck;
	}

	/**
	 * @return the reference to the deck of king's tiles
	 */
	public KingRewardsDeck getKingTiles() {
		return kingTiles;
	}
	
	/**
	 * @return the collection of cities
	 */
	public List<City> getCities() {
		return cities;
	}

	/**
	 * @return a reference to the king
	 */
	public King getKing() {
		return king;
	}

	/**
	 * @param index the index of the nobility track ArrayList
	 * @return the nobility cell related to the specified index
	 */
	public NobilityCell getNobilityCell(int index) {
		return nobilityTrack.get(index);
	}
	
	/**
	 * @return a reference to the nobility track
	 */
	public List<NobilityCell> getNobilityTrack() {
		return nobilityTrack;
	}

	/**
	 * @return the collection of unemployed councelors
	 */
	public List<Councelor> getUnemployedCouncelors() {
		return unemployedCouncelors;
	}

	/**
	 * @return the road network graph
	 */
	public UndirectedGraph<City, DefaultEdge> getRoadNetwork() {
		return roadNetwork;
	}
	
}
