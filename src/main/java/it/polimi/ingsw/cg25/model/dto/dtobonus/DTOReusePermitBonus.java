package it.polimi.ingsw.cg25.model.dto.dtobonus;

import it.polimi.ingsw.cg25.model.dashboard.bonus.ReusePermitBonus;

/**
 * 
 * @author Giovanni
 *
 */
public class DTOReusePermitBonus extends DTOBonus {

	/**
	 * Serial number for serialization
	 */
	private static final long serialVersionUID = -1493104998208368385L;
	
	/**
	 * DTOReusePermitBonus class constructor
	 * @param bonus an object whose type is {@link ReusePermitBonus}
	 */
	public DTOReusePermitBonus(ReusePermitBonus bonus) {
		super(bonus.getNumOfPermitToChoose());
	}
	
}
