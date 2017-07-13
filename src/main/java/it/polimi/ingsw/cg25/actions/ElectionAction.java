package it.polimi.ingsw.cg25.actions;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.model.Coin;
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
public class ElectionAction extends MainAction{

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
	/**
	 * 
	 * @param model is the game-model to which the action is referred.
	 */
	public ElectionAction(MatchCD4 model) {
		super(model);
	}

	/**
	 * This method executes the current action on the model using the parameters registered in the interactions
	 * @return the next action to be notified to the controllerCD4
	 * @throws CannotPerformActionException 
	 */
	@Override
	public Action doAction() throws CannotPerformActionException{
		this.getModel().getCurrentPlayer().getPocket().addPocketable(new Coin(4));
		Council council;
		Councelor c;
		try {
			council = this.dtoCouncil.getReply().decode(candidates);
			c = this.dtoCouncelor.getReply().decode(this.getModel().getBoard().getUnemployedCouncelors());
		} catch (ElementNotFoundException e) {
			this.getModel().getLogger().log(e);
			throw new CannotPerformActionException("Something went wrong with your selection");
		}
		this.getModel().getBoard().getUnemployedCouncelors().remove(c);
		this.getModel().getBoard().getUnemployedCouncelors().add(council.electCouncelor(c));
		this.getModel().useMainAction();
		if(this.getModel().actionStackIsEmpty())
			return new ChooseActionTypeAction<>(this.getModel());
		return this.getModel().popAction();
	}
	
	/**
	 * returns the string representation of the Action, the action description
	 */
	@Override
	public String toString()
	{
		return "Elect a councelor in one of the councils and get 4 coins";
	}

	/**
	 * this method is meant to initialize correctly the Interaction needed to perform the current action.Can override previous settings of the action
	 * @param councelor is the set of unemployed councelors between which the user has to choose
	 * @param councils is the complete list of all available councils
	 */
	public void setup(List<Councelor> councelor,List<Council> councils)
	{
		this.getInteractions().clear();
		this.candidates = councils;
		this.dtoCouncelor = new SingleChoiceInteraction<>("Choose the Councelor that you want to elect",DTOCouncelor.convertAll(councelor));
		this.dtoCouncil = new SingleChoiceInteraction<>("Choose the council where you want your councelor to be elected",DTOCouncil.convertAll(councils));
		this.setInteraction(this.dtoCouncelor);
		this.setInteraction(this.dtoCouncil);
	}
	
	/**
	 * Sets the action up with default options.Can not override previous setups
	 */
	@Override
	public void setup()
	{
		if(!this.getInteractions().isEmpty())
			return;
		ArrayList<Council> councils = new ArrayList<>();
		councils.add(this.getModel().getBoard().getKingCouncil());
		for(int i=0;i<this.getModel().getBoard().getRegions().size();i++)
			councils.add(this.getModel().getBoard().getRegions().get(i).getCouncil());
		setup(this.getModel().getBoard().getUnemployedCouncelors(),councils);
	}
}
