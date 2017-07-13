package it.polimi.ingsw.cg25.exceptions;

/**
 * 
 * @author Giovanni
 *
 */
public class NotEnoughAssistantsException extends Exception {

	/**
	 * Serial number for serial communication
	 */
	private static final long serialVersionUID = 7321107074383307580L;

	/**
	 * NotEnoughAssistantsException class constructor
	 */
	public NotEnoughAssistantsException() {
		super("You don't have enough assistants!");
	}
	
	/**
	 * NotEnoughAssistantsException class constructor
	 * @param s a custom message for this exception
	 */
	public NotEnoughAssistantsException(String s) {
		super(s);
	}
	
}
