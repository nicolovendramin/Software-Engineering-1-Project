package it.polimi.ingsw.cg25.tradeactions;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import it.polimi.ingsw.cg25.ActionTest;
import it.polimi.ingsw.cg25.actions.BuyingAction;
import it.polimi.ingsw.cg25.actions.Interaction;
import it.polimi.ingsw.cg25.actions.MultipleChoiceInteraction;
import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.CannotSetupActionException;
import it.polimi.ingsw.cg25.exceptions.NotEnoughAssistantsException;
import it.polimi.ingsw.cg25.exceptions.NotEnoughCoinException;
import it.polimi.ingsw.cg25.model.Assistant;
import it.polimi.ingsw.cg25.model.Coin;
import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.dashboard.Party;
import it.polimi.ingsw.cg25.model.dashboard.bonus.AssistantBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonus;
import it.polimi.ingsw.cg25.model.dashboard.cards.PermitCard;
import it.polimi.ingsw.cg25.model.dashboard.cards.PoliticsCard;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOProduct;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOSellable;
import it.polimi.ingsw.cg25.model.trade.Market;
import it.polimi.ingsw.cg25.model.trade.Product;
import it.polimi.ingsw.cg25.model.trade.ProductBonus;
import it.polimi.ingsw.cg25.model.trade.ProductPermitCard;
import it.polimi.ingsw.cg25.model.trade.ProductPoliticsCard;
import it.polimi.ingsw.cg25.model.trade.Sellable;

public class buyingActionTest extends ActionTest{

	List<PoliticsCard> politics;
	List<PermitCard>  permits;
	
	@Before
	public void setUp() throws Exception {
		this.getModel().getMarket().openMarket();
		Market market = this.getModel().getMarket();
		List<Bonus> bonuses  = new ArrayList<>();
		bonuses.add(new AssistantBonus(4));
		politics = new ArrayList<>();
		permits = new ArrayList<>();
		PoliticsCard card = new PoliticsCard(new Party(new HSBColor(0,0,0,"special"),false));
		PoliticsCard card1 = new PoliticsCard(new Party(new HSBColor(0,0,0,"special1"),false));
		PoliticsCard card2 = new PoliticsCard(new Party(new HSBColor(0,0,0,"special2"),false));
		politics.add(card2);
		politics.add(card1);
		politics.add(card);
		permits.addAll(model.getBoard().getRegions().get(0).getFaceUpPermits());
		market.addProduct(new ProductBonus(10, player2,bonuses, model.getMarket().getNextProductTag()));
		market.addProduct(new ProductPermitCard(10, player3,permits, model.getMarket().getNextProductTag()));
		market.addProduct(new ProductPoliticsCard(10, player4, politics, model.getMarket().getNextProductTag()));
		market.addProduct(new ProductBonus(1000, player2,bonuses, model.getMarket().getNextProductTag()));
		//Here i will instantiate in the market 3 products,I will make them be bought from different players 
		//and I will check that the owner has the right situation after having bought the products
	}

	public void rob(PlayerCD4 player) {
		try {
			player.getPocket().subPocketable(new Assistant(player.getPocket().getAssistants().getSupply()));
		} catch (NotEnoughAssistantsException e) {
			e.printStackTrace();
		}
		try {
			player.getPocket().subPocketable(new Coin(player.getPocket().getCoins().getSupply()));
		} catch (NotEnoughCoinException e) {
			e.printStackTrace();
		}
		player.getPermitsToBeUsed().removeIf(c -> true);
		
	}
	
	public void present(PlayerCD4 player) {
			player.getPocket().addPocketable(new Assistant(100));
			player.getPocket().addPocketable(new Coin(100));
	}
	
