package it.polimi.ingsw.cg25.model.dto.dtobonus;

import it.polimi.ingsw.cg25.model.dashboard.bonus.NobilityPointBonus;

/**
 * 
 * @author Giovanni
 *
 */
public class DTONobilityPointBonus extends DTOBonus {

	/**
	 * Serial number for serialization
	 */
	private static final long serialVersionUID = -836003160820619040L;
	
	/**
	 * DTONobilityPointBonus class constructor
	 * @param bonus an object whose type is {@link NobilityPointBonus}
	 */
	public DTONobilityPointBonus(NobilityPointBonus bonus) {
		super(bonus.getSteps());
	}
	
}
