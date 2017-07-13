/**
 * 
 */
package it.polimi.ingsw.cg25.model.dashboard.cards;

import java.util.List;

import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonusable;
import it.polimi.ingsw.cg25.model.dashboard.topological.City;
import it.polimi.ingsw.cg25.model.dashboard.topological.Region;
import it.polimi.ingsw.cg25.model.trade.Sellable;

/**
 * @author Davide
 *
 */
public class PermitCard implements Card,Bonusable,Sellable{

	/**
	 * Bonuses of the card
	 * @see Bonus
	 */
	private final List<Bonus> bonuses;
	
	/**
	 * Valid cities for the card
	 * @see City
	 */
	private final List<City> validCities;

	/**
	 * Creates a Permit Card
	 * 
	 * @param region
	 *            {@link Region} the region of the Permit Card
	 * @param validCities
	 *            ArrayList<{@link City}> the cities of the Permit Card
	 * @param bonuses
	 *            ArrayList<{@link Bonus}> the bonuses of the Permit Card
	 */
	public PermitCard(List<City> validCities, List<Bonus> bonuses) {
		if (bonuses.isEmpty())
			throw new IllegalArgumentException("No bonus in Permit Card.");
		if (validCities.isEmpty())
			throw new IllegalArgumentException("No valid cities for Permit Card.");
		this.bonuses = bonuses;
		this.validCities = validCities;
	}

	@Override
	public void takeAllBonuses(PlayerCD4 target) {
		for (Bonus b : bonuses) {
			b.acquireBonus(target);
		}
	}

	/**
	 * Return the bonuses of the Permit Card
	 * 
	 * @return List<{@link Bonus}> the bonuses
	 */
	public List<Bonus> getBonuses() {
		return bonuses;
	}

	/**
	 * @return List<{@link City}> the valid cities for the permit card
	 */
	public List<City> getValidCities() {
		return validCities;
	}

	/**
	 * 
	 * @return String: the first letters of each valid city of the Card
	 */
	public String getCitiesFirstLetters() {
		String s = "";
		for (City c : validCities)
			s = s + (c.getName().charAt(0)) + ", ";
		return s.substring(0, s.length() - 2);
	}

	public Boolean isValid(City c) {
		return getValidCities().contains(c);
	}
	
}
