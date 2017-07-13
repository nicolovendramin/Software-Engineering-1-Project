/**
 * 
 */
package it.polimi.ingsw.cg25.exceptions;

/**
 * @author Davide
 *
 */
public class CannotCreateGameException extends Exception {

	/**
	 * Serial number for serial communication
	 */
	private static final long serialVersionUID = -3704250491963753103L;
	
	/**
	 * CannotCreateGameException class constructor
	 */
	public CannotCreateGameException() {
		super("Game's creation failed.");
	}
	
	/**
	 * CannotCreateGameException class constructor
	 * @param message a custom message for this exception
	 */
	public CannotCreateGameException(String s) {
		super(s);
	}
	
	/**
	 * CannotCreateGameException class constructor
	 * @param message a custom message for this exception
	 * @param e the cause of this exception
	 */
	public CannotCreateGameException(String s, Throwable cause) {
		super(s,cause);
	}
	
}
