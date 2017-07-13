package it.polimi.ingsw.cg25.model.dto.dtoproduct;

import it.polimi.ingsw.cg25.model.dto.DTOPermitCard;
import it.polimi.ingsw.cg25.model.dto.DTOPlayerCD4;
import it.polimi.ingsw.cg25.model.trade.ProductPermitCard;

public class DTOProductPermit extends DTOProduct<DTOPermitCard> {

	/**
	 * Serial number for serial communication
	 */
	private static final long serialVersionUID = -6232071447730858371L;

	/**
	 * DTOProductPermit class constructor
	 * @param product the ProductPermitCard to be turned into a DTOProductPermit object
	 */
	public DTOProductPermit(ProductPermitCard product) {
		super(product.getPrice(), new DTOPlayerCD4(product.getOwner()), 
				DTOPermitCard.convertAll(product.getContent()), product.getBarcode());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Product Owner: " + this.getOwner().getName() + ", Price: " + this.getPrice() + ", Contents:\n");
		int index = 1;
		for(DTOPermitCard pc : this.getItemsToSell()) {
			sb.append("\t" + index + ") " + pc + "\n");
			index++;
		}
	
		return sb.toString();
	}

}
