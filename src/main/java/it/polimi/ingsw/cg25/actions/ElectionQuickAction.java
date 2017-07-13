package it.polimi.ingsw.cg25.actions;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.exceptions.NotEnoughAssistantsException;
import it.polimi.ingsw.cg25.model.Assistant;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.dashboard.Councelor;
import it.polimi.ingsw.cg25.model.dashboard.Council;
import it.polimi.ingsw.cg25.model.dto.DTOCouncelor;
import it.polimi.ingsw.cg25.model.dto.DTOCouncil;
/**
 * 
 * @author nicolo
 *
 */
public class ElectionQuickAction extends QuickAction{

	/**
	 * The interaction for the choice of the Councelor to be elected
	 */
	private SingleChoiceInteraction<DTOCouncelor> dtoCouncelor;
	/**
	 * The interaction for the choice of the Council where to elect him
	 */
	private SingleChoiceInteraction<DTOCouncil> dtoCouncil;
	/**
	 * The list of candidate councils use for the decode of the choice
	 */
	private List<Council> candidates;
	
	public ElectionQuickAction(MatchCD4 model) {
		super(model);
	}

	/**
	 * Executes the action on the given model
	 * @return the next action to be sent to the controller
	 * @throws CannotPerformActionException 
	 */
	@Override
	public Action doAction() throws CannotPerformActionException {
		Councelor councelor;
		Council council;
		if(this.getModel().getRemainingQuickActions()<1)
			throw new CannotPerformActionException("You can do no more quick actions for this turn");
		try {
			councelor = this.dtoCouncelor.getReply().decode(getModel().getBoard().getUnemployedCouncelors());
			council = this.dtoCouncil.getReply().decode(candidates);
		} catch (ElementNotFoundException e1) {
			this.getModel().getLogger().log(e1);
			throw new CannotPerformActionException("Something with you choice went wrong");
		}
		try{
			this.getModel().getCurrentPlayer().getPocket().subPocketable(new Assistant(1));
		}
		catch(NotEnoughAssistantsException e)
		{
			this.getModel().getLogger().log(e);
			throw  new CannotPerformActionException("You can not perform this action. Not enough assistants");
		}
		try{
			this.getModel().getBoard().getUnemployedCouncelors().add(council.electCouncelor(councelor));
			this.getModel().getBoard().getUnemployedCouncelors().remove(councelor);
			this.getModel().useQuickAction();
		}
		catch(CannotPerformActionException e){
			this.getModel().getLogger().log(e);
			this.getModel().getCurrentPlayer().getPocket().addPocketable(new Assistant(1));
			throw new CannotPerformActionException("You can not do other quick actions");
		}
		if(this.getModel().actionStackIsEmpty())	
			return new ChooseActionTypeAction<MatchCD4>(this.getModel());
		return this.getModel().popAction();
	}
	
	/**
	 * Returns a string representation of the action
	 */
	@Override
	public String toString()
	{
		return "Pay an assistant to elect a councelor in one of the councils";
	}

	/**
	 * Sets up the right interactions for the current action. CAn override previous setups
	 * @param councelors are the unemployed councelors between which to choose
	 * @param councils are the possible destination councils of the election
	 */
	public void setup(List<Councelor> councelors,List<Council> councils)
	{
		this.getInteractions().clear();
		this.candidates = councils;
		this.dtoCouncelor = new SingleChoiceInteraction<>("Choose the Councelor that you want to elect",DTOCouncelor.convertAll(councelors));
		this.setInteraction(dtoCouncelor);
		this.dtoCouncil = new SingleChoiceInteraction<>("Choose the council where you want your councelor to be elected",DTOCouncil.convertAll(councils));
		this.setInteraction(dtoCouncil);
	}
	
	/**
	 * default implementation for setup. Can never override previous setups
	 */
	@Override
	public void setup(){
		if(!this.getInteractions().isEmpty())
			return;
		ArrayList<Council> councils = new ArrayList<>();
		councils.add(this.getModel().getBoard().getKingCouncil());
		for(int i=0;i<this.getModel().getBoard().getRegions().size();i++)
			councils.add(this.getModel().getBoard().getRegions().get(i).getCouncil());
		setup(this.getModel().getBoard().getUnemployedCouncelors(),councils);
	}
}
