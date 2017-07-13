package it.polimi.ingsw.cg25.model.dto.dtoproduct;

import it.polimi.ingsw.cg25.model.dto.DTOPlayerCD4;
import it.polimi.ingsw.cg25.model.dto.DTOPoliticsCard;
import it.polimi.ingsw.cg25.model.trade.ProductPoliticsCard;

/**
 * 
 * @author Giovanni
 *
 */
public class DTOProductPoliticsCard extends DTOProduct<DTOPoliticsCard> {

	/**
	 * Serial number for serial communication
	 */
	private static final long serialVersionUID = -4106322860854757219L;

	/**
	 * DTOProductPoliticsCard class constructor
	 * @param product the politics card to be turned into a DTOProductPoliticsCard object
	 */
	public DTOProductPoliticsCard(ProductPoliticsCard product) {
		super(product.getPrice(), new DTOPlayerCD4(product.getOwner()), 
				DTOPoliticsCard.convertAll(product.getContent()), product.getBarcode());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Product Owner: " + this.getOwner().getName() + ", Price: " + this.getPrice() + ", Contents:\n");
		int index = 1;
		for(DTOPoliticsCard pc : this.getItemsToSell()) {
			sb.append("\t" + index + ") " + pc + "\n");
			index++;
		}
	
		return sb.toString();
	}
	
}
