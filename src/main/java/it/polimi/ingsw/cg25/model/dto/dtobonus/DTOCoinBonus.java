package it.polimi.ingsw.cg25.model.dto.dtobonus;

import it.polimi.ingsw.cg25.model.dashboard.bonus.CoinBonus;

/**
 * 
 * @author Giovanni
 *
 */
public class DTOCoinBonus extends DTOBonus {

	/**
	 * Serial number for serialization
	 */
	private static final long serialVersionUID = -6746839222929106783L;
	
	/**
	 * DTOCoinBonus class constructor
	 * @param bonus an object whose type is {@link CoinBonus}
	 */
	public DTOCoinBonus(CoinBonus bonus) {
		super(bonus.getNumberOfCoins());
	}
	
}
