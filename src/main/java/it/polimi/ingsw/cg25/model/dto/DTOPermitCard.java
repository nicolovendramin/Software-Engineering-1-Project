/**
 * 
 */
package it.polimi.ingsw.cg25.model.dto;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.model.dashboard.cards.PermitCard;
import it.polimi.ingsw.cg25.model.dto.dtobonus.*;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOSellable;

/**
 * @author Davide
 *
 */
public class DTOPermitCard implements DTO, DTOSellable {

	/**
	 * Serial number for serial communication
	 */
	private static final long serialVersionUID = -4211142563515750807L;

	private final List<DTOBonus> bonuses;
	private final List<DTOCity> cities;

	public DTOPermitCard(PermitCard permitCard) {
		this.bonuses = DTOBonus.convertAll(permitCard.getBonuses());
		this.cities = DTOCity.convertAll(permitCard.getValidCities());
	}
	
	/**
	 * Converts PermitCards into DTOPermitCard object
	 * @param List<{@link PermitCards}> to convert
	 * @return List<{@link DTOPermitCard}> the list of converted DTO object
	 * @throws IllegalArgumentException on null parameter
	 */
	public static List<DTOPermitCard> convertAll(List<PermitCard> permitCards) {
		if(permitCards == null)
			throw new IllegalArgumentException("Null parameter is not allowed.");
		List<DTOPermitCard> converted = new ArrayList<>();
		for (PermitCard p : permitCards)
			converted.add(new DTOPermitCard(p));
		return converted;
	}
	
	/**
	 * Decode a list of {@link DTOPermitCard} into a list of {@link PermitCard}
	 * @param List<{@link DTOPermitCard}> cards to decode
	 * @param List<{@link PermitCards}> candidates
	 * @return List<{@link PermitCards}> converted (same index as input)
	 * @throws ElementNotFoundException if almost a decode operation failed
	 * @throws IllegalArgumentException on null parameters
	 */
	public static List<PermitCard> decodeAll(List<DTOPermitCard> toDecode, List<PermitCard> candidates) throws ElementNotFoundException {
		if(toDecode == null || candidates == null)
			throw new IllegalArgumentException("Null parameter is not allowed.");
		List<DTOPermitCard> toDecodeCopy = new ArrayList<>(toDecode);
		List<PermitCard> decoded = new ArrayList<>();
		List<PermitCard> candidateCopy = new ArrayList<>(candidates);
		for(DTOPermitCard dc : toDecodeCopy){
			PermitCard temp = dc.decode(candidateCopy);
			decoded.add(temp);
			candidateCopy.remove(temp);
			}
		return decoded;
	}

	/**
	 * @return the bonuses
	 */
	public List<DTOBonus> getBonuses() {
		return bonuses;
	}

	/**
	 * @return the cities
	 */
	public List<DTOCity> getCities() {
		return cities;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Permit Card -> Bonus: ");
		for(DTOBonus b : bonuses) {
			sb.append(b + ", ");
		}
		sb.setLength(sb.length() - 2);
		sb.append("; ");
		sb.append("Cities: ");
		for(DTOCity c : cities) {
			sb.append(c.getName() + ", ");
		}
		sb.setLength(sb.length() - 2);
		sb.append(";");
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bonuses == null) ? 0 : bonuses.hashCode());
		result = prime * result + ((cities == null) ? 0 : cities.hashCode());
		return result;
	}
	
	/**
	 * The equals method receives a permit card and checks if the current DTO permit card is the same
	 * @param p the permit card to compare with
	 * @return true or false
	 */
	@Override
	public boolean equals(Object p) {
		if (p == null)
			return false;
		if(p.getClass() == PermitCard.class) {
			PermitCard permit = (PermitCard)p;
			return this.bonuses.equals(permit.getBonuses()) && this.cities.equals(permit.getValidCities());
		}
		if(p.getClass() == DTOPermitCard.class) {
			DTOPermitCard permit = (DTOPermitCard)p;
			return this.bonuses.equals(permit.getBonuses()) && this.cities.equals(permit.getCities());
		}
		return false;
	}

	/**
	 * decode allows you to check if in the List of canditate permit cards there's one equal to this DTO permit card
	 * @param candidates the List of permit cards
	 * @return the permit card equals to this DTO permit card
	 * @throws ElementNotFoundException
	 */
	public PermitCard decode(List<PermitCard> candidates) throws ElementNotFoundException {
		for (PermitCard p : candidates)
			if (this.equals(p))
				return p;
		throw new ElementNotFoundException();
	}
}
