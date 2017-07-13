package it.polimi.ingsw.cg25.model.dto.dtobonus;

import it.polimi.ingsw.cg25.model.dashboard.bonus.AssistantBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.CityBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.CoinBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.DrawPoliticsCardBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.MainActionBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.NobilityPointBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.PermitCardBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.ReusePermitBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.VictoryPointBonus;

/**
 * 
 * This class, which implements BonusVisitor, overrides all the methods contained in that
 * interface in order to create a DTO bonus of the same type of the received one 
 * 
 * @author Giovanni
 *
 */
public class BonusGenerator implements BonusVisitor {

	@Override
	public DTOAssistantBonus visit(AssistantBonus bonus) {
		return new DTOAssistantBonus(bonus);
	}

	@Override
	public DTOCityBonus visit(CityBonus bonus) {
		return new DTOCityBonus(bonus);
	}

	@Override
	public DTOCoinBonus visit(CoinBonus bonus) {
		return new DTOCoinBonus(bonus);
	}

	@Override
	public DTODrawPoliticsCardBonus visit(DrawPoliticsCardBonus bonus) {
		return new DTODrawPoliticsCardBonus(bonus);
	}

	@Override
	public DTOMainActionBonus visit(MainActionBonus bonus) {
		return new DTOMainActionBonus(bonus);
	}

	@Override
	public DTONobilityPointBonus visit(NobilityPointBonus bonus) {
		return new DTONobilityPointBonus(bonus);
	}

	@Override
	public DTOPermitCardBonus visit(PermitCardBonus bonus) {
		return new DTOPermitCardBonus(bonus);
	}

	@Override
	public DTOReusePermitBonus visit(ReusePermitBonus bonus) {
		return new DTOReusePermitBonus(bonus);
	}

	@Override
	public DTOVictoryPointBonus visit(VictoryPointBonus bonus) {
		return new DTOVictoryPointBonus(bonus);
	}

}
