package it.polimi.ingsw.cg25.exceptions;

/**
 * 
 * @author Giovanni
 *
 */
public class OneEmporiumOnlyException extends Exception {

	/**
	 * Serial number for serial communication
	 */
	private static final long serialVersionUID = -3354062925117505951L;

	/**
	 * OneEmporiumOnly class constructor
	 */
	public OneEmporiumOnlyException() {
		super("You cannot build more than one emporium!");
	}
	
	/**
	 * OneEmporiumOnlyException class constructor
	 * @param s a custom message for this exception
	 */
	public OneEmporiumOnlyException(String s) {
		super(s);
	}
	
}
