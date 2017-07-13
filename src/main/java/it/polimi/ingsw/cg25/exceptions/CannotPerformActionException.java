package it.polimi.ingsw.cg25.exceptions;

public class CannotPerformActionException extends Exception {

	/**
	 * Serial number for serial communication
	 */
	private static final long serialVersionUID = 7750031430905346057L;

	/**
	 * CannotPerformActionException class constructor
	 */
	public CannotPerformActionException() {
		super("The action couldn't be performed. Try again.");
	}
	
	/**
	 * CannotPerformActionException class constructor
	 * @param message a custom message for this exception
	 */
	public CannotPerformActionException(String s) {
		super(s);
	}

	/**
	 * CannotPerformActionException class constructor
	 * @param message a custom message for this exception
	 * @param e the cause of this exception
	 */
	public CannotPerformActionException(String message, Throwable cause) {
		super(message,cause);
	}
	
}
