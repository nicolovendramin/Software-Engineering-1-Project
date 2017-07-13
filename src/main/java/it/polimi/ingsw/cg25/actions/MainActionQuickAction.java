package it.polimi.ingsw.cg25.actions;

import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.NotEnoughAssistantsException;
import it.polimi.ingsw.cg25.model.Assistant;
import it.polimi.ingsw.cg25.model.MatchCD4;
/**
 * 
 * @author nicolo
 *
 */
public class MainActionQuickAction extends QuickAction{
	
	public MainActionQuickAction(MatchCD4 model) {
		super(model);	
	}

	/**
	 * Returns a string representation of the current action
	 */
	@Override
	public String toString()
	{
		return "Send 3 assistants to get an additional Main Action for the current turn";
	}

	/**
	 * executes the current action on the given model
	 * @throws CannotPerformActionException when the player has not enough assistant to perform the action
	 * @return the next action to be passed to the controller
	 */
	@Override
	public Action doAction() throws CannotPerformActionException {
		if(this.getModel().getRemainingQuickActions()<1)
			throw new CannotPerformActionException("You can do no more quick actions for this turn");
		try{
			this.getModel().getCurrentPlayer().getPocket().subPocketable(new Assistant(3));
			this.getModel().useQuickAction();
		}
		catch(NotEnoughAssistantsException e)
		{
			this.getModel().getLogger().log(e);
			throw new CannotPerformActionException("You don't have enough assistants to perform this aciton");
		} catch (CannotPerformActionException e) {
			this.getModel().getLogger().log(e);
			this.getModel().getCurrentPlayer().getPocket().addPocketable(new Assistant(3));
			throw new CannotPerformActionException(e.getMessage());
		}
		this.getModel().addMainAction(1);
	    if(this.getModel().actionStackIsEmpty())	
	    	return new ChooseActionTypeAction<>(this.getModel());
	    return this.getModel().popAction();
	}
}
