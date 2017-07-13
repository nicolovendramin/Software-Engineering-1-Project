package it.polimi.ingsw.cg25.exceptions;

/**
 * 
 * @author Giovanni
 *
 */
public class NoCardsException extends Exception {
	
	/**
	 * Serial number for serial communication
	 */
	private static final long serialVersionUID = -5447650082359818202L;

	/**
	 * NoCardException class constructor
	 */
	public NoCardsException() {
		super("No cards are left.");
	}
	
	/**
	 * NoCardsException class constructor
	 * @param s a custom message for this exception
	 */
	public NoCardsException(String s) {
		super(s);
	}
}
