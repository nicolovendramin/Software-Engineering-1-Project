package it.polimi.ingsw.cg25.model.dto.dtobonus;

import it.polimi.ingsw.cg25.model.dashboard.bonus.AssistantBonus;

/**
 * 
 * @author Giovanni
 *
 */
public class DTOAssistantBonus extends DTOBonus {

	/**
	 * Serial number for serialization
	 */
	private static final long serialVersionUID = 6318694391027536422L;
	
	/**
	 * DTOAssistantBonus class constructor
	 * @param bonus an object whose type is {@link AssistantBonus}
	 */
	public DTOAssistantBonus(AssistantBonus bonus) {
		super(bonus.getNumberOfAssistants());
	}

}
