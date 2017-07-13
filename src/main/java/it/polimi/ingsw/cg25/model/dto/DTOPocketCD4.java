package it.polimi.ingsw.cg25.model.dto;

import it.polimi.ingsw.cg25.model.PocketCD4;

/**
 * 
 * @author Giovanni
 *
 */
public class DTOPocketCD4 implements DTO {

	/**
	 * Serial number for serial communication
	 */
	private static final long serialVersionUID = -6418903883111256906L;
	/**
	 * The number of victory points
	 */
	private final int victoryPoints;
	/**
	 * The number of nobilty points
	 */
	private final int nobilityRank;
	/**
	 * The number of coins
	 */
	private final int coins;
	/**
	 * The number of assistants
	 */
	private final int assistants;
	
	/**
	 * DTOPocketCD4 class constructor
	 * @param pocket a PocketCD4 type object that has to be turned into
	 * a data transfer object
	 */
	public DTOPocketCD4(PocketCD4 pocket) {
		this.victoryPoints = pocket.getVictoryPoints().getSupply();
		this.nobilityRank = pocket.getNobilityRank().getSupply();
		this.coins = pocket.getCoins().getSupply();
		this.assistants = pocket.getAssistants().getSupply();
	}

	/**
	 * @return the number of victory points in the pocket
	 */
	public int getVictoryPoints() {
		return victoryPoints;
	}

	/**
	 * @return the nobility rank included in the pocket
	 */
	public int getNobilityRank() {
		return nobilityRank;
	}

	/**
	 * @return the number of coins in the pocket
	 */
	public int getCoins() {
		return coins;
	}

	/** 
	 * @return the number of assistants in the pocket
	 */
	public int getAssistants() {
		return assistants;
	}
	
}
