package it.polimi.ingsw.cg25.model.trade;

import java.util.List;

import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.dashboard.cards.PermitCard;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOProductPermit;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.ProductGenerator;

/**
 * 
 * @author Giovanni
 *
 */
public class ProductPermitCard extends Product<PermitCard> {
	
	/**
	 * ProductPermitCard class constructor
	 * @param permitCard the List of permit cards included in this product
	 * @param price the price of the product
	 * @param owner the owner of the product
	 */
	public ProductPermitCard(int price, PlayerCD4 owner, List<PermitCard> permitCards, int barcode) {
		super(price, owner, permitCards, barcode);
	}

	/**
	 * The player who will buy this product will also achieve the
	 * permit card included in the product itself
	 * @param target the player who buy the product
	 */
	@Override
	public void achieveProduct(PlayerCD4 target) {
		//Assegna al giocatore la/le permit contenuta nel prodotto
		target.getPermitsToBeUsed().addAll(this.getContent());
	}

	@Override
	public DTOProductPermit generate(ProductGenerator pg) {
		return pg.visit(this);
	}

}
