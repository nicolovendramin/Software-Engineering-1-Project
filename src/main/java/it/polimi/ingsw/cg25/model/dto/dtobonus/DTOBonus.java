package it.polimi.ingsw.cg25.model.dto.dtobonus;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonus;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOSellable;

/**
 * 
 * @author Giovanni
 *
 */
public abstract class DTOBonus implements DTOSellable {
	/**
	 * Serial number for serialization
	 */
	private static final long serialVersionUID = -7406100839875431640L;
	
	/**
	 * The bonus quantity
	 */
	private final int bnsQnty;
	/**
	 * The BonusGenerator object used to create DTO bonuses which
	 * realizes a visitor pattern
	 */
	private static final BonusGenerator bg = new BonusGenerator();
	
	/**
	 * DTOBonus abstract class constructor
	 * @param bnsQnty the quantity related to the bonus to create
	 */
	public DTOBonus(int bnsQnty) {
		this.bnsQnty = bnsQnty;
	}
	
	/**
	 * @return the bonus quantity
	 */
	public int getBnsQnty() {
		return bnsQnty;
	}
	
	/**
	 * Static method to convert a Bonus type object in a DTOBonus object
	 * @param toConvert the bonus to convert
	 * @return the corresponding DTOBonus object
	 */
	public static DTOBonus convert(Bonus toConvert) {
		DTOBonus converted = toConvert.generate(bg);

		return converted;
	}
	
	/**
	 * Static method to convert a list of {@link Bonus} type objects in a List of {@link DTOBonus} objects
	 * @param toConvert the bonuses to convert
	 * @return the corresponding List of {@link DTOBonus} objects
	 */
	public static List<DTOBonus> convertAll(List<? extends Bonus> toConvert) {
		List<DTOBonus> converted = new ArrayList<>();
		toConvert.forEach(b -> converted.add(DTOBonus.convert(b)));

		return converted;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bnsQnty;
		return result;
	}

	@Override
	public boolean equals(Object bonus) {
		if(bonus == null)
			return false;
		//Se bonus è un DTOBonus
		if(bonus instanceof DTOBonus) {
			DTOBonus converted = (DTOBonus)bonus;
			return this.getClass() == converted.getClass() && this.getBnsQnty() == converted.getBnsQnty();
		}
		//Se bonus è un Bonus
		if(bonus instanceof Bonus) {
			//Converti bonus in DTOBonus
			DTOBonus convertedBonus = DTOBonus.convert((Bonus)bonus);
			//Controllo tipo e quantità
			return this.getClass() == convertedBonus.getClass() && this.getBnsQnty() == convertedBonus.getBnsQnty();
		}
		return false;
	}

	@Override
	public String toString() {
		return (this.getClass().getSimpleName()).replaceAll("DTO", "") + ": " + bnsQnty;
	}

}
