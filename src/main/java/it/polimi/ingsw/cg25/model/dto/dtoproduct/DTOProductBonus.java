package it.polimi.ingsw.cg25.model.dto.dtoproduct;

import it.polimi.ingsw.cg25.model.dto.DTOPlayerCD4;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOBonus;
import it.polimi.ingsw.cg25.model.trade.ProductBonus;

public class DTOProductBonus extends DTOProduct<DTOBonus> {

	/**
	 * Serial number for serial communication
	 */
	private static final long serialVersionUID = -7087562493224941058L;	

	/**
	 * DTOProductBonus class constructor
	 * @param product the ProductBonus to be turned into a DTOProductBonus object
	 */
	public DTOProductBonus(ProductBonus product) {
		super(product.getPrice(), new DTOPlayerCD4(product.getOwner()), 
				DTOBonus.convertAll(product.getContent()), product.getBarcode());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Product Owner: " + this.getOwner().getName() + ", Price: " + this.getPrice() + ", Contents:\n");
		int index = 1;
		for(DTOBonus b : this.getItemsToSell()) {
			sb.append("\t" + index + ") " + b + "\n");
			index++;
		}
	
		return sb.toString();
	}

}
