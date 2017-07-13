package it.polimi.ingsw.cg25.actions;
import it.polimi.ingsw.cg25.exceptions.CannotSetupActionException;
import it.polimi.ingsw.cg25.model.trade.Market;
/**
 * 
 * @author nicolo
 *
 */
public abstract class SellingAction extends MarketAction {

	/**
	 * The interaction to choose the price of the product. Is common to all the SellingAction
	 */
	private AskQuantityInteraction price;
	
	/**
	 * Constructor for the SellingAction
	 * @param model the market on which the selling action will be executed
	 */
	public SellingAction(Market model) {
		super(model);
	}
	
	/**
	 * Getter for the price 
	 * @return the price chosen for the Product
	 */
	public int getPrice(){
		return this.price.getReply();
	}
	
	@Override
	public void setup() throws CannotSetupActionException{
		this.price = new AskQuantityInteraction();
		this.getInteractions().add(price);
	}
}
