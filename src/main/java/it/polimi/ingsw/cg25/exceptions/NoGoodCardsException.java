package it.polimi.ingsw.cg25.exceptions;

/**
 * 
 * @author Giovanni
 *
 */
public class NoGoodCardsException extends Exception {

	/**
	 * Serial number for serial communication
	 */
	private static final long serialVersionUID = 3181399331519060949L;

	/**
	 * NoGoodCardsException class constructor
	 */
	public NoGoodCardsException() {
		super("You cannot satisfy the council!");
	}
	
	/**
	 * NoGoodCardsException class constructor
	 * @param s a custom message for this exception
	 */
	public NoGoodCardsException(String s) {
		super(s);
	}
	
}
