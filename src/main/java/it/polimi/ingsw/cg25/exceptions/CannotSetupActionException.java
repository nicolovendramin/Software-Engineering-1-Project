package it.polimi.ingsw.cg25.exceptions;

public class CannotSetupActionException extends Exception{

	/**
	 * Serial number for serial communication
	 */
	private static final long serialVersionUID = 8556072326819716613L;

	/**
	 * CannotSetupActionException class constructor
	 */
	public CannotSetupActionException() {
		super("There was an error setting up this Action. Try again");
	}
	
	/**
	 * CannotSetupActionException class constructor
	 * @param s a custom message for this exception
	 */
	public CannotSetupActionException(String s) {
		super(s);
	}

	/**
	 * CannotSetupActionException class constructor
	 * @param message a custom message for this exception
	 * @param e the cause of this exception
	 */
	public CannotSetupActionException(String message, Throwable e) {
		super(message,e);
	}
}
