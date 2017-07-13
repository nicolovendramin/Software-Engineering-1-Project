package it.polimi.ingsw.cg25.model.dashboard.bonus;

import it.polimi.ingsw.cg25.model.PlayerCD4;

/**
 * 
 * @author Giovanni
 *
 */
@FunctionalInterface
public interface Bonusable {

	/**
	 * Add all the bonuses of an object to the target player's statistics
	 * @param target
	 */
	public void takeAllBonuses(PlayerCD4 target);
	
}
