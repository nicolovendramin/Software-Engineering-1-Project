package it.polimi.ingsw.cg25.model.dashboard.bonus;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg25.actions.ReusePermitBonusAction;
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
public class ReusePermitBonus implements Bonus {

	/**
	 * The number of permit cards a player can choose among all those 
	 * he owns
	 */
	private final int numOfPermitToChoose;

	/**
	 * ReusePermitBonus class constructor
	 * @param numOfPermitToChose the number of faced down permits the player can choose
	 * @throws IllegalArgumentException when the number of permit cards to choose is less than 1
	 */
	public ReusePermitBonus(int numOfPermitToChose) {
		if(numOfPermitToChose <= 0)
			throw new IllegalArgumentException("You can't create a ReusePermitBonus object with less than 1 permit to choose!");
		else this.numOfPermitToChoose = numOfPermitToChose;
	}

	@Override
	public void acquireBonus(PlayerCD4 target) {
		ReusePermitBonusAction reusePermitAction = new ReusePermitBonusAction(target.getCurrentMatch());
		//Crea un lista con tutte le permit del giocatore, usate e non usate
		List<PermitCard> options = new ArrayList<>();
		//Permit da usare
		for(PermitCard pc : target.getPermitsToBeUsed())
			options.add(pc);
		//Permit già usate
		for(PermitCard pc : target.getUsedPermits())
			options.add(pc);
		
		try {
			reusePermitAction.setup(numOfPermitToChoose, options);
		} catch (CannotSetupActionException e) {
			target.getCurrentMatch().getLogger().log(e, "Something went wrong with ReusePermitBonus.acquireBonus() for player "+target.getName());
		}
		
		target.getCurrentMatch().pushAction(reusePermitAction);
	}

	/**
	 * @return the number of permit cards to be reused
	 */
	public int getNumOfPermitToChoose() {
		return numOfPermitToChoose;
	}

	@Override
	public String toString() {
		return "Number Of Permits To Reuse -> " + numOfPermitToChoose;
	}

	@Override
	public DTOBonus generate(BonusGenerator bg) {
		return bg.visit(this);
	}

}
