package it.polimi.ingsw.cg25.model.dashboard.bonus;

import java.util.NoSuchElementException;

/**
 * 
 * @author Giovanni
 *
 */
public class BonusConcreteCreator extends BonusCreator {

	@Override
	public Bonus createBonus(String bonus, String attribute) {
		switch(bonus) {
			case"CoinBonus":
				return new CoinBonus(Integer.parseInt(attribute));
			case"AssistantBonus":
				return new AssistantBonus(Integer.parseInt(attribute));
			case"MainActionBonus":
				return new MainActionBonus(Integer.parseInt(attribute));
			case"NobilityPointBonus":
				return new NobilityPointBonus(Integer.parseInt(attribute));
			case"VictoryPointBonus":
				return new VictoryPointBonus(Integer.parseInt(attribute));
			case"CityBonus":
				return new CityBonus(Integer.parseInt(attribute));
			case"DrawPoliticsCardBonus":
				return new DrawPoliticsCardBonus(Integer.parseInt(attribute));
			case"ReusePermitBonus":
				return new ReusePermitBonus(Integer.parseInt(attribute));
			case"PermitCardBonus":
				return new PermitCardBonus(Integer.parseInt(attribute));
			default:
				throw new NoSuchElementException("A problem during the creation of a bonus has occurred! "
						+ "Try to check your source files!");
		}
	}
	
}
