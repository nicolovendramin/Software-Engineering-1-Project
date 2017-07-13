package it.polimi.ingsw.cg25.actions;

import java.io.Serializable;

import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.model.MatchCD4;
/**
 * 
 * @author nicolo
 *
 * @param <T> the type of the object displayed by the Action
 */
public class DisplayAction<T extends Serializable> extends MatchAction{
	
	public DisplayAction(MatchCD4 model) {
		super(model);
	}

	@Override
	public Action doAction() throws CannotPerformActionException {
		return this.getModel().getStartingAction();
	}

	/**
	 * The setup of this action requires the display interaction containing the content to display
	 * @param toDisplay is the display interaction containing the content that the action should display
	 */
	public void setup(DisplayInteraction<T> toDisplay){
		this.setInteraction(toDisplay);
	}

}
