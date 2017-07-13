package it.polimi.ingsw.cg25.dto.dtoproduct;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.exceptions.CannotCreateGameException;
import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.exceptions.NoCardsException;
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
import it.polimi.ingsw.cg25.model.dashboard.cards.PermitCard;
import it.polimi.ingsw.cg25.model.dashboard.cards.PoliticsCard;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOProduct;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOProductBonus;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOProductPermit;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOProductPoliticsCard;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOSellable;
import it.polimi.ingsw.cg25.model.trade.Product;
import it.polimi.ingsw.cg25.model.trade.ProductBonus;
import it.polimi.ingsw.cg25.model.trade.ProductPermitCard;
import it.polimi.ingsw.cg25.model.trade.ProductPoliticsCard;
import it.polimi.ingsw.cg25.model.trade.Sellable;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

public class DTOProductTest {

	private MatchCD4 model;
	private ModelProxy proxy;
	private BoardFactory factory;
	
	private PlayerCD4 player;
	private PocketCD4 pocket;
	
	private List<Product<? extends Sellable>> products;
	
	@Before
	public void setUp() throws FileNotFoundException, CannotCreateGameException, NoCardsException {
		factory = new BoardFactory(new FileReader("src/test/resources/nobilityCellsFULL.txt"),
				new FileReader("src/test/resources/politicsFULL.txt"), 
				new FileReader("src/test/resources/citiesFULL.txt"),
				new FileReader("src/test/resources/graphFULL.txt"), 
				new FileReader("src/test/resources/kingFULL.txt"),
				new FileReader("src/test/resources/regionsFULL.txt"));
		this.proxy = new ModelProxy();
		this.model = new MatchCD4(factory.getBoard(), this.proxy,false,10);
		//Init pocket and player
		pocket = new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(0), new VictoryPoint(0));
		player = new PlayerCD4(1, "Gio", HSBColor.getNDifferent(1).get(0), model, pocket);
		
		List<Bonus> bToSell = new ArrayList<>();
		bToSell.add(new AssistantBonus(2));
		bToSell.add(new AssistantBonus(5));
		
		List<PoliticsCard> pscToSell = new ArrayList<>();
		pscToSell.add(model.getBoard().getPoliticsDeck().drawCard());
		pscToSell.add(model.getBoard().getPoliticsDeck().drawCard());
		
		List<PermitCard> pcToSell = new ArrayList<>();
		pcToSell.add(model.getBoard().getRegions().get(0).getPermit(0));
		pcToSell.add(model.getBoard().getRegions().get(0).getPermit(1));
		
