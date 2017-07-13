package it.polimi.ingsw.cg25.model.dto.dtoproduct;

import it.polimi.ingsw.cg25.model.trade.ProductBonus;
import it.polimi.ingsw.cg25.model.trade.ProductPermitCard;
import it.polimi.ingsw.cg25.model.trade.ProductPoliticsCard;

/**
 * 
 * This class, which implements ProductVisitor, overrides all the methods contained in that
 * interface in order to create a DTO product of the same type of the received one 
 * 
 * @author Giovanni
 *
 */
public class ProductGenerator implements ProductVisitor {

	@Override
	public DTOProductBonus visit(ProductBonus product) {
		return new DTOProductBonus(product);
	}

	@Override
	public DTOProductPermit visit(ProductPermitCard product) {
		return new DTOProductPermit(product);
	}

	@Override
	public DTOProductPoliticsCard visit(ProductPoliticsCard product) {
		return new DTOProductPoliticsCard(product);
	}

}
