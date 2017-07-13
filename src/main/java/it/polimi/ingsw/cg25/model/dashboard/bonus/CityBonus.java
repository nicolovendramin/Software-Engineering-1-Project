package it.polimi.ingsw.cg25.model.dashboard.bonus;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg25.actions.CityBonusAction;
import it.polimi.ingsw.cg25.exceptions.CannotSetupActionException;
import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.dashboard.Emporium;
import it.polimi.ingsw.cg25.model.dashboard.topological.City;
import it.polimi.ingsw.cg25.model.dto.dtobonus.BonusGenerator;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOBonus;

/**
 * 
 * @author Giovanni
 *
 */
public class CityBonus implements Bonus {

	/**
	 * The number of cities the player can choose to obtain reward tokens.
	 * The player must have an emporium there.
	 */
	private final int numOfCitiesToChoose;
	
	/**
	 * CityBonus class constructor
	 * @param numOfCitiesToChoose the number of city to choose
	 * @throws IllegalArgumentException when the number of cities to choose is less than 1
	 */
	public CityBonus(int numOfCitiesToChoose) {
		if(numOfCitiesToChoose <= 0)
			throw new IllegalArgumentException("You can't create a CityBonus object with less than 1 city to choose!");
		else this.numOfCitiesToChoose = numOfCitiesToChoose;
	}
	
	@Override
	public void acquireBonus(PlayerCD4 target) {
		CityBonusAction cityAction = new CityBonusAction(target.getCurrentMatch());
		//Città con emporio
		List<City> citiesWithEmp = new ArrayList<>();
		for(Emporium emp : target.getPlayerEmporiums())
			citiesWithEmp.add(emp.getCity());
		
		try {
			cityAction.setup(citiesWithEmp, numOfCitiesToChoose);
		} catch (CannotSetupActionException e) {
			target.getCurrentMatch().getLogger().log(e, "Something went wrong with CityBonus.acquireBonus() for player "+target.getName());
		}
		
		target.getCurrentMatch().pushAction(cityAction);
	}
	
	/**
	 * @return the number of cities to choose related to the current bonus
	 */
	public int getNumOfCitiesToChoose() {
		return numOfCitiesToChoose;
	}

	@Override
	public String toString() {
		return "Cities To Choose -> " + numOfCitiesToChoose;
	}

	@Override
	public DTOBonus generate(BonusGenerator bg) {
		return bg.visit(this);
	}

}
