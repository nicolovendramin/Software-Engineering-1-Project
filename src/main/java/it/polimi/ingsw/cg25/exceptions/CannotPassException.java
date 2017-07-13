/**
 * 
 */
package it.polimi.ingsw.cg25.exceptions;

/**
 * @author Davide
 *
 */
public class CannotPassException extends Exception {

	/**
	 * Serial number for serial communication
	 */
	private static final long serialVersionUID = 1130608672460138516L;

	/**
	 * CannotPassException class constructor
	 */
	public CannotPassException() {
		super("You shall not pass!");
	}
	
	/**
	 * CannotPassException class constructor
	 * @param s a message for this exception
	 */
	public CannotPassException(String s) {
		super(s);
	}

}
