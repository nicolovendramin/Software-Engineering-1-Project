package it.polimi.ingsw.cg25.gui;

import it.polimi.ingsw.cg25.model.dto.DTO;

/**
 * 
 * @author Giovanni
 *
 * @param <T> something which extends DTO
 */
public interface Updatable<T extends DTO> {

	/**
	 * This method is paramount important because it can be overrided
	 * by those classes that need to update their components from
	 * particulars objects contained in a game status
	 * @param object the object of a game status used to update the gui.
	 * It can be the GameStatus itself
	 */
	public void update(T object);
}
