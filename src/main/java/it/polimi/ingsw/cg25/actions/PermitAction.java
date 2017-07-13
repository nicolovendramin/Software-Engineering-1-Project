package it.polimi.ingsw.cg25.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.exceptions.NoCardsException;
import it.polimi.ingsw.cg25.exceptions.NoGoodCardsException;
import it.polimi.ingsw.cg25.exceptions.NotEnoughCoinException;
import it.polimi.ingsw.cg25.model.Coin;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.dashboard.cards.PermitCard;
import it.polimi.ingsw.cg25.model.dashboard.cards.PoliticsCard;
import it.polimi.ingsw.cg25.model.dashboard.topological.Region;
import it.polimi.ingsw.cg25.model.dto.DTOPermitCard;
import it.polimi.ingsw.cg25.model.dto.DTOPoliticsCard;
import it.polimi.ingsw.cg25.model.dto.DTORegion;

/**
 * 
 * @author nicolo
 *
 */
public class PermitAction extends MainAction {


	/**
	 * The interaction for the choice of the politics cards to use
	 */
	private MultipleChoiceInteraction<DTOPoliticsCard> dtoPolitics;
	/**
	 * The interaction for the choice of the region of the council
	 */
	private SingleChoiceInteraction<DTORegion> dtoRegions;
	/**
	 * The interaction for the choice of the permit to draw
	 */
	private SingleChoiceInteraction<DTOPermitCard> dtoPermit;
	/**
	 * The possible permits,stored to be used for the decode function of DTOPermitCard
	 */
	private List<PermitCard> candidates;
	
	
	public PermitAction(MatchCD4 model) {
		super(model);
	}

	/**
	 * Executes the action on the specified model
	 * 
	 * @throws CannotPerformActionException
	 *             when the Player has no good cards to satisfy the council or
	 *             money to complete the action
	 * @return the nex action to be notified to the model
	 */
	@Override
	public Action doAction() throws CannotPerformActionException {
		int money = 0;
		int cardIndex = 0;
		int regionIndex = 0;
		List<PoliticsCard> politics;
		List<PoliticsCard> polCopy;
		PermitCard permit;
		try {
			politics = DTOPoliticsCard.decodeAll(this.dtoPolitics.getReply(),getModel().getCurrentPlayer().getPoliticsCards());
			polCopy = new ArrayList<>(politics);
			permit = this.dtoPermit.getReply().decode(candidates);
		} catch (ElementNotFoundException e1) {
			this.getModel().getLogger().log(e1);
			throw new CannotPerformActionException("Something went wrong with your selection");
		}
		try {
			for (int i = 0; i < this.getModel().getBoard().getRegions().size(); i++)
				if (this.getModel().getBoard().getRegions().get(i).getFaceUpPermits().contains(permit)) {
					regionIndex = i;
					cardIndex = this.getModel().getBoard().getRegions().get(i).getFaceUpPermits()
							.indexOf(permit);
				}
			
			money = this.getModel().getBoard().getRegions().get(regionIndex).getCouncil()
					.goodHand(politics);
			//Rimangono solo le carte scartate
			polCopy.removeAll(politics);
			
			this.getModel().getCurrentPlayer().getPocket().subPocketable(new Coin(money));
			this.getModel().getBoard().getRegions().get(regionIndex).getPermit(cardIndex);
			this.getModel().getCurrentPlayer().getPermitsToBeUsed().add(permit);
			permit.takeAllBonuses(this.getModel().getCurrentPlayer());
		} catch (NoGoodCardsException e) {
			this.getModel().getLogger().log(e);
			throw new CannotPerformActionException("Choose other cards because this one don't match");
		} catch (NotEnoughCoinException e) {
			this.getModel().getLogger().log(e);
			throw new CannotPerformActionException("You don't have enough money to do this");
		} catch (NoCardsException e) {
			this.getModel().getLogger().log(e);
			this.getModel().getCurrentPlayer().getPocket().addPocketable(new Coin(money));
			throw new IllegalArgumentException("This region has no more permits to give");
		}
		//If everything ok, remove player's cards
		Iterator<PoliticsCard> i = polCopy.iterator();
		while(i.hasNext())
		{
			PoliticsCard p = i.next();
			this.getModel().getBoard().getPoliticsDeck().discardCard(p);	
			this.getModel().getCurrentPlayer().discardPolitics(p);
		}
		
		this.getModel().useMainAction();
		if (this.getModel().actionStackIsEmpty())
			return this.getModel().getStartingAction();
		return this.getModel().popAction();
	}

	/**
	 * Returns a String representation of the object
	 */
	@Override
	public String toString() {
		return "Influence a Council to draw one of the face-up permits of its Region";
	}

	/**
	 * initialize the correct interactions. This can override previous setups
	 * 
	 * @param politics
	 *            is the politics card between which the player must choose the
	 *            one to discard
	 * @param regions
	 *            is the set of all the regions
	 */
	public void setup(List<PoliticsCard> politics, List<PermitCard> permits,List<Region> regions) {
		this.getInteractions().clear();
		this.candidates = permits;
		this.dtoPermit = new SingleChoiceInteraction<>("Choose which permit you want to obtain", DTOPermitCard.convertAll(permits));
		this.dtoPolitics = new MultipleChoiceInteraction<>("Choose the politics to influence the council", DTOPoliticsCard.convertAll(politics), 1, 4);
		this.dtoRegions = new SingleChoiceInteraction<>("Choose from which region you want to receive your permit",DTORegion.convertAll(regions));
		this.setInteraction(dtoRegions);
		this.setInteraction(this.dtoPolitics);
		this.setInteraction(this.dtoPermit);
	}

	/**
	 * default implementation for setup. Can not override previous setups
	 */
	public void setup() {
		if (!this.getInteractions().isEmpty())
			return;
		List<PermitCard> permits = new ArrayList<>();
		List<Region> regionWithPermits = new ArrayList<>();
		for (Region r :this.getModel().getBoard().getRegions()) {
			permits.addAll(r.getFaceUpPermits());
			if(!r.getFaceUpPermits().isEmpty())
				regionWithPermits.add(r);
		}
		this.setup(this.getModel().getCurrentPlayer().getPoliticsCards(), permits, this.getModel().getBoard().getRegions());
	}
	
	@Override
	public Interaction next(){
		Interaction i = super.next();
		if(i==this.dtoPermit)
		{
			this.dtoPermit = new SingleChoiceInteraction<>(dtoPermit,c -> this.dtoRegions.getReply().getFaceUpPermits().contains(c));
			int index = this.getInteractions().indexOf(i);
			this.getInteractions().remove(i);
			this.getInteractions().add(index,this.dtoPermit);
			return dtoPermit;
		}
		return i;
	}
}
