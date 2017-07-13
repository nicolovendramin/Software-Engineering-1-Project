package it.polimi.ingsw.cg25.actions;

import it.polimi.ingsw.cg25.model.trade.Market;

/**
 * 
 * @author nicolo
 *
 */
public abstract class MarketAction extends Action {

	/**
	 * The model of the action
	 */
	private Market model;
	
	/**
	 * Initialize the model of the action
	 * @param model is the model the action relates to
	 */
	public MarketAction(Market model){
		if(model==null)	
			throw new IllegalArgumentException("The market reference cannot be null");
		this.model = model;
	}
	
	@Override
	public Market getModel() {
		return this.model;
	}

}