		products = new ArrayList<>();
		products.add(new ProductBonus(1, player, bToSell, 0));
		products.add(new ProductPoliticsCard(2, player, pscToSell, 1));
		products.add(new ProductPermitCard(5, player, pcToSell, 2));
	}
	
	@Test
	public void testGetPrice() {
		List<DTOProduct<? extends DTOSellable>> converted = DTOProduct.convertAll(products);
		assertEquals(1, converted.get(0).getPrice());
		assertEquals(2, converted.get(1).getPrice());
		assertEquals(5, converted.get(2).getPrice());
	}
	
	@Test
	public void testGetOwner() {
		List<DTOProduct<? extends DTOSellable>> converted = DTOProduct.convertAll(products);
		converted.forEach(p -> assertTrue(p.getOwner().equals(player)));
	}
	
	@Test
	public void testGetItemsToSell() {
		List<DTOProduct<? extends DTOSellable>> converted = DTOProduct.convertAll(products);
		//Check the DTO items to sell are the corresponding content of the real product
		assertTrue(converted.get(0).getItemsToSell().equals(products.get(0).getContent()));
		assertTrue(converted.get(1).getItemsToSell().equals(products.get(1).getContent()));
		assertTrue(converted.get(2).getItemsToSell().equals(products.get(2).getContent()));
	}
	
	@Test
	public void testGetBarcode() {
		List<DTOProduct<? extends DTOSellable>> converted = DTOProduct.convertAll(products);
		for(int i = 0; i < converted.size(); i++) {
			assertEquals(i, converted.get(i).getBarcode());
		}
	}
	
	@Test
	public void testConvert() {
		DTOProduct<? extends DTOSellable> dtoPb = DTOProduct.convert(products.get(0));
		assertEquals(DTOProductBonus.class, dtoPb.getClass());
		assertTrue(dtoPb.equals(products.get(0)));
		
		DTOProduct<? extends DTOSellable> dtoPsc = DTOProduct.convert(products.get(1));
		assertEquals(DTOProductPoliticsCard.class, dtoPsc.getClass());
		assertTrue(dtoPsc.equals(products.get(1)));
		
		DTOProduct<? extends DTOSellable> dtoPc = DTOProduct.convert(products.get(2));
		assertEquals(DTOProductPermit.class, dtoPc.getClass());
		assertTrue(dtoPc.equals(products.get(2)));
	}
	
	@Test
	public void testConvertAll() {
		List<DTOProduct<? extends DTOSellable>> converted = DTOProduct.convertAll(products);
		assertEquals(3, converted.size());
		
		for(int i = 0; i < converted.size(); i++) {
			assertTrue(products.get(i).getPrice() == converted.get(i).getPrice() && 
					converted.get(i).getOwner().equals(products.get(i).getOwner()) &&
					converted.get(i).getItemsToSell().equals(products.get(i).getContent()));
		}
	}
	
	@Test
	public void testHashCode() {
		List<DTOProduct<? extends DTOSellable>> converted = DTOProduct.convertAll(products);
		//Same products
		assertTrue(converted.get(0).hashCode() == converted.get(0).hashCode());
		//Different products
		assertFalse(converted.get(1).hashCode() == converted.get(0).hashCode());
		assertFalse(converted.get(2).hashCode() == converted.get(1).hashCode());
	}
	
	@Test
	public void testDecode() throws ElementNotFoundException {
		DTOProduct<? extends DTOSellable> converted = DTOProduct.convert(products.get(1));
		//Decode the dto product
		Product<? extends Sellable> decoded = converted.decode(products);
		//Check attributes
		assertEquals(converted.getPrice(), decoded.getPrice());
		assertEquals("Gio", decoded.getOwner().getName());
		assertTrue(converted.getItemsToSell().equals(decoded.getContent()));
		assertEquals(converted.getBarcode(), decoded.getBarcode());
	}
	
	@Test(expected=ElementNotFoundException.class)
	public void testDecodeFailElementNotFoundException() throws ElementNotFoundException {
		//Convert product 1
		DTOProduct<? extends DTOSellable> converted = DTOProduct.convert(products.get(1));
		//Delete product 1 from "products" List
		products.remove(1);
		//Try to decode "converted". This triggers the exception
		converted.decode(products);
	}
	
	@Test
	public void testDecodeAll() throws ElementNotFoundException {
		//List of converted products
		List<DTOProduct<? extends DTOSellable>> converted = DTOProduct.convertAll(products);
		List<Product<? extends Sellable>> decoded = DTOProduct.decodeAll(converted, products);
		
		assertEquals(converted.size(), decoded.size());
		for(int i = 0; i < converted.size(); i++)
			assertTrue(converted.get(i).equals(decoded.get(i)));
	}
	
	@Test
	public void testEquals() {
		List<DTOProduct<? extends DTOSellable>> converted = DTOProduct.convertAll(products);
		DTOProduct<? extends DTOSellable> convTemp = DTOProduct.convert(products.get(2));
		//Null
		assertFalse(converted.get(0).equals(null));
		//Other type
		assertFalse(converted.get(1).equals(new Assistant(10)));
		//Two dto products
		assertTrue(converted.get(2).equals(convTemp));
		assertFalse(converted.get(1).equals(converted.get(2)));
		assertFalse(converted.get(0).equals(converted.get(2)));
		//DTOProduct and Product
		for(int i = 0; i < converted.size(); i++)
			assertTrue(converted.get(i).equals(products.get(i)));
	}
	
	@Test
	public void testToString() {
		List<DTOProduct<? extends DTOSellable>> converted = DTOProduct.convertAll(products);
		//Check the string returned is not empty
		assertTrue(converted.get(0).toString().length() > 0);
		assertTrue(converted.get(1).toString().length() > 0);
		assertTrue(converted.get(2).toString().length() > 0);
	}
	
}
