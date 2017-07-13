package it.polimi.ingsw.cg25.model.dto.dtobonus;

import it.polimi.ingsw.cg25.model.dashboard.bonus.PermitCardBonus;

/**
 * 
 * @author Giovanni
 *
 */
public class DTOPermitCardBonus extends DTOBonus {

	/**
	 * Serial number for serialization
	 */
	private static final long serialVersionUID = 2524912473397776826L;
	
	/**
	 * DTOPermitCardBonus class constructor
	 * @param bonus an object whose type is {@link PermitCardBonus}
	 */
	public DTOPermitCardBonus(PermitCardBonus bonus) {
		super(bonus.getNumOfPermitToChoose());
	}

}
