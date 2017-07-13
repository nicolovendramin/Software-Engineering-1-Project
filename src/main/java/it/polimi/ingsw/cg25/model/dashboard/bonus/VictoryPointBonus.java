package it.polimi.ingsw.cg25.model.dashboard.bonus;

import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.VictoryPoint;
import it.polimi.ingsw.cg25.model.dto.dtobonus.BonusGenerator;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOBonus;

/**
 * 
 * @author Giovanni
 *
 */
public class VictoryPointBonus implements Bonus {

	/**
	 * Number of victory points
	 */
	final private int numberOfVPoints;
	
	/**
	 * VictoryPointBonus class constructor
	 * @param numberOfVPoints the number of victory points needed to init the bonus
	 * @throws IllegalArgumentException when the number of victory points is less than 1
	 */
	public VictoryPointBonus(int numberOfVPoints) {
		if(numberOfVPoints <= 0)
			throw new IllegalArgumentException("You can't create a VictoryPointBonus object with less than 1 victory point!");
		else this.numberOfVPoints = numberOfVPoints;
	}
	
	@Override
	public void acquireBonus(PlayerCD4 target) {
		target.getPocket().addPocketable(new VictoryPoint(numberOfVPoints));		
	}

	/**
	 * @return the number of victory points related to the current bonus
	 */
	public int getNumberOfVPoints() {
		return numberOfVPoints;
	}

	@Override
	public String toString() {
		return "Number Of Victory Points -> " + numberOfVPoints;
	}

	@Override
	public DTOBonus generate(BonusGenerator bg) {
		return bg.visit(this);
	}

}
