package it.polimi.ingsw.cg25.model.dashboard.bonus;

import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.dto.dtobonus.BonusGenerator;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOBonus;
import it.polimi.ingsw.cg25.model.trade.Sellable;

/**
 * 
 * @author Giovanni
 *
 */
public interface Bonus extends Sellable {

	/**
	 * Add the bonus to the target player's statistics
	 * @param target the player who will achieve this bonus
	 */
	public void acquireBonus(PlayerCD4 target);
	
	/**
	 * The method is used to create a visitor pattern.
	 * It has to be overrided in all the bonus classes.
	 * @param bg a BonusGenerator object
	 * @return the DTO bonus decoded by the visitor pattern
	 */
	public DTOBonus generate(BonusGenerator bg);
	
}
