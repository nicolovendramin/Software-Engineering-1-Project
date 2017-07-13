package it.polimi.ingsw.cg25.model.dashboard.bonus;

import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.dto.dtobonus.BonusGenerator;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOBonus;

/**
 * 
 * @author Giovanni
 *
 */
public class MainActionBonus implements Bonus {

	/**
	 * Number of additional main actions
	 */
	private final int additionalMainActions;
	
	/**
	 * MainActionBonus class constructor
	 * @param additionalMainActions the number of additional main actions to init the bonus
	 * @throws IllegalArgumentException when the number of additional main action is less than 1
	 */
	public MainActionBonus(int additionalMainActions) {
		if(additionalMainActions <= 0) {
			throw new IllegalArgumentException("You can't create a MainActionBonus object with less than 1 additional main action!");
		}
		else this.additionalMainActions = additionalMainActions;
		
	}
	
	
	/**
	 * @return the number of additional main action related to the current bonus
	 */
	public int getAdditionalMainActions() {
		return additionalMainActions;
	}

	@Override
	public void acquireBonus(PlayerCD4 target) {
		target.addMainAction(this.additionalMainActions);
	}

	@Override
	public String toString() {
		return "Additional Main Actions -> " + additionalMainActions;
	}

	@Override
	public DTOBonus generate(BonusGenerator bg) {
		return bg.visit(this);
	}

}
