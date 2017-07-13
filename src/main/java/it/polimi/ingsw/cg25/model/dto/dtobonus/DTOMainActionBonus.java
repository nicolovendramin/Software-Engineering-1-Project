package it.polimi.ingsw.cg25.model.dto.dtobonus;

import it.polimi.ingsw.cg25.model.dashboard.bonus.MainActionBonus;

/**
 * 
 * @author Giovanni
 *
 */
public class DTOMainActionBonus extends DTOBonus {

	/**
	 * Serial number for serialization
	 */
	private static final long serialVersionUID = 4601003485498946510L;
	
	/**
	 * DTOMainActionBonus lass constructor
	 * @param bonus an object whose type is {@link MainActionBonus}
	 */
	public DTOMainActionBonus(MainActionBonus bonus) {
		super(bonus.getAdditionalMainActions());
	}
	
}
