package it.polimi.ingsw.cg25.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.CannotSetupActionException;
import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.exceptions.NoGoodCardsException;
import it.polimi.ingsw.cg25.exceptions.NotEnoughAssistantsException;
import it.polimi.ingsw.cg25.exceptions.NotEnoughCoinException;
import it.polimi.ingsw.cg25.exceptions.OneEmporiumOnlyException;
import it.polimi.ingsw.cg25.model.Assistant;
import it.polimi.ingsw.cg25.model.Coin;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.dashboard.cards.PoliticsCard;
import it.polimi.ingsw.cg25.model.dashboard.topological.City;
import it.polimi.ingsw.cg25.model.dto.DTOCity;
import it.polimi.ingsw.cg25.model.dto.DTOPoliticsCard;
/**
 * 
 * @author nicolo
 *
 */
public class BuildWithKingAction extends MainAction {
	/**
	 * The interaction for the choice of the PoliticsCards to be used to influence the king council
	 */
	private MultipleChoiceInteraction<DTOPoliticsCard> dtoCards;
	/**
	 * The interaction for the choice of the city where to build
	 */
	private SingleChoiceInteraction<DTOCity> dtoCity;
	
	/**
	 * Simple constructor for the actions. Doesn't initialize any Interaction
	 * @param model is the MatchCD4 on which the action has to be applied
	 */
	public BuildWithKingAction(MatchCD4 model) {
		super(model);
	}

	/**
	 * Executes the action on the specified model
	 * @throws CannotPerfomrActionException when the doAction fails
	 * @return the next action to be executed
	 */
	@Override
	public Action doAction() throws CannotPerformActionException{
		int money = 0;
		int assistants = 0;
		List<PoliticsCard> res;
		List<PoliticsCard> resCopy;
		City city;
		try {
			res = DTOPoliticsCard.decodeAll(this.dtoCards.getReply(), this.getModel().getCurrentPlayer().getPoliticsCards());
			//Create a res copy to substract cards to the player's hand
			resCopy = new ArrayList<>(res);
			city = this.dtoCity.getReply().decode(this.getModel().getBoard().getCities());
		} catch (ElementNotFoundException e1) {
			this.getModel().getLogger().log(e1);
			throw new CannotPerformActionException("Something went wrong with your choices");
		}
		if(this.getModel().getCurrentPlayer().getPlayerEmporiums().size() >= 10)
			throw new CannotPerformActionException("You can not build more than 10 emporiums");
		try{
			money = this.getModel().getBoard().getKingCouncil().goodHand(res);
			//Now res has no more matching cards
			//Rimangono solo le carte scartate
			resCopy.removeAll(res);
			money += this.getModel().getBoard().getKing().moveKing(city);
			this.getModel().getCurrentPlayer().getPocket().subPocketable(new Coin(money));
			assistants += city.getNumEmp();
			this.getModel().getCurrentPlayer().getPocket().subPocketable(new Assistant(assistants));
			this.getModel().getCurrentPlayer().buildEmporium(city);
			}
		catch(NotEnoughCoinException e)
		{
			this.getModel().getLogger().log(e);
			throw new CannotPerformActionException("You don't have enough money to perform this action");	
		}
		catch(NotEnoughAssistantsException e)
		{
			this.getModel().getLogger().log(e);
			this.getModel().getCurrentPlayer().getPocket().addPocketable(new Coin(money));
			throw new CannotPerformActionException("You don't have enough assistants to perform this action");
		}
		catch(OneEmporiumOnlyException e)
		{
			this.getModel().getLogger().log(e);
			this.getModel().getCurrentPlayer().getPocket().addPocketable(new Coin(money));
			this.getModel().getCurrentPlayer().getPocket().addPocketable(new Assistant(assistants));
			throw new CannotPerformActionException("You can not build two emporiums in the same city");
		}
		catch(NoGoodCardsException e)
		{
			this.getModel().getLogger().log(e);
			throw new CannotPerformActionException("The cards you selected are not good to influence that council");
		}
		this.getModel().useMainAction();
		this.getModel().getBoard().getKing().setKingCity(city);
		Iterator<PoliticsCard> i = resCopy.iterator();
		while(i.hasNext())
		{
			PoliticsCard p = i.next();
			this.getModel().getBoard().getPoliticsDeck().discardCard(p);	
			this.getModel().getCurrentPlayer().discardPolitics(p);
		}
		if(this.getModel().actionStackIsEmpty()) 
			return new ChooseActionTypeAction<>(this.getModel());
		return this.getModel().popAction();
	}

	/**
	 * Returns a String representation of the action.
	 */
	@Override
	public String toString()
	{
		return "Influence the Kings council and move the King to the city where you want to build paying 2 coins for every link used";
	}
	
	/**
	 * sets up all the right interactions with the needed parameters
	 * @param cards is the list of cards between which to choose 
	 * @param cities is the list of cities to decide where you want to build(aka where you want to move the king)
	 * @throws CannotSetupActionException when the parameters are not well formed (null)
	 */
	public void setup(List<PoliticsCard> cards,List<City> cities) throws CannotSetupActionException 
	{
		if(cards == null || cities == null) 
			throw new CannotSetupActionException("You should pass a non empty list of cards and cities");
		try{
		this.dtoCards = new MultipleChoiceInteraction<>("Choose the politics to use to influence the king council",DTOPoliticsCard.convertAll(cards),1,4);
		this.dtoCity = new SingleChoiceInteraction<>("Choose the city where you want to move the King:",DTOCity.convertAll(cities));
		}catch(IllegalArgumentException e){
			throw new CannotSetupActionException(e.getMessage(),e);
		}
		this.setInteraction(this.dtoCards);
		this.setInteraction(this.dtoCity);
	}
	/**
	 * default setup which passes the politics in the hand of the player and all the cities of the board
	 * @throws CannotSetupActionException
	 */
	@Override
	public void setup() throws CannotSetupActionException
	{
		if(!this.getInteractions().isEmpty()) 
			return;
		this.setup(this.getModel().getCurrentPlayer().getPoliticsCards(), this.getModel().getBoard().getCities());
	}
}
