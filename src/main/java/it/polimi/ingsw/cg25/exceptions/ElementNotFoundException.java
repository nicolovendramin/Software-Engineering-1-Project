/**
 * 
 */
package it.polimi.ingsw.cg25.exceptions;

/**
 * @author Davide
 *
 */
public class ElementNotFoundException extends Exception {

	/**
	 * Serial number for serial communication
	 */
	private static final long serialVersionUID = -337799166109738583L;

	/**
	 * ElementNotFoundException class constructor
	 */
	public ElementNotFoundException() {
		super("Element not found");
	}

	/**
	 * ElementNotFoundException class constructor
	 * @param s a custom message for this exception
	 */
	public ElementNotFoundException(String s) {
		super(s);
	}
}
