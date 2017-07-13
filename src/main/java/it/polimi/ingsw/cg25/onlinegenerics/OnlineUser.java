/**
 * 
 */
package it.polimi.ingsw.cg25.onlinegenerics;

/**
 * @author Davide
 *
 */
public interface OnlineUser {

	/**
	 * Set the player's connectivity status (Active, Inactive)
	 * 
	 * @param active
	 *            boolean true for active, false for inactive
	 */
	public void setStatus(boolean active);

	/**
	 * Checks if the player is active
	 * 
	 * @return boolean true if the player is active, false otherwise
	 */
	public boolean isActive();

	/**
	 * Gets the user identification number
	 * 
	 * @return int user's id
	 */
	public int getUserID();

}
