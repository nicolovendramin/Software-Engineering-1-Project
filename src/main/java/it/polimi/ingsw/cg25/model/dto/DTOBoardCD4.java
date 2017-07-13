package it.polimi.ingsw.cg25.model.dto;

import java.util.List;
import java.util.NoSuchElementException;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polimi.ingsw.cg25.model.BoardCD4;

/**
 * 
 * @author Giovanni
 *
 */
public class DTOBoardCD4 implements DTO {

	/**
	 * Serial number for serialization
	 */
	private static final long serialVersionUID = -7302156554831097878L;
	/**
	 * the regions included in the board
	 */
	private final List<DTORegion> regions;
	/**
	 * the king's council of the board
	 */
	private final DTOCouncil kingCouncil;
	/**
	 * the nobility track with all its cells
	 */
	private final List<DTONobilityCell> nobilityTrack;
	/**
	 * the List of unemployed councelors
	 */
	private final List<DTOCouncelor> unemployedCouncelors;
	/**
	 * the List of cities
	 */
	private final List<DTOCity> cities;
	/**
	 * the city where the king is
	 */
	private final DTOCity kingCity;
	/**
	 * The upper king reward card
	 */
	private final DTORewardCard kingReward;
	/**
	 * the DTO counterpart of the graph
	 */
	private final SimpleGraph<DTOCity, DefaultEdge> roadNetwork;
	
	/**
	 * DTOBoardCD4 class constructor
	 * @param board the BoardCD4 type object to convert
	 * @throws NullPointerException if board is null
	 */
	public DTOBoardCD4(BoardCD4 board) {
		if(board == null)
			throw new NullPointerException("You can't create a DTO board without a valid board!");
		
		this.regions = DTORegion.convertAll(board.getRegions());
		this.kingCouncil = new DTOCouncil(board.getKingCouncil());
		this.nobilityTrack = DTONobilityCell.convertAll(board.getNobilityTrack());
		this.unemployedCouncelors = DTOCouncelor.convertAll(board.getUnemployedCouncelors());
		this.cities = DTOCity.convertAll(board.getCities());
		this.kingCity = new DTOCity(board.getKing().getCurrentCity());
		
		//It could be null
		this.kingReward = new DTORewardCard(board.getKingTiles().peek());
		
		this.roadNetwork = new SimpleGraph<>(DefaultEdge.class);
		//Setting up the graph with DTOCity vertexes
		//Vertexes are the DTO cities included in the DTO Board
		//Add vertexes
		for(DTOCity c : this.cities)
			this.roadNetwork.addVertex(c);
		//Add edges
		for(DefaultEdge edge : board.getRoadNetwork().edgeSet()) {
			this.roadNetwork.addEdge(cityFromName(board.getRoadNetwork().getEdgeSource(edge).getName()), 
					cityFromName(board.getRoadNetwork().getEdgeTarget(edge).getName()));
		}
	}

	/**
	 * @return the List of DTO regions
	 */
	public List<DTORegion> getRegions() {
		return regions;
	}

	/**
	 * @return the DTO king's council
	 */
	public DTOCouncil getKingCouncil() {
		return kingCouncil;
	}

	/**
	 * @return the List of DTO nobility cells
	 */
	public List<DTONobilityCell> getNobilityTrack() {
		return nobilityTrack;
	}

	/**
	 * @return the List of DTO unemployed councelors
	 */
	public List<DTOCouncelor> getUnemployedCouncelors() {
		return unemployedCouncelors;
	}

	/**
	 * @return the List f DTO cities
	 */
	public List<DTOCity> getCities() {
		return cities;
	}

	/**
	 * @return the DTO city where the King is
	 */
	public DTOCity getKingCity() {
		return kingCity;
	}

	/**
	 * @return the upper king reward DTO card
	 */
	public DTORewardCard getKingReward() {
		return kingReward;
	}

	/**
	 * @return the graph in which vertexes are DTO cities
	 */
	public SimpleGraph<DTOCity, DefaultEdge> getRoadNetwork() {
		return roadNetwork;
	}
	
	/**
	 * Private method used by the constructor of the class in order to select
	 * a city by its name.
	 * @param name the name of the city to find
	 * @return the corresponding DTO city
	 * @throws NoSuchElementException if the city of that name is not found
	 */
	private DTOCity cityFromName(String name) {
		for(DTOCity c : this.cities) {
			if(c.getName().equals(name))
				return c;
		}
		throw new NoSuchElementException("Error in creating the DTO graph!");
	}
	
}
