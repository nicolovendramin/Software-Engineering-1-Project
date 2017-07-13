package it.polimi.ingsw.cg25.model.dashboard.bonus;

import it.polimi.ingsw.cg25.model.NobilityRank;
import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.dto.dtobonus.BonusGenerator;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOBonus;

/**
 * 
 * @author Giovanni
 *
 */
public class NobilityPointBonus implements Bonus {

	/**
	 * The number of steps achieved on the nobility track
	 */
	private final int steps;
	
	/**
	 * NobilityPointBonus class constructor
	 * @param steps the number of steps needed to init the bonus
	 * @throws IllegalArgumentException when the number of steps is less than 1
	 */
	public NobilityPointBonus(int steps) {
		if(steps <= 0)
			throw new IllegalArgumentException("You can't create a NobilityPointBonus object with less than 1 step!");
		else this.steps = steps;
	}
	
	/**
	 * @param target the player who will achieve this bonus
	 */
	@Override
	public void acquireBonus(PlayerCD4 target) {
		target.getPocket().addPocketable(new NobilityRank(steps));
		//This is necessary to make the player acquire the bonus contained in the nobility cell he arrives after acquiring the nobility bonus
		try{
			target.getCurrentMatch().getBoard().getNobilityTrack().get(target.getPocket().getNobilityRank().getSupply()).takeAllBonuses(target);
		}catch(IndexOutOfBoundsException e){
			target.getCurrentMatch().getLogger().log(e, "There are no more interesting cells in the Track");
		}
	}
	
	/**
	 * @return number of nobility track steps related to the current bonus object
	 */
	public int getSteps() {
		return this.steps;
	}

	@Override
	public String toString() {
		return "Nobility Steps -> " + steps;
	}

	@Override
	public DTOBonus generate(BonusGenerator bg) {
		return bg.visit(this);
	}

}
