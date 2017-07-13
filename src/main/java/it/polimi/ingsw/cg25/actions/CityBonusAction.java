package it.polimi.ingsw.cg25.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.CannotSetupActionException;
import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.dashboard.topological.City;
import it.polimi.ingsw.cg25.model.dto.DTOCity;
/**
 * 
 * @author nicolo
 *
 */
public class CityBonusAction extends MatchAction {
	/**
	 * The interaction for the choice of the city from which to take the bonus
	 */
	private MultipleChoiceInteraction<DTOCity> dtoCity;
	
	public CityBonusAction(MatchCD4 model) {
		super(model);
	}

	/**
	 * Executes the action on the given model
	 * @return the next action to be performed
	 * @throws CannotPerformActionException if there is any problem in the performing of the action
	 */
	@Override
	public Action doAction() throws CannotPerformActionException {
		Iterator<DTOCity> i = dtoCity.getReply().iterator();
		while(i.hasNext()){
			City c;
			try {
				c = i.next().decode(this.getModel().getBoard().getCities());
			} catch (ElementNotFoundException e) {
				this.getModel().getLogger().log(e);
				throw new CannotPerformActionException("Something went wrong with the cities you chose");
			}
			c.takeAllBonuses(this.getModel().getCurrentPlayer());
		}
		if(this.getModel().actionStackIsEmpty()) 
			return new ChooseActionTypeAction<MatchCD4>(this.getModel());
		return this.getModel().popAction();
	}
	
	/**
	 * this implements the specific setup for the action. Can override previous setups
	 * @param options the cities among which the player must choose
	 * @param bonusSize the number of cities to be chosen
	 * @throws CannotSetupActionException 
	 */
	public void setup(List<City> options, int bonusSize) throws CannotSetupActionException {
		this.getInteractions().clear();
		if(options.isEmpty()) 
			throw new CannotSetupActionException("You can't use the bonus because you have no emporiums!");
		this.dtoCity = new MultipleChoiceInteraction<>("Choose the cities with the bonus you want to achieve", DTOCity.convertAll(options),bonusSize,bonusSize);
		this.setInteraction(dtoCity);
	}
	
	/**
	 * This is the default setup which initialize the interaction with 1 single choice between the cities where he has built an emporium
	 * @throws CannotSetupActionException 
	 */
	@Override
	public void setup() throws CannotSetupActionException{
		if(!this.getInteractions().isEmpty())
			return;
		List<City> temp = new ArrayList<>();
		//All the cities where the player has an emporium
		for(int i = 0; i < this.getModel().getCurrentPlayer().getPlayerEmporiums().size(); i++) {
			temp.add(this.getModel().getCurrentPlayer().getPlayerEmporiums().get(i).getCity());
		}
		this.setup(temp, 1);
	}
	
	
}
