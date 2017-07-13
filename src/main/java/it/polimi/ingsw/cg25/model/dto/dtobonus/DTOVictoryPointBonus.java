package it.polimi.ingsw.cg25.model.dto.dtobonus;

import it.polimi.ingsw.cg25.model.dashboard.bonus.VictoryPointBonus;

/**
 * 
 * @author Giovanni
 *
 */
public class DTOVictoryPointBonus extends DTOBonus {

	/**
	 * Serial number for serialization
	 */
	private static final long serialVersionUID = 4109638474511611803L;
	
	/**
	 * DTOVictoryPointBonus class constructor
	 * @param bonus an object whose type is{@link VictoryPointBonus}
	 */
	public DTOVictoryPointBonus(VictoryPointBonus bonus) {
		super(bonus.getNumberOfVPoints());
	}

}
