package it.polimi.ingsw.cg25.model.dto.dtoproduct;

import it.polimi.ingsw.cg25.model.trade.ProductBonus;
import it.polimi.ingsw.cg25.model.trade.ProductPermitCard;
import it.polimi.ingsw.cg25.model.trade.ProductPoliticsCard;

/**
 * 
 * Product visitor interface is part of a visitor pattern together with ProductGenerator class. 
 * The purpouse of this interface is that of containing methods useful to
 * realize a visitor pattern (in this case related to products). Here the are 
 * all the necessary to create any kind of dto product starting from a normal product
 * by using the overloading feature provided in Java language.
 * 
 * @author Giovanni
 *
 */
public interface ProductVisitor {

	public DTOProductBonus visit(ProductBonus product);
	
	public DTOProductPermit visit(ProductPermitCard product);
	
	public DTOProductPoliticsCard visit(ProductPoliticsCard product);
	
}
