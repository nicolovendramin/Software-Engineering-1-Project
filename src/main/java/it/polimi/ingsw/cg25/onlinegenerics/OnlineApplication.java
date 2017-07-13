package it.polimi.ingsw.cg25.onlinegenerics;

/**
 * An online application is an appllication with users identified by an Id
 * @author nicolo
 *
 */
public interface OnlineApplication extends Applet{

	/**
	 * Connects the user to the online application.
	 * @param id the id of the user
	 */
	public void connect(int id);
	
	/**
	 * Disconnects the user from the online application.
	 * @param id the id of the user
	 * @param report the cause of the disconnection
	 * @throws IllegalArgumentException when the id is not known
	 */
	public void disconnect(int id,String report); 
	
	/**
	 * Writes a message in the public chat 
	 * @param message
	 */
	public void chat(String message);
	
	/**
	 * Resolves an id into a UserName
	 * @param id is the id of the player of which you want to get the Username
	 * @return the username of the player with that id
	 * @throws IllegalArgumentException when the id is not known by the online application
	 */
	public String addressBook(int id);
	
	/**
	 * Resolves a name into an user id
	 * @param name the username of the player
	 * @return the id corresponding to that player
	 * @throws IllegalArgumentException when the name is not known.
	 */
	public int nameService(String name);
	
	/**
	 * Changes the username of the user with the indicated id
	 * @param id the id of the user which has produced the request
	 * @param newName the new name to set to the player
	 */
	public void changeUsername(int id,String newName);
	
	/**
	 * Adds a new user to the online application
	 * @param e the user that you want to add
	 * @Throws NullPointerException if the user is null
	 */
	public void addUser(User e);
}
