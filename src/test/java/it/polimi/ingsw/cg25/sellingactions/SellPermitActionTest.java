package it.polimi.ingsw.cg25.sellingactions;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.ActionTest;
import it.polimi.ingsw.cg25.actions.MultipleChoiceInteraction;
import it.polimi.ingsw.cg25.actions.SellPermit;
import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.CannotSetupActionException;
import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.model.dashboard.cards.PermitCard;
import it.polimi.ingsw.cg25.model.dto.DTOPermitCard;
import it.polimi.ingsw.cg25.model.trade.Product;
import it.polimi.ingsw.cg25.model.trade.Sellable;

public class SellPermitActionTest extends ActionTest{

	@Before
	public void setUp() throws Exception {
		model.getMarket().openMarket();
	}

	@Test (expected = CannotPerformActionException.class)
	public void testDoActionWithoutReply() throws CannotPerformActionException, CannotSetupActionException {
		SellPermit action = new SellPermit(model.getMarket());
		model.getMarket().openMarket();
		action.setup();
		action.doAction();
	}
	
	@Test (expected = CannotSetupActionException.class)
	public void testFailingSetupCauseIdonthavePermits() throws CannotSetupActionException, CannotPerformActionException{
		player1.getPermitsToBeUsed().removeIf(c -> true);
		SellPermit action = new SellPermit(model.getMarket());
		action.setup();
	}
	
	@Test (expected = CannotPerformActionException.class)
	public void retrievingReplyWhichNotExists() throws CannotPerformActionException, CannotSetupActionException{
		SellPermit action = new SellPermit(model.getMarket());
		player1.getPermitsToBeUsed().addAll(model.getBoard().getRegions().get(0).getFaceUpPermits());
		action.setup();
		action.getInteraction(0).registerReply("0");
		action.getInteraction(1).registerReply("11");
		player1.getPermitsToBeUsed().removeIf(c -> true);
		action.doAction();
	}
	
	@Test 
	public void testDoAction() throws CannotSetupActionException, CannotPerformActionException{
		player1.getPermitsToBeUsed().removeIf(c -> true);
		model.getMarket().getCurrentPlayer().getPermitsToBeUsed().addAll(model.getBoard().getRegions().get(0).getFaceUpPermits());
		SellPermit action = new SellPermit(model.getMarket());
		action.setup();
		List<Product<? extends Sellable>> before = new ArrayList<>(model.getMarket().getProducts());
		assertTrue(model.getMarket().getCurrentPlayer().getPermitsToBeUsed().containsAll(model.getBoard().getRegions().get(0).getFaceUpPermits()));
		action.getInteraction(0).registerReply("0,1");
		action.getInteraction(1).registerReply("11");
		action.doAction();
		for(PermitCard p : model.getBoard().getRegions().get(0).getFaceUpPermits())
			assertFalse(model.getMarket().getCurrentPlayer().getPermitsToBeUsed().contains(p));
		List<Product<? extends Sellable>> after = new ArrayList<>(model.getMarket().getProducts());
		for(Product<? extends Sellable> p : after)
			if(before.contains(p))
				after.remove(p);
		assertTrue(after.size()==1);
		assertTrue(after.get(0).getPrice()==11);
		assertEquals(after.get(0).getContent().toString(),model.getBoard().getRegions().get(0).getFaceUpPermits().toString());
	}

	@Test (expected = IllegalArgumentException.class)
	public void testSellPermit() {
		new SellPermit(null);
	}

	@Test (expected = CannotSetupActionException.class)
	public void testSetupWithNull() throws CannotSetupActionException {
		SellPermit action = new SellPermit(model.getMarket());
		action.setup(null);
	}

	@Test
	public void testSetupListOfPermitCard() throws CannotSetupActionException, ElementNotFoundException {
		player1.getPermitsToBeUsed().addAll(model.getBoard().getRegions().get(0).getFaceUpPermits());
		SellPermit action = new SellPermit(model.getMarket());
		action.setup(player1.getPermitsToBeUsed());
		@SuppressWarnings("unchecked")
		MultipleChoiceInteraction<DTOPermitCard> per = (MultipleChoiceInteraction<DTOPermitCard>)(action.getInteraction(0));
		assertTrue(per.getChoices().containsAll(DTOPermitCard.convertAll(model.getBoard().getRegions().get(0).getFaceUpPermits())));
		action.setup();
		assertTrue(per.getChoices().containsAll(DTOPermitCard.convertAll(model.getBoard().getRegions().get(0).getFaceUpPermits())));
	}

	@Test
	public void testToString() {
		assertEquals(new SellPermit(model.getMarket()).toString(),"Sell some of your permits");
	}

}
