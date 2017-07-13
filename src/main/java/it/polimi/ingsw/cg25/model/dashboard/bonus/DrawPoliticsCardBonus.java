package it.polimi.ingsw.cg25.model.dashboard.bonus;

import it.polimi.ingsw.cg25.exceptions.NoCardsException;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.dto.dtobonus.BonusGenerator;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOBonus;
/**
 * 
 * @author Giovanni
 *
 */
public class DrawPoliticsCardBonus implements Bonus {

	/**
	 * The number of politics cards the player will receive with this bonus
	 */
	private final int numOfCardsToDraw;
	
	/**
	 * DrawPoliticsCardBonus class constructor
	 * @param numOfCardsToDraw the number of politcs cards assigned to this bonus
	 * @throws IllegalArgumentException when the number of politics cards to draw is less than 1
	 */
	public DrawPoliticsCardBonus(int numOfCardsToDraw) {
		if(numOfCardsToDraw <= 0)
			throw new IllegalArgumentException("I can't create a DrawPoliticsCard object with less than 1 politics card to draw!");
		else this.numOfCardsToDraw = numOfCardsToDraw;
	}
	
	@Override
	public void acquireBonus(PlayerCD4 target) {
		for(int i = 0; i < numOfCardsToDraw; i++) {
			MatchCD4 currentMatch = target.getCurrentMatch();
			//Eccezione gestita se nel mazzo non ci sono più carte
			try {
				target.addPoliticsCard(currentMatch.getBoard().getPoliticsDeck().drawCard());
			} catch (NoCardsException e) {
				//TODO: inform the player
				currentMatch.getLogger().log("Player "+target.getName()+" does not get Politics Card because deck is empty."+e);
			}
		}
	}
	
	/**
	 * @return the number of politics cards to draw related the current bonus
	 */
	public int getNumOfCardsToDraw() {
		return numOfCardsToDraw;
	}

	@Override
	public String toString() {
		return "Cards To Draw -> " + numOfCardsToDraw;
	}

	@Override
	public DTOBonus generate(BonusGenerator bg) {
		return bg.visit(this);
	}

}
