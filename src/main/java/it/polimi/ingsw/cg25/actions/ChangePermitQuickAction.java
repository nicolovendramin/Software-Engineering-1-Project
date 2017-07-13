package it.polimi.ingsw.cg25.actions;

import java.util.List;

import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.CannotSetupActionException;
import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.exceptions.NoCardsException;
import it.polimi.ingsw.cg25.exceptions.NotEnoughAssistantsException;
import it.polimi.ingsw.cg25.model.Assistant;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.dashboard.topological.Region;
import it.polimi.ingsw.cg25.model.dto.DTORegion;
/**
 * 
 * @author nicolo
 *
 */
public class ChangePermitQuickAction extends QuickAction{

	/**
	 * The interactoin for the choice of the Region where you want to change the permit
	 */
	private SingleChoiceInteraction<DTORegion> dtoRegion;
	
	public ChangePermitQuickAction(MatchCD4 model) {
		super(model);
	}

	/**
	 * 
	 * @return the next action to be notified to the controller
	 * @throws CannotPerformActionException when the execution of the action fails
	 */
	@Override
	public Action doAction() throws CannotPerformActionException {
		Region region = null;
		
		// Decode DTORegion to Region
		try {
			region = this.dtoRegion.getReply().decode(this.getModel().getBoard().getRegions());
		} catch (ElementNotFoundException e1) {
			this.getModel().getLogger().log(e1);
			throw new CannotPerformActionException("Something went wrong with the selection of the region");
		}
		// Checks if the player can pay for this action
		if(this.getModel().getCurrentPlayer().getPocket().getAssistants().getSupply()<1)
			throw new CannotPerformActionException("You don't have enough assistants to make that move");
		try {
			if(this.getModel().getRemainingQuickActions()<=0)
				throw new CannotPerformActionException("You can do no more quick actions for this turn");
			this.getModel().getCurrentPlayer().getPocket().subPocketable(new Assistant(1));
			region.changeFaceUpPermits();
			this.getModel().useQuickAction();
		}
		// No permit cards left in the region's deck
		catch(NoCardsException e)
		{
			this.getModel().getLogger().log(e);
			throw new CannotPerformActionException("You can not do this move because there are no more cards in that region");
		}
		// Current player can't pay for the action
		catch(NotEnoughAssistantsException e)
		{
			this.getModel().getLogger().log(e);
			throw new CannotPerformActionException("You can not do this move becuase you don't have enough assistants");
		}
		// Current player can't do another quick action in this turn
		catch (CannotPerformActionException e) {
			this.getModel().getLogger().log(e);
			throw new CannotPerformActionException("You can do no more quick actions for this turn");
		}
		
		// Asks the player for a new available action
		if(this.getModel().actionStackIsEmpty()) 
			return new ChooseActionTypeAction<>(this.getModel());
		return this.getModel().popAction();
	}
	
	/**
	 * Returns a string representation of the current action
	 */
	@Override
	public String toString()
	{
		return "Send one assistant to change the face-up permit cards of one of the regions";
	}
	
	/**
	 * Sets up all the necessary interactions for the current action. Can override previous setups of the action
	 * @param regions are the possible regions where the player can decide to apply the action
	 * @throws CannotSetupActionException  if the list of regions is null or empty
	 */
	public void setup(List<Region> regions) throws CannotSetupActionException
	{
		this.getInteractions().clear();
		if(regions == null || regions.isEmpty()) 
			throw new CannotSetupActionException("You must provide a non empty list of regions");
		this.dtoRegion = new SingleChoiceInteraction<>("Choose the region where you want to change the cards",DTORegion.convertAll(regions));
		this.setInteraction(dtoRegion);
	}
	
	/**
	 * Default implementation for setup. The regions displayed are all the regions of the board
	 * @throws CannotSetupActionException when the region list of the board is null
	 */
	@Override
	public void setup() throws CannotSetupActionException{
		if(!this.getInteractions().isEmpty())
			return;
		this.setup(this.getModel().getBoard().getRegions());
	}
}
