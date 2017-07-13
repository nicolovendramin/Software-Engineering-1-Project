package it.polimi.ingsw.cg25.model.dashboard.bonus;

/**
 * 
 * @author Giovanni
 *
 */
public abstract class BonusCreator {

	/**
	 * This method has to be implemented in a subclass which extends BonusCreator,
	 * such as a concrete factory.
	 * Its purpouse is to return a reference to a bonus of the seleted type. 
	 * @param bonus the type of bonus to return 
	 * @param attribute the attribute of the bonus
	 * @return a bonus of the selected type with the specified attribute
	 */
	public abstract Bonus createBonus(String bonus, String attribute);
	
}
