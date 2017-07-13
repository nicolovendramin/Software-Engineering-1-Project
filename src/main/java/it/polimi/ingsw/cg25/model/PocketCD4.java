package it.polimi.ingsw.cg25.model;

import it.polimi.ingsw.cg25.exceptions.NotEnoughAssistantsException;
import it.polimi.ingsw.cg25.exceptions.NotEnoughCoinException;

/**
 * 
 * @author Giovanni
 *
 */
public class PocketCD4 {

	/**
	 * The Coin object which contains player's coins
	 */
	private Coin coins;
	/**
	 * The Assistants object which contains player's assistants
	 */
	private Assistant assistants;
	/**
	 * The NobilityRank object which contains player's nobility points
	 */
	private NobilityRank nobilityRank;
	/**
	 * The VictoryPoint object which contains player's victory points
	 */
	private VictoryPoint victoryPoints;
	
	/**
	 * PocketCD4 class constructor. It receives all the types of objects it needs to init
	 * the player's pocket.
	 * @param coins an object Coin
	 * @param assistants an object Assistant
	 * @param nobilityRank an object NobilityRank
	 * @param victoryPoints an object VictoryPoint
	 */
	public PocketCD4(Coin coins, Assistant assistants, NobilityRank nobilityRank, VictoryPoint victoryPoints) {
		if(coins == null)
			throw new IllegalArgumentException("You can't create a pocket without a Coin object!");
		if(assistants == null)
			throw new IllegalArgumentException("You can't create a pocket without an Assistant object!");
		if(nobilityRank == null)
			throw new IllegalArgumentException("You can't create a pocket without a NobilityRank object!");
		if(victoryPoints == null)
			throw new IllegalArgumentException("You can't create a pocket without a VictoryPoint object!");
		this.coins = coins;
		this.assistants = assistants;
		this.nobilityRank = nobilityRank;
		this.victoryPoints = victoryPoints;
	}
	
	/**
	 * The method is used in order to add the number of coins of a Coin object obj
	 * to the number of coins included in the current Coin object. It creates a new Coin object.
	 * @param obj the object Coin whose coins have to be added to the pocket
	 */
	public void addPocketable(Coin obj) {
		//Crea nuovo oggetto Coin con la somma dei coins
		coins = new Coin(coins.getSupply() + obj.getSupply());
	}
	
	/**
	 * The method is used in order to add the number of assistants of an Assistant object obj
	 * to the number of assistants included in the current Assistant object. It creates a new Assistant object.
	 * @param obj the object Assistant whose assistants have to be added to the pocket
	 */
	public void addPocketable(Assistant obj) {
		//Crea nuovo oggetto Coin con la somma dei coins
		assistants = new Assistant(assistants.getSupply() + obj.getSupply());
	}
	
	/**
	 * The method is used in order to add the number of nobility points of a NobilityRank object obj
	 * to the number of nobility points included in the current NobilityRank object. It creates a new NobilityRank object.
	 * @param obj the object NobilityRank whose nobility points have to be added to the pocket
	 */
	public void addPocketable(NobilityRank obj) {
		//Crea nuovo oggetto NobilityRank con la somma dei punti nobiltà
		nobilityRank = new NobilityRank(nobilityRank.getSupply() + obj.getSupply());
	}
	
	/**
	 * The method is used in order to add the number of victory points of a VictoryPoint object obj
	 * to the number of victory points included in the current VictoryPoint object. It creates a new VictoryPoint object.
	 * @param obj the object VictoryPoint whose victory points have to be added to the pocket
	 */
	public void addPocketable(VictoryPoint obj) {
		//Crea nuovo oggetto VictoryPoint con la somma dei punti vittoria
		victoryPoints = new VictoryPoint(victoryPoints.getSupply() + obj.getSupply());
	}
	
	/**
	 * The method is used in order to subtract the number of coins of a Coin object obj
	 * to the number of coins included in the current Coin object. It creates a new Coin object.
	 * @param obj the object Coin whose coins have to be removed from the pocket
	 */
	public void subPocketable(Coin obj) throws NotEnoughCoinException {
		//Crea nuovo oggetto Coin con la differenza dei coins
		if(coins.getSupply() - obj.getSupply() < 0)
			throw new NotEnoughCoinException();
		else coins = new Coin(coins.getSupply() - obj.getSupply());
	}
	
	/**
	 * The method is used in order to subtract the number of assistants of an Assistant object obj
	 * to the number of assistants included in the current Assistant object. It creates a new Assistant object.
	 * @param obj the object Assistant whose assistants have to be removed from the pocket
	 */
	public void subPocketable(Assistant obj) throws NotEnoughAssistantsException {
		//Crea nuovo oggetto Assistant con la differenza degli assistenti
		if(assistants.getSupply() - obj.getSupply() < 0)
			throw new NotEnoughAssistantsException();
		else assistants = new Assistant(assistants.getSupply() - obj.getSupply());
	}
	
	/**
	 * @return the object Coin included in the pocket
	 */
	public Coin getCoins() {
		return coins;
	}

	/**
	 * @return the object Assistant included in the pocket
	 */
	public Assistant getAssistants() {
		return assistants;
	}

	/**
	 * @return the object NobilityRank included in the pocket
	 */
	public NobilityRank getNobilityRank() {
		return nobilityRank;
	}

	/**
	 * @return the object VictoryPoint included in the pocket
	 */
	public VictoryPoint getVictoryPoints() {
		return victoryPoints;
	}
	
}
