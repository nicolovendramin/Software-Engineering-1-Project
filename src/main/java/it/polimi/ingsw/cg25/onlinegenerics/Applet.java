package it.polimi.ingsw.cg25.onlinegenerics;

import java.io.Serializable;

import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
/**
 * This interface should be implemented by all those applications who can be considered
 * as an inner part of another bigger application.
 * @author nicolo
 *
 */
public interface Applet {
	
	/**
	 * Checks whether the applet is running or not
	 * @return TRUE if the applet is running FALSE otherwise
	 */
	public boolean isRunning();
	
	/**
	 * This method is called to ask the applet to return the representation of it's status
	 * @param id the id of the requesting view
	 * @return the Serializable representation of the status of the applet
	 * @throws ElementNotFoundException
	 */
	public Serializable getStatus(int id) throws ElementNotFoundException;
	
}
