package it.polimi.ingsw.cg25.sellingactions;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.ActionTest;
import it.polimi.ingsw.cg25.actions.MultipleChoiceInteraction;
import it.polimi.ingsw.cg25.actions.SellPolitics;
import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.CannotSetupActionException;
import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.exceptions.NoCardsException;
import it.polimi.ingsw.cg25.model.dashboard.cards.PoliticsCard;
import it.polimi.ingsw.cg25.model.dto.DTOPoliticsCard;
import it.polimi.ingsw.cg25.model.trade.Product;
import it.polimi.ingsw.cg25.model.trade.Sellable;

public class SellPoliticsTest extends ActionTest{

	@Before
	public void setUp() throws Exception {
		model.getMarket().openMarket();
	}

	@Test (expected = CannotPerformActionException.class)
	public void testDoActionWithoutReply() throws CannotPerformActionException, CannotSetupActionException {
		SellPolitics action = new SellPolitics(model.getMarket());
		model.getMarket().openMarket();
		action.setup();
		action.doAction();
	}
	
	@Test (expected = CannotSetupActionException.class)
	public void testFailingSetupCauseIdonthavePermits() throws CannotSetupActionException, CannotPerformActionException{
		player1.getPoliticsCards().removeIf(c -> true);
		SellPolitics action = new SellPolitics(model.getMarket());
		action.setup();
	}
	
	@Test (expected = CannotPerformActionException.class)
	public void retrievingReplyWhichNotExists() throws CannotPerformActionException, CannotSetupActionException, NoCardsException{
		SellPolitics action = new SellPolitics(model.getMarket());
		List<PoliticsCard> carte = new ArrayList<>();
		for(int i =0;i<5;i++){
			carte.add(model.getBoard().getPoliticsDeck().drawCard());
		}
		player1.getPoliticsCards().addAll(carte);
		action.setup();
		action.getInteraction(0).registerReply("0");
		action.getInteraction(1).registerReply("11");
		player1.getPoliticsCards().removeIf(c -> true);
		action.doAction();
	}
	
	@Test 
	public void testDoAction() throws CannotSetupActionException, CannotPerformActionException, NoCardsException{
		player1.getPoliticsCards().removeIf(c -> true);
		List<PoliticsCard> carte = new ArrayList<>();
		for(int i =0;i<2;i++){
			carte.add(model.getBoard().getPoliticsDeck().drawCard());
		}
		player1.getPoliticsCards().addAll(carte);
		SellPolitics action = new SellPolitics(model.getMarket());
		action.setup();
		List<Product<? extends Sellable>> before = new ArrayList<>(model.getMarket().getProducts());
		assertTrue(model.getMarket().getCurrentPlayer().getPoliticsCards().containsAll(carte));
		action.getInteraction(0).registerReply("0,1");
		action.getInteraction(1).registerReply("11");
		action.doAction();
		for(PoliticsCard p : carte)
			assertFalse(model.getMarket().getCurrentPlayer().getPoliticsCards().contains(p));
		List<Product<? extends Sellable>> after = new ArrayList<>(model.getMarket().getProducts());
		for(Product<? extends Sellable> p : after)
			if(before.contains(p))
				after.remove(p);
		assertTrue(after.size()==1);
		assertTrue(after.get(0).getPrice()==11);
		assertEquals(after.get(0).getContent().get(0).toString(),carte.get(0).toString());
	}

	@Test (expected = IllegalArgumentException.class)
	public void testSellPermit() {
		new SellPolitics(null);
	}

	@Test (expected = CannotSetupActionException.class)
	public void testSetupWithNull() throws CannotSetupActionException {
		SellPolitics action = new SellPolitics(model.getMarket());
		action.setup(null);
	}

	@Test
	public void testSetupListOfPermitCard() throws CannotSetupActionException, ElementNotFoundException, NoCardsException {
		List<PoliticsCard> carte = new ArrayList<>();
		for(int i =0;i<5;i++){
			carte.add(model.getBoard().getPoliticsDeck().drawCard());
		}
		player1.getPoliticsCards().addAll(carte);
		SellPolitics action = new SellPolitics(model.getMarket());
		action.setup(player1.getPoliticsCards());
		@SuppressWarnings("unchecked")
		MultipleChoiceInteraction<DTOPoliticsCard> per = (MultipleChoiceInteraction<DTOPoliticsCard>)(action.getInteraction(0));
		assertTrue(per.getChoices().containsAll(DTOPoliticsCard.convertAll(carte)));
		action.setup();
		assertTrue(per.getChoices().containsAll(DTOPoliticsCard.convertAll(carte)));
	}

	@Test
	public void testToString() {
		assertEquals(new SellPolitics(model.getMarket()).toString(),"Sell some of your politics card");
	}

}
