package it.polimi.ingsw.cg25.exceptions;

/**
 * 
 * @author Giovanni
 *
 */
public class NotEnoughCoinException extends Exception {

	/**
	 * Serial number for serial communication
	 */
	private static final long serialVersionUID = -7490821207257999075L;

	/**
	 * NotEnoughCoinException class constructor
	 */
	public NotEnoughCoinException() {
		super("Bounced payment!");
	}
	
	/**
	 * NotEnoughCoinException class constructor
	 * @param s a custom message for this exception
	 */
	public NotEnoughCoinException(String s) {
		super(s);
	}
	
}
