package it.polimi.ingsw.cg25.actions;


import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.CannotSetupActionException;
import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.exceptions.OneEmporiumOnlyException;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.dashboard.cards.PermitCard;
import it.polimi.ingsw.cg25.model.dashboard.topological.City;
import it.polimi.ingsw.cg25.model.dto.DTOCity;
import it.polimi.ingsw.cg25.model.dto.DTOPermitCard;
/**
 * 
 * @author nicolo
 *
 */
public class BuildWithPermitAction extends MainAction{

	/**
	 * The interaction for the choice of the PermitCard to use
	 */
	private SingleChoiceInteraction<DTOPermitCard> dtoCards;
	/**
	 * The interaction for the choice of the City where to build
	 */
	private SingleChoiceInteraction<DTOCity> dtoCity;
	
	/**
	 * Simple constructor.Just calls the super constructor of MainAction
	 * @param model is the MatchCD4 on which the action will be applied
	 */
	public BuildWithPermitAction(MatchCD4 model) {
		super(model);
	}

	/**
	 * Executes the action on the specified model
	 * @return the next action that the controller has to specify
	 * @throws CannotPerformActionException when there is an error in the execution of the action
	 */
	@Override
	public Action doAction() throws CannotPerformActionException{
		PermitCard p = null;
		try {
			try{
				p = dtoCards.getReply().decode(this.getModel().getCurrentPlayer().getPermitsToBeUsed());
			}catch(Exception e)
			{
				this.getModel().getLogger().log(e);
				throw new CannotPerformActionException("The permits chosen are not valid");
			}
			City city = this.dtoCity.getReply().decode(this.getModel().getBoard().getCities());
			if(p.isValid(city))
			{
				try{
					this.getModel().getCurrentPlayer().buildEmporium(city);
					this.getModel().getCurrentPlayer().getPermitsToBeUsed().remove(p);
					this.getModel().getCurrentPlayer().getUsedPermits().add(p);
				}
				catch(OneEmporiumOnlyException e){
					throw new IllegalArgumentException("You can not build two emporiums in the same city",e);
				}
			}
			else
				throw new IllegalArgumentException("You can not build in the selected city");
		} catch (ElementNotFoundException e1) {

			throw new CannotPerformActionException("Something went wrong with your selection",e1);
		}
		this.getModel().useMainAction();
		if(this.getModel().actionStackIsEmpty()) 
			return new ChooseActionTypeAction<>(this.getModel());
		return this.getModel().popAction();
	}
	
	/**
	 * This is the custom setup. Can override previous setups
	 * @param options is the set of Permit that can be used in the hand of the player
	 * @param cities is the whole set of cities between which to choose
	 * @throws CannotSetupActionException if one of the argument is null or one of the chosing pools are empty
	 */
	public void setup(List<PermitCard> options,List<City> cities) throws CannotSetupActionException{
		this.getInteractions().clear();
		if(options == null || cities == null)
			throw new CannotSetupActionException("You cannot choose neither if you have no permits nor if there are no cities");
		try{
		this.dtoCards = new SingleChoiceInteraction<>("Choose the permit to use",DTOPermitCard.convertAll(options));
		this.dtoCity = new SingleChoiceInteraction<>(DTOCity.convertAll(cities));}
		catch(IllegalArgumentException e){
			throw new CannotSetupActionException("You cannot make this action",e);
		}
		this.setInteraction(this.dtoCards);
		this.setInteraction(this.dtoCity);
	}

	/**
	 * default implementation for setup. Cannot override previously done setups.
	 * @throws CannotSetupActionException if the player has no permits to be used or the board has no cities
	 */
	@Override
	public void setup() throws CannotSetupActionException
	{
		if(!this.getInteractions().isEmpty()) 
			return;
		this.setup(this.getModel().getCurrentPlayer().getPermitsToBeUsed(), this.getModel().getBoard().getCities());
	}
	/**
	 * Returns a String representation of the action
	 */
	@Override
	public String toString()
	{
		return "Use one of the Permits you have in your hand to build an Emporium";
	}
	
	
	/**
	 * Overrides the previous implementation of next to implement the filtering logic between what the user
	 * has chosen for the Permit interaction and the possible cities where he can choose to build
	 * @return the next interaction updated using the filter
	 */
	@Override
	public Interaction next(){
		Interaction i = super.next();
		if(i==dtoCity)
		{
			List<DTOCity> allowedCities = new ArrayList<>(this.dtoCards.getReply().getCities());
			this.dtoCity = new SingleChoiceInteraction<>(dtoCity, c -> allowedCities.contains(c));
			int index = this.getInteractions().indexOf(i);
			this.getInteractions().remove(i);
			this.getInteractions().add(index,this.dtoCity);
			return dtoCity;
		}
		else 
			return i;
	}
}
