package it.polimi.ingsw.cg25.model.dto.dtobonus;

import it.polimi.ingsw.cg25.model.dashboard.bonus.DrawPoliticsCardBonus;
/**
 * 
 * @author Giovanni
 *
 */
public class DTODrawPoliticsCardBonus extends DTOBonus {

	/**
	 * Serial number for serialization
	 */
	private static final long serialVersionUID = 1168112587604217714L;
	
	/**
	 * DTODrawPoliticsCardBonus class constructor
	 * @param bonus an object whose type is {@link DrawPoliticsCardBonus}
	 */
	public DTODrawPoliticsCardBonus(DrawPoliticsCardBonus bonus) {
		super(bonus.getNumOfCardsToDraw());
	}

}
