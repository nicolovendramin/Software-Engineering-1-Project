package it.polimi.ingsw.cg25.actions;

import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.dto.DTOAction;

public class ChooseMainActionAction extends MatchAction{

	/**
	 * The interaction for the choice of the main action to do
	 */
	private SingleChoiceInteraction<DTOAction> nextAction;
	
	public ChooseMainActionAction(MatchCD4 model) {
		super(model);
	}

	/**
	 * This executes the action on the current model. Precisely change the controllers current action
	 * @throws CannotPerformActionException when the setup of the chosen main action fails
	 */
	@Override
	public Action doAction() throws CannotPerformActionException {
		return this.nextAction.getReply().decode(getModel());
	}

	/**
	 * Initialize all the correct interactions
	 */
	@Override
	public void setup()
	{
		if(!this.getInteractions().isEmpty())
			return;
		this.nextAction = new SingleChoiceInteraction<>("Choose which main action you would like to perform",DTOAction.convertAll(MainAction.getMainActions(getModel())));
		this.setInteraction(nextAction);
	}
	
	/**
	 * Returns a String representation of the action
	 */
	@Override
	public String toString()
	{
		return "Do a main action";
	}
	
}
