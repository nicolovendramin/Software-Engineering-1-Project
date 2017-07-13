package it.polimi.ingsw.cg25.actions;

import java.util.List;

import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.CannotSetupActionException;
import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.model.dashboard.cards.PoliticsCard;
import it.polimi.ingsw.cg25.model.dto.DTOPoliticsCard;
import it.polimi.ingsw.cg25.model.trade.Market;
import it.polimi.ingsw.cg25.model.trade.ProductPoliticsCard;
/**
 * 
 * @author nicolo
 *
 */
public class SellPolitics extends SellingAction{

	/**
	 * The interaction for the choice of the politics cards to sell
	 */
	private MultipleChoiceInteraction<DTOPoliticsCard> chosen;
	
	/**
	 * Constructor for the action
	 * @param model the model of the action
	 */
	public SellPolitics(Market model) {
		super(model);
	}


	@Override
	public Action doAction() throws CannotPerformActionException {
		try{
			List<PoliticsCard> chosenList = DTOPoliticsCard.decodeAll(chosen.getReply(),this.getModel().getCurrentPlayer().getPoliticsCards());
			for(PoliticsCard p:chosenList){
				this.getModel().getCurrentPlayer().getPoliticsCards().remove(p);
			}
			//AGGIUNTO DA GIO
			this.getModel().addProduct(new ProductPoliticsCard(super.getPrice(), this.getModel().getCurrentPlayer(), chosenList, this.getModel().getNextProductTag()));
		}
		catch(ElementNotFoundException e){
			throw new CannotPerformActionException("The player cannot sell cards that he doesn't have",e);
		}
		catch(NullPointerException e){
			throw new CannotPerformActionException("You must fill the interactions before using the replies",e);
		}
		return new ChooseActionTypeAction<>(this.getModel());
	}
	
	/**
	 * This setup method initialize the action explicitely with the list of politics cards that the
	 * player can sell
	 * @param canBeSold is the List of politics cards that the player can sell
	 * @throws CannotSetupActionException if the list is empty
	 */
	public void setup(List<PoliticsCard> canBeSold) throws CannotSetupActionException{
		if(canBeSold == null)
			throw new CannotSetupActionException("You don't have any permit to sell");
		try{
			this.chosen = new MultipleChoiceInteraction<>("Choose the following card you want to sell. "
				+ "All the one you choose in this action will be considered as an unique product"
				,DTOPoliticsCard.convertAll(canBeSold),0,this.getModel().getCurrentPlayer().getPoliticsCards().size());}
		catch(IllegalArgumentException e){
			throw new CannotSetupActionException("You don't have any Politics card to sell",e);
		}
		this.setInteraction(chosen);
		super.setup();
	}
	
	@Override
	public void setup() throws CannotSetupActionException{
		this.setup(this.getModel().getCurrentPlayer().getPoliticsCards());
	}
	
	@Override
	public String toString(){
		return "Sell some of your politics card";
	}
	
}
