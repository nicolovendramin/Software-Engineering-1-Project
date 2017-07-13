package it.polimi.ingsw.cg25.model.dashboard.bonus;

import it.polimi.ingsw.cg25.model.Assistant;
import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.dto.dtobonus.BonusGenerator;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOBonus;

/**
 * 
 * @author Giovanni
 *
 */
public class AssistantBonus implements Bonus {

	/**
	 * Number of assistants
	 */
	private final int numberOfAssistants;
	
	/**
	 * AssistantBonus class constructor
	 * @param numberOfAssistants the number of assistants related to the current bonus
	 * @throws IllegalArgumentException when the number of assistants is less than 1
	 */
	public AssistantBonus(int numberOfAssistants) {
		if(numberOfAssistants <= 0) {
			throw new IllegalArgumentException("You can't create an AssistantBonus object with less than 1 assistant!");
		}
		else this.numberOfAssistants = numberOfAssistants;
	}
	
	/**
	 * @return the number of assistants related to the current bonus
	 */
	public int getNumberOfAssistants() {
		return numberOfAssistants;
	}

	@Override
	public void acquireBonus(PlayerCD4 target) {
		target.getPocket().addPocketable(new Assistant(numberOfAssistants));
	}

	@Override
	public String toString() {
		return "Assistants -> " + numberOfAssistants;
	}

	@Override
	public DTOBonus generate(BonusGenerator bg) {
		return bg.visit(this);
	}
	
}
