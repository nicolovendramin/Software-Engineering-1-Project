package it.polimi.ingsw.cg25.model.dashboard.bonus;

import it.polimi.ingsw.cg25.model.Coin;
import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.dto.dtobonus.BonusGenerator;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOBonus;

/**
 * 
 * @author Giovanni
 *
 */
public class CoinBonus implements Bonus {

	/**
	 * Number of coins
	 */
	final private int numberOfCoins;
	
	/**
	 * CoinBonus class constructor
	 * @param numberOfCoins the number of coins needed to init the bonus
	 * @throws IllegalArgumentException when the number of coins is less than 1 coin
	 */
	public CoinBonus(int numberOfCoins) {
		if(numberOfCoins <= 0)
			throw new IllegalArgumentException("You can't create a CoinBonus object with less than 1 coin!");
		else this.numberOfCoins = numberOfCoins;
	}
	
	@Override
	public void acquireBonus(PlayerCD4 target) {
		target.getPocket().addPocketable(new Coin(numberOfCoins));
	}

	/**
	 * @return number of coins related to the current bonus object
	 */
	public int getNumberOfCoins() {
		return this.numberOfCoins;
	}

	@Override
	public String toString() {
		return "Coins -> " + numberOfCoins;
	}

	@Override
	public DTOBonus generate(BonusGenerator bg) {
		return bg.visit(this);
	}
	
}
