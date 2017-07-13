package it.polimi.ingsw.cg25.actions;

import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.NotEnoughAssistantsException;
import it.polimi.ingsw.cg25.exceptions.NotEnoughCoinException;
import it.polimi.ingsw.cg25.model.Assistant;
import it.polimi.ingsw.cg25.model.Coin;
import it.polimi.ingsw.cg25.model.MatchCD4;
/**
 * 
 * @author nicolo
 *
 */
public class HireAssistantQuickAction extends QuickAction{
	
	public HireAssistantQuickAction(MatchCD4 model) {
		super(model);
	}

	/**
	 * Returns the string representation of the action
	 */
	@Override
	public String toString()
	{
		return "Pay 3 coins to hire an assistant";
	}

	/**
	 * Executes the action on the given model. 
	 * @throws CannotPerformActionException when the player doesn't have enough money to hire the assistant
	 * @return the next actions to be notified to the controller
	 * @throws NotEnoughAssistantsException 
	 */
	@Override
	public Action doAction() throws CannotPerformActionException {
		if(this.getModel().getRemainingQuickActions()<1)
			throw new CannotPerformActionException("You can do no more quick actions for this turn");
		try{
			this.getModel().getCurrentPlayer().getPocket().subPocketable(new Coin(3));
			this.getModel().useQuickAction();
		}
		catch(NotEnoughCoinException e)
		{
			this.getModel().getLogger().log(e);
			throw new CannotPerformActionException("You don't have enough coins to perform this action");
		}
		catch(CannotPerformActionException e){
			this.getModel().getLogger().log(e);
			this.getModel().getCurrentPlayer().getPocket().addPocketable(new Coin(3));
			throw new CannotPerformActionException("You can not do other quick actions");
		}
		this.getModel().getCurrentPlayer().getPocket().addPocketable(new Assistant(1));
		if(this.getModel().actionStackIsEmpty())	
			return new ChooseActionTypeAction<>(this.getModel());
		return this.getModel().popAction();
	}

}
