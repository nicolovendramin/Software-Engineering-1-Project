package it.polimi.ingsw.cg25.model.dto.dtobonus;

import it.polimi.ingsw.cg25.model.dashboard.bonus.CityBonus;

/**
 * 
 * @author Giovanni
 *
 */
public class DTOCityBonus extends DTOBonus {

	
	/**
	 * Serial number for serialization
	 */
	private static final long serialVersionUID = -6761535527795294992L;
	
	/**
	 * DTOCity class constructor
	 * @param bonus an object whose type is {@link CityBonus}
	 */
	public DTOCityBonus(CityBonus bonus) {
		super(bonus.getNumOfCitiesToChoose());
	}
	
}
