package it.polimi.ingsw.cg25.actions;

import java.util.List;

import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.CannotSetupActionException;
import it.polimi.ingsw.cg25.gamegenerics.ActionBasedGame;
import it.polimi.ingsw.cg25.model.dto.DTOAction;

/**
 * 
 * @author nicolo
 *
 */
public class ChooseActionTypeAction<T extends ActionBasedGame> extends ControlAction<T> {

	/**
	 * The interaction for the choice of the next action to do
	 */
	private SingleChoiceInteraction<DTOAction> dtoNextAction;
	
	public ChooseActionTypeAction(T s)
	{
		super(s);
	}
	
	/**
	 * executes the current action on the provided model
	 * @throws CannotPerformActionException when the setup of the following action chosen fails
	 */
	@Override
	public Action doAction() throws CannotPerformActionException {
		Action c;
		try{
			c = dtoNextAction.getReply().decode(getModel());
		}catch(NullPointerException e){
			this.getModel().getLogger().log(e);
			throw new CannotPerformActionException(e.getMessage());
		}
		return c;
	}

	/**
	 * Default implementation for setup. Uses the main action and quick action remaining getters of the model. Cannot override previous setups
	 */
	@Override
	public void setup() throws CannotSetupActionException{
		if(!this.getInteractions().isEmpty())
			return;
		List<Action> actionlist = this.getModel().getAvailableActions();
		if(actionlist == null) 
			throw new CannotSetupActionException("There are no available actions to do");
		this.dtoNextAction = new SingleChoiceInteraction<>("Choose what you want to do",DTOAction.convertAll(actionlist));
		this.setInteraction(dtoNextAction);
	}
	
	@Override
	public String toString(){
		return "Choose among the different possible types of actions available in this ActionBasedGame";
	}
}
