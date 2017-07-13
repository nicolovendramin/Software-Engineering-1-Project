package it.polimi.ingsw.cg25.model.dto.dtoproduct;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.model.dto.DTO;
import it.polimi.ingsw.cg25.model.dto.DTOPlayerCD4;
import it.polimi.ingsw.cg25.model.trade.Product;
import it.polimi.ingsw.cg25.model.trade.Sellable;

/**
 * 
 * @author Giovanni
 *
 * @param <T> specifies the type of objects included in the product
 * 
 */
public abstract class DTOProduct<T extends DTOSellable> implements DTO {

	/**
	 * Serial number for serial communication
	 */
	private static final long serialVersionUID = 8168423406926931295L;
	/**
	 * The price of the DTO product
	 */
	private final int price;
	/**
	 * The DTO player who is the owner of the product and wants to sell it
	 */
	private final DTOPlayerCD4 owner;
	/**
	 * The List of items to be sold included in the DTO product
	 */
	private final List<T> itemsToSell;
	/**
	 * The barcode of the DTO product
	 */
	private final int barcode;
	/**
	 * A ProductGenerator object which realizes a visitor pattern
	 */
	private static final ProductGenerator pg = new ProductGenerator();
	
	/**
	 * DTOProduct class constructor
	 * @param price the price of the DTO product
	 * @param owner the owner of the DTO product
	 * @param itemsToSell the List of DTO products to sell
	 * @param barcode the barcode of the DTO product
	 */
	public DTOProduct(int price, DTOPlayerCD4 owner, List<T> itemsToSell, int barcode) {
		this.price = price;
		this.owner = owner;
		this.itemsToSell = itemsToSell;
		this.barcode = barcode;
	}
	
	/**
	 * This method is used to convert a single Product into a
	 * DTO product which implements DTOSellable interface
	 * @param toConvert the product to convert
	 * @return the DTO converted product
	 */
	public static DTOProduct<? extends DTOSellable> convert(Product<? extends Sellable> toConvert) {
		DTOProduct<? extends DTOSellable> converted = toConvert.generate(pg);
		return converted;
	}
	
	/**
	 * This method is used to convert a List of Products into a List of
	 * DTO products which implement DTOSellable interface
	 * @param toConvert the List of products to convert
	 * @return a List of converted DTO products
	 */
	public static List<DTOProduct<? extends DTOSellable>> convertAll(List<Product<? extends Sellable>> toConvert) {
		List<DTOProduct<? extends DTOSellable>> converted = new ArrayList<>();
		toConvert.forEach(p -> converted.add(DTOProduct.convert(p)));
		
		return converted;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + barcode;
		result = prime * result + itemsToSell.hashCode();
		result = prime * result + owner.hashCode();
		result = prime * result + price;
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		//Se obj è un DTOProduct...
		if(obj instanceof DTOProduct) {
			DTOProduct converted = (DTOProduct)obj;
			return this.barcode == converted.getBarcode();
		}
		if(obj instanceof Product) {
			Product converted = (Product)obj;
			return this.barcode == converted.getBarcode();
		}
		return false;
	}

	/**
	 * The method allows to find a non-DTO product in a List of products
	 * which is equal to this DTOProduct object
	 * @param candidates a List of Product type objects
	 * @return a Product type object of the previous List
	 * @throws ElementNotFoundException if the list of candidates does not contain
	 * a product that can be mapped into this DTO Product
	 */
	public Product<? extends Sellable> decode(List<Product<? extends Sellable>> candidates) throws ElementNotFoundException {
		for(Product<? extends Sellable> p : candidates)
			if(this.equals(p))
				return p;
		throw new ElementNotFoundException();
	}
	
	/**
	 * It compares a List of DTOProduct objects to a List of Product objects and
	 * returns a List of decoded Product objects (see decode method)
	 * @param toDecode the List of DTOProduct objects to decode
	 * @param candidates the List of Product objects to compare
	 * @return a List of decoded products
	 * @throws ElementNotFoundException if a DTOProduct object does not appear
	 * in the List of condidates
	 */
	public static List<Product<? extends Sellable>> decodeAll(List<DTOProduct<? extends DTOSellable>> toDecode, 
			List<Product<? extends Sellable>> candidates) throws ElementNotFoundException {
		
		List<DTOProduct<? extends DTOSellable>> toDecodeCopy = new ArrayList<>(toDecode);
		List<Product<? extends Sellable>> decoded = new ArrayList<>();
		List<Product<? extends Sellable>> candidateCopy = new ArrayList<>(candidates);
		for(DTOProduct<? extends DTOSellable> dc : toDecodeCopy){
			Product<? extends Sellable> temp = dc.decode(candidateCopy);
			decoded.add(temp);
			candidateCopy.remove(temp);
			}
		return decoded;
	}
	
	/**
	 * @return the price of the DTO product
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @return the owner of the DTO product
	 */
	public DTOPlayerCD4 getOwner() {
		return owner;
	}

	/**
	 * @return the List of items included in the DTO product
	 */
	public List<T> getItemsToSell() {
		return itemsToSell;
	}

	/**
	 * @return the barcode of the DTO product
	 */
	public int getBarcode() {
		return barcode;
	}

}