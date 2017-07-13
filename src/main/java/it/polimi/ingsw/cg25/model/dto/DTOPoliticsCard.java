/**
 * 
 */
package it.polimi.ingsw.cg25.model.dto;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.dashboard.cards.PoliticsCard;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOSellable;

/**
 * @author Davide
 *
 */
public class DTOPoliticsCard implements DTO, DTOSellable {

	/**
	 * Serial number for serial communication
	 */
	private static final long serialVersionUID = 5545168548387313063L;
	private final HSBColor color;
	private final boolean isJolly;

	public DTOPoliticsCard(PoliticsCard politicsCard) {
		this.color = politicsCard.getParty().getColor();
		this.isJolly = politicsCard.getParty().getIsJolly();
	}

	/**
	 * Converts PoliticsCards into DTOPoliticsCard object
	 * 
	 * @param toConvert
	 *            List<{@link PoliticsCards}> the cards to convert
	 * @return List<{@link DTOPoliticsCard}> the converted DTO objects
	 * @throws IllegalArgumentException
	 *             if the list of cards to convert is null
	 */
	public static List<DTOPoliticsCard> convertAll(List<PoliticsCard> toConvert) {
		if (toConvert == null)
			throw new IllegalArgumentException("Null parameter is not allowed.");
		List<DTOPoliticsCard> converted = new ArrayList<>();
		for (PoliticsCard tc : toConvert)
			converted.add(new DTOPoliticsCard(tc));

		return converted;
	}

	/**
	 * Decode a list of {@link DTOPoliticsCard} into a list of
	 * {@link PoliticsCard}
	 * 
	 * @param toDecode
	 *            List<{@link DTOPoliticsCards}> cards to decode
	 * @param candidates
	 *            List<{@link PoliticsCards}> the candidates for the decoding
	 *            operation
	 * @return List<{@link PoliticsCards}> the list of converted cards (same
	 *         index as input)
	 * @throws ElementNotFoundException
	 *             if almost a decode operation failed
	 * @throws IllegalArgumentException
	 *             on null parameters
	 */
	public static List<PoliticsCard> decodeAll(List<DTOPoliticsCard> toDecode, List<PoliticsCard> candidates)
			throws ElementNotFoundException {
		if (toDecode == null || candidates == null)
			throw new IllegalArgumentException("Null parameter not allowed.");
		List<DTOPoliticsCard> toDecodeCopy = new ArrayList<>(toDecode);
		List<PoliticsCard> decoded = new ArrayList<>();
		List<PoliticsCard> candidateCopy = new ArrayList<>(candidates);
		for (DTOPoliticsCard dc : toDecodeCopy) {
			PoliticsCard temp = dc.decode(candidateCopy);
			decoded.add(temp);
			candidateCopy.remove(temp);
		}
		return decoded;
	}

	/**
	 * @return the color of the DTO
	 */
	public HSBColor getColor() {
		return color;
	}

	/**
	 * @return if the DTO is a jolly
	 */
	public boolean isJolly() {
		return isJolly;
	}

	@Override
	public String toString() {
		if (this.isJolly)
			return "Politics Card: color -> Jolly";
		else
			return "Politics Card: color -> " + this.color.printTag();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + (isJolly ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object p) {
		if (p == null)
			return false;
		if (p.getClass() == PoliticsCard.class) {
			PoliticsCard pol = (PoliticsCard) p;
			return pol.getParty().getColor().equals(this.color) && pol.getParty().getIsJolly() == this.isJolly;
		}
		if (p.getClass() == DTOPoliticsCard.class) {
			DTOPoliticsCard pol = (DTOPoliticsCard) p;
			return pol.getColor().equals(this.color) && pol.isJolly() == this.isJolly;
		}
		return false;
	}

	/**
	 * Decode a {@link DTOPoliticsCard} into a {@link PoliticsCard}
	 * 
	 * @param candidates
	 *            List<{@link PoliticsCards}> the candidates for the decoding
	 *            operation
	 * @return the converted {@link PoliticsCards}
	 * @throws ElementNotFoundException
	 *             if decode operation failed
	 */
	public PoliticsCard decode(List<PoliticsCard> candidates) throws ElementNotFoundException {
		for (PoliticsCard p : candidates)
			if (this.equals(p))
				return p;
		throw new ElementNotFoundException();
	}

}
