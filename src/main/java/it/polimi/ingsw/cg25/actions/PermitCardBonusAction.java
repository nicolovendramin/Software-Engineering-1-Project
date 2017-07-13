package it.polimi.ingsw.cg25.actions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.CannotSetupActionException;
import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.dashboard.cards.PermitCard;
import it.polimi.ingsw.cg25.model.dashboard.topological.Region;
import it.polimi.ingsw.cg25.model.dto.DTOPermitCard;
/**
 * 
 * @author nicolo
 *
 */
public class PermitCardBonusAction extends MatchAction{

	/**
	 * The interaction for the choice of the permit cards to draw
	 */
	private MultipleChoiceInteraction<DTOPermitCard> dtoCards;
	private List<PermitCard> candidates;
	public PermitCardBonusAction(MatchCD4 model) {
		super(model);
	}

	/**
	 * This doAction moves the selected permit to the hand of the player
	 * @return the following action to be passed to the controller
	 */
	@Override
	public Action doAction() throws CannotPerformActionException {
		List<Region> regions = this.getModel().getBoard().getRegions();
		List<PermitCard> cards;
		try {
			cards = DTOPermitCard.decodeAll(this.dtoCards.getReply(), candidates);
		} catch (ElementNotFoundException e1) {
			this.getModel().getLogger().log(e1);
			throw new CannotPerformActionException("Something went wrong with your selection");
		}
		try{
		for(int k=0;k<cards.size();k++){
			PermitCard pc = cards.get(k);
			for(int i=0;i<regions.size();i++){
				int j = regions.get(i).getFaceUpPermits().indexOf(pc);
				if(j>=0) 
				{
					this.getModel().getCurrentPlayer().getPermitsToBeUsed().add(regions.get(i).getPermit(j));
				}
			}
			Iterator<PermitCard> iterator = cards.iterator();
			while(iterator.hasNext()){
				iterator.next().takeAllBonuses(this.getModel().getCurrentPlayer());
			}
		}
		}catch(Exception e){
			this.getModel().getLogger().log(e);
		}
		if(this.getModel().actionStackIsEmpty()) 
			return new ChooseActionTypeAction<MatchCD4>(this.getModel());
		return this.getModel().popAction();
	}
	
	/**
	 * This is the custom setup which depends on the specific action. If called more than one time can ovverride previous changes
	 * @param numberOfCards the number of permit that the player wants to draw for free
	 * @param options the list of permits between which to choose
	 * @throws CannotSetupActionException when there are no more faced up permit cards on each region
	 */
	public void setup(int numberOfCards,List<PermitCard> options) throws CannotSetupActionException{
		if(options == null) 
			throw new CannotSetupActionException("You can't use the bonus because there are no available permits on the board");
		this.candidates = options;
		this.dtoCards = new MultipleChoiceInteraction<>("Choose which cards you want to draw for free",DTOPermitCard.convertAll(options),numberOfCards,numberOfCards);
		this.setInteraction(dtoCards);
	}
	
	/**
	 * This is the default implementation of the setup routine. If called after on an already set up action returns without modifications
	 * @throws CannotSetupActionException when there are no permits in any of the regions
	 */
	@Override
	public void setup() throws CannotSetupActionException{
		if(!this.getInteractions().isEmpty()) 
			return;
		List<PermitCard> temp = new ArrayList<>();
		List<Region> regions = this.getModel().getBoard().getRegions();
		for(int i=0;i<regions.size();i++){
			temp.addAll(regions.get(i).getFaceUpPermits());
		}
		if(temp.isEmpty()) 
			throw new CannotSetupActionException("No available permits on the board");
		this.setup(1,temp);
	}
}
