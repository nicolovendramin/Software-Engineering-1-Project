package it.polimi.ingsw.cg25.observer;

public interface Observer<C> {
	
	/**
	 * Update function triggered by observed object
	 */
	public void update();
	
	/**
	 * Update function triggered by observed object
	 * @param change: the parameter passed by the observed object
	 */
	public void update(C change);
	
}
