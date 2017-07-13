package it.polimi.ingsw.cg25.model.trade;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOProduct;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOSellable;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.ProductGenerator;

/**
 * 
 * @author Giovanni
 *
 */
public abstract class Product<T extends Sellable> {

	/**
	 * The price of the product
	 */
	private final int price;
	/**
	 * The player who is the owner of the product and wants to sell it
	 */
	private final PlayerCD4 owner;
	/**
	 * The List of items (bonus or cards) to sell
	 */
	private final List<T> itemsToSell;
	/**
	 * The barcode of the product given by the market
	 */
	private final int barcode;

	/**
	 * Product abstract class constructor
	 * @param price the price of the object
	 * @param owner the owner of the object
	 * @param itemsToSell the List of items to sell
	 */
	public Product(int price, PlayerCD4 owner, List<T> itemsToSell, int barcode) {
		if(price < 0)
			throw new IllegalArgumentException("You can't create a product with a price less than 0 coins!");
		if(owner == null)
			throw new NullPointerException("You can't create a product without an owner!");
		if(itemsToSell == null)
			throw new NullPointerException("You can't create a product without a List of items!");
		if(itemsToSell.isEmpty())
			throw new IllegalArgumentException("You can't create an empty product!");
		
		this.price = price;
		this.owner = owner;
		this.barcode = barcode;
		this.itemsToSell = new ArrayList<>();
		this.itemsToSell.addAll(itemsToSell);
	}
	
	/**
	 * @return the price of the product
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @return the owner of the product
	 */
	public PlayerCD4 getOwner() {
		return owner;
	}
	
	/**
	 * @return the content of the product
	 */
	public List<T> getContent() {
		return itemsToSell;
	}
	
	/**
	 * @return the barcode of the product
	 */
	public int getBarcode() {
		return barcode;
	}
	
	/**
	 * The target player will obtain the selected product
	 * @param target the player who'll obtain the chosen product
	 */
	public abstract void achieveProduct(PlayerCD4 target);
	
	/**
	 * This method is overrided in concrete product classes and it is used to
	 * generate the corresponding DTO product by using the polymorphism of Java
	 * @param pg a ProductGenerator object
	 * @return the DTO product related to the concrete product
	 */
	public abstract DTOProduct<? extends DTOSellable> generate(ProductGenerator pg);
	
}
