package it.polimi.ingsw.cg25.model.trade;

import java.util.List;

import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.dashboard.cards.PoliticsCard;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOProduct;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOSellable;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.ProductGenerator;

/**
 * 
 * @author Giovanni
 *
 */
public class ProductPoliticsCard extends Product<PoliticsCard> {
	
	/**
	 * ProductPoliticsCard class constructor
	 * @param polCard the List of politics cards included in this product
	 * @param price the price of the product
	 * @param owner the owner of the product
	 */
	public ProductPoliticsCard(int price, PlayerCD4 owner, List<PoliticsCard> polCard, int barcode) {
		super(price, owner, polCard, barcode);
	}

	/**
	 * the player who will buy this product will also achieve
	 * the politics card included in the product itself
	 * @param target the player who buy the product
	 */
	@Override
	public void achieveProduct(PlayerCD4 target) {
		//Assegna al giocatore la carta politica contenuta nel prodotto
		target.getPoliticsCards().addAll(this.getContent());
	}

	@Override
	public DTOProduct<? extends DTOSellable> generate(ProductGenerator pg) {
		return pg.visit(this);
	}

}
