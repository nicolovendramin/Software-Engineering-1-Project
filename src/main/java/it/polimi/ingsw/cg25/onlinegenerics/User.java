/**
 * 
 */
package it.polimi.ingsw.cg25.onlinegenerics;

/**
 * @author Davide
 *
 */
public class User implements OnlineUser {

	/**
	 * Status of the player (acrive or inactive)
	 */
	private boolean active;
	
	/**
	 * User Identifier
	 */
	private final int userID;
	
	/**
	 * Name of the user
	 */
	private String name;
	
	/**
	 * Constructor for User
	 * @param viewID int the view identifier
	 * @param name String the name of the user
	 */
	public User(int viewID,String name) {
		this.userID = viewID;
		this.active = true;
		this.name = name;
	}

	@Override
	public void setStatus(boolean active) {
		this.active = active;

	}

	@Override
	public int getUserID() {
		return userID;
	}

	@Override
	public boolean isActive() {
		return this.active;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [userID=" + userID + ", active=" + active + "]" ;
	}

	/**
	 * 
	 * @return String the name of the user
	 */
	public String getName(){
		return this.name;
	}

	/**
	 * Changes the name of the user
	 * @param newName String new user name
	 */
	public void rename(String newName) {
		this.name = newName;
	}
}
