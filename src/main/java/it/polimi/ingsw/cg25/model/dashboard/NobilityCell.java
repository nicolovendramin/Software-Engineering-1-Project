package it.polimi.ingsw.cg25.model.dashboard;

import java.util.*;

import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonusable;

/**
 * 
 * @author Giovanni
 *
 */
public class NobilityCell implements Bonusable{

	/**
	 * The nobility cell id
	 */
	private final int id;
	/**
	 * The nobility cell bonus List
	 */
	private final List<Bonus> bonusList;
	
	/**
	 * NobilityCell class constructor
	 * @param id the id of the current cell
	 * @param bonusList the arrayList of bonus to init the cell
	 * @exception IllegalArgumentException when the id is less than 0 or when bonusList is null
	 */
	public NobilityCell(int id, List<Bonus> bonusList) {
		if(id < 0)
			throw new IllegalArgumentException("Invalid nobility cell's id!");
		if(bonusList == null)
			throw new IllegalArgumentException("You can't create a nobility cell without a bonus List!");
		this.id = id;
		this.bonusList = bonusList;
	}

	@Override
	public void takeAllBonuses(PlayerCD4 target) {
		for(Bonus b : bonusList) {
			b.acquireBonus(target);
		}
		
	}
	
	/**
	 * @return the nobility cell id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return a reference to the bonus List
	 */
	public List<Bonus> getBonusList() {
		return bonusList;
	}
	
}