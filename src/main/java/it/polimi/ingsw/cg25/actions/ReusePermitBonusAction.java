package it.polimi.ingsw.cg25.actions;

import java.util.ArrayList;
import java.util.List;
import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.CannotSetupActionException;
import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.dashboard.cards.PermitCard;
import it.polimi.ingsw.cg25.model.dto.DTOPermitCard;

public class ReusePermitBonusAction extends MatchAction{

	/**
	 * The interaction for the choice of the permit cards to retake the bonus from
	 */
	private MultipleChoiceInteraction<DTOPermitCard> dtoCards;
	/**
	 * The possible PermitCard to be used for the decode of the choice
	 */
	private List<PermitCard> candidates;
	
	public ReusePermitBonusAction(MatchCD4 model) {
		super(model);
	}

	/**
	 * This doAction moves the selected permit to the hand of the player
	 * @return the following action to be passed to the controller
	 */
	@Override
	public Action doAction() throws CannotPerformActionException {
		List<PermitCard> cards;
		try {
			cards = DTOPermitCard.decodeAll(this.dtoCards.getReply(), candidates);
		} catch (ElementNotFoundException e) {
			this.getModel().getLogger().log(e);
			throw new CannotPerformActionException("Something went wrong with your selection.");
		}
		for(PermitCard pc : cards)
			pc.takeAllBonuses(getModel().getCurrentPlayer());
		
		if(this.getModel().actionStackIsEmpty()) 
			return new ChooseActionTypeAction<>(this.getModel());
		return this.getModel().popAction();
	}
	
	/**
	 * This is the custom setup which depends on the specific action. If called more than one time can ovverride previous changes
	 * @param numberOfCards the number of permit that can be reused
	 * @param options the list of permits between which to choose(used permits of the current player)
	 * @throws CannotSetupActionException when the player has no used permits in its hand
	 */
	public void setup(int numberOfCards, List<PermitCard> options) throws CannotSetupActionException{
		if(options.isEmpty()) 
			throw new CannotSetupActionException("You can't use the bonus because there are no used cards in you hand");
		this.candidates = options;
		this.dtoCards = new MultipleChoiceInteraction<>("Choose which cards you want to reuse", DTOPermitCard.convertAll(options),numberOfCards,numberOfCards);
		this.setInteraction(dtoCards);
	}
	
	/**
	 * This is the default implementation of the setup routine. If called after on an already set up action returns without modifications
	 * @throws CannotSetupActionException when the player has no used permits in its hand
	 */
	@Override
	public void setup() throws CannotSetupActionException{
		if(!this.getInteractions().isEmpty()) 
			return;
		List<PermitCard> temp = new ArrayList<>();
		temp.addAll(getModel().getCurrentPlayer().getUsedPermits());
		temp.addAll(getModel().getCurrentPlayer().getPermitsToBeUsed());
		this.setup(1,temp);
	}
}
