/**
 * 
 */
package it.polimi.ingsw.cg25.model.dashboard.cards;

import java.util.List;

import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonusable;

/**
 * @author Davide
 *
 */
public class RewardCard implements Card, Bonusable {
	/**
	 * Bonuses of the card
	 * @see Bonus
	 */
	private final List<Bonus> bonuses;

	/**
	 * Create a Reward Card
	 * 
	 * @param List<Bonus>:
	 *            a not empty list of bonuses to attach to the card
	 */
	public RewardCard(List<Bonus> bonuses) {
		if (bonuses.isEmpty())
			throw new IllegalArgumentException("Almost a Bonus is required to create a Reward Card");
		this.bonuses = bonuses;
	}

	/**
	 * Return the list of bonuses attached to the card
	 * 
	 * @return List<Bonus> : the bonuses
	 */
	public List<Bonus> getBonuses() {
		return bonuses;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Reward Card: Bonus = ");
		if(bonuses.isEmpty())
			sb.append("none");
		else {
		for(Bonus b : bonuses)
			sb.append(b + ", ");
		sb.setLength(sb.length() - 2);
		}
		return sb.toString();
	}

	@Override
	public void takeAllBonuses(PlayerCD4 target) {
		for (Bonus b : bonuses)
			b.acquireBonus(target);
		bonuses.clear();
	}

}
