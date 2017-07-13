package it.polimi.ingsw.cg25.actions;

import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.dto.DTOAction;

/**
 * 
 * @author nicolo
 *
 */
public class ChooseQuickActionAction extends MatchAction{
	
	/**
	 * The interaction for the choice of the quick action to do
	 */
	private SingleChoiceInteraction<DTOAction> nextAction;
	

	public ChooseQuickActionAction(MatchCD4 model)
	{
		super(model);
	}

	/**
	 * sets up the action
	 */
	@Override
	public void setup()
	{
		if(!this.getInteractions().isEmpty()) 
			return;
		this.nextAction = new SingleChoiceInteraction<>("Choose which quick action you would like to perform",DTOAction.convertAll(QuickAction.getQuickActions(this.getModel())));
		this.setInteraction(nextAction);
	}
	
	/**
	 * Executes the following action on the given model
	 */
	@Override
	public Action doAction() {
		return this.nextAction.getReply().decode(getModel());
	}
	
	/**
	 * returns a string representation of the object
	 */
	@Override
	public String toString()
	{
		return "Do a quick action";
	}
}
