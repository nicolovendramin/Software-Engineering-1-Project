package it.polimi.ingsw.cg25.actions;

import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.gamegenerics.ActionBasedGame;
/**
 * 
 * @author nicolo
 *
 * @param <T> the type of the model. Must be an ActionBasedGame
 */
public class ErrorAction<T extends ActionBasedGame> extends ControlAction<T>{
	
	/**
	 * This DisplayInteraction object carries the message related to the error
	 */
	private DisplayInteraction<String> message;
	
	/**
	 * ErrorAction class constructor
	 * @param model an object whose type is T which represents
	 * the game model (e.g.: MatchCD4 and all its contents)
	 */
	public ErrorAction(T model) {
		super(model);
	}

	@Override
	public Action doAction() throws CannotPerformActionException {
		return new ChooseActionTypeAction<T>(this.getModel());
	}

	/**
	 * Default implementation for setup. Can not override previous setups
	 */
	public void setup(){
		if(!this.getInteractions().isEmpty()) 
			return;
		this.message = new DisplayInteraction<>("Unfortunately an error occurred. Retry\nSend [Action]-anyKey- to go on");
		this.setInteraction(message);
	}
	
	/**
	 * This can override previous configurations
	 * @param message to be shown in the interaction
	 */
	public void setup(String message){
		this.getInteractions().clear();
		this.message = new DisplayInteraction<>(message.concat("\nSend [Action]-anyKey- to go on"));
		this.setInteraction(this.message);
	}

}
