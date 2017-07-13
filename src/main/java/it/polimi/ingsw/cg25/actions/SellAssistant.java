package it.polimi.ingsw.cg25.actions;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.CannotSetupActionException;
import it.polimi.ingsw.cg25.exceptions.NotEnoughAssistantsException;
import it.polimi.ingsw.cg25.model.Assistant;
import it.polimi.ingsw.cg25.model.dashboard.bonus.AssistantBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonus;
import it.polimi.ingsw.cg25.model.trade.Market;
import it.polimi.ingsw.cg25.model.trade.ProductBonus;
/**
 * 
 * @author nicolo
 *
 */
public class SellAssistant extends SellingAction{

	/**
	 * The interaction for the choice of the number of assistants to sell
	 */
	private AskQuantityInteraction numberOfAssistant;
	
	/**
	 * Simple constructor for the Action
	 * @param model is the reference to the market to which the action has to be applied
	 */
	public SellAssistant(Market model) {
		super(model);
	}

	@Override
	public Action doAction() throws CannotPerformActionException {
		try {
			this.getModel().getCurrentPlayer().getPocket().subPocketable(new Assistant(numberOfAssistant.getReply()));
		} catch (NotEnoughAssistantsException e) {
			this.getModel().getLogger().log(e);
			throw new CannotPerformActionException("You cannot sell more assistants than the one you have in your pocket");
		}
		//AGGIUNTO DA GIO
		List<Bonus> assistantsToSell = new ArrayList<>();
		assistantsToSell.add(new AssistantBonus(numberOfAssistant.getReply()));
		this.getModel().addProduct(new ProductBonus(super.getPrice(), 
				this.getModel().getCurrentPlayer(), assistantsToSell, this.getModel().getNextProductTag()));
		
		return new ChooseActionTypeAction<>(this.getModel());
	}

	/**
	 * The explicit setup for the action
	 * @param numberOfAssistants is the max number of assistans that the player can sell
	 * @throws CannotSetupActionException if this number is 0
	 */
	public void setup(int numberOfAssistants) throws CannotSetupActionException{
		try{
			this.numberOfAssistant = new AskQuantityInteraction("Write how many assistants you want to sell",numberOfAssistants);
		}
		catch(IllegalArgumentException e){
			this.getModel().getLogger().log(e);
			throw new CannotSetupActionException("You don't have any Assistant to sell",e);
		}
		super.setInteraction(numberOfAssistant);
		super.setup();
	}
	
	@Override
	public void setup() throws CannotSetupActionException{
		if(this.getInteractions().size()!=0)
			return;
		this.setup(this.getModel().getCurrentPlayer().getPocket().getAssistants().getSupply());
	}

	@Override
	public String toString(){
		return "Sell some of your assistants";
	}
}
