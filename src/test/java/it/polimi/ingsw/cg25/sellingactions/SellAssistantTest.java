package it.polimi.ingsw.cg25.sellingactions;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.actions.SellAssistant;
import it.polimi.ingsw.cg25.exceptions.CannotCreateGameException;
import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.CannotSetupActionException;
import it.polimi.ingsw.cg25.exceptions.NotEnoughAssistantsException;
import it.polimi.ingsw.cg25.model.Assistant;
import it.polimi.ingsw.cg25.model.Coin;
import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.NobilityRank;
import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.PocketCD4;
import it.polimi.ingsw.cg25.model.VictoryPoint;
import it.polimi.ingsw.cg25.model.trade.Market;
import it.polimi.ingsw.cg25.model.trade.ProductBonus;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

public class SellAssistantTest {

	private MatchCD4 model;
	private ModelProxy proxy;
	private Market market;
	private BoardFactory factory;
	
	private PlayerCD4 player;
	private PocketCD4 pocket;
	
	@Before
	public void setUp() throws FileNotFoundException, CannotCreateGameException {
		factory = new BoardFactory(new FileReader("src/test/resources/nobilityCellsFULL.txt"),
				new FileReader("src/test/resources/politicsFULL.txt"), 
				new FileReader("src/test/resources/citiesFULL.txt"),
				new FileReader("src/test/resources/graphFULL.txt"), 
				new FileReader("src/test/resources/kingFULL.txt"),
				new FileReader("src/test/resources/regionsFULL.txt"));
		this.proxy = new ModelProxy();
		this.model = new MatchCD4(factory.getBoard(), this.proxy,true,10);
		//Init pocket and player
		pocket = new PocketCD4(new Coin(0), new Assistant(5), new NobilityRank(0), new VictoryPoint(0));
		player = new PlayerCD4(1, "Gio", HSBColor.getNDifferent(1).get(0), model, pocket);
		model.addPlayer(player);
		player.setStatus(true);
		market = model.getMarket();
		market.openMarket();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCantCreateSellAssistantWithNullMarket() {
		new SellAssistant(null);
	}
	
	@Test(expected=CannotSetupActionException.class)
	public void settingUpActionWithZeroAssistantInMyPocket() throws CannotSetupActionException {
		SellAssistant action = new SellAssistant(market);
		action.setup(-12);
	}
	
	
	@Test(expected = CannotPerformActionException.class)
	public void sellingToManyAssistants() throws CannotSetupActionException, CannotPerformActionException, NotEnoughAssistantsException{
		SellAssistant action = new SellAssistant(market);
		//Sell all player's assistants
		market.getCurrentPlayer().getPocket().addPocketable(new Assistant(12313));
		action.setup();
		market.getCurrentPlayer().getPocket().subPocketable(new Assistant(12313));
		action.getInteraction(0).registerReply("1234");
		action.getInteraction(1).registerReply("2");
		action.sendReply(action.getInteraction(0));
		action.doAction();
	}
	
	@Test
	public void testGetModel() {
		SellAssistant action = new SellAssistant(market);
		assertTrue(action.getModel() == market);
	}
	
	
	@Test
	public void testDoAction() throws CannotSetupActionException, CannotPerformActionException {
		SellAssistant action = new SellAssistant(market);
		//Sell all player's assistants
		action.setup();
		action.getInteraction(0).registerReply("2");
		action.getInteraction(1).registerReply("2");
		action.sendReply(action.getInteraction(0));
		action.doAction();
		//Check the player now has 3 assistants
		assertEquals(3, player.getPocket().getAssistants().getSupply());
		assertEquals(1, market.getProducts().size());
		assertEquals(ProductBonus.class, market.getProducts().get(0).getClass());
	}
	
	@After
	public void teardown() {
		model.getLogger().close();
	}
	
}
