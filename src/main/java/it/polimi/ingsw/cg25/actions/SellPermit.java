package it.polimi.ingsw.cg25.actions;
import java.util.List;
import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.CannotSetupActionException;
import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.model.dashboard.cards.PermitCard;
import it.polimi.ingsw.cg25.model.dto.DTOPermitCard;
import it.polimi.ingsw.cg25.model.trade.Market;
import it.polimi.ingsw.cg25.model.trade.ProductPermitCard;
/**
 * 
 * @author nicolo
 *
 */
public class SellPermit extends SellingAction{

	/**
	 * The interaction for the choice of the permits cards to sell
	 */
	private MultipleChoiceInteraction<DTOPermitCard> chosen;
	
	/**
	 * Constructor for the action
	 * @param model the Market on which the action is executed
	 */
	public SellPermit(Market model) {
		super(model);
	}


	@Override
	public Action doAction() throws CannotPerformActionException {
		try{
			List<PermitCard> chosenList = DTOPermitCard.decodeAll(chosen.getReply(),this.getModel().getCurrentPlayer().getPermitsToBeUsed());
			for(PermitCard p:chosenList){
				this.getModel().getCurrentPlayer().getPermitsToBeUsed().remove(p);
			}
			//MODIFICATO DA GIO
			this.getModel().addProduct(new ProductPermitCard(super.getPrice(), this.getModel().getCurrentPlayer(), chosenList, this.getModel().getNextProductTag()));
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
	 * This setup method initialize the action explicitely with the list of permit cards that the
	 * player can sell
	 * @param canBeSold is the List of permit cards that the player can sell
	 * @throws CannotSetupActionException if the list is empty
	 */
	public void setup(List<PermitCard> canBeSold) throws CannotSetupActionException{
		if(canBeSold == null)
			throw new CannotSetupActionException("You don't have any permit to sell");
		try{
			this.chosen = new MultipleChoiceInteraction<>("Choose the following card you want to sell. "
				+ "All the one you choose in this action will be considered as an unique product"
				,DTOPermitCard.convertAll(canBeSold));}
		catch(IllegalArgumentException e){
			throw new CannotSetupActionException("You don't have any Permit card to sell",e);
		}
		this.setInteraction(this.chosen);
		super.setup();
	}
	
	@Override
	public void setup() throws CannotSetupActionException{
		this.setup(this.getModel().getCurrentPlayer().getPermitsToBeUsed());
	}
	
	@Override
	public String toString(){
		return "Sell some of your permits";
	}
	
	
}
