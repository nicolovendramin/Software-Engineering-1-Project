package it.polimi.ingsw.cg25.trade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.exceptions.CannotCreateGameException;
import it.polimi.ingsw.cg25.exceptions.NoCardsException;
import it.polimi.ingsw.cg25.model.Assistant;
import it.polimi.ingsw.cg25.model.Coin;
import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.NobilityRank;
import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.PocketCD4;
import it.polimi.ingsw.cg25.model.VictoryPoint;
import it.polimi.ingsw.cg25.model.dashboard.cards.PermitCard;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOProduct;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOProductPermit;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOSellable;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.ProductGenerator;
import it.polimi.ingsw.cg25.model.trade.ProductPermitCard;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

/**
 * 
 * @author Giovanni
 *
 */
public class ProductPermitTest {

	private MatchCD4 model;
	private ModelProxy proxy;
	private BoardFactory factory;
	
	private PlayerCD4 player;
	private PocketCD4 pocket;
	
	private PermitCard testCard;
	
	@Before
	public void setUp() throws FileNotFoundException, CannotCreateGameException, NoCardsException {
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
		
		testCard = model.getBoard().getRegions().get(0).getPermit(0);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void cantCreateAProductPermitWithAnIllegalPrice() {
		List<PermitCard> itemsToSell = new ArrayList<>();
		itemsToSell.add(testCard);
		//This triggers the exception
		new ProductPermitCard(-2, player, itemsToSell, 1);
	}
	
	@Test(expected=NullPointerException.class)
	public void cantCreateAProductPermitWithANullOwner() {
		List<PermitCard> itemsToSell = new ArrayList<>();
		itemsToSell.add(testCard);
		//This triggers the exception
		new ProductPermitCard(1, null, itemsToSell, 1);
	}

	@Test(expected=NullPointerException.class)
	public void cantCreateAProductPermitWithANullItemsToSellList() {
		//This triggers the exception
		new ProductPermitCard(10, player, null, 1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void cantCreateAProductPermitWithAnEmptyItemsToSellList() {
		//This triggers the exception
		new ProductPermitCard(10, player, new ArrayList<>(), 1);
	}
	
	@Test
	public void testGetOwner() {
		List<PermitCard> itemsToSell = new ArrayList<>();
		itemsToSell.add(testCard);
		//Test same reference
		assertTrue(player == new ProductPermitCard(10, player, itemsToSell, 1).getOwner());
	}
	
	@Test
	public void testGetPrice() {
		List<PermitCard> itemsToSell = new ArrayList<>();
		itemsToSell.add(testCard);
		//Test same price
		assertEquals(10, new ProductPermitCard(10, player, itemsToSell, 1).getPrice());
	}
	
	@Test
	public void testGetContent() {
		List<PermitCard> itemsToSell = new ArrayList<>();
		itemsToSell.add(testCard);
		//Test same contents
		assertEquals(1, new ProductPermitCard(10, player, itemsToSell, 1).getContent().size());
		PermitCard pc = new ProductPermitCard(10, player, itemsToSell, 1).getContent().get(0);
		//Test same reference
		assertTrue(itemsToSell.get(0) == pc);
	}
	
	@Test
	public void testGetBarcode() {
		List<PermitCard> itemsToSell = new ArrayList<>();
		itemsToSell.add(testCard);
		//Test same barcode
		assertEquals(1, new ProductPermitCard(10, player, itemsToSell, 1).getBarcode());
	}
	
	@Test
	public void testAchieveProduct() {
		List<PermitCard> itemsToSell = new ArrayList<>();
		itemsToSell.add(testCard);
		new ProductPermitCard(1, player, itemsToSell, 1).achieveProduct(player);
		
		assertEquals(1, player.getPermitsToBeUsed().size());
		assertEquals(itemsToSell.get(0), player.getPermitsToBeUsed().get(0));
	}
	
	@Test
	public void testGenerate() {
		List<PermitCard> itemsToSell = new ArrayList<>();
		itemsToSell.add(testCard);
		DTOProduct<? extends DTOSellable> ppc = new ProductPermitCard(1, player, itemsToSell, 1).generate(new ProductGenerator());
		
		assertEquals(DTOProductPermit.class, ppc.getClass());
	}
	
	@After
	public void teardown() {
		model.getLogger().close();
	}
}
