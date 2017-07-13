/**
 * 
 */
package it.polimi.ingsw.cg25.model.dto;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.model.dashboard.cards.RewardCard;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOBonus;

/**
 * @author Davide
 *
 */
public class DTORewardCard implements DTO {

	/**
	 * Serial number for serial communication
	 */
	private static final long serialVersionUID = -263033524911663106L;

	private final List<DTOBonus> bonuses;

	public DTORewardCard(RewardCard rewardCard) {
		this.bonuses = DTOBonus.convertAll(rewardCard.getBonuses());
	}

	/**
	 * Converts RewardCards into DTORewardCard object
	 * @param List<{@link RewardCard}> to convert
	 * @return List<{@link DTORewardCard}> 
	 * @throws IllegalArgumentException
	 *             if the list of cards to convert is null
	 */
	public static List<DTORewardCard> convertAll(List<RewardCard> rewardCards) {
		if(rewardCards == null)
			throw new IllegalArgumentException("Null parameter not allowed.");
		List<DTORewardCard> converted = new ArrayList<>();
		for (RewardCard p : rewardCards)
			converted.add(new DTORewardCard(p));
		return converted;
	}

	/**
	 * Decode a list of {@link DTORewardCard} into a list of {@link RewardCard}
	 * @param List<{@link DTORewardCards}> cards to decode
	 * @param List<{@link RewardCards}> candidates
	 * @return List<{@link RewardCards}> converted (same index as input)
	 * @throws ElementNotFoundException if almost a decode operation failed
	 * @throws IllegalArgumentException on null parameters
	 */
	public static List<RewardCard> decodeAll(List<DTORewardCard> toDecode, List<RewardCard> candidates) throws ElementNotFoundException {
		if(toDecode == null || candidates == null)
			throw new IllegalArgumentException("Null parameters not allowed.");
		List<DTORewardCard> toDecodeCopy = new ArrayList<>(toDecode);
		List<RewardCard> decoded = new ArrayList<>();
		List<RewardCard> candidateCopy = new ArrayList<>(candidates);
		for(DTORewardCard dc : toDecodeCopy){
			RewardCard temp = dc.decode(candidateCopy);
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

	@Override
	public String toString() {
		return "DTORewardCard [bonuses=" + bonuses + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bonuses == null) ? 0 : bonuses.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object p) {
		if (p == null)
			return false;
		if(p.getClass() == RewardCard.class) {
			RewardCard rew = (RewardCard)p;
			return this.bonuses.equals(rew.getBonuses());
		}
		if(p.getClass() == DTORewardCard.class) {
			DTORewardCard rew = (DTORewardCard)p;
			return this.bonuses.equals(rew.getBonuses());
		}
		return false;
	}

	/**
	 * Decode a {@link DTORewardCard} into a {@link RewardCard}
	 * @param List<{@link RewardCards}> candidates
	 * @return {@link RewardCards} converted
	 * @throws ElementNotFoundException the decode operation failed
	 */
	public RewardCard decode(List<RewardCard> candidates) throws ElementNotFoundException {
		for (RewardCard r : candidates)
			if (this.equals(r))
				return r;
		throw new ElementNotFoundException();
	}

}
