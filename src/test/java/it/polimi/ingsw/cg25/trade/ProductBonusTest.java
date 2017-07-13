package it.polimi.ingsw.cg25.trade;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.exceptions.CannotCreateGameException;
import it.polimi.ingsw.cg25.model.Assistant;
import it.polimi.ingsw.cg25.model.Coin;
import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.NobilityRank;
import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.PocketCD4;
import it.polimi.ingsw.cg25.model.VictoryPoint;
import it.polimi.ingsw.cg25.model.dashboard.bonus.AssistantBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonus;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOProduct;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOProductBonus;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOSellable;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.ProductGenerator;
import it.polimi.ingsw.cg25.model.trade.ProductBonus;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

/**
 * 
 * @author Giovanni
 *
 */
public class ProductBonusTest {

	private MatchCD4 model;
	private ModelProxy proxy;
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
		//Init pocket
		pocket = new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(0), new VictoryPoint(0));
		player = new PlayerCD4(1, "Gio", HSBColor.getNDifferent(1).get(0), model, pocket);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void cantCreateAProductBonusWithAnIllegalPrice() {
		List<Bonus> itemsToSell = new ArrayList<>();
		itemsToSell.add(new AssistantBonus(10));
		//This triggers the exception
		new ProductBonus(-1, player, itemsToSell, 1);
	}
	
	@Test(expected=NullPointerException.class)
	public void cantCreateAProductBonusWithANullOwner() {
		List<Bonus> itemsToSell = new ArrayList<>();
		itemsToSell.add(new AssistantBonus(10));
		//This triggers the exception
		new ProductBonus(10, null, itemsToSell, 1);
	}

	@Test(expected=NullPointerException.class)
	public void cantCreateAProductBonusWithANullItemsToSellList() {
		//This triggers the exception
		new ProductBonus(10, player, null, 1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void cantCreateAProductBonusWithAnEmptyItemsToSellList() {
		//This triggers the exception
		new ProductBonus(10, player, new ArrayList<>(), 1);
	}
	
	@Test
	public void testGetOwner() {
		List<Bonus> itemsToSell = new ArrayList<>();
		itemsToSell.add(new AssistantBonus(10));
		//Test same reference
		assertTrue(player == new ProductBonus(10, player, itemsToSell, 1).getOwner());
	}
	
	@Test
	public void testGetPrice() {
		List<Bonus> itemsToSell = new ArrayList<>();
		itemsToSell.add(new AssistantBonus(10));
		//Test same price
		assertEquals(10, new ProductBonus(10, player, itemsToSell, 1).getPrice());
	}
	
	@Test
	public void testGetContent() {
		List<Bonus> itemsToSell = new ArrayList<>();
		itemsToSell.add(new AssistantBonus(10));
		//Test same contents
		assertEquals(1, new ProductBonus(10, player, itemsToSell, 1).getContent().size());
		AssistantBonus ab = (AssistantBonus)new ProductBonus(10, player, itemsToSell, 1).getContent().get(0);
		assertEquals(10, ab.getNumberOfAssistants());
	}
	
	@Test
	public void testGetBarcode() {
		List<Bonus> itemsToSell = new ArrayList<>();
		itemsToSell.add(new AssistantBonus(10));
		//Test same barcode
		assertEquals(1, new ProductBonus(10, player, itemsToSell, 1).getBarcode());
	}
	
	@Test
	public void testAchieveProduct() {
		List<Bonus> itemsToSell = new ArrayList<>();
		itemsToSell.add(new AssistantBonus(10));
		new ProductBonus(1, player, itemsToSell, 1).achieveProduct(player);
		
		assertEquals(10, player.getPocket().getAssistants().getSupply());
	}
	
	@Test
	public void testGenerate() {
		List<Bonus> itemsToSell = new ArrayList<>();
		itemsToSell.add(new AssistantBonus(10));
		DTOProduct<? extends DTOSellable> pb = new ProductBonus(1, player, itemsToSell, 1).generate(new ProductGenerator());
		
		assertEquals(DTOProductBonus.class, pb.getClass());
	}
	
	@After
	public void teardown() {
		model.getLogger().close();
	}
	
}