	@Test
	public void testDoAction() throws CannotSetupActionException, CannotPerformActionException {
		List<PlayerCD4> players = model.getPlayers();
		for(PlayerCD4 p : players)
		{
			rob(p);
			p.setStatus(true);
		}
		present(this.getModel().getMarket().getCurrentPlayer());
		int ass = this.getModel().getMarket().getCurrentPlayer().getPocket().getAssistants().getSupply();
		BuyingAction action = new BuyingAction(model.getMarket());
		Product<? extends Sellable> prod = model.getMarket().getProducts().get(0);
		int precCoins = player2.getPocket().getCoins().getSupply();
		action.setup();
		action.getInteraction(0).registerReply("0");
		action.sendReply(action.getInteraction(0));
		action.doAction();
		assertFalse(model.getMarket().getProducts().get(0)==prod);
		assertFalse(model.getMarket().getProducts().contains(prod));
		assertEquals(model.getMarket().getCurrentPlayer().getPocket().getAssistants().getSupply(),ass+4);
		assertTrue(player2.getPocket().getCoins().getSupply()==precCoins+10);
		assertTrue(model.getMarket().getCurrentPlayer().getPocket().getCoins().getSupply()==90);
		precCoins = player3.getPocket().getCoins().getSupply();
		prod = model.getMarket().getProducts().get(0);
		action = new BuyingAction(model.getMarket());
		action.setup();
		for(PermitCard p : permits)
		{
			assertFalse(model.getMarket().getCurrentPlayer().getPermitsToBeUsed().contains(p));
		}
		action.getInteraction(0).registerReply("0");
		action.sendReply(action.getInteraction(0));
		action.doAction();
		assertFalse(model.getMarket().getProducts().get(0)==prod);
		assertFalse(model.getMarket().getProducts().contains(prod));
		assertEquals(player3.getPocket().getCoins().getSupply(),precCoins+prod.getPrice());
		assertTrue(model.getMarket().getCurrentPlayer().getPermitsToBeUsed().containsAll(permits));
		assertTrue(model.getMarket().getCurrentPlayer().getPocket().getCoins().getSupply()==80);
		precCoins = player4.getPocket().getCoins().getSupply();
		prod = model.getMarket().getProducts().get(0);
		action = new BuyingAction(model.getMarket());
		action.setup();
		for(PoliticsCard p : politics)
			assertFalse(model.getMarket().getCurrentPlayer().getPoliticsCards().contains(p));
		action.getInteraction(0).registerReply("0");
		action.sendReply(action.getInteraction(0));
		action.doAction();
		assertFalse(model.getMarket().getProducts().get(0)==prod);
		assertFalse(model.getMarket().getProducts().contains(prod));
		assertEquals(player4.getPocket().getCoins().getSupply(),precCoins+prod.getPrice());
		assertTrue(model.getMarket().getCurrentPlayer().getPoliticsCards().containsAll(politics));
		assertTrue(model.getMarket().getCurrentPlayer().getPocket().getCoins().getSupply()==70);
		action = new BuyingAction(model.getMarket());
		action.setup();
		List<Interaction> actoins = action.getInteractions();
		action.setup();
		assertTrue(actoins.equals(action.getInteractions()));
		action.getInteraction(0).registerReply("0");
		action.sendReply(action.getInteraction(0));
		try{
			action.doAction();
			fail("Should throw not enough coin exception");
			}catch(CannotPerformActionException e){
			
		}
	}

	@Test (expected = CannotSetupActionException.class)
	public void cannotSetupWithNoProductsToBuy() throws CannotSetupActionException{
		Market market = new Market(this.getModel(),proxy);
		BuyingAction action = new BuyingAction(market);
		action.setup();
	}
	
	@Test
	public void testSetupWithoutProducts() throws CannotSetupActionException {
		BuyingAction action = new BuyingAction(this.getModel().getMarket());
		action.setup();
		@SuppressWarnings("unchecked")
		MultipleChoiceInteraction<DTOProduct<? extends DTOSellable>> per = (MultipleChoiceInteraction<DTOProduct<? extends DTOSellable>>)action.getInteraction(0);
		DTOProduct.convertAll(this.getModel().getMarket().getProducts()).equals(per.getChoices());
	}

	@Test
	public void testBuyingAction() {
		BuyingAction action = new BuyingAction(model.getMarket());
		assertTrue(action!=null);
		assertTrue(action.getInteractions()!=null);
		assertTrue(action.getModel()!=null);
	}

	@Test
	public void testSetupListOfProductOfQextendsSellable() throws CannotSetupActionException {
		BuyingAction action = new BuyingAction(this.getModel().getMarket());
		action.setup(this.getModel().getMarket().getProducts());
		@SuppressWarnings("unchecked")
		MultipleChoiceInteraction<DTOProduct<? extends DTOSellable>> per = (MultipleChoiceInteraction<DTOProduct<? extends DTOSellable>>)action.getInteraction(0);
		DTOProduct.convertAll(this.getModel().getMarket().getProducts()).equals(per.getChoices());
	}
	
	@Test (expected = CannotSetupActionException.class)
	public void testSetupListOfProductOfQextendsSellableNull() throws CannotSetupActionException {
		BuyingAction action = new BuyingAction(model.getMarket());
		action.setup(null);
	}
	
	@Test (expected = CannotSetupActionException.class)
	public void testSetupListOfProductOfQextendsSellableEmpty() throws CannotSetupActionException {
		BuyingAction action = new BuyingAction(model.getMarket());
		action.setup(new ArrayList<Product<? extends Sellable>>());
	}

	@Test
	public void testToString() {
		assertTrue("Buy some of the products available on the market.".equals(new BuyingAction(this.getModel().getMarket()).toString()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testINitWithNull() {
		BuyingAction action = new BuyingAction(null);
		assertTrue(this.getModel().getMarket()==action.getModel());
	}

	
	@Test
	public void testGetModel() {
		BuyingAction action = new BuyingAction(this.getModel().getMarket());
		assertTrue(this.getModel().getMarket()==action.getModel());
	}

}
