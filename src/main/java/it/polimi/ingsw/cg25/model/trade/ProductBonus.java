package it.polimi.ingsw.cg25.model.trade;

import java.util.List;

import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonus;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOProduct;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOSellable;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.ProductGenerator;

/**
 * 
 * @author Giovanni
 *
 */
public class ProductBonus extends Product<Bonus> {
	
	/**
	 * ProductBonus class constructor
	 * @param price the price of the product
	 * @param owner the owner of the product
	 * @param itemsToSell the List of items to sell
	 * @param barcode the barcode of the product
	 */
	public ProductBonus(int price, PlayerCD4 owner, List<Bonus> itemsToSell, int barcode){
		super(price, owner, itemsToSell, barcode);
	}

	@Override
	public void achieveProduct(PlayerCD4 target) {
		this.getContent().stream().forEach(b -> b.acquireBonus(target));
	}

	@Override
	public DTOProduct<? extends DTOSellable> generate(ProductGenerator pg) {
		return pg.visit(this);
	}
	
}
