package it.polimi.ingsw.cg25.model.dto;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.PlayerCD4;

/**
 * 
 * @author Giovanni
 *
 */
public class DTOPlayerCD4 implements DTO {

	/**
	 * Serial number for serial communication
	 */
	private static final long serialVersionUID = 3354103453187437197L;
	/**
	 * The name of the DTO player
	 */
	private final String name;
	/**
	 * The pocket of the DTO player
	 */
	private final DTOPocketCD4 pocket;
	/**
	 * The current hand of the dto player
	 */
	private final List<DTOPoliticsCard> hand;
	/**
	 * The List of permit cards to be used
	 */
	private final List<DTOPermitCard> permitsToBeUsed;
	/**
	 * The List of used permit cards
	 */
	private final List<DTOPermitCard> usedPermits;
	/**
	 * The color of the DTO player
	 */
	private final HSBColor playerColor;
	
	/**
	 * DTOPlayerCD4 class constructor
	 * @param player the PlayerCD4 to be turned into a DTO player
	 */
	public DTOPlayerCD4(PlayerCD4 player) {
		if(player == null)
			throw new IllegalArgumentException("You can't create a DTO player without a valid player!");
		this.name = player.getName();
		this.pocket = new DTOPocketCD4(player.getPocket());
		this.hand = DTOPoliticsCard.convertAll(player.getPoliticsCards());
		this.permitsToBeUsed = DTOPermitCard.convertAll(player.getPermitsToBeUsed());
		this.usedPermits = DTOPermitCard.convertAll(player.getUsedPermits());
		this.playerColor = player.getColor();
	}

	/**
	 * This method allows you to convert a List of players into a List of DTO players
	 * @param toConvert the List of players to be converted
	 * @return the List of converted players
	 */
	public static List<DTOPlayerCD4> convertAll(List<PlayerCD4> toConvert) {
		List<DTOPlayerCD4> converted = new ArrayList<>();
		for (PlayerCD4 pl : toConvert)
			converted.add(new DTOPlayerCD4(pl));

		return converted;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hand.hashCode();
		result = prime * result + name.hashCode();
		result = prime * result + permitsToBeUsed.hashCode();
		result = prime * result + playerColor.hashCode();
		result = prime * result + pocket.hashCode();
		result = prime * result + usedPermits.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object pl) {
		if(pl == null)
			return false;
		if(pl.getClass() == PlayerCD4.class) {
			PlayerCD4 player = (PlayerCD4)pl;
			//Confronta il nome del giocatore con quello del DTO
			return this.name.equals(player.getName());
		}
		if(pl.getClass() == DTOPlayerCD4.class) {
			DTOPlayerCD4 player = (DTOPlayerCD4)pl;
			//Confronta il nome del giocatore DTO con quello del DTO
			return this.name.equals(player.getName());
		}
		return false;
	}

	/**
	 * @return the name of the DTO player
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the pocket related to this DTO player
	 */
	public DTOPocketCD4 getPocket() {
		return pocket;
	}

	/**
	 * @return the List of politics cards that are the hand of the DTO player
	 */
	public List<DTOPoliticsCard> getHand() {
		return hand;
	}

	/**
	 * @return the List of permit cards to be used
	 */
	public List<DTOPermitCard> getPermitsToBeUsed() {
		return permitsToBeUsed;
	}

	/**
	 * @return the List of used permit cards
	 */
	public List<DTOPermitCard> getUsedPermits() {
		return usedPermits;
	}
	
	/**
	 * @return the HSB color of the DTO player
	 */
	public HSBColor getPlayerColor() {
		return playerColor;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name: " + name + "\n");
		sb.append("\tColor: " + playerColor.printTag() + "\n");
		sb.append("\tVictory points: " + pocket.getVictoryPoints() + "\n");
		sb.append("\tNobility points: " + pocket.getNobilityRank() + "\n");
		sb.append("\tCoins: " + pocket.getCoins() + "\n");
		sb.append("\tAvailable assistants: " + pocket.getAssistants() + "\n");
		sb.append("\tCurrent hand:\n");
		int index = 1;
		for(DTOPoliticsCard pc : hand) {
			sb.append("\t\t" + index + ") " + pc.toString() + "\n");
			index++;
		}
		
		return sb.toString();
	}
	
}
