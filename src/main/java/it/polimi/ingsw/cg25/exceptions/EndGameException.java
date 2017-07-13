package it.polimi.ingsw.cg25.exceptions;

public class EndGameException extends Exception {
	
	/**
	 * Serial number for serial communication
	 */
	private static final long serialVersionUID = 943321506494963819L;

	/**
	 * EndGameException class constructor
	 */
	public EndGameException() {
		super();
	}

	/**
	 * EndGameException class constructor
	 * @param message a custom message for this exception
	 */
	public EndGameException(String message) {
		super(message);
	}
}
