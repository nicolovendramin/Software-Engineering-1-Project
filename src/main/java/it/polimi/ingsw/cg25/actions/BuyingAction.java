package it.polimi.ingsw.cg25.actions;

import java.util.List;

import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.CannotSetupActionException;
import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.exceptions.NotEnoughCoinException;
import it.polimi.ingsw.cg25.model.Coin;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOProduct;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOSellable;
import it.polimi.ingsw.cg25.model.trade.Market;
import it.polimi.ingsw.cg25.model.trade.Product;
import it.polimi.ingsw.cg25.model.trade.Sellable;
/**
 * 
 * @author nicolo
 *
 */
public class BuyingAction extends MarketAction {

	/**
	 * The interaction for the choice of the products to buy
	 */
	private MultipleChoiceInteraction<DTOProduct<? extends DTOSellable>> productsToBeBought;
	/**
	 * The list of Product on which apply the decode function
	 */
	private List<Product<? extends Sellable>> candidates;
	
	/**
	 * Just a constructor
	 * @param model
	 */
	public BuyingAction(Market model) {
		super(model);
	}

	@Override
	public Action doAction() throws CannotPerformActionException {
		try {
			List<Product<? extends Sellable>> products = DTOProduct.decodeAll(productsToBeBought.getReply(),candidates);
			int sum = products.stream().mapToInt(p -> p.getPrice()).sum();
			try{
				// Tries to remove the price from the buyer
				this.getModel().getCurrentPlayer().getPocket().subPocketable(new Coin(sum));
			}catch(NotEnoughCoinException e){
				throw new CannotPerformActionException("You don't have enough money to buy those products",e);
			}
			// Gives the price to the seller
			products.stream().forEach(p -> p.getOwner().getPocket().addPocketable(new Coin(p.getPrice())));
			// Gives items to buyer
			products.stream().forEach(p -> p.achieveProduct(this.getModel().getCurrentPlayer()));
			// Removes bought products from available products
			this.getModel().getProducts().removeAll(products);
		} catch (ElementNotFoundException e) {
			throw new CannotPerformActionException("Couldn't fin the product you are referring to.",e);
		}
		return this.getModel().getStartingAction();
	}
	
	/**
	 * The esplicit setup for the action
	 * @param products are the available products on the market
	 * @throws CannotSetupActionException if there are no available products on the market
	 */
	public void setup(List<Product<? extends Sellable>> products) throws CannotSetupActionException{
		if(products == null || products.size() == 0)
			throw new CannotSetupActionException("No products to sell in the market");
		candidates = products;
		this.productsToBeBought = new MultipleChoiceInteraction<>("Choose which products you would like to buy",DTOProduct.convertAll(products));
		this.setInteraction(productsToBeBought);
	}
	
	@Override
	public void setup() throws CannotSetupActionException{
		if(this.getInteractions().size()!=0)
			return;
		this.setup(this.getModel().getProducts());
	}

	@Override
	public String toString(){
		return "Buy some of the products available on the market.";
	}
}
