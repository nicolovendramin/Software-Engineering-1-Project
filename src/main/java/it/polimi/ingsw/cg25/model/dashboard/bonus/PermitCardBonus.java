package it.polimi.ingsw.cg25.model.dashboard.bonus;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg25.actions.PermitCardBonusAction;
import it.polimi.ingsw.cg25.exceptions.CannotSetupActionException;
import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.dashboard.cards.PermitCard;
import it.polimi.ingsw.cg25.model.dto.dtobonus.BonusGenerator;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOBonus;

/**
 * 
 * @author Giovanni
 *
 */
public class PermitCardBonus implements Bonus {

	/**
	 * The number of permit cards a player can choose among all those 
	 * that are faced up in each region
	 */
	private final int numOfPermitToChoose;
	
	/**
	 * PermitCardBonus class construtor
	 * @param numOfPermitToChose the number of faced up permits a player can choose
	 * in a specified region
	 * @throws IllegalArgumentException when the number of permit cards to choose is less than 1
	 */
	public PermitCardBonus(int numOfPermitToChose) {
		if(numOfPermitToChose <= 0)
			throw new IllegalArgumentException("I can't create a PermitCardBonus object with less than 1 permit to choose!");
		else this.numOfPermitToChoose = numOfPermitToChose;
	}
	
	@Override
	public void acquireBonus(PlayerCD4 target) {
		PermitCardBonusAction permitAction = new PermitCardBonusAction(target.getCurrentMatch());
		//ArrayList con tutte le permit card rivolte verso l'alto
		List<PermitCard> facedUpPermits = new ArrayList<>();
		//Aggiungi all'array tutte le permit rivolte verso l'alto di tutte le regioni
		for(int i = 0; i < target.getCurrentMatch().getBoard().getRegions().size(); i++)
			facedUpPermits.addAll(target.getCurrentMatch().getBoard().getRegions().get(i).getFaceUpPermits());
		try {
			permitAction.setup(numOfPermitToChoose, facedUpPermits);
		} catch (CannotSetupActionException e) {
			// Basta fare il log?
			target.getCurrentMatch().getLogger().log(e, "Something went wrong with PermitCardBonus.acquireBonus() for player "+target.getName());
		}
		target.getCurrentMatch().pushAction(permitAction);
	}
	
	/**
	 * @return the number of faced up permit cards to choose
	 */
	public int getNumOfPermitToChoose() {
		return numOfPermitToChoose;
	}

	@Override
	public String toString() {
		return "Number Of Permits To Choose -> " + numOfPermitToChoose;
	}

	@Override
	public DTOBonus generate(BonusGenerator bg) {
		return bg.visit(this);
	}

}
